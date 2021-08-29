/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;

import junit.framework.JUnit4TestAdapter;

/**
 * TextRendererTests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TextRendererTests.class);
    }
    
    private TextRenderer textRenderer = TextRenderer.getDefault();
    
    // ============================= TextRenderer methods Tests =========================================

    @Test
    public void hasFormatExpression_DefaultNull() {
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        assertFalse(textRenderer.hasFormatExpression(object));
    }
    
    @Test
    public void hasFormatExpression_Set() {
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        object.getFeatures().putString(TextRenderer.FEATURE_NAME, "expression");
        assertTrue(textRenderer.hasFormatExpression(object));
    }

    @Test
    public void getFormatExpression_DefaultNull() {
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        assertNull(textRenderer.getFormatExpression(object));
    }
    
    @Test
    public void getFormatExpression_Set() {
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        object.getFeatures().putString(TextRenderer.FEATURE_NAME, "expression");
        assertEquals("expression", textRenderer.getFormatExpression(object));
    }
    
    @Test
    public void getFormatExpressionFromAncestorFolder() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        IFolder folder1 =  IArchimateFactory.eINSTANCE.createFolder();
        businessFolder.getFolders().add(folder1);
        IFolder folder2 =  IArchimateFactory.eINSTANCE.createFolder();
        folder1.getFolders().add(folder2);
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createBusinessActor();
        folder2.getElements().add(concept);
        
        assertNull(textRenderer.getFormatExpressionFromAncestorFolder(concept));
        
        businessFolder.getFeatures().putString(TextRenderer.FEATURE_NAME, "${name}");
        assertEquals("${name}", textRenderer.getFormatExpressionFromAncestorFolder(concept));
        
        folder1.getFeatures().putString(TextRenderer.FEATURE_NAME, "${doc}");
        assertEquals("${doc}", textRenderer.getFormatExpressionFromAncestorFolder(concept));
        
        folder2.getFeatures().putString(TextRenderer.FEATURE_NAME, "${type}");
        assertEquals("${type}", textRenderer.getFormatExpressionFromAncestorFolder(concept));
    }

    @Test
    public void isSupportedObject() {
        assertTrue(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createDiagramModelGroup()));
        assertTrue(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createDiagramModelNote()));
        assertTrue(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createDiagramModelReference()));
        assertTrue(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createDiagramModelConnection()));
        assertTrue(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createFolder()));
        
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertTrue(textRenderer.isSupportedObject(dmo));
        
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createJunction());
        assertFalse(textRenderer.isSupportedObject(dmo));

        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateRelationship(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertTrue(textRenderer.isSupportedObject(dmc));

        assertFalse(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createBusinessActor()));
        assertFalse(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createAssociationRelationship()));

        assertFalse(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createDiagramModelImage()));
        assertFalse(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createSketchModelActor()));
        assertFalse(textRenderer.isSupportedObject(IArchimateFactory.eINSTANCE.createSketchModelSticky()));
    }
    
    // ============================= General Expression Tests =========================================
    
    @Test
    public void render_EmptyString() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        assertEquals("", textRenderer.renderWithExpression(group, null));
    }
    
    @Test
    public void render_NoExpression() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        assertEquals("Just Some Text", textRenderer.renderWithExpression(group, "Just Some Text"));
    }

    @Test
    public void render_InfiniteLoop() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setName("${name} Name");
        assertEquals("*** Recursion Error in Label Expression ***", textRenderer.renderWithExpression(group, "${name}"));
    }

    @Test
    public void render_NonInfiniteLoop() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setName("${name}");
        assertEquals("${name}", textRenderer.renderWithExpression(group, "${name}"));
    }

    @Test
    public void render_FromObject() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setName("Group Name");
        assertEquals("", textRenderer.render(group));
        
        group.getFeatures().putString(TextRenderer.FEATURE_NAME, "${name}");
        assertEquals("Group Name", textRenderer.render(group));
    }

    @Test
    public void render_WithDefault() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setName("Group Name");
        assertEquals("default", textRenderer.render(group, "default"));
        
        group.getFeatures().putString(TextRenderer.FEATURE_NAME, "${name}");
        assertEquals("Group Name", textRenderer.render(group, "default"));
    }

    @Test
    public void render_Test1() {
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createBusinessActor();
        concept.setName("Concept Name");
        concept.setDocumentation("Concept Documentation");
        concept.getProperties().add(IArchimateFactory.eINSTANCE.createProperty("p1", "v1"));
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateConcept(concept);
        
        String expression = "${name} ${documentation} ${property:p1}";
        String result = textRenderer.renderWithExpression(dmo, expression);
        assertEquals("Concept Name Concept Documentation v1", result);
    }
    
    // ============================= Property Expression Tests =========================================
    
    @Test
    public void render_PropertyKey_Indirection() {
        IDiagramModelArchimateObject dmo = createDiagramModelObject();
        addProperty(dmo.getArchimateConcept(), "en", "English");
        addProperty(dmo.getArchimateConcept(), "fr", "French");

        IProperty property = TextRendererTests.addProperty(dmo.getArchimateModel(), "lang", "en");
        
        assertEquals("English", textRenderer.renderWithExpression(dmo, "${property:$model{property:lang}}"));
        
        property.setValue("fr");
        assertEquals("French", textRenderer.renderWithExpression(dmo, "${property:$model{property:lang}}"));
        
        assertEquals("French French", textRenderer.renderWithExpression(dmo, "${property:$model{property:lang}} ${property:$model{property:lang}}"));
    }
    
    @Test
    public void render_PropertyKey_MultipleIndirection1() {
        IDiagramModelArchimateObject dmo = createDiagramModelObject();

        addProperty(dmo.getArchimateConcept(), "p1", "${property:p2}");
        addProperty(dmo.getArchimateConcept(), "p2", "${property:p3}");
        addProperty(dmo.getArchimateConcept(), "p3", "Result!");
        
        assertEquals("Result!", textRenderer.renderWithExpression(dmo, "${property:p1}"));
        assertEquals("Result! Result!", textRenderer.renderWithExpression(dmo, "${property:p1} ${property:p1}"));
    }

    @Test
    public void render_PropertyKey_MultipleIndirection2() {
        IDiagramModelArchimateObject dmo = createDiagramModelObject();

        addProperty(dmo.getArchimateModel(), "p1", "$model{property:p2}");
        addProperty(dmo.getArchimateModel(), "p2", "$model{property:p3}");
        addProperty(dmo.getArchimateModel(), "p3", "Result!");
        
        assertEquals("Result!", textRenderer.renderWithExpression(dmo, "$model{property:p1}"));
        assertEquals("Result! Result!", textRenderer.renderWithExpression(dmo, "$model{property:p1} $model{property:p1}"));
    }

    @Test
    public void render_PropertyKey_CircularReference() {
        IDiagramModelArchimateObject dmo = createDiagramModelObject();

        addProperty(dmo.getArchimateConcept(), "p1", "${property:p2}");
        addProperty(dmo.getArchimateConcept(), "p2", "${property:p3}");
        addProperty(dmo.getArchimateConcept(), "p3", "${property:p1}");
        
        assertEquals("${property:p1}", textRenderer.renderWithExpression(dmo, "${property:p1}"));
    }

    // ============================= Word Wrap Expression Tests =========================================
    
    @Test
    public void render_WordWrap_Name() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("Concept \nName", textRenderer.renderWithExpression(dmo, "${wordwrap:6:${name}}"));
    }
    
    @Test
    public void render_WordWrap_Documentation() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("Concept \nDocumentation", textRenderer.renderWithExpression(dmo, "${wordwrap:6:${documentation}}"));
    }
    
    @Test
    public void render_WordWrap_ModelPrefix() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("Model \nName", textRenderer.renderWithExpression(dmo, "${wordwrap:6:$model{name}}"));
    }
    
    @Test
    public void render_WordWrap_ViewPrefix() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("View \nName", textRenderer.renderWithExpression(dmo, "${wordwrap:6:$view{name}}"));
    }
    
    @Test
    public void render_WordWrap_Property() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        addProperty(dmo.getArchimateConcept(), "p1", "This is a Property");
        assertEquals("This \nis a \nProperty", textRenderer.renderWithExpression(dmo, "${wordwrap:6:${property:p1}}"));
    }

    @Test
    public void render_WordWrap_ModelProperty() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        addProperty(dmo.getArchimateModel(), "p1", "This is a Property");
        assertEquals("This \nis a \nProperty", textRenderer.renderWithExpression(dmo, "${wordwrap:6:$model{property:p1}}"));
    }

    // ============================= Utils =========================================
    
    static IDiagramModelArchimateObject createDiagramModelObject() {
        // Model
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setName("Model Name");
        model.setPurpose("Model Purpose");
        addProperty(model, "k1", "model_v1");
        addProperty(model, "k2", "model_v2");
        addProperty(model, "k3", "model_v3");
        addProperty(model, "k3", "model_v4"); // Dupe
        model.setDefaults();
        
        // Profiles
        IProfile profile1 = IArchimateFactory.eINSTANCE.createProfile();
        profile1.setName("Profile 1");
        profile1.setConceptType(IArchimatePackage.eINSTANCE.getBusinessActor().getName());
        model.getProfiles().add(profile1);
        
        IProfile profile2 = IArchimateFactory.eINSTANCE.createProfile();
        profile2.setName("Profile 2");
        profile2.setConceptType(IArchimatePackage.eINSTANCE.getBusinessActor().getName());
        model.getProfiles().add(profile2);

        // Diagram Model
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setName("View Name");
        dm.setDocumentation("View Documentation");
        addProperty(dm, "k1", "view_v1");
        addProperty(dm, "k2", "view_v2");
        addProperty(dm, "k3", "view_v3");
        addProperty(dm, "k3", "view_v4"); // Dupe
        
        IFolder folder = model.getDefaultFolderForObject(dm);
        folder.setDocumentation("View Folder Documentation");
        addProperty(folder, "k1", "view_folder_v1");
        addProperty(folder, "k2", "view_folder_v2");
        addProperty(folder, "k3", "view_folder_v3");
        addProperty(folder, "k3", "view_folder_v4"); // Dupe
        folder.getElements().add(dm);
        
        // Concept
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createBusinessActor();
        concept.setName("Concept Name");
        concept.setDocumentation("Concept Documentation");
        addProperty(concept, "k1", "concept_v1");
        addProperty(concept, "k2", "concept_v2");
        addProperty(concept, "k3", "concept_v3");
        addProperty(concept, "k3", "concept_v4"); // Dupe
        concept.getProfiles().add(profile1);
        
        folder = model.getDefaultFolderForObject(concept);
        folder.setDocumentation("Concept Folder Documentation");
        addProperty(folder, "k1", "concept_folder_v1");
        addProperty(folder, "k2", "concept_folder_v2");
        addProperty(folder, "k3", "concept_folder_v3");
        addProperty(folder, "k3", "concept_folder_v4"); // Dupe
        folder.getElements().add(concept);
        
        // Diagram Model Object
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateConcept(concept);
        dm.getChildren().add(dmo);
        
        return dmo;
    }

    static IDiagramModelArchimateConnection createDiagramModelConnection() {
        // Model
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setName("Model Name");
        model.setPurpose("Model Purpose");
        model.setDefaults();

        // Profiles
        IProfile profile1 = IArchimateFactory.eINSTANCE.createProfile();
        profile1.setName("Profile 3");
        profile1.setConceptType(IArchimatePackage.eINSTANCE.getBusinessActor().getName());
        model.getProfiles().add(profile1);
        
        IProfile profile2 = IArchimateFactory.eINSTANCE.createProfile();
        profile2.setName("Profile 4");
        profile2.setConceptType(IArchimatePackage.eINSTANCE.getBusinessRole().getName());
        model.getProfiles().add(profile2);

        // Diagram Model
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setName("View Name");
        dm.setDocumentation("View Documentation");
        model.getDefaultFolderForObject(dm).getElements().add(dm);

        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setName("Relation Name");
        relation.setDocumentation("Relation Documentation");
        model.getDefaultFolderForObject(relation).getElements().add(relation);
        
        IArchimateConcept sourceConcept = IArchimateFactory.eINSTANCE.createBusinessActor();
        sourceConcept.setName("Source Concept");
        sourceConcept.setDocumentation("Source Documentation");
        sourceConcept.getProfiles().add(profile1);
        addProperty(sourceConcept, "k1", "sconcept_v1");
        addProperty(sourceConcept, "k2", "sconcept_v2");
        addProperty(sourceConcept, "k3", "sconcept_v3");
        addProperty(sourceConcept, "k3", "sconcept_v4"); // Dupe
        model.getDefaultFolderForObject(sourceConcept).getElements().add(sourceConcept);
        
        IArchimateConcept targetConcept = IArchimateFactory.eINSTANCE.createBusinessRole();
        targetConcept.setName("Target Concept");
        targetConcept.setDocumentation("Target Documentation");
        targetConcept.getProfiles().add(profile2);
        addProperty(targetConcept, "k1", "tconcept_v1");
        addProperty(targetConcept, "k2", "tconcept_v2");
        addProperty(targetConcept, "k3", "tconcept_v3");
        addProperty(targetConcept, "k3", "tconcept_v4"); // Dupe
        model.getDefaultFolderForObject(targetConcept).getElements().add(targetConcept);
        
        relation.connect(sourceConcept, targetConcept);
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateConcept(sourceConcept);
        dm.getChildren().add(dmo1);
        
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateConcept(targetConcept);
        dm.getChildren().add(dmo2);
        
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateConcept(relation);
        
        dmc.connect(dmo1, dmo2);
        
        return dmc;
    }
    
    static IProperty addProperty(IProperties object, String key, String value) {
        IProperty property = IArchimateFactory.eINSTANCE.createProperty(key, value);
        object.getProperties().add(property);
        return property;
    }

}
