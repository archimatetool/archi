/**
 * 
 */
package com.archimatetool.editor.diagram.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.actions.ChangeElementTypeAction;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.impl.ArchimateElement;
import com.archimatetool.model.impl.ArchimateModel;
import com.archimatetool.model.impl.DiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;

/**
 * @author etienne-sf
 *
 */
public class ChangeElementTypeCommandTests {

	private static final String ID_ARCHIMATE_ELEMENT = "674";
	private static final String ID_DIAGRAM_MODEL_ARCHIMATE_OBJECT = "3786";

	IArchimateModel model;
	DiagramModelArchimateObject dmo;
	ArchimateElement elementBusinessObject;

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ChangeElementTypeCommandTests.class);
	}

	@Before
	public void runBeforeEachTest() throws IOException {
		ArchimateTestModel tm = new ArchimateTestModel(new File(TestSupport.getTestDataFolder(), "models/Archisurance.archimate"));
		model = tm.loadModelWithCommandStack();

		elementBusinessObject = (ArchimateElement) ArchimateModelUtils.getObjectByID(model, ID_ARCHIMATE_ELEMENT);
		assertNotNull("Must have found the element on which we will execute the tests", elementBusinessObject);
		assertEquals("Check that we've got the expected element (name)", "Customer File", elementBusinessObject.getName());
		assertEquals("Check that we've got the expected element (type)", IArchimatePackage.eINSTANCE.getBusinessObject(),
				elementBusinessObject.eClass());

		dmo = (DiagramModelArchimateObject) ArchimateModelUtils.getObjectByID(model, ID_DIAGRAM_MODEL_ARCHIMATE_OBJECT);
		assertNotNull("Must have found the dmo on which we will execute the tests", dmo);
		assertEquals("Must have found the dmo on which we will execute the tests (check element)", elementBusinessObject, dmo.getArchimateElement());
	}

	/**
	 * Create an instance of every available Archimate type, to check that there is no issue with these creations
	 */
	// TODO allow and recheck this test, once ArchimateTestModel doesn't throw an exception any more
	@Test
	public void test_createArchimateElement() {
		for (EClass type : ChangeElementTypeAction.archimateObjectTypes) {
			for (EClass eclass : ChangeElementTypeAction.archimateObjects.get(type)) {
				ChangeElementTypeCommand command = new ChangeElementTypeCommand(dmo, eclass);
				// Let's check the instance creation
				assertNotNull("Check creation of instance of " + eclass.getName(), command.createArchimateElement(eclass));
			}
		} // for(archimateObjectTypes)
	}

	// TODO allow and finish coding this test, once ArchimateTestModel doesn't throw an exception any more
	@Test
	public void test_changeType() {

		// Some other checks, before beginning
		checkArchimateElementIsUnique(elementBusinessObject);
		assertEquals("(before 1) IFolder", true, elementBusinessObject.eContainer() instanceof IFolder);
		assertEquals("(before 1) folder", "Information", ((IFolder) elementBusinessObject.eContainer()).getName());

		// In the Archinsurance model, the BusinessObject "Customer File", is used in two distinct views, and is used twice in the "Business Process
		// View" view.
		// It is stored into the "Business/Information" folder

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// First transformation: from a BusinessObject to an ApplicationObject
		ChangeElementTypeCommand change1ToBusinessRole = new ChangeElementTypeCommand(dmo, IArchimatePackage.eINSTANCE.getBusinessRole());
		assertEquals("(before 1) The source Business Object is 3 times in the model", 3, change1ToBusinessRole.modelProperties.size());
		assertEquals("(before 1) can execute", true, change1ToBusinessRole.canExecute());
		// Let's do the type change 1
		change1ToBusinessRole.execute();

		checkBusinessRole("after 1");

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Second transformation: from a BusinessObject to an ApplicationObject
		ChangeElementTypeCommand change2ToDataObject = new ChangeElementTypeCommand(dmo, IArchimatePackage.eINSTANCE.getDataObject());
		assertEquals("(before 2) The source Business Object is 3 times in the model", 3, change2ToDataObject.modelProperties.size());
		assertEquals("(before 2) can execute", true, change2ToDataObject.canExecute());
		// Let's do the type change 2
		change2ToDataObject.execute();
		//
		checkDataObject("after 2");

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Undo second transformation: ApplicationObject back to a BusinessRole
		assertTrue("change2ToDataObject can be undone", change2ToDataObject.canUndo());
		//
		change2ToDataObject.undo();
		//
		checkBusinessRole("after undo2");

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Undo first transformation: BusinessObject back to a BusinessObject
		assertTrue("change1ToBusinessRole can be undone", change1ToBusinessRole.canUndo());
		//
		change1ToBusinessRole.undo();
		//
		ArchimateElement newElement = (ArchimateElement) ArchimateModelUtils.getObjectByID(model, ID_ARCHIMATE_ELEMENT);
		assertEquals("(after undo1, id) Properties are saved", elementBusinessObject.getId(), newElement.getId());
		assertEquals("(after undo1, name) Properties are saved", elementBusinessObject.getName(), newElement.getName());
		assertEquals("(after undo1, documentation) Properties are saved", elementBusinessObject.getDocumentation(), newElement.getDocumentation());
		assertEquals("(after undo1, props) Properties are saved", elementBusinessObject.getProperties(), newElement.getProperties());
		assertNotNull("(after undo1) There must be an element with this id: " + ID_ARCHIMATE_ELEMENT, newElement);
		assertEquals("(after undo1) It must be of this type", IArchimatePackage.eINSTANCE.getBusinessObject(), newElement.eClass());
		assertEquals("(after undo1) IFolder", true, newElement.eContainer() instanceof IFolder);
		assertEquals("(after undo1) folder, the folder is again Information", "Information", ((IFolder) newElement.eContainer()).getName());
		checkArchimateElementIsUnique(newElement);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Redo first transformation: BusinessObject again changed to a BusinessRole
		assertTrue("change1ToBusinessRole can be redone", change1ToBusinessRole.canRedo());
		//
		change1ToBusinessRole.redo();
		//
		checkBusinessRole("after redo1");

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Redo second transformation: BusinessRole again changed to a Data Object
		assertTrue("change2ToDataObject can be redone", change2ToDataObject.canRedo());
		//
		change2ToDataObject.redo();
		//
		checkDataObject("after redo2");
	}

	private void checkDataObject(String change) {
		ArchimateElement dataObjectElement = (ArchimateElement) ArchimateModelUtils.getObjectByID(model, ID_ARCHIMATE_ELEMENT);
		assertEquals("(" + change + ", id) Properties are saved", elementBusinessObject.getId(), dataObjectElement.getId());
		assertEquals("(" + change + ", name) Properties are saved", elementBusinessObject.getName(), dataObjectElement.getName());
		assertEquals("(" + change + ", documentation) Properties are saved", elementBusinessObject.getDocumentation(),
				dataObjectElement.getDocumentation());
		assertEquals("(" + change + ", props) Properties are saved", elementBusinessObject.getProperties(), dataObjectElement.getProperties());
		assertNotNull("(" + change + ") There must be an element with this id: " + ID_ARCHIMATE_ELEMENT, dataObjectElement);
		assertEquals("(" + change + ") It must be of this type", IArchimatePackage.eINSTANCE.getDataObject(), dataObjectElement.eClass());
		assertEquals("(" + change + ") IFolder", true, dataObjectElement.eContainer() instanceof IFolder);
		assertEquals("(" + change + ") folder, the folder is now the default folder for Data Objects", "Application",
				((IFolder) dataObjectElement.eContainer()).getName());
		checkArchimateElementIsUnique(dataObjectElement);
	}

	private void checkBusinessRole(String change) {
		ArchimateElement elementBusinessRole = (ArchimateElement) ArchimateModelUtils.getObjectByID(model, ID_ARCHIMATE_ELEMENT);

		assertEquals("(" + change + ", id) Properties are saved", elementBusinessObject.getId(), elementBusinessRole.getId());
		assertEquals("(" + change + ", name) Properties are saved", elementBusinessObject.getName(), elementBusinessRole.getName());
		assertEquals("(" + change + ", documentation) Properties are saved", elementBusinessObject.getDocumentation(),
				elementBusinessRole.getDocumentation());
		assertEquals("(" + change + ", props) Properties are saved", elementBusinessObject.getProperties(), elementBusinessRole.getProperties());
		assertNotNull("(" + change + ") There must be an element with this id: " + ID_ARCHIMATE_ELEMENT, elementBusinessRole);
		assertEquals("(" + change + ") It must be of this type", IArchimatePackage.eINSTANCE.getBusinessRole(), elementBusinessRole.eClass());
		assertEquals("(" + change + ") IFolder", true, elementBusinessRole.eContainer() instanceof IFolder);
		assertEquals("(" + change + ") folder, the folder has not changed", "Information", ((IFolder) elementBusinessRole.eContainer()).getName());
		checkArchimateElementIsUnique(elementBusinessRole);
	}

	/**
	 * This method go through all the content of the {@link ArchimateModel}, and check that no other element has the same id.
	 * 
	 * @param element The element which id is checked. This element must be found at least once, or the unit test fails (a call to
	 *                {@link #fail(String)} is executed
	 */
	private void checkArchimateElementIsUnique(ArchimateElement element) {

		boolean found = false;

		for (Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
			EObject e = iter.next();
			if (e instanceof IIdentifier && ((IIdentifier) e).getId().equals(element.getId())) {
				assertEquals("another item of same id has been found. It must be the same object", element, e);
				// If Ok, then we have found the provide element
				found = true;
			}
		}

		assertTrue("The element of if " + element.getId() + " must exist in the model", found);
	}
}
