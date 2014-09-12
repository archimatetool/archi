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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.geometry.PolarPoint;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IDiagramModelArchimateConnection;


/**
 * Assignment Connection Figure class
 * 
 * @author Phillip Beauvoir
 * @author Jean-Baptiste Sarrodie
 */
public class AssignmentConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * Skewy-line ball
     */
    static class Endpoint1 extends Figure implements RotatableDecoration {
        private int radius = 2;
        private Point pRef;

        Endpoint1() {
            setSize(radius * 2 + 1, radius * 2 + 1);
        }

        public void setReferencePoint(Point ref) {
            pRef = ref.getCopy();
        }

        @Override
        public void setLocation(Point p) {
            if(pRef != null) {
                PolarPoint pp = new PolarPoint(pRef, p.getCopy());
                pp.r -= radius;
                super.setLocation(pp.toAbsolutePoint(pRef).getTranslated(-radius, -radius));
            }
            else {
                super.setLocation(p);
            }
        }

        @Override
        public void paintFigure(Graphics graphics) {
            if(isEnabled()) {
                graphics.setBackgroundColor(getParent().getForegroundColor());
            }
            else {
                graphics.setBackgroundColor(ColorConstants.buttonDarker);
            }
            graphics.fillOval(bounds);
        }
    }

    /**
     * Overlapping on edge ball
     */
    static class Endpoint2 extends Endpoint1 {
        
        Endpoint2() {
        }

        @Override
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
    }

    /**
     * "Classic" endpoint (aka "shit")
     */
    static class Endpoint3 extends PolygonDecoration {
        Endpoint3() {
            setScale(2, 1.3);
            
            PointList decorationPointList = new PointList();
            
            decorationPointList.addPoint(0, 0);
            decorationPointList.addPoint(0, -1);
            decorationPointList.addPoint(-1, -1);
            decorationPointList.addPoint(-1, 0);
            decorationPointList.addPoint(-1, 1);
            decorationPointList.addPoint(0, 1);
            
            setTemplate(decorationPointList);
        }
    }

    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        int type = Preferences.STORE.getInt(IPreferenceConstants.ASSIGNMENT_CONNECTION_ENDPOINT);
        switch(type) {
            case 0:
                return new Endpoint1();

            case 1:
                return new Endpoint2();

            case 2:
                return new Endpoint3();

            default:
                return new Endpoint1();
        }
    }
    
    /**
     * @return Decoration to use on Source Node
     */
    public static RotatableDecoration createFigureSourceDecoration() {
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
