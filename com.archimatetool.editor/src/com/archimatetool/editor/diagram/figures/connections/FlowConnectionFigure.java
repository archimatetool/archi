/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.swt.SWT;



/**
 * Flow Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class FlowConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        return new PolygonDecoration();
    }

    public FlowConnectionFigure() {
    }
    
    @Override
    protected void setFigureProperties() {
        setTargetDecoration(createFigureTargetDecoration()); 
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(getLineDashes(1.0));
    }
    
    @Override
    public void handleZoomChanged(double newZoomValue) {
        setLineDash(getLineDashes(newZoomValue));
    }
    
    private float[] getLineDashes(double zoomLevel) {
        return new float[] { (float)(6 * zoomLevel), (float)(3 * zoomLevel) }; 
    }

}
