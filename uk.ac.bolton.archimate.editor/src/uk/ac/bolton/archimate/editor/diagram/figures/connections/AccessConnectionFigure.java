/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

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
	
    private PolylineDecoration fDecorator = new PolylineDecoration();
    
	public AccessConnectionFigure(IDiagramModelArchimateConnection connection) {
	    super(connection);
	}
	
    @Override
    protected void setFigureProperties() {
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(new float[] { 4 });
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        
        // Access type
        IAccessRelationship relation = (IAccessRelationship)getModelConnection().getRelationship();
        switch(relation.getAccessType()) {
            case IAccessRelationship.WRITE_ACCESS:
                setSourceDecoration(null);
                setTargetDecoration(fDecorator); // arrow at target endpoint
                break;

            case IAccessRelationship.READ_ACCESS:
                setTargetDecoration(null);
                setSourceDecoration(fDecorator); // arrow at source endpoint
                break;

            case IAccessRelationship.UNSPECIFIED_ACCESS:
                setTargetDecoration(null);
                setSourceDecoration(null); // arrow at source endpoint
                break;

            default:
                break;
        }
    }
    
    @Override
    protected void setToolTip() {
        super.setToolTip();
        
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

            default:
                break;
        }
        
        ((ToolTipFigure)getToolTip()).setType("Type: " + type);
    }
}
