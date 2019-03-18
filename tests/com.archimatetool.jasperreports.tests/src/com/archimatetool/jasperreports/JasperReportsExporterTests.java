/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;


@SuppressWarnings("nls")
public class JasperReportsExporterTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JasperReportsExporterTests.class);
    }

    private static IArchimateModel model;
    private static JasperPrint jasperPrint;
    
    private static File exportFolder;
    private static File tmpFolder;
    private static String exportFileName = "archi";
    private static File mainTemplateFile = TestSupport.CUSTOM_REPORT_MAIN_FILE;
    private static String reportTitle = "Title";
    
    private static JasperReportsExporter exporter;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws JRException, IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
        
        // Setup some folders
        exportFolder = TestUtils.createTempFolder("export");
        tmpFolder = new File(exportFolder, "tmp");
        tmpFolder.mkdirs();
        
        // Create exporter
        exporter = new JasperReportsExporter(model, exportFolder, exportFileName, mainTemplateFile, reportTitle, null, 0);
        
        // Set up diagrams and Jasper Print once (expensive operation)
        exporter.writeDiagrams(tmpFolder);
        jasperPrint = exporter.createJasperPrint(tmpFolder);
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() throws IOException {
        // Clean up
        FileUtils.deleteFolder(TestUtils.TMP_FOLDER);
    }
    
    @Test
    public void testExportHTML() throws JRException {
        File file = new File(exportFolder, exportFileName + ".html");
        exporter.exportHTML(jasperPrint, file);
        
        assertTrue(file.exists());
        
        // Image files in folder
        File htmlFolder = new File(exportFolder, exportFileName + ".html_files");
        assertTrue(htmlFolder.exists());
        assertTrue(htmlFolder.isDirectory());
        assertEquals(18, htmlFolder.listFiles().length);
    }
    
    @Test
    public void testExportPDF() throws JRException {
        File file = new File(exportFolder, exportFileName + ".pdf");
        exporter.exportPDF(jasperPrint, file);
        assertTrue(file.exists());
    }

    @Test
    public void testExportDOCX() throws JRException {
        File file = new File(exportFolder, exportFileName + ".docx");
        exporter.exportDOCX(jasperPrint, file);
        assertTrue(file.exists());
    }

    @Test
    public void testExportPPT() throws JRException {
        File file = new File(exportFolder, exportFileName + ".ppt");
        exporter.exportPPT(jasperPrint, file);
        assertTrue(file.exists());
    }

    @Test
    public void testExportRTF() throws JRException {
        File file = new File(exportFolder, exportFileName + ".rtf");
        exporter.exportRTF(jasperPrint, file);
        assertTrue(file.exists());
    }

    @Test
    public void testExportODT() throws JRException {
        File file = new File(exportFolder, exportFileName + ".odt");
        exporter.exportODT(jasperPrint, file);
        assertTrue(file.exists());
    }

    @Test
    public void testDiagramsHaveBeenWritten() {
        File[] imageFiles = tmpFolder.listFiles();
        assertEquals(17, imageFiles.length);
        Arrays.sort(imageFiles);
        assertEquals("16fe3cf9.png", imageFiles[0].getName());
        assertEquals("3641.png", imageFiles[1].getName());
        assertEquals("3698.png", imageFiles[2].getName());
        assertEquals("3722.png", imageFiles[3].getName());
        assertEquals("3761.png", imageFiles[4].getName());
        assertEquals("3821.png", imageFiles[5].getName());
        assertEquals("3865.png", imageFiles[6].getName());
        assertEquals("3893.png", imageFiles[7].getName());
        assertEquals("3944.png", imageFiles[8].getName());
        assertEquals("3965.png", imageFiles[9].getName());
        assertEquals("3999.png", imageFiles[10].getName());
        assertEquals("4025.png", imageFiles[11].getName());
        assertEquals("4056.png", imageFiles[12].getName());
        assertEquals("4165.png", imageFiles[13].getName());
        assertEquals("4224.png", imageFiles[14].getName());
        assertEquals("4279.png", imageFiles[15].getName());
        assertEquals("4318.png", imageFiles[16].getName());
    }

    @Test
    public void testJasperPrint() {
        assertEquals("main_report", jasperPrint.getName());
    }
}
