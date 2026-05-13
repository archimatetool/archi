/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.diagram.commands.ConnectionTextRelativePositionCommand;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Connection Label Relative Position Composite
 * 
 * @author Phillip Beauvoir
 */
class DiagramConnectionLabelRelativePositionComposite {
    
    private record Position(String text, int position) {
        // Get combo index of given position
        static int indexOf(int position) {
            for(int i = 0; i < comboTextRelativePositions.length; i++) {
                if(comboTextRelativePositions[i].position() == position) {
                    return i;
                }
            }
            return 0;
        }
    }
    
    private static final Position[] comboTextRelativePositions = {
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_0, IDiagramModelConnection.CENTER),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_1, IDiagramModelConnection.NORTH),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_2, IDiagramModelConnection.SOUTH),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_3, IDiagramModelConnection.EAST),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_4, IDiagramModelConnection.WEST),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_5, IDiagramModelConnection.NORTH_EAST),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_6, IDiagramModelConnection.NORTH_WEST),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_7, IDiagramModelConnection.SOUTH_EAST),
            new Position(Messages.DiagramConnectionLabelRelativePositionComposite_8, IDiagramModelConnection.SOUTH_WEST),
    };
    
    
    private Combo combo;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    DiagramConnectionLabelRelativePositionComposite(AbstractECorePropertySection section, Composite parent) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createControl(composite);
    }
    
    private void createControl(Composite parent) {
        section.createLabel(parent, Messages.DiagramConnectionLabelRelativePositionComposite_9 + ":", //$NON-NLS-1$
                ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        combo = new Combo(parent, SWT.READ_ONLY);
        section.getWidgetFactory().adapt(combo, true, true);
        GridDataFactory.create(SWT.NONE).grab(true, false).applyTo(combo);

        for(Position pos : comboTextRelativePositions) {
            combo.add(pos.text());
        }
        
        combo.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            CompoundCommand result = new CompoundCommand();

            for(EObject connection : section.getEObjects()) {
                if(section.isAlive(connection)) {
                    Command cmd = new ConnectionTextRelativePositionCommand((IDiagramModelConnection)connection,
                                                                            comboTextRelativePositions[combo.getSelectionIndex()].position());
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }

            section.executeCommand(result.unwrap());
        }));
    }
    
    void updateControl() {
        IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)section.getFirstSelectedObject();
        combo.select(Position.indexOf(lastSelectedConnection.getRelativePosition()));
        combo.setEnabled(!section.isLocked(lastSelectedConnection)
                && lastSelectedConnection.getTextPosition() == IDiagramModelConnection.CONNECTION_TEXT_POSITION_MIDDLE);
    }
    
    void dispose() {
        composite.dispose();
        composite = null;
        section = null;
        combo = null;
    }
}
