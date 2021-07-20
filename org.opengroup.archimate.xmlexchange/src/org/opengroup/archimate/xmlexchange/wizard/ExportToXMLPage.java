/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange.wizard;

import java.io.File;
import java.util.Locale;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.opengroup.archimate.xmlexchange.IPreferenceConstants;
import org.opengroup.archimate.xmlexchange.IXMLExchangeGlobals;
import org.opengroup.archimate.xmlexchange.XMLExchangePlugin;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Export to XML Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class ExportToXMLPage extends WizardPage implements IPreferenceConstants {

    private static String HELP_ID = "com.archimatetool.help.ExportToXMLPage"; //$NON-NLS-1$
    
    private Text fFileTextField;
    private Button fOrganiseButton;
    private Button fIncludeXSDButton;
    private Button fValidateAfterExportButton;
    private Combo fLanguageCombo;
    
    private String fModelName;
    
    public ExportToXMLPage(String modelName) {
        super("ExportToXMLPage"); //$NON-NLS-1$
        
        fModelName = modelName;
        
        setTitle(Messages.ExportToXMLPage_0);
        setDescription(Messages.ExportToXMLPage_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        Group exportGroup = new Group(container, SWT.NULL);
        exportGroup.setText(Messages.ExportToXMLPage_2);
        exportGroup.setLayout(new GridLayout(3, false));
        exportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportToXMLPage_3);
        
        fFileTextField = UIUtils.createSingleTextControl(exportGroup, SWT.BORDER, false);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        String fileName = StringUtils.isSet(fModelName) ? fModelName + ".xml" : "exported.xml"; //$NON-NLS-1$ //$NON-NLS-2$
        
        // Get last folder used
        String lastFolderName = XMLExchangePlugin.INSTANCE.getPreferenceStore().getString(XMLEXCHANGE_PREFS_LAST_FILE_LOCATION);
        File lastFolder = new File(lastFolderName);
        
        if(lastFolder.exists() && lastFolder.isDirectory()) {
            fFileTextField.setText(new File(lastFolder, fileName).getPath());
        }
        else {
            fFileTextField.setText(new File(System.getProperty("user.home"), fileName).getPath()); //$NON-NLS-1$
        }
        
        fFileTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button fileButton = new Button(exportGroup, SWT.PUSH);
        fileButton.setText(Messages.ExportToXMLPage_10);
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
        optionsGroup.setText(Messages.ExportToXMLPage_4);
        optionsGroup.setLayout(new GridLayout(2, false));
        optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        
        fOrganiseButton = new Button(optionsGroup, SWT.CHECK);
        fOrganiseButton.setSelection(XMLExchangePlugin.INSTANCE.getPreferenceStore().getBoolean(XMLEXCHANGE_PREFS_ORGANISATION));
        fOrganiseButton.setText(Messages.ExportToXMLPage_5);
        fOrganiseButton.setLayoutData(gd);
        
        fValidateAfterExportButton = new Button(optionsGroup, SWT.CHECK);
        fValidateAfterExportButton.setSelection(XMLExchangePlugin.INSTANCE.getPreferenceStore().getBoolean(XMLEXCHANGE_PREFS_VALIDATE_AFTER_EXPORT));
        fValidateAfterExportButton.setText(Messages.ExportToXMLPage_11);
        fValidateAfterExportButton.setLayoutData(gd);
        
        fIncludeXSDButton = new Button(optionsGroup, SWT.CHECK);
        fIncludeXSDButton.setSelection(XMLExchangePlugin.INSTANCE.getPreferenceStore().getBoolean(XMLEXCHANGE_PREFS_INCLUDE_XSD));
        fIncludeXSDButton.setText(Messages.ExportToXMLPage_6);
        fIncludeXSDButton.setLayoutData(gd);
        
        label = new Label(optionsGroup, SWT.NULL);
        label.setText(Messages.ExportToXMLPage_7);
        
        fLanguageCombo = new Combo(optionsGroup, SWT.READ_ONLY);
        fLanguageCombo.setItems(Locale.getISOLanguages());
        gd = new GridData();
        gd.widthHint = 70;
        fLanguageCombo.setLayoutData(gd);
        
        String lastLanguage = XMLExchangePlugin.INSTANCE.getPreferenceStore().getString(XMLEXCHANGE_PREFS_LANGUAGE);
        if(StringUtils.isSet(lastLanguage)) {
            fLanguageCombo.setText(lastLanguage);
        }
        else {
            String code = Locale.getDefault().getLanguage();
            if(code == null) {
                code = "en"; //$NON-NLS-1$
            }
            fLanguageCombo.setText(code);
        }
    }

    String getFileName() {
        return fFileTextField.getText();
    }
    
    boolean doSaveOrganisation() {
        return fOrganiseButton.getSelection();
    }

    boolean doIncludeXSD() {
        return fIncludeXSDButton.getSelection();
    }
    
    boolean doValidateAfterExport() {
        return fValidateAfterExportButton.getSelection();
    }
    
    String getLanguageCode() {
        return fLanguageCombo.getText();
    }

    private void validateFields() {
        String fileName = getFileName();
        if(!StringUtils.isSetAfterTrim(fileName)) {
            setErrorMessage(Messages.ExportToXMLPage_8);
            return;
        }
        
        setErrorMessage(null);
    }

    /**
     * Update the page status
     */
    @Override
    public void setErrorMessage(String message) {
        super.setErrorMessage(message);
        setPageComplete(message == null);
    }

    private File chooseFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText(Messages.ExportToXMLPage_9);
        
        File file = new File(fFileTextField.getText());
        dialog.setFileName(file.getName());
        
        dialog.setFilterExtensions(new String[] { IXMLExchangeGlobals.FILE_EXTENSION_WILDCARD, "*.*" } ); //$NON-NLS-1$
        
        // Does nothing on macOS 10.15+. On Windows will work after Eclipse 4.21
        dialog.setOverwrite(false);
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(IXMLExchangeGlobals.FILE_EXTENSION)) {
            path += IXMLExchangeGlobals.FILE_EXTENSION;
        }
        
        return new File(path);
    }

    void storePreferences() {
        IPreferenceStore store = XMLExchangePlugin.INSTANCE.getPreferenceStore();
        
        File file = new File(getFileName());
        if(file.getParentFile().exists()) {
            store.setValue(XMLEXCHANGE_PREFS_LAST_FILE_LOCATION, file.getParentFile().getPath());
        }
        else {
            store.setValue(XMLEXCHANGE_PREFS_LAST_FILE_LOCATION, ""); //$NON-NLS-1$
        }
        
        store.setValue(XMLEXCHANGE_PREFS_ORGANISATION, doSaveOrganisation());
        store.setValue(XMLEXCHANGE_PREFS_INCLUDE_XSD, doIncludeXSD());
        store.setValue(XMLEXCHANGE_PREFS_VALIDATE_AFTER_EXPORT, doValidateAfterExport());
        store.setValue(XMLEXCHANGE_PREFS_LANGUAGE, getLanguageCode());
    }
}
