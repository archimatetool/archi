/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;




/**
 * Constant definitions for plug-in preferences
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface IPreferenceConstants {
    String USER_DATA_FOLDER = "userDataFolder";
    
    String MRU_MAX = "mruMax";
    
    String GRID_SIZE = "gridSize";
    String GRID_VISIBLE = "gridVisible";
    String GRID_SNAP = "gridSnap";
    String GRID_SHOW_GUIDELINES = "gridShowGuidelines";
    
    String VIEW_TOOLTIPS = "viewTooltips";
    
    String OPEN_DIAGRAMS_ON_LOAD = "openDiagramsOnLoad";
    String BACKUP_ON_SAVE = "backupOnSave";
    
    String ANTI_ALIAS = "antiAlias";
    
    String DEFAULT_VIEW_FONT = "defaultViewFont";
    String MULTI_LINE_TEXT_FONT = "multiLineTextFont";
    
    String LINK_VIEW = "linkView";
    
    String SKETCH_DEFAULT_BACKGROUND = "sketchDefaultBackground";
    
    String PALETTE_STATE = "palette_state";

    String MAGIC_CONNECTOR_POLARITY = "polarityMagicConnector";
    
    String USE_NESTED_CONNECTIONS = "useNestedConnections";
    String CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER = "createRelationWhenAddingNewElementToContainer";
    String CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER = "createRelationWhenAddingModelTreeElementToContainer";
    String CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER = "createRelationWhenMovingElementToContainer";
    
    String SHOW_WARNING_ON_RECONNECT = "showWarningonRecoonect";
    
    String NEW_RELATIONS_TYPES = "newRelationsTypes";
    String NEW_REVERSE_RELATIONS_TYPES = "newReverseRelationsTypes";
    String HIDDEN_RELATIONS_TYPES = "hiddenRelationsTypes";
    
    String VIEWPOINTS_FILTER_MODEL_TREE = "viewpointsFilterModelTree";
    String VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS = "viewpointsGhostDiagramElements";
    String VIEWPOINTS_HIDE_PALETTE_ELEMENTS = "viewpointsHidePaletteElements";
    String VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS = "viewpointsHideMagicConnectorElements";

    String EDIT_NAME_ON_NEW_OBJECT = "editNameOnNewObject";
    
    String DEFAULT_FILL_COLOR_PREFIX = "defaultFillColour_";
    String SAVE_USER_DEFAULT_COLOR = "saveUserDefaultFillColorInFile";
    String DERIVE_ELEMENT_LINE_COLOR = "deriveElementLineColor";
    String DERIVE_ELEMENT_LINE_COLOR_FACTOR = "deriveElementLineColorFactor";
    
    String DEFAULT_ELEMENT_LINE_COLOR = "defaultElementLineColor";
    String DEFAULT_CONNECTION_LINE_COLOR = "defaultConnectionLineColor";
    
    String FOLDER_COLOUR_PREFIX = "folderColour_";
    
    String DEFAULT_FIGURE_PREFIX = "defaultFigure"; 

    String USE_ORTHOGONAL_ANCHOR = "orthogonalAnchor";
    String USE_LINE_CURVES = "lineCurves";
    String USE_LINE_JUMPS = "lineJumps";
    
    String SHOW_GRADIENT = "showGradient";
    
    String ARCHIMATE_FIGURE_WORD_WRAP_STYLE = "archimateFigureWordWrapStyle";
    
    String SHOW_STATUS_LINE = "showStatusLine";
    
    String DEFAULT_ARCHIMATE_FIGURE_WIDTH = "defaultArchiMateFigureWidth";
    String DEFAULT_ARCHIMATE_FIGURE_HEIGHT = "defaultArchiMateFigureHeight";
    String DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT = "defaultArchiMateTextAlignment";
    String DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION = "defaultArchiMateTextPosition";
    
    String HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE = "highlightUnusedElementsInModelTree";
    
    String DIAGRAM_PASTE_SPECIAL_BEHAVIOR = "pasteSpecialBehavior";
    
    String DOWNLOAD_URL = "downloadURL";
    String UPDATE_URL = "updateURL";
    
    String SCALE_IMAGE_EXPORT = "scaleImageExport";
    
    String DIAGRAM_OBJECT_RESIZE_BEHAVIOUR = "resizeBehaviour"; 
    
    String TREE_SEARCH_AUTO = "treeSearchAuto";
    
    String THEME_AUTO = "themeAuto";
}
