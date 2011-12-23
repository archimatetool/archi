/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.ui.IEditorInput;

import uk.ac.bolton.archimate.canvas.editparts.CanvasModelEditPartFactory;
import uk.ac.bolton.archimate.canvas.model.ICanvasModel;
import uk.ac.bolton.archimate.editor.diagram.DiagramEditorInput;
import uk.ac.bolton.archimate.editor.diagram.IDiagramEditorFactory;
import uk.ac.bolton.archimate.model.IDiagramModel;



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
