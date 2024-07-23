/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.search;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;
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
    private Set<Object> fExpandedObjects;
    
    private IAction fActionFilterName;
    private IAction fActionFilterDoc;
    
    private MenuManager fPropertiesMenu;
    private MenuManager fSpecializationsMenu;
    
    private List<IAction> fObjectActions = new ArrayList<>();
    
    private Timer fKeyDelayTimer;
    
    private static int TIMER_DELAY = 600;
    
    // Hook into the global edit Action Handlers and null them when the text control has the focus
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
            fExpandedObjects.add(event.getElement());
        }
        
        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            fExpandedObjects.remove(event.getElement());
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
        
        setupToolBar();
        setupSearchTextWidget();
    }
    
    @Override
    public boolean setFocus() {
        // On Linux the text control can be disposed at this point
        return fSearchText.isDisposed() ? false : fSearchText.setFocus();
    }

    private void setupSearchTextWidget() {
        fSearchText = UIUtils.createSingleTextControl(this, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH, false);
        fSearchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Mac bug workaround
        UIUtils.applyMacUndoBugFilter(fSearchText);
        
        // Use auto search
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.TREE_SEARCH_AUTO)) {
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
                        Display.getDefault().syncExec(() -> { // This has to run in the UI thread
                            if(!fSearchText.isDisposed()) {
                                fSearchFilter.setSearchText(fSearchText.getText());
                                refreshTree();
                            }
                        });
                    }
                }, TIMER_DELAY);
            });
        }
        // Search on Return key press
        else {
            fSearchText.addListener(SWT.DefaultSelection, event -> {
                fSearchFilter.setSearchText(fSearchText.getText());
                refreshTree();
            });
        }
        
        // Hook into the global edit Action Handlers and null them when the text control has the focus
        fSearchText.addListener(SWT.Activate, textControlListener);
        fSearchText.addListener(SWT.Deactivate, textControlListener);
    }

    private void setupToolBar() {
        fActionFilterName = new Action(Messages.SearchWidget_0, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setFilterOnName(isChecked());
            	refreshTree();
            };
        };
        fActionFilterName.setToolTipText(Messages.SearchWidget_1);
        fActionFilterName.setChecked(true);
        
        fActionFilterDoc = new Action(Messages.SearchWidget_2, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setFilterOnDocumentation(isChecked());
            	refreshTree();
            }
        };
        fActionFilterDoc.setToolTipText(Messages.SearchWidget_3);

        final ToolBarManager toolBarmanager = new ToolBarManager(SWT.FLAT);
        toolBarmanager.createControl(this);

        AbstractDropDownAction dropDownAction = new AbstractDropDownAction(Messages.SearchWidget_4) {
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FILTER);
            }
        };
        toolBarmanager.add(dropDownAction);

        // Name & Documentation
        dropDownAction.add(fActionFilterName);
        dropDownAction.add(fActionFilterDoc);
        
        // Properties
        fPropertiesMenu = new MenuManager(Messages.SearchWidget_5);
        dropDownAction.add(fPropertiesMenu);
        populatePropertiesMenu();
        
        // Specializations
        fSpecializationsMenu = new MenuManager(Messages.SearchWidget_16);
        dropDownAction.add(fSpecializationsMenu);
        populateSpecializationsMenu();
        
        dropDownAction.add(new Separator());
        
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
        
        dropDownAction.add(new Separator());
        
        IAction action = new Action(Messages.SearchWidget_12, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setShowAllFolders(isChecked());
            	refreshTree();
            }
        };
        action.setChecked(fSearchFilter.isShowAllFolders());
        dropDownAction.add(action);
        
        dropDownAction.add(new Separator());
        
        action = new Action(Messages.SearchWidget_13) {
            @Override
            public void run() {
            	reset();
            }
        };
        dropDownAction.add(action);
        
        toolBarmanager.update(true);
    }
    
    private void reset() {
        // Clear Documentation
        fActionFilterDoc.setChecked(false);

        // Clear Objects
        for(IAction action : fObjectActions) {
            action.setChecked(false);
        }

        // Clear & Reset Properties sub-menus
        fPropertiesMenu.removeAll();
        populatePropertiesMenu();
        
        // Clear & Reset Specializations sub-menus
        fSpecializationsMenu.removeAll();
        populateSpecializationsMenu();

        // Filter on name
        fActionFilterName.setChecked(true);

        fSearchFilter.reset();
        refreshTree();
    }
    
    public void softReset() {
        // Clear & Reset Properties and Specializations
        fPropertiesMenu.removeAll();
        populatePropertiesMenu();
        populateSpecializationsMenu();
        fSearchFilter.resetPropertiesFilter();
        refreshTree();
    }

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
        
        fObjectActions.add(action);
        
        return action;
    }

	private void populatePropertiesMenu() {
	    // Models that are loaded are the ones in the Models Tree
	    Set<String> set = new LinkedHashSet<>(); // LinkedHashSet is faster when sorting

	    for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
	        getAllUniquePropertyKeysForModel(model, set);
	    }
	    
	    List<String> list = new ArrayList<>(set);
	    
	    // Sort alphabetically, but don't use Collator.getInstance() as it's too slow
	    list.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
	    
        // Limit to a sensible menu size
        if(list.size() > 1000) {
            list = list.subList(0, 999);
        }

	    for(String key : list) {
	        IAction action = new Action(key, IAction.AS_CHECK_BOX) {
	            @Override
	            public void run() {
	                if(isChecked()) {
	                    fSearchFilter.addPropertiesFilter(key);
	                }
	                else {
	                    fSearchFilter.removePropertiesFilter(key);
	                }
	                refreshTree();
	            }
	        };

	        fPropertiesMenu.add(action);
	    }

	    fPropertiesMenu.update(true);
	}

    private void getAllUniquePropertyKeysForModel(IArchimateModel model, Set<String> set) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
            	String key = ((IProperty)element).getKey();
            	if(StringUtils.isSetAfterTrim(key)) {
            	    set.add(key);
            	}
            }
        }
    }
    
    private void populateSpecializationsMenu() {
        // Models that are loaded are the ones in the Models Tree
        List<IProfile> profiles = new ArrayList<>();

        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
            for(Entry<IProfile, List<IProfiles>> entry : ArchimateModelUtils.findProfilesUsage(model).entrySet()) {
                if(!hasProfile(profiles, entry.getKey())) {
                    profiles.add(entry.getKey());
                }
            }
        }
        
        // Sort alphabetically
        Collator collator = Collator.getInstance();
        profiles.sort((p1, p2) -> collator.compare(p1.getName(), p2.getName()));

        for(final IProfile profile : profiles) {
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
                    return PlatformUtils.isWindows11() ? null : ArchiLabelProvider.INSTANCE.getImageDescriptor(profile.getConceptClass());
                }
            };

            fSpecializationsMenu.add(action);
        }

        fSpecializationsMenu.update(true);
    }
    
    private boolean hasProfile(List<IProfile> profiles, IProfile profile) {
        for(IProfile p : profiles) {
            if(ArchimateModelUtils.isMatchingProfile(p, profile)) {
                return true;
            }
        }
        
        return false;
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
        fExpandedObjects = new HashSet<>(Arrays.asList(fViewer.getVisibleExpandedElements()));  // only the visible ones
    }

    private void restoreTreeState() {
        if(fExpandedObjects != null) {
            try {
                fViewer.getTree().setRedraw(false); // Need this on Windows
                fViewer.setExpandedElements(fExpandedObjects.toArray());
            }
            finally {
                fViewer.getTree().setRedraw(true);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        
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
        
        fExpandedObjects = null;
        fViewer = null;
        fSearchFilter = null;
        fObjectActions = null;
    }
}
