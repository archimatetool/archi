/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.archimatetool.editor.diagram.editparts.diagram.EmptyEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.LineConnectionEditPart;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;


/**
 * Factory for creating Edit Parts based on graphical domain model objects
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditPartFactory
implements EditPartFactory {
    
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart child = null;
        
        if(model == null) {
            return null;
        }
        
        // Main Diagram Edit Part
        if(model instanceof ISketchModel) {
            child = new SketchDiagramPart();
        }
        
        // Actor
        else if(model instanceof ISketchModelActor) {
            child = new SketchActorEditPart();
        }
        
        // Sticky
        else if(model instanceof ISketchModelSticky) {
            child = new StickyEditPart();
        }
        
        // Diagram Model Reference
        else if(model instanceof IDiagramModelReference) {
            child = new SketchDiagramModelReferenceEditPart();
        }
        
        // Group
        else if(model instanceof IDiagramModelGroup) {
            child = new SketchGroupEditPart();
        }
        
        // Connections
        else if(model instanceof IDiagramModelConnection) {
            child = new LineConnectionEditPart();
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
