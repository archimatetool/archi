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
public interface IArchiImages {
    
    ImageFactory ImageFactory = new ImageFactory(ArchiPlugin.INSTANCE);

    String IMGPATH = "img/"; //$NON-NLS-1$
    
    String ARCHIMATE_IMGPATH = IMGPATH + "archimate/"; //$NON-NLS-1$
    
    String ICON_APP = IMGPATH + "app-16.png"; //$NON-NLS-1$
    String ICON_APP_32 = IMGPATH + "app-32.png"; //$NON-NLS-1$
    String ICON_APP_48 = IMGPATH + "app-48.png"; //$NON-NLS-1$
    String ICON_APP_64 = IMGPATH + "app-64.png"; //$NON-NLS-1$
    String ICON_APP_128 = IMGPATH + "app-128.png"; //$NON-NLS-1$
    
    String BROWN_PAPER_BACKGROUND = IMGPATH + "br_paper.jpg"; //$NON-NLS-1$
    String CORK_BACKGROUND = IMGPATH + "cork.jpg"; //$NON-NLS-1$
    String DEFAULT_MODEL_THUMB = IMGPATH + "default_model_thumb.png"; //$NON-NLS-1$
    
    // ECLIPSE IMAGES
    String ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON = IMGPATH + "prop_ps.png"; //$NON-NLS-1$
    String ECLIPSE_IMAGE_OUTLINE_VIEW_ICON = IMGPATH + "outline.png"; //$NON-NLS-1$
    String ECLIPSE_IMAGE_NEW_WIZARD = IMGPATH + "new_wiz.png"; //$NON-NLS-1$
    String ECLIPSE_IMAGE_IMPORT_PREF_WIZARD = IMGPATH + "importpref_wiz.png";  //$NON-NLS-1$
    String ECLIPSE_IMAGE_EXPORT_DIR_WIZARD = IMGPATH + "exportdir_wiz.png"; //$NON-NLS-1$
    String ECLIPSE_IMAGE_FILE = IMGPATH + "file_obj.png"; //$NON-NLS-1$
    String ECLIPSE_IMAGE_FOLDER = IMGPATH + "fldr_obj.png"; //$NON-NLS-1$
    
    String ICON_FOLDER_DEFAULT = IMGPATH + "folder-default.png"; //$NON-NLS-1$
    
    String MENU_ARROW = IMGPATH + "menu-arrow.png"; //$NON-NLS-1$
    
    String ZOOM_IN = IMGPATH + "zoomin.png"; //$NON-NLS-1$
    String ZOOM_OUT = IMGPATH + "zoomout.png"; //$NON-NLS-1$
    String ZOOM_NORMAL = IMGPATH + "zoomnormal.png"; //$NON-NLS-1$
    
    // Plain
    String ICON_ACTOR = ARCHIMATE_IMGPATH + "actor.png"; //$NON-NLS-1$

