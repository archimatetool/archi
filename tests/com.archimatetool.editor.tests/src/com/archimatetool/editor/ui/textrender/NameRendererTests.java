/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IFolder;

import junit.framework.JUnit4TestAdapter;



/**
 * NameRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class NameRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NameRendererTests.class);
    }
    
    private NameRenderer renderer = new NameRenderer();
    
    private IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
    private IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
    
    @Override
    protected NameRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Name() {
        String result = renderer.render(dmo, "${name}");
        assertEquals("Concept Name", result);
    }
    
    @Test
    public void render_ModelName() {
        String result = renderer.render(dmo, "$model{name}");
        assertEquals("Model Name", result);
    }
    
    @Test
    public void render_ViewName() {
        String result = renderer.render(dmo, "$view{name}");
        assertEquals("View Name", result);
    }

    @Test
    public void render_ModelFolderName() {
        String result = renderer.render(dmo, "$mfolder{name}");
        assertEquals("Business", result);
    }
    
    @Test
    public void render_ViewFolderName() {
        String result = renderer.render(dmo, "$vfolder{name}");
        assertEquals("Views", result);
    }

    @Test
    public void render_SourceName() {
        String result = renderer.render(dmc, "$source{name}");
        assertEquals("Source Concept", result);
    }
    
    @Test
    public void render_ParentName() {
        String result = renderer.render(dmo, "$parent{name}");
        assertEquals("View Name", result);
        
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setName("Nemo");
        group.getChildren().add(dmo);
        result = renderer.render(dmo, "$parent{name}");
        assertEquals("Nemo", result);
        
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        folder.setName("Folder Name");
        IArchimateModelObject child = IArchimateFactory.eINSTANCE.createBusinessActor();
        folder.getElements().add(child);
        result = renderer.render(child, "$parent{name}");
        assertEquals("Folder Name", result);
    }

    @Test
    public void render_TargetName() {
        String result = renderer.render(dmc, "$target{name}");
        assertEquals("Target Concept", result);
    }

    @Test
    public void render_ConnectedSourceName() {
        String result = renderer.render(dmc.getTarget(), "$assignment:source{name}");
        assertEquals("Source Concept", result);
    }
    
    @Test
    public void render_ConnectedTargetName() {
        String result = renderer.render(dmc.getSource(), "$assignment:target{name}");
        assertEquals("Target Concept", result);
    }

}
