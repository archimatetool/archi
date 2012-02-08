/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.model.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Representation of the ArchiMate Relationships Matrix
 * 
 * @author Phillip Beauvoir
 */
public class RelationshipsMatrix {
    
    /**
     * Singleton instance
     */
    public static RelationshipsMatrix INSTANCE = new RelationshipsMatrix();
    
    /**
     * The Bundle ID
     */
    static final String BUNDLE_ID = "uk.ac.bolton.archimate.model"; //$NON-NLS-1$

    /**
     * The Key letters XML file
     */
    static final String RELATIONSHIPS_KEYS_FILE = "model/relationships-keys.xml"; //$NON-NLS-1$

    /**
     * The Relationships 1.0 XML file
     */
    static final String RELATIONSHIPS_1_0_FILE = "model/relationships-2.0.xml"; //$NON-NLS-1$
    
    /*
     * XML element and attribute names
     */
    static final String XML_ELEMENT_ROOT = "relationships"; //$NON-NLS-1$
    static final String XML_ELEMENT_KEYS = "keys"; //$NON-NLS-1$
    static final String XML_ELEMENT_KEY = "key"; //$NON-NLS-1$
    static final String XML_ELEMENT_ELEMENTS = "elements"; //$NON-NLS-1$
    static final String XML_ELEMENT_SOURCE = "source"; //$NON-NLS-1$
    static final String XML_ELEMENT_TARGET = "target"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_CHAR = "char"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_RELATIONSHIP = "relationship"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_RELATIONS = "relations"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_ELEMENT = "element"; //$NON-NLS-1$

    public static class TargetMatrix {
        EClass targetClass;
        List<EClass> relationships = new ArrayList<EClass>();
        
        public EClass getTargetClass() {
            return targetClass;
        }
        
        public List<EClass> getRelationships() {
            return relationships;
        }
    }
    
    /**
     * Mapping of source elements to target elements and possible relations
     */
    private Map<EClass, List<TargetMatrix>> matrixMap = new HashMap<EClass, List<TargetMatrix>>();
    
    /**
     * Mapping of key letters to relationships
     */
    private Map<Character, EClass> relationsKeyMap = new LinkedHashMap<Character, EClass>();
        
    /**
     * Mapping of relationships to key letters
     */
    private Map<EClass, Character> relationsValueMap = new LinkedHashMap<EClass, Character>();

    private RelationshipsMatrix() {
        // Load Key letters file
        loadKeyLetters();
        
        // URL to the Relationships 1.0 XML file
        URL url = Platform.getBundle(BUNDLE_ID).getResource(RELATIONSHIPS_1_0_FILE);
        loadRelationships(url);
    }
    
    public Map<EClass, List<TargetMatrix>> getRelationshipsMatrix() {
        return matrixMap;
    }
    
    public Map<EClass, Character> getRelationshipsValueMap() {
        return relationsValueMap;
    }

