/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
// snap-to-grid patch by Jean-Baptiste Sarrodie (aka Jaiguru)
// Use alternate BendpointEditPolicy
//import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.archimatetool.editor.diagram.commands.BendpointCommand;
import com.archimatetool.editor.diagram.commands.CreateBendpointCommand;
import com.archimatetool.editor.diagram.commands.DeleteBendpointCommand;
import com.archimatetool.editor.diagram.commands.MoveBendpointCommand;
import com.archimatetool.editor.diagram.policies.snaptogrid.ExtendedBendpointEditPolicy;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * BendPoint Edit Policy
 * 
 * @author Phillip Beauvoir
 */
public class ManualBendpointEditPolicy extends ExtendedBendpointEditPolicy {

    @Override
    protected Command getCreateBendpointCommand(BendpointRequest request) {
        CreateBendpointCommand command = new CreateBendpointCommand();
        Point p = request.getLocation();
        Connection conn = getConnection();
        
        conn.translateToRelative(p);

        command.setLocation(p);
        Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
        Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

        conn.translateToRelative(ref1);
        conn.translateToRelative(ref2);

        command.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
        command.setDiagramModelConnection((IDiagramModelConnection)request.getSource().getModel());
        command.setIndex(request.getIndex());
        
        return command;
    }

    @Override
    protected Command getDeleteBendpointCommand(BendpointRequest request) {
        BendpointCommand command = new DeleteBendpointCommand();
        
        Point p = request.getLocation();
        command.setLocation(p);
        command.setDiagramModelConnection((IDiagramModelConnection)request.getSource().getModel());
        command.setIndex(request.getIndex());
        
        return command;
    }

    @Override
    protected Command getMoveBendpointCommand(BendpointRequest request) {
        MoveBendpointCommand command = new MoveBendpointCommand();
        Point p = request.getLocation();
        Connection conn = getConnection();
        
        conn.translateToRelative(p);

        command.setLocation(p);

        Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
        Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

        conn.translateToRelative(ref1);
        conn.translateToRelative(ref2);

        command.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
        command.setDiagramModelConnection((IDiagramModelConnection)request.getSource().getModel());
        command.setIndex(request.getIndex());
        
        return command;
    }

}
