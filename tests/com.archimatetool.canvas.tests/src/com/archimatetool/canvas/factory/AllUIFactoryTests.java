/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    CanvasBlockUIProviderTests.class,
    CanvasImageUIProviderTests.class,
    CanvasLineConnectionUIProviderTests.class,
    CanvasModelUIProviderTests.class,
    CanvasStickyUIProviderTests.class
})

public class AllUIFactoryTests {
}