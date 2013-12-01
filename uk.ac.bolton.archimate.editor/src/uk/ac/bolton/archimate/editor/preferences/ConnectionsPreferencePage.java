/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.preferences;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;


/**
 * Connections Preference Page
 * 
 * @author Phillip Beauvoir
 */
public class ConnectionsPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {

    private static String HELP_ID = "uk.ac.bolton.archimate.help.prefsConnections"; //$NON-NLS-1$

    private Button fDoAnimateMagicConnectorButton;
    private Button fMagicConnectorPolarity1Button;
    private Button fMagicConnectorPolarity2Button;
    
    private Button fAllowCircularConnectionsButton;
    private Button fUseOrthogonalAnchorButton;
    // rounded-connection patch
    private Button fUseRoundedConnectionButton;
    
    private Button fUseNestedConnectionsButton;
    private Button fCreateRelationWhenAddingNewElementButton;
    private Button fCreateRelationWhenAddingModelTreeElementButton;
    private Button fCreateRelationWhenMovingElement;
    
    private CheckboxTableViewer fTableViewerNewRelations, fTableViewerHiddenRelations;
    
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
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // Magic Connector
        Group magicConnectorGroup = new Group(client, SWT.NULL);
        magicConnectorGroup.setText(Messages.ConnectionsPreferencePage_0);
        magicConnectorGroup.setLayout(new GridLayout());
        magicConnectorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fDoAnimateMagicConnectorButton = new Button(magicConnectorGroup, SWT.CHECK);
        fDoAnimateMagicConnectorButton.setText(Messages.ConnectionsPreferencePage_1);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fDoAnimateMagicConnectorButton.setLayoutData(gd);
        
        fMagicConnectorPolarity1Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity1Button.setText(Messages.ConnectionsPreferencePage_2);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fMagicConnectorPolarity1Button.setLayoutData(gd);
        
        fMagicConnectorPolarity2Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity2Button.setText(Messages.ConnectionsPreferencePage_3);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fMagicConnectorPolarity2Button.setLayoutData(gd);
        
        // Connections
        Group connectorGroup = new Group(client, SWT.NULL);
        connectorGroup.setText(Messages.ConnectionsPreferencePage_4);
        connectorGroup.setLayout(new GridLayout());
        connectorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fAllowCircularConnectionsButton = new Button(connectorGroup, SWT.CHECK);
        fAllowCircularConnectionsButton.setText(Messages.ConnectionsPreferencePage_5);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fAllowCircularConnectionsButton.setLayoutData(gd);
        
        fUseOrthogonalAnchorButton = new Button(connectorGroup, SWT.CHECK);
        fUseOrthogonalAnchorButton.setText(Messages.ConnectionsPreferencePage_13);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fUseOrthogonalAnchorButton.setLayoutData(gd);
        
        // rounded-connection patch
        fUseRoundedConnectionButton = new Button(connectorGroup, SWT.CHECK);
        fUseRoundedConnectionButton.setText(Messages.ConnectionsPreferencePage_14);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fUseRoundedConnectionButton.setLayoutData(gd);

