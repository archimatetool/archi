/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.BendpointCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.CreateBendpointCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.DeleteBendpointCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.MoveBendpointCommand;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;

/**
 * BendPoint Edit Policy
 * 
 * @author Phillip Beauvoir
 */
public class ManualBendpointEditPolicy extends BendpointEditPolicy {

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
