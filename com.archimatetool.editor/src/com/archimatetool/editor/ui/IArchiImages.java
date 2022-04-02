/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import com.archimatetool.editor.ArchiPlugin;


/**
 * Image Factory for this application
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface IArchiImages {
    
    ImageFactory ImageFactory = new ImageFactory(ArchiPlugin.INSTANCE);

    String IMGPATH = "img/";
    
    String ARCHIMATE_IMGPATH = IMGPATH + "archimate/";
    
    String ICON_APP = IMGPATH + "app-16.png";
    String ICON_APP_32 = IMGPATH + "app-32.png";
    String ICON_APP_48 = IMGPATH + "app-48.png";
    String ICON_APP_64 = IMGPATH + "app-64.png";
    String ICON_APP_128 = IMGPATH + "app-128.png";
    
    String BROWN_PAPER_BACKGROUND = IMGPATH + "br_paper.jpg";
    String CORK_BACKGROUND = IMGPATH + "cork.jpg";
    String DEFAULT_MODEL_THUMB = IMGPATH + "default_model_thumb.png";
    
    // ECLIPSE IMAGES
    String ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON = IMGPATH + "prop_ps.png";
    String ECLIPSE_IMAGE_OUTLINE_VIEW_ICON = IMGPATH + "outline.png";
    String ECLIPSE_IMAGE_NEW_WIZARD = IMGPATH + "new_wiz.png";
    String ECLIPSE_IMAGE_IMPORT_PREF_WIZARD = IMGPATH + "importpref_wiz.png"; 
    String ECLIPSE_IMAGE_EXPORT_DIR_WIZARD = IMGPATH + "exportdir_wiz.png";
    String ECLIPSE_IMAGE_FILE = IMGPATH + "file_obj.png";
    String ECLIPSE_IMAGE_FOLDER = IMGPATH + "fldr_obj.png";
    
    String ICON_FOLDER_DEFAULT = IMGPATH + "folder-default.png";
    
    String MENU_ARROW = IMGPATH + "menu-arrow.png";
    
    String ZOOM_IN = IMGPATH + "zoomin.png";
    String ZOOM_OUT = IMGPATH + "zoomout.png";
    String ZOOM_NORMAL = IMGPATH + "zoomnormal.png";
    
    // Plain
    String ICON_ACTOR = ARCHIMATE_IMGPATH + "actor.png";

    // Elements
    String ICON_APPLICATION_COLLABORATION = ARCHIMATE_IMGPATH + "application-collaboration.png";
    String ICON_APPLICATION_COMPONENT = ARCHIMATE_IMGPATH + "application-component.png";
    String ICON_APPLICATION_EVENT = ARCHIMATE_IMGPATH + "application-event.png";
    String ICON_APPLICATION_FUNCTION = ARCHIMATE_IMGPATH + "application-function.png";
    String ICON_APPLICATION_INTERACTION = ARCHIMATE_IMGPATH + "application-interaction.png";
    String ICON_APPLICATION_INTERFACE = ARCHIMATE_IMGPATH + "application-interface.png";
    String ICON_APPLICATION_PROCESS = ARCHIMATE_IMGPATH + "application-process.png";
    String ICON_APPLICATION_SERVICE = ARCHIMATE_IMGPATH + "application-service.png";
    String ICON_ARTIFACT = ARCHIMATE_IMGPATH + "artifact.png";
    String ICON_ASSESSMENT = ARCHIMATE_IMGPATH + "assessment.png";
    String ICON_BUSINESS_ACTOR = ARCHIMATE_IMGPATH + "business-actor.png";
    String ICON_BUSINESS_COLLABORATION = ARCHIMATE_IMGPATH + "business-collaboration.png";
    String ICON_BUSINESS_EVENT = ARCHIMATE_IMGPATH + "business-event.png";
    String ICON_BUSINESS_FUNCTION = ARCHIMATE_IMGPATH + "business-function.png";
    String ICON_BUSINESS_INTERACTION = ARCHIMATE_IMGPATH + "business-interaction.png";
    String ICON_BUSINESS_INTERFACE = ARCHIMATE_IMGPATH + "business-interface.png";
    String ICON_BUSINESS_OBJECT = ARCHIMATE_IMGPATH + "business-object.png";
    String ICON_BUSINESS_PROCESS = ARCHIMATE_IMGPATH + "business-process.png";
    String ICON_BUSINESS_ROLE = ARCHIMATE_IMGPATH + "business-role.png";
    String ICON_BUSINESS_SERVICE = ARCHIMATE_IMGPATH + "business-service.png";
    String ICON_COMMUNICATION_NETWORK = ARCHIMATE_IMGPATH + "communication-network.png";
    String ICON_CAPABILITY = ARCHIMATE_IMGPATH + "capability.png";
    String ICON_CONSTRAINT = ARCHIMATE_IMGPATH + "constraint.png";
    String ICON_CONTRACT = ARCHIMATE_IMGPATH + "contract.png";
    String ICON_COURSE_OF_ACTION = ARCHIMATE_IMGPATH + "course-of-action.png";
    String ICON_DATA_OBJECT = ARCHIMATE_IMGPATH + "data-object.png";
    String ICON_DELIVERABLE = ARCHIMATE_IMGPATH + "deliverable.png";
    String ICON_DEVICE = ARCHIMATE_IMGPATH + "device.png";
    String ICON_DISTRIBUTION_NETWORK = ARCHIMATE_IMGPATH + "distribution-network.png";
    String ICON_DRIVER = ARCHIMATE_IMGPATH + "driver.png";
    String ICON_EQUIPMENT = ARCHIMATE_IMGPATH + "equipment.png";
    String ICON_FACILITY = ARCHIMATE_IMGPATH + "facility.png";
    String ICON_GAP = ARCHIMATE_IMGPATH + "gap.png";
    String ICON_GOAL = ARCHIMATE_IMGPATH + "goal.png";
    String ICON_GROUPING = ARCHIMATE_IMGPATH + "grouping.png";
    String ICON_IMPLEMENTATION_EVENT = ARCHIMATE_IMGPATH + "implementation-event.png";
    String ICON_LOCATION = ARCHIMATE_IMGPATH + "location.png";
    String ICON_MATERIAL = ARCHIMATE_IMGPATH + "material.png";
    String ICON_MEANING = ARCHIMATE_IMGPATH + "meaning.png";
    String ICON_NODE = ARCHIMATE_IMGPATH + "node.png";
    String ICON_OUTCOME = ARCHIMATE_IMGPATH + "outcome.png";
    String ICON_PATH = ARCHIMATE_IMGPATH + "path.png";
    String ICON_PLATEAU = ARCHIMATE_IMGPATH + "plateau.png";
    String ICON_PRINCIPLE = ARCHIMATE_IMGPATH + "principle.png";
    String ICON_PRODUCT = ARCHIMATE_IMGPATH + "product.png";
    String ICON_REPRESENTATION = ARCHIMATE_IMGPATH + "representation.png";
    String ICON_RESOURCE = ARCHIMATE_IMGPATH + "resource.png";
    String ICON_REQUIREMENT = ARCHIMATE_IMGPATH + "requirement.png";
    String ICON_STAKEHOLDER = ARCHIMATE_IMGPATH + "stakeholder.png";
    String ICON_SYSTEM_SOFTWARE = ARCHIMATE_IMGPATH + "system-software.png";
    String ICON_TECHNOLOGY_COLLABORATION = ARCHIMATE_IMGPATH + "technology-collaboration.png";
    String ICON_TECHNOLOGY_EVENT = ARCHIMATE_IMGPATH + "technology-event.png";
    String ICON_TECHNOLOGY_FUNCTION = ARCHIMATE_IMGPATH + "technology-function.png";
    String ICON_TECHNOLOGY_INTERACTION = ARCHIMATE_IMGPATH + "technology-interaction.png";
    String ICON_TECHNOLOGY_INTERFACE = ARCHIMATE_IMGPATH + "technology-interface.png";
    String ICON_TECHNOLOGY_PROCESS = ARCHIMATE_IMGPATH + "technology-process.png";
    String ICON_TECHNOLOGY_SERVICE = ARCHIMATE_IMGPATH + "technology-service.png";
    String ICON_VALUE = ARCHIMATE_IMGPATH + "value.png";
    String ICON_VALUE_STREAM = ARCHIMATE_IMGPATH + "value-stream.png";
    String ICON_WORKPACKAGE = ARCHIMATE_IMGPATH + "workpackage.png";
    
    // Relations
    String ICON_ACESS_RELATION = ARCHIMATE_IMGPATH + "access.png";
    String ICON_AGGREGATION_RELATION = ARCHIMATE_IMGPATH + "aggregation.png";
    String ICON_ASSIGNMENT_RELATION = ARCHIMATE_IMGPATH + "assignment.png";
    String ICON_ASSOCIATION_RELATION = ARCHIMATE_IMGPATH + "association.png";
    String ICON_COMPOSITION_RELATION = ARCHIMATE_IMGPATH + "composition.png";
    String ICON_FLOW_RELATION = ARCHIMATE_IMGPATH + "flow.png";
    String ICON_INFLUENCE_RELATION = ARCHIMATE_IMGPATH + "influence.png";
    String ICON_REALIZATION_RELATION = ARCHIMATE_IMGPATH + "realization.png";
    String ICON_SPECIALIZATION_RELATION = ARCHIMATE_IMGPATH + "specialization.png";
    String ICON_TRIGGERING_RELATION = ARCHIMATE_IMGPATH + "triggering.png";
    String ICON_SERVING_RELATION = ARCHIMATE_IMGPATH + "serving.png";
    
    // Junctions
    String ICON_AND_JUNCTION = ARCHIMATE_IMGPATH + "and-junction.png";
    String ICON_OR_JUNCTION = ARCHIMATE_IMGPATH + "or-junction.png";
    

    
    // Other
    String ICON_ALIGN_TEXT_LEFT = IMGPATH + "alignleft.gif";
    String ICON_ALIGN_TEXT_CENTER = IMGPATH + "aligncenter.gif";
    String ICON_ALIGN_TEXT_RIGHT = IMGPATH + "alignright.gif";

    String ICON_ALIGN_TEXT_TOP = IMGPATH + "aligntop.png";
    String ICON_ALIGN_TEXT_MIDDLE = IMGPATH + "alignmiddle.png";
    String ICON_ALIGN_TEXT_BOTTOM = IMGPATH + "alignbottom.png";

    
    String ICON_ASPECT_RATIO = IMGPATH + "aspect-ratio.png";
    String ICON_BROWSER = IMGPATH + "browser.png";
    String ICON_CANCEL_SEARCH = IMGPATH + "cancelsearch.png";
    String ICON_COG = IMGPATH + "cog.png";
    String ICON_COLLAPSEALL = IMGPATH + "collapseall.png";
    String ICON_DIAGRAM = IMGPATH + "diagram.png";
    String ICON_DEFAULT_SIZE = IMGPATH + "default-size.png";
    String ICON_DERIVED = IMGPATH + "derived.png";
    String ICON_DERIVED_SM = IMGPATH + "derived-sm.png";
    String ICON_EXPANDALL = IMGPATH + "expandall.png";
    String ICON_FILTER = IMGPATH + "filter.png";
    String ICON_FONT = IMGPATH + "font.png";
    String ICON_FORMAT_PAINTER = IMGPATH + "formatpainter.png";
    String ICON_FORMAT_PAINTER_GREY = IMGPATH + "formatpainter-grey.png";
    String ICON_GROUP = IMGPATH + "group.png";
    String ICON_LINKED = IMGPATH + "linked.png";
    String ICON_LANDSCAPE = IMGPATH + "landscape.png";
    String ICON_LOCK = IMGPATH + "lockedstate.png";
    String ICON_MAGIC_CONNECTION = IMGPATH + "magic_connection.png";
    String ICON_MINUS = IMGPATH + "minus.png";
    String ICON_MODELS = IMGPATH + "models.png";
    String ICON_MUTIPLE = IMGPATH + "mutiple.png";
    String ICON_NAVIGATOR = IMGPATH + "navigator.png";
    String ICON_NAVIGATOR_DOWNWARD = IMGPATH + "nav-downward.png";
    String ICON_NAVIGATOR_UPWARD = IMGPATH + "nav-upward.png";
    String ICON_NEW_FILE = IMGPATH + "newfile_wiz.png";
    String ICON_NOTE = IMGPATH + "note.png";
    String ICON_OPEN = IMGPATH + "open.png";
    String ICON_PIN = IMGPATH + "pin.png";
    String ICON_PLUS = IMGPATH + "plus.png";
    String ICON_SEARCH = IMGPATH + "search.png";
    String ICON_SEARCH_LIGHT = IMGPATH + "search_light.png";
    String ICON_SKETCH = IMGPATH + "sketch.png";
    String ICON_SMALL_X = IMGPATH + "smallx.png";
    String ICON_SORT = IMGPATH + "alphab_sort_co.png";
    String ICON_STICKY = IMGPATH + "sticky.png";
    String ICON_TRASH = IMGPATH + "trash.png";
    String ICON_UNLOCK = IMGPATH + "unlockedstate.png";
    
    String ICON_CONNECTION_PLAIN = IMGPATH + "connection-plain.png";
    String ICON_CONNECTION_ARROW = IMGPATH + "connection-arrow.png";
    String ICON_CONNECTION_DASHED_ARROW = IMGPATH + "connection-dashed-arrow.png";
    String ICON_CONNECTION_DOTTED_ARROW = IMGPATH + "connection-dotted-arrow.png";
    
    String LINE_SOLID = IMGPATH + "line-solid.png";
    String LINE_DASHED = IMGPATH + "line-dashed.png";
    String LINE_DOTTED = IMGPATH + "line-dotted.png";
    
    String ARROW_SOURCE_FILL = IMGPATH + "arrow-source-fill.png";
    String ARROW_TARGET_FILL = IMGPATH + "arrow-target-fill.png";
    String ARROW_SOURCE_HOLLOW = IMGPATH + "arrow-source-hollow.png";
    String ARROW_TARGET_HOLLOW = IMGPATH + "arrow-target-hollow.png";
    String ARROW_SOURCE_LINE = IMGPATH + "arrow-source-line.png";
    String ARROW_TARGET_LINE = IMGPATH + "arrow-target-line.png";

    // Cursors
    String CURSOR_IMG_MAGIC_CONNECTOR = IMGPATH + "magic-connector-cursor.png";
    
    String CURSOR_IMG_ADD = IMGPATH + "add-cursor.png";
    String CURSOR_IMG_ADD_NOT = IMGPATH + "addnot-cursor.png";
    
    String CURSOR_IMG_ADD_CONNECTION = IMGPATH + "add-connection-cursor.png";
    String CURSOR_IMG_ADD_NOT_CONNECTION = IMGPATH + "addnot-connection-cursor.png";
    
    String CURSOR_IMG_PLUG = IMGPATH + "plug-cursor.png";
    String CURSOR_IMG_PLUG_NOT = IMGPATH + "plugnot-cursor.png";
}
