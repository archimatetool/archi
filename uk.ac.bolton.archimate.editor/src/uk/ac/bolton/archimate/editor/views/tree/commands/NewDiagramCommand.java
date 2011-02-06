/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.EditorManager;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;


/**
 * Add New Diagram Command
 * 
 * @author Phillip Beauvoir
 */
public class NewDiagramCommand extends Command {
    
    private IFolder fFolder;
    private IDiagramModel fDiagramModel;

    public NewDiagramCommand(IFolder folder, IDiagramModel diagramModel, String label) {
        super(label);
        fFolder = folder;
        fDiagramModel = diagramModel;
    }
    
    @Override
    public void execute() {
        fFolder.getElements().add(fDiagramModel);
        // Fire this event so the tree view can select the element
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                ITreeModelView.PROPERTY_MODEL_ELEMENT_NEW, null, fDiagramModel);
        
        // Open Editor
        EditorManager.openDiagramEditor(fDiagramModel);
    }
    
    @Override
    public void undo() {
        // Close Editor FIRST!
        EditorManager.closeDiagramEditor(fDiagramModel);

        fFolder.getElements().remove(fDiagramModel);
        
        // Fire this event so the tree view can select a parent node
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                ITreeModelView.PROPERTY_SELECTION_CHANGED, null, fFolder);
    }
    
    @Override
    public void dispose() {
        fFolder = null;
        fDiagramModel = null;
    }
}
