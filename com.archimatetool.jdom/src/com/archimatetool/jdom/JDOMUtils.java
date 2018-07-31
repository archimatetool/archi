/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jdom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * Some useful XML Utilities that leverage the JDOM Package<br>
 *
 * @author Phillip Beauvoir
 */
public final class JDOMUtils {
	
    /**
     * The XSI Namespace
     */
    public static Namespace XSI_Namespace = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");  //$NON-NLS-1$//$NON-NLS-2$

    /**
     * The Old XSI Namespace
     */
    public static Namespace XSI_NamespaceOLD = Namespace.getNamespace("xsi", "http://www.w3.org/2000/10/XMLSchema-instance");  //$NON-NLS-1$//$NON-NLS-2$

    /**
     * The schemaLocation String
     */
    public static String XSI_SchemaLocation = "schemaLocation"; //$NON-NLS-1$
	
	/**
	 * Writes a JDOM Document to file
	 * @param doc The JDOM Document to write
	 * @param file The file to write to
	 * @throws IOException
	 */
	public static void write2XMLFile(Document doc, File file) throws IOException {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        
		// Create parent folder if it doesn't exist
		File parent = file.getParentFile();
		if(parent != null) {
		    parent.mkdirs();
		}
		
		FileOutputStream out = new FileOutputStream(file);
		outputter.output(doc, out);
		out.close();
	}
	
	/**
	 * Convert a JDOM Document to a String format
	 * @param doc The JDOM Document
	 * @return The resulting String
	 * @throws IOException
	 */
	public static String write2XMLString(Document doc) throws IOException {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		
        StringWriter out = new StringWriter();
		outputter.output(doc, out);
		out.close();

        return out.toString();
	}
	
	/**
	 * Reads and returns a JDOM Document from file with Schema validation
	 * @param xmlFile The XML File
	 * @param schemaFile One or more Schema files
	 * @return The JDOM Document or null if not found
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document readXMLFile(File xmlFile, File... schemaFiles) throws IOException, JDOMException {
		XMLReaderJDOMFactory factory = new XMLReaderXSDFactory(schemaFiles);
		SAXBuilder builder = new SAXBuilder(factory);
        
        // Don't allow DTD loading in case of XSS exploits
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
        builder.setFeature("http://xml.org/sax/features/external-general-entities", false); //$NON-NLS-1$
        builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //$NON-NLS-1$
		
		// This allows UNC mapped locations to load
		return builder.build(new FileInputStream(xmlFile));
	}
	
	/**
	 * Reads and returns a JDOM Document from file without Schema Validation
	 * @param file The XML File
	 * @return The JDOM Document or null if not found
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document readXMLFile(File file) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		
        // Don't allow DTD loading in case of XSS exploits
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false); //$NON-NLS-1$
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //$NON-NLS-1$
		
		// This allows UNC mapped locations to load
		return builder.build(new FileInputStream(file));
	}
	
	/**
	 * Reads and returns a JDOM Document from String without Schema Validation
	 * @param xmlString
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document readXMLString(String xmlString) throws JDOMException, IOException {
	    SAXBuilder builder = new SAXBuilder();
	    return builder.build(new StringReader(xmlString));
	}
}