/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchimateEditorPlugin;



/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = ArchimateEditorPlugin.INSTANCE.getPreferenceStore();
        
		store.setDefault(USER_DATA_FOLDER, ArchimateEditorPlugin.INSTANCE.getWorkspaceFolder().getPath());
		
		store.setDefault(MRU_MAX, 6);
        
		store.setDefault(GRID_SIZE, 12);
		store.setDefault(GRID_VISIBLE, false);
		store.setDefault(GRID_SNAP, true);
		store.setDefault(GRID_SHOW_GUIDELINES, true);
        
        store.setDefault(VIEW_TOOLTIPS, true);
        
        store.setDefault(ANIMATE, true);
        store.setDefault(ANIMATION_SPEED, 300);
        
        store.setDefault(ANTI_ALIAS, true);
        
        store.setDefault(DEFAULT_VIEW_FONT, ""); //$NON-NLS-1$
        
        store.setDefault(LINK_VIEW, false);
        
        store.setDefault(OPEN_DIAGRAMS_ON_LOAD, false);
        store.setDefault(BACKUP_ON_SAVE, true);
        
        store.setDefault(SKETCH_DEFAULT_BACKGROUND, 1);
        
        store.setDefault(PALETTE_STATE, true);

        store.setDefault(ANIMATE_MAGIC_CONNECTOR, true);
        store.setDefault(MAGIC_CONNECTOR_POLARITY, false);
        
        store.setDefault(ALLOW_CIRCULAR_CONNECTIONS, false);

        store.setDefault(USE_NESTED_CONNECTIONS, true);
        store.setDefault(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER, true);
        store.setDefault(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER, true);
        store.setDefault(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER, true);
        
        store.setDefault(NEW_RELATIONS_TYPES, 1 << 9 | 1 << 8 | 1 << 7);
        store.setDefault(HIDDEN_RELATIONS_TYPES, 1 << 9 | 1 << 8 | 1 << 7);
        
        store.setDefault(VIEWPOINTS_FILTER_MODEL_TREE, true);
        store.setDefault(VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS, false);
        store.setDefault(VIEWPOINTS_HIDE_PALETTE_ELEMENTS, true);
        store.setDefault(VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS, true);
        
        store.setDefault(EDIT_NAME_ON_NEW_OBJECT, true);
        
        store.setDefault(SAVE_USER_DEFAULT_COLOR, false);
        store.setDefault(SHOW_FILL_COLORS_IN_GUI, false);
        store.setDefault(DERIVE_ELEMENT_LINE_COLOR, true);
        store.setDefault(DERIVE_ELEMENT_LINE_COLOR_FACTOR, 7);
        
        store.setDefault(USE_ORTHOGONAL_ANCHOR, false);
        store.setDefault(USE_LINE_CURVES, true);
        store.setDefault(USE_LINE_JUMPS, true);
        
        store.setDefault(ANIMATE_VISUALISER_NODES, true);
        
        // Show shadows
        store.setDefault(SHOW_SHADOWS, false);
        
        // Eclipse traditional tabs
        IPreferenceStore eclipseStore = PlatformUI.getPreferenceStore();
        eclipseStore.setDefault(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
    }
}
