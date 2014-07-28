/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.commands.CommandStack;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.csv.CSVParseException;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class CSVImporterTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CSVImporterTests.class);
    }
    
    File testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.csv.tests", "testdata");
    
    File elements1File = new File(testFolder, "test1-elements.csv");
    File relations1File = new File(testFolder, "test1-relations.csv");
    File properties1File = new File(testFolder, "test1-properties.csv");
    
    File elements2File = new File(testFolder, "test2-elements.csv");
    File relations2File = new File(testFolder, "test2-relations.csv");
    File properties2File = new File(testFolder, "test2-properties.csv");
    
    File elements3File = new File(testFolder, "test3-elements.csv");
    File relations3File = new File(testFolder, "test3-relations.csv");
    
    private IArchimateModel model;
    private CSVImporter importer;
    
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    

    @Before
    public void runOnceBeforeEachTest() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setAdapter(CommandStack.class, new CommandStack());
        
        importer = new CSVImporter(model);
    }
    
    @Test
    public void testDoImport() throws Exception {
        importer.doImport(elements1File);
        
        testDoImportPart1();
        
        // Now undo it
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        stack.undo();
        
        assertEquals("", model.getName());
        assertNull(model.getPurpose());
        assertEquals(0, model.getProperties().size());
        
        assertEquals(0, model.getFolder(FolderType.BUSINESS).getElements().size());
        assertEquals(0, model.getFolder(FolderType.RELATIONS).getElements().size());
    }
    
    @Test
    public void testDoImportWithUpdatedElements() throws Exception {
        // Set up with original data
        importer.doImport(elements1File);
        
        testDoImportPart1();
        
        // Import data that is edited
        importer = new CSVImporter(model);
        importer.doImport(elements2File);
        
        // Ensure new elements is empty
        assertTrue(importer.newElements.isEmpty());

        // Ensure new properties is empty
        assertTrue(importer.newProperties.isEmpty());
        
        // Model information
        assertEquals("Test Model changed", model.getName());
        assertEquals("Model Documentation Changed", model.getPurpose());
        assertEquals(2, model.getProperties().size());
        
        IArchimateElement element = (IArchimateElement)ArchimateModelUtils.getObjectByID(model, "f00aa5b4");
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessActor(), element.eClass());
        assertEquals("Name changed", element.getName());
        assertEquals("This is the Business Actor\r\nDocumentation\r\nHere \"\"\r\n", element.getDocumentation());
        assertEquals(4, element.getProperties().size());
        
        element = (IArchimateElement)ArchimateModelUtils.getObjectByID(model, "d9fe8c17");
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessInterface(), element.eClass());
        assertEquals("Business Interface", element.getName());
        assertEquals("", element.getDocumentation());
        assertEquals(0, element.getProperties().size());
        
        IRelationship relation = (IRelationship)ArchimateModelUtils.getObjectByID(model, "cdbfc933");
        assertEquals(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), relation.eClass());
        assertEquals("Assignment relation changed", relation.getName());
        assertEquals("Assignment documentation changed", relation.getDocumentation());
        assertEquals(0, relation.getProperties().size());
        
        relation = (IRelationship)ArchimateModelUtils.getObjectByID(model, "5854f8a3");
        assertEquals(IArchimatePackage.eINSTANCE.getCompositionRelationship(), relation.eClass());
        assertEquals("5854f8a3", relation.getId());
        assertEquals("Compo", relation.getName());
        assertEquals("Here it is\r\nagain\r\n\r\n\r\n", relation.getDocumentation());

        assertEquals(1, relation.getProperties().size());
        IProperty property = relation.getProperties().get(0);
        assertEquals("This", property.getKey());
        assertEquals("value changes", property.getValue());
    }
    
    private void testDoImportPart1() {
        // Model information
        String modelID = model.getId(); // This should not be changed
        assertEquals(modelID, model.getId());
        assertEquals("Test Model", model.getName());
        assertEquals("This is the Purpose of the Model.\r\nIt has a line break and \"some\" quotes.\r\n", model.getPurpose());
        assertEquals(2, model.getProperties().size());

        // Elements
        assertEquals(3, model.getFolder(FolderType.BUSINESS).getElements().size());
        
        // Relations
        assertEquals(2, model.getFolder(FolderType.RELATIONS).getElements().size());
        
        // Ensure updated elements is empty
        assertTrue(importer.updatedElements.isEmpty());
    }
    
    @Test
    public void testImportModelElements() throws Exception {
        importer.importElements(elements1File);
        
        assertEquals(3, importer.newElements.size());
        
        IArchimateElement element = importer.newElements.get("f00aa5b4");
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessActor(), element.eClass());
        assertEquals("f00aa5b4", element.getId());
        assertEquals("Business Actor", element.getName());
        assertEquals("This is the Business Actor\r\nDocumentation\r\nHere \"\"\r\n", element.getDocumentation());
        
        element = importer.newElements.get("d9fe8c17");
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessInterface(), element.eClass());
        assertEquals("d9fe8c17", element.getId());
        assertEquals("Business Interface", element.getName());
        assertEquals("", element.getDocumentation());
        
        element = importer.newElements.get("f6a18059");
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessRole(), element.eClass());
        assertEquals("f6a18059", element.getId());
        assertEquals("Business Role", element.getName());
        assertEquals("Some more docs\r\nHere\r\n", element.getDocumentation());
    }

    @Test
    public void testImportElementsAndRelationsWithNoIDsHaveIDsGenerated() throws Exception {
        importer.importElements(elements3File);
        importer.importRelations(relations3File);
        
        assertEquals(6, importer.newElements.size());
        for(String id : importer.newElements.keySet()) {
            assertTrue(StringUtils.isSet(id));
        }
    }

    @Test
    public void testImportRelations() throws Exception {
        importer.importElements(elements1File);
        importer.importRelations(relations1File);
        
        assertEquals(5, importer.newElements.size());
        
        IRelationship relation = (IRelationship)importer.newElements.get("cdbfc933");
        assertEquals(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), relation.eClass());
        assertEquals("cdbfc933", relation.getId());
        assertEquals("Assignment relation", relation.getName());
        assertEquals("Assignment documentation\r\nIs here \"hello\"", relation.getDocumentation());
        IArchimateElement source = relation.getSource();
        assertNotNull(source);
        assertEquals("f00aa5b4", source.getId());
        IArchimateElement target = relation.getTarget();
        assertNotNull(target);
        assertEquals("f6a18059", target.getId());
        
        relation = (IRelationship)importer.newElements.get("5854f8a3");
        assertEquals(IArchimatePackage.eINSTANCE.getCompositionRelationship(), relation.eClass());
        assertEquals("5854f8a3", relation.getId());
        assertEquals("Compo", relation.getName());
        assertEquals("Here it is\r\nagain\r\n\r\n\r\n", relation.getDocumentation());
        source = relation.getSource();
        assertNotNull(source);
        assertEquals("f00aa5b4", source.getId());
        target = relation.getTarget();
        assertNotNull(target);
        assertEquals("d9fe8c17", target.getId());
    }
    
    @Test
    public void testImportProperties() throws Exception {
        importer.importElements(elements1File);
        importer.importRelations(relations1File);
        importer.importProperties(properties1File);
        
        assertEquals(7, importer.newProperties.size());
    }

    @Test
    public void testIsElementsFileName() {
        assertFalse(CSVImporter.isElementsFileName(new File("relations.csv")));
        assertFalse(CSVImporter.isElementsFileName(new File(CSVConstants.ELEMENTS_FILENAME + "-suffix.csv")));
        assertTrue(CSVImporter.isElementsFileName(new File(CSVConstants.ELEMENTS_FILENAME + ".csv")));
        assertTrue(CSVImporter.isElementsFileName(new File(CSVConstants.ELEMENTS_FILENAME)));
        assertTrue(CSVImporter.isElementsFileName(new File("prefix-" + CSVConstants.ELEMENTS_FILENAME)));
    }
    
    @Test
    public void testIsRelationsFileName() {
        assertFalse(CSVImporter.isRelationsFileName(new File("elements.csv")));
        assertFalse(CSVImporter.isRelationsFileName(new File(CSVConstants.RELATIONS_FILENAME + "-suffix.csv")));
        assertTrue(CSVImporter.isRelationsFileName(new File(CSVConstants.RELATIONS_FILENAME + ".csv")));
        assertTrue(CSVImporter.isRelationsFileName(new File(CSVConstants.RELATIONS_FILENAME)));
        assertTrue(CSVImporter.isRelationsFileName(new File("prefix-" + CSVConstants.RELATIONS_FILENAME)));
    }
    
    @Test
    public void testIsPropertiesFileName() {
        assertFalse(CSVImporter.isPropertiesFileName(new File("elements.csv")));
        assertFalse(CSVImporter.isPropertiesFileName(new File(CSVConstants.PROPERTIES_FILENAME + "-suffix.csv")));
        assertTrue(CSVImporter.isPropertiesFileName(new File(CSVConstants.PROPERTIES_FILENAME + ".csv")));
        assertTrue(CSVImporter.isPropertiesFileName(new File(CSVConstants.PROPERTIES_FILENAME)));
        assertTrue(CSVImporter.isPropertiesFileName(new File("prefix-" + CSVConstants.PROPERTIES_FILENAME)));
    }
    
    @Test
    public void testGetAccessoryFileFromElementsFile_Relations() {
        String parentFolder = "parentFolder";
        
        File file = new File(parentFolder, "elements");
        assertEquals(new File(parentFolder, "relations"),
                CSVImporter.getAccessoryFileFromElementsFile(file, CSVConstants.RELATIONS_FILENAME));
        assertEquals(new File(parentFolder, "properties"),
                CSVImporter.getAccessoryFileFromElementsFile(file, CSVConstants.PROPERTIES_FILENAME));
        
        file = new File(parentFolder, "elements.csv");
        assertEquals(new File(parentFolder, "relations.csv"),
                CSVImporter.getAccessoryFileFromElementsFile(file, CSVConstants.RELATIONS_FILENAME));
        assertEquals(new File(parentFolder, "properties.csv"),
                CSVImporter.getAccessoryFileFromElementsFile(file, CSVConstants.PROPERTIES_FILENAME));
        
        file = new File(parentFolder, "prefix-elements.csv");
        assertEquals(new File(parentFolder, "prefix-relations.csv"),
                CSVImporter.getAccessoryFileFromElementsFile(file, CSVConstants.RELATIONS_FILENAME));
        assertEquals(new File(parentFolder, "prefix-properties.csv"),
                CSVImporter.getAccessoryFileFromElementsFile(file, CSVConstants.PROPERTIES_FILENAME));
    }
    
    @Test
    public void testNormalise() {
        assertEquals("", importer.normalise(null));
        assertEquals("ok here", importer.normalise("ok here"));
        assertEquals("tab here", importer.normalise("tab\there"));
        assertEquals("line feed", importer.normalise("line\rfeed"));
        assertEquals("line feed", importer.normalise("line\nfeed"));
        assertEquals("line feed", importer.normalise("line\r\nfeed"));
    }
    
    @Test
    public void testCheckID_Null() throws CSVParseException {
        expectedEx.expect(CSVParseException.class);
        expectedEx.expectMessage("ID not found");
        
        importer.checkID(null);
    }
    
    @Test
    public void testCheckID_Duplicate() throws Exception {
        expectedEx.expect(CSVParseException.class);
        expectedEx.expectMessage("Duplicate ID");
        
        importer.doImport(elements1File);
        importer.checkID("f00aa5b4");
    }
    
    @Test
    public void testFindElementInModel() throws Exception {
        importer.doImport(elements1File);
        assertNotNull(importer.findElementInModel("f00aa5b4", IArchimatePackage.eINSTANCE.getBusinessActor()));
    }

    @Test
    public void testFindElementInModel_DifferentClass() throws Exception {
        expectedEx.expect(CSVParseException.class);
        expectedEx.expectMessage("Found element with same id but different class: f6a18059");
        
        importer.doImport(elements1File);
        importer.findElementInModel("f6a18059", IArchimatePackage.eINSTANCE.getBusinessActor());
    }
    
    @Test
    public void testFindReferencedElement() throws Exception {
        importer.doImport(elements1File);
        assertNotNull(importer.findReferencedElement("f6a18059"));
    }
    
    @Test
    public void testFindReferencedElement_IsRelationship() throws Exception {
        expectedEx.expect(CSVParseException.class);
        expectedEx.expectMessage("Type should be of ArchiMate element type: 5854f8a3");
        
        importer.doImport(elements1File);
        importer.findReferencedElement("5854f8a3");
    }

    @Test(expected=CSVParseException.class)
    public void testFindReferencedElement_NotFound() throws Exception {
        importer.doImport(elements1File);
        importer.findReferencedElement("someid");
    }

    @Test(expected=CSVParseException.class)
    public void testFindReferencedElement_Null() throws CSVParseException {
        importer.findReferencedElement(null);
    }
   
    @Test
    public void testIsArchimateElementEClass() {
        assertFalse(importer.isArchimateElementEClass(null));
        assertFalse(importer.isArchimateElementEClass(IArchimatePackage.eINSTANCE.getAccessRelationship()));
        assertTrue(importer.isArchimateElementEClass(IArchimatePackage.eINSTANCE.getBusinessActor()));
    }
   
    @Test
    public void testIsRelationshipEClass() {
        assertFalse(importer.isRelationshipEClass(null));
        assertFalse(importer.isRelationshipEClass(IArchimatePackage.eINSTANCE.getBusinessActor()));
        assertTrue(importer.isRelationshipEClass(IArchimatePackage.eINSTANCE.getAccessRelationship()));
    }
    
    @Test
    public void testGetProperty() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        IProperty property = IArchimateFactory.eINSTANCE.createProperty();
        property.setKey("key");
        property.setValue("value");
        element.getProperties().add(property);
        
        assertEquals(property, importer.getProperty(element, "key"));
        assertNull(importer.getProperty(element, "key2"));
    }
}
