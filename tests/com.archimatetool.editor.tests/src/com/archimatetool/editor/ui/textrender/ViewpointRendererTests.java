/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



/**
 * ViewpointRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ViewpointRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewpointRendererTests.class);
    }
    
    private ViewpointRenderer renderer = new ViewpointRenderer();
    
    @Override
    protected ViewpointRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Viewpoint1() {
        String result = renderer.render(TextRendererTests.createDiagramModelObject(), "${viewpoint}");
        assertEquals("None", result);
    }
    
    @Test
    public void render_Viewpoint2() {
        String result = renderer.render(TextRendererTests.createDiagramModelConnection(), "${viewpoint}");
        assertEquals("None", result);
    }
    
    @Test
    public void render_Viewpoint3() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        ((IArchimateDiagramModel)dmo.getDiagramModel()).setViewpoint("organization");
        
        String result = renderer.render(dmo, "${viewpoint}");
        assertEquals("Organization", result);
    }
    
}
