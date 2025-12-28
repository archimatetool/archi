/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.cheatsheets;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
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
    
    @Override
    public void run(String[] params, ICheatSheetManager manager) {
        IViewPart viewPart = ViewManager.showViewPart(ITreeModelView.ID, true);
        if(viewPart == null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), Messages.CreateMapViewCheatSheetAction_0,
                    Messages.CreateMapViewCheatSheetAction_1);
            return;
        }
        
        IArchimateModel model = viewPart.getAdapter(IArchimateModel.class);
        if(model == null) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), Messages.CreateMapViewCheatSheetAction_2,
                    Messages.CreateMapViewCheatSheetAction_3);
            return;
        }
        
        List<IDiagramModel> diagramModels = model.getDiagramModels();
        if(diagramModels.isEmpty()) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(), Messages.CreateMapViewCheatSheetAction_4,
                    Messages.CreateMapViewCheatSheetAction_5);
            return;
        }
        
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            stack.execute(new NewMapViewCommand(model));
        }
    }
    
    static class NewMapViewCommand extends Command {
        IArchimateModel model;
        IFolder parentFolder;
        IDiagramModel diagramModel;
        
        NewMapViewCommand(IArchimateModel model) {
            super(Messages.CreateMapViewCheatSheetAction_7);
            this.model = model;
        }
        
        @Override
        public void execute() {
            createMapView(model);
            EditorManager.openDiagramEditor(diagramModel);
        }
        
        @Override
        public void undo() {
            // Close Editor FIRST!
            EditorManager.closeDiagramEditor(diagramModel);
            parentFolder.getElements().remove(diagramModel);
        }
        
        @Override
        public void redo() {
            parentFolder.getElements().add(diagramModel);
            EditorManager.openDiagramEditor(diagramModel);
        }
        
        private void createMapView(IArchimateModel smodel) {
            // Create a new DiagramModel
            diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
            diagramModel.setName(Messages.CreateMapViewCheatSheetAction_6);
            
            // Add all diagram model references...
            
            final int width = 320;
            final int height = 100;
            
            int count = 0;
            int xPos = 20;
            int yPos = 20;
            
            for(IDiagramModel dm : model.getDiagramModels()) {
                IDiagramModelReference ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
                ref.setReferencedModel(dm);
                ref.setBounds(xPos, yPos, width, height);
                ref.setGradient(ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.DEFAULT_GRADIENT));
                ColorFactory.setDefaultColors(ref);
                
                diagramModel.getChildren().add(ref);

                if(++count % 100 == 0) {
                    xPos += width + 20;
                    yPos = 20;
                }
                else {
                    yPos += height + 20;
                }
            }
            
            // Add to folder afterwards
            parentFolder = model.getDefaultFolderForObject(diagramModel);
            parentFolder.getElements().add(diagramModel);
        }
        
        @Override
        public void dispose() {
            model = null;
            parentFolder = null;
            diagramModel = null;
        }
    }
    
}
