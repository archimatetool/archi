/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.model.IArchimateModel;


/**
 * New ArchiMate Model Action
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelAction
extends Action
implements IWorkbenchAction
{
    
    public NewArchimateModelAction() {
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NEW_FILE));
        setText(Messages.NewArchimateModelAction_0);
        setToolTipText(Messages.NewArchimateModelAction_1);
        setId("com.archimatetool.editor.action.newModel"); //$NON-NLS-1$
        setActionDefinitionId(getId()); // register key binding
    }
    
    @Override
    public void run() {
        // Create new Model
        IArchimateModel model = IEditorModelManager.INSTANCE.createNewModel();
        
        // Open Diagram Editor
        EditorManager.openDiagramEditor(model.getDefaultDiagramModel(), false);
        
        // Edit in-place in Tree
        UIRequestManager.INSTANCE.fireRequestAsync(new TreeEditElementRequest(this, model));
    }

    @Override
    public void dispose() {
    } 
}