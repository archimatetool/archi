/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine.Builder;
import org.eclipse.gef.commands.CommandStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.tests.TestData;


public class LoadModelFromFileProviderTests {
    
    LoadModelFromFileProvider provider;
    
    @BeforeEach
    public void runOnceBeforeEachTest() {
        provider = new LoadModelFromFileProvider();
        provider.doLog = false;
    }
    
    @Test
    public void getOptionsNotNull() {
        assertNotNull(provider.getOptions());
    }
    
    @Test
    public void runProvider() throws Exception {
        CommandLineState.setModel(null);
        
        String filePath = TestData.TEST_MODEL_FILE_ARCHISURANCE.getAbsolutePath();
        
        Option option = new Option(LoadModelFromFileProvider.OPTION_LOAD_FILE_MODEL, null);
        option.getValuesList().add(filePath);
        
        CommandLine commandLine = new Builder()
                .addOption(option)
                .build();

        provider.run(commandLine); 
        
        IArchimateModel model = CommandLineState.getModel();
        assertNotNull(model);
        
        assertNotNull(model.getAdapter(IArchiveManager.class));
        assertNotNull(model.getAdapter(CommandStack.class));
        
        assertEquals(TestData.TEST_MODEL_FILE_ARCHISURANCE, model.getFile());
    }
}
