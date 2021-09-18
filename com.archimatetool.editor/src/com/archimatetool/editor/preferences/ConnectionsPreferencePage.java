/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

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
    
    private Combo fConnectionLabelStrategyCombo;
    
    private String[] CONNECTION_LABEL_STRATEGIES = {
            Messages.ConnectionsPreferencePage_0,
            Messages.ConnectionsPreferencePage_1,
            Messages.ConnectionsPreferencePage_2
    };

    private Button fShowReconnectionWarningButton;

    public ConnectionsPreferencePage() {
        setPreferenceStore(ArchiPlugin.PREFERENCES);
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
        magicConnectorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        fMagicConnectorPolarity1Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity1Button.setText(Messages.ConnectionsPreferencePage_4);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fMagicConnectorPolarity1Button.setLayoutData(gd);
        
        fMagicConnectorPolarity2Button = new Button(magicConnectorGroup, SWT.RADIO);
        fMagicConnectorPolarity2Button.setText(Messages.ConnectionsPreferencePage_5);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fMagicConnectorPolarity2Button.setLayoutData(gd);
        
        // Drawing
        Group connectorGroup = new Group(client, SWT.NULL);
        connectorGroup.setText(Messages.ConnectionsPreferencePage_6);
        connectorGroup.setLayout(new GridLayout(2, false));
        connectorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fDoAntiAliasButton = new Button(connectorGroup, SWT.CHECK);
        fDoAntiAliasButton.setText(Messages.ConnectionsPreferencePage_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoAntiAliasButton.setLayoutData(gd);
        
        fUseOrthogonalAnchorButton = new Button(connectorGroup, SWT.CHECK);
        fUseOrthogonalAnchorButton.setText(Messages.ConnectionsPreferencePage_8);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseOrthogonalAnchorButton.setLayoutData(gd);
        
        fUseLineCurvesButton = new Button(connectorGroup, SWT.CHECK);
        fUseLineCurvesButton.setText(Messages.ConnectionsPreferencePage_9);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseLineCurvesButton.setLayoutData(gd);

        fUseLineJumpsButton = new Button(connectorGroup, SWT.CHECK);
        fUseLineJumpsButton.setText(Messages.ConnectionsPreferencePage_10);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fUseLineJumpsButton.setLayoutData(gd);
        
        Label label = new Label(connectorGroup, SWT.NONE);
        label.setText(Messages.ConnectionsPreferencePage_11);
        
        fConnectionLabelStrategyCombo = new Combo(connectorGroup, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fConnectionLabelStrategyCombo.setLayoutData(gd);
        fConnectionLabelStrategyCombo.setItems(CONNECTION_LABEL_STRATEGIES);
        
        // General
        Group generalGroup = new Group(client, SWT.NULL);
        generalGroup.setText(Messages.ConnectionsPreferencePage_12);
        generalGroup.setLayout(new GridLayout());
        generalGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
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
        
        fConnectionLabelStrategyCombo.select(getPreferenceStore().getDefaultInt(CONNECTION_LABEL_STRATEGY));
        
        fShowReconnectionWarningButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_WARNING_ON_RECONNECT));

        super.performDefaults();
    }
}
