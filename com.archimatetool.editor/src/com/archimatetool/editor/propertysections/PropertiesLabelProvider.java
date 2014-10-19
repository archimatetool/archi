/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Properties Label Provider
 * 
 * @author Phillip Beauvoir
 */
public class PropertiesLabelProvider implements ILabelProvider {

    public Image getImage(Object object) {
        if(!(object instanceof IStructuredSelection)) {
            return null;
        }
        
        object = ((IStructuredSelection)object).getFirstElement();
        
        object = getWrappedElement(object);

        if(object instanceof IAdaptable) {
            object = ((IAdaptable)object).getAdapter(object.getClass());
        }

        return ArchimateLabelProvider.INSTANCE.getImage(object);
    }

    public String getText(Object object) {
        if(!(object instanceof IStructuredSelection)) {
            return " "; //$NON-NLS-1$
        }
        
        object = ((IStructuredSelection)object).getFirstElement();
        
        object = getWrappedElement(object);
        
        if(object instanceof IAdaptable) {
            object = ((IAdaptable)object).getAdapter(object.getClass());
        }

        // An Archimate Element is a special text
        if(object instanceof IArchimateElement) {
            return getArchimateElementText((IArchimateElement)object);
        }

        // Check the main label provider
        String text = ArchimateLabelProvider.INSTANCE.getLabel(object);
        if(StringUtils.isSet(text)) {
            return StringUtils.escapeAmpersandsInText(text);
        }
        
        return " "; // Ensure the title bar is displayed //$NON-NLS-1$
    }
    
    private Object getWrappedElement(Object object) {
        if(object instanceof EditPart) {
            object = ((EditPart)object).getModel();
        }
        
        if(object instanceof IDiagramModelArchimateObject) {
            return ((IDiagramModelArchimateObject)object).getArchimateElement();
        }
        
        if(object instanceof IDiagramModelArchimateConnection) {
            return ((IDiagramModelArchimateConnection)object).getRelationship();
        }
        
        return object;
    }

    String getArchimateElementText(IArchimateElement element) {
        String name = StringUtils.escapeAmpersandsInText(element.getName());
        
        String typeName = ArchimateLabelProvider.INSTANCE.getDefaultName(element.eClass());
        
        if(StringUtils.isSet(name)) {
            return name + " (" + typeName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return typeName;
    }
    
    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }

}
