/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.model.IArchimateModel;


/**
 * New ArchiMate Model Action Invoked from Welcome screen
 * 
 * @author Phillip Beauvoir
 */
public class WelcomeNewArchimateModelAction extends Action {
    
    public WelcomeNewArchimateModelAction() {
    }
    
    @Override
    public void run() {
        // Close the intro part first
        PlatformUI.getWorkbench().getIntroManager().closeIntro(PlatformUI.getWorkbench().getIntroManager().getIntro());

        // Create a new Model
        IArchimateModel model = IEditorModelManager.INSTANCE.createNewModel();
        
        // Because of a native JVM crash that can happen on Mac when quitting Archi we async this
        Display.getCurrent().asyncExec(() -> {
            // Open Diagram Editor
            EditorManager.openDiagramEditor(model.getDefaultDiagramModel(), false);
            
            // Edit model name in-place in Tree
            UIRequestManager.INSTANCE.fireRequest(new TreeEditElementRequest(this, model));
        });
    }
}