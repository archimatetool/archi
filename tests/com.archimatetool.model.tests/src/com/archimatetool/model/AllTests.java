/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.model.impl.AllArchimateElementTypeTests;
import com.archimatetool.model.impl.AllArchimateRelationshipTypeTests;
import com.archimatetool.model.impl.ArchimateDiagramModelTests;
import com.archimatetool.model.impl.ArchimateFactoryTests;
import com.archimatetool.model.impl.ArchimateModelTests;
import com.archimatetool.model.impl.BoundsTests;
import com.archimatetool.model.impl.DiagramModelArchimateConnectionTests;
import com.archimatetool.model.impl.DiagramModelArchimateObjectTests;
import com.archimatetool.model.impl.DiagramModelBendpointTests;
import com.archimatetool.model.impl.DiagramModelConnectionTests;
import com.archimatetool.model.impl.DiagramModelExtraTests;
import com.archimatetool.model.impl.DiagramModelGroupTests;
import com.archimatetool.model.impl.DiagramModelImageTests;
import com.archimatetool.model.impl.DiagramModelNoteTests;
import com.archimatetool.model.impl.DiagramModelReferenceTests;
import com.archimatetool.model.impl.FeaturesEListTests;
import com.archimatetool.model.impl.FolderTests;
import com.archimatetool.model.impl.MetadataTests;
import com.archimatetool.model.impl.ProfileTests;
import com.archimatetool.model.impl.PropertyTests;
import com.archimatetool.model.impl.SketchModelActorTests;
import com.archimatetool.model.impl.SketchModelStickyTests;
import com.archimatetool.model.impl.SketchModelTests;
import com.archimatetool.model.util.ArchimateModelUtilsTests;
import com.archimatetool.model.util.ArchimateResourceFactoryTests;
import com.archimatetool.model.util.RelationshipsMatrixTests;
import com.archimatetool.model.util.UUIDFactoryTests;
import com.archimatetool.model.viewpoints.ViewpointManagerTests;
import com.archimatetool.model.viewpoints.ViewpointTests;

@Suite
@SelectClasses({
    // impl
    ArchimateDiagramModelTests.class,
    ArchimateFactoryTests.class,
    ArchimateModelTests.class,

    DiagramModelArchimateConnectionTests.class,
    DiagramModelArchimateObjectTests.class,
    DiagramModelBendpointTests.class,
    DiagramModelConnectionTests.class,
    DiagramModelExtraTests.class,
    DiagramModelGroupTests.class,
    DiagramModelImageTests.class,
    DiagramModelNoteTests.class,
    DiagramModelReferenceTests.class,
    
    BoundsTests.class,
    FolderTests.class,
    MetadataTests.class,
    FeaturesEListTests.class,
    ProfileTests.class,
    PropertyTests.class,
    
    SketchModelTests.class,
    SketchModelActorTests.class,
    SketchModelStickyTests.class,
    
    AllArchimateElementTypeTests.class,
    AllArchimateRelationshipTypeTests.class,
    
    // util
    ArchimateModelUtilsTests.class,
    ArchimateResourceFactoryTests.class,
    UUIDFactoryTests.class,
    RelationshipsMatrixTests.class,

    // viewpoints
    ViewpointTests.class,
    ViewpointManagerTests.class
})
@SuiteDisplayName("All Model Tests")
public class AllTests {
}