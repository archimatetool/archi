/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;




/**
 * Constant definitions for main preferences
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface IPreferenceConstants {
    
    // ======================================= Appearance =======================================
    
    // Appearance
    
    String SHOW_STATUS_LINE = "showStatusLine";

    // Colours
    
    String DEFAULT_CONNECTION_LINE_COLOR = "defaultConnectionLineColor";
    String DEFAULT_ELEMENT_LINE_COLOR = "defaultElementLineColor";
    String DEFAULT_FILL_COLOR_PREFIX = "defaultFillColour_";
    String FOLDER_COLOUR_PREFIX = "folderColour_";

    String DERIVE_ELEMENT_LINE_COLOR = "deriveElementLineColor";
    String DERIVE_ELEMENT_LINE_COLOR_FACTOR = "deriveElementLineColorFactor";
    String SAVE_USER_DEFAULT_COLOR = "saveUserDefaultFillColorInFile";
    
    // Fonts
    
    String ANALYSIS_TABLE_FONT = "analysisTableFont";
    String DEFAULT_VIEW_FONT = "defaultViewFont";
    String MODEL_TREE_FONT = "modelTreeFont";
    String MULTI_LINE_TEXT_FONT = "multiLineTextFont";
    String NAVIGATOR_TREE_FONT = "navigatorTreeFont";
    String PROPERTIES_TABLE_FONT = "propertiesTableFont";
    String SINGLE_LINE_TEXT_FONT = "singleLineTextFont";
    
    // ======================================= Connections =======================================
    
    // Connections
    
    String ANTI_ALIAS = "antiAlias";
    String MAGIC_CONNECTOR_POLARITY = "polarityMagicConnector";
    String USE_ORTHOGONAL_ANCHOR = "orthogonalAnchor";
    String USE_LINE_CURVES = "lineCurves";
    String USE_LINE_JUMPS = "lineJumps";
    
    String CONNECTION_LABEL_STRATEGY = "connectionLabelStrategy";

    String SHOW_WARNING_ON_RECONNECT = "showWarningOnReconnect";

    // ARM
    
    String CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER = "createRelationWhenAddingNewElementToContainer";
    String CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER = "createRelationWhenAddingModelTreeElementToContainer";
    String CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER = "createRelationWhenMovingElementToContainer";

    String HIDDEN_RELATIONS_TYPES = "hiddenRelationsTypes";
    String USE_NESTED_CONNECTIONS = "useNestedConnections";

    String NEW_RELATIONS_TYPES = "newRelationsTypes";
    String NEW_REVERSE_RELATIONS_TYPES = "newReverseRelationsTypes";

    
    // ======================================= Diagram =======================================
    
    // General
    
    String GRID_SIZE = "gridSize";

    String EDIT_NAME_ON_NEW_OBJECT = "editNameOnNewObject";
    String PALETTE_STATE = "palette_state";
    String VIEW_TOOLTIPS = "viewTooltips";
    String SHOW_SPECIALIZATIONS_IN_PALETTE = "showSpecializationsInPalette";

    String DIAGRAM_OBJECT_RESIZE_BEHAVIOUR = "resizeBehaviour"; 
    String DIAGRAM_PASTE_SPECIAL_BEHAVIOR = "pasteSpecialBehavior";
   
    String VIEWPOINTS_FILTER_MODEL_TREE = "viewpointsFilterModelTree";
    String VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS = "viewpointsGhostDiagramElements";
    String VIEWPOINTS_HIDE_PALETTE_ELEMENTS = "viewpointsHidePaletteElements";
    String VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS = "viewpointsHideMagicConnectorElements";

    // Whether to use the older method to scale images first when drawing them
    String USE_SCALED_IMAGES = "scaledImages";
    
    // Whether to use a line offset in all cases for figures drawn on hi-res screens
    String USE_FIGURE_LINE_OFFSET = "figureLineOffset";

    // Appearance
    
    String DEFAULT_ARCHIMATE_FIGURE_WIDTH = "defaultArchiMateFigureWidth";
    String DEFAULT_ARCHIMATE_FIGURE_HEIGHT = "defaultArchiMateFigureHeight";
    
    String DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT = "defaultArchiMateTextAlignment";
    String DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION = "defaultArchiMateTextPosition";
    
    String ARCHIMATE_FIGURE_WORD_WRAP_STYLE = "archimateFigureWordWrapStyle";
    String DEFAULT_GRADIENT = "defaultGradient";
    String SKETCH_DEFAULT_BACKGROUND = "sketchDefaultBackground";
    
    // Default Figures

    String DEFAULT_FIGURE_PREFIX = "defaultFigure"; 
    
    
    // ======================================= General =======================================
    
    String OPEN_DIAGRAMS_ON_LOAD = "openDiagramsOnLoad";
    String BACKUP_ON_SAVE = "backupOnSave";
    
    String HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE = "highlightUnusedElementsInModelTree";
    
    String TREE_SEARCH_AUTO = "treeSearchAuto";
    String SHOW_WARNING_ON_DELETE_FROM_TREE = "showWarningOnDeleteFromTree";
    
    String USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE = "useLabelExpressionInAnalysisTable";
    
    String SCALE_IMAGE_EXPORT = "scaleImageExport";
    
    // Animation
    String ANIMATE_VIEW = "animateView";
    String ANIMATION_VIEW_TIME = "animationViewTime";
    String ANIMATE_VISUALISER_NODES = "animateVisualiserNodes";
    String ANIMATE_VISUALISER_TIME = "animationVisualiserTime";
    
    String EDGE_BROWSER = "edgeBrowser";
    
    String HINTS_BROWSER_JS_ENABLED = "hintsJSEnabled";
    String HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED = "hintsExternalHostsEnabled";

    // ======================================= Internal =======================================
    
    String GRID_VISIBLE = "gridVisible";
    String GRID_SNAP = "gridSnap";
    String GRID_SHOW_GUIDELINES = "gridShowGuidelines";

    String LINK_VIEW = "linkView";
    String MRU_MAX = "mruMax";

    String USER_DATA_FOLDER = "userDataFolder";
    
    // Check for Archi update
    String DOWNLOAD_URL = "downloadURL";
    String UPDATE_URL = "updateURL";
    
    // Single column layout in Properties View
    String PROPERTIES_SINGLE_COLUMN = "propertiesSingleColumn";
}
