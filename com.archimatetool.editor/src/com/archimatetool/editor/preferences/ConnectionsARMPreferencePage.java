/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.ArchiLabelProvider;



/**
 * Connections ARM Preference Page
 * 
 * @author Phillip Beauvoir
 */
public class ConnectionsARMPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {

    private static String HELP_ID = "com.archimatetool.help.prefsConnections"; //$NON-NLS-1$

    private Button fUseNestedConnectionsButton;
    private Button fCreateRelationWhenAddingNewElementButton;
    private Button fCreateRelationWhenAddingModelTreeElementButton;
    private Button fCreateRelationWhenMovingElement;
    
    private CheckboxTableViewer fTableViewerNewRelations, fTableViewerReversedRelations, fTableViewerHiddenRelations;
    
    public ConnectionsARMPreferencePage() {
        setPreferenceStore(ArchiPlugin.PREFERENCES);
        setDescription(Messages.ConnectionsARMPreferencePage_0);
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        // Nested Connections
        
        fUseNestedConnectionsButton = new Button(client, SWT.CHECK);
        fUseNestedConnectionsButton.setText(Messages.ConnectionsARMPreferencePage_1);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fUseNestedConnectionsButton.setLayoutData(gd);
        fUseNestedConnectionsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenAddingNewElementButton = new Button(client, SWT.CHECK);
        fCreateRelationWhenAddingNewElementButton.setText(Messages.ConnectionsARMPreferencePage_2);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenAddingNewElementButton.setLayoutData(gd);
        fCreateRelationWhenAddingNewElementButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenAddingModelTreeElementButton = new Button(client, SWT.CHECK);
        fCreateRelationWhenAddingModelTreeElementButton.setText(Messages.ConnectionsARMPreferencePage_3);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenAddingModelTreeElementButton.setLayoutData(gd);
        fCreateRelationWhenAddingModelTreeElementButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenMovingElement = new Button(client, SWT.CHECK);
        fCreateRelationWhenMovingElement.setText(Messages.ConnectionsARMPreferencePage_4);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenMovingElement.setLayoutData(gd);
        fCreateRelationWhenMovingElement.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        Label label = new Label(client, SWT.NONE);
        label.setText(Messages.ConnectionsARMPreferencePage_5);
        fTableViewerNewRelations = createRelationsTable(client);
        
        label = new Label(client, SWT.NONE);
        label.setText(Messages.ConnectionsARMPreferencePage_6);
        fTableViewerReversedRelations = createRelationsTable(client);

        label = new Label(client, SWT.NONE);
        label.setText(Messages.ConnectionsARMPreferencePage_7);
        fTableViewerHiddenRelations = createRelationsTable(client);
        
        // On Mac the tables are laid out later
        Display.getCurrent().asyncExec(() -> {
            setValues();
        });

        return client;
    }
    
    private CheckboxTableViewer createRelationsTable(Composite parent) {
        final CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
        
        GridDataFactory.create(GridData.FILL_HORIZONTAL).hint(SWT.DEFAULT, 100).applyTo(viewer.getTable());
        
        viewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ArchiLabelProvider.INSTANCE.getDefaultName((EClass)element);
            }
            
            @Override
            public Image getImage(Object element) {
                return ArchiLabelProvider.INSTANCE.getImage(element);
            }
        });
        
        viewer.setContentProvider(new IStructuredContentProvider () {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return ConnectionPreferences.RELATION_KEYMAP.keySet().toArray();
            }
        });
        
        viewer.setCheckStateProvider(new ICheckStateProvider() {
            @Override
            public boolean isGrayed(Object element) {
                return false;
            }
            
            @Override
            public boolean isChecked(Object element) {
                int value = (Integer)viewer.getInput();
                return (value & ConnectionPreferences.RELATION_KEYMAP.get(element)) != 0;
            }
        });
        
        return viewer;
    }
    
    private void enableNestedConnectionComponents() {
        boolean enabled = fUseNestedConnectionsButton.getSelection();
        
        fCreateRelationWhenAddingNewElementButton.setEnabled(enabled);
        fCreateRelationWhenAddingModelTreeElementButton.setEnabled(enabled);
        fCreateRelationWhenMovingElement.setEnabled(enabled);
        
        boolean alsoEnabled = enabled && 
                (fCreateRelationWhenAddingNewElementButton.getSelection() || 
                        fCreateRelationWhenAddingModelTreeElementButton.getSelection() ||
                            fCreateRelationWhenMovingElement.getSelection());
        
        fTableViewerNewRelations.getTable().setEnabled(alsoEnabled);
        fTableViewerReversedRelations.getTable().setEnabled(alsoEnabled);
        
        fTableViewerHiddenRelations.getTable().setEnabled(enabled);
    }
    
    private void setValues() {
        fUseNestedConnectionsButton.setSelection(getPreferenceStore().getBoolean(USE_NESTED_CONNECTIONS));
        fCreateRelationWhenAddingNewElementButton.setSelection(getPreferenceStore().getBoolean(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenAddingModelTreeElementButton.setSelection(getPreferenceStore().getBoolean(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenMovingElement.setSelection(getPreferenceStore().getBoolean(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER));
        
        fTableViewerNewRelations.setInput(getPreferenceStore().getInt(NEW_RELATIONS_TYPES));
        fTableViewerReversedRelations.setInput(getPreferenceStore().getInt(NEW_REVERSE_RELATIONS_TYPES));
        fTableViewerHiddenRelations.setInput(getPreferenceStore().getInt(HIDDEN_RELATIONS_TYPES));
        
        enableNestedConnectionComponents();
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(USE_NESTED_CONNECTIONS, fUseNestedConnectionsButton.getSelection());
        getPreferenceStore().setValue(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER, fCreateRelationWhenAddingNewElementButton.getSelection());
        getPreferenceStore().setValue(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER, fCreateRelationWhenAddingModelTreeElementButton.getSelection());
        getPreferenceStore().setValue(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER, fCreateRelationWhenMovingElement.getSelection());
        
        int value = 0;
        for(Object checked :  fTableViewerNewRelations.getCheckedElements()) {
            value |= ConnectionPreferences.RELATION_KEYMAP.get(checked);
        }
        getPreferenceStore().setValue(NEW_RELATIONS_TYPES, value);
        
        value = 0;
        for(Object checked :  fTableViewerReversedRelations.getCheckedElements()) {
            value |= ConnectionPreferences.RELATION_KEYMAP.get(checked);
        }
        getPreferenceStore().setValue(NEW_REVERSE_RELATIONS_TYPES, value);
        
        value = 0;
        for(Object checked :  fTableViewerHiddenRelations.getCheckedElements()) {
            value |= ConnectionPreferences.RELATION_KEYMAP.get(checked);
        }
        getPreferenceStore().setValue(HIDDEN_RELATIONS_TYPES, value);
        
        return true;
    }

    @Override
    protected void performDefaults() {
        fUseNestedConnectionsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_NESTED_CONNECTIONS));
        fCreateRelationWhenAddingNewElementButton.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenAddingModelTreeElementButton.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenMovingElement.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER));
        
        fTableViewerNewRelations.setInput(getPreferenceStore().getDefaultInt(NEW_RELATIONS_TYPES));
        fTableViewerReversedRelations.setInput(getPreferenceStore().getDefaultInt(NEW_REVERSE_RELATIONS_TYPES));
        fTableViewerHiddenRelations.setInput(getPreferenceStore().getDefaultInt(HIDDEN_RELATIONS_TYPES));
        
        enableNestedConnectionComponents();

        super.performDefaults();
    }
}