    boolean isValidRelationshipStart(IArchimateElement sourceElement, EClass relationshipType) {
        EClass sourceType = getSuperEClass(sourceElement.eClass());
        
        List<TargetMatrix> listMatrix = matrixMap.get(sourceType);
        if(listMatrix != null) {
            for(TargetMatrix targetMatrix : listMatrix) {
                if(targetMatrix.getRelationships().contains(relationshipType)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    boolean isValidRelationship(EClass sourceType, EClass targetType, EClass relationshipType) {
        if(relationshipType == null) {
            return false;
        }
        
        if(!IArchimatePackage.eINSTANCE.getRelationship().isSuperTypeOf(relationshipType)) {
            return false;
        }
        
        sourceType = getSuperEClass(sourceType);
        targetType = getSuperEClass(targetType);
        
        List<TargetMatrix> listMatrix = matrixMap.get(sourceType);
        if(listMatrix != null) {
            for(TargetMatrix targetMatrix : listMatrix) {
                if(targetMatrix.getTargetClass() == targetType && targetMatrix.getRelationships().contains(relationshipType)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * @return The actual super EClass of an EClass
     */
    private EClass getSuperEClass(EClass eClass) {
        // Use one super class type for Junctions
        if(IArchimatePackage.eINSTANCE.getJunctionElement().isSuperTypeOf(eClass)) {
            return IArchimatePackage.eINSTANCE.getJunction();
        }
        
        return eClass;
    }
    
    private void loadKeyLetters() {
        // URL to the Relationships 1.0 XML file
        URL url = Platform.getBundle(BUNDLE_ID).getResource(RELATIONSHIPS_KEYS_FILE);

        // Load the JDOM Document from XML
        Document doc = null;
        try {
            doc = new SAXBuilder().build(url);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return;
        }

        for(Object object : doc.getRootElement().getChildren(XML_ELEMENT_KEY)) {
            Element elementKey = (Element)object;

            String keyLetter = elementKey.getAttributeValue(XML_ATTRIBUTE_CHAR);
            if(keyLetter == null || keyLetter.length() != 1) {
                System.err.println(getClass() + ": Key letter incorrect: " + keyLetter); //$NON-NLS-1$
                continue;
            }

            String relationName = elementKey.getAttributeValue(XML_ATTRIBUTE_RELATIONSHIP);
            if(relationName == null) {
                System.err.println(getClass() + ": Relationship name incorrect: " + relationName); //$NON-NLS-1$
                continue;
            }

            EClass relationship = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(relationName);
            if(relationship == null) {
                System.err.println(getClass() + ": Couldn't find relationship " + relationName); //$NON-NLS-1$
                continue;
            }

            relationsKeyMap.put(keyLetter.charAt(0), relationship);
            relationsValueMap.put(relationship, keyLetter.charAt(0));
        }
    }

    private void loadRelationships(URL url) {
        // Load the JDOM Document from XML
        Document doc = null;
        try {
            doc = new SAXBuilder().build(url);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        
        // Iterate through all "source" elements
        Element elementElements = doc.getRootElement().getChild(XML_ELEMENT_ELEMENTS);
        if(elementElements == null) { // oops
            System.err.println(getClass() + ": Couldn't find elements element."); //$NON-NLS-1$
            return;
        }
        
        for(Object object : elementElements.getChildren(XML_ELEMENT_SOURCE)) {
            Element elementSource = (Element)object;

            // Source element name
            String sourceName = elementSource.getAttributeValue(XML_ATTRIBUTE_ELEMENT);
            if(sourceName == null) {
                continue;
            }
            
            // Get EClass source from mapping
            EClass source = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(sourceName);
            if(source == null) {
                System.err.println(getClass() + ": Couldn't find source " + sourceName); //$NON-NLS-1$
                continue;
            }
            
            // Create a new list of type TargetMatrix
            List<TargetMatrix> matrixList = new ArrayList<TargetMatrix>();
            
            // Put it in the main matrix map
            matrixMap.put(source, matrixList);
            
            // Iterate through all child "target" elements
            for(Object objectChild : elementSource.getChildren(XML_ELEMENT_TARGET)) {
                Element elementTarget = (Element)objectChild;
                
                // Target element name
                String targetName = elementTarget.getAttributeValue(XML_ATTRIBUTE_ELEMENT);
                if(targetName == null) {
                    continue;
                }
                
                // Get EClass target from mapping
                EClass target = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(targetName);
                if(target == null) {
                    System.err.println(getClass() + ": Couldn't find target " + targetName); //$NON-NLS-1$
                    continue;
                }
                
                // Create a new TargetMatrix and add it to the list
                TargetMatrix matrix = new TargetMatrix();
                matrixList.add(matrix);
                
                // Set target class 
                matrix.targetClass = target;
                
                // Get relations string
                String relations = elementTarget.getAttributeValue(XML_ATTRIBUTE_RELATIONS);
                if(relations == null) {
                    continue;
                }
                
                // Take each character and add the relationship from the mapping
                for(int i = 0; i < relations.length(); i++) {
                    char key = relations.charAt(i);
                    EClass relationship = relationsKeyMap.get(key);
                    if(relationship != null) {
                        matrix.getRelationships().add(relationship);
                    }
                    else {
                        System.err.println(getClass() + ": Found unmapped key char: " + key); //$NON-NLS-1$
                    }
                }
            }
        }
    }
}
