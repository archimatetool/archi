/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.junit.Test;

import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class JDOMUtilsTests {
    
    public static File TESTDATA_FOLDER = TestUtils.getLocalBundleFolder("com.archimatetool.jdom.tests", "testdata");
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDOMUtilsTests.class);
    }
    
    @Test
    public void testWrite2XMLFile() throws Exception {
        File tmpFile = TestUtils.createTempFile(".xml");
        Document doc = createDocument();
        JDOMUtils.write2XMLFile(doc, tmpFile);
        assertTrue(tmpFile.exists());
    }

    @Test
    public void testWrite2XMLString() throws Exception {
        Document doc = createDocument();
        String lineSep = Format.getPrettyFormat().getLineSeparator();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + lineSep + "<root />" + lineSep;
        String result = JDOMUtils.write2XMLString(doc);
        assertEquals(expected, result);
    }
    
    private Document createDocument() {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        return doc;
    }

    @Test
    public void testReadXMLFile_File_File() throws Exception {
        File xmlFile = new File(TESTDATA_FOLDER, "validate_this.xml");
        File schemaFile = new File(TESTDATA_FOLDER, "imscp_v1p2.xsd");
        Document doc = JDOMUtils.readXMLFile(xmlFile, schemaFile);
        assertNotNull(doc);
        assertTrue(doc.hasRootElement());
    }

    @Test
    public void testReadXMLFile_File() throws Exception {
        File file = new File(TESTDATA_FOLDER, "imsmanifest.xml");
        Document doc = JDOMUtils.readXMLFile(file);
        assertNotNull(doc);
        assertTrue(doc.hasRootElement());
    }

    @Test
    public void testReadXMLString() throws Exception {
        String testString = "<root> <element att=\"hello\">Some text</element> </root>";
        Document doc = JDOMUtils.readXMLString(testString);
        assertNotNull(doc);
        assertTrue(doc.hasRootElement());
    }
}
