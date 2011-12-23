/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;


/**
 * Image Factory for this application
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateImages {
    
    ImageFactory ImageFactory = new ImageFactory(ArchimateEditorPlugin.INSTANCE);

    String IMGPATH = "img/"; //$NON-NLS-1$
    
    String ARCHIMATE_IMGPATH = IMGPATH + "archimate/"; //$NON-NLS-1$
    
    String ICON_APP_16 = IMGPATH + "app-16.png"; //$NON-NLS-1$
    String ICON_APP_32 = IMGPATH + "app-32.png"; //$NON-NLS-1$
    String ICON_APP_48 = IMGPATH + "app-48.png"; //$NON-NLS-1$
    String ICON_APP_64 = IMGPATH + "app-64.png"; //$NON-NLS-1$
    String ICON_APP_128 = IMGPATH + "app-128.png"; //$NON-NLS-1$
    
    String BROWN_PAPER = IMGPATH + "br_paper.jpg";
    String DEFAULT_MODEL_THUMB = IMGPATH + "default_model_thumb.png";
    
    
    // ARCHIMATE IMAGES
    
    // Connections
    String ICON_ACESS_CONNECTION_16 = ARCHIMATE_IMGPATH + "access-16.png"; //$NON-NLS-1$
    String ICON_AGGREGATION_CONNECTION_16 = ARCHIMATE_IMGPATH + "aggregation-16.png"; //$NON-NLS-1$
    String ICON_ASSIGNMENT_CONNECTION_16 = ARCHIMATE_IMGPATH + "assignment-16.png"; //$NON-NLS-1$
    String ICON_ASSOCIATION_CONNECTION_16 = ARCHIMATE_IMGPATH + "association-16.png"; //$NON-NLS-1$
    String ICON_COMPOSITION_CONNECTION_16 = ARCHIMATE_IMGPATH + "composition-16.png"; //$NON-NLS-1$
    String ICON_FLOW_CONNECTION_16 = ARCHIMATE_IMGPATH + "flow-16.png"; //$NON-NLS-1$
    String ICON_REALISATION_CONNECTION_16 = ARCHIMATE_IMGPATH + "realisation-16.png"; //$NON-NLS-1$
    String ICON_SPECIALISATION_CONNECTION_16 = ARCHIMATE_IMGPATH + "specialisation-16.png"; //$NON-NLS-1$
    String ICON_TRIGGERING_CONNECTION_16 = ARCHIMATE_IMGPATH + "triggering-16.png"; //$NON-NLS-1$
    String ICON_USED_BY_CONNECTION_16 = ARCHIMATE_IMGPATH + "used-by-16.png"; //$NON-NLS-1$
    
    // Junctions
    String ICON_JUNCTION_16 = ARCHIMATE_IMGPATH + "junction-16.png"; //$NON-NLS-1$
    String ICON_JUNCTION_AND_16 = ARCHIMATE_IMGPATH + "junction-and-16.png"; //$NON-NLS-1$
    String ICON_JUNCTION_OR_16 = ARCHIMATE_IMGPATH + "junction-or-16.png"; //$NON-NLS-1$
    
    // Plain
    String ICON_ACTIVITY_16 = ARCHIMATE_IMGPATH + "activity-16.png"; //$NON-NLS-1$
    String ICON_ACTOR_16 = ARCHIMATE_IMGPATH + "actor-16.png"; //$NON-NLS-1$
    String ICON_COLLABORATION_16 = ARCHIMATE_IMGPATH + "collaboration-16.png"; //$NON-NLS-1$
    String ICON_COMMUNICATION_PATH_16 = ARCHIMATE_IMGPATH + "communication-path-16.png"; //$NON-NLS-1$
    String ICON_COMPONENT_16 = ARCHIMATE_IMGPATH + "component-16.png"; //$NON-NLS-1$
    String ICON_CONTRACT_16 = ARCHIMATE_IMGPATH + "contract-16.png"; //$NON-NLS-1$
    String ICON_DEVICE_16 = ARCHIMATE_IMGPATH + "device-16.png"; //$NON-NLS-1$
    String ICON_EVENT_16 = ARCHIMATE_IMGPATH + "event-16.png"; //$NON-NLS-1$
    String ICON_FUNCTION_16 = ARCHIMATE_IMGPATH + "function-16.png"; //$NON-NLS-1$
    String ICON_INTERACTION_16 = ARCHIMATE_IMGPATH + "interaction-16.png"; //$NON-NLS-1$
    String ICON_INTERFACE_16 = ARCHIMATE_IMGPATH + "interface-16.png"; //$NON-NLS-1$
    String ICON_INTERFACE_REQUIRED_16 = ARCHIMATE_IMGPATH + "interface-required-16.png"; //$NON-NLS-1$
    String ICON_INTERFACE_SYMMETRIC_16 = ARCHIMATE_IMGPATH + "interface-symmetric-16.png"; //$NON-NLS-1$
    String ICON_MEANING_16 = ARCHIMATE_IMGPATH + "meaning-16.png"; //$NON-NLS-1$
    String ICON_NETWORK_16 = ARCHIMATE_IMGPATH + "network-16.png"; //$NON-NLS-1$
    String ICON_NODE_16 = ARCHIMATE_IMGPATH + "node-16.png"; //$NON-NLS-1$
    String ICON_OBJECT_16 = ARCHIMATE_IMGPATH + "object-16.png"; //$NON-NLS-1$
    String ICON_PROCESS_16 = ARCHIMATE_IMGPATH + "process-16.png"; //$NON-NLS-1$
    String ICON_PRODUCT_16 = ARCHIMATE_IMGPATH + "product-16.png"; //$NON-NLS-1$
    String ICON_REPRESENTATION_16 = ARCHIMATE_IMGPATH + "representation-16.png"; //$NON-NLS-1$
    String ICON_ROLE_16 = ARCHIMATE_IMGPATH + "role-16.png"; //$NON-NLS-1$
    String ICON_SERVICE_16 = ARCHIMATE_IMGPATH + "service-16.png"; //$NON-NLS-1$
    String ICON_SYSTEM_SOFTWARE_16 = ARCHIMATE_IMGPATH + "system-software-16.png"; //$NON-NLS-1$
    String ICON_VALUE_16 = ARCHIMATE_IMGPATH + "value-16.png"; //$NON-NLS-1$

    // Business Coloured
    String ICON_BUSINESS_ACTIVITY_16 = ARCHIMATE_IMGPATH + "business-activity-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_ACTOR_16 = ARCHIMATE_IMGPATH + "business-actor-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_COLLABORATION_16 = ARCHIMATE_IMGPATH + "business-collaboration-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_CONTRACT_16 = ARCHIMATE_IMGPATH + "business-contract-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_EVENT_16 = ARCHIMATE_IMGPATH + "business-event-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_FUNCTION_16 = ARCHIMATE_IMGPATH + "business-function-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_INTERACTION_16 = ARCHIMATE_IMGPATH + "business-interaction-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_INTERFACE_16 = ARCHIMATE_IMGPATH + "business-interface-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_MEANING_16 = ARCHIMATE_IMGPATH + "business-meaning-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_OBJECT_16 = ARCHIMATE_IMGPATH + "business-object-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_PROCESS_16 = ARCHIMATE_IMGPATH + "business-process-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_PRODUCT_16 = ARCHIMATE_IMGPATH + "business-product-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_REPRESENTATION_16 = ARCHIMATE_IMGPATH + "business-representation-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_ROLE_16 = ARCHIMATE_IMGPATH + "business-role-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_SERVICE_16 = ARCHIMATE_IMGPATH + "business-service-16.png"; //$NON-NLS-1$
    String ICON_BUSINESS_VALUE_16 = ARCHIMATE_IMGPATH + "business-value-16.png"; //$NON-NLS-1$
    
    // Application Coloured
    String ICON_APPLICATION_COLLABORATION_16 = ARCHIMATE_IMGPATH + "application-collaboration-16.png"; //$NON-NLS-1$
    String ICON_APPLICATION_COMPONENT_16 = ARCHIMATE_IMGPATH + "application-component-16.png"; //$NON-NLS-1$
    String ICON_APPLICATION_FUNCTION_16 = ARCHIMATE_IMGPATH + "application-function-16.png"; //$NON-NLS-1$
    String ICON_APPLICATION_INTERACTION_16 = ARCHIMATE_IMGPATH + "application-interaction-16.png"; //$NON-NLS-1$
    String ICON_APPLICATION_INTERFACE_16 = ARCHIMATE_IMGPATH + "application-interface-16.png"; //$NON-NLS-1$
    String ICON_APPLICATION_DATA_OBJECT_16 = ARCHIMATE_IMGPATH + "application-data-object-16.png"; //$NON-NLS-1$
    String ICON_APPLICATION_SERVICE_16 = ARCHIMATE_IMGPATH + "application-service-16.png"; //$NON-NLS-1$
    
    // Technology Coloured
    String ICON_TECHNOLOGY_ARTIFACT_16 = ARCHIMATE_IMGPATH + "technology-artifact-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_COMMUNICATION_PATH_16 = ARCHIMATE_IMGPATH + "technology-communication-path-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_NETWORK_16 = ARCHIMATE_IMGPATH + "technology-network-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_INFRASTRUCTURE_INTERFACE_16 = ARCHIMATE_IMGPATH + "technology-infra-interface-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_INFRASTRUCTURE_SERVICE_16 = ARCHIMATE_IMGPATH + "technology-infra-service-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_NODE_16 = ARCHIMATE_IMGPATH + "technology-node-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_SYSTEM_SOFTWARE_16 = ARCHIMATE_IMGPATH + "technology-system-software-16.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_DEVICE_16 = ARCHIMATE_IMGPATH + "technology-device-16.png"; //$NON-NLS-1$
    
    // Other
    String ICON_ALIGN_TEXT_LEFT = IMGPATH + "alignleft.gif"; //$NON-NLS-1$
    String ICON_ALIGN_TEXT_CENTER = IMGPATH + "aligncenter.gif"; //$NON-NLS-1$
    String ICON_ALIGN_TEXT_RIGHT = IMGPATH + "alignright.gif"; //$NON-NLS-1$
    String ICON_ASPECT_RATIO = IMGPATH + "aspect-ratio.png"; //$NON-NLS-1$
    String ICON_CANCEL_SEARCH_16 = IMGPATH + "cancelsearch-16.png"; //$NON-NLS-1$
    String ICON_COG = IMGPATH + "cog.png"; //$NON-NLS-1$
    String ICON_DIAGRAM_16 = IMGPATH + "diagram-16.png"; //$NON-NLS-1$
    String ICON_DEFAULT_SIZE = IMGPATH + "default-size.png"; //$NON-NLS-1$
    String ICON_DERIVED_16 = IMGPATH + "derived-16.png"; //$NON-NLS-1$
    String ICON_DERIVED_SM_16 = IMGPATH + "derived-sm-16.png"; //$NON-NLS-1$
    String ICON_FILTER_16 = IMGPATH + "filter.gif"; //$NON-NLS-1$
    String ICON_FORMAT_PAINTER_16 = IMGPATH + "formatpainter-16.png"; //$NON-NLS-1$
    String ICON_FORMAT_PAINTER_GREY_16 = IMGPATH + "formatpainter-grey-16.png"; //$NON-NLS-1$
    String ICON_GROUP_16 = IMGPATH + "group-16.png"; //$NON-NLS-1$
    String ICON_LINKED_16 = IMGPATH + "linked.gif"; //$NON-NLS-1$
    String ICON_LANDSCAPE_16 = IMGPATH + "landscape-16.png"; //$NON-NLS-1$
    String ICON_LOCK_16 = IMGPATH + "lock-16.png"; //$NON-NLS-1$
    String ICON_MAGIC_CONNECTION_16 = IMGPATH + "magic_connection.gif"; //$NON-NLS-1$
    String ICON_MINUS = IMGPATH + "minus.png"; //$NON-NLS-1$
    String ICON_MODELS_16 = IMGPATH + "models.gif"; //$NON-NLS-1$
    String ICON_MUTIPLE = IMGPATH + "mutiple.png"; //$NON-NLS-1$
    String ICON_NAVIGATOR_16 = IMGPATH + "navigator-16.png"; //$NON-NLS-1$
    String ICON_NAVIGATOR_DOWNWARD_16 = IMGPATH + "nav-downward.gif"; //$NON-NLS-1$
    String ICON_NAVIGATOR_UPWARD_16 = IMGPATH + "nav-upward.gif"; //$NON-NLS-1$
    String ICON_NEW_FILE_16 = IMGPATH + "newfile.gif"; //$NON-NLS-1$
    String ICON_NEW_OVERLAY_16 = IMGPATH + "new_overlay.gif"; //$NON-NLS-1$
    String ICON_NOTE_16 = IMGPATH + "note-16.gif"; //$NON-NLS-1$
    String ICON_OPEN_16 = IMGPATH + "open.gif"; //$NON-NLS-1$
    String ICON_PIN_16 = IMGPATH + "pin.gif"; //$NON-NLS-1$
    String ICON_PLUS = IMGPATH + "plus.png"; //$NON-NLS-1$
    String ICON_PROPERTIES_16 = IMGPATH + "properties.gif"; //$NON-NLS-1$
    String ICON_SEARCH_16 = IMGPATH + "search-16.png"; //$NON-NLS-1$
    String ICON_SKETCH_16 = IMGPATH + "sketch-16.png"; //$NON-NLS-1$
    String ICON_SMALL_X = IMGPATH + "smallx.png"; //$NON-NLS-1$
    String ICON_SORT_16 = IMGPATH + "alphab_sort_co.gif"; //$NON-NLS-1$
    String ICON_STICKY_16 = IMGPATH + "sticky-16.png"; //$NON-NLS-1$
    String ICON_TRASH_16 = IMGPATH + "trash.gif"; //$NON-NLS-1$
    String ICON_UNLOCK_16 = IMGPATH + "unlock-16.png"; //$NON-NLS-1$
    String ICON_VIEWPOINTS_16 = IMGPATH + "viewpoints-16.png"; //$NON-NLS-1$
    String ICON_VIEWPOINT_BUSINESS_16 = IMGPATH + "vp-business-16.png"; //$NON-NLS-1$
    String ICON_VIEWPOINT_APPLICATION_16 = IMGPATH + "vp-application-16.png"; //$NON-NLS-1$
    String ICON_VIEWPOINT_TECHNOLOGY_16 = IMGPATH + "vp-technology-16.png"; //$NON-NLS-1$
    
    String ICON_CONNECTION_PLAIN_16 = IMGPATH + "connection-plain-16.png"; //$NON-NLS-1$
    String ICON_CONNECTION_ARROW_16 = IMGPATH + "connection-arrow-16.png"; //$NON-NLS-1$
    String ICON_CONNECTION_DASHED_ARROW_16 = IMGPATH + "connection-dashed-arrow-16.png"; //$NON-NLS-1$
    String ICON_CONNECTION_DOTTED_ARROW_16 = IMGPATH + "connection-dotted-arrow-16.png"; //$NON-NLS-1$
    
    String LINE_SOLID = IMGPATH + "line-solid.png"; //$NON-NLS-1$
    String LINE_DASHED = IMGPATH + "line-dashed.png"; //$NON-NLS-1$
    String LINE_DOTTED = IMGPATH + "line-dotted.png"; //$NON-NLS-1$
    
    String ARROW_SOURCE_FILL = IMGPATH + "arrow-source-fill.png"; //$NON-NLS-1$
    String ARROW_TARGET_FILL = IMGPATH + "arrow-target-fill.png"; //$NON-NLS-1$
    String ARROW_SOURCE_HOLLOW = IMGPATH + "arrow-source-hollow.png"; //$NON-NLS-1$
    String ARROW_TARGET_HOLLOW = IMGPATH + "arrow-target-hollow.png"; //$NON-NLS-1$
    String ARROW_SOURCE_LINE = IMGPATH + "arrow-source-line.png"; //$NON-NLS-1$
    String ARROW_TARGET_LINE = IMGPATH + "arrow-target-line.png"; //$NON-NLS-1$

    String CURSOR_IMG_FORMAT_PAINTER = IMGPATH + "formatpainter-cursor.gif"; //$NON-NLS-1$
    String CURSOR_IMG_FORMAT_PAINTER_GREY = IMGPATH + "formatpainter-grey-cursor.gif"; //$NON-NLS-1$
    
    String CURSOR_IMG_MAGIC_CONNECTOR = IMGPATH + "magic-connector-cursor.gif"; //$NON-NLS-1$
    
    // Figures
    String FIGURES_IMGPATH = IMGPATH + "figures/"; //$NON-NLS-1$
    
    String FIGURE_BUSINESS_INTERFACE1 = FIGURES_IMGPATH + "bi1.png";
    String FIGURE_BUSINESS_INTERFACE2 = FIGURES_IMGPATH + "bi2.png";
    String FIGURE_APPLICATION_COMPONENT1 = FIGURES_IMGPATH + "ac1.png";
    String FIGURE_APPLICATION_COMPONENT2 = FIGURES_IMGPATH + "ac2.png";
    String FIGURE_APPLICATION_INTERFACE1 = FIGURES_IMGPATH + "ai1.png";
    String FIGURE_APPLICATION_INTERFACE2 = FIGURES_IMGPATH + "ai2.png";
    String FIGURE_TECHNOLOGY_DEVICE1 = FIGURES_IMGPATH + "td1.png";
    String FIGURE_TECHNOLOGY_DEVICE2 = FIGURES_IMGPATH + "td2.png";
    String FIGURE_TECHNOLOGY_NODE1 = FIGURES_IMGPATH + "tn1.png";
    String FIGURE_TECHNOLOGY_NODE2 = FIGURES_IMGPATH + "tn2.png";
    String FIGURE_TECHNOLOGY_INTERFACE1 = FIGURES_IMGPATH + "ti1.png";
    String FIGURE_TECHNOLOGY_INTERFACE2 = FIGURES_IMGPATH + "ti2.png";

}
