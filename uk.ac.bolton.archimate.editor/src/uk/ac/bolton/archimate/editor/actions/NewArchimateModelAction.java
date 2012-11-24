/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.services.EditorManager;
import uk.ac.bolton.archimate.editor.ui.services.UIRequestManager;
import uk.ac.bolton.archimate.editor.views.tree.TreeEditElementRequest;
import uk.ac.bolton.archimate.model.IArchimateModel;

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
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NEW_FILE_16));
        setText(Messages.NewArchimateModelAction_0);
        setToolTipText(Messages.NewArchimateModelAction_1);
        setId("uk.ac.bolton.archimate.editor.action.newModel"); //$NON-NLS-1$
        setActionDefinitionId(getId()); // register key binding
    }
    
    @Override
    public void run() {
        // Create new Model
        IArchimateModel model = IEditorModelManager.INSTANCE.createNewModel();
        
        // Open Diagram Editor
        EditorManager.openDiagramEditor(model.getDefaultDiagramModel());
        
        // Edit in-place in Tree
        UIRequestManager.INSTANCE.fireRequest(new TreeEditElementRequest(this, model));
    }

    public void dispose() {
    } 
}