/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.jdom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


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
	 * @param file The XML File
	 * @param schemaNamespace The Schema Target Namespace
	 * @param schemaLocation The Schema Location
	 * @return The JDOM Document or null if not found
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document readXMLFile(File file, String schemaNamespace, String schemaLocation) throws IOException, JDOMException {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder(true);
		builder.setFeature("http://apache.org/xml/features/validation/schema", true); //$NON-NLS-1$
		builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", //$NON-NLS-1$
				schemaNamespace + " " + schemaLocation); //$NON-NLS-1$
		// This allows UNC mapped locations to load
		doc = builder.build(new FileInputStream(file));
		return doc;
	}
	
	/**
	 * Reads and returns a JDOM Document from file without Schema Validation
	 * @param file The XML File
	 * @return The JDOM Document or null if not found
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document readXMLFile(File file) throws IOException, JDOMException {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		// This allows UNC mapped locations to load
		doc = builder.build(new FileInputStream(file));
		return doc;
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
	
    /**
     * Recursively replace a given Namespace with another one.  This can be used for removing Namespace prefixes.
     * This is recursive and traverses the whole JDOM tree.
     * @param element The starting Element
     */
    public static void replaceNamespaces(Element root, Namespace oldNamespace, Namespace newNamespace) {
        if(root.getNamespace().equals(oldNamespace)) {
            root.setNamespace(newNamespace);
        }
    
        for(Object element : root.getChildren()) {
            replaceNamespaces((Element)element, oldNamespace, newNamespace);
        }
    }

}