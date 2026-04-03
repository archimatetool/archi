/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.model.IArchimateRelationship;
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
    
    // Internal preference to swap label positions
    // Can be set manually in .metadata/.plugins/org.eclipse.core.runtime/.settings/com.archimatetool.editor.prefs
    static private final boolean INVERT_LABEL_POSITIONS = ArchiPlugin.getInstance().getPreferenceStore().getBoolean("invertConnectionLabels"); //$NON-NLS-1$

    private IArchimateRelationship fRelationship;

    // Saved state
    private List<ConnectionState> fConnectionStates;

    private record ConnectionState(IDiagramModelArchimateConnection connection, int textPosition, List<IDiagramModelBendpoint> bendpoints) {}

    public InvertConnectionCommand(IArchimateRelationship relationship) {
        if(relationship == null) {
            throw new IllegalArgumentException();
        }

        fRelationship = relationship;
        setLabel(Messages.InvertConnectionCommand_0);
    }

    @Override
    public void execute() {
        fConnectionStates = new ArrayList<>();
        
        for(IDiagramModelArchimateConnection connection : fRelationship.getReferencingDiagramConnections()) {
            List<IDiagramModelBendpoint> bendpoints = (List<IDiagramModelBendpoint>)EcoreUtil.copyAll(connection.getBendpoints());
            fConnectionStates.add(new ConnectionState(connection, connection.getTextPosition(), bendpoints));
        }

        doInvert();
    }

    @Override
    public void redo() {
        doInvert();
    }

    @Override
    public void undo() {
        // Restore each diagram connection
        for(ConnectionState state : fConnectionStates) {
            // Bendpoints
            for(int i = 0; i < state.connection().getBendpoints().size(); i++) {
                IDiagramModelBendpoint bp = state.connection().getBendpoints().get(i);
                IDiagramModelBendpoint bpOld = state.bendpoints().get(i);
                bp.setStartX(bpOld.getStartX());
                bp.setStartY(bpOld.getStartY());
                bp.setEndX(bpOld.getEndX());
                bp.setEndY(bpOld.getEndY());
            }

            // Text position
            state.connection().setTextPosition(state.textPosition());

            // This last
            state.connection().connect(state.connection().getTarget(), state.connection().getSource());
        }
    }

    private void doInvert() {
        // Swap source/target on each diagram connection and adjust bendpoints/text position
        for(ConnectionState state : fConnectionStates) {
            // Bendpoints in reverse
            List<IDiagramModelBendpoint> reversed = state.bendpoints().reversed();
            
            for(int i = 0; i < state.connection().getBendpoints().size(); i++) {
                IDiagramModelBendpoint bp = state.connection().getBendpoints().get(i);
                IDiagramModelBendpoint bpReverse = reversed.get(i);
                bp.setStartX(bpReverse.getEndX()); // old endX
                bp.setStartY(bpReverse.getEndY()); // old endY
                bp.setEndX(bpReverse.getStartX()); // old startX
                bp.setEndY(bpReverse.getStartY()); // old startY
            }
            
            // Swap text position: SOURCE↔TARGET, MIDDLE stays
            if(INVERT_LABEL_POSITIONS) {
                switch(state.connection().getTextPosition()) {
                    case IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE -> {
                        state.connection().setTextPosition(IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET);
                    }
                    case IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET -> {
                        state.connection().setTextPosition(IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE);
                    }
                }
            }

            // This last
            state.connection().connect(state.connection().getTarget(), state.connection().getSource());
        }
    }

    @Override
    public void dispose() {
        fRelationship = null;
        fConnectionStates = null;
    }
}
