/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import uk.ac.bolton.archimate.editor.actions.AbstractDropDownAction;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IProperty;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * Search Widget
 * 
 * @author Phillip Beauvoir
 */
public class SearchWidget extends Composite {

    private Control fSearchControl;
    
    private SearchFilter fSearchFilter;
    
    private IAction fActionFilterName;
    private IAction fActionFilterDoc;
    
    private MenuManager fPropertiesMenu;
    
    private List<IAction> fObjectActions = new ArrayList<IAction>();
    
    public SearchWidget(Composite parent, SearchFilter filter) {
        super(parent, SWT.NULL);
        
        fSearchFilter = filter;
        
        GridLayout layout = new GridLayout(2, false);
        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        setupToolBar();
        setupSearchTextWidget();
        
        fSearchFilter.saveState();
    }
    
    @Override
    public boolean setFocus() {
        return fSearchControl.setFocus();
    }

    protected void setupSearchTextWidget() {
        if(PlatformUtils.isWindows()) {
            fSearchControl = new SearchTextWidget(this);
            ((SearchTextWidget)fSearchControl).getTextControl().addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    fSearchFilter.setSearchText(((SearchTextWidget)fSearchControl).getText());
                }
            });
            fSearchControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }
        else {
            fSearchControl = new Text(this, SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
            ((Text)fSearchControl).addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    fSearchFilter.setSearchText(((Text)fSearchControl).getText());
                }
            });
            fSearchControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }
    }

    protected void setupToolBar() {
        fActionFilterName = new Action("Name", IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setFilterOnName(isChecked());
            };
        };
        fActionFilterName.setToolTipText("Search in Name");
        fActionFilterName.setChecked(true);
        fSearchFilter.setFilterOnName(true);
        
        fActionFilterDoc = new Action("Documentation", IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setFilterOnDocumentation(isChecked());
            }
        };
        fActionFilterDoc.setToolTipText("Search in Documentation");

        final ToolBarManager toolBarmanager = new ToolBarManager(SWT.FLAT);
        toolBarmanager.createControl(this);

        AbstractDropDownAction dropDownAction = new AbstractDropDownAction("Filter Options") {
            @Override
            public void run() {
            	showMenu(toolBarmanager);
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_FILTER_16);
            }
        };
        toolBarmanager.add(dropDownAction);

        // Name & Documentation
        dropDownAction.add(fActionFilterName);
        dropDownAction.add(fActionFilterDoc);
        
        // Properties
        fPropertiesMenu = new MenuManager("Properties");
        dropDownAction.add(fPropertiesMenu);
        populatePropertiesMenu(fPropertiesMenu);
        
        dropDownAction.add(new Separator());
        
        MenuManager businessMenu = new MenuManager("Business");
        dropDownAction.add(businessMenu);
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            businessMenu.add(createObjectAction(eClass));
        }
        
        MenuManager applicationMenu = new MenuManager("Application");
        dropDownAction.add(applicationMenu);
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            applicationMenu.add(createObjectAction(eClass));
        }
        
        MenuManager technologyMenu = new MenuManager("Technology");
        dropDownAction.add(technologyMenu);
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            technologyMenu.add(createObjectAction(eClass));
        }
        
        MenuManager relationsMenu = new MenuManager("Relations");
        dropDownAction.add(relationsMenu);
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            relationsMenu.add(createObjectAction(eClass));
        }
        
        dropDownAction.add(new Separator());
        
        IAction action = new Action("Show All Folders", IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
            	fSearchFilter.setShowAllFolders(isChecked());
            }
        };
        dropDownAction.add(action);
        
        dropDownAction.add(new Separator());
        
        action = new Action("Reset Filters") {
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
        populatePropertiesMenu(fPropertiesMenu);

        fSearchFilter.resetFilters();

        // Default to search on Name
        fActionFilterName.setChecked(true);
        fSearchFilter.setFilterOnName(true);
    }

	private IAction createObjectAction(final EClass eClass) {
        IAction action = new Action(ArchimateNames.getDefaultName(eClass), IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                if(isChecked()) {
                    fSearchFilter.addObjectFilter(eClass);
                }
                else {
                    fSearchFilter.removeObjectFilter(eClass);
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ArchimateLabelProvider.INSTANCE.getImageDescriptor(eClass);
            }
        };
        
        fObjectActions.add(action);
        
        return action;
    }

	private void populatePropertiesMenu(MenuManager propertiesMenu) {
	    // Models that are loaded are the ones in the Models Tree
	    List<String> list = new ArrayList<String>();

	    for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
	        getAllUniquePropertyKeysForModel(model, list);
	    }

	    for(final String key : list) {
	        IAction action = new Action(key, IAction.AS_CHECK_BOX) {
	            @Override
	            public void run() {
	                if(isChecked()) {
	                    fSearchFilter.addPropertiesFilter(key);
	                }
	                else {
	                    fSearchFilter.removePropertiesFilter(key);
	                }
	            }
	        };

	        propertiesMenu.add(action);
	    }

	    propertiesMenu.update(true);
	}

    private void getAllUniquePropertyKeysForModel(IArchimateModel model, List<String> list) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
            	String key = ((IProperty)element).getKey();
            	if(StringUtils.isSetAfterTrim(key) && !list.contains(key)) {
            		list.add(key);
            	}
            }
        }
    }
}
