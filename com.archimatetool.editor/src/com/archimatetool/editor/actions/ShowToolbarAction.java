/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IEvaluationService;


/**
 * Show / Hide toolbar
 * 
 * @author Phillip Beauvoir
 */
public class ShowToolbarAction extends Action {

    public ShowToolbarAction() {
        // Do this once the workbench is displayed
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                setText(isVisible() ? Messages.ShowToolbarAction_0 : Messages.ShowToolbarAction_1);
            }
        });
    }
    
    @Override
    public void run() {
        try {
            IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
            handlerService.executeCommand("org.eclipse.ui.ToggleCoolbarAction", null); //$NON-NLS-1$
            setText(isVisible() ? Messages.ShowToolbarAction_0 : Messages.ShowToolbarAction_1);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    };
    
    private boolean isVisible() {
        boolean isVisible = false;
        IEvaluationService service = PlatformUI.getWorkbench().getService(IEvaluationService.class);
        IEvaluationContext appState = service.getCurrentState();
        Object coolbar = appState.getVariable(ISources.ACTIVE_WORKBENCH_WINDOW_IS_COOLBAR_VISIBLE_NAME);
        if(coolbar instanceof Boolean) {
            isVisible = ((Boolean)coolbar).booleanValue();
        }
        return isVisible;
    }

}
