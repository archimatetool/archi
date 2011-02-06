/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.action.Action;


/**
 * Enable or disable the grid. It does not show or hide it.
 * 
 * @author Phillip Beauvoir
 */
public class ToggleGridEnabledAction extends Action {

    private GraphicalViewer diagramViewer;

    /**
     * Constructor
     * @param   diagramViewer   the GraphicalViewer whose grid enablement and 
     *                          visibility properties are to be toggled
     */
    public ToggleGridEnabledAction(GraphicalViewer diagramViewer) {
        super("Snap to Grid", AS_CHECK_BOX);
        this.diagramViewer = diagramViewer;
        setToolTipText("Snap to Grid");
        setId(SnapToGrid.PROPERTY_GRID_ENABLED);
        setActionDefinitionId(SnapToGrid.PROPERTY_GRID_ENABLED);
        setChecked(isChecked());
    }

    @Override
    public boolean isChecked() {
        Boolean val = (Boolean)diagramViewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
        if (val != null)
            return val.booleanValue();
        return false;
    }

    @Override
    public void run() {
        boolean val = !isChecked();
        diagramViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(val));
    }
}
