/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.emf.ecore.EClass;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.ArchiLabelProvider;



/**
 * Connections Preference Page
 * 
 * @author Phillip Beauvoir
 */
public class ConnectionsPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {

    private static String HELP_ID = "com.archimatetool.help.prefsConnections"; //$NON-NLS-1$

    private Button fMagicConnectorPolarity1Button;
    private Button fMagicConnectorPolarity2Button;
    
    private Button fDoAntiAliasButton;
    private Button fUseOrthogonalAnchorButton;
    private Button fUseLineCurvesButton;
    private Button fUseLineJumpsButton;
    
    private Combo fConnectionLabelStrategyCombo;
    
    private String[] CONNECTION_LABEL_STRATEGIES = {
            Messages.ConnectionsPreferencePage_23,
            Messages.ConnectionsPreferencePage_24,
            Messages.ConnectionsPreferencePage_25
    };

    private Button fUseNestedConnectionsButton;
    private Button fCreateRelationWhenAddingNewElementButton;
    private Button fCreateRelationWhenAddingModelTreeElementButton;
    private Button fCreateRelationWhenMovingElement;
    
    private CheckboxTableViewer fTableViewerNewRelations, fTableViewerReversedRelations, fTableViewerHiddenRelations;
    
    private Button fShowReconnectionWarningButton;

    private TabFolder fTabFolder;
    
