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
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ui.factory.ElementUIFactory;
import uk.ac.bolton.archimate.editor.ui.factory.IElementUIProvider;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelImage;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.ISketchModel;


/**
 * Main Label Provider for Archimate Editor
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
            return ""; //$NON-NLS-1$
        }
        
        String name = null;
        
        // Get Name
        if(element instanceof INameable) {
            name = ((INameable)element).getName();
        }
        
        // It's blank. Can we get a default name from its class?
        if(!StringUtils.isSet(name) && element instanceof EObject) {
            name = getDefaultName(((EObject)element).eClass());
        }
        
        // Yes
        if(StringUtils.isSet(name)) {
            return name;
        }
        
        // Defaults for empty strings
        if(element instanceof IArchimateDiagramModel) {
            return Messages.ArchimateLabelProvider_0;
        }
        else if(element instanceof ISketchModel) {
            return Messages.ArchimateLabelProvider_1;
        }
        else if(element instanceof IDiagramModelImage) {
            return Messages.ArchimateLabelProvider_2;
        }
       
        // If it's blank try registered extensions
        if(!StringUtils.isSet(name)) {
            name = LabelProviderExtensionHandler.INSTANCE.getLabel(element);
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
            image = getEClassImage((EClass)element);
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
     * @return An Image for an EObject instance
     */
    private Image getObjectImage(EObject eObject) {
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eObject.eClass());
        if(provider != null) {
            return provider.getImage(eObject);
        }
        
        return getEClassImage(eObject.eClass());
    }
    
    /**
     * @return An Image for an EClass
     */
    private Image getEClassImage(EClass eClass) {
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getImage();
        }
        
        String imageName = getImageName(eClass);
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
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getImageDescriptor();
        }
        
        String imageName = getImageName(eClass);
        if(imageName != null) {
            return IArchimateImages.ImageFactory.getImageDescriptor(imageName);
        }
        
        return null;
    }
    
    private String getImageName(EClass eClass) {
        switch(eClass.getClassifierID()) {
            // Other
            case IArchimatePackage.ARCHIMATE_MODEL:
                return IArchimateImages.ICON_MODELS_16;
            case IArchimatePackage.ARCHIMATE_DIAGRAM_MODEL:
                return IArchimateImages.ICON_DIAGRAM_16;
            case IArchimatePackage.FOLDER:
                return IArchimateImages.ECLIPSE_IMAGE_FOLDER;
            
            // Sketch
            case IArchimatePackage.SKETCH_MODEL:
                return IArchimateImages.ICON_SKETCH_16;
        }
        
        return null;
    }
    
    /**
     * Get a default human-readable name for an EClass
     * @param eClass The Class
     * @return A name or null
     */
    public String getDefaultName(EClass eClass) {
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getDefaultName();
        }
        
        return ""; //$NON-NLS-1$
    }
    
    /**
     * Get a default human-readable short name for an EClass
     * @param eClass The Class
     * @return A name or null
     */
    public String getDefaultShortName(EClass eClass) {
        IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
        if(provider != null) {
            return provider.getDefaultShortName();
        }
        
        return ""; //$NON-NLS-1$
    }

    /**
     * @param relation
     * @return A sentence that describes the relationship between the relationship's source and target elements
     */
    public String getRelationshipSentence(IRelationship relation) {
        if(relation != null) {
            if(relation.getSource() != null && relation.getTarget() != null) {
                String nameSource = ArchimateLabelProvider.INSTANCE.getLabel(relation.getSource());
                String nameTarget = ArchimateLabelProvider.INSTANCE.getLabel(relation.getTarget());
                
                switch(relation.eClass().getClassifierID()) {
                    case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_3, nameSource, nameTarget);

                    case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_4, nameSource, nameTarget);

                    case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_5, nameSource, nameTarget);

                    case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_6, nameSource, nameTarget);

                    case IArchimatePackage.FLOW_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_7, nameSource, nameTarget);

                    case IArchimatePackage.ACCESS_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_8, nameSource, nameTarget);

                    case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_9, nameSource, nameTarget);

                    case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_10, nameSource, nameTarget);

                    case IArchimatePackage.REALISATION_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_11, nameSource, nameTarget);

                    case IArchimatePackage.USED_BY_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_12, nameSource, nameTarget);

                    case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                        return NLS.bind(Messages.ArchimateLabelProvider_13, nameSource, nameTarget);

                    default:
                        return ""; //$NON-NLS-1$
                }
            }
        }
        
        return ""; //$NON-NLS-1$
    }

}
