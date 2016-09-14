/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.figures.connections.AbstractDiagramConnectionFigure;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Line Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class LineConnectionFigure extends AbstractDiagramConnectionFigure {
    
    private PolygonDecoration fArrowheadSourceFilled;
    private PolylineDecoration fArrowheadSourceLine;
    private PolygonDecoration fArrowheadSourceHollow;
    
    private PolygonDecoration fArrowheadTargetFilled;
    private PolylineDecoration fArrowheadTargetLine;
    private PolygonDecoration fArrowheadTargetHollow;
    
    @Override
    protected void setFigureProperties() {
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        
        setLineStyleFromZoomLevel(1);

        int connectionType = getModelConnection().getType();
        
        // Source Arrow head
        if((connectionType & IDiagramModelConnection.ARROW_FILL_SOURCE) != 0) {
            setSourceDecoration(getArrowheadSourceFilled());
        }
        else if((connectionType & IDiagramModelConnection.ARROW_LINE_SOURCE) != 0) {
            setSourceDecoration(getArrowheadSourceLine());
        }
        else if((connectionType & IDiagramModelConnection.ARROW_HOLLOW_SOURCE) != 0) {
            setSourceDecoration(getArrowheadSourceHollow());
        }
        else {
            setSourceDecoration(null);
        }

        // Target Arrow head
        if((connectionType & IDiagramModelConnection.ARROW_FILL_TARGET) != 0) {
            setTargetDecoration(getArrowheadTargetFilled());
        }
        else if((connectionType & IDiagramModelConnection.ARROW_LINE_TARGET) != 0) {
            setTargetDecoration(getArrowheadTargetLine());
        }
        else if((connectionType & IDiagramModelConnection.ARROW_HOLLOW_TARGET) != 0) {
            setTargetDecoration(getArrowheadTargetHollow());
        }
        else {
            setTargetDecoration(null);
        }
        
        repaint(); // repaint when figure changes
    }
    
    protected void setLineStyleFromZoomLevel(double zoomLevel) {
        int connectionType = getModelConnection().getType();
        
        // Line Style
        if((connectionType & IDiagramModelConnection.LINE_DASHED) != 0) {
            setLineStyle(SWT.LINE_CUSTOM);
            setLineDash(new float[] { (float)(4 * zoomLevel) });
        }
        else if((connectionType & IDiagramModelConnection.LINE_DOTTED) != 0) {
            setLineStyle(SWT.LINE_CUSTOM);
            setLineDash(new float[] { (float)(1 * zoomLevel), (float)(4 * zoomLevel) });
        }
        else {
            setLineStyle(Graphics.LINE_SOLID);
        }
    }
    
    @Override
    public void handleZoomChanged(double newZoomValue) {
        setLineStyleFromZoomLevel(newZoomValue);
    }
    
    protected PolygonDecoration getArrowheadSourceFilled() {
        if(fArrowheadSourceFilled == null) {
            fArrowheadSourceFilled = createArrowheadFilled();
        }
        return fArrowheadSourceFilled;
    }
    
    protected PolylineDecoration getArrowheadSourceLine() {
        if(fArrowheadSourceLine == null) {
            fArrowheadSourceLine = createArrowheadLine();
        }
        return fArrowheadSourceLine;
    }
    
    protected PolygonDecoration getArrowheadSourceHollow() {
        if(fArrowheadSourceHollow == null) {
            fArrowheadSourceHollow = createArrowheadHollow();
        }
        return fArrowheadSourceHollow;
    }

    protected PolygonDecoration getArrowheadTargetFilled() {
        if(fArrowheadTargetFilled == null) {
            fArrowheadTargetFilled = createArrowheadFilled();
        }
        return fArrowheadTargetFilled;
    }
    
    protected PolylineDecoration getArrowheadTargetLine() {
        if(fArrowheadTargetLine == null) {
            fArrowheadTargetLine = createArrowheadLine();
        }
        return fArrowheadTargetLine;
    }
    
    protected PolygonDecoration getArrowheadTargetHollow() {
        if(fArrowheadTargetHollow == null) {
            fArrowheadTargetHollow = createArrowheadHollow();
        }
        return fArrowheadTargetHollow;
    }
    
    protected PolygonDecoration createArrowheadFilled() {
        PolygonDecoration poly = new PolygonDecoration();
        poly.setScale(10, 6);
        return poly;
    }

    protected PolygonDecoration createArrowheadHollow() {
        PolygonDecoration poly = new PolygonDecoration();
        poly.setScale(10, 7);
        poly.setBackgroundColor(ColorConstants.white);
        return poly;
    }
    
    protected PolylineDecoration createArrowheadLine() {
        PolylineDecoration poly = new PolylineDecoration();
        poly.setScale(8, 5);
        return poly;
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        String text = ArchiLabelProvider.INSTANCE.getLabel(getModelConnection());
        tooltip.setText(text);
        tooltip.setType(Messages.LineConnectionFigure_0);
        
        return tooltip;
    }
}
