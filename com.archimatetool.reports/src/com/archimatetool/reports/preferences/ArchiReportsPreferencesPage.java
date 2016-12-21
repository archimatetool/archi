/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.reports.ArchiReportsPlugin;


/**
 * Canvas Preferences Page
 *
 * @author Tun Schlechter
 */
public class ArchiReportsPreferencesPage
extends PreferencePage
implements IWorkbenchPreferencePage, IArchiReportsPreferenceConstants {

    private static String HELP_ID = "com.archimatetool.help.prefsArchiReports";//$NON-NLS-1$

    private Button viewsExportDocumentationButton;
    private Button viewsExportPropertiesButton;
    private Button viewsExportElementsButton;

    private Combo viewsDefaultTabCombo;

    private Button elementsExportDocumentationButton;
    private Button elementsExportPropertiesButton;
    private Button elementsExportViewsButton;

    private Combo elementsDefaultTabCombo;


    public ArchiReportsPreferencesPage() {
        setPreferenceStore(ArchiReportsPlugin.INSTANCE.getPreferenceStore());
    }

    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        client.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        Group exportViewsSettingsGroup = new Group(client, SWT.NULL);
        exportViewsSettingsGroup.setText(Messages.ArchiReportsPreferencesPage_0);

        exportViewsSettingsGroup.setLayout(new GridLayout(3,  false));

        // checkboxes to select the tabs to be exported
        viewsExportDocumentationButton = new Button(exportViewsSettingsGroup, SWT.CHECK);
        viewsExportDocumentationButton.setText(DOCUMENTATION);
        viewsExportDocumentationButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateViewsCombo();//Combo(viewsDefaultTabCombo, EArchiReportsTabs.Documentation, event);
            }
        });
        viewsExportPropertiesButton = new Button(exportViewsSettingsGroup, SWT.CHECK);
        viewsExportPropertiesButton.setText(PROPERTIES);
        viewsExportPropertiesButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateViewsCombo();//Combo(viewsDefaultTabCombo, EArchiReportsTabs.Properties, event);
            }
        });
        viewsExportElementsButton = new Button(exportViewsSettingsGroup, SWT.CHECK);
        viewsExportElementsButton.setText(ELEMENTS);
        viewsExportElementsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateViewsCombo();//Combo(viewsDefaultTabCombo, EArchiReportsTabs.Elements, event);
            }
        });

        // Combobox to select the tab that is active by default
        Label viewsDefaultTabComboLabel = new Label(exportViewsSettingsGroup, SWT.NONE);
        viewsDefaultTabComboLabel.setText(Messages.ArchiReportsPreferencesPage_2);
        viewsDefaultTabCombo = new Combo(exportViewsSettingsGroup, SWT.READ_ONLY);

        Group exportElementsSettingsGroup = new Group(client, SWT.NULL);
        exportElementsSettingsGroup.setText(Messages.ArchiReportsPreferencesPage_1);

        exportElementsSettingsGroup.setLayout(new GridLayout(3,  false));

        // checkboxes to select the tabs to be exported
        elementsExportDocumentationButton = new Button(exportElementsSettingsGroup, SWT.CHECK);
        elementsExportDocumentationButton.setText(DOCUMENTATION);
        elementsExportDocumentationButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateElementsCombo();//Combo(elementsDefaultTabCombo, EArchiReportsTabs.Documentation, event);
            }
        });
        elementsExportPropertiesButton = new Button(exportElementsSettingsGroup, SWT.CHECK);
        elementsExportPropertiesButton.setText(PROPERTIES);
        elementsExportPropertiesButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateElementsCombo();//Combo(elementsDefaultTabCombo, EArchiReportsTabs.Properties, event);
            }
        });
        elementsExportViewsButton = new Button(exportElementsSettingsGroup, SWT.CHECK);
        elementsExportViewsButton.setText(VIEWS);
        elementsExportViewsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateElementsCombo();//Combo(elementsDefaultTabCombo, EArchiReportsTabs.Views, event);
            }
        });
        // Combobox to select the tab that is active by default
        Label elementsDefaultTabComboLabel = new Label(exportElementsSettingsGroup, SWT.NONE);
        elementsDefaultTabComboLabel.setText(Messages.ArchiReportsPreferencesPage_2);
        elementsDefaultTabCombo = new Combo(exportElementsSettingsGroup, SWT.READ_ONLY);

        // set the values from store!
        setValues();

        return client;
    }

    private void updateElementsCombo() {
        // store current selection
        String currentSelection = elementsDefaultTabCombo.getText();
        fillElementsCombo();
        if (currentSelection != null && !currentSelection.isEmpty()) {
            elementsDefaultTabCombo.setText(currentSelection);
            
        }
        if (elementsDefaultTabCombo.getSelectionIndex() == -1) {
            elementsDefaultTabCombo.select(0);
        }        
    }
    
    private void updateViewsCombo() {
        // store current selection
        String currentSelection = viewsDefaultTabCombo.getText();
        fillViewsCombo();
        if (currentSelection != null && !currentSelection.isEmpty()) {
            viewsDefaultTabCombo.setText(currentSelection);
        } 
        if (viewsDefaultTabCombo.getSelectionIndex() == -1){
            viewsDefaultTabCombo.select(0);
        }        
    }    

    private void setValues() {
        // initialize checkboxes
        viewsExportDocumentationButton.setSelection(getPreferenceStore().getBoolean(VIEWS_SHOW_DOCUMENTATION));
        viewsExportPropertiesButton.setSelection(getPreferenceStore().getBoolean(VIEWS_SHOW_PROPERTIES));
        viewsExportElementsButton.setSelection(getPreferenceStore().getBoolean(VIEWS_SHOW_ELEMENTS));
      
        elementsExportDocumentationButton.setSelection(getPreferenceStore().getBoolean(ELEMENTS_SHOW_DOCUMENTATION));
        elementsExportPropertiesButton.setSelection(getPreferenceStore().getBoolean(ELEMENTS_SHOW_PROPERTIES));
        elementsExportViewsButton.setSelection(getPreferenceStore().getBoolean(ELEMENTS_SHOW_VIEWS));

        fillCombos();
        
        // preselect default tab
        viewsDefaultTabCombo.setText(getPreferenceStore().getString(VIEWS_DEFAULT_TAB));
        elementsDefaultTabCombo.setText(getPreferenceStore().getString(ELEMENTS_DEFAULT_TAB));
        
    }

    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(VIEWS_SHOW_DOCUMENTATION, viewsExportDocumentationButton.getSelection());
        getPreferenceStore().setValue(VIEWS_SHOW_PROPERTIES, viewsExportPropertiesButton.getSelection());
        getPreferenceStore().setValue(VIEWS_SHOW_ELEMENTS, viewsExportElementsButton.getSelection());

        getPreferenceStore().setValue(VIEWS_DEFAULT_TAB, viewsDefaultTabCombo.getText());

        getPreferenceStore().setValue(ELEMENTS_SHOW_DOCUMENTATION, elementsExportDocumentationButton.getSelection());
        getPreferenceStore().setValue(ELEMENTS_SHOW_PROPERTIES, elementsExportPropertiesButton.getSelection());
        getPreferenceStore().setValue(ELEMENTS_SHOW_VIEWS, elementsExportViewsButton.getSelection());

        getPreferenceStore().setValue(ELEMENTS_DEFAULT_TAB, elementsDefaultTabCombo.getText());

        return true;
    }

    @Override
    protected void performDefaults() {
        viewsExportDocumentationButton.setSelection(VIEWS_SHOW_DOCUMENTATION_DEFAULT_VALUE);
        viewsExportPropertiesButton.setSelection(VIEWS_SHOW_PROPERTIES_DEFAULT_VALUE);
        viewsExportElementsButton.setSelection(VIEWS_SHOW_ELEMENTS_DEFAULT_VALUE);
       

        elementsExportDocumentationButton.setSelection(ELEMENTS_SHOW_DOCUMENTATION_DEFAULT_VALUE);
        elementsExportPropertiesButton.setSelection(ELEMENTS_SHOW_PROPERTIES_DEFAULT_VALUE);
        elementsExportViewsButton.setSelection(ELEMENTS_SHOW_VIEWS_DEFAULT_VALUE);

        fillCombos();
        
        viewsDefaultTabCombo.setText(VIEWS_DEFAULT_TAB_DEFAULT_VALUE);
        elementsDefaultTabCombo.setText(ELEMENTS_DEFAULT_TAB_DEFAULT_VALUE);
        super.performDefaults();
    }
    
    private void fillCombos() {
        fillViewsCombo();
        fillElementsCombo();
    }
    
    private void fillViewsCombo() {
        // determine what values need to be added to the select_default_tab-combobox based on initial selection
        viewsDefaultTabCombo.removeAll();
        if (viewsExportDocumentationButton.getSelection()) {
            viewsDefaultTabCombo.add(DOCUMENTATION);
        }
        if (viewsExportPropertiesButton.getSelection()) {
            viewsDefaultTabCombo.add(PROPERTIES);
        }
        if (viewsExportElementsButton.getSelection()) {
            viewsDefaultTabCombo.add(ELEMENTS);
        }
    }
    
    private void fillElementsCombo() {
        elementsDefaultTabCombo.removeAll();
        // determine what values need to be added to the select_default_tab-combobox based on initial selection        
        if (elementsExportDocumentationButton.getSelection()) {
            elementsDefaultTabCombo.add(DOCUMENTATION);
        }
        if (elementsExportPropertiesButton.getSelection()) {
            elementsDefaultTabCombo.add(PROPERTIES);
        }
        if (elementsExportViewsButton.getSelection()) {
            elementsDefaultTabCombo.add(VIEWS);
        }
    }

    public void init(IWorkbench workbench) {
    }
}
