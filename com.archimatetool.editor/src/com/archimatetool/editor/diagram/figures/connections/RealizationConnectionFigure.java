/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;



/**
 * Realization Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class RealizationConnectionFigure extends AbstractArchimateConnectionFigure {
    
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        PolygonDecoration decoration = new PolygonDecoration() {
            @Override
            protected void fillShape(Graphics g) {
                // Draw this as white in case it is disabled
                g.setBackgroundColor(ColorConstants.white);
                super.fillShape(g);
            }
        };
        decoration.setScale(10, 7);
        decoration.setBackgroundColor(ColorConstants.white);
        
        return decoration;
    }
    
    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();
    
    public RealizationConnectionFigure() {
    }
	
    @Override
    protected void setFigureProperties() {
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(getLineDashFloats());
    }
    
    @Override
    protected float[] getLineDashFloats() {
        double scale = Math.min(FigureUtils.getFigureScale(this), 1.0); // only scale below 1.0
        return new float[] { (float)(2 * scale) };
    }
    
    @Override
    public void refreshVisuals() {
        setTargetDecoration(usePlainJunctionTargetDecoration() ? null : fDecoratorTarget);
        
        // This last
        super.refreshVisuals();
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            graphics.setLineWidth(1);
            
            Path path = new Path(null);
            graphics.setLineDash(new float[] { 1.5f });
            path.moveTo(pt.x, pt.y + 13);
            path.lineTo(pt.x + 10, pt.y + 3);
            graphics.drawPath(path);
            path.dispose();
            
            path = new Path(null);
            path.moveTo(pt.x + 7, pt.y);
            path.lineTo(pt.x + 13, pt.y);
            path.lineTo(pt.x + 13, pt.y + 6);
            path.close();
            graphics.setLineDash((float[])null); // Have to do it this way because it's not reset to normal using graphics.setLineStyle(SWT.LINE_SOLID);
            graphics.drawPath(path);
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
