/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.model.IModelImporter;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.components.HeapStatusWidget.HeapStatusWidgetToolBarContributionItem;
import com.archimatetool.editor.ui.dialog.RelationshipsMatrixDialog;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.views.navigator.INavigatorView;
import com.archimatetool.editor.views.tree.ITreeModelView;



/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateEditorActionBarAdvisor
extends ActionBarAdvisor {
    
    private IWorkbenchAction fActionNewArchimateModel;
    private IWorkbenchAction fActionOpenModel;
    private IWorkbenchAction fActionOpenDiagram;
    private IWorkbenchAction fActionCloseModel;
    private IWorkbenchAction fActionCloseEditor;
    private IWorkbenchAction fActionCloseAllEditors;
    private IWorkbenchAction fActionSave;
    private IWorkbenchAction fActionSaveAs;
    private IWorkbenchAction fActionQuit;
    private IWorkbenchAction fActionAbout;
    private IWorkbenchAction fActionProperties;
    private IWorkbenchAction fActionPrint;
    
    private IWorkbenchAction fActionCut;
    private IWorkbenchAction fActionCopy;
    private IWorkbenchAction fActionPaste;
    private IWorkbenchAction fActionDelete;
    private IWorkbenchAction fActionRename;
    private IWorkbenchAction fActionDuplicate;
    
    private IWorkbenchAction fActionUndo;
    private IWorkbenchAction fActionRedo;

    private IWorkbenchAction fActionSelectAll;

    private IWorkbenchAction fActionResetPerspective;
    private Action fActionToggleCoolbar;
    
    private IAction fShowModelsView;
    private IAction fShowPropertiesView;
    private IAction fShowOutlineView;
    private IAction fShowNavigatorView;
    private IAction fShowPaletteView;
    
    private IAction fActionShowRelationsMatrix;
    
    private IAction fArchiWebsiteAction;
    private IAction fActionCheckForNewVersion;

    
    /**
     * Constructor
     * @param configurer
     */
    public ArchimateEditorActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    @Override
    protected void makeActions(final IWorkbenchWindow window) {
        
        // Open Model
        fActionOpenModel = new OpenModelAction(window);
        register(fActionOpenModel);
        
        // New Archimate model
        fActionNewArchimateModel = new NewArchimateModelAction();
        register(fActionNewArchimateModel);
        
        // Open Diagram
        fActionOpenDiagram = ArchimateEditorActionFactory.OPEN_DIAGRAM.create(window);
        register(fActionOpenDiagram);
        
        // Close Model
        fActionCloseModel = ArchimateEditorActionFactory.CLOSE_MODEL.create(window);
        register(fActionCloseModel);
        
        // Close Editor
        fActionCloseEditor = ActionFactory.CLOSE.create(window);
        fActionCloseEditor.setText(Messages.ArchimateEditorActionBarAdvisor_0);
        register(fActionCloseEditor);

        // Close All Editors
        fActionCloseAllEditors = ActionFactory.CLOSE_ALL.create(window);
        fActionCloseAllEditors.setText(Messages.ArchimateEditorActionBarAdvisor_1);
        register(fActionCloseAllEditors);
        
        // Save
        fActionSave = ArchimateEditorActionFactory.SAVE_MODEL.create(window);
        register(fActionSave);
        
        // Save As
        fActionSaveAs = ArchimateEditorActionFactory.SAVE_AS.create(window);
        register(fActionSaveAs);
        
        // Properties
        fActionProperties = ActionFactory.PROPERTIES.create(window);
        register(fActionProperties);
        
        // Quit
        fActionQuit = ActionFactory.QUIT.create(window);
        register(fActionQuit);
        
        // Undo
        fActionUndo = ActionFactory.UNDO.create(window);
        register(fActionUndo);

        // Redo
        fActionRedo = ActionFactory.REDO.create(window);
        register(fActionRedo);
        
        // Cut
        fActionCut = ActionFactory.CUT.create(window);
        register(fActionCut);
        
        // Copy
        fActionCopy = ActionFactory.COPY.create(window);
        register(fActionCopy);
        
        // Paste
        fActionPaste = ActionFactory.PASTE.create(window);
        register(fActionPaste);
        
        // Delete
        fActionDelete = ArchimateEditorActionFactory.DELETE.create(window);
        register(fActionDelete);
        
        // Rename
        fActionRename = ArchimateEditorActionFactory.RENAME.create(window);
        register(fActionRename);
        
        // Duplicate
        fActionDuplicate = ArchimateEditorActionFactory.DUPLICATE.create(window);
        register(fActionDuplicate);

        // Select All
        fActionSelectAll = ActionFactory.SELECT_ALL.create(window);
        register(fActionSelectAll);
        
        // Print
        fActionPrint = ActionFactory.PRINT.create(window);
        register(fActionPrint);
        
        // About
        fActionAbout = ActionFactory.ABOUT.create(window);
        register(fActionAbout);
        
        // Reset Perspective
        fActionResetPerspective = ActionFactory.RESET_PERSPECTIVE.create(window);
        fActionResetPerspective.setText(Messages.ArchimateEditorActionBarAdvisor_2);
        register(fActionResetPerspective);
        
        // Toggle Coolbar
        fActionToggleCoolbar = new ShowToolbarAction();
        
        // Show Views
        fShowModelsView = new ToggleViewAction(ITreeModelView.NAME, ITreeModelView.ID,
                "com.archimatetool.editor.action.showTreeModelView", ITreeModelView.IMAGE_DESCRIPTOR); //$NON-NLS-1$
        register(fShowModelsView);
        
        fShowPropertiesView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_3, ViewManager.PROPERTIES_VIEW,
                "com.archimatetool.editor.action.showPropertiesView", IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON)); //$NON-NLS-1$
        register(fShowPropertiesView);
        
        fShowOutlineView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_4, ViewManager.OUTLINE_VIEW,
                "com.archimatetool.editor.action.showOutlineView", IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_OUTLINE_VIEW_ICON)); //$NON-NLS-1$
        register(fShowOutlineView);
        
        fShowNavigatorView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_5, INavigatorView.ID,
                "com.archimatetool.editor.action.showNavigatorView", INavigatorView.IMAGE_DESCRIPTOR); //$NON-NLS-1$
        register(fShowNavigatorView);

        fShowPaletteView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_6, PaletteView.ID,
                                    "com.archimatetool.editor.action.showPaletteView", //$NON-NLS-1$
                                    AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.gef", //$NON-NLS-1$
                                    "$nl$/icons/palette_view.gif")) { //$NON-NLS-1$
            @Override
            public String getToolTipText() {
                return Messages.ArchimateEditorActionBarAdvisor_7;
            };
        };
        register(fShowPaletteView);
        
        // Show Relationships matrix dialog
        fActionShowRelationsMatrix = new Action(Messages.ArchimateEditorActionBarAdvisor_17) {
            @Override
            public void run() {
                RelationshipsMatrixDialog dialog = new RelationshipsMatrixDialog(window.getShell());
                dialog.open();
            }
        };
        
        // Archi website
        fArchiWebsiteAction = new ArchiWebsiteAction();
        
        // Check for new Version
        fActionCheckForNewVersion = new CheckForNewVersionAction();
     }
    
    @Override
    protected void fillMenuBar(IMenuManager menuBar) {
        // File
        menuBar.add(createFileMenu());

        // Edit
        menuBar.add(createEditMenu());
        
        // Window
        menuBar.add(createWindowMenu());

        // Help
        menuBar.add(createHelpMenu());
    }
    
    /**
     * Create the File menu
     * @return
     */
    private MenuManager createFileMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        
        MenuManager menu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_8, IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

        // New
        MenuManager newMenu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_9, "new_menu"); //$NON-NLS-1$
        menu.add(newMenu);
        newMenu.add(fActionNewArchimateModel);
        newMenu.add(new GroupMarker("new_menu.ext")); //$NON-NLS-1$
        menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
        
        menu.add(fActionOpenModel);
        
        // Open Recent
        MenuManager openRecentMenu = new MRUMenuManager(window);
        menu.add(openRecentMenu);
        
        menu.add(new Separator());
        
        menu.add(fActionOpenDiagram);
        menu.add(fActionCloseModel);
        menu.add(fActionCloseEditor);
        menu.add(fActionCloseAllEditors);
        menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
        menu.add(new Separator());

        menu.add(fActionSave);
        menu.add(fActionSaveAs);
        menu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
        menu.add(new Separator());
        
        menu.add(fActionPrint);
        menu.add(new Separator());
        
        MenuManager importMenu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_10, "import_menu"); //$NON-NLS-1$
        menu.add(importMenu);
        addImportModelExtensionMenuItems(window, importMenu);
        importMenu.add(new GroupMarker("import_ext")); //$NON-NLS-1$
        importMenu.add(new Separator());
        
        MenuManager exportMenu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_11, "export_menu"); //$NON-NLS-1$
        menu.add(exportMenu);
        addExportModelExtensionMenuItems(window, exportMenu);
        exportMenu.add(new GroupMarker("export_ext")); //$NON-NLS-1$
        exportMenu.add(new Separator());
        
        MenuManager reportMenu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_12, "report_menu"); //$NON-NLS-1$
        menu.add(reportMenu);
        reportMenu.add(new GroupMarker("report_ext")); //$NON-NLS-1$
        
        menu.add(new Separator());
        
        menu.add(fActionProperties);
        menu.add(new Separator());
        
        // Not needed on a Mac
        if(!PlatformUtils.isMac()) {
            menu.add(fActionQuit);
        }
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

        return menu;
    }

    /**
     * Create the Editor menu
     * @return
     */
    private MenuManager createEditMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        
        MenuManager menu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_13, IWorkbenchActionConstants.M_EDIT);
        menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
        
        menu.add(fActionUndo);
        menu.add(fActionRedo);
        menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
        menu.add(new Separator());
        
        menu.add(fActionCut);
        menu.add(fActionCopy);
        menu.add(fActionPaste);
        menu.add(fActionDelete);
        menu.add(new Separator(IWorkbenchActionConstants.CUT_EXT));
        
        menu.add(fActionDuplicate);
        menu.add(fActionRename);
        menu.add(new Separator(fActionRename.getId()));
        
        menu.add(fActionSelectAll);
        menu.add(new Separator(fActionSelectAll.getId()));
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        
        /*
         * On a Mac, Eclipse adds a "Preferences" menu item under the application menu bar.
         * However, it does nothing unless you add the Preferences menu item manually elsewhere.
         * See - http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg30749.html
         * 
         */
        IWorkbenchAction preferenceAction = ActionFactory.PREFERENCES.create(window);
        ActionContributionItem item = new ActionContributionItem(preferenceAction);
        item.setVisible(!PlatformUtils.isMac());
        
        menu.add(new Separator());
        menu.add(item);

        menu.add(new Separator(IWorkbenchActionConstants.EDIT_END));
        return menu;
    }

    /**
     * Create the Window menu
     * @return
     */
    private MenuManager createWindowMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();

        MenuManager menu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_14, IWorkbenchActionConstants.M_WINDOW);

        //MenuManager perspectiveMenu = new MenuManager(Messages.LDAuthorActionBarAdvisor_10, "openPerspective"); //$NON-NLS-1$
        //IContributionItem perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        //perspectiveMenu.add(perspectiveList);
        //menu.add(perspectiveMenu);
        
        //MenuManager showViewMenu = new MenuManager(Messages.LDAuthorActionBarAdvisor_11);
        //menu.add(showViewMenu);

        //IContributionItem viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        //showViewMenu.add(viewList);

        //menu.add(new Separator("PerspectiveMenu")); //$NON-NLS-1$
        
        menu.add(fShowModelsView);
        menu.add(fShowPropertiesView);
        menu.add(fShowOutlineView);
        menu.add(fShowNavigatorView);
        menu.add(fShowPaletteView);
        menu.add(new GroupMarker("show_view_append")); //$NON-NLS-1$
        menu.add(new GroupMarker("show_view_end")); //$NON-NLS-1$
        menu.add(new Separator());

        menu.add(fActionResetPerspective);
        menu.add(fActionToggleCoolbar);

        menu.add(new Separator("nav")); //$NON-NLS-1$

        MenuManager navigationMenu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_15);
        menu.add(navigationMenu);

        IAction a = ActionFactory.NEXT_EDITOR.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.PREVIOUS_EDITOR.create(window);
        register(a);
        navigationMenu.add(a);

        navigationMenu.add(new Separator());

        a = ActionFactory.NEXT_PART.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.PREVIOUS_PART.create(window);
        register(a);
        navigationMenu.add(a);

        menu.add(ContributionItemFactory.OPEN_WINDOWS.create(window));

        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        
        return menu;
    }

    /**
     * Create the Help menu
     * @return
     */
    private MenuManager createHelpMenu() {
        IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
        
        MenuManager menu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_16, IWorkbenchActionConstants.M_HELP);
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
        
        menu.add(ActionFactory.HELP_CONTENTS.create(window));
        menu.add(ActionFactory.HELP_SEARCH.create(window));
        menu.add(ActionFactory.DYNAMIC_HELP.create(window));
        
        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        
        menu.add(fArchiWebsiteAction);
        menu.add(fActionCheckForNewVersion);
        
        menu.add(new Separator());
        menu.add(fActionShowRelationsMatrix);

        /*
         * On a Mac, Eclipse adds an "About" menu item under the application menu bar.
         * However, it does nothing unless you add the About menu item manually elsewhere.
         * See - http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg30749.html
         * 
         */
        ActionContributionItem item = new ActionContributionItem(fActionAbout);
        item.setVisible(!PlatformUtils.isMac());
        
        menu.add(new Separator());
        menu.add(item);

        return menu;
    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBarManager) {
        IToolBarManager toolBarFile = new ToolBarManager(SWT.FLAT);
        coolBarManager.add(new ToolBarContributionItem(toolBarFile, "toolbar_file")); //$NON-NLS-1$
        
        toolBarFile.add(new GroupMarker("start")); //$NON-NLS-1$
        
        toolBarFile.add(fActionOpenModel);
        toolBarFile.add(fActionSave);
        toolBarFile.add(new GroupMarker("end")); //$NON-NLS-1$

        IToolBarManager toolBarEdit = new ToolBarManager(SWT.FLAT);
        coolBarManager.add(new ToolBarContributionItem(toolBarEdit, "toolbar_edit")); //$NON-NLS-1$
        
        toolBarEdit.add(new GroupMarker("start")); //$NON-NLS-1$
        toolBarEdit.add(fActionUndo);
        toolBarEdit.add(fActionRedo);
        toolBarEdit.add(new Separator());
        toolBarEdit.add(fActionCut);
        toolBarEdit.add(fActionCopy);
        toolBarEdit.add(fActionPaste);
        toolBarEdit.add(fActionDelete);
        toolBarEdit.add(new GroupMarker("end")); //$NON-NLS-1$
        
        IToolBarManager toolBarViews = new ToolBarManager(SWT.FLAT);
        coolBarManager.add(new ToolBarContributionItem(toolBarViews, "toolbar_views")); //$NON-NLS-1$
        
        toolBarViews.add(new GroupMarker("start")); //$NON-NLS-1$
        toolBarViews.add(fShowModelsView);
        toolBarViews.add(fShowPropertiesView);
        toolBarViews.add(fShowOutlineView);
        toolBarViews.add(fShowNavigatorView);
        toolBarViews.add(fShowPaletteView);
        toolBarViews.add(new GroupMarker("append")); //$NON-NLS-1$
        toolBarViews.add(new GroupMarker("end")); //$NON-NLS-1$
        toolBarViews.add(new Separator());
        
        // If System Property to VM arguments is "-Dshowheap=true" then Show Heap Widget
        if("true".equals(System.getProperty("showheap"))) { //$NON-NLS-1$ //$NON-NLS-2$
            IToolBarManager toolBarTools = new ToolBarManager(SWT.FLAT);
            toolBarTools.add(new HeapStatusWidgetToolBarContributionItem());
            coolBarManager.add(new ToolBarContributionItem(toolBarTools, "toolbar_tools")); //$NON-NLS-1$
        }
    }

    /**
     * Add any contributed export model menu items
     */
    private void addExportModelExtensionMenuItems(IWorkbenchWindow window, IMenuManager exportMenu) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor("com.archimatetool.editor.exportHandler")) { //$NON-NLS-1$
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                String label = configurationElement.getAttribute("label"); //$NON-NLS-1$
                IModelExporter exporter = (IModelExporter)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && label != null && exporter != null) {
                    ExportModelAction action = new ExportModelAction(window, id, label, exporter);
                    exportMenu.add(action);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    /**
     * Add any contributed import model menu items
     */
    private void addImportModelExtensionMenuItems(IWorkbenchWindow window, IMenuManager importMenu) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor("com.archimatetool.editor.importHandler")) { //$NON-NLS-1$
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                String label = configurationElement.getAttribute("label"); //$NON-NLS-1$
                IModelImporter importer = (IModelImporter)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && label != null && importer != null) {
                    ImportModelAction action = new ImportModelAction(window, id, label, importer);
                    importMenu.add(action);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
}
