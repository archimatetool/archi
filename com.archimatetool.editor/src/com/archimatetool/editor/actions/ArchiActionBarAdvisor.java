/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.archimatetool.editor.WorkbenchCleaner;
import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.model.IModelImporter;
import com.archimatetool.editor.model.ISelectedModelImporter;
import com.archimatetool.editor.ui.IArchiImages;
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
public class ArchiActionBarAdvisor
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
    private IWorkbenchAction fActionPasteSpecial;
    private IWorkbenchAction fActionDelete;
    private IWorkbenchAction fActionRename;
    private IWorkbenchAction fActionDuplicate;
    private IWorkbenchAction fActionFindReplace;
    
    private IWorkbenchAction fActionUndo;
    private IWorkbenchAction fActionRedo;

    private IWorkbenchAction fActionSelectAll;
    
    private IWorkbenchAction fActionGenerateView;

    private IWorkbenchAction fActionResetPerspective;
    private IAction fActionToggleCoolbar;
    
    private IAction fShowModelsView;
    private IAction fShowPropertiesView;
    private IAction fShowOutlineView;
    private IAction fShowNavigatorView;
    private IAction fShowPaletteView;
    
    private IAction fActionShowRelationsMatrix;
    
    private IAction fDonateAction;
    private IAction fActionCheckForNewVersion;

    private IAction fActionResetApplication;
    
    /**
     * Constructor
     * @param configurer
     */
    public ArchiActionBarAdvisor(IActionBarConfigurer configurer) {
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
        fActionOpenDiagram = ArchiActionFactory.OPEN_DIAGRAM.create(window);
        register(fActionOpenDiagram);
        
        // Close Model
        fActionCloseModel = ArchiActionFactory.CLOSE_MODEL.create(window);
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
        fActionSave = ArchiActionFactory.SAVE_MODEL.create(window);
        register(fActionSave);
        
        // Save As
        fActionSaveAs = ArchiActionFactory.SAVE_AS.create(window);
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
        
        // Paste Special
        fActionPasteSpecial = ArchiActionFactory.PASTE_SPECIAL.create(window);
        register(fActionPasteSpecial);

        // Delete
        fActionDelete = ArchiActionFactory.DELETE.create(window);
        register(fActionDelete);
        
        // Rename
        fActionRename = ArchiActionFactory.RENAME.create(window);
        register(fActionRename);
        
        // Duplicate
        fActionDuplicate = ArchiActionFactory.DUPLICATE.create(window);
        register(fActionDuplicate);

        // Select All
        fActionSelectAll = ActionFactory.SELECT_ALL.create(window);
        register(fActionSelectAll);
        
        // Find/Replace
        fActionFindReplace = ActionFactory.FIND.create(window);
        register(fActionFindReplace);
        
        // Print
        fActionPrint = ActionFactory.PRINT.create(window);
        register(fActionPrint);
        
        // About
        fActionAbout = ActionFactory.ABOUT.create(window);
        register(fActionAbout);
        
        // Register our own About Handler for our own custom dialog
        IHandlerService srv = window.getService(IHandlerService.class);
        srv.activateHandler(IWorkbenchCommandConstants.HELP_ABOUT, new AboutHandler());
        
        // TODO: Mac 10.15 bug on Eclipse. Remove this hack when Eclipse fixed
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=552514
        if(PlatformUtils.isMac() && PlatformUtils.compareOSVersion("10.15") >= 0) { //$NON-NLS-1$
            // Hook our own action handler for Preferences
            srv.activateHandler(IWorkbenchCommandConstants.WINDOW_PREFERENCES, new AbstractHandler() {
                @Override
                public Object execute(ExecutionEvent event) throws ExecutionException {
                    PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, null, null, null);
                    
                    // Force a resize on the Tree's parent to force a layout
                    Display.getCurrent().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            dialog.getTreeViewer().getControl().getParent().pack();
                        }
                    });
                    
                    dialog.open();
                    return null;
                }
            });
        }
        
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
                "com.archimatetool.editor.action.showPropertiesView", IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON)); //$NON-NLS-1$
        register(fShowPropertiesView);
        
        fShowOutlineView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_4, ViewManager.OUTLINE_VIEW,
                "com.archimatetool.editor.action.showOutlineView", IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_OUTLINE_VIEW_ICON)); //$NON-NLS-1$
        register(fShowOutlineView);
        
        fShowNavigatorView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_5, INavigatorView.ID,
                "com.archimatetool.editor.action.showNavigatorView", INavigatorView.IMAGE_DESCRIPTOR); //$NON-NLS-1$
        register(fShowNavigatorView);

        fShowPaletteView = new ToggleViewAction(Messages.ArchimateEditorActionBarAdvisor_6, PaletteView.ID,
                                    "com.archimatetool.editor.action.showPaletteView", //$NON-NLS-1$
                                    ResourceLocator.imageDescriptorFromBundle("org.eclipse.gef", //$NON-NLS-1$
                                    "$nl$/icons/palette_view.gif").orElse(null)) { //$NON-NLS-1$
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
        fDonateAction = new WebBrowserAction(Messages.ArchimateEditorActionBarAdvisor_18, "https://www.archimatetool.com/donate"); //$NON-NLS-1$
        
        // Check for new Version
        fActionCheckForNewVersion = new CheckForNewVersionAction();
        
        // Reset application
        fActionResetApplication = new Action(Messages.ArchimateEditorActionBarAdvisor_20) {
            @Override
            public void run() {
                WorkbenchCleaner.askResetWorkbench();
            }
        };
        
        // Generate View For Element
        fActionGenerateView = ArchiActionFactory.GENERATE_VIEW.create(window);
        register(fActionGenerateView);
     }
    
    @Override
    protected void fillMenuBar(IMenuManager menuBar) {
        // File
        menuBar.add(createFileMenu());

        // Edit
        menuBar.add(createEditMenu());
        
        // Tools
        menuBar.add(createToolsMenu());
        
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
        
        // Quit action not needed on a Mac
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
        menu.add(fActionPasteSpecial);
        menu.add(fActionDelete);
        menu.add(new Separator(IWorkbenchActionConstants.CUT_EXT));
        
        menu.add(fActionDuplicate);
        menu.add(fActionRename);
        menu.add(new Separator(fActionRename.getId()));
        
        menu.add(fActionSelectAll);
        menu.add(fActionFindReplace);
        menu.add(new Separator(fActionSelectAll.getId()));
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        
        /*
         * On a Mac, there is a "Preferences" menu item under the application menu bar.
         */
        if(!PlatformUtils.isMac()) {
            IWorkbenchAction preferenceAction = ActionFactory.PREFERENCES.create(window);
            ActionContributionItem item = new ActionContributionItem(preferenceAction);
            menu.add(new Separator());
            menu.add(item);
        }

        menu.add(new Separator(IWorkbenchActionConstants.EDIT_END));
        return menu;
    }

    /**
     * Create the Editor menu
     * @return
     */
    private MenuManager createToolsMenu() {
        MenuManager menu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_19, "tools"); //$NON-NLS-1$
        menu.add(new GroupMarker("tools_start")); //$NON-NLS-1$
        
        menu.add(fActionGenerateView);
        
        menu.add(new GroupMarker("tools_end")); //$NON-NLS-1$
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
        menu.add(new Separator("show_view_end")); //$NON-NLS-1$

        menu.add(fActionResetPerspective);
        menu.add(fActionToggleCoolbar);

        menu.add(new Separator("nav_start")); //$NON-NLS-1$

        MenuManager navigationMenu = new MenuManager(Messages.ArchimateEditorActionBarAdvisor_15);
        menu.add(navigationMenu);

        IAction a = ActionFactory.NEXT_EDITOR.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.PREVIOUS_EDITOR.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.SHOW_WORKBOOK_EDITORS.create(window);
        register(a);
        navigationMenu.add(a);
        
        navigationMenu.add(new Separator());

        a = ActionFactory.NEXT_PART.create(window);
        register(a);
        navigationMenu.add(a);

        a = ActionFactory.PREVIOUS_PART.create(window);
        register(a);
        navigationMenu.add(a);
        
        menu.add(new GroupMarker("nav_end")); //$NON-NLS-1$

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
        
        menu.add(ActionFactory.INTRO.create(window));
        menu.add(new Separator());
        
        menu.add(ActionFactory.HELP_CONTENTS.create(window));
        menu.add(ActionFactory.HELP_SEARCH.create(window));
        menu.add(ActionFactory.DYNAMIC_HELP.create(window));
        menu.add(new Separator());
        
        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        
        menu.add(new GroupMarker("support_start")); //$NON-NLS-1$

        menu.add(fDonateAction);
        menu.add(fActionCheckForNewVersion);
        
        menu.add(new Separator("support_end")); //$NON-NLS-1$
        
        menu.add(fActionResetApplication);
        
        menu.add(new Separator());
        
        menu.add(fActionShowRelationsMatrix);

        /*
         * On a Mac, there is an "About" menu item under the application menu bar.
         */
        if(!PlatformUtils.isMac()) {
            ActionContributionItem item = new ActionContributionItem(fActionAbout);
            menu.add(new Separator());
            menu.add(item);
        }
        
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
        
        IToolBarManager toolBarTools = new ToolBarManager(SWT.FLAT);
        coolBarManager.add(new ToolBarContributionItem(toolBarTools, "toolbar_tools")); //$NON-NLS-1$

        // If System Property to VM arguments is "-Dshowheap=true" then Show Heap Widget
        if("true".equals(System.getProperty("showheap"))) { //$NON-NLS-1$ //$NON-NLS-2$
            // BUG on Windows - the Contribution Item Height is not computed unless there is also an ActionContributionItem
            if(PlatformUtils.isWindows()) {
                toolBarTools.add(new Action(" ") { //$NON-NLS-1$
                    @Override
                    public boolean isEnabled() {
                        return false;
                    }
                });
            }
            toolBarTools.add(new HeapStatusWidgetToolBarContributionItem());
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
                String extensionName = configurationElement.getName();
                
                if("importHandler".equals(extensionName)) { //$NON-NLS-1$
                    IModelImporter importer = (IModelImporter)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                    if(id != null && label != null && importer != null) {
                        ImportModelAction action = new ImportModelAction(window, id, label, importer);
                        importMenu.add(action);
                    }
                }
                else if("importHandler2".equals(extensionName)) { //$NON-NLS-1$
                    ISelectedModelImporter importer = (ISelectedModelImporter)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                    if(id != null && label != null && importer != null) {
                        ImportIntoModelAction action = new ImportIntoModelAction(window, id, label, importer);
                        importMenu.add(action);
                    }
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
}
