/**
 * 
 */
package com.archimatetool.editor.diagram.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.editor.diagram.actions.ChangeElementTypeAction;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

/**
 * @author gauthiereti
 *
 */
public class ChangeElementTypeCommandTests {

	ArchimateElement elementBusinessObject;
	DiagramModelArchimateObject dm;

//	public static junit.framework.Test suite() {
//		return new JUnit4TestAdapter(ChangeElementTypeCommandTests.class);
//	}

//	@Before
	public void runBeforeEachTest() throws IOException {
		ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
		IArchimateModel model = tm.loadModelWithCommandStack();
		dm = (DiagramModelArchimateObject) ArchimateModelUtils.getObjectByID(model, "674");
		elementBusinessObject = (ArchimateElement) dm.getArchimateElement();
		assertEquals("Check that we've got the expected element (name)", "Customer File", elementBusinessObject.getName());
		assertEquals("Check that we've got the expected element (type)", IArchimatePackage.eINSTANCE.getBusinessObject(),
				elementBusinessObject.eClass());
	}

	/**
	 * Create an instance of every available Archimate type, to check that there is no issue with these creations
	 */
	// TODO allow and recheck this test, once ArchimateTestModel doesn't throw an exception any more
//	@Test
	public void test_createArchimateElement() {
		for (EClass type : ChangeElementTypeAction.archimateObjectTypes) {
			for (EClass eclass : ChangeElementTypeAction.archimateObjects.get(type)) {
				ChangeElementTypeCommand command = new ChangeElementTypeCommand(dm, eclass);
				// Let's check the instance creation
				assertNotNull("Check creation of instance of " + eclass.getName(), command.createArchimateElement(eclass));
			}
		} // for(archimateObjectTypes)
	}

//	@Test
	// TODO allow and finish coding this test, once ArchimateTestModel doesn't throw an exception any more
	public void test_changeType() {

		// In the Archinsurance model, the BusinessObject "Customer File", is used in two distinct views, and is used twice in the "Business Process
		// View" view.
		// It is stored into the "Business/Information" folder

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// First transfo: from a BusinessObject to an ApplicationObject
		ChangeElementTypeCommand commandApplicationObject = new ChangeElementTypeCommand(dm, IArchimatePackage.eINSTANCE.getDataObject());
		assertEquals("(before 1) The source Business Object is 3 times in the model", 3, commandApplicationObject.modelProperties.size());
		assertEquals("(before 1) can execute", true, commandApplicationObject.canExecute());
		assertEquals("(before 1) IFolder", true, elementBusinessObject.eContainer() instanceof IFolder);
		assertEquals("(before 1) folder", "Information", ((IFolder) elementBusinessObject.eContainer()).getName());
		// Let's do the type change 1
		commandApplicationObject.execute();
//        
//        SelectAllAction action = new SelectAllAction(mock(IWorkbenchPart.class));
//        Set<GraphicalEditPart> selected = action.getSelectableEditParts(editor.getGraphicalViewer().getContents());
//        assertEquals(47, selected.size());

	}
}
