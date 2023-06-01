/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.canvas.editparts.CanvasModelEditPartFactoryTests;
import com.archimatetool.canvas.factory.AllUIFactoryTests;


@RunWith(Suite.class)

@Suite.SuiteClasses({
    // editparts
    CanvasModelEditPartFactoryTests.class,
    // factory
    AllUIFactoryTests.class,
    // this
    CanvasModelFactoryTests.class
})

public class AllTests {
}