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
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.FigureUtils;



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
}
