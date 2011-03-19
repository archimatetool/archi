/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.connections.AbstractDiagramConnectionFigure;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Line Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class LineConnectionFigure extends AbstractDiagramConnectionFigure {

    public LineConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }

    @Override
    protected void setFigureProperties() {
    }
    
    @Override
    protected void setToolTip() {
        if(!Preferences.doShowViewTooltips()) {
            setToolTip(null); // clear it in case user changed Prefs
            return;
        }

        if(getToolTip() == null) {
            setToolTip(new ToolTipFigure());
        }

        ((ToolTipFigure)getToolTip()).setText("Connection");
    }
}
