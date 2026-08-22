/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    
    // Singleton instance
    public static RelationshipsMatrix INSTANCE = new RelationshipsMatrix();
    
    // The Bundle ID
    private static final String BUNDLE_ID = "com.archimatetool.model";

    // The Key letters XML file
    private static final String RELATIONSHIPS_KEYS_FILE = "model/relationships-keys.xml";

    // The Relationships XML file
    private static final String RELATIONSHIPS_FILE = "model/relationships.xml";
    
    // XML element and attribute names
    private static final String XML_ELEMENT_KEY = "key";
    private static final String XML_ELEMENT_SOURCE = "source";
    private static final String XML_ELEMENT_TARGET = "target";
    private static final String XML_ATTRIBUTE_CHAR = "char";
    private static final String XML_ATTRIBUTE_RELATIONSHIP = "relationship";
    private static final String XML_ATTRIBUTE_RELATIONS = "relations";
    private static final String XML_ATTRIBUTE_CONCEPT = "concept";
    
    // Generic pseudo concept class name for all relationships
    private static final String RELATIONSHIP_CONCEPT = "Relationship";

    /*
     * A collection of allowed relationships for a target concept.
     * In XML it is <target concept="BusinessActor" relations="fotv" />
     * The target concept is in the main matrixMap Map<EClass, TargetRelations>
     * relationKeys - The key string like "fotv"
     * relationships - Allowed relationship classes mapped to derived (true) or direct (false)
     */
    record TargetRelations(String relationKeys, Map<EClass, Boolean> relationships) {
        TargetRelations(String relationKeys) {
            this(relationKeys, new HashMap<>());
        }
    }
    
    // Mapping of key letters to relationships
    final private Map<Character, EClass> relationsKeyMap;

    // Mapping of source concepts to target concepts with allowed relations between them
    // Source EClass -> Map of Target EClass -> Relations
    final private Map<EClass, Map<EClass, TargetRelations>> matrixMap;

    private RelationshipsMatrix() {
        // Load Key letters file
        relationsKeyMap = loadKeyLetters();
        
        // Load Relationships file
        matrixMap = loadRelationships();
    }
    
    /**
     * @return The character key -> relationship EClass (for example, 'o' -> AssociationRelationship)
     */
    public Map<Character, EClass> getRelationsKeyMap() {
        return relationsKeyMap;
    }
    
    /**
     * @return The relations key string ("cfgostv") for sourceType -> targetType or empty string if not found
     */
    public String getRelationKeys(EClass sourceType, EClass targetType) {
        TargetRelations relations = getTargetRelations(sourceType, targetType);
        return relations == null ? "" : relations.relationKeys();
    }

    /**
     * @return true if relationType is a valid relation starting from sourceType
     */
    boolean isValidRelationshipStart(EClass sourceType, EClass relationType) {
        Map<EClass, TargetRelations> targets = matrixMap.get(getMapEClass(sourceType));
        if(targets == null || relationType == null) {
            return false;
        }
        
        return targets.values().stream()
                               .anyMatch(mapping -> mapping.relationships().containsKey(relationType));
    }
    
    /**
     * @return true if relationType is a valid relation from sourceType to targetType
     */
    boolean isValidRelationship(EClass sourceType, EClass targetType, EClass relationType) {
        TargetRelations relations = getTargetRelations(sourceType, targetType);
        return relations == null ? false : relations.relationships().containsKey(relationType);
    }
    
    /**
     * TODO: Currently this will always return true if relationType is present until the relationships.xml
     *       file has upper case characters to denote direct relations.
     * @return RelationshipDerivation.INVALID if no relationship mapping is present for the given source and target types, or if relationType is missing.
     *         Or RelationshipDerivation.DERIVED or RelationshipDerivation.DIRECT
     */    
    RelationshipDerivation getRelationshipDerivation(EClass sourceType, EClass targetType, EClass relationType) {
        TargetRelations relations = getTargetRelations(sourceType, targetType);
        if(relations == null) {
            return RelationshipDerivation.INVALID;
        }
        
        Boolean derived = relations.relationships().get(relationType);
        return derived == null ? RelationshipDerivation.INVALID : derived ? RelationshipDerivation.DERIVED : RelationshipDerivation.DIRECT;    
    }
    
    /**
     * @param isDerived if true relationships are derived, if false they are direct
     * @return a set of relationships from sourceType to targetType by their derived state.
     */
    Set<EClass> getRelationshipsByDerivation(EClass sourceType, EClass targetType, boolean isDerived) {
        TargetRelations relations = getTargetRelations(sourceType, targetType);
        if(relations == null) {
            return Collections.emptySet();
        }
        
        return relations.relationships().entrySet().stream() // stream  target relationships entrySet
                                        .filter(entry -> entry.getValue() == isDerived) // filter by isDerived
                                        .map(Entry::getKey) // map to the Key (EClass)
                                        .collect(Collectors.toUnmodifiableSet()); // Add to unmodifiable Set
    }
    
    /**
     * @return a TargetRelations from sourceType to targetType or null
     */
    TargetRelations getTargetRelations(EClass sourceType, EClass targetType) {
        Map<EClass, TargetRelations> targets = matrixMap.get(getMapEClass(sourceType));
        return targets == null ? null : targets.get(getMapEClass(targetType));
    }
    
    /**
     * Load the key letters to EClass mapping from the relationships-keys.xml file
     */
    private Map<Character, EClass> loadKeyLetters() {
        // Load the JDOM Document from XML
        Document doc = null;
        try {
            URL url = Platform.getBundle(BUNDLE_ID).getEntry(RELATIONSHIPS_KEYS_FILE);
            doc = new SAXBuilder().build(url);
        }
        catch(Exception ex) {
            logError("Could not load relationships-keys.xml file", ex);
            return Map.of();
        }

        Map<Character, EClass> keymap = new TreeMap<>(); // TreeMap sorts by character
        
        for(Element elementKey : doc.getRootElement().getChildren(XML_ELEMENT_KEY)) {
            String keyLetter = elementKey.getAttributeValue(XML_ATTRIBUTE_CHAR);
            if(keyLetter == null || keyLetter.length() != 1) {
                logError("Key letter incorrect: " + keyLetter);
                continue;
            }

            char key = Character.toLowerCase(keyLetter.charAt(0));
            if(keymap.containsKey(key)) {
                logError("Duplicate Key letter: " + keyLetter);
                continue;
            }

            String relationName = elementKey.getAttributeValue(XML_ATTRIBUTE_RELATIONSHIP);
            if(relationName == null) {
                logError("Relationship name not found for: " + key);
                continue;
            }

            EClass relationship = getEClass(relationName);
            if(relationship == null) {
                logError("Couldn't find relationship " + relationName);
                continue;
            }
            
            if(keymap.containsValue(relationship)) {
                logError("Duplicate relationship already mapped: " + relationName);
                continue;
            }
            
            keymap.put(key, relationship);
        }
        
        return Collections.unmodifiableMap(keymap);
    }

    /**
     * Load the relationships matrix from the relationships.xml file
     */
    private Map<EClass, Map<EClass, TargetRelations>> loadRelationships() {
        // Load the JDOM Document from XML
        Document doc = null;
        try {
            URL url = Platform.getBundle(BUNDLE_ID).getEntry(RELATIONSHIPS_FILE);
            doc = new SAXBuilder().build(url);
        }
        catch(Exception ex) {
            logError("Could not load relationships.xml file", ex);
            return Map.of();
        }
        
        Map<EClass, Map<EClass, TargetRelations>> map = new HashMap<>();
        
        // Iterate through all "source" concepts
        for(Element elementSource : doc.getRootElement().getChildren(XML_ELEMENT_SOURCE)) {
            // Source concept name
            String sourceName = elementSource.getAttributeValue(XML_ATTRIBUTE_CONCEPT);
            if(sourceName == null) {
                continue;
            }
            
            // Get EClass source from sourceName
            EClass source = getEClass(sourceName);
            if(source == null) {
                logError("Couldn't find source " + sourceName);
                continue;
            }
            
            // Create a new map for target concepts
            Map<EClass, TargetRelations> targets = new HashMap<>();
            
            // Put it in the main matrix map
            map.put(source, targets);
            
            // Iterate through all child "target" concepts
            for(Element elementTarget : elementSource.getChildren(XML_ELEMENT_TARGET)) {
                // Target concept name
                String targetName = elementTarget.getAttributeValue(XML_ATTRIBUTE_CONCEPT);
                if(targetName == null) {
                    continue;
                }
                
                // Get EClass target from targetName
                EClass target = getEClass(targetName);
                if(target == null) {
                    logError("Couldn't find target " + targetName);
                    continue;
                }
                
                // Get relations key string
                String relationKeys = elementTarget.getAttributeValue(XML_ATTRIBUTE_RELATIONS);
                if(relationKeys == null) {
                    logError("Couldn't find target relations for " + targetName);
                    continue;
                }
                
                // Create a new TargetRelations and put it in the map
                TargetRelations relations = new TargetRelations(relationKeys);
                targets.put(target, relations);
                
                // Take each character in the relations key string and add the mapped relationship.
                // If the character is lowercase then it's a derived relationship.
                for(char key : relationKeys.toCharArray()) {
                    EClass relationship = relationsKeyMap.get(Character.toLowerCase(key));
                    if(relationship == null) {
                        logError("Unknown key '" + key + "' for " + source.getName() + " -> " + target.getName());
                    }
                    else {
                        if(relations.relationships().containsKey(relationship)) {
                            logError("Duplicate key '" + key + "' for " + source.getName() + " -> " + target.getName());
                        }
                        else {
                            relations.relationships().put(relationship, Character.isLowerCase(key));
                        }
                    }
                }
            }
        }
        
        return Collections.unmodifiableMap(map);
    }
    
    /**
     * @return the EClass to use in a map.
     * If eClass is a super type of ArchimateRelationship then return the generic
     * ArchimateRelationship EClass, else return eClass
     */
    private EClass getMapEClass(EClass eClass) {
        return IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass) ?
                                           IArchimatePackage.eINSTANCE.getArchimateRelationship() : eClass;
    }
    
    /**
     * @return EClass for className
     * If className is "Relationship" return generic ArchimateRelationship EClass
     */
    private EClass getEClass(String className) {
        return RELATIONSHIP_CONCEPT.equals(className) ? IArchimatePackage.eINSTANCE.getArchimateRelationship()
                                                        : (EClass)IArchimatePackage.eINSTANCE.getEClassifier(className);
    }
    
    private void logError(String message) {
        ILog.of(getClass()).error(getClass() + ": " + message);
    }
    
    private void logError(String message, Exception ex) {
        ILog.of(getClass()).error(message, ex);
    }
}
