/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;

import junit.framework.JUnit4TestAdapter;

/**
 * AbstractTextRendererTests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public abstract class AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractTextRendererTests.class);
    }
    
    protected abstract AbstractTextRenderer getRenderer();
    
    
    @Test
    public void getActualObject() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        assertSame(group, getRenderer().getActualObject(group));
        
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        assertSame(folder, getRenderer().getActualObject(folder));
        
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IBusinessActor ba = IArchimateFactory.eINSTANCE.createBusinessActor();
        dmo.setArchimateConcept(ba);
        assertSame(ba, getRenderer().getActualObject(dmo));
        
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IAssociationRelationship relation = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        dmc.setArchimateConcept(relation);
        assertSame(relation, getRenderer().getActualObject(dmc));
    }

    
    @Test
    public void getObjectFromPrefix_NullObject() {
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createBusinessActor();
        assertNull( getRenderer().getObjectFromPrefix(object, ""));
    }

    @Test
    public void getObjectFromPrefix_NullPrefix() {
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createBusinessActor();
        assertSame(object, getRenderer().getObjectFromPrefix(object, null));
    }
    
    @Test
    public void getObjectFromPrefix_Model() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForObject(object).getElements().add(object);
        
        assertSame(model, getRenderer().getObjectFromPrefix(object, ITextRenderer.modelPrefix));
    }
    
    @Test
    public void getObjectFromPrefix_View() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        
        IDiagramModelObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(dmo);
        assertSame(dm, getRenderer().getObjectFromPrefix(dmo, ITextRenderer.viewPrefix));
        
        IDiagramModelConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        dmc.connect(dmo, dmo);
        assertSame(dm, getRenderer().getObjectFromPrefix(dmc, ITextRenderer.viewPrefix));
    }
    
    @Test
    public void getObjectFromPrefix_ModelFolder() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        IArchimateModelObject object = IArchimateFactory.eINSTANCE.createBusinessActor();
        IFolder folder = model.getDefaultFolderForObject(object);
        folder.getElements().add(object);
        
        assertSame(folder, getRenderer().getObjectFromPrefix(object, ITextRenderer.modelFolderPrefix));
    }

    @Test
    public void getObjectFromPrefix_ViewFolder() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IFolder folder = model.getDefaultFolderForObject(dm);
        folder.getElements().add(dm);

        assertSame(folder, getRenderer().getObjectFromPrefix(dm, ITextRenderer.viewFolderPrefix));
        
        IDiagramModelObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(dmo);
        assertSame(folder, getRenderer().getObjectFromPrefix(dmo, ITextRenderer.viewFolderPrefix));
        
        IDiagramModelConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        dmc.connect(dmo, dmo);
        assertSame(folder, getRenderer().getObjectFromPrefix(dmc, ITextRenderer.viewFolderPrefix));
    }
    
    @Test
    public void getObjectFromPrefix_ConnectionSourceTarget() {
        IAssociationRelationship relation = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IBusinessActor ba1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IBusinessActor ba2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        relation.connect(ba1, ba2);
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateConcept(ba1);
        
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateConcept(ba2);
        
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateConcept(relation);
        
        dmc.connect(dmo1, dmo2);
        
        assertSame(ba1, getRenderer().getObjectFromPrefix(dmc, ITextRenderer.sourcePrefix));
        assertSame(ba2, getRenderer().getObjectFromPrefix(dmc, ITextRenderer.targetPrefix));
    }

    @Test
    public void getObjectFromPrefix_LinkedObject() {
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        IBusinessActor ba = IArchimateFactory.eINSTANCE.createBusinessActor();
        IBusinessRole br = IArchimateFactory.eINSTANCE.createBusinessRole();
        relation.connect(ba, br);
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateConcept(ba);
        
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateConcept(br);
        
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateConcept(relation);
        
        dmc.connect(dmo1, dmo2);
        
        assertSame(ba, getRenderer().getObjectFromPrefix(dmo2, "assignment:source"));
        assertSame(br, getRenderer().getObjectFromPrefix(dmo1, "assignment:target"));
    }
    
}
