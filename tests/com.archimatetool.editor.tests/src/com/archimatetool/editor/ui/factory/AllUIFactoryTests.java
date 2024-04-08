/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
    ArchimateDiagramModelUIProviderTests.class,
    ArchimateModelUIProviderTests.class,
    
    AllArchiMateElementUIProviderTests.class,
    AllArchimateRelationshipUIProviderTests.class,
    
    DiagramModelImageUIProviderTests.class,
    DiagramModelReferenceUIProviderTests.class,
    FolderUIProviderTests.class,
    GroupUIProviderTests.class,
    LineConnectionUIProviderTests.class,
    NoteUIProviderTests.class,
    
    SketchActorUIProviderTests.class,
    SketchModelUIProviderTests.class,
    SketchStickyUIProviderTests.class,
    
    ObjectUIFactoryProviderTests.class
})
@SuiteDisplayName("All UI Factory Tests")
public class AllUIFactoryTests {
}