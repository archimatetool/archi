/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelReference;

/**
 * Add Mini-Diagram Model Reference Command Use when dragging and dropping a
 * Diagram Model from the tree to the diagram. Mini-Diagram Model Reference is a
 * specially pre-formatted Diagram Model Reference
 * 
 * @author Andy Turner
 */
public class AddMiniDiagramModelReferenceCommand extends Command {
    private final static int miniRefSize = 25;

    private IDiagramModelContainer fParent;
    private IDiagramModelReference fReference;

    public AddMiniDiagramModelReferenceCommand(IDiagramModelContainer parent, IDiagramModel diagramModel, int x,
            int y) {
        setLabel(Messages.AddDiagramModelReferenceCommand_0);

        fParent = parent;
        fReference = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        fReference.setReferencedModel(diagramModel);

        // Set color as parent and set position to bottom right (if x,y if
        // parent bottom right)
        fReference.setFillColor(ColorFactory.convertColorToString(ColorFactory.getDefaultFillColor(parent)));
        fReference.setBounds(x - miniRefSize, y - miniRefSize, miniRefSize, miniRefSize);
    }

    @Override
    public void execute() {
        fParent.getChildren().add(fReference);
    }

    @Override
    public void undo() {
        fParent.getChildren().remove(fReference);
    }

    @Override
    public void dispose() {
        fParent = null;
        fReference = null;
    }
}