/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.io.IOException;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.SecureStorageUtils;


/**
 * Network Connections Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class NetworkConnectionsPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static final String HELP_ID = "com.archimatetool.help.prefsNetwork"; //$NON-NLS-1$
    
    private Button fUseProxyButton, fRequiresProxyAuthenticationButton;
    private Text fProxyHostTextField;
    private Text fProxyPortTextField;
    private Text fProxyUserNameTextField;
    private Text fProxyUserPasswordTextField;
    
    // Because we are displaying dummy characters in the password field we should only save the field's contents if user changed it
    private boolean proxyPasswordChanged;
    
    private boolean proxyUserNameChanged;
    
	public NetworkConnectionsPreferencePage() {
		setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        // Proxy Group
        Group proxyGroup = new Group(client, SWT.NULL);
        proxyGroup.setText(Messages.NetworkConnectionsPreferencePage_0);
        proxyGroup.setLayout(new GridLayout(4, false));
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(proxyGroup);
        
        fUseProxyButton = new Button(proxyGroup, SWT.CHECK);
        fUseProxyButton.setText(Messages.NetworkConnectionsPreferencePage_1);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(4, 1).applyTo(fUseProxyButton);
        fUseProxyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateProxyControls();
            }
        });
        
        new Label(proxyGroup, SWT.NULL).setText(Messages.NetworkConnectionsPreferencePage_2);
        fProxyHostTextField = UIUtils.createSingleTextControl(proxyGroup, SWT.BORDER, false);
        fProxyHostTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fProxyHostTextField.setEnabled(false);

        new Label(proxyGroup, SWT.NULL).setText(Messages.NetworkConnectionsPreferencePage_3);
        fProxyPortTextField = UIUtils.createSingleTextControl(proxyGroup, SWT.BORDER, false);
        fProxyPortTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fProxyPortTextField.setEnabled(false);
        
        fProxyPortTextField.addVerifyListener(new VerifyListener() {
            @Override
            public void verifyText(VerifyEvent e) {
                String currentText = ((Text)e.widget).getText();
                String port = currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
                try {
                    int portNum = Integer.valueOf(port);
                    if(portNum < 0 || portNum > 65535) {
                        e.doit = false;
                    }
                }
                catch(NumberFormatException ex) {
                    if(!port.equals("")) { //$NON-NLS-1$
                        e.doit = false;
                    }
                }
            }
        });
        
        fRequiresProxyAuthenticationButton = new Button(proxyGroup, SWT.CHECK);
        fRequiresProxyAuthenticationButton.setText(Messages.NetworkConnectionsPreferencePage_4);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(4, 1).applyTo(fRequiresProxyAuthenticationButton);
        fRequiresProxyAuthenticationButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateProxyControls();
            }
        });
        
        new Label(proxyGroup, SWT.NULL).setText(Messages.NetworkConnectionsPreferencePage_5);
        fProxyUserNameTextField = UIUtils.createSingleTextControl(proxyGroup, SWT.BORDER, false);
        fProxyUserNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fProxyUserNameTextField.setEnabled(false);
        
        new Label(proxyGroup, SWT.NULL).setText(Messages.NetworkConnectionsPreferencePage_6);
        fProxyUserPasswordTextField = UIUtils.createSingleTextControl(proxyGroup, SWT.BORDER | SWT.PASSWORD, false);
        fProxyUserPasswordTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fProxyUserPasswordTextField.setEnabled(false);

        setValues();
        
        return client;
    }

    private void setValues() {
        // Proxy details
        fUseProxyButton.setSelection(getPreferenceStore().getBoolean(PREFS_PROXY_ENABLED));
        fProxyHostTextField.setText(getPreferenceStore().getString(PREFS_PROXY_HOST));
        fProxyPortTextField.setText(getPreferenceStore().getString(PREFS_PROXY_PORT));
        fRequiresProxyAuthenticationButton.setSelection(getPreferenceStore().getBoolean(PREFS_PROXY_REQUIRES_AUTHENTICATION));
        
        try {
            ISecurePreferences archiNode = SecurePreferencesFactory.getDefault().node(ArchiPlugin.PLUGIN_ID);
            fProxyUserNameTextField.setText(archiNode.get(IPreferenceConstants.PREFS_PROXY_USERNAME, "")); //$NON-NLS-1$
            fProxyUserPasswordTextField.setText(archiNode.get(IPreferenceConstants.PREFS_PROXY_PASSWORD, "").isEmpty() ? "" : "********"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch(StorageException ex) {
            ex.printStackTrace();
            showErrorDialog(ex);
        }
        
        fProxyUserNameTextField.addModifyListener(event -> {
            proxyUserNameChanged = true;
        });

        fProxyUserPasswordTextField.addModifyListener(event -> {
            proxyPasswordChanged = true;
        });
        
        updateProxyControls();
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(PREFS_PROXY_ENABLED, fUseProxyButton.getSelection());
        getPreferenceStore().setValue(PREFS_PROXY_HOST, fProxyHostTextField.getText());
        getPreferenceStore().setValue(PREFS_PROXY_PORT, fProxyPortTextField.getText());
        getPreferenceStore().setValue(PREFS_PROXY_REQUIRES_AUTHENTICATION, fRequiresProxyAuthenticationButton.getSelection());
        
        try {
            if(proxyUserNameChanged) {
                ISecurePreferences archiNode = SecureStorageUtils.getSecurePreferences(ArchiPlugin.getInstance().getBundle());
                SecureStorageUtils.putOrRemove(archiNode, IPreferenceConstants.PREFS_PROXY_USERNAME, fProxyUserNameTextField.getText(), true);
            }
            if(proxyPasswordChanged) {
                ISecurePreferences archiNode = SecureStorageUtils.getSecurePreferences(ArchiPlugin.getInstance().getBundle());
                SecureStorageUtils.putOrRemove(archiNode, IPreferenceConstants.PREFS_PROXY_PASSWORD, fProxyUserPasswordTextField.getText(), true);
            }
        }
        catch(StorageException | IOException ex) {
            ex.printStackTrace();
            showErrorDialog(ex);
        }
       
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fUseProxyButton.setSelection(getPreferenceStore().getDefaultBoolean(PREFS_PROXY_ENABLED));
        fProxyHostTextField.setText(getPreferenceStore().getDefaultString(PREFS_PROXY_HOST));
        fProxyPortTextField.setText(getPreferenceStore().getDefaultString(PREFS_PROXY_PORT));
        fRequiresProxyAuthenticationButton.setSelection(getPreferenceStore().getDefaultBoolean(PREFS_PROXY_REQUIRES_AUTHENTICATION));
        updateProxyControls();
        super.performDefaults();
    }
    
    private void updateProxyControls() {
        fProxyHostTextField.setEnabled(fUseProxyButton.getSelection());
        fProxyPortTextField.setEnabled(fUseProxyButton.getSelection());

        fRequiresProxyAuthenticationButton.setEnabled(fUseProxyButton.getSelection());
        fProxyUserNameTextField.setEnabled(fUseProxyButton.getSelection() && fRequiresProxyAuthenticationButton.getSelection());
        fProxyUserPasswordTextField.setEnabled(fUseProxyButton.getSelection() && fRequiresProxyAuthenticationButton.getSelection());
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
    
    
    private void showErrorDialog(Object obj) {
        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.NetworkConnectionsPreferencePage_7,
                obj.toString());
    }

}