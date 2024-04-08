/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline.providers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.cli.CommandLine;
import org.eclipse.gef.commands.CommandStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateModel;


public class CreateEmptyModelProviderTests {
    
    CreateEmptyModelProvider provider;
    
    @BeforeEach
    public void runOnceBeforeEachTest() {
        provider = new CreateEmptyModelProvider();
        provider.doLog = false;
    }
    
    @Test
    public void getOptionsNotNull() {
        assertNotNull(provider.getOptions());
    }
    
    @Test
    public void runCreatesEmptyModel() throws Exception {
        CommandLineState.setModel(null);
        
        CommandLine commandLine = mock(CommandLine.class);
        when(commandLine.hasOption(CreateEmptyModelProvider.OPTION_CREATE_EMPTY_MODEL)).thenReturn(true);
        provider.run(commandLine);
        
        IArchimateModel model = CommandLineState.getModel();
        assertNotNull(model);
        
        assertNotNull(model.getAdapter(IArchiveManager.class));
        assertNotNull(model.getAdapter(CommandStack.class));
    }
}
