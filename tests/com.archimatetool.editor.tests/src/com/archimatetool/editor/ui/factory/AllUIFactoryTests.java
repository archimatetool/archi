/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllUIFactoryTests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.editor.ui.factory");

        suite.addTest(ArchimateDiagramModelUIProviderTests.suite());
        suite.addTest(ArchimateModelUIProviderTests.suite());
        
        suite.addTest(AllArchiMateElementUIProviderTests.suite());
        suite.addTest(AllArchimateRelationshipUIProviderTests.suite());
        
        suite.addTest(DiagramModelImageUIProviderTests.suite());
        suite.addTest(DiagramModelReferenceUIProviderTests.suite());
        suite.addTest(FolderUIProviderTests.suite());
        suite.addTest(GroupUIProviderTests.suite());
        suite.addTest(LineConnectionUIProviderTests.suite());
        suite.addTest(NoteUIProviderTests.suite());
        
        suite.addTest(SketchActorUIProviderTests.suite());
        suite.addTest(SketchModelUIProviderTests.suite());
        suite.addTest(SketchStickyUIProviderTests.suite());
        
        suite.addTest(ObjectUIFactoryProviderTests.suite());

        return suite;
    }

}