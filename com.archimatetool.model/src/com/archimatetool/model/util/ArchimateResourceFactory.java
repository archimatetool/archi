/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


/**
 * <!-- begin-user-doc -->
 * The <b>Resource Factory</b> associated with the package.
 * <!-- end-user-doc -->
 * @see com.archimatetool.model.util.ArchimateResource
 * @generated
 */
public class ArchimateResourceFactory extends ResourceFactoryImpl {
    
    /**
     * Creates an instance of the resource factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArchimateResourceFactory() {
        super();
    }
    
    /**
     * @return a Resource that allows saving and loading files with any type of file extension
     *          as registered in plugin.xml
     */
    public static Resource createNewResource(File file) {
        return createNewResource(URI.createFileURI(file.getAbsolutePath()));
    }

    /**
     * @return a Resource that allows saving and loading files with any type of file extension
     *          as registered in plugin.xml
     */
    public static Resource createNewResource(URI uri) {
        // This will return an ArchimateResource as registered in plugin.xml
        ResourceSet resourceSet = createResourceSet();
        return resourceSet.createResource(uri);
    }

    /**
     * @return a ResourceSet that allows saving and loading files with any type of extension
     */
    public static ResourceSet createResourceSet() {
        ResourceSet resourceSet = new ResourceSetImpl();
        
        /*
         * Register the * extension on the ResourceSet to over-ride the ECore global one 
         * This is needed to create an ArchimateModel object from any file (thus pattern "*") without relying on its extension.
         * Without this code it is impossible to load a model from file without extension (error "Class 'model' is not found or is abstract").
         */
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ArchimateResourceFactory());  //$NON-NLS-1$
        
        return resourceSet;
    }

    /**
     * Creates an instance of the resource.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Resource createResource(URI uri) {
        return new ArchimateResource(uri);
    }

} //ArchimateResourceFactory
