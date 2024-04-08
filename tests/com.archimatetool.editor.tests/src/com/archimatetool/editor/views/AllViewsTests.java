/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.editor.views.tree.TreeModelViewerDragDropHandlerTests;
import com.archimatetool.editor.views.tree.TreeModelViewerFindReplaceProviderTests;
import com.archimatetool.editor.views.tree.commands.DeleteCommandHandlerTests;

@Suite
@SelectClasses({
    // views.tree
    TreeModelViewerDragDropHandlerTests.class,
    TreeModelViewerFindReplaceProviderTests.class,
    // views.tree.commands
    DeleteCommandHandlerTests.class
})
@SuiteDisplayName("All Views Tests")
public class AllViewsTests {
}