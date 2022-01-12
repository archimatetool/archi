/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



/**
 * IfRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class IfRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IfRendererTests.class);
    }
    
    private IfRenderer renderer = new IfRenderer();
    
    private TextRenderer textRenderer = TextRenderer.getDefault();
    
    private IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
    
    @Override
    protected IfRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_IfThen_Name() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:${name}}");
        assertEquals("Concept Name", result);
    }
    
    @Test
    public void render_IfThen_NoName() {
        dmo = TextRendererTests.createDiagramModelObject();
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:${name}}");
        assertEquals("", result);
    }

    @Test
    public void render_IfThen_Specialization() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${specialization}:<<${specialization}}>>");
        assertEquals("<<Profile 1>>", result);
    }
    
    @Test
    public void render_IfThen_NoSpecialization() {
        dmo = TextRendererTests.createDiagramModelObject();
        dmo.getArchimateConcept().getProfiles().clear();
        String result = textRenderer.renderWithExpression(dmo, "${if:${specialization}:<<${specialization}>>}");
        assertEquals("", result);
    }
    
    @Test
    public void render_IfThenElse_Name() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:${name}:(${type})}");
        assertEquals("Concept Name", result);
    }

    @Test
    public void render_IfThenElse_NoName() {
        dmo = TextRendererTests.createDiagramModelObject();
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:${name}:${type}}");
        assertEquals("Business Actor", result);
    }
    
    @Test
    public void render_IfThenElse_Specialization() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${specialization}:<<${specialization}>>:(${type})}");
        assertEquals("<<Profile 1>>", result);
    }

    @Test
    public void render_IfThenElse_NoSpecialization() {
        dmo = TextRendererTests.createDiagramModelObject();
        dmo.getArchimateConcept().getProfiles().clear();
        String result = textRenderer.renderWithExpression(dmo, "${if:${specialization}:<<${specialization}>>:(${type})}");
        assertEquals("(Business Actor)", result);
    }
    
    @Test
    public void render_Nvl_Name() {
        String result = textRenderer.renderWithExpression(dmo, "${nvl:${name}:Hello}");
        assertEquals("Concept Name", result);
    }

    @Test
    public void render_Nvl_NoName() {
        dmo = TextRendererTests.createDiagramModelObject();
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo, "${nvl:${name}:Hello}");
        assertEquals("Hello", result);
    }
    
    @Test
    public void render_Nvl_Specialization() {
        String result = textRenderer.renderWithExpression(dmo, "(${nvl:${specialization}:${type}})");
        assertEquals("(Profile 1)", result);
    }

    @Test
    public void render_Nvl_NoSpecialization() {
        dmo = TextRendererTests.createDiagramModelObject();
        dmo.getArchimateConcept().getProfiles().clear();
        String result = textRenderer.renderWithExpression(dmo, "(${nvl:${specialization}:${type}})");
        assertEquals("(Business Actor)", result);
    }
    
    @Test
    public void render_IfThen_Property() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${property:k1}:${name}}");
        assertEquals("Concept Name", result);
    }

    @Test
    public void render_IfThenElse_Property() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${property:k1}:${name}:${type}}");
        assertEquals("Concept Name", result);
    }

    @Test
    public void render_IfThenElse_NoProperty() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${property:k8}:${name}:${type}}");
        assertEquals("Business Actor", result);
    }
    
    @Test
    public void render_EscapeColon1() {
        String result = textRenderer.renderWithExpression(dmo, "${if:not\\:Empty:Is\\:OK:Not\\:Shown}");
        assertEquals("Is:OK", result);
    }

    @Test
    public void render_EscapeColon2() {
        String result = textRenderer.renderWithExpression(dmo, "${if::Not\\:Shown:Is\\:KO}");
        assertEquals("Is:KO", result);
    }
}
