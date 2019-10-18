/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


@SuppressWarnings("nls")
public class ArchimateModelDataSourceTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelDataSourceTests.class);
    }

    private static IArchimateModel model;
    
    private ArchimateModelDataSource ds;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        ds = new ArchimateModelDataSource(model);
    }
    
    @Test
    public void getModelIsSame() {
        assertEquals(model, ds.getModel());
    }
    
    @Test
    public void getPropertiesDataSourceNotNull() {
        PropertiesModelDataSource source = ds.getPropertiesDataSource();
        assertNotNull(source);
    }

    @Test
    public void getViewsDataSourceNotNull() {
        ViewModelDataSource source = ds.getViewsDataSource();
        assertNotNull(source);
    }

    @Test
    public void getElementsDataSourceTypesNotNull() {
        ElementsDataSource source = ds.getElementsDataSource(IDataConstants.ELEMENTS_BUSINESS);
        assertNotNull(source);
        source = ds.getElementsDataSource(IDataConstants.ELEMENTS_APPLICATION);
        assertNotNull(source);
        source = ds.getElementsDataSource(IDataConstants.ELEMENTS_TECHNOLOGY);
        assertNotNull(source);
        source = ds.getElementsDataSource(IDataConstants.ELEMENTS_MOTIVATION);
        assertNotNull(source);
    }

    @Test
    public void hasElements() {
        assertTrue(ds.hasElements(IDataConstants.ELEMENTS_BUSINESS));
        assertTrue(ds.hasElements(IDataConstants.ELEMENTS_APPLICATION));
        assertTrue(ds.hasElements(IDataConstants.ELEMENTS_TECHNOLOGY));
        assertTrue(ds.hasElements(IDataConstants.ELEMENTS_MOTIVATION));
        assertFalse(ds.hasElements("bogus"));
    }
    
    @Test
    public void getElementEqualsModel() {
        assertEquals(model, ds.getElement());
    }

    @Test
    public void getElementByID() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createApplicationService();
        model.getDefaultFolderForObject(element).getElements().add(element);
        String id = element.getId();
        assertEquals(element, ds.getElementByID(id));
    }
   
    @Test
    public void next() throws JRException {
        assertTrue(ds.next());
        assertFalse(ds.next());
    }
    
    @Test
    public void testClone() {
        assertSame(model, ds.clone().getModel());
        assertNotSame(ds, ds.clone());
    }

    @Test
    public void getFieldValue() throws JRException {
        JRField field = mock(JRField.class);
        
        when(field.getName()).thenReturn("this");
        assertEquals(model, ds.getFieldValue(field));
        
        when(field.getName()).thenReturn("name");
        assertEquals(model.getName(), ds.getFieldValue(field));
    }

    
    @Test
    public void getClasses() {
        assertEquals(0, ArchimateModelDataSource.getClasses("").size());
        
        assertEquals(ArchimateModelUtils.getAllArchimateClasses().length, ArchimateModelDataSource.getClasses("elements").size());
        assertEquals(ArchimateModelUtils.getRelationsClasses().length, ArchimateModelDataSource.getClasses("relations").size());
        assertEquals(ArchimateModelUtils.getBusinessClasses().length, ArchimateModelDataSource.getClasses("business").size());
        
        assertEquals(ArchimateModelUtils.getBusinessClasses().length + ArchimateModelUtils.getTechnologyClasses().length,
                ArchimateModelDataSource.getClasses("business|technology").size());
        
        assertEquals(ArchimateModelUtils.getApplicationClasses().length + ArchimateModelUtils.getImplementationMigrationClasses().length,
                        ArchimateModelDataSource.getClasses("application|impl_migration").size());

        assertEquals(1, ArchimateModelDataSource.getClasses("BusinessService").size());
        assertEquals(3, ArchimateModelDataSource.getClasses("BusinessService|Node|DataObject").size());
        assertEquals(3, ArchimateModelDataSource.getClasses("BusinessService|Node|DataObject|NOTREAL").size());
    }

   
    @Test
    public void getConceptsInDiagram() {
        IDiagramModel dm = model.getDiagramModels().get(1);
        assertEquals(30, ArchimateModelDataSource.getConceptsInDiagram(dm, "elements").size());
        assertEquals(28, ArchimateModelDataSource.getConceptsInDiagram(dm, "relations").size());
        assertEquals(58, ArchimateModelDataSource.getConceptsInDiagram(dm, "elements|relations").size());
        assertEquals(15, ArchimateModelDataSource.getConceptsInDiagram(dm, "business").size());
        assertEquals(2, ArchimateModelDataSource.getConceptsInDiagram(dm, "BusinessActor").size());
    }
}
