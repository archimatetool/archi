/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.IconicDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelNote;


/**
 * Legend Figure
 * 
 * @author Phillip Beauvoir
 */
public class LegendFigure extends AbstractDiagramModelObjectFigure {
    
    private LegendGraphics legendGraphics;
    
    /**
     * Set true to display images
     * NoteUIProvider will also need to return true for IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH
     *                in NoteUIProvider#shouldExposeFeature
     */
    private static final boolean displayImage = false;
    
    public LegendFigure(IDiagramModelNote note) {
        super(note);
        setFigureDelegate(new RectangleFigureDelegate(this));
        legendGraphics = new LegendGraphics(note);
    }
    
    @Override
    public IDiagramModelNote getDiagramModelObject() {
        return (IDiagramModelNote)super.getDiagramModelObject();
    }
    
    /**
     * Update legend
     */
    public void updateLegend() {
        legendGraphics.update();
        refreshVisuals();
    }
    
    @Override
    public Dimension getDefaultSize() {
        // Always check for nulls as getDefaultSize() can be called after the figure is disposed
        Dimension figureSize = legendGraphics != null ? legendGraphics.getDefaultSize() : super.getDefaultSize();
        return figureSize.equals(0, 0) ? super.getDefaultSize() : figureSize;
    }
    
    @Override
    protected void setUI() {
        // Icon
        if(displayImage) {
            setIconicDelegate(new IconicDelegate(getDiagramModelObject()));
        }
    }
    
    @Override
    public IFigure getTextControl() {
        return this;
    }
    
    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        // Rectangle
        super.drawFigure(graphics);
        
        // Icon
        if(displayImage) {
            drawIconImage(graphics, getBounds());
        }
        
        // Legend
        legendGraphics.drawLegend(getBounds(), graphics, getForegroundColor());
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(legendGraphics != null) {
            legendGraphics.dispose();
            legendGraphics = null;
        }
    }
}