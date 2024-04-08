/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.editor.ui.factory.AllUIFactoryTests;
import com.archimatetool.editor.ui.textrender.AllTextRenderTests;

@Suite
@SelectClasses({
    // ui
    ArchiLabelProviderTests.class,
    ColorFactoryTests.class,
    FigureImagePreviewFactoryTests.class,
    FontFactoryTests.class,
    ImageFactoryTests.class,
    // factory
    AllUIFactoryTests.class,
    // textrender
    AllTextRenderTests.class
})
@SuiteDisplayName("All UI Tests")
public class AllUITests {
}