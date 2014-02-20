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

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IRelationship;



/**
 * Archimate Model Utils
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelUtils {
    
    /*
     * Flags for methods
     */
    private static final int SOURCE_RELATIONSHIPS = 1;
    private static final int TARGET_RELATIONSHIPS = 1 << 1;
    
    /**
     * Determine if a given relationship type is allowed as a source for an Archimate element
     * @param sourceElement The source element
     * @param relationshipType The class of relationship to check
     * @return True if relationshipType is a valid source relationship for sourceElement
     */
    public static final boolean isValidRelationshipStart(IArchimateElement sourceElement, EClass relationshipType) {
        return RelationshipsMatrix.INSTANCE.isValidRelationshipStart(sourceElement, relationshipType);
    }
    
    /**
     * Determine if a given relationship type is allowed between source and target Archimate elements
     * @param sourceElement The source element
     * @param targetElement The target element
     * @param relationshipType The relationship type to check
     * @return True if relationshipType is an allowed relationship type between sourceElement and targetElement
     */
    public static final boolean isValidRelationship(IArchimateElement sourceElement, IArchimateElement targetElement, EClass relationshipType) {
        return isValidRelationship(sourceElement.eClass(), targetElement.eClass(), relationshipType);
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
     * Get an array of all valid relationship class types between source and target Archimate elements
     * @param sourceElement The source element
     * @param targetElement The target element
     * @return An array of all valid relationship class types between sourceElement and targetElement
     */
    public static EClass[] getValidRelationships(IArchimateElement sourceElement, IArchimateElement targetElement) {
        return getValidRelationships(sourceElement.eClass(), targetElement.eClass());
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
     * @param element The Archimate element to check
     * @return A list of all relationships that an element has, both as target and as source
     */
    public static List<IRelationship> getRelationships(IArchimateElement element) {
        return __getRelationships(element, SOURCE_RELATIONSHIPS | TARGET_RELATIONSHIPS);
    }
    
    /**
     * @param element The Archimate element to check
     * @return A list of all source relationships that an element has
     */
    public static List<IRelationship> getSourceRelationships(IArchimateElement element) {
        return __getRelationships(element, SOURCE_RELATIONSHIPS);
    }
    
    /**
     * @param element The Archimate element to check
     * @return A list of all target relationships that an element has
     */
    public static List<IRelationship> getTargetRelationships(IArchimateElement element) {
        return __getRelationships(element, TARGET_RELATIONSHIPS);
    }

    private static List<IRelationship> __getRelationships(IArchimateElement element, int type) {
        List<IRelationship> relationships = new ArrayList<IRelationship>();
        
        if(element.getArchimateModel() != null) { // An important guard because the element might have been deleted
            IFolder folder = element.getArchimateModel().getFolder(FolderType.RELATIONS);
            __getRelationshipsForElement(folder, element, type, relationships);
            
            folder = element.getArchimateModel().getFolder(FolderType.DERIVED);
            __getRelationshipsForElement(folder, element, type, relationships);
        }
        
        return relationships;
    }
    
    private static void __getRelationshipsForElement(IFolder folder, IArchimateElement element, int type, List<IRelationship> relationships) {
        if(folder == null || element == null) {
            return;
        }
        
        for(EObject object : folder.getElements()) {
            if(object instanceof IRelationship) {
                IRelationship relationship = (IRelationship)object;
                if((type & SOURCE_RELATIONSHIPS) != 0) {
                    if(relationship.getSource() == element && !relationships.contains(relationship)) {
                        relationships.add(relationship);
                    }
                }
                if((type & TARGET_RELATIONSHIPS) != 0) {
                    if(relationship.getTarget() == element && !relationships.contains(relationship)) {
                        relationships.add(relationship);
                    }
                }
            }
        }
        
        // Child folders
        for(IFolder f : folder.getFolders()) {
            __getRelationshipsForElement(f, element, type, relationships);
        }
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
                //IArchimatePackage.eINSTANCE.getBusinessActivity(), // Deprecated in ArchiMate 2.0
                IArchimatePackage.eINSTANCE.getBusinessEvent(),
                IArchimatePackage.eINSTANCE.getBusinessInteraction(),
                IArchimatePackage.eINSTANCE.getProduct(),
                IArchimatePackage.eINSTANCE.getContract(),
                IArchimatePackage.eINSTANCE.getBusinessService(),
                IArchimatePackage.eINSTANCE.getValue(),
                IArchimatePackage.eINSTANCE.getMeaning(),
                IArchimatePackage.eINSTANCE.getRepresentation(),
                IArchimatePackage.eINSTANCE.getBusinessObject(),
                IArchimatePackage.eINSTANCE.getLocation()
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
                IArchimatePackage.eINSTANCE.getDataObject()
        };
    }
    
    /**
     * @return A list of EClass types in the Technology layer in preferred order
     */
    public static EClass[] getTechnologyClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getArtifact(),
                IArchimatePackage.eINSTANCE.getCommunicationPath(),
                IArchimatePackage.eINSTANCE.getNetwork(),
                IArchimatePackage.eINSTANCE.getInfrastructureInterface(),
                IArchimatePackage.eINSTANCE.getInfrastructureFunction(),
                IArchimatePackage.eINSTANCE.getInfrastructureService(),
                IArchimatePackage.eINSTANCE.getNode(),
                IArchimatePackage.eINSTANCE.getSystemSoftware(),
                IArchimatePackage.eINSTANCE.getDevice()
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
                IArchimatePackage.eINSTANCE.getPrinciple(),
                IArchimatePackage.eINSTANCE.getRequirement(),
                IArchimatePackage.eINSTANCE.getConstraint()
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
     * @return A list of EClass types for Relationships in preferred order
     */
    public static EClass[] getRelationsClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getSpecialisationRelationship(),
                IArchimatePackage.eINSTANCE.getCompositionRelationship(),
                IArchimatePackage.eINSTANCE.getAggregationRelationship(),
                IArchimatePackage.eINSTANCE.getAssignmentRelationship(),
                IArchimatePackage.eINSTANCE.getRealisationRelationship(),
                IArchimatePackage.eINSTANCE.getTriggeringRelationship(),
                IArchimatePackage.eINSTANCE.getFlowRelationship(),
                IArchimatePackage.eINSTANCE.getUsedByRelationship(),
                IArchimatePackage.eINSTANCE.getAccessRelationship(),
                IArchimatePackage.eINSTANCE.getAssociationRelationship(),
                IArchimatePackage.eINSTANCE.getInfluenceRelationship(),
        };
    }

    /**
     * @return A list of all EClass types for Connectors in preferred order
     */
    public static EClass[] getConnectorClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getJunction(),
                IArchimatePackage.eINSTANCE.getAndJunction(),
                IArchimatePackage.eINSTANCE.getOrJunction()
        };
    }
    
    /**
     * @return A list of all Archimate Element EClass types (excluding connector classes)
     */
    public static EClass[] getAllArchimateClasses() {
        ArrayList<EClass> list = new ArrayList<EClass>();
        list.addAll(Arrays.asList(getBusinessClasses()));
        list.addAll(Arrays.asList(getApplicationClasses()));
        list.addAll(Arrays.asList(getTechnologyClasses()));
        list.addAll(Arrays.asList(getMotivationClasses()));
        list.addAll(Arrays.asList(getImplementationMigrationClasses()));
        return list.toArray(new EClass[list.size()]);
    }
}
