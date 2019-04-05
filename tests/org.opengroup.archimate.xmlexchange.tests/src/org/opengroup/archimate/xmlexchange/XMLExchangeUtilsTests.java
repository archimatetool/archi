/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;


/**
 * XML Model Exporter Tests
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangeUtilsTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(XMLExchangeUtilsTests.class);
    }

    @Test
    public void testGetAbsoluteBounds() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        
        IDiagramModelGroup dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo1.setBounds(10, 15, 500, 500);
        dm.getChildren().add(dmo1);
        
        IBounds bounds = XMLExchangeUtils.getAbsoluteBounds(dmo1);
        assertEquals(10, bounds.getX());
        assertEquals(15, bounds.getY());
        
        IDiagramModelGroup dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo2.setBounds(10, 15, 400, 400);
        dmo1.getChildren().add(dmo2);

        bounds = XMLExchangeUtils.getAbsoluteBounds(dmo2);
        assertEquals(20, bounds.getX());
        assertEquals(30, bounds.getY());
        
        IDiagramModelGroup dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo3.setBounds(10, 15, 300, 300);
        dmo2.getChildren().add(dmo3);

        bounds = XMLExchangeUtils.getAbsoluteBounds(dmo3);
        assertEquals(30, bounds.getX());
        assertEquals(45, bounds.getY());
    }
    
    
    @Test
    public void testGetRelativeBounds() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        
        // Add main parent diagram model object
        IDiagramModelGroup dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo1.setBounds(10, 10, 200, 200);
        dm.getChildren().add(dmo1);
        
        // Add child
        IDiagramModelGroup dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo1.getChildren().add(dmo2);

        // Get relative bounds
        IBounds absoluteBounds = IArchimateFactory.eINSTANCE.createBounds(50, 60, 100, 100);
        IBounds relativebounds = XMLExchangeUtils.getRelativeBounds(absoluteBounds, dmo1);
        assertEquals(40, relativebounds.getX());
        assertEquals(50, relativebounds.getY());
        dmo2.setBounds(relativebounds);
        
        IDiagramModelGroup dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo2.getChildren().add(dmo3);

        absoluteBounds = IArchimateFactory.eINSTANCE.createBounds(90, 75, 500, 500);
        relativebounds = XMLExchangeUtils.getRelativeBounds(absoluteBounds, dmo2);
        assertEquals(40, relativebounds.getX());
        assertEquals(15, relativebounds.getY());
        dmo3.setBounds(relativebounds);
    }
    
    @Test
    public void testGetNegativeOffsetForDiagram() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        
        IDiagramModelObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dmo1.setBounds(10, 10, 100, 100);
        dm.getChildren().add(dmo1);
        
        Point pt = XMLExchangeUtils.getNegativeOffsetForDiagram(dm);
        assertEquals(0, pt.x);
        assertEquals(0, pt.y);

        IDiagramModelObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        dmo2.setBounds(0, 0, 100, 100);
        dm.getChildren().add(dmo2);
        
        pt = XMLExchangeUtils.getNegativeOffsetForDiagram(dm);
        assertEquals(0, pt.x);
        assertEquals(0, pt.y);
        
        dmo1.setBounds(-10, -300, 100, 100);
        dmo2.setBounds(-100, -200, 100, 100);
        
        pt = XMLExchangeUtils.getNegativeOffsetForDiagram(dm);
        assertEquals(-100, pt.x);
        assertEquals(-300, pt.y);
    }

}
