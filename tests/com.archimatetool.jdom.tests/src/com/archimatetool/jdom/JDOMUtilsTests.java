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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
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
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + lineSep + "<root />" + lineSep + lineSep;
        String result = JDOMUtils.write2XMLString(doc);
        assertEquals(expected, result);
    }
    
    private Document createDocument() {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        return doc;
    }

    // We don't use this yet
    public void testReadXMLFile_FileStringString() {
    }

    @Test
    public void testReadXMLFile_File() throws Exception {
        File file = new File(TESTDATA_FOLDER, "imsmanifest.xml");
        Document doc = JDOMUtils.readXMLFile(file);
        assertNotNull("Document was null", doc);
    }

    @Test
    public void testReadXMLString() throws Exception {
        String testString = "<root> <element att=\"hello\">Some text</element> </root>";
        Document doc = JDOMUtils.readXMLString(testString);
        assertNotNull("Document was null", doc);
    }

    @Test
    public void testReplaceNamespaces() throws Exception {
        File file = new File(TESTDATA_FOLDER, "imsmanifest.xml");
        Document doc = JDOMUtils.readXMLFile(file);
        
        // Replace Metadata Namespace
        Element root = doc.getRootElement();
        Namespace oldNS = root.getNamespace("imsmd");
        Namespace newNS = Namespace.getNamespace("reload", "http://www.reload.ac.uk");
        JDOMUtils.replaceNamespaces(root, oldNS, newNS);
        
        // Now try to get an Element with that Namespace
        Element mdElement = root.getChild("metadata", root.getNamespace());
        Element lomElement = mdElement.getChild("lom", newNS);
        
        assertNotNull("Namespace should have been replaced", lomElement);
    }


}
