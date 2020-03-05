/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.actions.ArchiActionFactory;


/**
 * Manages de-activating and restoring of Global Action Handlers setting them to null
 * so that they cannot be invoked by the user when editing text in a text type control.<p>
 * This ensures that edit key shortcuts are bound to the control in focus, not the global application.
 * 
 * @author Phillip Beauvoir
 */
public class GlobalActionDisablementHandler {

    // These are the global savedActions to set to null
    private static final String[] actionIds = new String[] {
            ActionFactory.CUT.getId(),
            ActionFactory.COPY.getId(),
            ActionFactory.PASTE.getId(),
            ArchiActionFactory.PASTE_SPECIAL.getId(),
            ActionFactory.DELETE.getId(),
            ActionFactory.SELECT_ALL.getId(),
            ActionFactory.FIND.getId(),
            ActionFactory.RENAME.getId(),
            ActionFactory.UNDO.getId(),
            ActionFactory.REDO.getId()
    };
    
    private Map<String, IAction> savedActions = new HashMap<String, IAction>();
    private IActionBars actionBars;
    
    
    /**
     * Will attempt to use the Action Bars for the Active Workbench part in focus
     */
    public GlobalActionDisablementHandler() {
        // Get Action Bars from Active Part
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if(window != null && window.getActivePage() != null && window.getActivePage().getActivePart() != null) {
            IWorkbenchPartSite site = window.getActivePage().getActivePart().getSite();
            if(site instanceof IEditorSite) {
                actionBars = ((IEditorSite)site).getActionBars();
            }
            else if(site instanceof IViewSite) {
                actionBars = ((IViewSite)site).getActionBars();
            }
        }
    }
    
    /**
     * Uses given Action Bars
     * @param actionBars
     */
    public GlobalActionDisablementHandler(IActionBars actionBars) {
        this.actionBars = actionBars;
    }

    /**
     * Update the action bars
     */
    public void update() {
        if(actionBars != null) {
            actionBars.updateActionBars();
        }
    }
    
    /**
     * Clear the Global Action Handlers for the Active Part
     */
    public void clearGlobalActions() {
        if(actionBars != null) {
            for(String id : actionIds) {
                IAction action = actionBars.getGlobalActionHandler(id);
                if(action != null) {
                    savedActions.put(id, action); // save action to restore it
                    actionBars.setGlobalActionHandler(id, null); // null it
                }
            }
            
            // Update
            actionBars.updateActionBars();
        }
    }
    
    /**
     * Restore the Global Action Handlers that were set to null
     */
    public void restoreGlobalActions() {
        if(actionBars != null) {
            // Restore Actions
            try {
                for(Entry<String, IAction> entry : savedActions.entrySet()) {
                    actionBars.setGlobalActionHandler(entry.getKey(), entry.getValue());
                }
                
                savedActions.clear();
                
                // Update
                actionBars.updateActionBars();
            }
            catch(NullPointerException ex) {
                // Catch this because in Archi 4.5 and later (Eclipse 4.11 and later) if we are editing
                // in a DirectEditManager and close the app we get a NPE in Eclipse
                // this just stops the NPE written to the log
            }
        }
    }
}
