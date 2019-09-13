package com.archimatetool.csv.export;

import java.io.File;
import java.io.IOException;

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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.csv.CSVImportExportPlugin;
import com.archimatetool.csv.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Export to CSV Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsCSVPage extends WizardPage implements IPreferenceConstants, CSVConstants {

    private static String HELP_ID = "com.archimatetool.help.ExportAsCSVPage"; //$NON-NLS-1$
    
    private Text fFolderTextField;
    private Combo fDelimiterCombo;
    private Text fFilePrefixTextField;
    private Button fStripNewlinesButton;
    private Button fLeadingCharsButton;
    
    private Label fElementsFileNameLabel;
    private Label fRelationsFileNameLabel;
    private Label fPropertiesFileNameLabel;
    
    private Combo fEncodingCombo;
    
    public ExportAsCSVPage() {
        super("ExportAsCSVPage"); //$NON-NLS-1$
        
        setTitle(Messages.ExportAsCSVPage_0);
        setDescription(Messages.ExportAsCSVPage_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        Group exportGroup = new Group(container, SWT.NULL);
        exportGroup.setText(Messages.ExportAsCSVPage_2);
        exportGroup.setLayout(new GridLayout(3, false));
        exportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportAsCSVPage_3);
        
        fFolderTextField = UIUtils.createSingleTextControl(exportGroup, SWT.BORDER, false);
        fFolderTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fFolderTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
                updateFileLabels();
            }
        });
        
        Button folderButton = new Button(exportGroup, SWT.PUSH);
        folderButton.setText(Messages.ExportAsCSVPage_4);
        folderButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String folderPath = chooseFolderPath();
                if(folderPath != null) {
                    fFolderTextField.setText(folderPath);
                }
            }
        });
        
        label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportAsCSVPage_5);
        
        fDelimiterCombo = new Combo(exportGroup, SWT.READ_ONLY);
        fDelimiterCombo.setItems(DELIMITER_NAMES);
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        fDelimiterCombo.setLayoutData(gd);
        
        label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportAsCSVPage_6);
        
        fFilePrefixTextField = UIUtils.createSingleTextControl(exportGroup, SWT.BORDER, false);
        fFilePrefixTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        gd = new GridData();
        gd.widthHint = 100;
        gd.horizontalSpan = 2;
        fFilePrefixTextField.setLayoutData(gd);
        
        fFilePrefixTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
                updateFileLabels();
            }
        });
        
        // Encoding
        label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportAsCSVPage_15);
        fEncodingCombo = new Combo(exportGroup, SWT.READ_ONLY);
        fEncodingCombo.setItems(ENCODINGS);
        
        Group optionsGroup = new Group(container, SWT.NULL);
        optionsGroup.setText(Messages.ExportAsCSVPage_7);
        optionsGroup.setLayout(new GridLayout(1, false));
        optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fStripNewlinesButton = new Button(optionsGroup, SWT.CHECK);
        fStripNewlinesButton.setText(Messages.ExportAsCSVPage_8);
        
        // See http://www.creativyst.com/Doc/Articles/CSV/CSV01.htm#CSVAndExcel
        fLeadingCharsButton = new Button(optionsGroup, SWT.CHECK);
        fLeadingCharsButton.setText(Messages.ExportAsCSVPage_9);
        
        label = new Label(container, SWT.NULL);
        
        Group filesGroup = new Group(container, SWT.NULL);
        filesGroup.setText(Messages.ExportAsCSVPage_10);
        filesGroup.setLayout(new GridLayout(1, false));
        filesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fElementsFileNameLabel = new Label(filesGroup, SWT.NULL);
        fRelationsFileNameLabel = new Label(filesGroup, SWT.NULL);
        fPropertiesFileNameLabel = new Label(filesGroup, SWT.NULL);
        
        loadPreferences();
        
        // Validate our fields
        validateFields();
    }
    
    /**
     * Update file labels in reponse to text edits
     */
    private void updateFileLabels() {
        fElementsFileNameLabel.setText(fFilePrefixTextField.getText() + ELEMENTS_FILENAME + FILE_EXTENSION);
        fRelationsFileNameLabel.setText(fFilePrefixTextField.getText() + RELATIONS_FILENAME + FILE_EXTENSION);
        fPropertiesFileNameLabel.setText(fFilePrefixTextField.getText() + PROPERTIES_FILENAME + FILE_EXTENSION);
        fElementsFileNameLabel.getParent().layout();
    }
    
    /**
     * @return The export folder path
     */
    String getExportFolderPath() {
        return fFolderTextField.getText();
    }
    
    /**
     * @return The delimiter index
     */
    int getDelimiterIndex() {
        return fDelimiterCombo.getSelectionIndex();
    }
    
    /**
     * @return The prefix for file name
     */
    String getFilenamePrefix() {
        return fFilePrefixTextField.getText().trim();
    }
    
    boolean getStripNewlines() {
        return fStripNewlinesButton.getSelection();
    }
    
    boolean getUseLeadingCharsHack() {
        return fLeadingCharsButton.getSelection();
    }
    
    String getEncoding() {
        return fEncodingCombo.getText();
    }

    private String chooseFolderPath() {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
        dialog.setText(Messages.ExportAsCSVPage_11);
        dialog.setMessage(Messages.ExportAsCSVPage_12);
        dialog.setFilterPath(fFolderTextField.getText());
        return dialog.open();
    }

    private void validateFields() {
        // Folder path
        String folderPath = getExportFolderPath();
        if(!StringUtils.isSetAfterTrim(folderPath)) {
            setErrorMessage(Messages.ExportAsCSVPage_13);
            return;
        }
        
        File file = new File(folderPath);
        if(file.isFile()) {
            setErrorMessage(Messages.ExportAsCSVPage_14);
            return;
        }
        
        // Check valid file name
        try {
            file = new File(getExportFolderPath(), fFilePrefixTextField.getText() + ELEMENTS_FILENAME + FILE_EXTENSION);
            file.getCanonicalPath();
        }
        catch(IOException ex) {
            setErrorMessage(Messages.ExportAsCSVPage_14);
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

    void storePreferences() {
        IPreferenceStore store = CSVImportExportPlugin.getDefault().getPreferenceStore();
        store.setValue(CSV_EXPORT_PREFS_LAST_FOLDER, getExportFolderPath());
        store.setValue(CSV_EXPORT_PREFS_SEPARATOR, getDelimiterIndex());
        store.setValue(CSV_EXPORT_PREFS_FILE_PREFIX, getFilenamePrefix());
        store.setValue(CSV_EXPORT_PREFS_STRIP_NEW_LINES, getStripNewlines());
        store.setValue(CSV_EXPORT_PREFS_LEADING_CHARS_HACK, getUseLeadingCharsHack());
        store.setValue(CSV_EXPORT_PREFS_ENCODING, getEncoding());
    }
    
    void loadPreferences() {
        IPreferenceStore store = CSVImportExportPlugin.getDefault().getPreferenceStore();
        
        // Last saved folder
        String lastFolderPath = store.getString(CSV_EXPORT_PREFS_LAST_FOLDER);
        if(lastFolderPath != null && !"".equals(lastFolderPath)) { //$NON-NLS-1$
            fFolderTextField.setText(lastFolderPath);
        }
        else {
            fFolderTextField.setText(new File(System.getProperty("user.home")).getPath()); //$NON-NLS-1$
        }

        // Delimiter
        int separator = store.getInt(CSV_EXPORT_PREFS_SEPARATOR);
        if(separator > -1 && separator < DELIMITER_NAMES.length) {
            fDelimiterCombo.setText(DELIMITER_NAMES[separator]);
        }
        
        // Last used file prefix
        String lastFilePrefix = store.getString(CSV_EXPORT_PREFS_FILE_PREFIX);
        if(lastFilePrefix != null && !"".equals(lastFilePrefix)) { //$NON-NLS-1$
            fFilePrefixTextField.setText(lastFilePrefix);
        }
        
        // Strip newlines
        boolean selected = store.getBoolean(CSV_EXPORT_PREFS_STRIP_NEW_LINES);
        fStripNewlinesButton.setSelection(selected);
        
        // Leading chars hack
        selected = store.getBoolean(CSV_EXPORT_PREFS_LEADING_CHARS_HACK);
        fLeadingCharsButton.setSelection(selected);
        
        // Encoding
        String encoding = store.getString(CSV_EXPORT_PREFS_ENCODING);
        fEncodingCombo.setText(encoding);
    }
}
