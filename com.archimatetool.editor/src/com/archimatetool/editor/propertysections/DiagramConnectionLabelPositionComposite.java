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

import com.archimatetool.editor.diagram.commands.ConnectionTextPositionCommand;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Connection Label Position Composite
 * 
 * @author Phillip Beauvoir
 */
class DiagramConnectionLabelPositionComposite {
    
    private static final String[] comboTextPositionItems = {
            Messages.DiagramConnectionLabelPositionComposite_0,
            Messages.DiagramConnectionLabelPositionComposite_1,
            Messages.DiagramConnectionLabelPositionComposite_2
    };
    
    private Combo combo;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    DiagramConnectionLabelPositionComposite(AbstractECorePropertySection section, Composite parent) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createControl(composite);
    }
    
    private void createControl(Composite parent) {
        section.createLabel(parent, Messages.DiagramConnectionLabelPositionComposite_3 + ":", //$NON-NLS-1$
                ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        combo = new Combo(parent, SWT.READ_ONLY);
        section.getWidgetFactory().adapt(combo, true, true);
        GridDataFactory.create(SWT.NONE).grab(true, false).applyTo(combo);
        combo.setItems(comboTextPositionItems);
        
        combo.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            CompoundCommand result = new CompoundCommand();

            for(EObject connection : section.getEObjects()) {
                if(section.isAlive(connection)) {
                    Command cmd = new ConnectionTextPositionCommand((IDiagramModelConnection)connection, combo.getSelectionIndex());
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
        combo.select(lastSelectedConnection.getTextPosition());
        combo.setEnabled(!section.isLocked(lastSelectedConnection));
    }
    
    void dispose() {
        composite.dispose();
        composite = null;
        section = null;
        combo = null;
    }
}
