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



/**
 * Properties Label Provider
 * 
 * @author Phillip Beauvoir
 */
public class PropertiesLabelProvider implements ILabelProvider {

    @Override
    public Image getImage(Object object) {
        if(!(object instanceof IStructuredSelection)) {
            return null;
        }
        
        object = ((IStructuredSelection)object).getFirstElement();
        
        if(object instanceof IAdaptable) {
            object = ((IAdaptable)object).getAdapter(object.getClass());
        }
        
        return ArchiLabelProvider.INSTANCE.getImage(object);
    }

    @Override
    public String getText(Object object) {
        if(!(object instanceof IStructuredSelection)) {
            return " "; //$NON-NLS-1$
        }
        
        object = ((IStructuredSelection)object).getFirstElement();
        
        if(object instanceof IAdaptable) {
            object = ((IAdaptable)object).getAdapter(object.getClass());
        }

        object = ArchiLabelProvider.INSTANCE.getWrappedElement(object);
        
        // An Archimate Concept is a special text
        if(object instanceof IArchimateConcept) {
            return getArchimateConceptText((IArchimateConcept)object);
        }

        // Check the main label provider
        String text = ArchiLabelProvider.INSTANCE.getLabel(object);
        if(StringUtils.isSet(text)) {
            return StringUtils.escapeAmpersandsInText(text);
        }
        
        return " "; // Ensure the title bar is displayed //$NON-NLS-1$
    }
    
    String getArchimateConceptText(IArchimateConcept archimateConcept) {
        String name = StringUtils.escapeAmpersandsInText(archimateConcept.getName());
        
        String typeName = ArchiLabelProvider.INSTANCE.getDefaultName(archimateConcept.eClass());
        
        if(StringUtils.isSet(name)) {
            return name + " (" + typeName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return typeName;
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
