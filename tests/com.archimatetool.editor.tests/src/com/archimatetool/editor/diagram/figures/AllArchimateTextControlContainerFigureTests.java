/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;

@SuppressWarnings("nls")
@RunWith(Parameterized.class)
public class AllArchimateTextControlContainerFigureTests extends AbstractTextControlContainerFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchimateTextControlContainerFigureTests.class);
    }
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        List<EClass[]> list = new ArrayList<EClass[]>();
        
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            list.add(new EClass[] { eClass });
        }
        
        return list;
    }
    
    private EClass eClass;
    
    public AllArchimateTextControlContainerFigureTests(EClass eClass) {
        this.eClass = eClass;
    }

    @Override
    protected AbstractDiagramModelObjectFigure createFigure() {
        IDiagramModelArchimateObject dmo =
                ArchimateTestModel.createDiagramModelArchimateObject((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setName("Hello World!");
        dm.getChildren().add(dmo);
        
        editor.layoutPendingUpdates();
        
        return (AbstractDiagramModelObjectFigure)editor.findFigure(dmo);
    }
    
    @Override
    @Test
    public void testDidClickTextControl() {
        Rectangle bounds = abstractFigure.getTextControl().getBounds().getCopy();
        abstractFigure.getTextControl().translateToAbsolute(bounds);
        assertTrue(abstractFigure.didClickTextControl(new Point(bounds.x + 10, bounds.y + 5)));
    }
}
