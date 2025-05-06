/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestUtils;


/**
 * XML Model Exporter Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class XMLModelExporterTests {
    
    @Test
    public void testExportModel() throws Exception {
        // Export
        File outputFile = export(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        
        // Validate
        XMLValidator validator = new XMLValidator();
        validator.validateXML(outputFile);
        
        // XSD files were copied
        assertTrue(new File(outputFile.getParentFile(), XMLExchangePlugin.ARCHIMATE3_DIAGRAM_XSD).exists());
        assertTrue(new File(outputFile.getParentFile(), XMLExchangePlugin.ARCHIMATE3_MODEL_XSD).exists());
        assertTrue(new File(outputFile.getParentFile(), XMLExchangePlugin.ARCHIMATE3_VIEW_XSD).exists());
    }

    /**
     * Export model file to XML
     */
    static File export(File file) throws Exception {
        ArchimateTestModel tm = new ArchimateTestModel(file);
        IArchimateModel model = tm.loadModel();
        
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
        
        // Export XSD
        exporter.setIncludeXSD(true);
        
        // Export
        File outputFile = TestUtils.createTempFile(".xml");
        exporter.exportModel(model, outputFile);
        
        return outputFile;
    }
}
