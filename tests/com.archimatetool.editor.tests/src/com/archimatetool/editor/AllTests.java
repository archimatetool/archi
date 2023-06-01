/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.actions.AllActionsTests;
import com.archimatetool.editor.diagram.AllDiagramTests;
import com.archimatetool.editor.model.AllModelTests;
import com.archimatetool.editor.p2.AllP2Tests;
import com.archimatetool.editor.propertysections.AllPropertySectionsTests;
import com.archimatetool.editor.ui.AllUITests;
import com.archimatetool.editor.utils.AllUtilsTests;
import com.archimatetool.editor.views.AllViewsTests;


@RunWith(Suite.class)

@Suite.SuiteClasses({
    // actions
    AllActionsTests.class,
    // diagram
    AllDiagramTests.class,
    // model
    AllModelTests.class,
    // p2
    AllP2Tests.class,
    // propertysections
    AllPropertySectionsTests.class,
    // ui
    AllUITests.class,
    // utils
    AllUtilsTests.class,
    // views
    AllViewsTests.class
})

public class AllTests {
}