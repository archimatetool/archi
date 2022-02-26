/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateRequest;

import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Create New Diagram Archimate Object Command
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramArchimateObjectCommand extends CreateDiagramObjectCommand {
    
    /**
     * Create Nested Relation Command
     */
    protected CompoundCommand subCommand = new CompoundCommand();
    
    public CreateDiagramArchimateObjectCommand(EditPart parentEditPart, CreateRequest request, Rectangle bounds) {
        super(parentEditPart, request, bounds);
    }
    
    @Override
    public void execute() {
        addChild();
        
        // Create Nested Connection/Relation as well if prefs and parent is an ArchiMate object
        if(ConnectionPreferences.createRelationWhenAddingNewElement() && fParent instanceof IDiagramModelArchimateObject) {
            Command cmd = new CreateNestedArchimateConnectionsWithDialogCommand((IDiagramModelArchimateObject)fParent,
                    (IDiagramModelArchimateObject)fChild);
            subCommand.add(cmd);
            subCommand.execute();
        }
        
        // Edit name
        editNameOfNewObject();
    }
    
    @Override
    public void undo() {
        super.undo();
        
        // Remove the Archimate model object from its containing folder
        ((IDiagramModelArchimateObject)fChild).removeArchimateConceptFromModel();
        
        subCommand.undo();
    }

    @Override
    public void redo() {
        // This first
        super.redo();

        // Add the Archimate model object to a default folder
        ((IDiagramModelArchimateObject)fChild).addArchimateConceptToModel(null);
        
        subCommand.redo();
    }
    
    @Override
    protected Dimension getPreferredSize() {
        Object object = fRequest.getNewObjectType();
        
        // Junction size should be in-built default
        if(object == IArchimatePackage.eINSTANCE.getJunction()) {
            IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProviderForClass((EClass)object);
            return provider.getDefaultSize();
        }
        
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        subCommand.dispose();
    }
}