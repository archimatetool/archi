/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * @see com.archimatetool.model.util.ArchimateResourceFactory
 * @generated
 */
@SuppressWarnings("nls")
public class ArchimateResource extends XMLResourceImpl {
    /**
     * Creates an instance of the resource.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param uri the URI of the new resource.
     * @generated
     */
    public ArchimateResource(URI uri) {
        super(uri);
    }
    
    @Override
    protected void init() {
        super.init();
        
        // This is the key thing! If not set loading is slow.
        setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
        
        Map<Object, Object> loadOptions = getDefaultLoadOptions();
        Map<Object, Object> saveOptions = getDefaultSaveOptions();
        
        // Ensure we have ExtendedMetaData for both Loading and Saving
        loadOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, new ConverterExtendedMetadata());
        saveOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, new BasicExtendedMetaData());

        loadOptions.put(XMLResource.OPTION_ENCODING, "UTF-8");
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8");
        
        loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        
        // Don't allow loading an unregistered URI in case of exploits
        loadOptions.put(XMLResource.OPTION_USE_PACKAGE_NS_URI_AS_LOCATION, false);
        
        // Don't allow DTD loading in case of XSS exploits
        Map<String, Object> parserFeatures = new HashMap<>();
        parserFeatures.put("http://apache.org/xml/features/disallow-doctype-decl", Boolean.TRUE);
        parserFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
        parserFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.FALSE);
        parserFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE);
        loadOptions.put(XMLResource.OPTION_PARSER_FEATURES, parserFeatures);
        
        // Not sure about this
        // saveOptions.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

        // Don't set this as it prefixes a hash # to ID references
        // loadOptions.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        // saveOptions.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

        // Not sure about this
        // loadOptions.put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
    }
    
    @Override
    public void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
        // Catch UncheckedIOException exception which can be thrown in ConverterExtendedMetadata and re-throw
        try {
            super.doLoad(inputStream, options);
        }
        catch(UncheckedIOException ex) {
            throw ex.getCause();
        }
    }

} //ArchimateResource
