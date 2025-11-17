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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IInfluenceRelationship;



/**
 * Influence Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class InfluenceConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        return new PolylineDecoration();
    }
    
    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();

    public InfluenceConnectionFigure() {
    }

    @Override
    protected void setFigureProperties() {
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(getLineDashFloats());
    }
    
    @Override
    public void setText() {
        super.setText();
        
        // Show Strength after Name if we don't show it already by means of the text expression
        if(getModelConnection().isNameVisible()) {
            String text = getConnectionLabel().getText();
            String strength = ((IInfluenceRelationship)getDiagramModelArchimateConnection().getArchimateRelationship()).getStrength();
            if(StringUtils.isSet(strength) && !text.contains(strength)) {
                text += " " + strength; //$NON-NLS-1$
                getConnectionLabel().setText(text);
            }
        }
    }
    
    @Override
    protected float[] getLineDashFloats() {
        double scale = Math.min(FigureUtils.getFigureScale(this), 1.0); // only scale below 1.0
        return new float[] { (float)(6 * scale), (float)(3 * scale) };
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
            graphics.setLineDash(new float[] { 3f, 1.5f });
            path.moveTo(pt.x, pt.y + 13);
            path.lineTo(pt.x + 13, pt.y);
            graphics.drawPath(path);
            path.dispose();
            
            path = new Path(null);
            
            graphics.setLineDash((float[])null); // Have to do it this way because it's not reset to normal using graphics.setLineStyle(SWT.LINE_SOLID);
            path.moveTo(pt.x + 8, pt.y);
            path.lineTo(pt.x + 13, pt.y);
            path.lineTo(pt.x + 13, pt.y + 5);
            
            // cross
            path.moveTo(pt.x + 9, pt.y + 8);
            path.lineTo(pt.x + 13, pt.y + 8);
            path.moveTo(pt.x + 11, pt.y + 6);
            path.lineTo(pt.x + 11, pt.y + 10);
            graphics.drawPath(path);
            
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
