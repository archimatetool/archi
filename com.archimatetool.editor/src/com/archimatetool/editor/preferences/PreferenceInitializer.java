/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.connections.IDiagramConnectionFigure;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = ArchiPlugin.PREFERENCES;
		
	    // ======================================= Appearance =======================================
		
		// Appearance
		
        store.setDefault(SHOW_STATUS_LINE, true);

	    // Colours
	    
        store.setDefault(DERIVE_ELEMENT_LINE_COLOR, true);
        store.setDefault(DERIVE_ELEMENT_LINE_COLOR_FACTOR, 7);
        store.setDefault(SAVE_USER_DEFAULT_COLOR, false);
        
        // Fonts
        
        store.setDefault(ANALYSIS_TABLE_FONT, "");
        store.setDefault(DEFAULT_VIEW_FONT, "");
        store.setDefault(MULTI_LINE_TEXT_FONT, "");
        store.setDefault(MODEL_TREE_FONT, "");
        store.setDefault(NAVIGATOR_TREE_FONT, "");
        store.setDefault(PROPERTIES_TABLE_FONT, "");
        store.setDefault(SINGLE_LINE_TEXT_FONT, "");

        // ======================================= Connections =======================================
        
        // Connections
        
        store.setDefault(ANTI_ALIAS, true);
        store.setDefault(MAGIC_CONNECTOR_POLARITY, false);
        store.setDefault(USE_ORTHOGONAL_ANCHOR, true);
        store.setDefault(USE_LINE_CURVES, true);
        store.setDefault(USE_LINE_JUMPS, true);
        
        store.setDefault(CONNECTION_LABEL_STRATEGY, IDiagramConnectionFigure.CONNECTION_LABEL_CLIPPED);

        store.setDefault(SHOW_WARNING_ON_RECONNECT, true);

        // ARM
        
        store.setDefault(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER, true);
        store.setDefault(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER, true);
        store.setDefault(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER, true);

        store.setDefault(HIDDEN_RELATIONS_TYPES, 1 << 10 | 1 << 9 | 1 << 8 | 1 << 7 | 1 << 6 | 1 << 5 | 1 << 4 | 1 << 3 | 1 << 2 | 1 << 1 | 1 << 0);
        store.setDefault(USE_NESTED_CONNECTIONS, true);
        
        store.setDefault(NEW_RELATIONS_TYPES, 1 << 9 | 1 << 8 | 1 << 7 | 1 << 6 | 1 << 5 | 1 << 1);
        
        // Reverse relationships - by default none are allowed set on 2020-11-11
        //store.setDefault(NEW_REVERSE_RELATIONS_TYPES, 1 << 9 | 1 << 8 | 1 << 7 | 1 << 6 | 1 << 5 | 1 << 1);
        store.setDefault(NEW_REVERSE_RELATIONS_TYPES, 0);

        
        // ======================================= Diagram =======================================
        
        // General
        
        store.setDefault(GRID_SIZE, 12);
        
        store.setDefault(EDIT_NAME_ON_NEW_OBJECT, true);
        store.setDefault(PALETTE_STATE, true);
        store.setDefault(VIEW_TOOLTIPS, true);
        store.setDefault(SHOW_SPECIALIZATIONS_IN_PALETTE, true);
        
        store.setDefault(DIAGRAM_OBJECT_RESIZE_BEHAVIOUR, 0);
        store.setDefault(DIAGRAM_PASTE_SPECIAL_BEHAVIOR, 0);

        store.setDefault(VIEWPOINTS_FILTER_MODEL_TREE, true);
        store.setDefault(VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS, true);
        store.setDefault(VIEWPOINTS_HIDE_PALETTE_ELEMENTS, true);
        store.setDefault(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS, true);
        
        // Old method of scaling figures
        store.setDefault(USE_SCALED_IMAGES, false);
        
        // Windows at hi-res needs this set to true
        store.setDefault(USE_FIGURE_LINE_OFFSET, PlatformUtils.isWindows() && ImageFactory.getDeviceZoom() > 100);

        // Appearance
        
        store.setDefault(DEFAULT_ARCHIMATE_FIGURE_WIDTH, 120);
        store.setDefault(DEFAULT_ARCHIMATE_FIGURE_HEIGHT, 55);
        
        store.setDefault(DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT, ITextAlignment.TEXT_ALIGNMENT_CENTER);
        store.setDefault(DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION, ITextPosition.TEXT_POSITION_TOP);

        store.setDefault(ARCHIMATE_FIGURE_WORD_WRAP_STYLE, 1);
        store.setDefault(DEFAULT_GRADIENT, IDiagramModelObject.FEATURE_GRADIENT_DEFAULT);
        store.setDefault(SKETCH_DEFAULT_BACKGROUND, 1);
        
        
        // ======================================= General =======================================
        
        store.setDefault(OPEN_DIAGRAMS_ON_LOAD, false);
        store.setDefault(BACKUP_ON_SAVE, true);

        store.setDefault(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE, true);
        
        store.setDefault(TREE_SEARCH_AUTO, true);
        store.setDefault(SHOW_WARNING_ON_DELETE_FROM_TREE, true);
        
        store.setDefault(USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE, true);

        // Windows hi-res and Mac Retina use 200 zoom, Linux is 100
        store.setDefault(SCALE_IMAGE_EXPORT, (!PlatformUtils.isLinux() && ImageFactory.getDeviceZoom() > 100) ? true : false);
        
        // Animation
        store.setDefault(ANIMATE_VIEW, false);
        store.setDefault(ANIMATION_VIEW_TIME, 200);
        store.setDefault(ANIMATE_VISUALISER_NODES, false);
        store.setDefault(ANIMATE_VISUALISER_TIME, 400);
        
        // Edge Browser
        store.setDefault(EDGE_BROWSER, false);
        
        // Hints Browser
        store.setDefault(HINTS_BROWSER_JS_ENABLED, false);
        store.setDefault(HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED, false);

        // ======================================= Internal =======================================
        
        store.setDefault(GRID_VISIBLE, false);
        store.setDefault(GRID_SNAP, true);
        store.setDefault(GRID_SHOW_GUIDELINES, true);

        store.setDefault(LINK_VIEW, false);
        store.setDefault(MRU_MAX, 6);

        store.setDefault(USER_DATA_FOLDER, ArchiPlugin.INSTANCE.getWorkspaceFolder().getPath());
        
        // Check for Archi update
        store.setDefault(DOWNLOAD_URL, "https://www.archimatetool.com/download");
        store.setDefault(UPDATE_URL, "https://www.archimatetool.com/archi-version.txt");
        
        store.setDefault(PROPERTIES_SINGLE_COLUMN, false);
    }
}
