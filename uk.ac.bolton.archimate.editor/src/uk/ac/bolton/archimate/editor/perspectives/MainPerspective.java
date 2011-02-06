/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import uk.ac.bolton.archimate.editor.ui.ViewManager;
import uk.ac.bolton.archimate.editor.views.navigator.INavigatorView;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;


/**
 * Main Perspective
 * 
 * @author Phillip Beauvoir
 */
public class MainPerspective implements IPerspectiveFactory {

    public static final String ID = "uk.ac.bolton.archimate.editor.perspectiveMain";
    
    /*
     * Folder Layouts
     */
    
    private IFolderLayout folderLayoutLeftTop;
    private IFolderLayout folderLayoutLeftBottom;

    //private IPlaceholderFolderLayout folderLayoutRight;
    //private IFolderLayout folderLayoutRight;
    
    //private IPlaceholderFolderLayout folderLayoutBottom;
    private IFolderLayout folderLayoutBottom;
    
    static String FOLDER_LEFT_TOP = "folderLeftTop"; //$NON-NLS-1$
    static String FOLDER_LEFT_BOTTOM = "folderLeftBottom"; //$NON-NLS-1$
    static String FOLDER_RIGHT = "folderRight"; //$NON-NLS-1$
    static String FOLDER_BOTTOM = "folderBottom"; //$NON-NLS-1$


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
        
        // Placeholder for all other views
        folderLayoutBottom.addPlaceholder("*"); //$NON-NLS-1$
    }

    /**
     * Add the main folder layout areas
     * @param layout
     */
    private void addFolderLayouts(IPageLayout layout) {
        folderLayoutLeftTop = layout.createFolder(FOLDER_LEFT_TOP, IPageLayout.LEFT, .23f, IPageLayout.ID_EDITOR_AREA);
        folderLayoutLeftBottom = layout.createFolder(FOLDER_LEFT_BOTTOM, IPageLayout.BOTTOM, .6f, FOLDER_LEFT_TOP);
        
        //folderLayoutRight = layout.createPlaceholderFolder(FOLDER_RIGHT, IPageLayout.RIGHT, .77f, IPageLayout.ID_EDITOR_AREA);
        //folderLayoutRight = layout.createFolder(FOLDER_RIGHT, IPageLayout.RIGHT, .77f, IPageLayout.ID_EDITOR_AREA);
        
        //folderLayoutBottom = layout.createPlaceholderFolder(FOLDER_BOTTOM, IPageLayout.BOTTOM, .6f, IPageLayout.ID_EDITOR_AREA);
        folderLayoutBottom = layout.createFolder(FOLDER_BOTTOM, IPageLayout.BOTTOM, .7f, IPageLayout.ID_EDITOR_AREA);
    }
    
    
}
