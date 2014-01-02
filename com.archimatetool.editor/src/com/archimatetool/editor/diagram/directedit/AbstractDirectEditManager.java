/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

import com.archimatetool.editor.ui.components.CellEditorGlobalActionHandler;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Direct Edit Manager that updates Global Action Handlers
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDirectEditManager extends DirectEditManager {
    
    private CellEditorGlobalActionHandler fGlobalActionHandler;

    public AbstractDirectEditManager(GraphicalEditPart source, @SuppressWarnings("rawtypes") Class editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
    }

    @Override
    protected void initCellEditor() {
        // Hook into the global Action Handlers and null them
        fGlobalActionHandler = new CellEditorGlobalActionHandler();
        fGlobalActionHandler.clearGlobalActions();
        
        // Stop OS X Lion closing Full Screen when Escape pressed
        if(PlatformUtils.supportsMacFullScreen()) {
            getCellEditor().getControl().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.keyCode == SWT.ESC) {
                        e.doit = false;
                    }
                }
            });
        }
    }
    
    @Override
    protected void bringDown() {
        // Restore the global Action Handlers
        fGlobalActionHandler.restoreGlobalActions();
        
        super.bringDown();
    }
}