    // Elements
    String ICON_APPLICATION_COLLABORATION = ARCHIMATE_IMGPATH + "application-collaboration.png"; //$NON-NLS-1$
    String ICON_APPLICATION_COMPONENT = ARCHIMATE_IMGPATH + "application-component.png"; //$NON-NLS-1$
    String ICON_APPLICATION_EVENT = ARCHIMATE_IMGPATH + "application-event.png"; //$NON-NLS-1$
    String ICON_APPLICATION_FUNCTION = ARCHIMATE_IMGPATH + "application-function.png"; //$NON-NLS-1$
    String ICON_APPLICATION_INTERACTION = ARCHIMATE_IMGPATH + "application-interaction.png"; //$NON-NLS-1$
    String ICON_APPLICATION_INTERFACE = ARCHIMATE_IMGPATH + "application-interface.png"; //$NON-NLS-1$
    String ICON_APPLICATION_PROCESS = ARCHIMATE_IMGPATH + "application-process.png"; //$NON-NLS-1$
    String ICON_APPLICATION_SERVICE = ARCHIMATE_IMGPATH + "application-service.png"; //$NON-NLS-1$
    String ICON_ARTIFACT = ARCHIMATE_IMGPATH + "artifact.png"; //$NON-NLS-1$
    String ICON_ASSESSMENT = ARCHIMATE_IMGPATH + "assessment.png"; //$NON-NLS-1$
    String ICON_BUSINESS_ACTOR = ARCHIMATE_IMGPATH + "business-actor.png"; //$NON-NLS-1$
    String ICON_BUSINESS_COLLABORATION = ARCHIMATE_IMGPATH + "business-collaboration.png"; //$NON-NLS-1$
    String ICON_BUSINESS_EVENT = ARCHIMATE_IMGPATH + "business-event.png"; //$NON-NLS-1$
    String ICON_BUSINESS_FUNCTION = ARCHIMATE_IMGPATH + "business-function.png"; //$NON-NLS-1$
    String ICON_BUSINESS_INTERACTION = ARCHIMATE_IMGPATH + "business-interaction.png"; //$NON-NLS-1$
    String ICON_BUSINESS_INTERFACE = ARCHIMATE_IMGPATH + "business-interface.png"; //$NON-NLS-1$
    String ICON_BUSINESS_OBJECT = ARCHIMATE_IMGPATH + "business-object.png"; //$NON-NLS-1$
    String ICON_BUSINESS_PROCESS = ARCHIMATE_IMGPATH + "business-process.png"; //$NON-NLS-1$
    String ICON_BUSINESS_ROLE = ARCHIMATE_IMGPATH + "business-role.png"; //$NON-NLS-1$
    String ICON_BUSINESS_SERVICE = ARCHIMATE_IMGPATH + "business-service.png"; //$NON-NLS-1$
    String ICON_COMMUNICATION_NETWORK = ARCHIMATE_IMGPATH + "communication-network.png"; //$NON-NLS-1$
    String ICON_CAPABILITY = ARCHIMATE_IMGPATH + "capability.png"; //$NON-NLS-1$
    String ICON_CONSTRAINT = ARCHIMATE_IMGPATH + "constraint.png"; //$NON-NLS-1$
    String ICON_CONTRACT = ARCHIMATE_IMGPATH + "contract.png"; //$NON-NLS-1$
    String ICON_COURSE_OF_ACTION = ARCHIMATE_IMGPATH + "course-of-action.png"; //$NON-NLS-1$
    String ICON_DATA_OBJECT = ARCHIMATE_IMGPATH + "data-object.png"; //$NON-NLS-1$
    String ICON_DELIVERABLE = ARCHIMATE_IMGPATH + "deliverable.png"; //$NON-NLS-1$
    String ICON_DEVICE = ARCHIMATE_IMGPATH + "device.png"; //$NON-NLS-1$
    String ICON_DISTRIBUTION_NETWORK = ARCHIMATE_IMGPATH + "distribution-network.png"; //$NON-NLS-1$
    String ICON_DRIVER = ARCHIMATE_IMGPATH + "driver.png"; //$NON-NLS-1$
    String ICON_EQUIPMENT = ARCHIMATE_IMGPATH + "equipment.png"; //$NON-NLS-1$
    String ICON_FACILITY = ARCHIMATE_IMGPATH + "facility.png"; //$NON-NLS-1$
    String ICON_GAP = ARCHIMATE_IMGPATH + "gap.png"; //$NON-NLS-1$
    String ICON_GOAL = ARCHIMATE_IMGPATH + "goal.png"; //$NON-NLS-1$
    String ICON_GROUPING = ARCHIMATE_IMGPATH + "grouping.png"; //$NON-NLS-1$
    String ICON_IMPLEMENTATION_EVENT = ARCHIMATE_IMGPATH + "implementation-event.png"; //$NON-NLS-1$
    String ICON_LOCATION = ARCHIMATE_IMGPATH + "location.png"; //$NON-NLS-1$
    String ICON_MATERIAL = ARCHIMATE_IMGPATH + "material.png"; //$NON-NLS-1$
    String ICON_MEANING = ARCHIMATE_IMGPATH + "meaning.png"; //$NON-NLS-1$
    String ICON_NODE = ARCHIMATE_IMGPATH + "node.png"; //$NON-NLS-1$
    String ICON_OUTCOME = ARCHIMATE_IMGPATH + "outcome.png"; //$NON-NLS-1$
    String ICON_PATH = ARCHIMATE_IMGPATH + "path.png"; //$NON-NLS-1$
    String ICON_PLATEAU = ARCHIMATE_IMGPATH + "plateau.png"; //$NON-NLS-1$
    String ICON_PRINCIPLE = ARCHIMATE_IMGPATH + "principle.png"; //$NON-NLS-1$
    String ICON_PRODUCT = ARCHIMATE_IMGPATH + "product.png"; //$NON-NLS-1$
    String ICON_REPRESENTATION = ARCHIMATE_IMGPATH + "representation.png"; //$NON-NLS-1$
    String ICON_RESOURCE = ARCHIMATE_IMGPATH + "resource.png"; //$NON-NLS-1$
    String ICON_REQUIREMENT = ARCHIMATE_IMGPATH + "requirement.png"; //$NON-NLS-1$
    String ICON_STAKEHOLDER = ARCHIMATE_IMGPATH + "stakeholder.png"; //$NON-NLS-1$
    String ICON_SYSTEM_SOFTWARE = ARCHIMATE_IMGPATH + "system-software.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_COLLABORATION = ARCHIMATE_IMGPATH + "technology-collaboration.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_EVENT = ARCHIMATE_IMGPATH + "technology-event.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_FUNCTION = ARCHIMATE_IMGPATH + "technology-function.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_INTERACTION = ARCHIMATE_IMGPATH + "technology-interaction.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_INTERFACE = ARCHIMATE_IMGPATH + "technology-interface.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_PROCESS = ARCHIMATE_IMGPATH + "technology-process.png"; //$NON-NLS-1$
    String ICON_TECHNOLOGY_SERVICE = ARCHIMATE_IMGPATH + "technology-service.png"; //$NON-NLS-1$
    String ICON_VALUE = ARCHIMATE_IMGPATH + "value.png"; //$NON-NLS-1$
    String ICON_VALUE_STREAM = ARCHIMATE_IMGPATH + "value-stream.png"; //$NON-NLS-1$
    String ICON_WORKPACKAGE = ARCHIMATE_IMGPATH + "workpackage.png"; //$NON-NLS-1$
    
