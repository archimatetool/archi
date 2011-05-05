/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.editor.diagram.DiagramConstants;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.connections.AbstractDiagramConnectionFigure;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
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
    public void refreshVisuals() {
        super.refreshVisuals();

        String type = getModelConnection().getType();
        if(DiagramConstants.CONNECTION_DASHED.equals(type)) {
            setLineStyle(SWT.LINE_CUSTOM);
            setLineDash(new float[] { 4 });
        }
        else if(DiagramConstants.CONNECTION_DOTTED.equals(type)) {
            setLineStyle(SWT.LINE_CUSTOM);
            setLineDash(new float[] { 1.5f, 3 });
        }
        else {
            setLineStyle(Graphics.LINE_SOLID);
        }
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
