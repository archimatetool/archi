/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.cheatsheets;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;



/**
 * Action to programmatically create a Map View
 * 
 * @author Phillip Beauvoir
 */
public class CreateMapViewCheatSheetAction
extends Action
implements ICheatSheetAction {
    
    public void run(String[] params, ICheatSheetManager manager) {
        IViewPart viewPart = ViewManager.showViewPart(ITreeModelView.ID, true);
        if(viewPart == null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), Messages.CreateMapViewCheatSheetAction_0,
                    Messages.CreateMapViewCheatSheetAction_1);
            return;
        }
        
        IArchimateModel model = (IArchimateModel)viewPart.getAdapter(IArchimateModel.class);
        if(model == null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), Messages.CreateMapViewCheatSheetAction_2,
                    Messages.CreateMapViewCheatSheetAction_3);
            return;
        }
        
        EList<IDiagramModel> diagramModels = model.getDiagramModels();
        if(diagramModels.size() < 2) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), Messages.CreateMapViewCheatSheetAction_4,
                    Messages.CreateMapViewCheatSheetAction_5);
            return;
        }
        
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            IArchimateDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
            diagramModel.setName(Messages.CreateMapViewCheatSheetAction_6);
            
            int y = 20; 
            
            for(IDiagramModel dm : diagramModels) {
                IDiagramModelReference ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
                ref.setReferencedModel(dm);
                ref.setBounds(20, y, 400, 100);
                diagramModel.getChildren().add(ref);
                y += 120;
            }
            
            IFolder folder = model.getDefaultFolderForElement(diagramModel);
            
            stack.execute(new NewViewCommand(folder, diagramModel));
        }
    }
    
    private static class NewViewCommand extends Command {
        IFolder fParent;
        IDiagramModel fDiagramModel;
        
        NewViewCommand(IFolder parent, IDiagramModel model) {
            super(Messages.CreateMapViewCheatSheetAction_7);
            fParent = parent;
            fDiagramModel = model;
        }
        
        @Override
        public void execute() {
            fParent.getElements().add(0, fDiagramModel);
            EditorManager.openDiagramEditor(fDiagramModel);
        }
        
        @Override
        public void undo() {
            // Close Editor FIRST!
            EditorManager.closeDiagramEditor(fDiagramModel);
            fParent.getElements().remove(fDiagramModel);
        }
    }
    
}
