/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;

import uk.ac.bolton.archimate.editor.diagram.dnd.DiagramDropRequest;
import uk.ac.bolton.archimate.model.IDiagramModel;

/**
 * File DnD listener
 * 
 * @author Phillip Beauvoir
 */
public class FileTransferDropTargetListener extends AbstractTransferDropTargetListener {
    
    public static String FILE_TRANSFER_REQUEST = "filetransfer";

    public FileTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer, FileTransfer.getInstance());
    }

    @Override
    protected void updateTargetRequest(){
        getNativeDropRequest().setData(getCurrentEvent().data);
        getNativeDropRequest().setDropLocation(getDropLocation());
    }
    
    @Override
    protected Request createTargetRequest() {
        return new DiagramDropRequest(FileTransfer.getInstance());
    }

    protected DiagramDropRequest getNativeDropRequest() {
        return (DiagramDropRequest)getTargetRequest();
    }
    
    /**
     * @return The Target Diagram Model
     */
    protected IDiagramModel getTargetDiagramModel() {
        return (IDiagramModel)getViewer().getContents().getModel();
    }
    
    @Override
    protected void handleDragOver() {
        getCurrentEvent().detail = DND.DROP_COPY;
        super.handleDragOver();
    }
    
}