    // Relations
    String ICON_ACESS_RELATION = ARCHIMATE_IMGPATH + "access.png"; //$NON-NLS-1$
    String ICON_AGGREGATION_RELATION = ARCHIMATE_IMGPATH + "aggregation.png"; //$NON-NLS-1$
    String ICON_ASSIGNMENT_RELATION = ARCHIMATE_IMGPATH + "assignment.png"; //$NON-NLS-1$
    String ICON_ASSOCIATION_RELATION = ARCHIMATE_IMGPATH + "association.png"; //$NON-NLS-1$
    String ICON_COMPOSITION_RELATION = ARCHIMATE_IMGPATH + "composition.png"; //$NON-NLS-1$
    String ICON_FLOW_RELATION = ARCHIMATE_IMGPATH + "flow.png"; //$NON-NLS-1$
    String ICON_INFLUENCE_RELATION = ARCHIMATE_IMGPATH + "influence.png"; //$NON-NLS-1$
    String ICON_REALIZATION_RELATION = ARCHIMATE_IMGPATH + "realization.png"; //$NON-NLS-1$
    String ICON_SPECIALIZATION_RELATION = ARCHIMATE_IMGPATH + "specialization.png"; //$NON-NLS-1$
    String ICON_TRIGGERING_RELATION = ARCHIMATE_IMGPATH + "triggering.png"; //$NON-NLS-1$
    String ICON_SERVING_RELATION = ARCHIMATE_IMGPATH + "serving.png"; //$NON-NLS-1$
    
    // Junctions
    String ICON_AND_JUNCTION = ARCHIMATE_IMGPATH + "and-junction.png"; //$NON-NLS-1$
    String ICON_OR_JUNCTION = ARCHIMATE_IMGPATH + "or-junction.png"; //$NON-NLS-1$
    

    
    // Other
    String ICON_ALIGN_TEXT_LEFT = IMGPATH + "alignleft.gif"; //$NON-NLS-1$
    String ICON_ALIGN_TEXT_CENTER = IMGPATH + "aligncenter.gif"; //$NON-NLS-1$
    String ICON_ALIGN_TEXT_RIGHT = IMGPATH + "alignright.gif"; //$NON-NLS-1$

