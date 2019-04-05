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

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.tests.TestUtils;


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
    public void testExportModel() throws IOException {
        TestUtils.ensureDefaultDisplay(); // Need to do this if running only these tests
        
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
        
        // Organization
        exporter.setSaveOrganisation(true);
        
        File outputFile = new File(TestUtils.createTempFolder("XMLModelExporterTests"), "archisurance.xml");
        exporter.exportModel(model, outputFile);
    }

}
