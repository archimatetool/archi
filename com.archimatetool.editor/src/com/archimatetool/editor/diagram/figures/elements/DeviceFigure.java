/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.GradientUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Device
 * 
 * @author Phillip Beauvoir
 */
public class DeviceFigure
extends AbstractArchimateFigure {
    
    protected static final int INDENT = 15;

    protected IFigureDelegate fFigureDelegate;
    
    public DeviceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate = new BoxFigureDelegate(this);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        int height_indent = bounds.height / 6;
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Bottom part
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y + bounds.height - 1);
        points1.addPoint(bounds.x + INDENT , bounds.y + bounds.height - height_indent - 1);
        points1.addPoint(bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 1);
        points1.addPoint(bounds.x + bounds.width, bounds.y + bounds.height - 1);
                
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points1);
        
        graphics.setForegroundColor(getLineColor());
        graphics.drawLine(bounds.x, bounds.y + bounds.height - 1,
                bounds.x + bounds.width, bounds.y + bounds.height - 1);
        graphics.drawLine(bounds.x, bounds.y + bounds.height - 1,
                bounds.x + INDENT, bounds.y + bounds.height - height_indent - 1);
        graphics.drawLine(bounds.x + bounds.width, bounds.y + bounds.height - 1,
                bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 1);

        // Top part
        Rectangle rect = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height - height_indent);

        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = GradientUtils.createScaledPattern(graphics, bounds, getFillColor());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillRoundRectangle(rect, 30, 30);
        
        if(gradient != null) {
            gradient.dispose();
        }

        graphics.setForegroundColor(getLineColor());
        rect = new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - height_indent - 1);
        graphics.drawRoundRectangle(rect, 30, 30);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        if(getFigureDelegate() != null) {
            return getFigureDelegate().calculateTextControlBounds();
        }

        Rectangle bounds = getBounds().getCopy();
        bounds.x += 20;
        bounds.y += 5;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }

    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
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
        return type == 0 ? fFigureDelegate : null;
    }
}