    String ICON_ALIGN_TEXT_TOP = IMGPATH + "aligntop.png"; //$NON-NLS-1$
    String ICON_ALIGN_TEXT_MIDDLE = IMGPATH + "alignmiddle.png"; //$NON-NLS-1$
    String ICON_ALIGN_TEXT_BOTTOM = IMGPATH + "alignbottom.png"; //$NON-NLS-1$

    
    String ICON_ASPECT_RATIO = IMGPATH + "aspect-ratio.png"; //$NON-NLS-1$
    String ICON_BROWSER = IMGPATH + "browser.png"; //$NON-NLS-1$
    String ICON_CANCEL_SEARCH = IMGPATH + "cancelsearch.png"; //$NON-NLS-1$
    String ICON_COG = IMGPATH + "cog.png"; //$NON-NLS-1$
    String ICON_COLLAPSEALL = IMGPATH + "collapseall.png"; //$NON-NLS-1$
    String ICON_DIAGRAM = IMGPATH + "diagram.png"; //$NON-NLS-1$
    String ICON_DEFAULT_SIZE = IMGPATH + "default-size.png"; //$NON-NLS-1$
    String ICON_DERIVED = IMGPATH + "derived.png"; //$NON-NLS-1$
    String ICON_DERIVED_SM = IMGPATH + "derived-sm.png"; //$NON-NLS-1$
    String ICON_EXPANDALL = IMGPATH + "expandall.png"; //$NON-NLS-1$
    String ICON_FILTER = IMGPATH + "filter.png"; //$NON-NLS-1$
    String ICON_FONT = IMGPATH + "font.png"; //$NON-NLS-1$
    String ICON_FORMAT_PAINTER = IMGPATH + "formatpainter.png"; //$NON-NLS-1$
    String ICON_FORMAT_PAINTER_GREY = IMGPATH + "formatpainter-grey.png"; //$NON-NLS-1$
    String ICON_GROUP = IMGPATH + "group.png"; //$NON-NLS-1$
    String ICON_LINKED = IMGPATH + "linked.png"; //$NON-NLS-1$
    String ICON_LANDSCAPE = IMGPATH + "landscape.png"; //$NON-NLS-1$
    String ICON_LOCK = IMGPATH + "lockedstate.png"; //$NON-NLS-1$
    String ICON_MAGIC_CONNECTION = IMGPATH + "magic_connection.png"; //$NON-NLS-1$
    String ICON_MINUS = IMGPATH + "minus.png"; //$NON-NLS-1$
    String ICON_MODELS = IMGPATH + "models.png"; //$NON-NLS-1$
    String ICON_MUTIPLE = IMGPATH + "mutiple.png"; //$NON-NLS-1$
    String ICON_NAVIGATOR = IMGPATH + "navigator.png"; //$NON-NLS-1$
    String ICON_NAVIGATOR_DOWNWARD = IMGPATH + "nav-downward.png"; //$NON-NLS-1$
    String ICON_NAVIGATOR_UPWARD = IMGPATH + "nav-upward.png"; //$NON-NLS-1$
    String ICON_NEW_FILE = IMGPATH + "newfile_wiz.png"; //$NON-NLS-1$
    String ICON_NOTE = IMGPATH + "note.png"; //$NON-NLS-1$
    String ICON_OPEN = IMGPATH + "open.png"; //$NON-NLS-1$
    String ICON_PIN = IMGPATH + "pin.png"; //$NON-NLS-1$
    String ICON_PLUS = IMGPATH + "plus.png"; //$NON-NLS-1$
    String ICON_SEARCH = IMGPATH + "search.png"; //$NON-NLS-1$
    String ICON_SKETCH = IMGPATH + "sketch.png"; //$NON-NLS-1$
    String ICON_SMALL_X = IMGPATH + "smallx.png"; //$NON-NLS-1$
    String ICON_SORT = IMGPATH + "alphab_sort_co.png"; //$NON-NLS-1$
    String ICON_SPECIALIZATION = IMGPATH + "specialization.png"; //$NON-NLS-1$
    String ICON_STICKY = IMGPATH + "sticky.png"; //$NON-NLS-1$
    String ICON_TRASH = IMGPATH + "trash.png"; //$NON-NLS-1$
    String ICON_UNLOCK = IMGPATH + "unlockedstate.png"; //$NON-NLS-1$
    
    String ICON_CONNECTION_PLAIN = IMGPATH + "connection-plain.png"; //$NON-NLS-1$
    String ICON_CONNECTION_ARROW = IMGPATH + "connection-arrow.png"; //$NON-NLS-1$
    String ICON_CONNECTION_DASHED_ARROW = IMGPATH + "connection-dashed-arrow.png"; //$NON-NLS-1$
    String ICON_CONNECTION_DOTTED_ARROW = IMGPATH + "connection-dotted-arrow.png"; //$NON-NLS-1$
    
    String LINE_SOLID = IMGPATH + "line-solid.png"; //$NON-NLS-1$
    String LINE_DASHED = IMGPATH + "line-dashed.png"; //$NON-NLS-1$
    String LINE_DOTTED = IMGPATH + "line-dotted.png"; //$NON-NLS-1$
    
    String ARROW_SOURCE_FILL = IMGPATH + "arrow-source-fill.png"; //$NON-NLS-1$
    String ARROW_TARGET_FILL = IMGPATH + "arrow-target-fill.png"; //$NON-NLS-1$
    String ARROW_SOURCE_HOLLOW = IMGPATH + "arrow-source-hollow.png"; //$NON-NLS-1$
    String ARROW_TARGET_HOLLOW = IMGPATH + "arrow-target-hollow.png"; //$NON-NLS-1$
    String ARROW_SOURCE_LINE = IMGPATH + "arrow-source-line.png"; //$NON-NLS-1$
    String ARROW_TARGET_LINE = IMGPATH + "arrow-target-line.png"; //$NON-NLS-1$

    String CURSOR_IMG_MAGIC_CONNECTOR = IMGPATH + "magic-connector-cursor.png"; //$NON-NLS-1$
}
