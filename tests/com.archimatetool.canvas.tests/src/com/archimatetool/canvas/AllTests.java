/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.canvas.editparts.CanvasModelEditPartFactoryTests;
import com.archimatetool.canvas.factory.AllUIFactoryTests;


@Suite
@SelectClasses({
    // editparts
    CanvasModelEditPartFactoryTests.class,
    // factory
    AllUIFactoryTests.class,
    // this
    CanvasModelFactoryTests.class
})
@SuiteDisplayName("All Canvas Tests")
public class AllTests {
}