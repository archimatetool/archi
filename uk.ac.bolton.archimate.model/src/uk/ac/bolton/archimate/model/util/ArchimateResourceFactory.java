/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

import uk.ac.bolton.archimate.model.IArchimatePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource Factory</b> associated with the package.
 * <!-- end-user-doc -->
 * @see uk.ac.bolton.archimate.model.util.ArchimateResource
 * @generated
 */
public class ArchimateResourceFactory extends ResourceFactoryImpl {
    
    private Map<Object, Object> options;
    
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
    public static Resource createResource(File file) {
        ResourceSet resourceSet = createResourceSet();
        
        // This will return an ArchimateResource as registered in plugin.xml
        Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
        return resource;
    }

    /**
     * @return a ResourceSet that allows saving and loading files with any type of extension
     */
    public static ResourceSet createResourceSet() {
        ResourceSet resourceSet = new ResourceSetImpl();
        // Register the * extension on the ResourceSet to over-ride the ECore global one
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ArchimateResourceFactory());
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
        ArchimateResource result = new ArchimateResource(uri);
        result.getDefaultLoadOptions().putAll(getOptions());
        result.getDefaultSaveOptions().putAll(getOptions());
        return result;
    }
    
    /**
     * Add customisation to serialsiation
     * See Chapter 15.3.5 of the EMF book, 2nd edition
     * @return
     */
    private Map<Object, Object> getOptions() {
        if(options == null) {
            options = new HashMap<Object, Object>();
            
            // UTF-8
            options.put(XMLResource.OPTION_ENCODING, "UTF-8");
            
            // A Map to map Ecore features and classes to elememt names and types
            ExtendedMetaData ext = new BasicExtendedMetaData(ExtendedMetaData.ANNOTATION_URI, 
                    EPackage.Registry.INSTANCE, new HashMap<EModelElement, EAnnotation>()) {
                
                @Override
                public EClassifier getType(EPackage ePackage, String name) {
                    /*
                     * Backwards compatibility for the old "DiagramModel" type
                     */
                    if("DiagramModel".equals(name)) {
                        return IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL;
                    }
                    return super.getType(ePackage, name);
                }
            };

            // Prevents the root-level element from being namespace qualified. 
            //ext.setQualified(IArchimatePackage.eINSTANCE, false);

            // "ArchimateModel" becomes "model"
            ext.setName(IArchimatePackage.Literals.ARCHIMATE_MODEL, "model");
            
            // The "purpose" attribute becomes an element
            ext.setFeatureKind(IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE, ExtendedMetaData.ELEMENT_FEATURE);

            // The "folders" list element becomes "folder"
            ext.setFeatureKind(IArchimatePackage.Literals.FOLDER_CONTAINER__FOLDERS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.FOLDER_CONTAINER__FOLDERS, "folder");

            // The "elements" list element becomes "element"
            ext.setFeatureKind(IArchimatePackage.Literals.FOLDER__ELEMENTS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.FOLDER__ELEMENTS, "element");
            
            // The "children" list element becomes "child"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_CONTAINER__CHILDREN, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_CONTAINER__CHILDREN, "child");

            // The "properties" list element becomes "property"
            ext.setFeatureKind(IArchimatePackage.Literals.PROPERTIES__PROPERTIES, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.PROPERTIES__PROPERTIES, "property");
            
            // The "sourceConnections" list element becomes "sourceConnection"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS, "sourceConnection");
            
            // The "bendpoints" list element becomes "bendpoint"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__BENDPOINTS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__BENDPOINTS, "bendpoint");

            // The "DiagramModelArchimateConnection" type becomes "Connection"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_CONNECTION, "Connection");

            // The "DiagramModelArchimateObject" type becomes "DiagramObject"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT, "DiagramObject");

            // The "documentation" attribute becomes an element
            ext.setFeatureKind(IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, ExtendedMetaData.ELEMENT_FEATURE);
            
            // The "DiagramModelNote" type becomes "Note"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_NOTE, "Note");

            // The "DiagramModelGroup" type becomes "Group"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_GROUP, "Group");

            // The TextContent "content" attribute becomes an element
            ext.setFeatureKind(IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, ExtendedMetaData.ELEMENT_FEATURE); 
            
            // The DiagramModelReference "referencedModel" becomes "model"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL, ExtendedMetaData.ATTRIBUTE_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL, "model");
            
            /*
             * Alternative method, but no good for saving as the element will also be saved as "DiagramModel"
             * DiagramModel is now abstract so:
             * DiagramModel becomes "AbstractDiagramModel" and
             * ArchimateDiagramModel is now "DiagramModel" for backwards compatibility
             */
            //ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL, "AbstractDiagramModel");
            //ext.setName(IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL, "DiagramModel");
            
            options.put(XMLResource.OPTION_EXTENDED_META_DATA, ext);
        }

        return options;
    }


} //ArchimateResourceFactory
