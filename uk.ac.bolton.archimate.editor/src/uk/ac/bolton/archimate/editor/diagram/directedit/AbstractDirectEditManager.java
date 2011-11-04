/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.CellEditorActionHandler;


/**
 * Direct Edit Manager that updates Global Action Handlers
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDirectEditManager extends DirectEditManager {
    
    private CellEditorActionHandler fCellEditorActionHandler;
    private IEditorPart fEditor;
    private IActionBars fActionBars;

    public AbstractDirectEditManager(GraphicalEditPart source, @SuppressWarnings("rawtypes") Class editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
    }

    @Override
    protected void initCellEditor() {
        // Hook into the Global Action Handlers
        // Note toolbar items don't work - https://bugs.eclipse.org/bugs/show_bug.cgi?id=321045
        fEditor =  ((DefaultEditDomain)getEditPart().getRoot().getViewer().getEditDomain()).getEditorPart();
        fActionBars = fEditor.getEditorSite().getActionBars();
        fCellEditorActionHandler = new CellEditorActionHandler(fActionBars);
        fCellEditorActionHandler.addCellEditor(getCellEditor());
        fActionBars.updateActionBars();
    }
    
    @Override
    protected void unhookListeners() {
        // Unhook and reset the Global Action Handlers
        fCellEditorActionHandler.dispose();
        IEditorActionBarContributor contributor = fEditor.getEditorSite().getActionBarContributor();
        contributor.setActiveEditor(fEditor);
        fActionBars.updateActionBars();
        
        super.unhookListeners();
    }

}
