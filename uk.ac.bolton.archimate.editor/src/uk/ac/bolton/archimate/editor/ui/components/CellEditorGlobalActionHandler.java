/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.components;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;


/**
 * Manages de-activating and restoring of editing type global Action Handlers setting them to null
 * so that they cannot be invoked by the user when editing text in a text cell editor.<p>
 * This ensures that edit key shortcuts are bound to the cell editor, not the application. 
 * 
 * @author Phillip Beauvoir
 */
public class CellEditorGlobalActionHandler {

    private IActionBars fActionBars;
    
    // These are the global actions to set to null
    private String[] actionIds = new String[] {
            ActionFactory.CUT.getId(),
            ActionFactory.COPY.getId(),
            ActionFactory.PASTE.getId(),
            ActionFactory.DELETE.getId(),
            ActionFactory.SELECT_ALL.getId(),
            ActionFactory.FIND.getId(),
            ActionFactory.RENAME.getId(),
            ActionFactory.UNDO.getId(),
            ActionFactory.REDO.getId()
    };
    
    private IAction[] actions = new IAction[actionIds.length];

    public CellEditorGlobalActionHandler(IActionBars actionbars) {
        fActionBars = actionbars;
        
        saveActions();
        nullActions();
    }
    
    /**
     * Restore the Global Action Handlers that were set to null
     */
    public void dispose() {
        restoreActions();
    }

    private void saveActions() {
        for(int i = 0; i < actionIds.length; i++) {
            actions[i] = fActionBars.getGlobalActionHandler(actionIds[i]);
        }
    }
    
    private void nullActions() {
        for(int i = 0; i < actionIds.length; i++) {
            fActionBars.setGlobalActionHandler(actionIds[i], null);
        }
        
        fActionBars.updateActionBars();
    }
    
    private void restoreActions() {
        for(int i = 0; i < actionIds.length; i++) {
            fActionBars.setGlobalActionHandler(actionIds[i], actions[i]);
        }
        
        fActionBars.updateActionBars();
    }
}
