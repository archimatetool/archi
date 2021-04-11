/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

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

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.model");

		// impl
        suite.addTest(ArchimateDiagramModelTests.suite());
        suite.addTest(ArchimateFactoryTests.suite());
		suite.addTest(ArchimateModelTests.suite());

		suite.addTest(DiagramModelArchimateConnectionTests.suite());
        suite.addTest(DiagramModelArchimateObjectTests.suite());
        suite.addTest(DiagramModelBendpointTests.suite());
        suite.addTest(DiagramModelConnectionTests.suite());
        suite.addTest(DiagramModelExtraTests.suite());
        suite.addTest(DiagramModelGroupTests.suite());
        suite.addTest(DiagramModelImageTests.suite());
        suite.addTest(DiagramModelNoteTests.suite());
        suite.addTest(DiagramModelReferenceTests.suite());
		
        suite.addTest(BoundsTests.suite());
        suite.addTest(FolderTests.suite());
        suite.addTest(MetadataTests.suite());
        suite.addTest(FeaturesEListTests.suite());
        suite.addTest(ProfileTests.suite());
        suite.addTest(PropertyTests.suite());
        
        suite.addTest(SketchModelTests.suite());
        suite.addTest(SketchModelActorTests.suite());
        suite.addTest(SketchModelStickyTests.suite());
		
        suite.addTest(AllArchimateElementTypeTests.suite());
        suite.addTest(AllArchimateRelationshipTypeTests.suite());
        
        // util
        suite.addTest(ArchimateModelUtilsTests.suite());
        suite.addTest(ArchimateResourceFactoryTests.suite());
        suite.addTest(UUIDFactoryTests.suite());
        suite.addTest(RelationshipsMatrixTests.suite());

        // viewpoints
        suite.addTest(ViewpointTests.suite());
        suite.addTest(ViewpointManagerTests.suite());

        return suite;
	}

}