/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.IInterfaceElement;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.ISketchModel;
import uk.ac.bolton.archimate.model.ISketchModelActor;
import uk.ac.bolton.archimate.model.ISketchModelSticky;


/**
 * Label Provider for Archimate Editor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateLabelProvider implements IEditorLabelProvider {
    
    public static ArchimateLabelProvider INSTANCE = new ArchimateLabelProvider();

    /**
     * @return A default name for an element.<br>
     *         This will be the element's name if of type INameable or the default Archimate name if an Archimate EObject,
     *         or a default name if blank.
     */
    @Override
    public String getLabel(Object element) {
        if(element == null) {
            return "";
        }
        
        String name = null;
        
        // Get Name
        if(element instanceof INameable) {
            name = ((INameable)element).getName();
        }
        
        // Blank. Is it an Archimate object?
        if(!StringUtils.isSet(name) && element instanceof EObject) {
            name = ArchimateNames.getDefaultName(((EObject)element).eClass());
        }
        
        if(StringUtils.isSet(name)) {
            return name;
        }
        
        // Defaults for empty strings
        if(element instanceof IDiagramModelConnection) {
            return "Connection";
        }
        else if(element instanceof IArchimateDiagramModel) {
            return "View";
        }
        else if(element instanceof IDiagramModelNote) {
            return "Note";
        }
        else if(element instanceof IDiagramModelGroup) {
            return "Group";
        }
        else if(element instanceof IDiagramModelReference) {
            return "View Reference";
        }
        else if(element instanceof ISketchModel) {
            return "Sketch";
        }
        else if(element instanceof ISketchModelActor) {
            return "Actor";
        }
        else if(element instanceof ISketchModelSticky) {
            return "Sticky";
        }
       
        return StringUtils.safeString(name);
    }

    @Override
    public Image getImage(Object element) {
        if(element == null) {
            return null;
        }
        
        Image image = null;
        
        // This first, since EClass is an EObject
        if(element instanceof EClass) {
            image = getEClassImage((EClass)element, null);
        }
        else if(element instanceof EObject) {
            image = getObjectImage(((EObject)element));
        }
        
        // Try registered extensions
        if(image == null) {
            image = LabelProviderExtensionHandler.INSTANCE.getImage(element);
        }
        
        return image;
    }
    
    /**
     * @return An Image for an EObject
     */
    private Image getObjectImage(EObject eObject) {
        Object type = null;
        
        // Interface Element Types
        if(eObject instanceof IInterfaceElement) {
            type = ((IInterfaceElement)eObject).getInterfaceType();
        }
        
        return getEClassImage(eObject.eClass(), type);
    }
    
    /**
     * @return An Image for an EClass
     */
    private Image getEClassImage(EClass eClass, Object type) {
        String imageName = getImageName(eClass, type);
        if(imageName != null) {
            return IArchimateImages.ImageFactory.getImage(imageName);
        }
        
        return null;
    }

    /**
     * @param eClass
     * @return An ImageDescriptor for an EClass
     */
    public ImageDescriptor getImageDescriptor(EClass eClass) {
        String imageName = getImageName(eClass, null);
        if(imageName != null) {
            return IArchimateImages.ImageFactory.getImageDescriptor(imageName);
        }
        
        return null;
    }
    
    private String getImageName(EClass eClass, Object type) {
        switch(eClass.getClassifierID()) {
            // Business
            case IArchimatePackage.BUSINESS_ACTIVITY:
                return IArchimateImages.ICON_BUSINESS_ACTIVITY_16;
            case IArchimatePackage.BUSINESS_ACTOR:
                return IArchimateImages.ICON_BUSINESS_ACTOR_16;
            case IArchimatePackage.BUSINESS_COLLABORATION:
                return IArchimateImages.ICON_BUSINESS_COLLABORATION_16;
            case IArchimatePackage.CONTRACT:
                return IArchimateImages.ICON_BUSINESS_CONTRACT_16;
            case IArchimatePackage.BUSINESS_EVENT:
                return IArchimateImages.ICON_BUSINESS_EVENT_16;
            case IArchimatePackage.BUSINESS_FUNCTION:
                return IArchimateImages.ICON_BUSINESS_FUNCTION_16;
            case IArchimatePackage.BUSINESS_INTERACTION:
                return IArchimateImages.ICON_BUSINESS_INTERACTION_16;
            case IArchimatePackage.BUSINESS_INTERFACE:
                if(type != null && type.equals(IInterfaceElement.REQUIRED)) {
                    return IArchimateImages.ICON_INTERFACE_REQUIRED_16;
                }
                return IArchimateImages.ICON_BUSINESS_INTERFACE_16;
            case IArchimatePackage.MEANING:
                return IArchimateImages.ICON_BUSINESS_MEANING_16;
            case IArchimatePackage.BUSINESS_OBJECT:
                return IArchimateImages.ICON_BUSINESS_OBJECT_16;
            case IArchimatePackage.BUSINESS_PROCESS:
                return IArchimateImages.ICON_BUSINESS_PROCESS_16;
            case IArchimatePackage.PRODUCT:
                return IArchimateImages.ICON_BUSINESS_PRODUCT_16;
            case IArchimatePackage.REPRESENTATION:
                return IArchimateImages.ICON_BUSINESS_REPRESENTATION_16;
            case IArchimatePackage.BUSINESS_ROLE:
                return IArchimateImages.ICON_BUSINESS_ROLE_16;
            case IArchimatePackage.BUSINESS_SERVICE:
                return IArchimateImages.ICON_BUSINESS_SERVICE_16;
            case IArchimatePackage.VALUE:
                return IArchimateImages.ICON_BUSINESS_VALUE_16;
                
            // Application
            case IArchimatePackage.APPLICATION_COLLABORATION:
                return IArchimateImages.ICON_APPLICATION_COLLABORATION_16;
            case IArchimatePackage.APPLICATION_COMPONENT:
                return IArchimateImages.ICON_APPLICATION_COMPONENT_16;
            case IArchimatePackage.APPLICATION_FUNCTION:
                return IArchimateImages.ICON_APPLICATION_FUNCTION_16;
            case IArchimatePackage.APPLICATION_INTERACTION:
                return IArchimateImages.ICON_APPLICATION_INTERACTION_16;
            case IArchimatePackage.APPLICATION_INTERFACE:
                if(type != null && type.equals(IInterfaceElement.REQUIRED)) {
                    return IArchimateImages.ICON_INTERFACE_REQUIRED_16;
                }
                return IArchimateImages.ICON_APPLICATION_INTERFACE_16;
            case IArchimatePackage.DATA_OBJECT:
                return IArchimateImages.ICON_APPLICATION_DATA_OBJECT_16;
            case IArchimatePackage.APPLICATION_SERVICE:
                return IArchimateImages.ICON_APPLICATION_SERVICE_16;

            // Technology
            case IArchimatePackage.ARTIFACT:
                return IArchimateImages.ICON_TECHNOLOGY_ARTIFACT_16;
            case IArchimatePackage.COMMUNICATION_PATH:
                return IArchimateImages.ICON_TECHNOLOGY_COMMUNICATION_PATH_16;
            case IArchimatePackage.NETWORK:
                return IArchimateImages.ICON_TECHNOLOGY_NETWORK_16;
            case IArchimatePackage.INFRASTRUCTURE_INTERFACE:
                if(type != null && type.equals(IInterfaceElement.REQUIRED)) {
                    return IArchimateImages.ICON_INTERFACE_REQUIRED_16;
                }
                return IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_INTERFACE_16;
            case IArchimatePackage.INFRASTRUCTURE_SERVICE:
                return IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_SERVICE_16;
            case IArchimatePackage.NODE:
                return IArchimateImages.ICON_TECHNOLOGY_NODE_16;
            case IArchimatePackage.SYSTEM_SOFTWARE:
                return IArchimateImages.ICON_TECHNOLOGY_SYSTEM_SOFTWARE_16;
            case IArchimatePackage.DEVICE:
                return IArchimateImages.ICON_TECHNOLOGY_DEVICE_16;
                
            // Junctions
            case IArchimatePackage.JUNCTION:
                return IArchimateImages.ICON_JUNCTION_16;
            case IArchimatePackage.AND_JUNCTION:
                return IArchimateImages.ICON_JUNCTION_AND_16;
            case IArchimatePackage.OR_JUNCTION:
                return IArchimateImages.ICON_JUNCTION_OR_16;
                
            // Relationships
            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return IArchimateImages.ICON_ACESS_CONNECTION_16;
            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return IArchimateImages.ICON_AGGREGATION_CONNECTION_16;
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return IArchimateImages.ICON_ASSIGNMENT_CONNECTION_16;
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return IArchimateImages.ICON_ASSOCIATION_CONNECTION_16;
            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return IArchimateImages.ICON_COMPOSITION_CONNECTION_16;
            case IArchimatePackage.FLOW_RELATIONSHIP:
                return IArchimateImages.ICON_FLOW_CONNECTION_16;
            case IArchimatePackage.REALISATION_RELATIONSHIP:
                return IArchimateImages.ICON_REALISATION_CONNECTION_16;
            case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                return IArchimateImages.ICON_SPECIALISATION_CONNECTION_16;
            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return IArchimateImages.ICON_TRIGGERING_CONNECTION_16;
            case IArchimatePackage.USED_BY_RELATIONSHIP:
                return IArchimateImages.ICON_USED_BY_CONNECTION_16;
                
            // Other
            case IArchimatePackage.ARCHIMATE_MODEL:
                return IArchimateImages.ICON_MODELS_16;
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL:
            case IArchimatePackage.DIAGRAM_MODEL_REFERENCE:
                return IArchimateImages.ICON_DIAGRAM_16;
            case IArchimatePackage.DIAGRAM_MODEL_GROUP:
                return IArchimateImages.ICON_GROUP_16;
            case IArchimatePackage.DIAGRAM_MODEL_CONNECTION:
                return IArchimateImages.ICON_CONNECTION_PLAIN_16;
            case IArchimatePackage.FOLDER:
                return "folder";
            case IArchimatePackage.DIAGRAM_MODEL_NOTE:
                return IArchimateImages.ICON_NOTE_16;
            case IArchimatePackage.DIAGRAM_MODEL_IMAGE:
                return IArchimateImages.ICON_LANDSCAPE_16;
            
            // Sketch
            case IArchimatePackage.SKETCH_MODEL:
                return IArchimateImages.ICON_SKETCH_16;
            case IArchimatePackage.SKETCH_MODEL_ACTOR:
                return IArchimateImages.ICON_ACTOR_16;
            case IArchimatePackage.SKETCH_MODEL_STICKY:
                return IArchimateImages.ICON_STICKY_16;
        }
        
        return null;
    }
}
