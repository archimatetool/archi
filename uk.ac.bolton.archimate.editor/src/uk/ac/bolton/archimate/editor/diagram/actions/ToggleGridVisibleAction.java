/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;


/**
 * Toggle the Visibility of the Grid.
 * 
 * @author Phillip Beauvoir
 */
public class ToggleGridVisibleAction extends Action {

    private GraphicalViewer diagramViewer;

    /**
     * Constructor
     * @param   diagramViewer   the GraphicalViewer whose grid enablement and 
     *                          visibility properties are to be toggled
     */
    public ToggleGridVisibleAction(GraphicalViewer diagramViewer) {
        super("Grid Visible", AS_CHECK_BOX);
        this.diagramViewer = diagramViewer;
        setToolTipText("Grid Visible");
        setId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setActionDefinitionId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setChecked(isChecked());
    }

    @Override
    public boolean isChecked() {
        Boolean val = (Boolean)diagramViewer.getProperty(SnapToGrid.PROPERTY_GRID_VISIBLE);
        if (val != null)
            return val.booleanValue();
        return false;
    }

    @Override
    public void run() {
        boolean val = !isChecked();
        diagramViewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(val));
    }
}
