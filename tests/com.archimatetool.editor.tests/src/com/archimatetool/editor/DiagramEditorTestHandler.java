/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;

import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelObject;



/**
 * A Test handler to manage an IDiagramModelEditor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorTestHandler {
    
    private IDiagramModelEditor editor;
    private IDiagramModel dm;

    public DiagramEditorTestHandler(IDiagramModel dm) {
        this.dm = dm;
    }
    
    /**
     * Open the Editor in the workbench.
     * If the Editor is already open, return the reference to it.
     */
    public IDiagramModelEditor openEditor() {
        if(editor == null) {
            editor = EditorManager.openDiagramEditor(dm);
        }
        
        return editor;
    }

    /**
     * Return the wrapped Editor.
     * If the Editor has not been opened, returns null
     */
    public IDiagramModelEditor getEditor() {
        return editor;
    }

    /**
     * Find a GraphicalEditPart in the Editor's Viewer from the model object
     * @param modelObject The diagram model object
     * @return The editpart or null if not found
     */
    public GraphicalEditPart findEditPart(IDiagramModelObject modelObject) {
        return (GraphicalEditPart)editor.getGraphicalViewer().getEditPartRegistry().get(modelObject);
    }

    /**
     * Find a Figure in an EditPart in the Editor's Viewer from the model object
     * @param modelObject The diagram model object
     * @return The figure or null if not found
     */
    public IFigure findFigure(IDiagramModelObject modelObject) {
        GraphicalEditPart editPart = findEditPart(modelObject);
        return editPart == null ? null : editPart.getFigure();
    }
    

}
