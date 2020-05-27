/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



/**
 * PropertiesRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class PropertiesRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PropertiesRendererTests.class);
    }
    
    private PropertiesRenderer renderer = new PropertiesRenderer();
    
    @Override
    protected PropertiesRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_PropertyKey() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("concept_v1", renderer.render(dmo, "${property:k1}"));
        assertEquals("concept_v2", renderer.render(dmo, "${property:k2}"));
        assertEquals("concept_v3", renderer.render(dmo, "${property:k3}"));
        assertEquals("concept_v1 concept_v2", renderer.render(dmo, "${property:k1} ${property:k2}"));
        assertEquals("concept_v1\nconcept_v2", renderer.render(dmo, "${property:k1}\n${property:k2}"));
    }
    
    @Test
    public void render_PropertyKey_Model() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("model_v1", renderer.render(dmo, "$model{property:k1}"));
        assertEquals("model_v2", renderer.render(dmo, "$model{property:k2}"));
        assertEquals("model_v3", renderer.render(dmo, "$model{property:k3}"));
        assertEquals("model_v1 model_v2", renderer.render(dmo, "$model{property:k1} $model{property:k2}"));
        assertEquals("model_v1\nmodel_v2", renderer.render(dmo, "$model{property:k1}\n$model{property:k2}"));
    }

    @Test
    public void render_PropertyKey_View() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_v1", renderer.render(dmo, "$view{property:k1}"));
        assertEquals("view_v2", renderer.render(dmo, "$view{property:k2}"));
        assertEquals("view_v3", renderer.render(dmo, "$view{property:k3}"));
        assertEquals("view_v1 view_v2", renderer.render(dmo, "$view{property:k1} $view{property:k2}"));
        assertEquals("view_v1\nview_v2", renderer.render(dmo, "$view{property:k1}\n$view{property:k2}"));
    }

    @Test
    public void render_PropertyKey_ModelFolder() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("concept_folder_v1", renderer.render(dmo, "$mfolder{property:k1}"));
        assertEquals("concept_folder_v2", renderer.render(dmo, "$mfolder{property:k2}"));
        assertEquals("concept_folder_v3", renderer.render(dmo, "$mfolder{property:k3}"));
        assertEquals("concept_folder_v1 concept_folder_v2", renderer.render(dmo, "$mfolder{property:k1} $mfolder{property:k2}"));
        assertEquals("concept_folder_v1\nconcept_folder_v2", renderer.render(dmo, "$mfolder{property:k1}\n$mfolder{property:k2}"));
    }

    @Test
    public void render_PropertyKey_ViewFolder() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_folder_v1", renderer.render(dmo, "$vfolder{property:k1}"));
        assertEquals("view_folder_v2", renderer.render(dmo, "$vfolder{property:k2}"));
        assertEquals("view_folder_v3", renderer.render(dmo, "$vfolder{property:k3}"));
        assertEquals("view_folder_v1 view_folder_v2", renderer.render(dmo, "$vfolder{property:k1} $vfolder{property:k2}"));
        assertEquals("view_folder_v1\nview_folder_v2", renderer.render(dmo, "$vfolder{property:k1}\n$vfolder{property:k2}"));
    }

    @Test
    public void render_PropertyKey_Parent() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_v1", renderer.render(dmo, "$parent{property:k1}"));
        assertEquals("view_v2", renderer.render(dmo, "$parent{property:k2}"));
        assertEquals("view_v3", renderer.render(dmo, "$parent{property:k3}"));
        assertEquals("view_v1 view_v2", renderer.render(dmo, "$parent{property:k1} $parent{property:k2}"));
        assertEquals("view_v1\nview_v2", renderer.render(dmo, "$parent{property:k1}\n$parent{property:k2}"));
    }

    @Test
    public void render_PropertyKey_Source() {
        IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
        assertEquals("sconcept_v1", renderer.render(dmc, "$source{property:k1}"));
        assertEquals("sconcept_v2", renderer.render(dmc, "$source{property:k2}"));
        assertEquals("sconcept_v3", renderer.render(dmc, "$source{property:k3}"));
        assertEquals("sconcept_v1 sconcept_v2", renderer.render(dmc, "$source{property:k1} $source{property:k2}"));
        assertEquals("sconcept_v1\nsconcept_v2", renderer.render(dmc, "$source{property:k1}\n$source{property:k2}"));
    }

    @Test
    public void render_PropertyKey_Target() {
        IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
        assertEquals("tconcept_v1", renderer.render(dmc, "$target{property:k1}"));
        assertEquals("tconcept_v2", renderer.render(dmc, "$target{property:k2}"));
        assertEquals("tconcept_v3", renderer.render(dmc, "$target{property:k3}"));
        assertEquals("tconcept_v1 tconcept_v2", renderer.render(dmc, "$target{property:k1} $target{property:k2}"));
        assertEquals("tconcept_v1\ntconcept_v2", renderer.render(dmc, "$target{property:k1}\n$target{property:k2}"));
    }

    @Test
    public void render_PropertyKey_ConnectedSource() {
        IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
        assertEquals("sconcept_v1", renderer.render(dmc.getTarget(), "$assignment:source{property:k1}"));
        assertEquals("sconcept_v2", renderer.render(dmc.getTarget(), "$assignment:source{property:k2}"));
        assertEquals("sconcept_v3", renderer.render(dmc.getTarget(), "$assignment:source{property:k3}"));
        assertEquals("sconcept_v1 sconcept_v2", renderer.render(dmc.getTarget(), "$assignment:source{property:k1} $assignment:source{property:k2}"));
        assertEquals("sconcept_v1\nsconcept_v2", renderer.render(dmc.getTarget(), "$assignment:source{property:k1}\n$assignment:source{property:k2}"));
    }

    @Test
    public void render_PropertyKey_ConnectedTarget() {
        IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
        assertEquals("tconcept_v1", renderer.render(dmc.getSource(), "$assignment:target{property:k1}"));
        assertEquals("tconcept_v2", renderer.render(dmc.getSource(), "$assignment:target{property:k2}"));
        assertEquals("tconcept_v3", renderer.render(dmc.getSource(), "$assignment:target{property:k3}"));
        assertEquals("tconcept_v1 tconcept_v2", renderer.render(dmc.getSource(), "$assignment:target{property:k1} $assignment:target{property:k2}"));
        assertEquals("tconcept_v1\ntconcept_v2", renderer.render(dmc.getSource(), "$assignment:target{property:k1}\n$assignment:target{property:k2}"));
    }

    @Test
    public void render_Properties() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("k1: concept_v1\nk2: concept_v2\nk3: concept_v3\nk3: concept_v4", renderer.render(dmo, "${properties}"));
    }
    
    @Test
    public void render_Properties_Model() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("k1: model_v1\nk2: model_v2\nk3: model_v3\nk3: model_v4", renderer.render(dmo, "$model{properties}"));
    }

    @Test
    public void render_Properties_View() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("k1: view_v1\nk2: view_v2\nk3: view_v3\nk3: view_v4", renderer.render(dmo, "$view{properties}"));
    }

    @Test
    public void render_Properties_Parent() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        String result = renderer.render(dmo, "$parent{properties}");
        assertEquals("k1: view_v1\nk2: view_v2\nk3: view_v3\nk3: view_v4", result);
    }

    @Test
    public void render_PropertiesValues() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("concept_v1\nconcept_v2\nconcept_v3\nconcept_v4", renderer.render(dmo, "${propertiesvalues}"));
    }
    
    @Test
    public void render_PropertiesValues_Model() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("model_v1\nmodel_v2\nmodel_v3\nmodel_v4", renderer.render(dmo, "$model{propertiesvalues}"));
    }

    @Test
    public void render_PropertiesValues_View() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_v1\nview_v2\nview_v3\nview_v4", renderer.render(dmo, "$view{propertiesvalues}"));
    }

    @Test
    public void render_PropertiesValues_Parent() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_v1\nview_v2\nview_v3\nview_v4", renderer.render(dmo, "$parent{propertiesvalues}"));
    }

    @Test
    public void render_PropertiesValues_CustomList() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("concept_v3\nconcept_v4", renderer.render(dmo, "${properties:\n:k3}"));
    }
    
    @Test
    public void render_PropertiesValues_CustomList_Model() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("model_v3\nmodel_v4", renderer.render(dmo, "$model{properties:\n:k3}"));
    }

    @Test
    public void render_PropertiesValues_CustomList_View() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_v3\nview_v4", renderer.render(dmo, "$view{properties:\n:k3}"));
    }
    
    @Test
    public void render_PropertiesValues_CustomList_Parent() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("view_v3\nview_v4", renderer.render(dmo, "$parent{properties:\n:k3}"));
    }

}
