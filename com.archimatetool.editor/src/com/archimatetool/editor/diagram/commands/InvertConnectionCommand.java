/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Invert Connection Direction Command
 *
 * Inverts the direction of an IArchimateRelationship and all its referencing diagram connections,
 * swapping source and target, reversing bendpoints, and swapping text position.
 */
public class InvertConnectionCommand extends Command {

    private IArchimateRelationship fRelationship;

    // Saved state for undo
    private IArchimateConcept fOldSource;
    private IArchimateConcept fOldTarget;
    private List<ConnectionState> fConnectionStates;

    private record ConnectionState(IDiagramModelArchimateConnection connection, IConnectable source, IConnectable target, int textPosition,
            List<int[]> bendpoints) {
    }

    public InvertConnectionCommand(IArchimateRelationship relationship) {
        if(relationship == null) {
            throw new IllegalArgumentException();
        }

        fRelationship = relationship;
        setLabel(Messages.InvertConnectionCommand_0);
    }

    @Override
    public void execute() {
        // Save state
        fOldSource = fRelationship.getSource();
        fOldTarget = fRelationship.getTarget();

        fConnectionStates = new ArrayList<>();
        for(IDiagramModelArchimateConnection connection : fRelationship.getReferencingDiagramConnections()) {
            List<int[]> bendpoints = new ArrayList<>();
            for(IDiagramModelBendpoint bp : connection.getBendpoints()) {
                bendpoints.add(new int[] { bp.getStartX(), bp.getStartY(), bp.getEndX(), bp.getEndY() });
            }
            fConnectionStates.add(new ConnectionState(connection, connection.getSource(), connection.getTarget(), connection.getTextPosition(), bendpoints));
        }

        doInvert();
    }

    @Override
    public void redo() {
        doInvert();
    }

    @Override
    public void undo() {
        // Restore relationship source/target
        fRelationship.connect(fOldSource, fOldTarget);

        // Restore each diagram connection
        for(ConnectionState state : fConnectionStates) {
            state.connection().connect(state.source(), state.target());
            state.connection().setTextPosition(state.textPosition());

            // Restore bendpoints
            List<IDiagramModelBendpoint> bps = state.connection().getBendpoints();
            for(int i = 0; i < bps.size(); i++) {
                int[] saved = state.bendpoints().get(i);
                IDiagramModelBendpoint bp = bps.get(i);
                bp.setStartX(saved[0]);
                bp.setStartY(saved[1]);
                bp.setEndX(saved[2]);
                bp.setEndY(saved[3]);
            }
        }
    }

    private void doInvert() {
        // Swap source/target on the relationship
        fRelationship.connect(fOldTarget, fOldSource);

        // Swap source/target on each diagram connection and adjust bendpoints/text position
        for(ConnectionState state : fConnectionStates) {
            state.connection().connect(state.target(), state.source());

            // Reverse bendpoints: reverse order and swap start↔end coordinates
            List<IDiagramModelBendpoint> bps = state.connection().getBendpoints();
            int size = bps.size();

            if(size > 0) {
                // Read current values into temp array
                int[][] values = new int[size][4];
                for(int i = 0; i < size; i++) {
                    IDiagramModelBendpoint bp = bps.get(i);
                    values[i] = new int[] { bp.getStartX(), bp.getStartY(), bp.getEndX(), bp.getEndY() };
                }

                // Write them back in reverse order with start↔end swapped
                for(int i = 0; i < size; i++) {
                    IDiagramModelBendpoint bp = bps.get(i);
                    int[] reversed = values[size - 1 - i];
                    bp.setStartX(reversed[2]); // old endX
                    bp.setStartY(reversed[3]); // old endY
                    bp.setEndX(reversed[0]);   // old startX
                    bp.setEndY(reversed[1]);   // old startY
                }
            }

            // Swap text position: SOURCE↔TARGET, MIDDLE stays
            int textPos = state.connection().getTextPosition();
            if(textPos == IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE) {
                state.connection().setTextPosition(IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET);
            }
            else if(textPos == IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET) {
                state.connection().setTextPosition(IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE);
            }
        }
    }

    @Override
    public void dispose() {
        fRelationship = null;
        fOldSource = null;
        fOldTarget = null;
        fConnectionStates = null;
    }
}
