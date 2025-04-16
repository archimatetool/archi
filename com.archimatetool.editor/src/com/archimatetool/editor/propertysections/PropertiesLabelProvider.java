/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModelObject;
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
                Object object1 = getAdaptable(objects[i]);
                Object object2 = getAdaptable(objects[i+1]);
                // Different
                if(ArchiLabelProvider.INSTANCE.getImage(object1) != ArchiLabelProvider.INSTANCE.getImage(object2)) {
                    return null;
                }
            }
        }
        
        Object firstSelected = getAdaptable(selection.getFirstElement());
        return ArchiLabelProvider.INSTANCE.getImage(firstSelected);
    }

    @Override
    public String getText(Object object) {
        if(!(object instanceof IStructuredSelection selection)) {
            return " "; //$NON-NLS-1$
        }
        
        if(selection.size() > 1) {
            return Messages.PropertiesLabelProvider_0;
        }
        
        Object firstSelected = getAdaptable(selection.getFirstElement());
        
        firstSelected = ArchiLabelProvider.INSTANCE.getWrappedElement(firstSelected);
        
        // An Archimate Concept is a special text
        if(firstSelected instanceof IArchimateConcept concept) {
            return getArchimateConceptText(concept);
        }

        // Check the main label provider
        String text = ArchiLabelProvider.INSTANCE.getLabel(firstSelected);
        if(StringUtils.isSet(text)) {
            return normalise(text);
        }
        
        return " "; // Ensure the title bar is displayed //$NON-NLS-1$
    }
    
    String getArchimateConceptText(IArchimateConcept concept) {
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
     * Return the underlying adaptable type if there is one
     * Some Classes like AbstractIssueType (in the Model Checker) will use this to return the right type
     */
    private Object getAdaptable(Object object) {
        return object instanceof IAdaptable adaptable ? adaptable.getAdapter(IArchimateModelObject.class) : object;
    }
    
    /**
     * Remove ampersands as well as newlines
     */
    private String normalise(String text) {
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
