/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.gef.commands.CommandStack;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;


public class CommandLineStateTests {
    
    @Test
    public void getModel_IsSet() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        CommandLineState.setModel(model);
        assertSame(model, CommandLineState.getModel());
        
        assertNotNull(model.getAdapter(IArchiveManager.class));
        assertNotNull(model.getAdapter(CommandStack.class));

        CommandLineState.setModel(null);
        assertNull(CommandLineState.getModel());
    }
    
}
