/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IProperty;

/**
 * Tests for the IfRenderer supporting nested expressions and real evaluation.
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
    // Original tests (unchanged)
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
     * When the "status" property equals "active", the condition is true
     * and the "then" branch is selected. Otherwise, the "else" branch is used.
     * Also verifies the case where the property is missing entirely.
     */
    @Test
    public void render_IfThenElse_WithCustomPropertyCondition() {
        // Add a property "status" with value "active"
        IProperty statusProp = dmo.getArchimateConcept().getProperties().add("status", "active");
        String result = textRenderer.renderWithExpression(dmo, "${if:${property:status}:actif:inactif}");
        assertEquals("actif", result);

        // Empty the property: condition is false -> else branch
        statusProp.setValue("");
        result = textRenderer.renderWithExpression(dmo, "${if:${property:status}:actif:inactif}");
        assertEquals("inactif", result);

        // Remove the property entirely (resolves to empty) -> else branch
        dmo.getArchimateConcept().getProperties().remove(statusProp);
        result = textRenderer.renderWithExpression(dmo, "${if:${property:status}:actif:inactif}");
        assertEquals("inactif", result);
    }

    /**
     * Tests that the "then" and "else" branches can themselves contain nested expressions.
     * Here, the "then" branch uses ${property:label} and the "else" branch uses ${type}.
     * Both should be resolved by the rendering pipeline before the final value is returned.
     */
    @Test
    public void render_IfThenElse_WithNestedBranches() {
        // Create a "label" property
        dmo.getArchimateConcept().getProperties().add("label", "Étiquette");
        String result = textRenderer.renderWithExpression(dmo,
                "${if:${property:label}:${property:label}:${type}}");
        assertEquals("Étiquette", result);

        // Without the property, the else branch is displayed
        dmo.getArchimateConcept().getProperties().clear();
        result = textRenderer.renderWithExpression(dmo,
                "${if:${property:label}:${property:label}:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * Tests that an nvl expression can be used inside an if condition,
     * and the branches can themselves contain expressions.
     * The nvl provides a fallback value if the name is empty,
     * ensuring the condition is never blank.
     */
    @Test
    public void render_IfThenElse_WithNvlInCondition_ThenFixed() {
        // If the name exists (resolved via nvl), display "OK", otherwise display the type.
        dmo.setName("Archi");
        String result = textRenderer.renderWithExpression(dmo,
                "${if:${nvl:${name}:default}:OK:${type}}");
        assertEquals("OK", result);

        // Empty name -> nvl returns "default" which is not blank -> condition true -> then "OK"
        dmo.setName("");
        result = textRenderer.renderWithExpression(dmo,
                "${if:${nvl:${name}:default}:OK:${type}}");
        assertEquals("OK", result);

        // Without nvl fallback, an empty name makes the condition false -> else branch
        result = textRenderer.renderWithExpression(dmo,
                "${if:${name}:OK:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * Tests nvl with a custom property as the condition.
     * When the property exists and is not blank, its value is used.
     * When it is missing or blank, the alternate value is used instead.
     */
    @Test
    public void render_Nvl_WithProperty() {
        dmo.getArchimateConcept().getProperties().add("comment", "A comment");
        String result = textRenderer.renderWithExpression(dmo,
                "${nvl:${property:comment}:No comment}");
        assertEquals("A comment", result);

        // Remove the property entirely
        dmo.getArchimateConcept().getProperties().clear();
        result = textRenderer.renderWithExpression(dmo,
                "${nvl:${property:comment}:No comment}");
        assertEquals("No comment", result);
    }

    /**
     * Tests that the alternate value in an nvl expression can itself be an expression
     * that gets resolved through the rendering pipeline.
     */
    @Test
    public void render_Nvl_WithExpressionInAlternate() {
        dmo.setName("");
        String result = textRenderer.renderWithExpression(dmo,
                "${nvl:${name}:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * Tests handling of a malformed if expression with a missing closing brace.
     * The unclosed expression should remain as literal text rather than causing an error.
     */
    @Test
    public void render_If_Malformed_Unclosed() {
        String result = textRenderer.renderWithExpression(dmo,
                "Before ${if:${name}:ok:ko after");
        assertEquals("Before ${if:Concept Name:ok:ko after", result);
    }

    /**
     * Tests a combination of escape sequences and nested expressions.
     * The escaped colon and closing brace in the condition are preserved
     * and later interpreted by the rendering pipeline.
     */
    @Test
    public void render_If_EscapedBracesAndColons() {
        dmo.getArchimateConcept().getProperties().add("key", "value");
        String result = textRenderer.renderWithExpression(dmo,
                "${if:not\\:empty\\}:${property:key}:fallback}");
        // Condition "not:empty}" (not blank) -> true, then branch = property "key" = "value"
        assertEquals("value", result);
    }

    /**
     * Tests nested if expressions.
     * An outer if selects between two alternatives based on a property,
     * and the "then" branch contains an inner if that tests the name.
     * Verifies all combinations of the inner and outer conditions.
     */
    @Test
    public void render_Nested_If() {
        // Add a property "mode" with value "full"
        dmo.getArchimateConcept().getProperties().add("mode", "full");
        // If mode=full, display a detailed content, otherwise just the type.
        String result = textRenderer.renderWithExpression(dmo,
                "${if:${property:mode}:${if:${name}:${name}:No name}:${type}}");
        // mode=full -> true, then = ${if:${name}:${name}:No name} -> name not empty -> displays the name
        assertEquals("Concept Name", result);

        // If the name is empty, the inner if should return "No name"
        dmo.setName("");
        result = textRenderer.renderWithExpression(dmo,
                "${if:${property:mode}:${if:${name}:${name}:No name}:${type}}");
        assertEquals("No name", result);

        // If the mode changes to "compact", the outer if falls back to the else branch
        dmo.getArchimateConcept().getProperties().get("mode").setValue("compact");
        dmo.setName("Concept Name"); // restore name
        result = textRenderer.renderWithExpression(dmo,
                "${if:${property:mode}:${if:${name}:${name}:No name}:${type}}");
        assertEquals("Business Actor", result);
    }

    /**
     * Verifies that spaces around separators and after the opening brace are ignored,
     * providing some flexibility in the expression syntax.
     */
    @Test
    public void render_If_WithSpaces() {
        String result = textRenderer.renderWithExpression(dmo,
                "${ if : ${name} : ${name} : ${type} }");
        assertEquals("Concept Name", result);
    }
}
