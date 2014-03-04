/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.util.ArchimateModelUtils;


public abstract class AbstractViewpointTests {
    
    protected AbstractViewpoint vp;
    protected int index;
    
    @Test
    public void testIsElementVisible() {
        EClass[] types = vp.getAllowedTypes();
        if(types != null) {
            for(EClass eClass : types) {
                // All allowed types
                IArchimateElement element = (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
                assertTrue(vp.isElementVisible(element));
                
                // All allowed types as diagram objects
                IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
                dmo.setArchimateElement(element);
                assertTrue(vp.isElementVisible(dmo));
            }
        }
        // null means show all types
        else {
            for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
                EObject eObject = IArchimateFactory.eINSTANCE.create(eClass);
                assertTrue(vp.isElementVisible(eObject));
            }
            for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
                assertTrue(vp.isElementVisible(eClass));
            }
        }
        
        // Other types
        EObject eObject = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        assertTrue(vp.isElementVisible(eObject));
        
        eObject = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        assertTrue(vp.isElementVisible(eObject));
        
        eObject = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        assertTrue(vp.isElementVisible(eObject));
    }

    @Test
    public void testIsElementVisible_Nested() {
        EClass[] types = vp.getAllowedTypes();
        if(types != null) {
            // Test is visible in Group
            IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();

            for(EClass eClass : types) {
                IDiagramModelArchimateObject dmo =
                        ArchimateTestModel.createDiagramModelArchimateObject((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
                group.getChildren().add(dmo);
                
                assertTrue(vp.isElementVisible(dmo));
            }

            // Test child that is normally visible is hidden when inside of parent that is hidden
            for(EClass eClass : types) {
                // Create a child that should be visible
                IDiagramModelArchimateObject dmoChild =
                        ArchimateTestModel.createDiagramModelArchimateObject((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
                assertTrue(vp.isElementVisible(dmoChild));
                
                // Put the child in a parent that will be invisible (not an allowed type)
                IDiagramModelArchimateObject dmoParent =
                        ArchimateTestModel.createDiagramModelArchimateObject(createElementThatsNotAllowedType());
                dmoParent.getChildren().add(dmoChild);
                
                assertFalse(vp.isElementVisible(dmoChild));
            }
        }
    }
    
    @Test
    public void testIsAllowedType() {
        EClass[] types = vp.getAllowedTypes();
        if(types != null) {
            for(EClass eClass : types) {
                assertTrue(vp.isAllowedType(eClass));
            }
        }
        // null means allow all types
        else {
            for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
                assertTrue(vp.isAllowedType(eClass));
            }
            for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
                assertTrue(vp.isAllowedType(eClass));
            }
        }
        
        // Other types
        EClass eClass = IArchimatePackage.eINSTANCE.getDiagramModelNote();
        assertFalse(vp.isAllowedType(eClass));
        
        eClass = IArchimatePackage.eINSTANCE.getDiagramModelGroup();
        assertFalse(vp.isAllowedType(eClass));
        
        eClass = IArchimatePackage.eINSTANCE.getDiagramModelNote();
        assertFalse(vp.isAllowedType(eClass));
    }
    
    @Test
    public void testGetName() {
        assertNotNull(vp.getName());
    }
    
    @Test
    public void testGetIndex() {
        assertEquals(index, vp.getIndex());
    }
    
    private IArchimateElement createElementThatsNotAllowedType() {
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            if(!vp.isAllowedType(eClass)) {
                return (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
            }
        }
        
        return null;
    }
    
}
