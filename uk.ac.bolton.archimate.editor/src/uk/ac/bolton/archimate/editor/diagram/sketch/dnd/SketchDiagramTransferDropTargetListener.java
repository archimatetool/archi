/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.dnd;

import org.eclipse.gef.EditPartViewer;

import uk.ac.bolton.archimate.editor.diagram.dnd.DiagramTransferDropTargetListener;
import uk.ac.bolton.archimate.model.IDiagramModel;

/**
 * Native DnD listener for Sketch View
 * 
 * @author Phillip Beauvoir
 */
public class SketchDiagramTransferDropTargetListener extends DiagramTransferDropTargetListener {

    public SketchDiagramTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer);
    }

    @Override
    protected boolean isEnabled(Object element) {
        // Only allow Diagram Model Reference
        if(element instanceof IDiagramModel) {
            return super.isEnabled(element);
        }
        
        return false;
    }
}
