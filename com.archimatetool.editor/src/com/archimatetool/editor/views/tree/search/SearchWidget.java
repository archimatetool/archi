/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.search;

import java.beans.PropertyChangeEvent;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.actions.AbstractDropDownAction;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.GlobalActionDisablementHandler;
import com.archimatetool.editor.ui.dialog.UserPropertiesKeySelectionDialog;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Search Widget
 * 
 * @author Phillip Beauvoir
 */
public class SearchWidget extends Composite {

    private Text fSearchText;
    
    private SearchFilter fSearchFilter;
    private TreeViewer fViewer;
    private Set<Object> fExpandedTreeObjects;
    
    private IAction fActionFilterName;
    private IAction fActionFilterDocumentation;
    private IAction fActionFilterPropertyValues;
    private IAction fActionFilterViews;
    private IAction fActionShowAllFolders;
    private IAction fActionMatchCase;
    private IAction fActionUseRegex;
    
    private List<IAction> fConceptActions = new ArrayList<>();
    
    private MenuManager fSpecializationsMenu;
    
    private Timer fKeyDelayTimer;
    
    private static int TIMER_DELAY = 600;
    
    private static final Color ERROR_COLOR = new Color(255, 0, 0);
    private static final String ERROR_COLOR_CSS = "color: RGB(255, 0, 0);"; //$NON-NLS-1$
    
    /**
     * Hook into the global edit Action Handlers and null them when the text control has the focus
     */
    private Listener textControlListener = new Listener() {
        private GlobalActionDisablementHandler globalActionHandler;
        
        @Override
        public void handleEvent(Event event) {
            switch(event.type) {
                case SWT.Activate:
                    globalActionHandler = new GlobalActionDisablementHandler();
                    globalActionHandler.clearGlobalActions();
                    break;

                case SWT.Deactivate:
                    if(globalActionHandler != null) {
                        globalActionHandler.restoreGlobalActions();
                    }
                    break;

                default:
                    break;
            }
        }
    };
    
