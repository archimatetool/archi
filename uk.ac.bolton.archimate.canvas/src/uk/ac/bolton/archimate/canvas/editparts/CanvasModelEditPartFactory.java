/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import uk.ac.bolton.archimate.canvas.model.ICanvasModel;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelBlock;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelConnection;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelSticky;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.DiagramImageEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.EmptyEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.LineConnectionEditPart;
import uk.ac.bolton.archimate.model.IDiagramModelImage;
import uk.ac.bolton.archimate.model.IDiagramModelReference;

/**
 * Factory for creating Edit Parts based on graphical domain model objects
 * 
 * @author Phillip Beauvoir
 */
public class CanvasModelEditPartFactory
implements EditPartFactory {
    
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart child = null;
        
        if(model == null) {
            return null;
        }
        
        // Main Diagram Edit Part
        if(model instanceof ICanvasModel) {
            child = new CanvasDiagramPart();
        }
        
        // Block
        else if(model instanceof ICanvasModelBlock) {
            child = new CanvasBlockEditPart();
        }
        
        // Image
        else if(model instanceof IDiagramModelImage) {
            child = new DiagramImageEditPart();
        }

        // Sticky
        else if(model instanceof ICanvasModelSticky) {
            child = new CanvasStickyEditPart();
        }
        
        // Diagram Model Reference
        else if(model instanceof IDiagramModelReference) {
            child = new DiagramModelReferenceEditPart();
        }
        
        // Connections
        else if(model instanceof ICanvasModelConnection) {
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
