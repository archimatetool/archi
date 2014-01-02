/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.dnd;

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.swt.dnd.DropTargetEvent;

import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.diagram.IDiagramModelEditor;



/**
 * This Drop Target Listener is used when dragging a Pallete Entry onto a Graphical Viewer.
 * We have to make sure that we are dragging the correct (template) entry from the correct Palette.
 * It is possible to re-arrange the Eclipse Viewers side-by side and drag from one Viewer's
 * Palette to another (different) Viewer causing a NPE because the EditPartFactory is trying to
 * create the wrong type of part.
 * 
 * @author Phillip Beauvoir
 */
public class PaletteTemplateTransferDropTargetListener extends TemplateTransferDropTargetListener {
    
    private IDiagramModelEditor fEditor;

    public PaletteTemplateTransferDropTargetListener(IDiagramModelEditor editor) {
        super(editor.getGraphicalViewer());
        fEditor = editor;
    }

    @Override
    public boolean isEnabled(DropTargetEvent event) {
        ICreationFactory factory = (ICreationFactory)getFactory(TemplateTransfer.getInstance().getTemplate());
        if(factory != null && !factory.isUsedFor(fEditor)) {
            return false;
        }
        return super.isEnabled(event);
    }
}
