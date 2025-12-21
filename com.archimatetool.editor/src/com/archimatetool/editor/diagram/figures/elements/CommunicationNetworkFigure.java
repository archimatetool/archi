/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Communication Network Figure
 * 
 * @author Phillip Beauvoir
 */
public class CommunicationNetworkFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public CommunicationNetworkFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        Rectangle imageBounds = rect.getCopy();
        
        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setBackgroundColor(getLineColor());
        
        int figureMaxSize = Math.min(rect.width, rect.height);
        float blobDiameter = (int)Math.max(10, (Math.sqrt(rect.width * rect.height) / 7d));
        float blobRadius = blobDiameter / 2;
        
        int lineWidth = (int)blobDiameter / 4;
        graphics.setLineWidth(lineWidth);
        
        float heightOffset = figureMaxSize / 4;
        float widthOffset = figureMaxSize / 3;
        
        Point center = rect.getCenter();
        float x = center.x - widthOffset;
        float y = center.y - heightOffset;
        float w = widthOffset * 2;
        float h = heightOffset * 2;
        float indent = w / 5;
        
        // Circles
        Path path = new Path(null);
        
        path.addArc(x, y + h - blobDiameter,
                blobDiameter, blobDiameter, 0, 360);
    
        path.addArc(x + w - indent - blobDiameter, y + h - blobDiameter,
                blobDiameter, blobDiameter, 0, 360);
    
        path.addArc(x + indent, y,
                blobDiameter, blobDiameter, 0, 360);
    
        path.addArc(x + w - blobDiameter, y,
                blobDiameter, blobDiameter, 0, 360);
    
        graphics.fillPath(path);
        path.dispose();
        
        // Lines
        path = new Path(null);
        
        float x1 = x + blobRadius;
        float y1 = y + h - blobRadius;
        float x2 = x1 + w - indent - blobDiameter;
        
        path.moveTo(x1, y1);
        path.lineTo(x2, y1);
        path.lineTo(x + w - blobRadius, y + blobRadius);
        path.lineTo(x + indent + blobRadius, y + blobRadius);
        path.lineTo(x1, y1);
        
        graphics.drawPath(path);
        path.dispose();
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.setLineWidthFloat(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            Path path = new Path(null);
            
            path.addArc(pt.x, pt.y, 5, 5, 0, 360);
            path.addArc(pt.x + 2, pt.y - 8, 5, 5, 0, 360);
            path.addArc(pt.x + 10, pt.y - 8, 5, 5, 0, 360);
            path.addArc(pt.x + 8, pt.y, 5, 5, 0, 360);
            
            path.moveTo(pt.x + 3, pt.y);
            path.lineTo(pt.x + 4, pt.y - 3);
            
            path.moveTo(pt.x + 11, pt.y);
            path.lineTo(pt.x + 12, pt.y - 3);
            
            path.moveTo(pt.x + 5, pt.y + 2.5f);
            path.lineTo(pt.x + 8, pt.y + 2.5f);
            
            path.moveTo(pt.x + 7, pt.y - 5.5f);
            path.lineTo(pt.x + 10, pt.y - 5.5f);
            
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 18 - getLineWidth(), rect.y + 14);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
