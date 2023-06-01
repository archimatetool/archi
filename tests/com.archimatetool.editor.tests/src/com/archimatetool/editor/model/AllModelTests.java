/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.model.commands.CommandsTests;
import com.archimatetool.editor.model.compatibility.ModelCompatibilityTests;
import com.archimatetool.editor.model.compatibility.handlers.ArchiMate2To3HandlerTests;
import com.archimatetool.editor.model.compatibility.handlers.FixDefaultSizesHandlerTests;
import com.archimatetool.editor.model.compatibility.handlers.OutlineOpacityHandlerTests;
import com.archimatetool.editor.model.impl.ArchiveManagerTests;
import com.archimatetool.editor.model.impl.ByteArrayStorageTests;
import com.archimatetool.editor.model.impl.EditorModelManagerTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    // model
    DiagramModelUtilsTests.class,
    DiagramModelUtilsNestedRelationsTests.class,
    IArchiveManagerTests.class,
    ModelCheckerTests.class,
    // model.commands
    CommandsTests.class,
    // model.compatibility
    ModelCompatibilityTests.class,
    // model.compatibility.handlers
    ArchiMate2To3HandlerTests.class,
    FixDefaultSizesHandlerTests.class,
    OutlineOpacityHandlerTests.class,
    // model.impl
    ArchiveManagerTests.class,
    ByteArrayStorageTests.class,
    EditorModelManagerTests.class
})

public class AllModelTests {
}