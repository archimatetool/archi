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
    String SAVE_USER_DEFAULT_COLOR = "saveUserDefaultFillColorInFile";
    
    // Theme Color Definition IDs
    String VIEW_BACKGROUND_COLOR = "com.archimatetool.editor.VIEW_BACKGROUND";
    String VISUALISER_BACKGROUND_COLOR = "com.archimatetool.editor.VISUALISER_BACKGROUND";
    
    // Default View Font
    String DEFAULT_VIEW_FONT = "defaultViewFont";
    
    // Font scaling
    String FONT_SCALING = "fontScaling";
    
    // Theme Font Definition IDs
    String ANALYSIS_TABLE_FONT = "com.archimatetool.editor.ANALYSIS_TABLE_FONT";
    String MODEL_TREE_FONT = "com.archimatetool.editor.MODEL_TREE_FONT";
    String MULTI_LINE_TEXT_FONT = "com.archimatetool.editor.MULTI_LINE_TEXT_FONT";
    String NAVIGATOR_TREE_FONT = "com.archimatetool.editor.NAVIGATOR_TREE_FONT";
    String PROPERTIES_TABLE_FONT = "com.archimatetool.editor.PROPERTIES_TABLE_FONT";
    String SINGLE_LINE_TEXT_FONT = "com.archimatetool.editor.SINGLE_LINE_TEXT_FONT";
    
    // Mac use native item heights
    String MAC_ITEM_HEIGHT_PROPERTY_KEY = "org.eclipse.swt.internal.cocoa.useNativeItemHeight";
    
    // Legend label prefix
    String LEGEND_LABEL_PREFIX = "legendLabel_";
    
    // Legend color scheme for new legends
    String LEGEND_COLORS_DEFAULT = "legendColorsDefault";
    
    // Legend rows per column default for new legends
    String LEGEND_ROWS_PER_COLUMN_DEFAULT = "legendRowsDefault";
    
    // Legend sort scheme for new legends
    String LEGEND_SORT_DEFAULT = "legendSortDefault";
    
    // ======================================= Connections =======================================
    
    // Connections
    
    String ANTI_ALIAS = "antiAlias";
    String MAGIC_CONNECTOR_POLARITY = "polarityMagicConnector";
    String SHOW_SELECTED_CONNECTIONS = "showSelectedConnections";
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
    String MARGIN_WIDTH = "marginWidth";

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
    String SHOW_SPECIALIZATION_ICONS_IN_MODEL_TREE = "showSpecializationIconsInModelTree";
    String SHOW_SPECIALIZATIONS_IN_MODEL_TREE_MENU = "showSpecializationsInModelTreeMenu";
    String TREE_DISPLAY_NODE_INCREMENT = "treeDisplayNodeIncrement";
    
    String TREE_ALPHANUMERIC_SORT = "treeAlphanumericSort";
    
    String USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE = "useLabelExpressionInAnalysisTable";
    
    // Whether to add a note to the documentation field of relationships that have been changed to Association when setting a new concept type
    String ADD_DOCUMENTATION_NOTE_ON_RELATION_CHANGE = "addDocumentationNoteOnRelationChange";
    String SCALE_IMAGE_EXPORT = "scaleImageExport";
    
    // Animation
    String ANIMATE_VIEW = "animateView";
    String ANIMATION_VIEW_TIME = "animationViewTime";
    String ANIMATE_VISUALISER_NODES = "animateVisualiserNodes";
    String ANIMATE_VISUALISER_TIME = "animationVisualiserTime";
    
    String EDGE_BROWSER = "edgeBrowser";
    
    String HINTS_BROWSER_JS_ENABLED = "hintsJSEnabled";
    String HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED = "hintsExternalHostsEnabled";
    
    // ======================================= Net / Proxy =======================================

    String PREFS_NETWORK_TIMEOUT = "networkTimeout";
    String PREFS_PROXY_ENABLED = "proxyEnabled";
    String PREFS_PROXY_HOST = "proxyHost";
    String PREFS_PROXY_PORT = "proxyPort";
    String PREFS_PROXY_REQUIRES_AUTHENTICATION = "proxyAuthenticate";
    String PREFS_PROXY_USERNAME = "proxyUserName";
    String PREFS_PROXY_PASSWORD = "proxyPassword";

    // ======================================= Internal =======================================
    
    String GRID_VISIBLE = "gridVisible";
    String GRID_SNAP = "gridSnap";
    String GRID_SHOW_GUIDELINES = "gridShowGuidelines";

    String LINK_VIEW = "linkView";
    String MRU_MAX = "mruMax";

    // Check for Archi update
    String DOWNLOAD_URL = "downloadURL";
    String UPDATE_URL = "updateURL";
    
    // Single column layout in Properties View
    String PROPERTIES_SINGLE_COLUMN = "propertiesSingleColumn";
    
    // Search Filter
    String SEARCHFILTER_NAME = "searchFilterName";
    String SEARCHFILTER_DOCUMENTATION = "searchFilterDocumentation";
    String SEARCHFILTER_PROPETY_VALUES = "searchFilterPropertyValues";
    String SEARCHFILTER_VIEWS = "searchFilterViews";
    String SEARCHFILTER_SHOW_ALL_FOLDERS = "searchFilterShowAllFolders";
    String SEARCHFILTER_MATCH_CASE = "searchFilterMatchCase";
    String SEARCHFILTER_USE_REGEX = "searchFilterUseRegex";
    
    // Maximum number of diagrams to open at once when opening model with option enabled or from "open" menu actions
    String MAX_DIAGRAMS_TO_OPEN_AT_ONCE = "maxDiagramsToOpenAtOnce";
}
