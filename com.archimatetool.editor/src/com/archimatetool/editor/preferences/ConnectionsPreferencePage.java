/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
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

import com.archimatetool.editor.ArchiPlugin;



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
    private Button fHighlightSelectedConnectionsButton;
    
    private Combo fConnectionLabelStrategyCombo;
    
    private String[] CONNECTION_LABEL_STRATEGIES = {
            Messages.ConnectionsPreferencePage_0,
            Messages.ConnectionsPreferencePage_1,
            Messages.ConnectionsPreferencePage_2
    };

    private Button fShowReconnectionWarningButton;

    public ConnectionsPreferencePage() {
        setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
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
        
        // Magic Connector
        Group magicConnectorGroup = new Group(client, SWT.NULL);
        magicConnectorGroup.setText(Messages.ConnectionsPreferencePage_3);
        magicConnectorGroup.setLayout(new GridLayout());
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(magicConnectorGroup);

        fMagicConnectorPolarity1Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity1Button.setText(Messages.ConnectionsPreferencePage_4);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fMagicConnectorPolarity1Button);
        
        fMagicConnectorPolarity2Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity2Button.setText(Messages.ConnectionsPreferencePage_5);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fMagicConnectorPolarity2Button);
        
        // Drawing
        Group connectorGroup = new Group(client, SWT.NULL);
        connectorGroup.setText(Messages.ConnectionsPreferencePage_6);
        connectorGroup.setLayout(new GridLayout(2, false));
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(connectorGroup);
        
        fDoAntiAliasButton = new Button(connectorGroup, SWT.CHECK);
        fDoAntiAliasButton.setText(Messages.ConnectionsPreferencePage_7);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fDoAntiAliasButton);
        
        fUseOrthogonalAnchorButton = new Button(connectorGroup, SWT.CHECK);
        fUseOrthogonalAnchorButton.setText(Messages.ConnectionsPreferencePage_8);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fUseOrthogonalAnchorButton);
        
        fUseLineCurvesButton = new Button(connectorGroup, SWT.CHECK);
        fUseLineCurvesButton.setText(Messages.ConnectionsPreferencePage_9);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fUseLineCurvesButton);

        fUseLineJumpsButton = new Button(connectorGroup, SWT.CHECK);
        fUseLineJumpsButton.setText(Messages.ConnectionsPreferencePage_10);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fUseLineJumpsButton);
        
        fHighlightSelectedConnectionsButton = new Button(connectorGroup, SWT.CHECK);
        fHighlightSelectedConnectionsButton.setText(Messages.ConnectionsPreferencePage_14);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(fHighlightSelectedConnectionsButton);
        
        Label label = new Label(connectorGroup, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_11);
        
        fConnectionLabelStrategyCombo = new Combo(connectorGroup, SWT.READ_ONLY);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fConnectionLabelStrategyCombo);
        fConnectionLabelStrategyCombo.setItems(CONNECTION_LABEL_STRATEGIES);
        
        // General
        Group generalGroup = new Group(client, SWT.NULL);
        generalGroup.setText(Messages.ConnectionsPreferencePage_12);
        generalGroup.setLayout(new GridLayout());
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(generalGroup);
        
        fShowReconnectionWarningButton = new Button(generalGroup, SWT.CHECK);
        fShowReconnectionWarningButton.setText(Messages.ConnectionsPreferencePage_13);
        
        setValues();

        return client;
    }
    
    private void setValues() {
        fMagicConnectorPolarity1Button.setSelection(getPreferenceStore().getBoolean(MAGIC_CONNECTOR_POLARITY));
        fMagicConnectorPolarity2Button.setSelection(!getPreferenceStore().getBoolean(MAGIC_CONNECTOR_POLARITY));
        
        fDoAntiAliasButton.setSelection(getPreferenceStore().getBoolean(ANTI_ALIAS));
        fUseOrthogonalAnchorButton.setSelection(getPreferenceStore().getBoolean(USE_ORTHOGONAL_ANCHOR));
        fUseLineCurvesButton.setSelection(getPreferenceStore().getBoolean(USE_LINE_CURVES));
        fUseLineJumpsButton.setSelection(getPreferenceStore().getBoolean(USE_LINE_JUMPS));
        fHighlightSelectedConnectionsButton.setSelection(getPreferenceStore().getBoolean(SHOW_SELECTED_CONNECTIONS));
        
        fConnectionLabelStrategyCombo.select(getPreferenceStore().getInt(CONNECTION_LABEL_STRATEGY));
        
        fShowReconnectionWarningButton.setSelection(getPreferenceStore().getBoolean(SHOW_WARNING_ON_RECONNECT));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(MAGIC_CONNECTOR_POLARITY, fMagicConnectorPolarity1Button.getSelection());
        
        getPreferenceStore().setValue(ANTI_ALIAS, fDoAntiAliasButton.getSelection());
        getPreferenceStore().setValue(USE_ORTHOGONAL_ANCHOR, fUseOrthogonalAnchorButton.getSelection());
        getPreferenceStore().setValue(USE_LINE_CURVES, fUseLineCurvesButton.getSelection());
        getPreferenceStore().setValue(USE_LINE_JUMPS, fUseLineJumpsButton.getSelection());
        getPreferenceStore().setValue(SHOW_SELECTED_CONNECTIONS, fHighlightSelectedConnectionsButton.getSelection());
        
        getPreferenceStore().setValue(CONNECTION_LABEL_STRATEGY, fConnectionLabelStrategyCombo.getSelectionIndex());
        
        getPreferenceStore().setValue(SHOW_WARNING_ON_RECONNECT, fShowReconnectionWarningButton.getSelection());
                
        return true;
    }

    @Override
    protected void performDefaults() {
        fMagicConnectorPolarity1Button.setSelection(getPreferenceStore().getDefaultBoolean(MAGIC_CONNECTOR_POLARITY));
        fMagicConnectorPolarity2Button.setSelection(!getPreferenceStore().getDefaultBoolean(MAGIC_CONNECTOR_POLARITY));
        
        fDoAntiAliasButton.setSelection(getPreferenceStore().getDefaultBoolean(ANTI_ALIAS));
        fUseOrthogonalAnchorButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_ORTHOGONAL_ANCHOR));
        fUseLineCurvesButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_LINE_CURVES));
        fUseLineJumpsButton.setSelection(getPreferenceStore().getDefaultBoolean(USE_LINE_JUMPS));
        fHighlightSelectedConnectionsButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_SELECTED_CONNECTIONS));
        
        fConnectionLabelStrategyCombo.select(getPreferenceStore().getDefaultInt(CONNECTION_LABEL_STRATEGY));
        
        fShowReconnectionWarningButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_WARNING_ON_RECONNECT));

        super.performDefaults();
    }
}
