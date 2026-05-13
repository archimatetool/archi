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

import org.eclipse.core.runtime.ILog;
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
@SuppressWarnings("nls")
public class RelationshipsMatrix {
    
    /**
     * Singleton instance
     */
    public static RelationshipsMatrix INSTANCE = new RelationshipsMatrix();
    
    /**
     * The Bundle ID
     */
    private static final String BUNDLE_ID = "com.archimatetool.model";

    /**
     * The Key letters XML file
     */
    private static final String RELATIONSHIPS_KEYS_FILE = "model/relationships-keys.xml";

    /**
     * The Relationships XML file
     */
    private static final String RELATIONSHIPS_FILE = "model/relationships.xml";
    
    /*
     * XML element and attribute names
     */
    private static final String XML_ELEMENT_KEY = "key";
    private static final String XML_ELEMENT_SOURCE = "source";
    private static final String XML_ELEMENT_TARGET = "target";
    private static final String XML_ATTRIBUTE_CHAR = "char";
    private static final String XML_ATTRIBUTE_RELATIONSHIP = "relationship";
    private static final String XML_ATTRIBUTE_RELATIONS = "relations";
    private static final String XML_ATTRIBUTE_CONCEPT = "concept";
    
    // Generic pseudo concept class name for all relationships
    private static final String RELATIONSHIP_CONCEPT = "Relationship";

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
    private Map<EClass, List<TargetMatrix>> matrixMap = new HashMap<>();
    
    /**
     * Mapping of key letters to relationships
     */
    private Map<Character, EClass> relationsKeyMap = new LinkedHashMap<>();
        
    /**
     * Mapping of relationships to key letters
     */
    private Map<EClass, Character> relationsValueMap = new LinkedHashMap<>();

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
        // Use ArchimateRelationship as a generic super type
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(sourceType)) {
            sourceType = IArchimatePackage.eINSTANCE.getArchimateRelationship();
        }
        
        List<TargetMatrix> listMatrix = matrixMap.get(sourceType);
        if(listMatrix == null) {
            return false;
        }
        
        return listMatrix.stream()
                         .anyMatch(targetMatrix -> targetMatrix.getRelationships().contains(relationshipType));
    }
    
    boolean isValidRelationship(EClass sourceType, EClass targetType, EClass relationshipType) {
        if(relationshipType == null) {
            return false;
        }
        
        // relationshipType has to be a Relationship class
        if(!IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(relationshipType)) {
            return false;
        }
        
        // Use ArchimateRelationship as a generic super type
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(sourceType)) {
            sourceType = IArchimatePackage.eINSTANCE.getArchimateRelationship();
        }
        
        // Use ArchimateRelationship as a generic super type
        if(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(targetType)) {
            targetType = IArchimatePackage.eINSTANCE.getArchimateRelationship();
        }
        
        List<TargetMatrix> listMatrix = matrixMap.get(sourceType);
        if(listMatrix == null) {
            return false;
        }
        
        final EClass targetClass = targetType;
        
        return listMatrix.stream()
                .anyMatch(targetMatrix -> targetMatrix.getTargetClass() == targetClass
                                          && targetMatrix.getRelationships().contains(relationshipType));
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
            ILog.of(getClass()).error("Could not load key letters", ex);
            return;
        }

        for(Element elementKey : doc.getRootElement().getChildren(XML_ELEMENT_KEY)) {
            String keyLetter = elementKey.getAttributeValue(XML_ATTRIBUTE_CHAR);
            if(keyLetter == null || keyLetter.length() != 1) {
                ILog.of(getClass()).error(getClass() + ": Key letter incorrect: " + keyLetter);
                continue;
            }

            String relationName = elementKey.getAttributeValue(XML_ATTRIBUTE_RELATIONSHIP);
            if(relationName == null) {
                ILog.of(getClass()).error(getClass() + ": Relationship name incorrect: " + relationName);
                continue;
            }

            EClass relationship = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(relationName);
            if(relationship == null) {
                ILog.of(getClass()).error(getClass() + ": Couldn't find relationship " + relationName);
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
            ILog.of(getClass()).error("Could not relationships", ex);
            return;
        }
        
        // Iterate through all "source" concepts
        for(Element elementSource : doc.getRootElement().getChildren(XML_ELEMENT_SOURCE)) {
            // Source concept name
            String sourceName = elementSource.getAttributeValue(XML_ATTRIBUTE_CONCEPT);
            if(sourceName == null) {
                continue;
            }
            
            // Get EClass source from mapping
            EClass source = null;
            
            // Use ArchimateRelationship as a generic super type
            if(sourceName.equals(RELATIONSHIP_CONCEPT)) {
                source = IArchimatePackage.eINSTANCE.getArchimateRelationship();
            }
            // Else use given class
            else {
                source = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(sourceName);
            }
            
            if(source == null) {
                ILog.of(getClass()).error(getClass() + ": Couldn't find source " + sourceName);
                continue;
            }
            
            // Create a new list of type TargetMatrix
            List<TargetMatrix> matrixList = new ArrayList<>();
            
            // Put it in the main matrix map
            matrixMap.put(source, matrixList);
            
            // Iterate through all child "target" concepts
            for(Element elementTarget : elementSource.getChildren(XML_ELEMENT_TARGET)) {
                // Target concept name
                String targetName = elementTarget.getAttributeValue(XML_ATTRIBUTE_CONCEPT);
                if(targetName == null) {
                    continue;
                }
                
                EClass target = null;
                
                // Get EClass target from mapping

                // Use ArchimateRelationship as a generic super type
                if(targetName.equals(RELATIONSHIP_CONCEPT)) {
                    target = IArchimatePackage.eINSTANCE.getArchimateRelationship();
                }
                // Else use given class
                else {
                    target = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(targetName);
                }
                
                if(target == null) {
                    ILog.of(getClass()).error(getClass() + ": Couldn't find target " + targetName);
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
                for(char key : relations.toCharArray()) {
                    EClass relationship = relationsKeyMap.get(key);
                    if(relationship != null) {
                        matrix.getRelationships().add(relationship);
                    }
                    else {
                        ILog.of(getClass()).error(getClass() + ": Found unmapped key char: " + key);
                    }
                }
            }
        }
    }
}
