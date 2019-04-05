/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;


/**
 * XML Validator
 * 
 * @author Phillip Beauvoir
 */
public final class XMLValidator {
    
    public void validateXML(File xmlInstance) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        
        // Resolver for xsd import
        factory.setResourceResolver(new ResourceResolver());
        
        // Local XSDs
        Schema schema = factory.newSchema(new Source[]{
                new StreamSource(XMLExchangePlugin.INSTANCE.getBundleInputStream(XMLExchangePlugin.XSD_FOLDER + XMLExchangePlugin.ARCHIMATE3_DIAGRAM_XSD)),
                new StreamSource(XMLExchangePlugin.INSTANCE.getBundleInputStream(XMLExchangePlugin.XSD_FOLDER + XMLExchangePlugin.DUBLINCORE_XSD))
        });

        Validator validator = schema.newValidator();
        
        // Don't allow DTD loading in case of XSS exploits
        validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); //$NON-NLS-1$
        validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); //$NON-NLS-1$
        
        // Fixes #274 https://github.com/archimatetool/archi/issues/274
        FileInputStream in = new FileInputStream(xmlInstance);
        
        try {
            validator.validate(new StreamSource(in));
        }
        catch(SAXException ex) {
            // Ignore error where an XSD declaration is one that we do not have locally (for example for additional metadata)
            if(!ex.getMessage().contains("The matching wildcard is strict, but no declaration can be found")) { //$NON-NLS-1$
                throw ex;
            }
        }
        finally {
            in.close();
        }
    }

    static class ResourceResolver implements LSResourceResolver {
        @Override
        public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
            // Resolve <xs:import namespace="http://www.w3.org/XML/1998/namespace" schemaLocation="http://www.w3.org/2001/xml.xsd"/>
            // in the main XSD file so that we don't have to go online to get it (takes ages)
            if("http://www.w3.org/2001/xml.xsd".equals(systemId)) { //$NON-NLS-1$
                try {
                    InputStream is = XMLExchangePlugin.INSTANCE.getBundleInputStream(XMLExchangePlugin.XSD_FOLDER + XMLExchangePlugin.XML_XSD);
                    return new Input(publicId, systemId, is);
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            // Resolve included XSDs
            if(XMLExchangePlugin.ARCHIMATE3_VIEW_XSD.equals(systemId) || XMLExchangePlugin.ARCHIMATE3_MODEL_XSD.equals(systemId)) {
                try {
                    InputStream is = XMLExchangePlugin.INSTANCE.getBundleInputStream(XMLExchangePlugin.XSD_FOLDER + systemId);
                    return new Input(publicId, systemId, is);
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }

            return null;
        }
    }
    
    /**
     * From http://stackoverflow.com/questions/2342808/problem-validating-an-xml-file-using-java-with-an-xsd-having-an-include
     */
    static class Input implements LSInput {
        private String publicId;
        private String systemId;
        private BufferedInputStream inputStream;

        public Input(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

        @Override
        public String getPublicId() {
            return publicId;
        }

        @Override
        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public InputStream getByteStream() {
            return null;
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public String getStringData() {
            synchronized(inputStream) {
                try {
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    String contents = new String(input);
                    return contents;
                }
                catch(IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public void setByteStream(InputStream byteStream) {
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public void setStringData(String stringData) {
        }

        @Override
        public String getSystemId() {
            return systemId;
        }

        @Override
        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
    }
}
