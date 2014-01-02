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

    public Image getImage(Object element) {
        if(!(element instanceof IStructuredSelection)) {
            return null;
        }
        
        element = ((IStructuredSelection)element).getFirstElement();

        if(element instanceof EditPart) {
            element = ((EditPart)element).getModel();
        }

        // Archimate Element 
        if(element instanceof IDiagramModelArchimateObject) {
            element = ((IDiagramModelArchimateObject)element).getArchimateElement();
        }

        // Archimate Relationship
        if(element instanceof IDiagramModelArchimateConnection) {
            element = ((IDiagramModelArchimateConnection)element).getRelationship();
        }

        return ArchimateLabelProvider.INSTANCE.getImage(element);
    }

    public String getText(Object element) {
        if(!(element instanceof IStructuredSelection)) {
            return ""; //$NON-NLS-1$
        }
        
        element = ((IStructuredSelection)element).getFirstElement();
        
        // Archimate Element
        if(element instanceof IArchimateElement) {
            return getArchimateElementText((IArchimateElement)element);
        }
        else if(element instanceof IAdaptable) {
            IArchimateElement archimateElement = (IArchimateElement)((IAdaptable)element).getAdapter(IArchimateElement.class);
            if(archimateElement != null) {
                return getArchimateElementText(archimateElement);
            }
        }

        // Other Diagram Edit Part, so get model object
        if(element instanceof EditPart) {
            element = ((EditPart)element).getModel();
        }
        
        // Check the main label provider
        String text = ArchimateLabelProvider.INSTANCE.getLabel(element);
        if(StringUtils.isSet(text)) {
            return StringUtils.escapeAmpersandsInText(text);
        }
        
        return " "; // Ensure the title bar is displayed //$NON-NLS-1$
    }

    private String getArchimateElementText(IArchimateElement element) {
        String name = StringUtils.escapeAmpersandsInText(element.getName());
        
        String typeName = ArchimateLabelProvider.INSTANCE.getDefaultName(element.eClass());
        
        if(name.length() > 0) {
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
