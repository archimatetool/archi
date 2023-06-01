/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IInfluenceRelationship;



/**
 * RelationshipRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class RelationshipRendererTests extends AbstractTextRendererTests {

    private RelationshipRenderer renderer = new RelationshipRenderer();
    
    @Override
    protected RelationshipRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Strength() {
        IInfluenceRelationship relation = IArchimateFactory.eINSTANCE.createInfluenceRelationship();
        relation.setStrength("+++");
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateConcept(relation);
        String result = renderer.render(dmc, "${strength}");
        assertEquals("+++", result);
    }
    
    @Test
    public void render_AccessType() {
        IAccessRelationship relation = IArchimateFactory.eINSTANCE.createAccessRelationship();
        relation.setAccessType(IAccessRelationship.READ_WRITE_ACCESS);
        IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        dmc.setArchimateConcept(relation);
        String result = renderer.render(dmc, "${accessType}");
        assertEquals("Read/Write", result);
    }
}
