/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import org.jdom2.Namespace;


/**
 * Global Element/Attribute/Namespace
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface IXMLExchangeGlobals {

    String FILE_EXTENSION = ".xml";
    String FILE_EXTENSION_WILDCARD = "*.xml";
    
    String ARCHIMATE_NAMESPACE_PREFIX = "archimate";
    
    Namespace ARCHIMATE3_NAMESPACE = Namespace.getNamespace("http://www.opengroup.org/xsd/archimate/3.0/");
    Namespace ARCHIMATE3_NAMESPACE_EMBEDDED = Namespace.getNamespace(ARCHIMATE_NAMESPACE_PREFIX, ARCHIMATE3_NAMESPACE.getURI());
    
    String ARCHIMATE3_SCHEMA_LOCATION = "http://www.opengroup.org/xsd/archimate/3.1/archimate3_Diagram.xsd";
    //String ARCHIMATE3_SCHEMA_LOCATION = "archimate3_Diagram.xsd"; // For local testing
    
    Namespace XSI_NAMESPACE = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    
    Namespace DC_NAMESPACE = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
    // The actual location is https://www.dublincore.org/schemas/xmls/qdc/2008/02/11/dc.xsd but we use this one in case it changes
    String DC_SCHEMA_LOCATION = "http://www.opengroup.org/xsd/archimate/3.1/dc.xsd";
    
    
    String ELEMENT_MODEL = "model";
    String ELEMENT_LABEL = "label";
    String ELEMENT_NAME = "name";
    String ELEMENT_DOCUMENTATION = "documentation";
    String ELEMENT_ELEMENT = "element";
    String ELEMENT_ELEMENTS = "elements";
    String ELEMENT_RELATIONSHIPS = "relationships";
    String ELEMENT_RELATIONSHIP = "relationship";
    String ELEMENT_PROPERTYDEFINITIONS = "propertyDefinitions";
    String ELEMENT_PROPERTYDEFINITION = "propertyDefinition";
    String ELEMENT_PROPERTIES = "properties";
    String ELEMENT_PROPERTY = "property";
    String ELEMENT_VALUE = "value";
    String ELEMENT_ORGANIZATIONS = "organizations";
    String ELEMENT_ITEM = "item";
    String ELEMENT_VIEWS = "views";
    String ELEMENT_VIEW = "view";
    String ELEMENT_DIAGRAMS = "diagrams";
    String ELEMENT_DIAGRAM = "diagram";
    String ELEMENT_STYLE = "style";
    String ELEMENT_NODE = "node";
    String ELEMENT_CONNECTION = "connection";
    String ELEMENT_FILLCOLOR = "fillColor";
    String ELEMENT_BENDPOINT = "bendpoint";
    String ELEMENT_LINECOLOR = "lineColor";
    String ELEMENT_FONT = "font";
    String ELEMENT_FONTCOLOR = "color";
    String ELEMENT_VIEWREF = "viewRef";
    
    String ELEMENT_METADATA = "metadata";
    String ELEMENT_SCHEMA = "schema";
    String ELEMENT_SCHEMAVERSION = "schemaversion";
    
    String ATTRIBUTE_LABEL = "label";
    String ATTRIBUTE_NAME = "name";
    String ATTRIBUTE_IDENTIFIER = "identifier";
    String ATTRIBUTE_IDENTIFIERREF = "identifierRef";
    String ATTRIBUTE_PROPERTY_IDENTIFIERREF = "propertyDefinitionRef";
    String ATTRIBUTE_TYPE = "type";
    String ATTRIBUTE_SOURCE = "source";
    String ATTRIBUTE_TARGET = "target";
    String ATTRIBUTE_INFLUENCE_MODIFIER = "modifier";
    String ATTRIBUTE_ACCESS_TYPE = "accessType";
    String ATTRIBUTE_ASSOCIATION_DIRECTED = "isDirected";
    String ATTRIBUTE_LANG = "lang";
    String ATTRIBUTE_VIEWPOINT = "viewpoint";
    String ATTRIBUTE_ELEMENTREF = "elementRef";
    String ATTRIBUTE_RELATIONSHIPREF = "relationshipRef";
    String ATTRIBUTE_REF = "ref";
    String ATTRIBUTE_ELEMENT_TYPE = "Element";
    String ATTRIBUTE_RELATIONSHIP_TYPE = "Relationship";
    String ATTRIBUTE_LABEL_TYPE = "Label";
    String ATTRIBUTE_LINE_TYPE = "Line";
    String ATTRIBUTE_CONTAINER_TYPE = "Container";
    String ATTRIBUTE_DIAGRAM_TYPE = "Diagram";
    
    String ATTRIBUTE_X = "x";
    String ATTRIBUTE_Y = "y";
    String ATTRIBUTE_WIDTH = "w";
    String ATTRIBUTE_HEIGHT = "h";
    String ATTRIBUTE_R = "r";
    String ATTRIBUTE_G = "g";
    String ATTRIBUTE_B = "b";
    String ATTRIBUTE_A = "a";
    String ATTRIBUTE_FONTNAME = "name";
    String ATTRIBUTE_FONTSIZE = "size";
    String ATTRIBUTE_FONTSTYLE = "style";
    String ATTRIBUTE_LINEWIDTH = "lineWidth";
    
    String ACCESS_TYPE_ACCESS = "Access";
    String ACCESS_TYPE_WRITE = "Write";
    String ACCESS_TYPE_READ = "Read";
    String ACCESS_TYPE_READ_WRITE = "ReadWrite";
}
