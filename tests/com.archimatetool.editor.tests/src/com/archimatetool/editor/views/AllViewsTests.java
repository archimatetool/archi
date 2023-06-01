/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.views.tree.TreeModelViewerDragDropHandlerTests;
import com.archimatetool.editor.views.tree.TreeModelViewerFindReplaceProviderTests;
import com.archimatetool.editor.views.tree.commands.DeleteCommandHandlerTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    // views.tree
    TreeModelViewerDragDropHandlerTests.class,
    TreeModelViewerFindReplaceProviderTests.class,
    // views.tree.commands
    DeleteCommandHandlerTests.class
})

public class AllViewsTests {
}