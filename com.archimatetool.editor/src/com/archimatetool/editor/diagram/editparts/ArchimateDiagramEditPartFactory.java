/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.editparts.diagram.EmptyEditPart;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;


/**
 * Factory for creating Edit Parts based on graphical domain model objects
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditPartFactory
implements EditPartFactory {

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        if(model == null) {
            return null;
        }

        EditPart child = null;
        IObjectUIProvider provider = null;
        
        if(model instanceof EObject) {
            provider = ObjectUIFactory.INSTANCE.getProvider(((EObject)model));
        }
        
        // We have a provider
        if(provider != null) {
            child = provider.createEditPart();
        }
        
        /*
         * It's better to return an Empty Edit Part in case of a corrupt model.
         * Returning null is disastrous and means the Diagram View won't open.
         */
        if(child == null) {
            Logger.logError("Could not create EditPart for: " + model); //$NON-NLS-1$
            child = new EmptyEditPart();
        }

        // Set the Model in the Edit part
        child.setModel(model);
        
        return child;
    }

}
