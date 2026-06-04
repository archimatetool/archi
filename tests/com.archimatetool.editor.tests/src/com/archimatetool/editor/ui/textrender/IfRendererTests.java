/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IProperty;

/**
 * IfRenderer Tests – covers basic rendering, nested expressions, escape sequences,
 * and real condition evaluation.
 *
 * @author Phillip Beauvoir, Jean-Baptiste Sarrodie
 */
@SuppressWarnings("nls")
public class IfRendererTests extends AbstractTextRendererTests {

    private IfRenderer renderer = new IfRenderer();

    private TextRenderer textRenderer = TextRenderer.getDefault();

    private IDiagramModelArchimateObject dmo;

    @BeforeEach
    public void setUp() {
        dmo = TextRendererTests.createDiagramModelObject();
    }

    @Override
    protected IfRenderer getRenderer() {
        return renderer;
    }

    // ----------------------------------------------------------------------
    // Original tests (unchanged except for usage of the shared dmo)
    // ----------------------------------------------------------------------
    @Test
    public void render_IfThen_Name() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:${name}}");
        assertEquals("Concept Name", result);
    }

    @Test
    public void render_IfThen_NoName() {
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

    // ----------------------------------------------------------------------
    // New tests for advanced features
    // ----------------------------------------------------------------------

    /**
     * Tests if-then-else with a custom property as the condition.
     * Verifies true/false/missing property behavior.
     */
    @Test
    public void render_IfThenElse_WithCustomPropertyCondition() {
        // Add a property "status" with value "active"
        IProperty statusProp = IArchimateFactory.eINSTANCE.createProperty();
        statusProp.setKey("status");
        statusProp.setValue("active");
        dmo.getArchimateConcept().getProperties().add(statusProp);

        String result = textRenderer.renderWithExpression(dmo, "${if:${property:status}:actif:inactif}");
        assertEquals("actif", result);

        // Empty the property -> condition false -> else branch
        statusProp.setValue("");
        result = textRenderer.renderWithExpression(dmo, "${if:${property:status}:actif:inactif}");
        assertEquals("inactif", result);

        // Remove the property entirely -> resolves to blank -> else
        dmo.getArchimateConcept().getProperties().remove(statusProp);
        result = textRenderer.renderWithExpression(dmo, "${if:${property:status}:actif:inactif}");
        assertEquals("inactif", result);
    }

    /**
     * Tests that then/else branches can contain nested expressions
     * that are fully resolved before the final choice.
     */
    @Test
    public void render_IfThenElse_WithNestedExpressionsInBranches() {
        // Create a "label" property
        IProperty labelProp = IArchimateFactory.eINSTANCE.createProperty();
        labelProp.setKey("label");
        labelProp.setValue("Étiquette");
        dmo.getArchimateConcept().getProperties().add(labelProp);

        String result = textRenderer.renderWithExpression(dmo,
                "${if:${property:label}:${property:label}:${type}}");
        assertEquals("Étiquette", result);

        // Remove the property -> else branch
        dmo.getArchimateConcept().getProperties().clear();
        result = textRenderer.renderWithExpression(dmo,
                "${if:${property:label}:${property:label}:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * Uses nvl inside the condition of an if to provide a fallback value,
     * ensuring the condition is never blank.
     */
    @Test
    public void render_IfThenElse_WithNvlInCondition() {
        // Name present -> nvl returns name -> condition true -> then "OK"
        dmo.setName("Archi");
        String result = textRenderer.renderWithExpression(dmo,
                "${if:${nvl:${name}:default}:OK:${type}}");
        assertEquals("OK", result);

        // Empty name -> nvl returns "default" -> non-blank -> true -> "OK"
        dmo.setName("");
        result = textRenderer.renderWithExpression(dmo,
                "${if:${nvl:${name}:default}:OK:${type}}");
        assertEquals("OK", result);

        // Without nvl, empty name makes condition false -> else
        result = textRenderer.renderWithExpression(dmo,
                "${if:${name}:OK:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * nvl with a custom property as condition and a literal alternate.
     */
    @Test
    public void render_Nvl_WithCustomProperty() {
        // Add a "comment" property
        IProperty commentProp = IArchimateFactory.eINSTANCE.createProperty();
        commentProp.setKey("comment");
        commentProp.setValue("A comment");
        dmo.getArchimateConcept().getProperties().add(commentProp);

        String result = textRenderer.renderWithExpression(dmo,
                "${nvl:${property:comment}:No comment}");
        assertEquals("A comment", result);

        // Remove the property -> alternate used
        dmo.getArchimateConcept().getProperties().clear();
        result = textRenderer.renderWithExpression(dmo,
                "${nvl:${property:comment}:No comment}");
        assertEquals("No comment", result);
    }

    /**
     * The alternate value in nvl can itself be an expression.
     */
    @Test
    public void render_Nvl_WithExpressionInAlternate() {
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo,
                "${nvl:${name}:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * An unclosed if expression is left as literal text.
     */
    @Test
    public void render_If_Malformed_Unclosed() {
        String result = textRenderer.renderWithExpression(dmo,
                "Before ${if:${name}:ok:ko after");
        assertEquals("Before ${if:Concept Name:ok:ko after", result);
    }

    /**
     * Escaped colons and braces inside expressions are preserved
     * and correctly interpreted by the rendering pipeline.
     */
    @Test
    public void render_If_EscapedBracesAndColons() {
        // Add a property "key" with value "value"
        IProperty keyProp = IArchimateFactory.eINSTANCE.createProperty();
        keyProp.setKey("key");
        keyProp.setValue("value");
        dmo.getArchimateConcept().getProperties().add(keyProp);

        String result = textRenderer.renderWithExpression(dmo,
                "${if:not\\:empty\\}:${property:key}:fallback}");
        assertEquals("value", result);
    }

    @Test
    public void render_Nested_If() {
        IProperty modeProp = IArchimateFactory.eINSTANCE.createProperty();
        modeProp.setKey("mode");
        modeProp.setValue("full");
        dmo.getArchimateConcept().getProperties().add(modeProp);
        String expr = "${if:${property:mode}:${if:${name}:${name}:No name}:${type}}";

        // mode = "full", name = "Concept Name"
        assertEquals("Concept Name", textRenderer.renderWithExpression(dmo, expr));

        // empty name
        dmo.setName("");
        assertEquals("No name", textRenderer.renderWithExpression(dmo, expr));

        // Supprimer la propriété → condition vide → branche else
        dmo.getArchimateConcept().getProperties().remove(modeProp);
        dmo.setName("Concept Name");
        assertEquals("Business Actor", textRenderer.renderWithExpression(dmo, expr));
    }

    /**
     * The renderer accepts spaces around the keyword and separators.
     */
    @Test
    public void render_If_WithSpaces() {
        String result = textRenderer.renderWithExpression(dmo,
                "${ if : ${name} : ${name} : ${type} }");
        assertEquals("Concept Name", result);
    }

    /**
     * nvl expression with spaces.
     */
    @Test
    public void render_Nvl_WithSpaces() {
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo,
                "${ nvl : ${name} : ${type} }");
        assertEquals("Business Actor", result);
    }

    /**
     * If with only then part and a true condition.
     */
    @Test
    public void render_IfThen_True() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:Hello}");
        assertEquals("Hello", result);
    }

    /**
     * If with only then part and a blank condition returns empty string.
     */
    @Test
    public void render_IfThen_BlankCondition() {
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}:Hello}");
        assertEquals("", result);
    }

    /**
     * nvl with both condition and alternate blank returns empty.
     */
    @Test
    public void render_Nvl_BothBlank() {
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo, "${nvl:${name}:}");
        assertEquals("", result);
    }

    /**
     * nvl with only condition (malformed) returns empty.
     */
    @Test
    public void render_Nvl_Malformed_OnePart() {
        String result = textRenderer.renderWithExpression(dmo, "${nvl:${name}}");
        assertEquals("", result);
    }

    /**
     * If with only condition (malformed) returns empty.
     */
    @Test
    public void render_If_Malformed_OnePart() {
        String result = textRenderer.renderWithExpression(dmo, "${if:${name}}");
        assertEquals("", result);
    }
}
