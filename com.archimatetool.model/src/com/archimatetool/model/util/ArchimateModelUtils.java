/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;



/**
 * Archimate Model Utils
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelUtils {
    
    /**
     * Determine if a given relationship type is allowed as a source for an Archimate concept
     * @param sourceConcept The source concept
     * @param relationshipType The class of relationship to check
     * @return True if relationshipType is a valid source relationship for sourceComponent
     */
    public static final boolean isValidRelationshipStart(IArchimateConcept sourceConcept, EClass relationshipType) {
        // If the source concept is a Junction check for valid relationships
        if(sourceConcept instanceof IJunction) {
            // Has to be the same type of relationship
            for(IArchimateRelationship rel : getAllRelationshipsForConcept(sourceConcept)) {
                if(!rel.eClass().equals(relationshipType)) {
                    return false;
                }
            }
        }
        
        return RelationshipsMatrix.INSTANCE.isValidRelationshipStart(sourceConcept.eClass(), relationshipType);
    }
    
    /**
     * Determine if a given relationship type is allowed between source and target Archimate components
     * @param sourceConcept The source concept
     * @param targetConcept The target concept
     * @param relationshipType The relationship type to check
     * @return True if relationshipType is an allowed relationship type between sourceComponent and targetComponent
     */
    public static final boolean isValidRelationship(IArchimateConcept sourceConcept, IArchimateConcept targetConcept, EClass relationshipType) {
        if(hasDirectRelationship(sourceConcept, targetConcept)) {
            return false;
        }
        
        // If the source concept is a Junction check for valid relationships
        if(sourceConcept instanceof IJunction) {
            // This is an invalid indirect relationship between a concept connected to the Junction and the target concept
            for(IArchimateRelationship rel : sourceConcept.getTargetRelationships()) {
                if(!isValidRelationship(rel.getSource().eClass(), targetConcept.eClass(), relationshipType)) {
                    return false;
                }
            }
            // Has to be the same type of relationship
            for(IArchimateRelationship rel : getAllRelationshipsForConcept(sourceConcept)) {
                if(!rel.eClass().equals(relationshipType)) {
                    return false;
                }
            }
        }
        
        // If the target concept is a Junction check for valid relationships
        if(targetConcept instanceof IJunction) {
            // This is an invalid indirect relationship between a concept connected to the Junction and the source concept
            for(IArchimateRelationship rel : targetConcept.getSourceRelationships()) {
                if(!isValidRelationship(sourceConcept.eClass(), rel.getTarget().eClass(), relationshipType)) {
                    return false;
                }
            }
            // Has to be the same type of relationship
            for(IArchimateRelationship rel : getAllRelationshipsForConcept(targetConcept)) {
                if(!rel.eClass().equals(relationshipType)) {
                    return false;
                }
            }
        }
        
        return isValidRelationship(sourceConcept.eClass(), targetConcept.eClass(), relationshipType);
    }

    /**
     * Determine if a given relationship type is allowed between source and target Archimate class types
     * @param sourceType The source type
     * @param targetType The target type
     * @param relationshipType The relationship type to check
     * @return True if relationshipType is an allowed relationship type between sourceType and targetType
     */
    public static final boolean isValidRelationship(EClass sourceType, EClass targetType, EClass relationshipType) {
        return RelationshipsMatrix.INSTANCE.isValidRelationship(sourceType, targetType, relationshipType);
    }
    
    /**
     * Get an array of all valid relationship class types between source and target Archimate components
     * @param sourceConcept The source concept
     * @param targetConcept The target concept
     * @return An array of all valid relationship class types between sourceElement and targetElement
     */
    public static EClass[] getValidRelationships(IArchimateConcept sourceConcept, IArchimateConcept targetConcept) {
        List<EClass> list = new ArrayList<EClass>();
        
        for(EClass eClass : getRelationsClasses()) {
            if(isValidRelationship(sourceConcept, targetConcept, eClass)) {
                list.add(eClass); 
            }
        }
        
        return list.toArray(new EClass[list.size()]);
    }
    
    /**
     * Get an array of all valid relationship class types between source and target Archimate class types
     * @param sourceType The source type
     * @param targetType The target type
     * @return All valid relationship types between sourceType and targetType
     */
    public static EClass[] getValidRelationships(EClass sourceType, EClass targetType) {
        List<EClass> list = new ArrayList<EClass>();
        
        for(EClass eClass : getRelationsClasses()) {
            if(isValidRelationship(sourceType, targetType, eClass)) {
                list.add(eClass); 
            }
        }
        
        return list.toArray(new EClass[list.size()]);
    }
    
    /**
     * @param concept1
     * @param concept2
     * @return True if concept1 is a relationship and concept2 already has it as a relationship
     *         OR 
     *         True if concept2 is a relationship and concept1 already has it as a relationship
     */
    public static boolean hasDirectRelationship(IArchimateConcept concept1, IArchimateConcept concept2) {
        if(concept1 instanceof IArchimateRelationship) {
            if(getAllRelationshipsForConcept(concept2).contains(concept1)) {
                return true;
            }
        }
        
        if(concept2 instanceof IArchimateRelationship) {
            if(getAllRelationshipsForConcept(concept1).contains(concept2)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @param concept The Archimate concept to get relationships for
     * @return A list of all relationships that a concept has, both as target and as source
     */
    public static List<IArchimateRelationship> getAllRelationshipsForConcept(IArchimateConcept concept) {
        Set<IArchimateRelationship> set = new HashSet<IArchimateRelationship>();
        set.addAll(concept.getSourceRelationships());
        set.addAll(concept.getTargetRelationships());
        return new ArrayList<>(set);
    }
    
    /**
     * Get an EObject type in an Archimate Model given its String ID
     * @param model The owning Archimate Model
     * @param id The ID of the object to search for
     * @return The matching EObject in the model given its ID or null if not found
     */
    public static EObject getObjectByID(IArchimateModel model, String id) {
        if(id == null || model == null) {
            return null;
        }
        
        if(id.equals(model.getId())) {
            return model;
        }
        
        // This is an expensive iteration!
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IIdentifier && id.equals(((IIdentifier)element).getId())) {
                return element;
            }
        }

        return null;
    }

    
    /**
     * @return A list of all EClass types in the Strategy layer in preferred order
     */
    public static EClass[] getStrategyClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getResource(),
                IArchimatePackage.eINSTANCE.getCapability(),
                IArchimatePackage.eINSTANCE.getValueStream(),
                IArchimatePackage.eINSTANCE.getCourseOfAction()
        };
    }

    /**
     * @return A list of all EClass types in the Business layer in preferred order
     */
    public static EClass[] getBusinessClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getBusinessActor(),
                IArchimatePackage.eINSTANCE.getBusinessRole(),
                IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
                IArchimatePackage.eINSTANCE.getBusinessInterface(),
                IArchimatePackage.eINSTANCE.getBusinessProcess(),
                IArchimatePackage.eINSTANCE.getBusinessFunction(),
                IArchimatePackage.eINSTANCE.getBusinessInteraction(),
                IArchimatePackage.eINSTANCE.getBusinessEvent(),
                IArchimatePackage.eINSTANCE.getBusinessService(),
                IArchimatePackage.eINSTANCE.getBusinessObject(),
                IArchimatePackage.eINSTANCE.getContract(),
                IArchimatePackage.eINSTANCE.getRepresentation(),
                IArchimatePackage.eINSTANCE.getProduct()
        };
    }
    
    /**
     * @return A list of all EClass types in the Application layer in preferred order
     */
    public static EClass[] getApplicationClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getApplicationComponent(),
                IArchimatePackage.eINSTANCE.getApplicationCollaboration(),
                IArchimatePackage.eINSTANCE.getApplicationInterface(),
                IArchimatePackage.eINSTANCE.getApplicationFunction(),
                IArchimatePackage.eINSTANCE.getApplicationInteraction(),
                IArchimatePackage.eINSTANCE.getApplicationProcess(),
                IArchimatePackage.eINSTANCE.getApplicationEvent(),
                IArchimatePackage.eINSTANCE.getApplicationService(),
                IArchimatePackage.eINSTANCE.getDataObject()
        };
    }
    
    /**
     * @return A list of EClass types in the Technology layer in preferred order
     */
    public static EClass[] getTechnologyClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getNode(),
                IArchimatePackage.eINSTANCE.getDevice(),
                IArchimatePackage.eINSTANCE.getSystemSoftware(),
                IArchimatePackage.eINSTANCE.getTechnologyCollaboration(),
                IArchimatePackage.eINSTANCE.getTechnologyInterface(),
                IArchimatePackage.eINSTANCE.getPath(),
                IArchimatePackage.eINSTANCE.getCommunicationNetwork(),
                IArchimatePackage.eINSTANCE.getTechnologyFunction(),
                IArchimatePackage.eINSTANCE.getTechnologyProcess(),
                IArchimatePackage.eINSTANCE.getTechnologyInteraction(),
                IArchimatePackage.eINSTANCE.getTechnologyEvent(),
                IArchimatePackage.eINSTANCE.getTechnologyService(),
                IArchimatePackage.eINSTANCE.getArtifact()
        };
    }

    /**
     * @return A list of all EClass types in the Physical layer in preferred order
     */
    public static EClass[] getPhysicalClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getEquipment(),
                IArchimatePackage.eINSTANCE.getFacility(),
                IArchimatePackage.eINSTANCE.getDistributionNetwork(),
                IArchimatePackage.eINSTANCE.getMaterial()
        };
    }

    /**
     * @return A list of EClass types in the Motivation extension in preferred order
     */
    public static EClass[] getMotivationClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getStakeholder(),
                IArchimatePackage.eINSTANCE.getDriver(),
                IArchimatePackage.eINSTANCE.getAssessment(),
                IArchimatePackage.eINSTANCE.getGoal(),
                IArchimatePackage.eINSTANCE.getOutcome(),
                IArchimatePackage.eINSTANCE.getPrinciple(),
                IArchimatePackage.eINSTANCE.getRequirement(),
                IArchimatePackage.eINSTANCE.getConstraint(),
                IArchimatePackage.eINSTANCE.getMeaning(),
                IArchimatePackage.eINSTANCE.getValue()
        };
    }
    
    /**
     * @return A list of EClass types in the Implementation and Migration extension in preferred order
     */
    public static EClass[] getImplementationMigrationClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getWorkPackage(),
                IArchimatePackage.eINSTANCE.getDeliverable(),
                IArchimatePackage.eINSTANCE.getImplementationEvent(),
                IArchimatePackage.eINSTANCE.getPlateau(),
                IArchimatePackage.eINSTANCE.getGap()
        };
    }

    /**
     * @return A list of all EClass types in the "Other" category in preferred order
     */
    public static EClass[] getOtherClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getLocation(),
                IArchimatePackage.eINSTANCE.getGrouping()
        };
    }

    /**
     * @return A list of all EClass types for Connectors in preferred order
     */
    public static EClass[] getConnectorClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getJunction()
        };
    }
    
    /**
     * @return A list of EClass types for Relationships in preferred order
     */
    public static EClass[] getRelationsClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getCompositionRelationship(),
                IArchimatePackage.eINSTANCE.getAggregationRelationship(),
                IArchimatePackage.eINSTANCE.getAssignmentRelationship(),
                IArchimatePackage.eINSTANCE.getRealizationRelationship(),
                IArchimatePackage.eINSTANCE.getServingRelationship(),
                IArchimatePackage.eINSTANCE.getAccessRelationship(),
                IArchimatePackage.eINSTANCE.getInfluenceRelationship(),
                IArchimatePackage.eINSTANCE.getTriggeringRelationship(),
                IArchimatePackage.eINSTANCE.getFlowRelationship(),
                IArchimatePackage.eINSTANCE.getSpecializationRelationship(),
                IArchimatePackage.eINSTANCE.getAssociationRelationship(),
        };
    }

    /**
     * @return A list of all Archimate Element EClass types (excluding connector classes)
     */
    public static EClass[] getAllArchimateClasses() {
        ArrayList<EClass> list = new ArrayList<EClass>();
        
        list.addAll(Arrays.asList(getStrategyClasses()));
        list.addAll(Arrays.asList(getBusinessClasses()));
        list.addAll(Arrays.asList(getApplicationClasses()));
        list.addAll(Arrays.asList(getTechnologyClasses()));
        list.addAll(Arrays.asList(getPhysicalClasses()));
        list.addAll(Arrays.asList(getMotivationClasses()));
        list.addAll(Arrays.asList(getImplementationMigrationClasses()));
        list.addAll(Arrays.asList(getOtherClasses()));
        
        return list.toArray(new EClass[list.size()]);
    }
    
    // =====================================================================================================
    // Profile / Specialization Utils
    // =====================================================================================================

    /**
     * @return True if a profile exists in the model with the given name and concept type
     */
    public static boolean hasProfileByNameAndType(IArchimateModel model, String profileName, String conceptType) {
        return hasProfileByNameAndType(model.getProfiles(), profileName, conceptType);
    }
    
    /**
     * @return True if a profile exists in the given Collection of Profiles with the given name and concept type
     */
    public static boolean hasProfileByNameAndType(Collection<IProfile> profiles, String profileName, String conceptType) {
        for(IProfile p : profiles) {
            if(p.getName() != null && p.getName().equals(profileName)
                    && p.getConceptType() != null && p.getConceptType().equals(conceptType)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @return A list of all model Profiles that have a concept type set to the given conceptType
     *         This is so we can find the ones suitable for a given concept
     */
    public static List<IProfile> findProfilesForConceptType(IArchimateModel model, EClass conceptType) {
        List<IProfile> profiles = new ArrayList<>();
        
        for(IProfile profile : model.getProfiles()) {
            if(conceptType != null && conceptType.getName().equals(profile.getConceptType())) {
                profiles.add(profile);
            }
        }
        
        return profiles;
    }
    
    /**
     * @return A list of all references of the given Profile in the model.
     *         If profile is null or is not contained in a model then an empty list is returned
     *         This is a slow process if this is called more than once
     */
    public static List<IProfiles> findProfileUsage(IProfile profile) {
        List<IProfiles> profiles = new ArrayList<>();
        
        if(profile != null && profile.getArchimateModel() != null) {
            for(Setting setting : UsageCrossReferencer.find(profile, profile.getArchimateModel())) {
                profiles.add((IProfiles)setting.getEObject());
            }
        }
        
        return profiles;
    }
    
    /**
     * @return A map of all references of all Profiles in the given model.
     *         This method is many times faster than calling findProfileUsage(IProfile) repeatedly
     */
    public static Map<IProfile, List<IProfiles>> findProfilesUsage(IArchimateModel model) {
        Map<IProfile, List<IProfiles>> map = new HashMap<>();
        
        // Iterate through all model contents
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            // This is a concept potentially containing profile references
            if(eObject instanceof IProfiles) {
                // Iterate through the profiles
                for(IProfile profile : ((IProfiles)eObject).getProfiles()) {
                    // Get the list from the map
                    List<IProfiles> list = map.get(profile);
                    // Create and add a new list if needed
                    if(list == null) {
                        list = new ArrayList<>();
                        map.put(profile, list);
                    }
                    // Add the concept to the list
                    list.add((IProfiles)eObject);
                }
            }
        }
        
        return map;
    }
}
