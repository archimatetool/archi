/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Add Diagram Object Command
 * 
 * @author Phillip Beauvoir
 */
public class AddDiagramObjectCommand extends Command {
    
    private IDiagramModelContainer fParent;
    private IDiagramModelObject fChild;

    public AddDiagramObjectCommand(IDiagramModelContainer parent, IDiagramModelObject object) {
        fParent = parent;
        fChild = object;
        setLabel(NLS.bind(Messages.AddDiagramObjectCommand_0, object.getName()));
    }

    @Override
    public void execute() {
        fParent.getChildren().add(fChild);
    }

    @Override
    public void undo() {
        fParent.getChildren().remove(fChild);
    }
    
    @Override
    public void dispose() {
        fParent = null;
        fChild = null;
    }
}