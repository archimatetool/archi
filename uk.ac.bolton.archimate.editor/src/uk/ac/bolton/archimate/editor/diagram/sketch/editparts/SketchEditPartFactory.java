/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import uk.ac.bolton.archimate.editor.diagram.editparts.DiagramPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.EmptyEditPart;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.ISketchModelActor;
import uk.ac.bolton.archimate.model.ISketchModelSticky;

/**
 * Factory for creating Edit Parts based on graphical domain model objects
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditPartFactory
implements EditPartFactory {
    
    public static final String CONNECTION_LINE = null;
    public static final String CONNECTION_ARROW = "sketch_arrow";
    public static final String CONNECTION_DASHED_ARROW = "sketch_dashed_arrow";

    public EditPart createEditPart(EditPart context, Object model) {
        EditPart child = null;
        
        if(model == null) {
            return null;
        }
        
        // Main Diagram Edit Part
        if(model instanceof IDiagramModel) {
            child = new DiagramPart();
        }
        
        // Actor
        else if(model instanceof ISketchModelActor) {
            child = new SketchActorEditPart();
        }
        
        // Sticky
        else if(model instanceof ISketchModelSticky) {
            child = new StickyEditPart();
        }
        
        // Connections
        else if(model instanceof IDiagramModelConnection) {
            IDiagramModelConnection connection = (IDiagramModelConnection)model;
            String type = connection.getType();
            if(CONNECTION_ARROW.equals(type)) {
                child = new ArrowConnectionEditPart();
            }
            else if(CONNECTION_DASHED_ARROW.equals(type)) {
                child = new DashedArrowConnectionEditPart();
            }
            else {
                child = new LineConnectionEditPart();
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
