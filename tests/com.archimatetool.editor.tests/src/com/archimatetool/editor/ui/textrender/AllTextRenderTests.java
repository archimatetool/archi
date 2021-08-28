/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTextRenderTests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.editor.ui.textrender");

        suite.addTest(DocumentationRendererTests.suite());
        suite.addTest(IfRendererTests.suite());
        suite.addTest(NameRendererTests.suite());
        suite.addTest(PropertiesRendererTests.suite());
        suite.addTest(RelationshipRendererTests.suite());
        suite.addTest(SpecializationRendererTests.suite());
        suite.addTest(TextContentRendererTests.suite());
        suite.addTest(TextRendererTests.suite());
        suite.addTest(TypeRendererTests.suite());
        suite.addTest(ViewpointRendererTests.suite());
        suite.addTest(WordWrapRendererTests.suite());

        return suite;
    }

}