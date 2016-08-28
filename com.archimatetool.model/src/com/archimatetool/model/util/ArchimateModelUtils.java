/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IIdentifier;



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
        return RelationshipsMatrix.INSTANCE.isValidRelationshipStart(sourceConcept, relationshipType);
    }
    
    /**
     * Determine if a given relationship type is allowed between source and target Archimate components
     * @param sourceConcept The source concept
     * @param targetConcept The target concept
     * @param relationshipType The relationship type to check
     * @return True if relationshipType is an allowed relationship type between sourceComponent and targetComponent
     */
    public static final boolean isValidRelationship(IArchimateConcept sourceConcept, IArchimateConcept targetConcept, EClass relationshipType) {
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
        return getValidRelationships(sourceConcept.eClass(), targetConcept.eClass());
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
     * @param concept The Archimate concept to get relationships for
     * @return A list of all relationships that a concept has, both as target and as source
     */
    public static List<IArchimateRelationship> getAllRelationshipsForConcept(IArchimateConcept concept) {
        List<IArchimateRelationship> list = new ArrayList<IArchimateRelationship>(); // make a copy
        
        for(IArchimateRelationship r : concept.getSourceRelationships()) {
            if(!list.contains(r)) {
                list.add(r);
            }
        }
        
        for(IArchimateRelationship r : concept.getTargetRelationships()) {
            if(!list.contains(r)) {
                list.add(r);
            }
        }
        
        return list;
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
                IArchimatePackage.eINSTANCE.getBusinessFunction(),
                IArchimatePackage.eINSTANCE.getBusinessProcess(),
                IArchimatePackage.eINSTANCE.getBusinessEvent(),
                IArchimatePackage.eINSTANCE.getBusinessInteraction(),
                IArchimatePackage.eINSTANCE.getProduct(),
                IArchimatePackage.eINSTANCE.getContract(),
                IArchimatePackage.eINSTANCE.getBusinessService(),
                IArchimatePackage.eINSTANCE.getRepresentation(),
                IArchimatePackage.eINSTANCE.getBusinessObject(),
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
                IArchimatePackage.eINSTANCE.getApplicationService(),
                IArchimatePackage.eINSTANCE.getApplicationFunction(),
                IArchimatePackage.eINSTANCE.getApplicationInteraction(),
                IArchimatePackage.eINSTANCE.getApplicationProcess(),
                IArchimatePackage.eINSTANCE.getApplicationEvent(),
                IArchimatePackage.eINSTANCE.getDataObject()
        };
    }
    
    /**
     * @return A list of EClass types in the Technology layer in preferred order
     */
    public static EClass[] getTechnologyClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getArtifact(),
                IArchimatePackage.eINSTANCE.getPath(),
                IArchimatePackage.eINSTANCE.getCommunicationNetwork(),
                IArchimatePackage.eINSTANCE.getTechnologyInterface(),
                IArchimatePackage.eINSTANCE.getTechnologyFunction(),
                IArchimatePackage.eINSTANCE.getTechnologyProcess(),
                IArchimatePackage.eINSTANCE.getTechnologyInteraction(),
                IArchimatePackage.eINSTANCE.getTechnologyEvent(),
                IArchimatePackage.eINSTANCE.getTechnologyService(),
                IArchimatePackage.eINSTANCE.getNode(),
                IArchimatePackage.eINSTANCE.getSystemSoftware(),
                IArchimatePackage.eINSTANCE.getTechnologyCollaboration(),
                IArchimatePackage.eINSTANCE.getDevice()
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
                IArchimatePackage.eINSTANCE.getAndJunction(),
                IArchimatePackage.eINSTANCE.getOrJunction()
        };
    }
    
    /**
     * @return A list of EClass types for Relationships in preferred order
     */
    public static EClass[] getRelationsClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getSpecializationRelationship(),
                IArchimatePackage.eINSTANCE.getCompositionRelationship(),
                IArchimatePackage.eINSTANCE.getAggregationRelationship(),
                IArchimatePackage.eINSTANCE.getAssignmentRelationship(),
                IArchimatePackage.eINSTANCE.getRealizationRelationship(),
                IArchimatePackage.eINSTANCE.getTriggeringRelationship(),
                IArchimatePackage.eINSTANCE.getFlowRelationship(),
                IArchimatePackage.eINSTANCE.getServingRelationship(),
                IArchimatePackage.eINSTANCE.getAccessRelationship(),
                IArchimatePackage.eINSTANCE.getAssociationRelationship(),
                IArchimatePackage.eINSTANCE.getInfluenceRelationship(),
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
}
