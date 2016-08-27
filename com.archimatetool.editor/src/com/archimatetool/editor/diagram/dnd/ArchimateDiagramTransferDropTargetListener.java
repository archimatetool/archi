/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.dnd.DND;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModel;


/**
 * Archimate Diagram Native DnD listener
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramTransferDropTargetListener extends AbstractDiagramTransferDropTargetListener {
    
    public static final String ADD_ELEMENT_CONNECTIONS = "add_element_connections"; //$NON-NLS-1$

    public ArchimateDiagramTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void updateTargetRequest(){
        super.updateTargetRequest();
        
        // If user holds down Copy Key then don't add connections to elements
        if(getCurrentEvent().detail == DND.DROP_COPY) {
            getNativeDropRequest().getExtendedData().put(ADD_ELEMENT_CONNECTIONS, false);
        }
        else {
            getNativeDropRequest().getExtendedData().put(ADD_ELEMENT_CONNECTIONS, true);
            getCurrentEvent().detail = DND.DROP_LINK; // Show link cursor
        }
    }
    
    @Override
    protected boolean isEnabled(Object element) {
        // Archimate Concept
        if(element instanceof IArchimateConcept) {
            return true;
        }
        // Diagram Model Reference
        else if(element instanceof IDiagramModel) {
            // Allowed, but not on the target diagram model
            if(element != getTargetDiagramModel()) {
                return true;
            }
        }
        
        return false;
    }
}
