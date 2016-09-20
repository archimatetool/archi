/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class HTMLReportExporterTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HTMLReportExporterTests.class);
    }
    
    @Test
    public void testCreateReport() throws Exception {
        // Create test model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        IArchimateModel model = tm.loadModel();
        
        // Create Exporter and output folder
        
        HTMLReportExporter exporter = new HTMLReportExporter(model);
        
        File targetFolder = TestUtils.createTempFolder("archi-html-report");
        
        File outputFile = exporter.createReport(targetFolder, "index.html");
        
        // Check main file and folders exist
        
        assertTrue(outputFile.exists());
        
        File modelFolder = new File(targetFolder, model.getId());
        assertTrue(modelFolder.exists());
        
        File elementsFolder = new File(modelFolder, "elements");
        assertTrue(elementsFolder.exists());
        
        File imagesFolder = new File(modelFolder, "images");
        assertTrue(imagesFolder.exists());
        
        File viewsFolder = new File(modelFolder, "views");
        assertTrue(viewsFolder.exists());
        
        assertTrue(new File(targetFolder, "css").exists());
        assertTrue(new File(targetFolder, "elements").exists());
        assertTrue(new File(targetFolder, "hints").exists());
        assertTrue(new File(targetFolder, "js").exists());
        assertTrue(new File(targetFolder, "lib").exists());
        
        // Check element and view files created
        assertTrue(new File(elementsFolder, "model.html").exists());
        
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            // Concepts
            if(eObject instanceof IArchimateConcept) {
                assertTrue(new File(elementsFolder, ((IArchimateConcept)eObject).getId() + ".html").exists());
            }
            
            // Views & Images
            if(eObject instanceof IArchimateDiagramModel) {
                assertTrue(new File(imagesFolder, ((IArchimateDiagramModel)eObject).getId() + ".png").exists());
                assertTrue(new File(viewsFolder, ((IArchimateDiagramModel)eObject).getId() + ".html").exists());
            }
        }
        
        
        // Clean up
        FileUtils.deleteFolder(TestUtils.TMP_FOLDER);
    }

}
