/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import junit.framework.JUnit4TestAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;


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
    public void testGetModel() {
        assertEquals(model, ds.getModel());
    }
    
    @Test
    public void testGetPropertiesDataSource() {
        PropertiesModelDataSource source = ds.getPropertiesDataSource();
        assertNotNull(source);
    }

    @Test
    public void testGetViewsDataSource() {
        ViewModelDataSource source = ds.getViewsDataSource();
        assertNotNull(source);
    }

    @Test
    public void testGetElementsDataSource() {
        ElementsDataSource source = ds.getElementsDataSource(ElementsDataSource.ELEMENTS_APPLICATION_DATA);
        assertNotNull(source);
        source = ds.getElementsDataSource(ElementsDataSource.ELEMENTS_APPLICATIONS);
        assertNotNull(source);
        source = ds.getElementsDataSource(ElementsDataSource.ELEMENTS_MOTIVATION);
        assertNotNull(source);
    }

    @Test
    public void testHasElements() {
        assertTrue(ds.hasElements(ElementsDataSource.ELEMENTS_BUSINESS_ACTORS));
        assertTrue(ds.hasElements(ElementsDataSource.ELEMENTS_APPLICATION_DATA));
        assertFalse(ds.hasElements("bogus"));
    }
    
    @Test
    public void testGetElement() {
        assertEquals(model, ds.getElement());
    }

    @Test
    public void testGetElementByID() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createApplicationService();
        model.getDefaultFolderForElement(element).getElements().add(element);
        String id = element.getId();
        assertEquals(element, ds.getElementByID(id));
    }
   
    @Test
    public void testNext() throws JRException {
        assertTrue(ds.next());
        assertFalse(ds.next());
    }
    
    @Test
    public void testGetFieldValue() throws JRException {
        JRField field = mock(JRField.class);
        
        when(field.getName()).thenReturn("this");
        assertEquals(model, ds.getFieldValue(field));
        
        when(field.getName()).thenReturn("name");
        assertEquals(model.getName(), ds.getFieldValue(field));
    }

}
