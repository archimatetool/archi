/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.figures;

import org.eclipse.draw2d.PolygonDecoration;

import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.connections.AbstractDiagramConnectionFigure;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * ArrowConnectionFigure
 * 
 * @author Phillip Beauvoir
 */
public class ArrowConnectionFigure extends AbstractDiagramConnectionFigure {

    public ArrowConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }
    
    @Override
    protected void setFigureProperties() {
        PolygonDecoration poly = new PolygonDecoration();
        poly.setScale(10, 6);
        setTargetDecoration(poly); // arrow at target endpoint
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

        String text = StringUtils.safeString(getModelConnection().getName());
        ((ToolTipFigure)getToolTip()).setText(text);
        ((ToolTipFigure)getToolTip()).setType("Type: Connection");
    }
}
