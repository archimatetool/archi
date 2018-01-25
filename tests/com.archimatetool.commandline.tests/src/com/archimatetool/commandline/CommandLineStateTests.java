/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;

import junit.framework.JUnit4TestAdapter;


public class CommandLineStateTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CommandLineStateTests.class);
    }
    
    @Test
    public void getModel_IsSet() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        CommandLineState.setModel(model);
        assertSame(model, CommandLineState.getModel());

        CommandLineState.setModel(null);
        assertNull(CommandLineState.getModel());
    }
    
}
