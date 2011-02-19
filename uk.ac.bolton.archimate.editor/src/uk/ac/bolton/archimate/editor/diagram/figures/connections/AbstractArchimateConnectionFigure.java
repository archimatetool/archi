/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;

import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Abstract implementation of an Archimate connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateConnectionFigure
extends AbstractDiagramConnectionFigure implements IArchimateConnectionFigure {

    public AbstractArchimateConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
    
    @Override
    public IDiagramModelArchimateConnection getModelConnection() {
        return (IDiagramModelArchimateConnection)super.getModelConnection();
    }

    @Override
    public void highlight(boolean set) {
        if(set) {
            setForegroundColor(ColorConstants.red);
            fLineColor = ColorConstants.red;
            setLineWidth(2);
        }
        else {
            setLineColor();
            setLineWidth();
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

        IRelationship relation = getModelConnection().getRelationship();
        String text = StringUtils.safeString(relation.getName());
        ((ToolTipFigure)getToolTip()).setText(text);

        String type = ArchimateNames.getDefaultName(relation.eClass());
        ((ToolTipFigure)getToolTip()).setType("Type: " + type);
    }

}
