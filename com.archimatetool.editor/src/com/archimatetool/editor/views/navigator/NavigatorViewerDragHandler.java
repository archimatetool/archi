/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.navigator;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;



/**
 * Navigator Drag Handler
 * 
 * @author Phillip Beauvoir
 */
public class NavigatorViewerDragHandler {

    private StructuredViewer fViewer;
    
    /**
     * Drag operations we support
     */
    private int fDragOperations = DND.DROP_COPY | DND.DROP_MOVE; 

    // Can only drag local type
    Transfer[] sourceTransferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
    
    public NavigatorViewerDragHandler(StructuredViewer viewer) {
        fViewer = viewer;
        registerDragSupport();
    }
    
    /**
     * Register drag support that starts from the Tree
     */
    private void registerDragSupport() {
        fViewer.addDragSupport(fDragOperations, sourceTransferTypes, new DragSourceListener() {
            
            @Override
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }

            @Override
            public void dragSetData(DragSourceEvent event) {
                // For consistency set the data to the selection even though
                // the selection is provided by the LocalSelectionTransfer
                // to the drop target adapter.
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }

            @Override
            public void dragStart(DragSourceEvent event) {
                // Drag started from the Tree
                LocalSelectionTransfer.getTransfer().setSelection(fViewer.getSelection());
                event.doit = true;
            }
        });
    }
    
}
