/**
 * 
 */
package com.archimatetool.editor.diagram.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.archimatetool.model.IArchimatePackage;

import junit.framework.JUnit4TestAdapter;

/**
 * @author etienne-sf
 *
 */
public class ChangeElementTypeActionTests {

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ChangeElementTypeActionTests.class);
	}

	@Test
	public void test_getEClassListFromSuperType() {
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

}
