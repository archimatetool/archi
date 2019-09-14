/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.ui.components.GlobalActionDisablementHandler;



/**
 * Direct Edit Manager that updates Global Action Handlers
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDirectEditManager extends DirectEditManager {
    
    private GlobalActionDisablementHandler fGlobalActionHandler;

    public AbstractDirectEditManager(GraphicalEditPart source, @SuppressWarnings("rawtypes") Class editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
    }

    @Override
    protected void initCellEditor() {
        // Hook into the global Action Handlers and null them
        fGlobalActionHandler = new GlobalActionDisablementHandler();
        fGlobalActionHandler.clearGlobalActions();
    }
    
    @Override
    protected void bringDown() {
        // Restore the global Action Handlers
        fGlobalActionHandler.restoreGlobalActions();
        
        super.bringDown();
    }
}
