/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperty;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class CSVExporterTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CSVExporterTests.class);
    }
    
    private CSVExporter exporter;
    
    @Before
    public void runOnceBeforeEachTest() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setId("0a9d34ab");
        model.setName("The Main Model");
        model.setPurpose("This is the Documentation");
        
        exporter = new CSVExporter(model);
    }
    
    @Test
    public void testCreateHeader() {
        String[] elements = { "One", "Two", "Three" };
        String expected = "\"One\",\"Two\",\"Three\"";
        assertEquals(expected, exporter.createHeader(elements));
    }

    @Test
    public void testCreateHeaderWithSemicolon() {
        exporter.setDelimiter(';');
        String[] elements = { "One", "Two", "Three" };
        String expected = "\"One\";\"Two\";\"Three\"";
        assertEquals(expected, exporter.createHeader(elements));
    }

    @Test
    public void testCreateHeaderWithTab() {
        exporter.setDelimiter('\t');
        String[] elements = { "One", "Two", "Three" };
        String expected = "\"One\"\t\"Two\"\t\"Three\"";
        assertEquals(expected, exporter.createHeader(elements));
    }

    @Test
    public void testNormalise() {
        assertEquals("Hello\nWorld\r\nWith\rSome Things", exporter.normalise("Hello\nWorld\r\nWith\rSome\tThings"));
        assertEquals("Hello World With Some Things", exporter.normalise("Hello World With Some Things"));
        assertEquals("\"\"Hello World\"\"", exporter.normalise("\"Hello World\""));
    }
    
    @Test
    public void testNormalise_StripNewLinesTrue() {
        exporter.setStripNewLines(true);
        assertEquals("Hello World With Some Things", exporter.normalise("Hello\nWorld\r\nWith\rSome\tThings"));
        assertEquals("Hello World With Some Things", exporter.normalise("Hello World With Some Things"));
    }
    
    @Test
    public void testSurroundWithQuotes() {
        assertEquals("\"Hello World\"", exporter.surroundWithQuotes("Hello World"));
        
        exporter.setExcelCompatible(true);
        assertEquals("\"=\"\" Hello World\"\"\"", exporter.surroundWithQuotes(" Hello World"));
        assertEquals("\"=\"\"0123\"\"\"", exporter.surroundWithQuotes("0123"));
    }
    
    @Test
    public void testNeedsLeadingCharHack() {
        assertFalse(exporter.needsLeadingCharHack(null));
        assertFalse(exporter.needsLeadingCharHack(""));
        assertFalse(exporter.needsLeadingCharHack("Hello World"));
        assertFalse(exporter.needsLeadingCharHack("   Hello World"));
        assertFalse(exporter.needsLeadingCharHack("0123"));
        
        exporter.setExcelCompatible(true);
        assertFalse(exporter.needsLeadingCharHack(null));
        assertFalse(exporter.needsLeadingCharHack(""));
        assertFalse(exporter.needsLeadingCharHack("Hello World"));
        assertTrue(exporter.needsLeadingCharHack("   Hello World"));
        assertTrue(exporter.needsLeadingCharHack("0123"));
    }

    @Test
    public void testNeedsLeadingCharFormulaHack() {
        assertFalse(exporter.needsLeadingCharFormulaHack(null));
        assertFalse(exporter.needsLeadingCharFormulaHack(""));
        assertFalse(exporter.needsLeadingCharFormulaHack("Hello World"));
        assertFalse(exporter.needsLeadingCharFormulaHack("   Hello World"));
        assertFalse(exporter.needsLeadingCharFormulaHack("0123"));
        
        exporter.setExcelCompatible(true);
        assertFalse(exporter.needsLeadingCharFormulaHack(null));
        assertFalse(exporter.needsLeadingCharFormulaHack(""));
        assertFalse(exporter.needsLeadingCharFormulaHack("Hello World"));
        assertTrue(exporter.needsLeadingCharFormulaHack("=cmd"));
        assertTrue(exporter.needsLeadingCharFormulaHack("+0123"));
        assertTrue(exporter.needsLeadingCharFormulaHack("-0123"));
        assertTrue(exporter.needsLeadingCharFormulaHack("@0123"));
    }

    @Test
    public void testCreateModelRow() {
        assertEquals("\"0a9d34ab\",\"ArchimateModel\",\"The Main Model\",\"This is the Documentation\",\"\"", exporter.createModelRow());
    }
    
    @Test
    public void testCreateModelRowWithSemicolon() {
        exporter.setDelimiter(';');
        assertEquals("\"0a9d34ab\";\"ArchimateModel\";\"The Main Model\";\"This is the Documentation\";\"\"", exporter.createModelRow());
    }
    
    @Test
    public void testCreateModelRowWithTab() {
        exporter.setDelimiter('\t');
        assertEquals("\"0a9d34ab\"\t\"ArchimateModel\"\t\"The Main Model\"\t\"This is the Documentation\"\t\"\"", exporter.createModelRow());
    }
    
    @Test
    public void testCreateElementRow() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        element.setId("a1234567");
        element.setName("The Main Man");
        element.setDocumentation("This is the Documentation");
        
        IProfile profile = IArchimateFactory.eINSTANCE.createProfile();
        profile.setName("Profile");
        element.getProfiles().add(profile);
        
        assertEquals("\"a1234567\",\"BusinessActor\",\"The Main Man\",\"This is the Documentation\",\"Profile\"", exporter.createElementRow(element));
    }
    
    @Test
    public void testCreateElementRow_WithNewLinesStripped() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        element.setId("d452fda");
        element.setName("The Main Man");
        element.setDocumentation("This is the\r\nDocumentation");
        exporter.setStripNewLines(true);
        
        assertEquals("\"d452fda\",\"BusinessActor\",\"The Main Man\",\"This is the Documentation\",\"\"", exporter.createElementRow(element));
    }

    @Test
    public void testCreateElementRow_WithLeadingCharsHack() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        element.setId("087dfa23");
        element.setName("  The Main Man");
        element.setDocumentation("0123");
        
        exporter.setExcelCompatible(true);
        
        assertEquals("\"=\"\"087dfa23\"\"\",\"BusinessActor\",\"=\"\"  The Main Man\"\"\",\"=\"\"0123\"\"\",\"\"", exporter.createElementRow(element));
    }

    @Test
    public void testCreateRelationshipRow() {
        IArchimateElement elementSource = IArchimateFactory.eINSTANCE.createBusinessActor();
        elementSource.setId("cfde5463e");
        IArchimateElement elementTarget = IArchimateFactory.eINSTANCE.createBusinessActor();
        elementTarget.setId("b1234dff");
        
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAccessRelationship();
        relation.setId("56435fd6");
        relation.setName("My relation");
        relation.setDocumentation("This is the Documentation");
        relation.setSource(elementSource);
        relation.setTarget(elementTarget);
        
        IProfile profile = IArchimateFactory.eINSTANCE.createProfile();
        profile.setName("Profile");
        relation.getProfiles().add(profile);
        
        assertEquals("\"56435fd6\",\"AccessRelationship\",\"My relation\",\"This is the Documentation\",\"cfde5463e\",\"b1234dff\",\"Profile\"",
                exporter.createRelationshipRow(relation));
    }
    
    
    @Test
    public void testCreatePropertyRow_Property() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        element.setId("1234567a");
        IProperty property = IArchimateFactory.eINSTANCE.createProperty();
        property.setKey("Some key");
        property.setValue("Some value");
        element.getProperties().add(property);
        
        assertEquals("\"1234567a\",\"Some key\",\"Some value\"", exporter.createPropertyRow(element.getId(), property));
    }

    @Test
    public void testCreatePropertyRow_String() {
        assertEquals("\"1234567a\",\"strength\",\"12\"", exporter.createPropertyRow("1234567a", "strength", "12"));
    }

    @Test
    public void testSort() {
        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createDevice();
        element1.setName("AA");
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        element2.setName("ZZ");
        IArchimateElement element3 = IArchimateFactory.eINSTANCE.createBusinessActor();
        element3.setName("AA");
        IArchimateElement element4 = IArchimateFactory.eINSTANCE.createApplicationCollaboration();
        element4.setName("ZZ");
        
        List<IArchimateConcept> list = new ArrayList<IArchimateConcept>();
        list.add(element1);
        list.add(element2);
        list.add(element3);
        list.add(element4);
        
        exporter.sort(list);
        
        assertEquals(element4, list.get(0));
        assertEquals(element3, list.get(1));
        assertEquals(element2, list.get(2));
        assertEquals(element1, list.get(3));
    }
    
    @Test
    public void testCreateElementsFileName() {
        assertEquals("elements.csv", exporter.createElementsFileName());
        exporter.setFilePrefix("12345-");
        assertEquals("12345-elements.csv", exporter.createElementsFileName());
    }
    
    @Test
    public void testCreateRelationsFileName() {
        assertEquals("relations.csv", exporter.createRelationsFileName());
        exporter.setFilePrefix("12345-");
        assertEquals("12345-relations.csv", exporter.createRelationsFileName());
    }

    @Test
    public void testCreatePropertiesFileName() {
        assertEquals("properties.csv", exporter.createPropertiesFileName());
        exporter.setFilePrefix("12345-");
        assertEquals("12345-properties.csv", exporter.createPropertiesFileName());
    }

}
