/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelReference;



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
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(fReference);
        Dimension size = provider.getDefaultSize();
        fReference.setBounds(x, y, size.width, size.height);
        
        fReference.setGradient(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_GRADIENT));
        
        ColorFactory.setDefaultColors(fReference);
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