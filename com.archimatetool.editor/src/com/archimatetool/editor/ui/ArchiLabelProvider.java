/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.factory.IDiagramModelUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.INameable;



/**
 * Main Label Provider for Archi
 * 
 * @author Phillip Beauvoir
 */
public class ArchiLabelProvider {
    
    public static ArchiLabelProvider INSTANCE = new ArchiLabelProvider();

    private Set<IArchiLabelProvider> providers = new HashSet<>();
    
    private ArchiLabelProvider() {
        // Register any label providers
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(IArchiLabelProvider.EXTENSIONPOINT_ID)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                IArchiLabelProvider provider = (IArchiLabelProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && provider != null) {
                    providers.add(provider);
                }
            } 
            catch(CoreException ex) {
                Logger.logError("Cannot register Provider", ex); //$NON-NLS-1$
                ex.printStackTrace();
            } 
        }
    }
    
    /**
     * @return A name for an object.<br>
     *         This will be the element's name if of type INameable or a default name as specified in its ElementUIProvider.
     */
    public String getLabel(Object object) {
        if(object == null) {
            return ""; //$NON-NLS-1$
        }
        
        object = getWrappedElement(object);
        
        String name = null;
        
        // Get Name
        if(object instanceof INameable) {
            name = ((INameable)object).getName();
        }
        
        // It's blank. Get a default name from its eClass
        if(!StringUtils.isSet(name) && object instanceof EObject) {
            name = getDefaultName(((EObject)object).eClass());
        }
        
        // Try registered providers
        if(name == null) {
            for(IArchiLabelProvider provider : providers) {
                name = provider.getLabel(object);
                if(name != null) {
                    break;
                }
            }
        }
        
        return StringUtils.safeString(name);
    }

    /**
     * @return A name for an object.<br>
     *         This will be the element's name if of type INameable or a default name as specified in its ElementUIProvider.
     *         All newline instances will be replaced by a single space
     */
    public String getLabelNormalised(Object object) {
        return StringUtils.normaliseNewLineCharacters(getLabel(object));
    }
    
    /**
     * Get a default human-readable name for an EClass
     * @param eClass The Class
     * @return A name or null
     */
    public String getDefaultName(EClass eClass) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
        if(provider != null) {
            return provider.getDefaultName();
        }
        
        return ""; //$NON-NLS-1$
    }
    
    /**
     * @param element
     * @return An image for an object
     */
    public Image getImage(Object object) {
        if(object == null) {
            return null;
        }
        
        object = getWrappedElement(object);
        
        // This first, since EClass is an EObject
        if(object instanceof EClass) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProviderForClass((EClass)object);
            if(provider != null) {
                return provider.getImage();
            }
        }
        else if(object instanceof EObject) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider((EObject)object);
            if(provider != null) {
                return provider.getImage();
            }
        }
        
        // Try providers
        for(IArchiLabelProvider provider : providers) {
            Image image = provider.getImage(object);
            if(image != null) {
                return image;
            }
        }
        
        return null;
    }
    
    /**
     * @param eClass
     * @return An ImageDescriptor for an EClass
     */
    public ImageDescriptor getImageDescriptor(EClass eClass) {
        if(eClass == null) {
            return null;
        }
        
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
        if(provider != null) {
            return provider.getImageDescriptor();
        }
        
        return null;
    }
    
    /**
     * @param element
     * @return A IGraphicsIcon for a Diagram Model
     */
    public IGraphicsIcon getGraphicsIconForDiagramModel(IDiagramModel dm) {
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(dm);
        if(provider instanceof IDiagramModelUIProvider) {
            return ((IDiagramModelUIProvider)provider).getGraphicsIcon();
        }
        
        return null;
    }
    
    /**
     * If the wrapper is an EditPart get the EditPart's model first
     * If then the wrapper is an IDiagramModelArchimateObject return the IArchimateElement
     * If then the wrapper is an IDiagramModelArchimateConnection return the IRelationship
     * Else return the object itself
     * @param object The wrapper objects
     * @return The actual model element in an object
     */
    public Object getWrappedElement(Object object) {
        if(object instanceof EditPart) {
            object = ((EditPart)object).getModel();
        }
        
        if(object instanceof IDiagramModelArchimateComponent) {
            return ((IDiagramModelArchimateComponent)object).getArchimateConcept();
        }
        
        return object;
    }
    
    /**
     * @param relation
     * @return A sentence that describes the relationship between the relation's source and target elements
     */
    public String getRelationshipSentence(IArchimateRelationship relation) {
        if(relation != null && relation.getSource() != null && relation.getTarget() != null) {
            return getRelationshipSentence(relation.eClass(), relation.getSource(), relation.getTarget());
        }
        
        return ""; //$NON-NLS-1$
    }

    /**
     * @param relation
     * @return A sentence that describes the relationship between the relation's source and target elements
     */
    public String getRelationshipSentence(EClass relationshipType, IArchimateConcept source, IArchimateConcept target) {
        String nameSource = getLabel(source);
        String nameTarget = getLabel(target);
        
        // If it's a Junction then do something special
        if(source instanceof IJunction || target instanceof IJunction) {
            return NLS.bind(Messages.ArchiLabelProvider_0, nameSource, nameTarget);
        }

        switch(relationshipType.getClassifierID()) {
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_3, nameSource, nameTarget);

            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_4, nameSource, nameTarget);

            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_5, nameSource, nameTarget);

            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_6, nameSource, nameTarget);

            case IArchimatePackage.FLOW_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_7, nameSource, nameTarget);

            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_8, nameSource, nameTarget);

            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_9, nameSource, nameTarget);

            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_10, nameSource, nameTarget);

            case IArchimatePackage.REALIZATION_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_11, nameSource, nameTarget);

            case IArchimatePackage.SERVING_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_12, nameSource, nameTarget);

            case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                return NLS.bind(Messages.ArchiLabelProvider_13, nameSource, nameTarget);

            default:
                return ""; //$NON-NLS-1$
        }
    }

    /**
     * @param eClass The Relationship class
     * @param reverseDirection If this is true then the phrase is from target to source
     * @return A phrase that describes the relationship, for example "Is Realised by", "Flows to"
     */
    public String getRelationshipPhrase(EClass eClass, boolean reverseDirection) {
        if(eClass == null) {
            return ""; //$NON-NLS-1$
        }
        
        switch(eClass.getClassifierID()) {
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_14 : Messages.ArchiLabelProvider_15;

            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_16 : Messages.ArchiLabelProvider_17;

            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_18 : Messages.ArchiLabelProvider_19;

            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_20 : Messages.ArchiLabelProvider_21;

            case IArchimatePackage.FLOW_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_22 : Messages.ArchiLabelProvider_23;

            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_24 : Messages.ArchiLabelProvider_25;

            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_26 : Messages.ArchiLabelProvider_27;

            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_28 : Messages.ArchiLabelProvider_29;

            case IArchimatePackage.REALIZATION_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_30 : Messages.ArchiLabelProvider_31;

            case IArchimatePackage.SERVING_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_32 : Messages.ArchiLabelProvider_33;

            case IArchimatePackage.INFLUENCE_RELATIONSHIP:
                return reverseDirection ? Messages.ArchiLabelProvider_34 : Messages.ArchiLabelProvider_35;

            default:
                return ""; //$NON-NLS-1$
        }
    }
}
