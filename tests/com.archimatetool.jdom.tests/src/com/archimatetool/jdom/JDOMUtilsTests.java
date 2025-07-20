/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jdom;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.junit.jupiter.api.Test;

import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class JDOMUtilsTests {
    
    private static final File TESTDATA_FOLDER = TestUtils.getLocalBundleFolder("com.archimatetool.jdom.tests", "testdata");
    private static final File CONTENT_PACKAGE = new File(TESTDATA_FOLDER, "cp.xml");
    private static final File CONTENT_PACKAGE_XSD = new File(TESTDATA_FOLDER, "imscp_v1p2.xsd");
    
    @Test
    public void write2XMLFile() throws Exception {
        File tmpFile = TestUtils.createTempFile(".xml");
        Document doc = createDocument();
        JDOMUtils.write2XMLFile(doc, tmpFile);
        assertTrue(tmpFile.exists());
    }

    @Test
    public void write2XMLString() throws Exception {
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
    public void readXMLFile() throws Exception {
        Document doc = JDOMUtils.readXMLFile(CONTENT_PACKAGE);
        assertNotNull(doc);
        assertTrue(doc.hasRootElement());
    }

    @Test
    public void readXMLFileWithSchema() throws Exception {
        Document doc = JDOMUtils.readXMLFile(CONTENT_PACKAGE, CONTENT_PACKAGE_XSD);
        assertNotNull(doc);
        assertTrue(doc.hasRootElement());
    }

    @Test
    public void readXMLString() throws Exception {
        String testString = "<root> <element att=\"hello\">Some text</element> </root>";
        Document doc = JDOMUtils.readXMLString(testString);
        assertNotNull(doc);
        assertTrue(doc.hasRootElement());
    }
}
