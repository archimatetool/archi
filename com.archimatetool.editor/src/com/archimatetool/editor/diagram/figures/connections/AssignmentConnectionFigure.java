/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.model.IDiagramModelArchimateConnection;


/**
 * Assignment Connection Figure class
 * 
 * @author Phillip Beauvoir
 * @author Jean-Baptiste Sarrodie
 */
public class AssignmentConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static class OvalDecoration extends Figure implements RotatableDecoration {

        private int radius = 2;

        public OvalDecoration() {
            setSize(radius * 2 + 1, radius * 2 + 1);
        }

        public void setReferencePoint(Point ref) {
        }

        @Override
        public void setLocation(Point p) {
            if(getLocation().equals(p)) {
                return;
            }
            Dimension size = getBounds().getSize();
            setBounds(new Rectangle(p.x - (size.width >> 1), p.y - (size.height >> 1), size.width, size.height));
        }

        @Override
        public void paintFigure(Graphics graphics) {
            graphics.setBackgroundColor(getParent().getForegroundColor());
            graphics.fillOval(bounds);
        }
    }

    /**
     * @return Decoration to use on Target Node
     */
    public static OvalDecoration createFigureTargetDecoration() {
        return new OvalDecoration();
    }
    
    /**
     * @return Decoration to use on Source Node
     */
    public static OvalDecoration createFigureSourceDecoration() {
        return createFigureTargetDecoration();
    }

    public AssignmentConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        setSourceDecoration(createFigureSourceDecoration());
        setTargetDecoration(createFigureTargetDecoration()); 
    }
}
