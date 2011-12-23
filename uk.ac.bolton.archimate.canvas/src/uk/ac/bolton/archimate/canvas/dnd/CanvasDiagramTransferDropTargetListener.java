/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.dnd.DND;

import uk.ac.bolton.archimate.editor.diagram.dnd.AbstractDiagramTransferDropTargetListener;
import uk.ac.bolton.archimate.model.IDiagramModel;

/**
 * Native DnD listener for Canvas View
 * 
 * @author Phillip Beauvoir
 */
public class CanvasDiagramTransferDropTargetListener extends AbstractDiagramTransferDropTargetListener {

    public CanvasDiagramTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer);
    }

    @Override
    protected void updateTargetRequest() {
        super.updateTargetRequest();
        getCurrentEvent().detail = DND.DROP_LINK; // Show link cursor
    }

    @Override
    protected boolean isEnabled(Object element) {
        // Only allow (other) Diagram Model References
        return (element instanceof IDiagramModel) && (element != getTargetDiagramModel());
    }
}
