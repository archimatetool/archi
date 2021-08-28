/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IProfile;

import junit.framework.JUnit4TestAdapter;



/**
 * SpecializationRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class SpecializationRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SpecializationRendererTests.class);
    }
    
    private SpecializationRenderer renderer = new SpecializationRenderer();
    
    private IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
    private IDiagramModelArchimateConnection dmc = TextRendererTests.createDiagramModelConnection();
    
    private IProfile profile1, profile2, profile3;
    
    @Before
    public void runOnceBeforeEachTest() {
        profile1 = dmo.getArchimateConcept().getPrimaryProfile();
        profile2 = ((IDiagramModelArchimateComponent)dmc.getSource()).getArchimateConcept().getPrimaryProfile();
        profile3 = ((IDiagramModelArchimateComponent)dmc.getTarget()).getArchimateConcept().getPrimaryProfile();
    }

    @Override
    protected SpecializationRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Specialization() {
        String result = renderer.render(dmo, "${specialization}");
        assertEquals(profile1.getName(), result);
    }
    
    @Test
    public void render_SourceSpecialization() {
        String result = renderer.render(dmc, "$source{specialization}");
        assertEquals(profile2.getName(), result);
    }
    
    @Test
    public void render_TargetSpecialization() {
        String result = renderer.render(dmc, "$target{specialization}");
        assertEquals(profile3.getName(), result);
    }

    @Test
    public void render_ParentSpecialization() {
        IDiagramModelArchimateObject parent = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createBusinessActor();
        concept.getProfiles().add(profile1);
        parent.setArchimateConcept(concept);
        parent.getChildren().add(dmo);
        String result = renderer.render(dmo, "$parent{specialization}");
        assertEquals(profile1.getName(), result);
    }

    @Test
    public void render_ConnectedSourceName() {
        String result = renderer.render(dmc.getTarget(), "$assignment:source{specialization}");
        assertEquals(profile2.getName(), result);
    }
    
    @Test
    public void render_ConnectedTargetName() {
        String result = renderer.render(dmc.getSource(), "$assignment:target{specialization}");
        assertEquals(profile3.getName(), result);
    }

}
