/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.components.CellEditorGlobalActionHandler;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;


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
        IActionBars actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().getActiveEditor().getEditorSite().getActionBars();

        fGlobalActionHandler = new CellEditorGlobalActionHandler(actionBars);
        
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
        fGlobalActionHandler.dispose();
        
        super.bringDown();
    }
}
