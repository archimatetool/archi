/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelReference;


/**
 * Add Diagram Model Reference Command
 * Use when dragging and dropping a Diagram Model from the tree to the diagram.
 * 
 * @author Phillip Beauvoir
 */
public class AddDiagramModelReferenceCommand extends Command {
    
    private IDiagramModelContainer fParent;
    private IDiagramModelReference fReference;

    public AddDiagramModelReferenceCommand(IDiagramModelContainer parent, IDiagramModel diagramModel, int x, int y) {
        setLabel(Messages.AddDiagramModelReferenceCommand_0);
        
        fParent = parent;
        fReference = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        fReference.setReferencedModel(diagramModel);
        fReference.setBounds(x, y, -1, -1);
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