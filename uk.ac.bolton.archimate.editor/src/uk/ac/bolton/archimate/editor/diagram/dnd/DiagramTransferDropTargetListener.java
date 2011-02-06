/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IDiagramModel;

/**
 * Native DnD listener
 * 
 * @author Phillip Beauvoir
 */
public class DiagramTransferDropTargetListener extends AbstractTransferDropTargetListener {

    public DiagramTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer, LocalSelectionTransfer.getTransfer());
    }

    @Override
    protected void updateTargetRequest(){
        getNativeDropRequest().setData(getCurrentEvent().data);
        getNativeDropRequest().setDropLocation(getDropLocation());
    }
    
    @Override
    protected Request createTargetRequest() {
        return new NativeDropRequest();
    }

    protected NativeDropRequest getNativeDropRequest() {
        return (NativeDropRequest)getTargetRequest();
    }
    
    @Override
    public boolean isEnabled(DropTargetEvent event) {
        // Iterate thru the selection and see if we have the required type for a drop
        ISelection selection =  LocalSelectionTransfer.getTransfer().getSelection();
        if(selection instanceof IStructuredSelection) {
            IDiagramModel targetDiagramModel = (IDiagramModel)getViewer().getContents().getModel();
            IArchimateModel targetArchimateModel = targetDiagramModel.getArchimateModel();
            
            // Firstly check they are from the same model
            for(Object object : ((IStructuredSelection)selection).toArray()) {
                if(object instanceof IArchimateModelElement) {
                    IArchimateModel sourceArchimateModel = ((IArchimateModelElement)object).getArchimateModel();
                    if(sourceArchimateModel != targetArchimateModel) {
                        return false;
                    }
                }
            }
            
            for(Object object : ((IStructuredSelection)selection).toArray()) {
                // Archimate elements
                if(object instanceof IArchimateElement) {
                    IArchimateElement element = (IArchimateElement)object;
                    
                    // Ensure we have at least one that is not already on the target diagram
                    if(!DiagramModelUtils.isElementReferencedInDiagram(targetDiagramModel, element)) {
                        return super.isEnabled(event);
                    }
                }
                // Diagram models
                else if(object instanceof IDiagramModel) {
                    // Ensure we have at least one that is not already on the target diagram
                    if(object != targetDiagramModel) {
                        return super.isEnabled(event);
                    }
                }
            }
        }
        
        return false;
    }
    
    @Override
    protected void handleDragOver() {
        getCurrentEvent().detail = DND.DROP_COPY;
        super.handleDragOver();
    }
    
}
