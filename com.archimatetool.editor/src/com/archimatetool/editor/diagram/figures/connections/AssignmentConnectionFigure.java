/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.geometry.PolarPoint;
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

        private Point pRef;
        private int radius = 2;

        public OvalDecoration() {
            setSize(radius * 2 + 1, radius * 2 + 1);
        }

        public void setReferencePoint(Point ref) {
            pRef = ref.getCopy();
        }

        @Override
        public void setLocation(Point p) {
            PolarPoint pp = new PolarPoint(pRef, p.getCopy());
            pp.r -= radius;
            super.setLocation(pp.toAbsolutePoint(pRef).getTranslated(-radius, -radius));
        }

        @Override
        public void paintFigure(Graphics graphics) {
            graphics.setAntialias(SWT.ON);
            graphics.setBackgroundColor(ColorConstants.black);
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