    /**
     * Track user expanded/collapsed nodes so we can restore the tree to that state.
     * Note this does not notify on programmatic expansion.
     */
    private ITreeViewerListener treeExpansionListener = new ITreeViewerListener() {
        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            fExpandedTreeObjects.add(event.getElement());
        }
        
        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            fExpandedTreeObjects.remove(event.getElement());
        }
    };
    
    public SearchWidget(TreeViewer viewer) {
        super(viewer.getTree().getParent(), SWT.NULL);
        
        fViewer = viewer;
        saveTreeState(); // save this now
        
        fSearchFilter = new SearchFilter();
        fViewer.addFilter(fSearchFilter);
        
        fViewer.addTreeListener(treeExpansionListener);
        
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        createToolBar();
        createSearchTextWidget();
        
        // Save preferences here as dispose() is not called when parent is disposed()
        addDisposeListener(e -> {
            savePreferences();
        });
    }
    
    @Override
    public boolean setFocus() {
        // On Linux the text control can be disposed at this point
        return fSearchText.isDisposed() ? false : fSearchText.setFocus();
    }

    private void createSearchTextWidget() {
        fSearchText = UIUtils.createSingleTextControl(this, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH, false);
        fSearchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Bug on Mac, single-click doesn't work on the cancel icon
        UIUtils.applyMacCancelIconListener(fSearchText);
        
        // Mac bug workaround
        UIUtils.applyMacUndoBugFilter(fSearchText);
        
        // Use auto search
        if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.TREE_SEARCH_AUTO)) {
            fSearchText.addModifyListener(event -> {
                // If we have a timer cancel it
                if(fKeyDelayTimer != null) {
                    fKeyDelayTimer.cancel();
                }

                // Set this to run with a short delay to allow for key presses
                fKeyDelayTimer = new Timer();
                fKeyDelayTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // This has to run in the UI thread
                        Display.getDefault().syncExec(() -> updateSearch());
                    }
                }, TIMER_DELAY);
            });
        }
        // Search on Return key press
        else {
            fSearchText.addListener(SWT.DefaultSelection, event -> {
                updateSearch();
            });
        }
        
        // Hook into the global edit Action Handlers and null them when the text control has the focus
        fSearchText.addListener(SWT.Activate, textControlListener);
        fSearchText.addListener(SWT.Deactivate, textControlListener);
    }
    
    private void updateSearch() {
        if(!fSearchText.isDisposed()) {
            fSearchFilter.setSearchText(fSearchText.getText());
            setValidSearchTextHint();
            refreshTree();
        }
    }
    
    /**
     * If we're using regex check that it's valid regex and set color to red and tooltip if not
     */
    private void setValidSearchTextHint() {
        boolean validSearchText = fSearchFilter.isValidSearchString();
        
        // Set color using css theme string *and* normal color
        // Normal color is needed for no theming and, for some reason, the light theme
        String cssColor = validSearchText ? null : ERROR_COLOR_CSS;
        if(!Objects.equals(fSearchText.getData("style"), cssColor)) { //$NON-NLS-1$
            fSearchText.setForeground(validSearchText ? null : ERROR_COLOR);
            fSearchText.setData("style", cssColor); //$NON-NLS-1$
            fSearchText.reskin(SWT.NONE);
        }
        
        fSearchText.setToolTipText(validSearchText ? null : Messages.SearchWidget_20);
    }

    private void createToolBar() {
        // Create a Toolbar
        ToolBarManager toolBarmanager = new ToolBarManager(SWT.FLAT);
        toolBarmanager.createControl(this);

        // And a Dropdown
        AbstractDropDownAction dropDownAction = new AbstractDropDownAction(Messages.SearchWidget_4) {
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FILTER);
            }
        };
        toolBarmanager.add(dropDownAction);

        // Name
        fActionFilterName = new Action(Messages.SearchWidget_0, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fSearchFilter.setFilterOnName(isChecked());
                refreshTree();
            };
        };
        fActionFilterName.setToolTipText(Messages.SearchWidget_1);
        dropDownAction.add(fActionFilterName);
        
        // Documentation
        fActionFilterDocumentation = new Action(Messages.SearchWidget_2, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fSearchFilter.setFilterOnDocumentation(isChecked());
                refreshTree();
            }
        };
        fActionFilterDocumentation.setToolTipText(Messages.SearchWidget_3);
        dropDownAction.add(fActionFilterDocumentation);
        
        // Property Values
        fActionFilterPropertyValues = new Action(Messages.SearchWidget_21, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fSearchFilter.setFilterOnPropertyValues(isChecked());
                refreshTree();
            };
        };
        fActionFilterPropertyValues.setToolTipText(Messages.SearchWidget_22);
        dropDownAction.add(fActionFilterPropertyValues);
        
        // Property Keys
        IAction actionProperties = new Action(Messages.SearchWidget_5, IAction.AS_PUSH_BUTTON) {
            @Override
            public void run() {
                UserPropertiesKeySelectionDialog dialog = new UserPropertiesKeySelectionDialog(getShell(), getAllUniquePropertyKeys(),
                        new ArrayList<>(fSearchFilter.getPropertyKeyFilter()));
                if(dialog.open() != Window.CANCEL) {
                    fSearchFilter.resetPropertyKeyFilter();
                    for(String key : dialog.getSelectedKeys()) {
                        fSearchFilter.addPropertyKeyFilter(key);
                    }
                    refreshTree();
                }
            }
        };
        actionProperties.setToolTipText(Messages.SearchWidget_23);
        dropDownAction.add(actionProperties);
        
        dropDownAction.add(new Separator());
        
        // Specializations
        fSpecializationsMenu = new MenuManager(Messages.SearchWidget_16);
        dropDownAction.add(fSpecializationsMenu);
        populateSpecializationsMenu();
        
        // Concept sub-menus
        MenuManager strategyMenu = new MenuManager(Messages.SearchWidget_15);
        dropDownAction.add(strategyMenu);
        for(EClass eClass : ArchimateModelUtils.getStrategyClasses()) {
            strategyMenu.add(createConceptAction(eClass));
        }
        
        MenuManager businessMenu = new MenuManager(Messages.SearchWidget_6);
        dropDownAction.add(businessMenu);
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            businessMenu.add(createConceptAction(eClass));
        }
        
        MenuManager applicationMenu = new MenuManager(Messages.SearchWidget_7);
        dropDownAction.add(applicationMenu);
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            applicationMenu.add(createConceptAction(eClass));
        }
        
        MenuManager technologyPhysicalMenu = new MenuManager(Messages.SearchWidget_8);
        dropDownAction.add(technologyPhysicalMenu);
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            technologyPhysicalMenu.add(createConceptAction(eClass));
        }
        technologyPhysicalMenu.add(new Separator());
        for(EClass eClass : ArchimateModelUtils.getPhysicalClasses()) {
            technologyPhysicalMenu.add(createConceptAction(eClass));
        }
        
        MenuManager motivationMenu = new MenuManager(Messages.SearchWidget_9);
        dropDownAction.add(motivationMenu);
        for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
            motivationMenu.add(createConceptAction(eClass));
        }

        MenuManager implementationMenu = new MenuManager(Messages.SearchWidget_10);
        dropDownAction.add(implementationMenu);
        for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
            implementationMenu.add(createConceptAction(eClass));
        }

        MenuManager otherMenu = new MenuManager(Messages.SearchWidget_14);
        dropDownAction.add(otherMenu);
        for(EClass eClass : ArchimateModelUtils.getOtherClasses()) {
            otherMenu.add(createConceptAction(eClass));
        }
        otherMenu.add(new Separator());
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            otherMenu.add(createConceptAction(eClass));
        }

        MenuManager relationsMenu = new MenuManager(Messages.SearchWidget_11);
        dropDownAction.add(relationsMenu);
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            relationsMenu.add(createConceptAction(eClass));
        }
        
        // Views
        fActionFilterViews = new Action(Messages.SearchWidget_17, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fSearchFilter.setFilterViews(isChecked());
                refreshTree();
            }
        };
        fActionFilterViews.setToolTipText(Messages.SearchWidget_24);
        dropDownAction.add(fActionFilterViews);

        dropDownAction.add(new Separator());
        
        // Show All Folders
        fActionShowAllFolders = new Action(Messages.SearchWidget_12, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setShowAllFolders(isChecked());
            	refreshTree();
            }
        };
        fActionShowAllFolders.setToolTipText(Messages.SearchWidget_25);
        dropDownAction.add(fActionShowAllFolders);
        
        // Match Case
        fActionMatchCase = new Action(Messages.SearchWidget_18, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fSearchFilter.setMatchCase(isChecked());
                refreshTree();
            }
        };
        fActionMatchCase.setToolTipText(Messages.SearchWidget_26);
        dropDownAction.add(fActionMatchCase);
        
        // Regex
        fActionUseRegex = new Action(Messages.SearchWidget_19, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                fSearchFilter.setUseRegex(isChecked());
                setValidSearchTextHint();
                refreshTree();
            }
        };
        fActionUseRegex.setToolTipText(Messages.SearchWidget_27);
        dropDownAction.add(fActionUseRegex);

        dropDownAction.add(new Separator());
        
        // Reset
        IAction actionReset = new Action(Messages.SearchWidget_13) {
            @Override
            public void run() {
            	reset();
            }
        };
        dropDownAction.add(actionReset);
        
        // Refresh
        IAction actionRefresh = new Action(Messages.SearchWidget_28) {
            @Override
            public void run() {
                if(fSearchFilter.isFiltering()) {
                    refreshTree();
                }
            }
        };
        dropDownAction.add(actionRefresh);
        
        loadPreferences();
        
        // Need to update toolbar manager now
        toolBarmanager.update(true);
    }
    
    private void loadPreferences() {
        IPreferenceStore store = ArchiPlugin.getInstance().getPreferenceStore();
        
        fActionFilterName.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_NAME));
        fSearchFilter.setFilterOnName(fActionFilterName.isChecked());
        
        fActionFilterDocumentation.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_DOCUMENTATION));
        fSearchFilter.setFilterOnDocumentation(fActionFilterDocumentation.isChecked());
        
        fActionFilterPropertyValues.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_PROPETY_VALUES));
        fSearchFilter.setFilterOnPropertyValues(fActionFilterPropertyValues.isChecked());
        
        fActionFilterViews.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_VIEWS));
        fSearchFilter.setFilterViews(fActionFilterViews.isChecked());
        
        fActionShowAllFolders.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_SHOW_ALL_FOLDERS));
        fSearchFilter.setShowAllFolders(fActionShowAllFolders.isChecked());

        fActionMatchCase.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_MATCH_CASE));
        fSearchFilter.setMatchCase(fActionMatchCase.isChecked());
        
        fActionUseRegex.setChecked(store.getBoolean(IPreferenceConstants.SEARCHFILTER_USE_REGEX));
        fSearchFilter.setUseRegex(fActionUseRegex.isChecked());
    }
    
    private void savePreferences() {
        if(fSearchFilter == null) {
            return;
        }
        
        IPreferenceStore store = ArchiPlugin.getInstance().getPreferenceStore();
        store.setValue(IPreferenceConstants.SEARCHFILTER_NAME, fSearchFilter.getFilterOnName());
        store.setValue(IPreferenceConstants.SEARCHFILTER_DOCUMENTATION, fSearchFilter.getFilterOnDocumentation());
        store.setValue(IPreferenceConstants.SEARCHFILTER_PROPETY_VALUES, fSearchFilter.getFilterOnPropertyValues());
        store.setValue(IPreferenceConstants.SEARCHFILTER_VIEWS, fSearchFilter.isFilteringViews());
        store.setValue(IPreferenceConstants.SEARCHFILTER_SHOW_ALL_FOLDERS, fSearchFilter.getShowAllFolders());
        store.setValue(IPreferenceConstants.SEARCHFILTER_MATCH_CASE, fSearchFilter.getMatchCase());
        store.setValue(IPreferenceConstants.SEARCHFILTER_USE_REGEX, fSearchFilter.getUseRegex());
    }
    
    /**
     * Reset all filters, properties, specializations
     */
    private void reset() {
        // Filter on Name menu item
        fActionFilterName.setChecked(true);

        // Don't filter on Documentation menu item
        fActionFilterDocumentation.setChecked(false);
        
        // Don't filter on Property Values menu item
        fActionFilterPropertyValues.setChecked(false);

        // Clear & uncheck Specializations menu items
        populateSpecializationsMenu();

        // Uncheck Concept menu items
        for(IAction action : fConceptActions) {
            action.setChecked(false);
        }
        
        // Views
        fActionFilterViews.setChecked(false);

        // This will set name filter true, documentation filter false, and clear concepts, properties, and specializations filters 
        fSearchFilter.reset();
        
        refreshTree();
    }
    
    /**
     * Update this search filter based on Notification
     * @param msg The Notification to respond to
     */
    public void update(Notification msg) {
        if(isDisposed()) {
            return;
        }
        
        Object feature = msg.getFeature();
        
        // Model Profiles added/deleted/changed
        if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__PROFILES) {
            // Repopulate the sub-menu
            populateSpecializationsMenu();
            
            // If filtering on Specializations reset filter and refresh tree
            if(fSearchFilter.isFilteringSpecializations()) {
                fSearchFilter.resetSpecializationsFilter();
                refreshTree();
            }
        }
        // Profile added/removed from a Concept or updated
        else if(feature == IArchimatePackage.Literals.PROFILES__PROFILES || msg.getNotifier() instanceof IProfile) {
            // If filtering on Specializations() refresh tree
            if(fSearchFilter.isFilteringSpecializations()) {
                refreshTree();
            }
        }
    }
    
    /**
     * Update this search filter based on Notifications
     * @param notifications The Notifications to respond to
     */
    public void update(List<Notification> notifications) {
        for(Notification msg : notifications) {
            update(msg);
        }
    }
    
    /**
     * Update this search filter based on PropertyChangeEvent
     * @param evt The PropertyChangeEvent to respond to
     */
    public void update(PropertyChangeEvent evt) {
        if(isDisposed()) {
            return;
        }
        
        // Model was opened or closed
        if(evt.getPropertyName() == IEditorModelManager.PROPERTY_MODEL_OPENED 
                                               || evt.getPropertyName() == IEditorModelManager.PROPERTY_MODEL_REMOVED) {
            // Reset Property keys
            fSearchFilter.resetPropertyKeyFilter();
            
            // Reset Specializations
            populateSpecializationsMenu();
            fSearchFilter.resetSpecializationsFilter();
            
            // Refresh
            refreshTree();
        }
    }
    
	/**
	 * Create a concept action based on eClass
	 */
	private IAction createConceptAction(final EClass eClass) {
        IAction action = new Action(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                if(isChecked()) {
                    fSearchFilter.addConceptFilter(eClass);
                }
                else {
                    fSearchFilter.removeConceptFilter(eClass);
                }
                refreshTree();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                // Windows 11 doen't show a tick if we use an image in the menu
                return PlatformUtils.isWindows11() ? null : ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass);
            }
        };
        
        fConceptActions.add(action);
        
        return action;
    }

    private List<String> getAllUniquePropertyKeys() {
        // Maximum amount of items to display when getting all unique keys
        final int MAX_ITEMS = 1000000;
        
        Set<String> set = new LinkedHashSet<>(); // LinkedHashSet is faster when sorting
        
        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
            for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
                EObject element = iter.next();
                if(element instanceof IProperty property) {
                    String key = property.getKey();
                    if(StringUtils.isSetAfterTrim(key)) {
                        set.add(key);
                        if(set.size() > MAX_ITEMS) { // Don't get more than this
                            break;
                        }
                    }
                }
            }
        }
        
        List<String> list = new ArrayList<>(set);
        
        // Sort alphabetically, but don't use Collator.getInstance() as it's too slow
        list.sort((s1, s2) -> s1.compareToIgnoreCase(s2));

        return list;
    }
    
    private void populateSpecializationsMenu() {
        fSpecializationsMenu.removeAll();
        
        List<IProfile> profiles = new ArrayList<>();

        // Models that are loaded are the ones in the Models Tree
        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
            for(IProfile profile : model.getProfiles()) {
                profiles.add(profile);
            }
        }
        
        // Sort Profiles into Elements, then Relations and by name
        ArchimateModelUtils.sortProfiles(profiles, Collator.getInstance());
        
        for(IProfile profile : profiles) {
            IAction action = new Action(profile.getName(), IAction.AS_CHECK_BOX) {
                @Override
                public void run() {
                    if(isChecked()) {
                        fSearchFilter.addSpecializationsFilter(profile);
                    }
                    else {
                        fSearchFilter.removeSpecializationsFilter(profile);
                    }
                    refreshTree();
                }
                
                @Override
                public ImageDescriptor getImageDescriptor() {
                    // Windows 11 doen't show checked state if we use an image in the menu
                    return PlatformUtils.isWindows11() ? null : ArchiLabelProvider.INSTANCE.getImageDescriptorForSpecialization(profile);
                }
            };

            fSpecializationsMenu.add(action);
        }

        fSpecializationsMenu.update(true);
    }
    
    private void refreshTree() {
        // Not sure if async makes any difference. Expanding/conctracting tree nodes consumes the UI thread anyway
        Display.getCurrent().asyncExec(() -> {
            // fViewer can be null if this portion of code is called asynchronously:
            // 1. Open this SearchWidget with some search text
            // 2. Open or close a model that will take some time. TreeModelView will call softReset().
            // 3. Quickly close this SearchWidget
            // 4. At this point softReset() will have arrived here
            if(fViewer == null || fViewer.getTree().isDisposed()) {
                return;
            }
            
            // Doing this first ensures that TreeViewer.refresh() is faster in many cases, especially on Windows
            fViewer.collapseAll();

            // Refresh the tree before expanding or restoring tree state
            fViewer.refresh();
            
            // If we have something to show expand all nodes
            if(fSearchFilter.isFiltering()) {
                fViewer.expandAll();
            }
            // Else restore the tree
            else {
                restoreTreeState();
            }
        });
    }
    
    private void saveTreeState() {
        fExpandedTreeObjects = new HashSet<>(Arrays.asList(fViewer.getVisibleExpandedElements()));  // only the visible ones
    }

    private void restoreTreeState() {
        if(fExpandedTreeObjects != null) {
            try {
                fViewer.getTree().setRedraw(false); // Need this on Windows
                fViewer.setExpandedElements(fExpandedTreeObjects.toArray());
            }
            finally {
                fViewer.getTree().setRedraw(true);
            }
        }
    }

    /**
     * Close and dispose this widget
     */
    public void close() {
        if(isDisposed()) {
            return;
        }
        
        // This will call dispose listener which will call savePreferences();
        dispose();
        
        fViewer.removeTreeListener(treeExpansionListener);
        
        if(fSearchFilter.isFiltering()) {
            IStructuredSelection selection = fViewer.getStructuredSelection(); // Store this before anything else
            fViewer.collapseAll();               // Doing this first ensures that TreeViewer.removeFilter() is faster in many cases, especially on Windows
            fViewer.removeFilter(fSearchFilter); // Do this before restoring tree state as it calls TreeViewer.refresh()
            restoreTreeState();
            fViewer.setSelection(selection, true);
        }
        else {
            fViewer.removeFilter(fSearchFilter);
        }
        
        fExpandedTreeObjects = null;
        fViewer = null;
        fSearchFilter = null;
        fConceptActions = null;
        fSpecializationsMenu = null;
    }
}
