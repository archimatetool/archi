/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.LabelProviderExtensionHandler;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;


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
            return "";
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
        
        // Check our label provider
        String text = ArchimateLabelProvider.INSTANCE.getLabel(element);
        if(StringUtils.isSet(text)) {
            return escapeText(text);
        }
        
        // Check registered label providers
        text = LabelProviderExtensionHandler.INSTANCE.getLabel(element);
        if(StringUtils.isSet(text)) {
            return escapeText(text);
        }

        return " "; // Ensure the title bar is displayed
    }

    private String getArchimateElementText(IArchimateElement element) {
        String name = escapeText(element.getName());
        
        String typeName = ArchimateNames.getDefaultName(element.eClass());
        
        if(name.length() > 0) {
            return name + " (" + typeName + ")";
        }
        
        return typeName;
    }
    
    private String escapeText(String text) {
        if(StringUtils.isSet(text)) {
            // Ampersands need to be doubled or they don't show
            return text.replaceAll("&", "&&");
        }
        return text;
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
