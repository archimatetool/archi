/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import uk.ac.bolton.archimate.editor.actions.AbstractModelSelectionAction;


/**
 * Abstract Delegate Action that updates depending on the currently selected Model.
 * 
 * Use this for plugins.
 * 
 * We have to implement IActionDelegate2 so that we get an init(IAction action) which is called
 * because plugins implements IStartup
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractModelSelectionDelegateAction extends AbstractModelSelectionAction
implements IWorkbenchWindowActionDelegate, IActionDelegate2 {
    
    /**
     * The workbench proxy action provided by Eclipse
     */
    protected IAction fProxyAction;

    @Override
    public void init(IAction action) {
        fProxyAction = action;
        fProxyAction.setEnabled(false);
    }

    public void init(IWorkbenchWindow window) {
        setWorkbenchWindow(window);
        
        // Update enabled state on current active part (if any)
        partActivated(window.getPartService().getActivePart());
    }

    @Override
    protected void updateState() {
        if(fProxyAction != null) {
            fProxyAction.setEnabled(getActiveArchimateModel() != null);
        }
    }

    public void run(IAction action) {
        run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void runWithEvent(IAction action, Event event) {
        run();
    }
}
