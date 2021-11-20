/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Import Model Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class ImportModelPage extends WizardPage implements IPreferenceConstants {

    private static String HELP_ID = "com.archimatetool.help.ImportModel"; //$NON-NLS-1$
    
    private Text fFileTextField;
    private Button fUpdateButton;
    private Button fUpdateAllButton;
    private Button fShowStatusDialog;
    
    public ImportModelPage() {
        super("ImportModelPage"); //$NON-NLS-1$
        
        setTitle(Messages.ImportModelPage_0);
        setDescription(Messages.ImportModelPage_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        Group importGroup = new Group(container, SWT.NULL);
        importGroup.setText(Messages.ImportModelPage_2);
        importGroup.setLayout(new GridLayout(3, false));
        importGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label label = new Label(importGroup, SWT.NULL);
        label.setText(Messages.ImportModelPage_3);
        
        fFileTextField = UIUtils.createSingleTextControl(importGroup, SWT.BORDER, false);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Get last file used
        String lastFileName = ImporterPlugin.INSTANCE.getPreferenceStore().getString(IMPORTER_PREFS_LAST_FILE);
        File lastFile = new File(lastFileName);
        
        if(lastFile.exists() && lastFile.isFile()) {
            fFileTextField.setText(lastFileName);
        }
        
        fFileTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button fileButton = new Button(importGroup, SWT.PUSH);
        fileButton.setText(Messages.ImportModelPage_4);
        fileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                File file = chooseFile();
                if(file != null) {
                    fFileTextField.setText(file.getPath());
                }
            }
        });
        
        Group optionsGroup = new Group(container, SWT.NULL);
        optionsGroup.setText(Messages.ImportModelPage_5);
        optionsGroup.setLayout(new GridLayout(2, false));
        optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        
        fUpdateButton = new Button(optionsGroup, SWT.CHECK);
        fUpdateButton.setSelection(ImporterPlugin.INSTANCE.getPreferenceStore().getBoolean(IMPORTER_PREFS_UPDATE));
        fUpdateButton.setText(Messages.ImportModelPage_6);
        fUpdateButton.setLayoutData(gd);
        fUpdateButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                setButtonState();
            }
        });
        
        fUpdateAllButton = new Button(optionsGroup, SWT.CHECK);
        fUpdateAllButton.setSelection(ImporterPlugin.INSTANCE.getPreferenceStore().getBoolean(IMPORTER_PREFS_UPDATE_ALL));
        fUpdateAllButton.setText(Messages.ImportModelPage_8);
        fUpdateAllButton.setLayoutData(gd);
        
        fShowStatusDialog = new Button(optionsGroup, SWT.CHECK);
        fShowStatusDialog.setSelection(ImporterPlugin.INSTANCE.getPreferenceStore().getBoolean(IMPORTER_PREFS_SHOW_STATUS_DIALOG));
        fShowStatusDialog.setText(Messages.ImportModelPage_9);
        fShowStatusDialog.setLayoutData(gd);
        
        setButtonState();
    }

    String getFileName() {
        return fFileTextField.getText();
    }

    boolean shouldUpdate() {
        return fUpdateButton.getSelection();
    }
    
    boolean shouldUpdateAll() {
        return fUpdateAllButton.getSelection();
    }
    
    boolean shouldShowStatusDialog() {
        return fShowStatusDialog.getSelection();
    }
    
    private void setButtonState() {
        fUpdateAllButton.setEnabled(fUpdateButton.getSelection());
        if(!fUpdateButton.getSelection()) {
            fUpdateAllButton.setSelection(false);
        }
    }
    
    private void validateFields() {
        String fileName = getFileName();
        if(!StringUtils.isSetAfterTrim(fileName)) {
            setErrorMessage(Messages.ImportModelPage_7);
            return;
        }
        
        setErrorMessage(null);
    }
    
    private File chooseFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.archimate", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        dialog.setFileName(getFileName());
        
        String path = dialog.open();
        
        return path != null ? new File(path) : null;
    }

    void storePreferences() {
        IPreferenceStore store = ImporterPlugin.INSTANCE.getPreferenceStore();
        store.setValue(IMPORTER_PREFS_LAST_FILE, getFileName());
        store.setValue(IMPORTER_PREFS_UPDATE, shouldUpdate());
        store.setValue(IMPORTER_PREFS_UPDATE_ALL, shouldUpdateAll());
        store.setValue(IMPORTER_PREFS_SHOW_STATUS_DIALOG, shouldShowStatusDialog());
    }
}
