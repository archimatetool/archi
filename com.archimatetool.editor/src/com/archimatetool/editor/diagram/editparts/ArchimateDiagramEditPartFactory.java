/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.archimatetool.editor.diagram.editparts.diagram.EmptyEditPart;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.factory.ElementUIFactory;
import com.archimatetool.editor.ui.factory.IElementUIProvider;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Factory for creating Edit Parts based on graphical domain model objects
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditPartFactory
implements EditPartFactory {

    public EditPart createEditPart(EditPart context, Object model) {
        if(model == null) {
            return null;
        }

        EditPart child = null;
        IElementUIProvider provider = null;
        
        // Archimate Model Element Parts
        if(model instanceof IDiagramModelArchimateObject) {
            provider = ElementUIFactory.INSTANCE.getProvider(((IDiagramModelArchimateObject)model).getArchimateElement().eClass());
        }
        
        // Archimate Connection Model Element Parts
        else if(model instanceof IDiagramModelArchimateConnection) {
            provider = ElementUIFactory.INSTANCE.getProvider(((IDiagramModelArchimateConnection)model).getRelationship().eClass());
        }
        
        // Other
        else if(model instanceof EObject) {
            provider = ElementUIFactory.INSTANCE.getProvider(((EObject)model).eClass());
        }
        
        // We have a provider
        if(provider != null) {
            child = provider.createEditPart();
            child.setModel(model);
            return child;
        }

        // Main Archimate Diagram Edit Part
        if(model instanceof IArchimateDiagramModel) {
            child = new ArchimateDiagramPart();

            // Add a Nested Connection Filter to this
            ((ArchimateDiagramPart)child).addEditPartFilter(new NestedConnectionEditPartFilter());
            
            // Add a Viewpoint Child EditPart Filter to this if set in Preferences (hides rather than ghosts)
            if(Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS)) {
                ((ArchimateDiagramPart)child).addEditPartFilter(new ViewpointEditPartFilter());
            }
        }
        
        /*
         * It's better to return an Empty Edit Part in case of a corrupt model.
         * Returning null is disastrous and means the Diagram View won't open.
         */
        else {
            child = new EmptyEditPart();
        }
        
        // Set the Model in the Edit part
        child.setModel(model);
        
        return child;
    }

}
