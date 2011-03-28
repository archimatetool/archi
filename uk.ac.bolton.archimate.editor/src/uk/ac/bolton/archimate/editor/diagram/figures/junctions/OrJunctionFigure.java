/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.junctions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Or Junction Figure
 * 
 * @author Phillip Beauvoir
 */
public class OrJunctionFigure extends RectangleFigure implements IDiagramModelObjectFigure {
    
    protected static final Dimension SIZE = new Dimension(15, 15);
    
    protected IDiagramModelArchimateObject fDiagramModelObject;
    
    public OrJunctionFigure(IDiagramModelArchimateObject diagramModelObject) {
        fDiagramModelObject = diagramModelObject;
    }
    
    @Override
    public IDiagramModelObject getDiagramModelObject() {
        return fDiagramModelObject;
    }

    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return SIZE;
    }

    @Override
    public void refreshVisuals() {
        setToolTip();
    }
    
    protected void setToolTip() {
        if(!Preferences.doShowViewTooltips()) {
            setToolTip(null); // clear it in case user changed Prefs
            return;
        }
        
        String text = StringUtils.safeString(fDiagramModelObject.getName());
        
        if(getToolTip() == null) {
            setToolTip(new ToolTipFigure());
        }
        
        ((ToolTipFigure)getToolTip()).setText(text);
        IArchimateElement element = fDiagramModelObject.getArchimateElement();
        String type = ArchimateNames.getDefaultName(element.eClass());
        ((ToolTipFigure)getToolTip()).setType("Type: " + type);
    }

    @Override
    public IFigure getTextControl() {
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public Dimension getDefaultSize() {
        return SIZE;
    }
    
    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public boolean didClickTextControl(Point requestLoc) {
        return false;
    }
}