    public ConnectionsPreferencePage() {
        setPreferenceStore(Preferences.STORE);
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        fTabFolder = new TabFolder(parent, SWT.NONE);
        
        Composite client = new Composite(fTabFolder, SWT.NULL);
        client.setLayout(new GridLayout());
        
        TabItem item = new TabItem(fTabFolder, SWT.NONE);
        item.setText(Messages.ConnectionsPreferencePage_4);
        item.setControl(client);
        
        // Magic Connector
        Group magicConnectorGroup = new Group(client, SWT.NULL);
        magicConnectorGroup.setText(Messages.ConnectionsPreferencePage_0);
        magicConnectorGroup.setLayout(new GridLayout());
        magicConnectorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fMagicConnectorPolarity1Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity1Button.setText(Messages.ConnectionsPreferencePage_2);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fMagicConnectorPolarity1Button.setLayoutData(gd);
        
        fMagicConnectorPolarity2Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity2Button.setText(Messages.ConnectionsPreferencePage_3);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fMagicConnectorPolarity2Button.setLayoutData(gd);
        
        // Drawing
        Group connectorGroup = new Group(client, SWT.NULL);
        connectorGroup.setText(Messages.ConnectionsPreferencePage_17);
        connectorGroup.setLayout(new GridLayout(2, false));
        connectorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fDoAntiAliasButton = new Button(connectorGroup, SWT.CHECK);
        fDoAntiAliasButton.setText(Messages.ConnectionsPreferencePage_13);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoAntiAliasButton.setLayoutData(gd);
        
        fUseOrthogonalAnchorButton = new Button(connectorGroup, SWT.CHECK);
        fUseOrthogonalAnchorButton.setText(Messages.ConnectionsPreferencePage_14);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseOrthogonalAnchorButton.setLayoutData(gd);
        
        fUseLineCurvesButton = new Button(connectorGroup, SWT.CHECK);
        fUseLineCurvesButton.setText(Messages.ConnectionsPreferencePage_15);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseLineCurvesButton.setLayoutData(gd);

        fUseLineJumpsButton = new Button(connectorGroup, SWT.CHECK);
        fUseLineJumpsButton.setText(Messages.ConnectionsPreferencePage_16);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseLineJumpsButton.setLayoutData(gd);
        
        Label label = new Label(connectorGroup, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_22);
        
        fConnectionLabelStrategyCombo = new Combo(connectorGroup, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fConnectionLabelStrategyCombo.setLayoutData(gd);
        fConnectionLabelStrategyCombo.setItems(CONNECTION_LABEL_STRATEGIES);
        
        // General
        Group generalGroup = new Group(client, SWT.NULL);
        generalGroup.setText(Messages.ConnectionsPreferencePage_1);
        generalGroup.setLayout(new GridLayout());
        generalGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fShowReconnectionWarningButton = new Button(generalGroup, SWT.CHECK);
        fShowReconnectionWarningButton.setText(Messages.ConnectionsPreferencePage_21);
        
        // Rules
//        Group rulesGroup = new Group(client, SWT.NULL);
//        rulesGroup.setText(Messages.ConnectionsPreferencePage_18);
//        rulesGroup.setLayout(new GridLayout());
//        rulesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // ARM
        
        Composite client2 = new Composite(fTabFolder, SWT.NULL);
        client2.setLayout(new GridLayout());
    
        TabItem item2 = new TabItem(fTabFolder, SWT.NONE);
        item2.setText(Messages.ConnectionsPreferencePage_19);
        item2.setControl(client2);
        
        // Nested Connections
        label = new Label(client2, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_6);

        fUseNestedConnectionsButton = new Button(client2, SWT.CHECK);
        fUseNestedConnectionsButton.setText(Messages.ConnectionsPreferencePage_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fUseNestedConnectionsButton.setLayoutData(gd);
        fUseNestedConnectionsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenAddingNewElementButton = new Button(client2, SWT.CHECK);
        fCreateRelationWhenAddingNewElementButton.setText(Messages.ConnectionsPreferencePage_8);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenAddingNewElementButton.setLayoutData(gd);
        fCreateRelationWhenAddingNewElementButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenAddingModelTreeElementButton = new Button(client2, SWT.CHECK);
        fCreateRelationWhenAddingModelTreeElementButton.setText(Messages.ConnectionsPreferencePage_9);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenAddingModelTreeElementButton.setLayoutData(gd);
        fCreateRelationWhenAddingModelTreeElementButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenMovingElement = new Button(client2, SWT.CHECK);
        fCreateRelationWhenMovingElement.setText(Messages.ConnectionsPreferencePage_10);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenMovingElement.setLayoutData(gd);
        fCreateRelationWhenMovingElement.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        label = new Label(client2, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_11);
        fTableViewerNewRelations = createRelationsTable(client2);
        
        label = new Label(client2, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_20);
        fTableViewerReversedRelations = createRelationsTable(client2);

        label = new Label(client2, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_12);
        fTableViewerHiddenRelations = createRelationsTable(client2);
        
        setValues();

        return fTabFolder;
    }
    
    private CheckboxTableViewer createRelationsTable(Composite parent) {
        final CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 80;
        viewer.getTable().setLayoutData(gd);
        
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
        fMagicConnectorPolarity1Button.setSelection(getPreferenceStore().getBoolean(MAGIC_CONNECTOR_POLARITY));
        fMagicConnectorPolarity2Button.setSelection(!getPreferenceStore().getBoolean(MAGIC_CONNECTOR_POLARITY));
        
        fDoAntiAliasButton.setSelection(getPreferenceStore().getBoolean(ANTI_ALIAS));
        fUseOrthogonalAnchorButton.setSelection(getPreferenceStore().getBoolean(USE_ORTHOGONAL_ANCHOR));
        fUseLineCurvesButton.setSelection(getPreferenceStore().getBoolean(USE_LINE_CURVES));
        fUseLineJumpsButton.setSelection(getPreferenceStore().getBoolean(USE_LINE_JUMPS));
        
        fConnectionLabelStrategyCombo.select(getPreferenceStore().getInt(CONNECTION_LABEL_STRATEGY));
        
        fShowReconnectionWarningButton.setSelection(getPreferenceStore().getBoolean(SHOW_WARNING_ON_RECONNECT));
        
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
        getPreferenceStore().setValue(MAGIC_CONNECTOR_POLARITY, fMagicConnectorPolarity1Button.getSelection());
        
        getPreferenceStore().setValue(ANTI_ALIAS, fDoAntiAliasButton.getSelection());
        getPreferenceStore().setValue(USE_ORTHOGONAL_ANCHOR, fUseOrthogonalAnchorButton.getSelection());
        getPreferenceStore().setValue(USE_LINE_CURVES, fUseLineCurvesButton.getSelection());
        getPreferenceStore().setValue(USE_LINE_JUMPS, fUseLineJumpsButton.getSelection());
        
        getPreferenceStore().setValue(CONNECTION_LABEL_STRATEGY, fConnectionLabelStrategyCombo.getSelectionIndex());
        
        getPreferenceStore().setValue(SHOW_WARNING_ON_RECONNECT, fShowReconnectionWarningButton.getSelection());
                
        getPreferenceStore().setValue(USE_NESTED_CONNECTIONS, fUseNestedConnectionsButton.getSelection());
        getPreferenceStore().setValue(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER, fCreateRelationWhenAddingNewElementButton.getSelection());
        getPreferenceStore().setValue(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER, fCreateRelationWhenAddingModelTreeElementButton.getSelection());
        getPreferenceStore().setValue(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER, fCreateRelationWhenMovingElement.getSelection());
        
        // Refresh the connection preferences *before* setting (which sends a notification)
        ConnectionPreferences.reset();

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
        switch(fTabFolder.getSelectionIndex()) {
            case 0:
                performConnectionsDefaults();
                break;

            case 1:
                performARMDefaults();
                break;

            default:
                break;
        }

        super.performDefaults();
    }
    
    private void performConnectionsDefaults() {
        fMagicConnectorPolarity1Button.setSelection(getPreferenceStore().getDefaultBoolean(MAGIC_CONNECTOR_POLARITY));
        fMagicConnectorPolarity2Button.setSelection(!getPreferenceStore().getDefaultBoolean(MAGIC_CONNECTOR_POLARITY));
        
        fDoAntiAliasButton.setSelection(getPreferenceStore().getDefaultBoolean(ANTI_ALIAS));
        fUseOrthogonalAnchorButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_ORTHOGONAL_ANCHOR));
        fUseLineCurvesButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_LINE_CURVES));
        fUseLineJumpsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_LINE_JUMPS));
        
        fConnectionLabelStrategyCombo.select(getPreferenceStore().getDefaultInt(CONNECTION_LABEL_STRATEGY));
        
        fShowReconnectionWarningButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_WARNING_ON_RECONNECT));
    }
    
    private void performARMDefaults() {
        fUseNestedConnectionsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_NESTED_CONNECTIONS));
        fCreateRelationWhenAddingNewElementButton.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenAddingModelTreeElementButton.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenMovingElement.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER));
        
        fTableViewerNewRelations.setInput(getPreferenceStore().getDefaultInt(NEW_RELATIONS_TYPES));
        fTableViewerReversedRelations.setInput(getPreferenceStore().getDefaultInt(NEW_REVERSE_RELATIONS_TYPES));
        fTableViewerHiddenRelations.setInput(getPreferenceStore().getDefaultInt(HIDDEN_RELATIONS_TYPES));
        
        enableNestedConnectionComponents();
    }
}
