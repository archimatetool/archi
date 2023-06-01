/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;


@SuppressWarnings("nls")
public class SketchModelFactoryTests {
    
    @Test
    public void testIsUsedFor() {
        ICreationFactory factory = new SketchModelFactory(null);
        assertTrue(factory.isUsedFor(new SketchEditor()));
        assertFalse(factory.isUsedFor(new ArchimateDiagramEditor()));
    }
    
    @Test
    public void testGetNewObjectGroup() {
        ICreationFactory factory = new SketchModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        IDiagramModelGroup group = (IDiagramModelGroup)factory.getNewObject();
        assertEquals("Group", group.getName());
    }
    
    @Test
    public void testGetNewObjectActor() {
        ICreationFactory factory = new SketchModelFactory(IArchimatePackage.eINSTANCE.getSketchModelActor());
        ISketchModelActor actor = (ISketchModelActor)factory.getNewObject();
        assertEquals("Actor", actor.getName());
    }
    
    @Test
    public void testGetNewObjectSticky() {
        ICreationFactory factory = new SketchModelFactory(IArchimatePackage.eINSTANCE.getSketchModelSticky(), new RGB(0, 0, 0));
        ISketchModelSticky sticky = (ISketchModelSticky)factory.getNewObject();
        assertEquals("Sticky", sticky.getName());
        assertEquals("#000000", sticky.getFillColor());
    }

    @Test
    public void testGetNewObjectConnection() {
        ICreationFactory factory = new SketchModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelConnection(), 2);
        IDiagramModelConnection connection = (IDiagramModelConnection)factory.getNewObject();
        assertEquals("", connection.getName());
        assertEquals(2, connection.getType());
    }
}
