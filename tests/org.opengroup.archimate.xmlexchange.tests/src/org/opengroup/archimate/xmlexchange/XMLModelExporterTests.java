/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;


/**
 * XML Model Exporter Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class XMLModelExporterTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(XMLModelExporterTests.class);
    }

    @Test
    public void testExportModel() throws IOException, SAXException {
        Resource resource = ArchimateResourceFactory.createNewResource(TestSupport.archiFile1);
        resource.load(null);
        
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        XMLModelExporter exporter = new XMLModelExporter();
        
        // Language code
        exporter.setLanguageCode("en");
        
        // Metadata
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put("creator", "Phil Beauvoir");
        metadata.put("date", "2015-01-21 17:50");
        metadata.put("description", "Test the Archisurance Exchange Model");
        metadata.put("language", "en");
        metadata.put("subject", "ArchiMate, Testing");
        metadata.put("title", "Archisurance Test Exchange Model");
        exporter.setMetadata(metadata);
        
        // Add Organization
        exporter.setSaveOrganisation(true);
        
        // Export
        File outputFile = TestUtils.createTempFile(".xml");
        exporter.exportModel(model, outputFile);
        
        // And Validate
        XMLValidator validator = new XMLValidator();
        validator.validateXML(outputFile);
    }

}
