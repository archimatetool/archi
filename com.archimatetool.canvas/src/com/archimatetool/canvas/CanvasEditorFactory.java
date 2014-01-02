/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.ui.IEditorInput;

import com.archimatetool.canvas.editparts.CanvasModelEditPartFactory;
import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.editor.diagram.DiagramEditorInput;
import com.archimatetool.editor.diagram.IDiagramEditorFactory;
import com.archimatetool.model.IDiagramModel;




/**
 * CanvasEditorFactory
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorFactory implements IDiagramEditorFactory {

    public CanvasEditorFactory() {
    }

    @Override
    public String getEditorID() {
        return ICanvasEditor.ID;
    }

    @Override
    public IEditorInput createEditorInput(IDiagramModel model) {
        return new DiagramEditorInput(model);
    }

    @Override
    public boolean isFactoryFor(IDiagramModel model) {
        return model instanceof ICanvasModel;
    }

    @Override
    public EditPartFactory createEditPartFactory() {
        return new CanvasModelEditPartFactory();
    }

}
