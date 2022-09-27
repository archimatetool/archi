/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import org.eclipse.gef.commands.CommandStack;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateModel;

/**
 * Command Line State
 * Represents current state across all Command Line Providers
 * 
 * @author Phillip Beauvoir
 */
public class CommandLineState {
    
    /**
     * The single loaded model
     */
    private static IArchimateModel singletonModel;
    
    public static IArchimateModel getModel() {
        return singletonModel;
    }
    
    public static void setModel(IArchimateModel model) {
        singletonModel = model;
        
        // Check that the model has a command stack and an ArchiveManager
        if(model != null) {
            if(model.getAdapter(CommandStack.class) == null) {
                model.setAdapter(CommandStack.class, new CommandStack());
            }
            if(model.getAdapter(IArchiveManager.class) == null) {
                model.setAdapter(IArchiveManager.class, IArchiveManager.FACTORY.createArchiveManager(model));
            }
        }
    }
    
}
