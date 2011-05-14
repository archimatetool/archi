/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.help.cheatsheets;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

import uk.ac.bolton.archimate.editor.ui.services.EditorManager;
import uk.ac.bolton.archimate.editor.ui.services.ViewManager;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.IFolder;


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
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Create Map View",
                    "Could not open the Model Tree View.");
            return;
        }
        
        IArchimateModel model = (IArchimateModel)viewPart.getAdapter(IArchimateModel.class);
        if(model == null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Create Map View",
                    "Could not locate a model in the tree.\n\nPlease select one and try again.");
            return;
        }
        
        EList<IDiagramModel> diagramModels = model.getDiagramModels();
        if(diagramModels.size() < 2) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Create Map View",
                    "There are not enough views in the model. There should be at least two.");
            return;
        }
        
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createDiagramModel();
            diagramModel.setName("Map View");
            
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
            super("Create Map View");
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
