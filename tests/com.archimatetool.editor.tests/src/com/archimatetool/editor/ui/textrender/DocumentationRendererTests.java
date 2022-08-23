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
 * DocumentationRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class DocumentationRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DocumentationRendererTests.class);
    }
    
    private DocumentationRenderer renderer = new DocumentationRenderer();
    
    private IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
    private IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
    
    @Override
    protected DocumentationRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Documentation() {
        String result = renderer.render(dmo, "${documentation}");
        assertEquals("Concept Documentation", result);
    }
    
    @Test
    public void render_Doc() {
        String result = renderer.render(dmo, "${doc}");
        assertEquals("Concept Documentation", result);
    }

    @Test
    public void render_ModelDocumentation() {
        String result = renderer.render(dmo, "$model{documentation}");
        assertEquals("Model Purpose", result);
    }
    
    @Test
    public void render_ViewDocumentation() {
        String result = renderer.render(dmo, "$view{documentation}");
        assertEquals("View Documentation", result);
    }

    @Test
    public void render_ModelFolderDocumentation() {
        String result = renderer.render(dmo, "$mfolder{documentation}");
        assertEquals("Concept Folder Documentation", result);
    }
    
    @Test
    public void render_ViewFolderDocumentation() {
        String result = renderer.render(dmo, "$vfolder{documentation}");
        assertEquals("View Folder Documentation", result);
    }

    @Test
    public void render_ParentDocumentation() {
        String result = renderer.render(dmo, "$parent{documentation}");
        assertEquals("View Documentation", result);
        
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setDocumentation("docco");
        group.getChildren().add(dmo);
        result = renderer.render(dmo, "$parent{documentation}");
        assertEquals("docco", result);
        
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        folder.setDocumentation("Folder Doc");
        IArchimateModelObject child = IArchimateFactory.eINSTANCE.createBusinessActor();
        folder.getElements().add(child);
        result = renderer.render(child, "$parent{documentation}");
        assertEquals("Folder Doc", result);
    }

    @Test
    public void render_SourceDocumentation() {
        String result = renderer.render(dmc, "$source{documentation}");
        assertEquals("Source Documentation", result);
    }
    
    @Test
    public void render_TargetDocumentation() {
        String result = renderer.render(dmc, "$target{documentation}");
        assertEquals("Target Documentation", result);
    }

    @Test
    public void render_ConnectedSourceDocumentation() {
        String result = renderer.render(dmc.getTarget(), "$assignment:source{documentation}");
        assertEquals("Source Documentation", result);
    }
    
    @Test
    public void render_ConnectedTargetDocumentation() {
        String result = renderer.render(dmc.getSource(), "$assignment:target{documentation}");
        assertEquals("Target Documentation", result);
    }

}
