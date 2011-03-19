/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.model.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IIdentifier;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Archimate Model Utils
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelUtils {
    
    /*
     * The following is a system to get the allowed Relationships between ArchiMate object types.
     * It's ugly, and you could probably do it from a two-dimensional array of bit-wise numbers,
     * but it works. And it's easier to copy the matrix in Appendix B of the spec.
     * Feel free to come up with a better solution. 
     */
    
    static Hashtable<EClass, String> keyLetters = new Hashtable<EClass, String>();
    static LinkedHashMap<EClass, String> table = new LinkedHashMap<EClass, String>();
    
    static {
        keyLetters.put(IArchimatePackage.eINSTANCE.getAccessRelationship(), "a");
        keyLetters.put(IArchimatePackage.eINSTANCE.getCompositionRelationship(), "c");
        keyLetters.put(IArchimatePackage.eINSTANCE.getFlowRelationship(), "f");
        keyLetters.put(IArchimatePackage.eINSTANCE.getAggregationRelationship(), "g");
        keyLetters.put(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), "i");
        keyLetters.put(IArchimatePackage.eINSTANCE.getAssociationRelationship(), "o");
        keyLetters.put(IArchimatePackage.eINSTANCE.getRealisationRelationship(), "r");
        keyLetters.put(IArchimatePackage.eINSTANCE.getSpecialisationRelationship(), "s");
        keyLetters.put(IArchimatePackage.eINSTANCE.getTriggeringRelationship(), "t");
        keyLetters.put(IArchimatePackage.eINSTANCE.getUsedByRelationship(), "u");
        
        // The order and number of these is meaningful!!!
        // Each key entry relates to the position of the table entry as per the matrix in Appendix B of the spec
        
        // 1 Junction
        table.put(IArchimatePackage.eINSTANCE.getJunction(),
                "ft,ft,ft,ft,ft,ft,ft,ft,ft,ft,,,ft,,,,," +
                "ft,ft,ft,ft,ft,ft,,,,ft,ft,ft,,ft,ft");
        
        // 2 Business Activity
        table.put(IArchimatePackage.eINSTANCE.getBusinessActivity(),
                "ft,fostu,fotu,fotu,fotu,ou,ou,ou,ou,fotu,ao,oru,oru,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,oru,ao,o,o,o,o,o,o,o,o");
        
        // 3 Business Event
        table.put(IArchimatePackage.eINSTANCE.getBusinessEvent(),
                "ft,fot,cfgost,fot,fot,o,o,o,o,fot,ao,o,o,o,ao,ao,o," +
                "o,o,o,o,o,o,o,o,o,o,o,o,o,o,o");
        
        // 4 Business Interaction
        table.put(IArchimatePackage.eINSTANCE.getBusinessInteraction(),
                "ft,fotu,fotu,cfgostu,fotu,ou,ou,ou,ou,fotu,ao,oru,oru,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,oru,ao,o,o,o,o,o,o,o,o");
        
        // 5 Business Process
        table.put(IArchimatePackage.eINSTANCE.getBusinessProcess(),
                "ft,cfgotu,cfgotu,fotu,cfgostu,ou,ou,ou,ou,cfgotu,ao,oru,oru,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,oru,ao,o,o,o,o,o,o,o,o");
        
        // 6 Business Actor
        table.put(IArchimatePackage.eINSTANCE.getBusinessActor(),
                "ft,iou,iou,ou,iou,cfgostu,fiotu,cfgiostu,cfgiostu,fiou,ao,oru,ioru,o,ao,ao,o," +
                "fotu,fotu,ou,ou,fotu,oru,ao,o,o,o,o,o,o,o,o");

        // 7 Business Interface
        table.put(IArchimatePackage.eINSTANCE.getBusinessInterface(),
                "ft,ou,ou,ou,ou,fotu,cfgostu,fotu,fotu,ou,ao,ou,iou,o,ao,ao,o," +
                "fotu,fotu,ou,ou,fotu,ou,ao,o,o,o,o,o,o,o,o");

        // 8 Business Collaboration
        table.put(IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
                "ft,iou,iou,iou,iou,cfgostu,cfgiotu,cfgiostu,cfgiostu,fiou,ao,oru,ioru,o,ao,ao,o," +
                "fotu,fotu,ou,ou,fotu,oru,ao,o,o,o,o,o,o,o,o");

        // 8 Business Role
        table.put(IArchimatePackage.eINSTANCE.getBusinessRole(),
                "ft,iou,iou,ou,iou,cfgostu,cfgiotu,cfgiostu,cfgiostu,fiou,ao,oru,ioru,o,ao,ao,o," +
                "fotu,fotu,ou,ou,fotu,oru,ao,o,o,o,o,o,o,o,o");

        // 10 Business Function
        table.put(IArchimatePackage.eINSTANCE.getBusinessFunction(),
                "ft,cfgotu,cfgotu,fotu,cfgotu,fou,ou,fou,fou,cfgostu,ao,oru,oru,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,oru,ao,o,o,o,o,o,o,o,o");

        // 11 Contract
        table.put(IArchimatePackage.eINSTANCE.getContract(),
                ",o,o,o,o,o,o,o,o,o,cgos,o,o,o,cgos,o,o," +
                "o,o,o,o,o,o,o,o,o,o,o,o,o,o,o");

        // 12 Product
        table.put(IArchimatePackage.eINSTANCE.getProduct(),
                ",ou,ou,ou,ou,ou,ou,ou,ou,ou,ago,cgosu,gou,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,gou,ao,o,o,o,o,o,o,o,o");

        // 13 Business Service
        table.put(IArchimatePackage.eINSTANCE.getBusinessService(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,cfgostu,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,fotu,ao,o,o,o,o,o,o,o,o");

        // 14 Value
        table.put(IArchimatePackage.eINSTANCE.getValue(),
                ",o,o,o,o,o,o,o,o,o,o,o,o,cgos,o,o,o," +
                "o,o,o,o,o,o,o,o,o,o,o,o,o,o,o");

        // 15 Business Object
        table.put(IArchimatePackage.eINSTANCE.getBusinessObject(),
                ",o,o,o,o,o,o,o,o,o,cgos,o,o,o,cgos,o,o," +
                "o,o,o,o,o,o,o,o,o,o,o,o,o,o,o");

        // 16 Representation
        table.put(IArchimatePackage.eINSTANCE.getRepresentation(),
                ",o,o,o,o,o,o,o,o,o,or,o,o,o,or,cgos,o," +
                "o,o,o,o,o,o,o,o,o,o,o,o,o,o,o");

        // 17 Meaning
        table.put(IArchimatePackage.eINSTANCE.getMeaning(),
                ",o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,cgos," +
                "o,o,o,o,o,o,o,o,o,o,o,o,o,o,o");
        
        // 18 Application Collaboration
        table.put(IArchimatePackage.eINSTANCE.getApplicationCollaboration(),
                "ft,iou,iou,iou,iou,fotu,fotu,fotu,fotu,iou,ao,oru,ioru,o,ao,ao,o," +
                "cfgostu,cfgostu,iou,iou,cfgotu,ioru,ao,o,o,o,o,o,o,o,o");

        // 19 Application Component
        table.put(IArchimatePackage.eINSTANCE.getApplicationComponent(),
                "ft,iou,iou,ou,iou,fotu,fotu,fotu,fotu,iou,ao,oru,ioru,o,ao,ao,o," +
                "cfgostu,cfgostu,iou,ou,cfgotu,ioru,ao,o,o,o,o,o,o,o,o");

        // 20 Application Function
        table.put(IArchimatePackage.eINSTANCE.getApplicationFunction(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
                "ou,ou,cfgostu,fotu,ou,oru,ao,o,o,o,o,o,o,o,o");

        // 21 Application Interaction
        table.put(IArchimatePackage.eINSTANCE.getApplicationInteraction(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
                "ou,ou,fotu,cfgostu,ou,oru,ao,o,o,o,o,o,o,o,o");

        // 22 Application Interface
        table.put(IArchimatePackage.eINSTANCE.getApplicationInterface(),
                "ft,ou,ou,ou,ou,fotu,fotu,fotu,fotu,ou,ao,ou,iou,o,ao,ao,o," +
                "fotu,fotu,ou,ou,cfgostu,iou,ao,o,o,o,o,o,o,o,o");

        // 23 Application Service
        table.put(IArchimatePackage.eINSTANCE.getApplicationService(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,fotu,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,cfgostu,ao,o,o,o,o,o,o,o,o");

        // 24 Data Object
        table.put(IArchimatePackage.eINSTANCE.getDataObject(),
                ",o,o,o,o,o,o,o,o,o,or,o,o,o,or,o,o," +
                "o,o,o,o,o,o,cgos,o,o,o,o,o,o,o,o");

        // 25 Artifact
        table.put(IArchimatePackage.eINSTANCE.getArtifact(),
                ",o,o,o,o,o,o,o,o,o,aor,or,o,o,aor,ao,o," +
                "oru,oru,oru,oru,oru,oru,aor,cgos,o,o,o,o,o,o,o");

        // 26 Communication Path
        table.put(IArchimatePackage.eINSTANCE.getCommunicationPath(),
                ",o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o," +
                "o,o,o,o,o,o,o,o,cgos,o,o,o,o,o,o");

        // 27 Device
        table.put(IArchimatePackage.eINSTANCE.getDevice(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
              //"ou,ou,ou,ou,ou,ou,ao,aiou,ioru,cfgostu,cfgostu,cfgotu,iou,ioru,cfgiostu"); // original as per spec
                "ou,oru,ou,ou,ou,ou,aor,aiou,ioru,cfgostu,cfgostu,cfgotu,iou,ioru,cfgiostu");

        // 28 Node
        table.put(IArchimatePackage.eINSTANCE.getNode(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
              //"ou,ou,ou,ou,ou,ou,ao,aiou,ioru,cfgostu,cfgostu,cfgotu,iou,ioru,cfgiostu"); // original as per spec
                "ou,oru,ou,ou,ou,ou,aor,aiou,ioru,cfgostu,cfgostu,cfgotu,iou,ioru,cfgiostu");

        // 29 Infrastructure Interface
        table.put(IArchimatePackage.eINSTANCE.getInfrastructureInterface(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,ou,ao,aou,ou,fotu,fotu,cfgostu,ou,iou,fotu");

        // 30 Network
        table.put(IArchimatePackage.eINSTANCE.getNetwork(),
                ",o,o,o,o,o,o,o,o,o,o,o,o,o,o,ao,o," +
                "o,o,o,o,o,o,o,o,or,o,o,o,cgos,o,o");

        // 31 Infrastructure Service
        table.put(IArchimatePackage.eINSTANCE.getInfrastructureService(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
                "ou,ou,ou,ou,ou,ou,ao,aou,ou,ou,ou,ou,ou,cfgostu,ou");

        // 32 System Software
        table.put(IArchimatePackage.eINSTANCE.getSystemSoftware(),
                "ft,ou,ou,ou,ou,ou,ou,ou,ou,ou,ao,ou,ou,o,ao,ao,o," +
              //"ou,ou,ou,ou,ou,ou,ao,aiou,ioru,cfgostu,cfgostu,cfgotu,iou,ioru,cfgiostu"); // original as per spec
                "ou,oru,ou,ou,ou,ou,aor,aiou,ioru,cfgostu,cfgostu,cfgotu,iou,ioru,cfgiostu");
    }
    
    /**
     * @param sourceElement
     * @param relationshipID
     * @return True if sourceElement is allowed to have a start relationship
     */
    public static final boolean isValidRelationshipStart(IArchimateElement sourceElement, EClass relationshipType) {
        String keyLetter = keyLetters.get(relationshipType);
        if(keyLetter == null) {
            System.err.println("Tried to get keyLetter for " + relationshipType);
            return false;
        }
        
        EClass eClass = getSuperEClass(sourceElement.eClass());
        
        // List of allowed relationships
        String allowed = table.get(eClass);
        if(allowed == null) {
            return false;
        }
        
        return allowed.contains(keyLetter);
    }
    
    public static final boolean isValidRelationship(IArchimateElement sourceElement, IArchimateElement targetElement, EClass relationshipType) {
        return isValidRelationship(sourceElement.eClass(), targetElement.eClass(), relationshipType);
    }

    public static final boolean isValidRelationship(EClass sourceType, EClass targetType, EClass relationshipType) {
        if(relationshipType == null) {
            return false;
        }
        
        if(!IArchimatePackage.eINSTANCE.getRelationship().isSuperTypeOf(relationshipType)) {
            return false;
        }
        
        String keyLetter = keyLetters.get(relationshipType);
        if(keyLetter == null) {
            System.err.println("Tried to get keyLetter for " + relationshipType);
            return false;
        }
        
        sourceType = getSuperEClass(sourceType);
        targetType = getSuperEClass(targetType);
        
        // List of allowed relationships for source
        String allowed = table.get(sourceType);
        if(allowed == null) {
            return false;
        }
        
        // Get position of target class in table
        int targetPos = 0;
        for(Object key : table.keySet()) {
            if(key.equals(targetType)) {
                break;
            }
            targetPos++;
        }
        
        if(targetPos >= table.size()) {
            System.err.println("targetPos was " + targetPos + " in ArchimateModelUtils.isValidRelationship()");
            return false;
        }
        
        String[] sections = allowed.split(",");
        String section = sections[targetPos];
        return section.contains(keyLetter);
    }
    
    /**
     * @param element
     * @return The actual super EClass of an EClass
     */
    private static EClass getSuperEClass(EClass eClass) {
        // Use one super class type for Junctions
        if(IArchimatePackage.eINSTANCE.getJunctionElement().isSuperTypeOf(eClass)) {
            return IArchimatePackage.eINSTANCE.getJunction();
        }
        else {
            return eClass;
        }
    }
    
    /**
     * @param sourceElement
     * @param targetElement
     * @return All valid relationship types between sourceElement and targetElement
     */
    public static EClass[] getValidRelationships(IArchimateElement sourceElement, IArchimateElement targetElement) {
        return getValidRelationships(sourceElement.eClass(), targetElement.eClass());
    }
    
    /**
     * @param sourceType
     * @param targetType
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
    
    private static final int SOURCE_RELATIONSHIPS = 1;
    private static final int TARGET_RELATIONSHIPS = 1 << 1;
    
    /**
     * @param element
     * @return A list of all relationships that element has, both as target and as source
     */
    public static List<IRelationship> getRelationships(IArchimateElement element) {
        return __getRelationships(element, SOURCE_RELATIONSHIPS | TARGET_RELATIONSHIPS);
    }
    
    /**
     * @param element
     * @return A list of all source relationships that element has
     */
    public static List<IRelationship> getSourceRelationships(IArchimateElement element) {
        return __getRelationships(element, SOURCE_RELATIONSHIPS);
    }
    
    /**
     * @param element
     * @return A list of all target relationships that element has
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
                    if(relationship.getSource() == element) {
                        relationships.add(relationship);
                    }
                }
                if((type & TARGET_RELATIONSHIPS) != 0) {
                    if(relationship.getTarget() == element) {
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
     * @param model
     * @param id
     * @return An Object in model given its ID
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
     * @return A list of EClass in the Business layer
     */
    public static EClass[] getBusinessClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getBusinessActor(),
                IArchimatePackage.eINSTANCE.getBusinessRole(),
                IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
                IArchimatePackage.eINSTANCE.getBusinessInterface(),
                IArchimatePackage.eINSTANCE.getBusinessFunction(),
                IArchimatePackage.eINSTANCE.getBusinessProcess(),
                IArchimatePackage.eINSTANCE.getBusinessActivity(),
                IArchimatePackage.eINSTANCE.getBusinessEvent(),
                IArchimatePackage.eINSTANCE.getBusinessInteraction(),
                IArchimatePackage.eINSTANCE.getProduct(),
                IArchimatePackage.eINSTANCE.getContract(),
                IArchimatePackage.eINSTANCE.getBusinessService(),
                IArchimatePackage.eINSTANCE.getValue(),
                IArchimatePackage.eINSTANCE.getMeaning(),
                IArchimatePackage.eINSTANCE.getRepresentation(),
                IArchimatePackage.eINSTANCE.getBusinessObject()
        };
    }
    
    /**
     * @return A list of EClass in the Application layer in preferred order
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
     * @return A list of EClass in the Technology layer in preferred order
     */
    public static EClass[] getTechnologyClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getArtifact(),
                IArchimatePackage.eINSTANCE.getCommunicationPath(),
                IArchimatePackage.eINSTANCE.getNetwork(),
                IArchimatePackage.eINSTANCE.getInfrastructureInterface(),
                IArchimatePackage.eINSTANCE.getInfrastructureService(),
                IArchimatePackage.eINSTANCE.getNode(),
                IArchimatePackage.eINSTANCE.getSystemSoftware(),
                IArchimatePackage.eINSTANCE.getDevice()
        };
    }

    
    /**
     * @return A list of EClass for Relations in preferred order
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
                IArchimatePackage.eINSTANCE.getAssociationRelationship()
        };
    }

    /**
     * @return A list of EClass for Connectors in preferred order
     */
    public static EClass[] getConnectorClasses() {
        return new EClass[] {
                IArchimatePackage.eINSTANCE.getJunction(),
                IArchimatePackage.eINSTANCE.getAndJunction(),
                IArchimatePackage.eINSTANCE.getOrJunction()
        };
    }
}
