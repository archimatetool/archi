/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



/**
 * TypeRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TypeRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TypeRendererTests.class);
    }
    
    private TypeRenderer renderer = new TypeRenderer();
    
    private IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
    private IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
    
    @Override
    protected TypeRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Type_Object() {
        String result = renderer.render(dmo, "${type}");
        assertEquals("Business Actor", result);
    }
    
    @Test
    public void render_Type_Relation() {
        String result = renderer.render(dmc, "${type}");
        assertEquals("Assignment relation", result);
    }
    
    @Test
    public void render_Type_Note() {
        String result = renderer.render(IArchimateFactory.eINSTANCE.createDiagramModelNote(), "${type}");
        assertEquals("Note", result);
    }

    @Test
    public void render_Type_Group() {
        String result = renderer.render(IArchimateFactory.eINSTANCE.createDiagramModelGroup(), "${type}");
        assertEquals("Group", result);
    }
    
    @Test
    public void render_Type_Connection() {
        String result = renderer.render(IArchimateFactory.eINSTANCE.createDiagramModelConnection(), "${type}");
        assertEquals("Connection", result);
    }

    @Test
    public void render_Type_Reference() {
        String result = renderer.render(IArchimateFactory.eINSTANCE.createDiagramModelReference(), "${type}");
        assertEquals("View Reference", result);
    }
}
