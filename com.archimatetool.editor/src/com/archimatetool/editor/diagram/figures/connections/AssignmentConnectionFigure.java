/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.PolarPoint;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Assignment Connection Figure class
 * 
 * @author Phillip Beauvoir
 * @author Jean-Baptiste Sarrodie
 */
public class AssignmentConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * Ball
     */
    static class BallEndpoint extends Figure implements RotatableDecoration {
        private int radius = 3;
        private Point pLocation;

        BallEndpoint() {
            setSize(radius * 2 + 1, radius * 2 + 1);
        }

        @Override
        public void setReferencePoint(Point ref) {
            if(pLocation != null) {
                PolarPoint pp = new PolarPoint(ref, pLocation);
                pp.r -= radius;
                super.setLocation(pp.toAbsolutePoint(ref).getTranslated(-radius, -radius));
            }
        }

        @Override
        public void setLocation(Point p) {
            pLocation = p.getCopy();
        }

        @Override
        public void paintFigure(Graphics graphics) {
            if(isEnabled()) {
                graphics.setBackgroundColor(getParent().getForegroundColor());
            }
            else {
                graphics.setBackgroundColor(ColorConstants.buttonDarker);
            }
            graphics.fillOval(getBounds());
        }
    }

    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        return new PolygonDecoration();
    }
    
    /**
     * @return Decoration to use on Source Node
     */
    public static RotatableDecoration createFigureSourceDecoration() {
        return new BallEndpoint();
    }
    
    private RotatableDecoration fDecoratorSource = createFigureSourceDecoration();
    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();
    
    public AssignmentConnectionFigure() {
    }

    @Override
    public void refreshVisuals() {
        setSourceDecoration(usePlainJunctionSourceDecoration() ? null : fDecoratorSource);
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
            
            graphics.setBackgroundColor(graphics.getForegroundColor());
            
            graphics.setLineWidth(1);
            
            Path path = new Path(null);
            
            path.moveTo(pt.x, pt.y + 13);
            path.lineTo(pt.x + 13, pt.y);
            
            path.moveTo(pt.x + 8, pt.y);
            path.lineTo(pt.x + 13, pt.y);
            path.lineTo(pt.x + 13, pt.y + 5);
            path.close();
            graphics.fillPath(path);
            graphics.drawPath(path); // need to draw and fill so it's centred
            
            path.dispose();
            
            // ball
            graphics.fillArc(pt.x - 1,  pt.y + 9, 5, 5, 0, 360);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
