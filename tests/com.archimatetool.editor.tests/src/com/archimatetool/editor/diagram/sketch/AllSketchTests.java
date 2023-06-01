/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactoryTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    SketchEditPartFactoryTests.class,
    SketchModelFactoryTests.class
})

public class AllSketchTests {
}