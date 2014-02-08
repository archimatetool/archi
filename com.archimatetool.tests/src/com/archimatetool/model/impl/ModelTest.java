/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.archimatetool.TestSupport;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;

@SuppressWarnings("nls")
public abstract class ModelTest {

    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    protected File saveModel(IArchimateModel model) throws IOException {
        File file = TestSupport.getTempFile(".archimate");
        
        ResourceSet resourceSet = ArchimateResourceFactory.createResourceSet();
        Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
        resource.getContents().add(model);
        resource.save(null);
    
        return file;
    }

    protected IArchimateModel loadModel(File file) throws IOException {
        ResourceSet resourceSet = ArchimateResourceFactory.createResourceSet();
        Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
        resource.load(null);
        return (IArchimateModel)resource.getContents().get(0);
    }

}