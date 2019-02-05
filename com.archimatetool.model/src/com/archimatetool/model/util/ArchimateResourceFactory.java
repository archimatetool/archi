/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;


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
        ArchimateResource resource = new ArchimateResource(uri);
        
        // Ensure we have ExtendedMetaData for both Saving and Loading
        ExtendedMetaData ext = new ConverterExtendedMetadata();

        resource.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, ext);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, ext);

        resource.getDefaultSaveOptions().put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        resource.getDefaultLoadOptions().put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        
        resource.getDefaultLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        resource.setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
        
        
        Map<String, Object> parserFeatures = new HashMap<String, Object>();
        // Don't allow DTD loading in case of XSS exploits
        parserFeatures.put("http://apache.org/xml/features/disallow-doctype-decl", Boolean.TRUE); //$NON-NLS-1$
        parserFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE); //$NON-NLS-1$
        parserFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.FALSE); //$NON-NLS-1$
        parserFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE); //$NON-NLS-1$
        resource.getDefaultLoadOptions().put(XMLResource.OPTION_PARSER_FEATURES, parserFeatures);
        
        // Not sure about this
        // resource.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

        // Don't set this as it prefixes a hash # to ID references
        // resource.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        // resource.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

        // Not sure about this
        // resource.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
        
        return resource;
    }

} //ArchimateResourceFactory
