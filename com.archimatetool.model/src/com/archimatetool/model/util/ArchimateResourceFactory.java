/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

import com.archimatetool.model.IArchimatePackage;


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
        XMLResource result = new ArchimateResource(uri);
        
        // Ensure we have ExtendedMetaData for both Saving and Loading
        ExtendedMetaData ext = new BasicExtendedMetaData() {
            /*
             * Backwards compatibility for the old "DiagramModel" type
             */
            @Override
            public EClassifier getType(EPackage ePackage, String name) {
                if(ePackage == IArchimatePackage.eINSTANCE && "DiagramModel".equals(name)) { //$NON-NLS-1$
                    return IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL;
                }
                return super.getType(ePackage, name);
            }
        };

        result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, ext);
        result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, ext);

        result.getDefaultSaveOptions().put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        result.getDefaultLoadOptions().put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        
        // Not sure about this
        // result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

        // Don't set this as it prefixes a hash # to ID references
        // result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        // result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

        // Not sure about this
        // result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
        
        return result;
    }

} //ArchimateResourceFactory
