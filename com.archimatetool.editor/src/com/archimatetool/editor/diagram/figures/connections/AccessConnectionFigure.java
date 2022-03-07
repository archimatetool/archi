/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IAccessRelationship;


/**
 * Access Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class AccessConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Source Node
     */
    public static RotatableDecoration createFigureSourceDecoration() {
        return new PolylineDecoration();
    }

    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        return new PolylineDecoration();
    }

    private RotatableDecoration fDecoratorSource = createFigureSourceDecoration();
    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();
    
    public AccessConnectionFigure() {
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
        boolean usePlainArrowHeadOnJunction = usePlainJunctionTargetDecoration();
        
        // Access type
        IAccessRelationship relation = (IAccessRelationship)getDiagramModelArchimateConnection().getArchimateRelationship();
        switch(relation.getAccessType()) {
            case IAccessRelationship.WRITE_ACCESS:
            default:
                setSourceDecoration(null);
                setTargetDecoration(usePlainArrowHeadOnJunction ? null : fDecoratorTarget); // arrow at target endpoint
                break;

            case IAccessRelationship.READ_ACCESS:
                setSourceDecoration(fDecoratorSource); // arrow at source endpoint
                setTargetDecoration(null);
                break;

            case IAccessRelationship.UNSPECIFIED_ACCESS:
                setSourceDecoration(null);  // no arrows
                setTargetDecoration(null);
                break;

            case IAccessRelationship.READ_WRITE_ACCESS:
                setSourceDecoration(fDecoratorSource); // both arrows
                setTargetDecoration(usePlainArrowHeadOnJunction ? null : fDecoratorTarget);
                break;
        }

        // This last
        super.refreshVisuals();
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        // Show access type in tooltip
        
        IAccessRelationship relation = (IAccessRelationship)getDiagramModelArchimateConnection().getArchimateRelationship();
        String type = ArchiLabelProvider.INSTANCE.getDefaultName(relation.eClass());
        
        switch(relation.getAccessType()) {
            case IAccessRelationship.WRITE_ACCESS:
                type += " " + Messages.AccessConnectionFigure_0; //$NON-NLS-1$
                break;
                
            case IAccessRelationship.READ_ACCESS:
                type += " " + Messages.AccessConnectionFigure_1; //$NON-NLS-1$
                break;

            case IAccessRelationship.UNSPECIFIED_ACCESS:
                type += " " + Messages.AccessConnectionFigure_2; //$NON-NLS-1$
                break;

            case IAccessRelationship.READ_WRITE_ACCESS:
                type += " " + Messages.AccessConnectionFigure_3; //$NON-NLS-1$
                break;

            default:
                break;
        }
        
        tooltip.setType(Messages.AccessConnectionFigure_4 + " " + type); //$NON-NLS-1$

        return tooltip;
    }
}
