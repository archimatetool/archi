/**
 * 
 */
package com.archimatetool.editor.diagram.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.archimatetool.editor.diagram.actions.ChangeElementTypeAction.ChangeElementTypeCommand;
import com.archimatetool.model.IArchimatePackage;

import junit.framework.JUnit4TestAdapter;

/**
 * @author gauthiereti
 *
 */
public class ChangeElementTypeActionTests {

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ChangeElementTypeActionTests.class);
	}

	@Test
	public void test_getEClassListFromSuperType() {
		// Below, an initialization, just for inspection at debug time
		IArchimatePackage archimatePackage = IArchimatePackage.eINSTANCE;

		///////////////////////////////////////////////////////////////////////////////////////////////
		///////// StrategyElement
		///////////////////////////////////////////////////////////////////////////////////////////////

		// Let's get all the eClasses of a type, transform the list as a list of String,
		// so that it's easier to check
		List<String> names = new ArrayList<>();
		for (EClass c : ChangeElementTypeAction.archimateObjects.get(IArchimatePackage.eINSTANCE.getStrategyElement())) {
			names.add(c.getName());
		}
		assertEquals(4, names.size());
		assertTrue(names.contains("Resource"));
		assertTrue(names.contains("Capability"));
		assertTrue(names.contains("ValueStream"));
		assertTrue(names.contains("CourseOfAction"));

		///////////////////////////////////////////////////////////////////////////////////////////////
		///////// BusinessElement
		///////////////////////////////////////////////////////////////////////////////////////////////

		// Let's get all the eClasses of a type, transform the list as a list of String,
		// so that it's easier to check
		names = new ArrayList<>();
		for (EClass c : ChangeElementTypeAction.archimateObjects.get(IArchimatePackage.eINSTANCE.getBusinessElement())) {
			names.add(c.getName());
		}

		assertEquals(13, names.size());
		assertTrue(names.contains("BusinessActor"));
		assertTrue(names.contains("BusinessCollaboration"));
		assertTrue(names.contains("BusinessEvent"));
		assertTrue(names.contains("BusinessFunction"));
		assertTrue(names.contains("BusinessInteraction"));
		assertTrue(names.contains("BusinessInterface"));
		assertTrue(names.contains("BusinessObject"));
		assertTrue(names.contains("BusinessProcess"));
		assertTrue(names.contains("BusinessRole"));
		assertTrue(names.contains("BusinessService"));
		assertTrue(names.contains("Contract"));
		assertTrue(names.contains("Representation"));
		assertTrue(names.contains("Product"));
	}

	/**
	 * Create an instance of every available Archimate type, to check that there is no issue with these creations
	 */
	@Test
	public void test_createArchimateElement() {
		for (EClass type : ChangeElementTypeAction.archimateObjectTypes) {
			for (EClass eclass : ChangeElementTypeAction.archimateObjects.get(type)) {
				ChangeElementTypeCommand command = new ChangeElementTypeCommand(null, eclass);
				// Let's check the instance creation
				assertNotNull("Check creation of instance of " + eclass.getName(), command.createArchimateElement(eclass));
			}
		} // for(archimateObjectTypes)
	}
}
