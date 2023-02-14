package com.archimatetool.editor.preferences;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.equinox.internal.security.storage.friends.IStorageConstants;
import org.eclipse.equinox.internal.security.storage.friends.InternalExchangeUtils;
import org.eclipse.equinox.internal.security.storage.friends.PasswordProviderDescription;
import org.eclipse.equinox.internal.security.storage.friends.ReEncrypter;
import org.eclipse.equinox.internal.security.ui.nls.SecUIMessages;
import org.eclipse.equinox.internal.security.ui.storage.ChangePasswordWizardDialog;
import org.eclipse.equinox.internal.security.ui.storage.PasswordRecoveryDialog;
import org.eclipse.equinox.internal.security.ui.storage.StorageUtils;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

@SuppressWarnings("restriction")
public class TabPassword {

	private static final String PREFERENCES_PLUGIN = "org.eclipse.equinox.security"; //$NON-NLS-1$

	private final static String PASSWORD_RECOVERY_NODE = "/org.eclipse.equinox.secure.storage/recovery/"; //$NON-NLS-1$

	protected Table providerTable;

	protected Button buttonClearPassword;
	protected Button buttonChangePassword;
	protected Button buttonRecoverPassword;

	protected Text detailsText;

	protected boolean providerModified = false;

	public TabPassword(TabFolder folder, int index, final Shell shell) {
		TabItem tab = new TabItem(folder, SWT.NONE, index);
		tab.setText(SecUIMessages.tabPassword);
		Composite page = new Composite(folder, SWT.NONE);
		page.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		tab.setControl(page);

		Group passwordGroup = new Group(page, SWT.NONE);
		passwordGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		passwordGroup.setLayout(new GridLayout(2, false));
		passwordGroup.setText(SecUIMessages.passwordCacheGroup);

		buttonClearPassword = new Button(passwordGroup, SWT.PUSH);
		buttonClearPassword.setText(SecUIMessages.logoutButton);
		buttonClearPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		buttonClearPassword.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				InternalExchangeUtils.passwordProvidersReset();
				enableLogout();
			}
		});
		setButtonSize(buttonClearPassword);

		Label passwordNote = new Label(passwordGroup, SWT.WRAP);
		GridData labelData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		labelData.widthHint = 340;
		passwordNote.setLayoutData(labelData);
		passwordNote.setText(SecUIMessages.passwordCacheNote);

		Group providersGroup = new Group(page, SWT.NONE);
		providersGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		providersGroup.setLayout(new GridLayout());
		// Philipus moved this further down...
		//providersGroup.setText(SecUIMessages.providerGroup);

		Label providersNote = new Label(providersGroup, SWT.WRAP);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.widthHint = 500;
		providersNote.setLayoutData(gridData);
		providersNote.setText(SecUIMessages.providerDescription);

		Composite providersComp = new Composite(providersGroup, SWT.NONE);
		providersComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		providersComp.setLayout(gridLayout);

		providerTable = new Table(providersComp, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);
		GridData tableData = new GridData(GridData.FILL, GridData.FILL, true, true);
		providerTable.setLayoutData(tableData);
		providerTable.setLinesVisible(true);
		providerTable.setHeaderVisible(true);
		fillProviderTable();

		providerTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if ((e.detail & SWT.CHECK) != 0)
					providerModified = true;
				enableButtons();
				updateDescription();
			}
		});
		GridDataFactory.defaultsFor(providerTable).span(1, 2).applyTo(providerTable);

		buttonChangePassword = new Button(providersComp, SWT.PUSH);
		buttonChangePassword.setText(SecUIMessages.changePasswordButton);
		buttonChangePassword.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		buttonChangePassword.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				PasswordProviderDescription selectedModule = getSelectedModule();
				if (selectedModule == null)
					return;
				String moduleID = getSelectedModuleID();
				ISecurePreferences rootNode = SecurePreferencesFactory.getDefault();
				if (selectedModule.hasHint(InternalExchangeUtils.HINT_PASSWORD_AUTOGEN)) {
					// do replacement behind the scene without showing the wizard
					changePassword(rootNode, moduleID, selectedModule.getName(), shell);
				} else {
					// show the wizard to provide separate "old" and "new" password entries
					ChangePasswordWizardDialog dialog = new ChangePasswordWizardDialog(shell, rootNode, moduleID);
					dialog.open();
				}
				enableLogout();
			}
		});
		setButtonSize(buttonChangePassword);

		buttonRecoverPassword = new Button(providersComp, SWT.PUSH);
		buttonRecoverPassword.setText(SecUIMessages.recoverPasswordButton);
		buttonRecoverPassword.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		buttonRecoverPassword.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String moduleID = getSelectedModuleID();
				ISecurePreferences rootNode = SecurePreferencesFactory.getDefault();
				String[] questions = InternalExchangeUtils.getPasswordRecoveryQuestions(rootNode, moduleID);
				if (questions.length == 0)
					return; // no password recovery questions were setup
				PasswordRecoveryDialog dialog = new PasswordRecoveryDialog(questions, shell, moduleID);
				dialog.open();
				enableLogout();
			}
		});
		setButtonSize(buttonRecoverPassword);

		enableButtons();

		Label descriptionLabel = new Label(providersComp, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		descriptionLabel.setText(SecUIMessages.providerDetails);

		detailsText = new Text(providersComp, SWT.MULTI | SWT.LEAD | SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		
        // Phillipus moved this here to workaround Mac Ventura bug
		providersGroup.setText(SecUIMessages.providerGroup);
		
		detailsText.setBackground(detailsText.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gridData.widthHint = 300;
		gridData.heightHint = 65;
		detailsText.setLayoutData(gridData);
		updateDescription();

		GridLayoutFactory.fillDefaults().margins(LayoutConstants.getSpacing()).generateLayout(page);
	}

	private void fillProviderTable() {
		TableColumn idColumn = new TableColumn(providerTable, SWT.LEAD);
		idColumn.setText(SecUIMessages.idColumn);

		TableColumn priorityColumn = new TableColumn(providerTable, SWT.CENTER);
		priorityColumn.setText(SecUIMessages.priorityColumn);

		HashSet<String> disabledModules = getDisabledModules();
		for (PasswordProviderDescription module : InternalExchangeUtils.passwordProvidersFind()) {
			TableItem item = new TableItem(providerTable, SWT.NONE);
			item.setText(new String[] {module.getName(), Integer.toString(module.getPriority())});
			item.setData(module);
			if (disabledModules == null)
				item.setChecked(true);
			else
				item.setChecked(!disabledModules.contains(module.getId()));
		}

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(5));
		layout.addColumnData(new ColumnWeightData(1));
		providerTable.setLayout(layout);

		if (providerTable.getItemCount() > 0)
			providerTable.select(0);
	}

	protected PasswordProviderDescription getSelectedModule() {
		if (providerTable == null)
			return null;
		TableItem[] items = providerTable.getSelection();
		if (items.length == 0)
			return null;
		return ((PasswordProviderDescription) items[0].getData());
	}

	protected String getSelectedModuleID() {
		PasswordProviderDescription selectedModule = getSelectedModule();
		if (selectedModule == null)
			return null;
		return selectedModule.getId();
	}

	protected void enableButtons() {
		String moduleID = getSelectedModuleID();
		if (moduleID == null) { // nothing selected
			buttonChangePassword.setEnabled(false);
			buttonRecoverPassword.setEnabled(false);
		} else {
			buttonChangePassword.setEnabled(true);

			ISecurePreferences rootNode = SecurePreferencesFactory.getDefault();
			String path = PASSWORD_RECOVERY_NODE + moduleID;
			boolean recoveryAvailable = rootNode.nodeExists(path);
			buttonRecoverPassword.setEnabled(recoveryAvailable);
		}
		enableLogout();
	}

	protected HashSet<String> getDisabledModules() {
		IScopeContext[] scopes = {ConfigurationScope.INSTANCE, DefaultScope.INSTANCE};
		IPreferencesService preferencesService = Platform.getPreferencesService();
		String defaultPreferenceValue = ""; //$NON-NLS-1$
		String tmp = preferencesService.getString(PREFERENCES_PLUGIN, IStorageConstants.DISABLED_PROVIDERS_KEY, defaultPreferenceValue, scopes);
		HashSet<String> disabledModules = splitModuleIds(tmp);
		return disabledModules;
	}

	public void performDefaults() {
		if (providerTable == null)
			return;
		Set<String> defaultDisabledModules = getDefaultDisabledModules();

		TableItem[] items = providerTable.getItems();
		for (TableItem item : items) {
			String moduleId = getModuleId(item);
			boolean enabled = defaultDisabledModules == null || moduleId == null || !defaultDisabledModules.contains(moduleId);
			if (item.getChecked() != enabled) {
				item.setChecked(enabled);
				providerModified = true;
			}
		}
	}

	public void performOk() {
		if (!providerModified)
			return;
		// save current selection
		StringBuilder tmp = new StringBuilder();
		boolean first = true;
		TableItem[] items = providerTable.getItems();
		for (TableItem item : items) {
			if (item.getChecked()) {
				continue;
			}
			if (!first)
				tmp.append(',');
			else
				first = false;
			tmp.append(((PasswordProviderDescription) item.getData()).getId());
		}

		IEclipsePreferences node = ConfigurationScope.INSTANCE.getNode(PREFERENCES_PLUGIN);
		node.put(IStorageConstants.DISABLED_PROVIDERS_KEY, tmp.toString());

		try {
			node.flush();
		} catch (BackingStoreException e) {
			// nothing can be done
		}

		// logout so that previously selected default provider is not reused
		InternalExchangeUtils.passwordProvidersReset();
	}

	public void onActivated() {
		enableLogout();
	}

	protected void enableLogout() {
		buttonClearPassword.setEnabled(InternalExchangeUtils.isLoggedIn());
	}

	protected void setButtonSize(Button button) {
		GridDataFactory.defaultsFor(button).align(SWT.FILL, SWT.BEGINNING).grab(false, false).applyTo(button);
	}

	protected boolean changePassword(ISecurePreferences node, String moduleID, String name, Shell shell) {
		ReEncrypter reEncrypter = new ReEncrypter(node, moduleID);
		if (!reEncrypter.decrypt()) {
			MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_WARNING);
			messageBox.setText(SecUIMessages.changePasswordWizardTitle);
			messageBox.setMessage(SecUIMessages.wizardDecodeWarning);
			if (messageBox.open() == SWT.YES)
				return false;
		}

		if (!reEncrypter.switchToNewPassword()) {
			MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
			messageBox.setText(SecUIMessages.changePasswordWizardTitle);
			messageBox.setMessage(SecUIMessages.wizardSwitchError);
			messageBox.open();
			return false;
		}
		reEncrypter.encrypt();

		// all good
		String msg = NLS.bind(SecUIMessages.passwordChangeDone, name);
		MessageDialog.openInformation(StorageUtils.getShell(), SecUIMessages.generalDialogTitle, msg);
		return true;
	}

	protected void updateDescription() {
		PasswordProviderDescription selectedModule = getSelectedModule();
		if (selectedModule != null && detailsText != null)
			detailsText.setText(selectedModule.getDescription());
	}

	private HashSet<String> getDefaultDisabledModules() {
		String defaultPreferenceValue = ""; //$NON-NLS-1$
		Preferences pluginNode = DefaultScope.INSTANCE.getNode(PREFERENCES_PLUGIN);
		String tmp = pluginNode.get(IStorageConstants.DISABLED_PROVIDERS_KEY, defaultPreferenceValue);
		HashSet<String> defaultDisabledModules = splitModuleIds(tmp);
		return defaultDisabledModules;
	}

	private String getModuleId(TableItem item) {
		String moduleId = null;
		Object itemData = item.getData();
		if (itemData instanceof PasswordProviderDescription) {
			PasswordProviderDescription module = (PasswordProviderDescription) itemData;
			moduleId = module.getId();
		}
		return moduleId;
	}

	private static HashSet<String> splitModuleIds(String joinedModuleIds) {
		if (joinedModuleIds == null || joinedModuleIds.isEmpty())
			return null;
		HashSet<String> modules = new HashSet<>();
		String[] disabledProviders = joinedModuleIds.split(","); //$NON-NLS-1$
		for (String disabledProvider : disabledProviders) {
			modules.add(disabledProvider);
		}
		return modules;
	}
}
