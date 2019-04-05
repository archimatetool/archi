/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import static org.junit.Assert.assertEquals;

import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelObject;

import junit.framework.JUnit4TestAdapter;


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
