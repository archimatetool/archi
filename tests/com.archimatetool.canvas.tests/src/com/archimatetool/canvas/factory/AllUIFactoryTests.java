/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
    CanvasBlockUIProviderTests.class,
    CanvasImageUIProviderTests.class,
    CanvasLineConnectionUIProviderTests.class,
    CanvasModelUIProviderTests.class,
    CanvasStickyUIProviderTests.class
})
@SuiteDisplayName("All Canvas UI FactoryTests")
public class AllUIFactoryTests {
}