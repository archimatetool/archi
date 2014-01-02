/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.editor.model.IEditorModelManager;



/**
 * Compound Command that will set a property to listeners to ignore notifications.
 * Used where many commands might cause excessive amounts of responses in clients listening to model changes. 
 * 
 * @author Phillip Beauvoir
 */
public class NonNotifyingCompoundCommand extends CompoundCommand {

    public NonNotifyingCompoundCommand() {
    }

    public NonNotifyingCompoundCommand(String label) {
        super(label);
    }
    
    @Override
    public void execute() {
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);
        
        super.execute();
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
    
    @Override
    public void undo() {
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);
        
        super.undo();
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
    
    @Override
    public void redo() { // redo() as called by CompoundCommand is *not* the same as execute()!
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);
        
        super.redo();
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
}