        // Nested Connections (ARM)
        Group nestedConnectionsGroup = new Group(client, SWT.NULL);
        nestedConnectionsGroup.setText(Messages.ConnectionsPreferencePage_6);
        nestedConnectionsGroup.setLayout(new GridLayout());
        nestedConnectionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fUseNestedConnectionsButton = new Button(nestedConnectionsGroup, SWT.CHECK);
        fUseNestedConnectionsButton.setText(Messages.ConnectionsPreferencePage_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fUseNestedConnectionsButton.setLayoutData(gd);
        fUseNestedConnectionsButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenAddingNewElementButton = new Button(nestedConnectionsGroup, SWT.CHECK);
        fCreateRelationWhenAddingNewElementButton.setText(Messages.ConnectionsPreferencePage_8);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenAddingNewElementButton.setLayoutData(gd);
        fCreateRelationWhenAddingNewElementButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenAddingModelTreeElementButton = new Button(nestedConnectionsGroup, SWT.CHECK);
        fCreateRelationWhenAddingModelTreeElementButton.setText(Messages.ConnectionsPreferencePage_9);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenAddingModelTreeElementButton.setLayoutData(gd);
        fCreateRelationWhenAddingModelTreeElementButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        fCreateRelationWhenMovingElement = new Button(nestedConnectionsGroup, SWT.CHECK);
        fCreateRelationWhenMovingElement.setText(Messages.ConnectionsPreferencePage_10);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fCreateRelationWhenMovingElement.setLayoutData(gd);
        fCreateRelationWhenMovingElement.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableNestedConnectionComponents();
            }
        });
        
        Label label = new Label(nestedConnectionsGroup, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_11);
        fTableViewerNewRelations = createRelationsTable(nestedConnectionsGroup);
        
        label = new Label(nestedConnectionsGroup, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_12);
        fTableViewerHiddenRelations = createRelationsTable(nestedConnectionsGroup);
        
        setValues();

        return client;
    }
    
    private CheckboxTableViewer createRelationsTable(Composite parent) {
        final CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 80;
        viewer.getTable().setLayoutData(gd);
        
        viewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ArchimateLabelProvider.INSTANCE.getDefaultName((EClass)element);
            }
            
            @Override
            public Image getImage(Object element) {
                return ArchimateLabelProvider.INSTANCE.getImage(element);
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
        
        fTableViewerNewRelations.getTable().setEnabled(enabled && 
                (fCreateRelationWhenAddingNewElementButton.getSelection() || 
                        fCreateRelationWhenAddingModelTreeElementButton.getSelection() ||
                            fCreateRelationWhenMovingElement.getSelection()));
        
        fTableViewerHiddenRelations.getTable().setEnabled(enabled);
    }
    
    private void setValues() {
        fDoAnimateMagicConnectorButton.setSelection(getPreferenceStore().getBoolean(ANIMATE_MAGIC_CONNECTOR));
        fMagicConnectorPolarity1Button.setSelection(getPreferenceStore().getBoolean(MAGIC_CONNECTOR_POLARITY));
        fMagicConnectorPolarity2Button.setSelection(!getPreferenceStore().getBoolean(MAGIC_CONNECTOR_POLARITY));
        
        fAllowCircularConnectionsButton.setSelection(getPreferenceStore().getBoolean(ALLOW_CIRCULAR_CONNECTIONS));
        fUseOrthogonalAnchorButton.setSelection(getPreferenceStore().getBoolean(USE_ORTHOGONAL_ANCHOR));
        // rounded-connection patch
        fUseRoundedConnectionButton.setSelection(getPreferenceStore().getBoolean(USE_ROUNDED_CONNECTION));
        
        fUseNestedConnectionsButton.setSelection(getPreferenceStore().getBoolean(USE_NESTED_CONNECTIONS));
        fCreateRelationWhenAddingNewElementButton.setSelection(getPreferenceStore().getBoolean(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenAddingModelTreeElementButton.setSelection(getPreferenceStore().getBoolean(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenMovingElement.setSelection(getPreferenceStore().getBoolean(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER));
        
        fTableViewerNewRelations.setInput(getPreferenceStore().getInt(NEW_RELATIONS_TYPES));
        fTableViewerHiddenRelations.setInput(getPreferenceStore().getInt(HIDDEN_RELATIONS_TYPES));
        
        enableNestedConnectionComponents();
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(ANIMATE_MAGIC_CONNECTOR, fDoAnimateMagicConnectorButton.getSelection());
        getPreferenceStore().setValue(MAGIC_CONNECTOR_POLARITY, fMagicConnectorPolarity1Button.getSelection());
        
        getPreferenceStore().setValue(ALLOW_CIRCULAR_CONNECTIONS, fAllowCircularConnectionsButton.getSelection());
        getPreferenceStore().setValue(USE_ORTHOGONAL_ANCHOR, fUseOrthogonalAnchorButton.getSelection());
        // rounded-connection patch
        getPreferenceStore().setValue(USE_ROUNDED_CONNECTION, fUseRoundedConnectionButton.getSelection());
        
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
        for(Object checked :  fTableViewerHiddenRelations.getCheckedElements()) {
            value |= ConnectionPreferences.RELATION_KEYMAP.get(checked);
        }
        getPreferenceStore().setValue(HIDDEN_RELATIONS_TYPES, value);
        
        return true;
    }

    @Override
    protected void performDefaults() {
        fDoAnimateMagicConnectorButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE_MAGIC_CONNECTOR));
        fMagicConnectorPolarity1Button.setSelection(getPreferenceStore().getDefaultBoolean(MAGIC_CONNECTOR_POLARITY));
        fMagicConnectorPolarity2Button.setSelection(!getPreferenceStore().getDefaultBoolean(MAGIC_CONNECTOR_POLARITY));
        
        fAllowCircularConnectionsButton.setSelection(getPreferenceStore().getDefaultBoolean(ALLOW_CIRCULAR_CONNECTIONS));
        fUseOrthogonalAnchorButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_ORTHOGONAL_ANCHOR));
        // rounded-connection patch
        fUseRoundedConnectionButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_ROUNDED_CONNECTION));
        
        fUseNestedConnectionsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_NESTED_CONNECTIONS));
        fCreateRelationWhenAddingNewElementButton.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_ADDING_NEW_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenAddingModelTreeElementButton.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_ADDING_MODEL_TREE_ELEMENT_TO_CONTAINER));
        fCreateRelationWhenMovingElement.setSelection(getPreferenceStore().getDefaultBoolean(CREATE_RELATION_WHEN_MOVING_ELEMENT_TO_CONTAINER));
        
        fTableViewerNewRelations.setInput(getPreferenceStore().getDefaultInt(NEW_RELATIONS_TYPES));
        fTableViewerHiddenRelations.setInput(getPreferenceStore().getDefaultInt(HIDDEN_RELATIONS_TYPES));
        
        enableNestedConnectionComponents();
        
        super.performDefaults();
    }
}
