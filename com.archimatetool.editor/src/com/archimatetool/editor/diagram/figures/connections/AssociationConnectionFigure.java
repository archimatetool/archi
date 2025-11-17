/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IAssociationRelationship;

/**
 * Association Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class AssociationConnectionFigure extends AbstractArchimateConnectionFigure {
    
    private static final PointList POINTS = new PointList();

    static {
        POINTS.addPoint(0, 0);
        POINTS.addPoint(-1, -1);
    }
    
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        PolylineDecoration dec = new PolylineDecoration();
        dec.setTemplate(POINTS);
        dec.setScale(9, 4);
        return dec;
    }

    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();
    
    public AssociationConnectionFigure() {
    }
	
    @Override
    protected void setFigureProperties() {
    }
    
    @Override
    public void refreshVisuals() {
        // Access type
        IAssociationRelationship relation = (IAssociationRelationship)getDiagramModelArchimateConnection().getArchimateRelationship();
        
        if(relation.isDirected()) {
            setTargetDecoration(fDecoratorTarget); // half-arrow at target endpoint
        }
        else {
            setTargetDecoration(null);
        }

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
            
            path.moveTo(pt.x, pt.y + 13);
            path.lineTo(pt.x + 13, pt.y);
            graphics.drawPath(path);
            
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
