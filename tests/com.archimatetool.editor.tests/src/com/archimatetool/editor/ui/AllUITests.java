/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.ui.factory.AllUIFactoryTests;
import com.archimatetool.editor.ui.textrender.AllTextRenderTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
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

public class AllUITests {
}