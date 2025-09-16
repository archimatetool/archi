/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.Adapters;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateRelationship;



/**
 * Properties Label Provider
 * 
 * @author Phillip Beauvoir
 */
public class PropertiesLabelProvider implements ILabelProvider {

    @Override
    public Image getImage(Object object) {
        if(!(object instanceof IStructuredSelection selection)) {
            return null;
        }
        
        // If we have selected more than one object check if they are all the same type
        if(selection.size() > 1) {
            Object[] objects = selection.toArray();
            for(int i = 0; i < objects.length - 1; i++) {
                // Different
                if(getImageFromLabelProvider(objects[i]) != getImageFromLabelProvider(objects[i+1])) {
                    return null;
                }
            }
        }
        
        return getImageFromLabelProvider(selection.getFirstElement());
    }
    
    /**
     * Get the image from the ArchiLabelProvider or, failing that, any registered adapters
     */
    protected Image getImageFromLabelProvider(Object object) {
        Image image = ArchiLabelProvider.INSTANCE.getImage(object);
        if(image != null) {
            return image;
        }
        
        // Try registered adapters
        ILabelProvider labelProvider = Adapters.adapt(object, ILabelProvider.class);
        return labelProvider != null ? labelProvider.getImage(object) : null;
    }

    @Override
    public String getText(Object object) {
        if(!(object instanceof IStructuredSelection selection)) {
            return " "; //$NON-NLS-1$
        }
        
        if(selection.size() > 1) {
            return Messages.PropertiesLabelProvider_0;
        }
        
        // An Archimate Concept is a special text so get adaptable object to check for IArchimateConcept
        if(ArchiLabelProvider.INSTANCE.getAdaptableObject(selection.getFirstElement()) instanceof IArchimateConcept concept) {
            return getArchimateConceptText(concept);
        }

        return getTextFromLabelProvider(selection.getFirstElement());
    }
    
    /**
     * Get the text from the ArchiLabelProvider or, failing that, any registered adapters
     */
    protected String getTextFromLabelProvider(Object object) {
        String text = ArchiLabelProvider.INSTANCE.getLabel(object);
        if(StringUtils.isSet(text)) {
            return normalise(text);
        }
        
        // Try registered adapters
        ILabelProvider labelProvider = Adapters.adapt(object, ILabelProvider.class);
        if(labelProvider != null) {
            text = labelProvider.getText(object);
            if(StringUtils.isSet(text)) {
                return normalise(text);
            }
        }
        
        return " "; // Ensure the title bar is displayed //$NON-NLS-1$
    }

    protected String getArchimateConceptText(IArchimateConcept concept) {
        String text = ""; //$NON-NLS-1$
        String name = normalise(concept.getName());
        String typeName = ArchiLabelProvider.INSTANCE.getDefaultName(concept.eClass());
        
        if(StringUtils.isSet(name)) {
            text = name + " (" + typeName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else {
            text = typeName;
        }
        
        if(concept instanceof IArchimateRelationship relationship) {
            text += " ("; //$NON-NLS-1$
            text += normalise(ArchiLabelProvider.INSTANCE.getLabel(relationship.getSource()));
            text += " - "; //$NON-NLS-1$
            text += normalise(ArchiLabelProvider.INSTANCE.getLabel(relationship.getTarget()));
            text += ")"; //$NON-NLS-1$
        }
        
        return text;
    }
    
    /**
     * Remove ampersands as well as newlines
     */
    protected String normalise(String text) {
        return StringUtils.normaliseNewLineCharacters(StringUtils.escapeAmpersandsInText(text));
    }
    
    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

}
