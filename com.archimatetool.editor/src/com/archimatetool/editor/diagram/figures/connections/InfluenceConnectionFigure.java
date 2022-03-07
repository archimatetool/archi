/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.FigureUtils;
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
}
