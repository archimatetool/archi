/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.model.IAccessRelationship;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;

/**
 * Access Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class AccessConnectionFigure extends AbstractArchimateConnectionFigure {
	
    private PolylineDecoration fDecoratorSource = new PolylineDecoration();
    private PolylineDecoration fDecoratorTarget = new PolylineDecoration();
    
	public AccessConnectionFigure(IDiagramModelArchimateConnection connection) {
	    super(connection);
	}
	
    @Override
    protected void setFigureProperties() {
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(new float[] { 1.5f, 3 });
    }
    
    @Override
    public void refreshVisuals() {
        // Access type
        IAccessRelationship relation = (IAccessRelationship)getModelConnection().getRelationship();
        switch(relation.getAccessType()) {
            case IAccessRelationship.WRITE_ACCESS:
            default:
                setSourceDecoration(null);
                setTargetDecoration(fDecoratorTarget); // arrow at target endpoint
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
                setTargetDecoration(fDecoratorTarget);
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
        
        IAccessRelationship relation = (IAccessRelationship)getModelConnection().getRelationship();
        String type = ArchimateNames.getDefaultName(relation.eClass());
        
        switch(relation.getAccessType()) {
            case IAccessRelationship.WRITE_ACCESS:
                type += " (write)";
                break;
                
            case IAccessRelationship.READ_ACCESS:
                type += " (read)";
                break;

            case IAccessRelationship.UNSPECIFIED_ACCESS:
                type += " (access)";
                break;

            case IAccessRelationship.READ_WRITE_ACCESS:
                type += " (read/write)";
                break;

            default:
                break;
        }
        
        tooltip.setType("Type: " + type);

        return tooltip;
    }
}
