/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.navigator.INavigatorView;
import com.archimatetool.editor.views.tree.ITreeModelView;



/**
 * Main Perspective
 * 
 * @author Phillip Beauvoir
 */
public class MainPerspective implements IPerspectiveFactory {

    public static final String ID = "com.archimatetool.editor.perspectiveMain"; //$NON-NLS-1$
    
    /*
     * Folder Layouts
     */
    private IFolderLayout folderLayoutLeftTop;
    private IFolderLayout folderLayoutLeftBottom;
    private IFolderLayout folderLayoutBottom;

    private IPlaceholderFolderLayout folderLayoutRight;
    
    static String FOLDER_LEFT_TOP = "folderLeftTop"; //$NON-NLS-1$
    static String FOLDER_LEFT_BOTTOM = "folderLeftBottom"; //$NON-NLS-1$
    static String FOLDER_RIGHT = "folderRight"; //$NON-NLS-1$
    static String FOLDER_BOTTOM = "folderBottom"; //$NON-NLS-1$


    @Override
    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(true);
        
        addFolderLayouts(layout);
        
        // Models View
        folderLayoutLeftTop.addView(ITreeModelView.ID);
        
        // Properties View
        folderLayoutBottom.addView(ViewManager.PROPERTIES_VIEW);
        
        // Outline View
        folderLayoutLeftBottom.addView(ViewManager.OUTLINE_VIEW);
        
        // Navigator View
        folderLayoutLeftBottom.addView(INavigatorView.ID);
        
        // Palette View
        folderLayoutRight.addPlaceholder("org.eclipse.gef.ui.palette_view"); //$NON-NLS-1$
        
        // Placeholder for all other views
        folderLayoutBottom.addPlaceholder("*"); //$NON-NLS-1$
    }

    /**
     * Add the main folder layout areas
     */
    private void addFolderLayouts(IPageLayout layout) {
        // The declared order of these matters
        folderLayoutLeftTop = layout.createFolder(FOLDER_LEFT_TOP, IPageLayout.LEFT, .23f, IPageLayout.ID_EDITOR_AREA);
        folderLayoutLeftBottom = layout.createFolder(FOLDER_LEFT_BOTTOM, IPageLayout.BOTTOM, .6f, FOLDER_LEFT_TOP);
        folderLayoutRight = layout.createPlaceholderFolder(FOLDER_RIGHT, IPageLayout.RIGHT, .85f, IPageLayout.ID_EDITOR_AREA);
        folderLayoutBottom = layout.createFolder(FOLDER_BOTTOM, IPageLayout.BOTTOM, .7f, IPageLayout.ID_EDITOR_AREA);
    }
}
