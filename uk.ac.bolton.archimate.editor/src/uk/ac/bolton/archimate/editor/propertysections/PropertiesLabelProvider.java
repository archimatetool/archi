/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.INameable;


/**
 * Properties Label Provider
 * 
 * @author Phillip Beauvoir
 */
public class PropertiesLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        if(element instanceof IStructuredSelection) {
            element = ((IStructuredSelection)element).getFirstElement();
            
            if(element instanceof EditPart) {
                element = ((EditPart)element).getModel();
            }
            
            if(element instanceof IDiagramModelArchimateObject) {
                element = ((IDiagramModelArchimateObject)element).getArchimateElement();
            }
            
            // This first before IDiagramModelConnection
            if(element instanceof IDiagramModelArchimateConnection) {
                element = ((IDiagramModelArchimateConnection)element).getRelationship();
            }
            
            if(element instanceof IDiagramModelConnection) {
                return ImageFactory.getImage(((EObject)element).eClass(), ((IDiagramModelConnection)element).getType());
            }
            
            if(element instanceof EObject) {
                return ImageFactory.getImage((EObject)element);
            }
        }
        
        return null;
    }

    public String getText(Object element) {
        String text = " ";
        IArchimateElement type = null;
        
        if(element instanceof IStructuredSelection) {
            element = ((IStructuredSelection)element).getFirstElement();
            
            if(element instanceof IArchimateElement) {
                type = (IArchimateElement)element;
            }
            else if(element instanceof IAdaptable) {
                type = (IArchimateElement)((IAdaptable)element).getAdapter(IArchimateElement.class);
            }

            if(element instanceof EditPart) {
                element = ((EditPart)element).getModel();
            }
            
            if(element instanceof INameable) {
                text = ((INameable)element).getName();
            }
        }
        
        if(!StringUtils.isSet(text)) {
            text = " "; // Stops white look
        }
        else {
            // These need to be doubled
            text = text.replaceAll("&", "&&");
        }
        
        // Element Type
        if(type != null) {
            text += " (" + ArchimateNames.getDefaultName(type.eClass()) + ")";
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
