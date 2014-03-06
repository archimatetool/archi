/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Technology Device
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigure
extends AbstractArchimateFigure {
    
    protected TechnologyDeviceFigureDelegate1 fFigureDelegate1;
    protected TechnologyDeviceFigureDelegate2 fFigureDelegate2;
    
    public TechnologyDeviceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate1 = new TechnologyDeviceFigureDelegate1(this);
        fFigureDelegate2 = new TechnologyDeviceFigureDelegate2(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        int type = getDiagramModelObject().getType();
        if(type == 1) {
            drawIcon(graphics);
        }
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        graphics.setLineWidth(1);
        graphics.setForegroundColor(ColorConstants.black);
        
        Point pt = getIconOrigin();
        
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 11, 8), 3, 3);
        
        graphics.drawPolygon(new int[] {
                pt.x - 1, pt.y + 12,
                pt.x + 2, pt.y + 8,
                pt.x + 9, pt.y + 8,
                pt.x + 12, pt.y + 12
        });
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 31, bounds.y + 18);
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        repaint(); // repaint when figure changes
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }
}