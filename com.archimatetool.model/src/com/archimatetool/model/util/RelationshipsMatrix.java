/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.archimatetool.model.IArchimatePackage;



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
    static final String BUNDLE_ID = "com.archimatetool.model"; //$NON-NLS-1$

    /**
     * The Key letters XML file
     */
    static final String RELATIONSHIPS_KEYS_FILE = "model/relationships-keys.xml"; //$NON-NLS-1$

    /**
     * The Relationships XML file
     */
    static final String RELATIONSHIPS_FILE = "model/relationships.xml"; //$NON-NLS-1$
    
    /*
     * XML element and attribute names
     */
    static final String XML_ELEMENT_ROOT = "relationships"; //$NON-NLS-1$
    static final String XML_ELEMENT_KEYS = "keys"; //$NON-NLS-1$
    static final String XML_ELEMENT_KEY = "key"; //$NON-NLS-1$
    static final String XML_ELEMENT_SOURCE = "source"; //$NON-NLS-1$
    static final String XML_ELEMENT_TARGET = "target"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_CHAR = "char"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_RELATIONSHIP = "relationship"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_RELATIONS = "relations"; //$NON-NLS-1$
    static final String XML_ATTRIBUTE_CONCEPT = "concept"; //$NON-NLS-1$

    public static class TargetMatrix {
        EClass targetClass;
        Set<EClass> relationships = new LinkedHashSet<>();
        
        public EClass getTargetClass() {
            return targetClass;
        }
        
        public Set<EClass> getRelationships() {
            return relationships;
        }
    }
    
    /**
     * Mapping of source concepts to target concepts and possible relations
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
        
        // Load Relationships file
        loadRelationships();
    }
    
    public Map<EClass, List<TargetMatrix>> getRelationshipsMatrix() {
        return Collections.unmodifiableMap(matrixMap);
    }
    
    public Map<EClass, Character> getRelationshipsValueMap() {
        return Collections.unmodifiableMap(relationsValueMap);
    }

    boolean isValidRelationshipStart(EClass sourceType, EClass relationshipType) {
        // Use "Relationship" as a generic super type
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(sourceType)) {
            sourceType = IArchimatePackage.eINSTANCE.getArchimateRelationship();
        }
        
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
        
        // relationshipType has to be a Relationship class
        if(!IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(relationshipType)) {
            return false;
        }
        
        // Use "Relationship" as a generic super type
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(sourceType)) {
            sourceType = IArchimatePackage.eINSTANCE.getArchimateRelationship();
        }
        
        // Use "Relationship" as a generic super type
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(targetType)) {
            targetType = IArchimatePackage.eINSTANCE.getArchimateRelationship();
        }
        
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
    
    private void loadKeyLetters() {
        //URL url = Platform.getBundle(BUNDLE_ID).getResource(RELATIONSHIPS_KEYS_FILE);
        URL url = Platform.getBundle(BUNDLE_ID).getEntry(RELATIONSHIPS_KEYS_FILE);

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

    private void loadRelationships() {
        //URL url = Platform.getBundle(BUNDLE_ID).getResource(RELATIONSHIPS_FILE);
        URL url = Platform.getBundle(BUNDLE_ID).getEntry(RELATIONSHIPS_FILE);
        
        // Load the JDOM Document from XML
        Document doc = null;
        try {
            doc = new SAXBuilder().build(url);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        
        // Iterate through all "source" concepts
        for(Object object : doc.getRootElement().getChildren(XML_ELEMENT_SOURCE)) {
            Element elementSource = (Element)object;

            // Source concept name
            String sourceName = elementSource.getAttributeValue(XML_ATTRIBUTE_CONCEPT);
            if(sourceName == null) {
                continue;
            }
            
            // Get EClass source from mapping
            EClass source = null;
            
            // Use "Relationship" as a generic super type
            if(sourceName.equals("Relationship")) { //$NON-NLS-1$
                source = IArchimatePackage.eINSTANCE.getArchimateRelationship();
            }
            // Else use given class
            else {
                source = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(sourceName);
            }
            
            if(source == null) {
                System.err.println(getClass() + ": Couldn't find source " + sourceName); //$NON-NLS-1$
                continue;
            }
            
            // Create a new list of type TargetMatrix
            List<TargetMatrix> matrixList = new ArrayList<TargetMatrix>();
            
            // Put it in the main matrix map
            matrixMap.put(source, matrixList);
            
            // Iterate through all child "target" concepts
            for(Object objectChild : elementSource.getChildren(XML_ELEMENT_TARGET)) {
                Element elementTarget = (Element)objectChild;
                
                // Target concept name
                String targetName = elementTarget.getAttributeValue(XML_ATTRIBUTE_CONCEPT);
                if(targetName == null) {
                    continue;
                }
                
                EClass target = null;
                
                // Get EClass target from mapping

                // Use "Relationship" as a generic super type
                if(targetName.equals("Relationship")) { //$NON-NLS-1$
                    target = IArchimatePackage.eINSTANCE.getArchimateRelationship();
                }
                // Else use given class
                else {
                    target = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(targetName);
                }
                
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
