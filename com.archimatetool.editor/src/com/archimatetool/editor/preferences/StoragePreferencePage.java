package com.archimatetool.editor.preferences;

import org.eclipse.equinox.internal.security.ui.storage.TabAdvanced;
import org.eclipse.equinox.internal.security.ui.storage.TabContents;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class StoragePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private static final String HELP_ID = "org.eclipse.equinox.security.ui.sec_storage_preferences_context"; //$NON-NLS-1$

	protected TabPassword passwordTab;
	protected TabContents contentsTab;
	protected TabAdvanced advancedTab;

	public StoragePreferencePage() {
		//empty
	}

	@Override
	public void init(IWorkbench workbench) {
		// nothing to do
	}

	@Override
	protected Control createContents(Composite parent) {

		final TabFolder folder = new TabFolder(parent, SWT.TOP);

		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.horizontalSpan = 1;
		folder.setLayoutData(gd);

		passwordTab = new TabPassword(folder, 0, getShell());
		contentsTab = new TabContents(folder, 1, getShell());
		advancedTab = new TabAdvanced(folder, 2, getShell());
		folder.setSelection(0);

		folder.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			int i = folder.getSelectionIndex();
			if (i == 0 && passwordTab != null) // password page
				passwordTab.onActivated();
		}));
		Dialog.applyDialogFont(folder);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), HELP_ID);
		return folder;
	}

	@Override
	protected void performDefaults() {
		if (passwordTab != null)
			passwordTab.performDefaults();
		if (advancedTab != null)
			advancedTab.performDefaults();
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		if (passwordTab != null)
			passwordTab.performOk();
		if (advancedTab != null)
			advancedTab.performOk();
		return super.performOk();
	}
}
