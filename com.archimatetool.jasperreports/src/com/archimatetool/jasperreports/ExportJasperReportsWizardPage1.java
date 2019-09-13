/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;



/**
 * Export Model to Jasper Reports Wizard Page 1
 * 
 * @author Phillip Beauvoir
 */
public class ExportJasperReportsWizardPage1 extends WizardPage {

    private static String HELP_ID = "com.archimatetool.help.ExportJasperReportsWizardPage1"; //$NON-NLS-1$
    
    static final String PREFS_LAST_FOLDER = "JR_LastFolder"; //$NON-NLS-1$
    static final String PREFS_LAST_FILENAME = "JR_Filename"; //$NON-NLS-1$
    static final String PREFS_EXPORT_OPTIONS = "JR_ExportOptions"; //$NON-NLS-1$

    private IArchimateModel fModel;

    private Text fTextOutputFolder;
    private Text fTextFilename;
    private Text fTextReportTitle;
    
    private Button fButtonExportHTML, fButtonExportPDF, fButtonExportDOCX, fButtonExportPPTX,
                   fButtonExportODT, fButtonExportRTF;
    
    private Image fImageHTML, fImagePDF, fImageDOCX, fImagePPTX, fImageODT, fImageRTF;

    public ExportJasperReportsWizardPage1(IArchimateModel model) {
        super("ExportJasperReportsWizardPage1"); //$NON-NLS-1$
        
        setTitle(Messages.ExportJasperReportsWizardPage1_0);
        setDescription(Messages.ExportJasperReportsWizardPage1_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
        
        fModel = model;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        IPreferenceStore store = JasperReportsPlugin.INSTANCE.getPreferenceStore();
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fieldContainer.setLayout(new GridLayout(3, false));
        
        Label label = new Label(fieldContainer, SWT.NONE);
        label.setText(Messages.ExportJasperReportsWizardPage1_2);
        
        fTextOutputFolder = UIUtils.createSingleTextControl(fieldContainer, SWT.BORDER, false);
        fTextOutputFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        String lastFolder = store.getString(PREFS_LAST_FOLDER);
        if(StringUtils.isSet(lastFolder)) {
            fTextOutputFolder.setText(lastFolder);
        }
        else {
            fTextOutputFolder.setText(new File(System.getProperty("user.home"), "exported").getPath()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        fTextOutputFolder.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button button = new Button(fieldContainer, SWT.PUSH);
        button.setText(Messages.ExportJasperReportsWizardPage1_3);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        
        label = new Label(fieldContainer, SWT.NONE);
        label.setText(Messages.ExportJasperReportsWizardPage1_4);
        fTextFilename = UIUtils.createSingleTextControl(fieldContainer, SWT.BORDER, false);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fTextFilename.setLayoutData(gd);
        String lastFilename = store.getString(PREFS_LAST_FILENAME);
        if(StringUtils.isSet(lastFilename)) {
            fTextFilename.setText(lastFilename);
        }
        else {
            fTextFilename.setText("report-filename"); //$NON-NLS-1$
        }

        fTextFilename.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });

        label = new Label(fieldContainer, SWT.NONE);
        label.setText(Messages.ExportJasperReportsWizardPage1_5);
        fTextReportTitle = UIUtils.createSingleTextControl(fieldContainer, SWT.BORDER, false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fTextReportTitle.setLayoutData(gd);
        fTextReportTitle.setText(fModel.getName());
        
        fTextReportTitle.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        loadImages();
        
        parent.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposeImages();
            }
        });
        
        Group exportTypesGroup = new Group(container, SWT.NULL);
        exportTypesGroup.setText(Messages.ExportJasperReportsWizardPage1_6);
        exportTypesGroup.setLayout(new GridLayout(4, false));
        exportTypesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        int exportOptions = store.getInt(PREFS_EXPORT_OPTIONS);
        if(exportOptions == 0) {
            exportOptions = JasperReportsExporter.EXPORT_HTML;
        }
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageHTML);
        fButtonExportHTML = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportHTML.setText(Messages.ExportJasperReportsWizardPage1_7);
        fButtonExportHTML.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportHTML.setSelection((exportOptions & JasperReportsExporter.EXPORT_HTML) != 0);
        fButtonExportHTML.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImagePDF);
        fButtonExportPDF = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportPDF.setText(Messages.ExportJasperReportsWizardPage1_8);
        fButtonExportPDF.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportPDF.setSelection((exportOptions & JasperReportsExporter.EXPORT_PDF) != 0);
        fButtonExportPDF.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageRTF);
        fButtonExportRTF = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportRTF.setText(Messages.ExportJasperReportsWizardPage1_9);
        fButtonExportRTF.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportRTF.setSelection((exportOptions & JasperReportsExporter.EXPORT_RTF) != 0);
        fButtonExportRTF.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageDOCX);
        fButtonExportDOCX = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportDOCX.setText(Messages.ExportJasperReportsWizardPage1_10);
        fButtonExportDOCX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportDOCX.setSelection((exportOptions & JasperReportsExporter.EXPORT_DOCX) != 0);
        fButtonExportDOCX.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImagePPTX);
        fButtonExportPPTX = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportPPTX.setText(Messages.ExportJasperReportsWizardPage1_11);
        fButtonExportPPTX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportPPTX.setSelection((exportOptions & JasperReportsExporter.EXPORT_PPT) != 0);
        fButtonExportPPTX.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageODT);
        fButtonExportODT = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportODT.setText(Messages.ExportJasperReportsWizardPage1_12);
        fButtonExportODT.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportODT.setSelection((exportOptions & JasperReportsExporter.EXPORT_ODT) != 0);
        fButtonExportODT.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
    }
    
    /**
     * @return The Folder for the Reports
     */
    public File getExportFolder() {
        return new File(fTextOutputFolder.getText());
    }
    
    public String getExportFilename() {
        return fTextFilename.getText();
    }
    
    public String getReportTitle() {
        return fTextReportTitle.getText();
    }
    
    /**
     * @return The Export options
     */
    public int getExportOptions() {
        int options = 0;
        options |= isExportPDF() ? JasperReportsExporter.EXPORT_PDF : 0;
        options |= isExportHTML() ? JasperReportsExporter.EXPORT_HTML : 0;
        options |= isExportDOCX() ? JasperReportsExporter.EXPORT_DOCX : 0;
        options |= isExportPPT() ? JasperReportsExporter.EXPORT_PPT : 0;
        options |= isExportODT() ? JasperReportsExporter.EXPORT_ODT : 0;
        options |= isExportRTF() ? JasperReportsExporter.EXPORT_RTF : 0;
        return options;
    }
    
    public boolean isExportHTML() {
        return fButtonExportHTML.getSelection();
    }
    
    public boolean isExportPDF() {
        return fButtonExportPDF.getSelection();
    }
    
    public boolean isExportDOCX() {
        return fButtonExportDOCX.getSelection();
    }
    
    public boolean isExportPPT() {
        return fButtonExportPPTX.getSelection();
    }
    
    public boolean isExportODT() {
        return fButtonExportODT.getSelection();
    }
    
    public boolean isExportRTF() {
        return fButtonExportRTF.getSelection();
    }
    
    private void validateFields() {
        boolean isOK = isExportHTML() || isExportPDF() || isExportDOCX() || isExportPPT() ||
                isExportODT() || isExportRTF();
        
        if(!isOK) {
            updateStatus(Messages.ExportJasperReportsWizardPage1_13);
            return;
        }
        
        String s = fTextOutputFolder.getText();
        if("".equals(s.trim())) { //$NON-NLS-1$
            updateStatus(Messages.ExportJasperReportsWizardPage1_14);
            return;
        }
        
        s = fTextFilename.getText();
        if("".equals(s.trim())) { //$NON-NLS-1$
            updateStatus(Messages.ExportJasperReportsWizardPage1_15);
            return;
        }
        
        s = fTextReportTitle.getText();
        if("".equals(s.trim())) { //$NON-NLS-1$
            updateStatus(Messages.ExportJasperReportsWizardPage1_16);
            return;
        }
        
        // OK
        updateStatus(null);
    }

    /**
     * Update the page status
     */
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    private void handleBrowse() {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText(Messages.ExportJasperReportsWizardPage1_17);
        dialog.setMessage(Messages.ExportJasperReportsWizardPage1_18);
        dialog.setFilterPath(fTextOutputFolder.getText());
        
        String path = dialog.open();
        if(path != null) {
            fTextOutputFolder.setText(path);
        }
    }
    
    private void loadImages() {
        fImageDOCX = loadImage("img/docx.png"); //$NON-NLS-1$
        fImageHTML = loadImage("img/html.png"); //$NON-NLS-1$
        fImageODT = loadImage("img/odt.png"); //$NON-NLS-1$
        fImagePDF = loadImage("img/pdf.gif"); //$NON-NLS-1$
        fImagePPTX = loadImage("img/pptx.png"); //$NON-NLS-1$
        fImageRTF = loadImage("img/rtf.png"); //$NON-NLS-1$
    }
    
    private void disposeImages() {
        if(fImageDOCX != null) {
            fImageDOCX.dispose();
        }
        if(fImageHTML != null) {
            fImageHTML.dispose();
        }
        if(fImageODT != null) {
            fImageODT.dispose();
        }
        if(fImagePDF != null) {
            fImagePDF.dispose();
        }
        if(fImagePPTX != null) {
            fImagePPTX.dispose();
        }
        if(fImageRTF != null) {
            fImageRTF.dispose();
        }
    }
    
    private Image loadImage(String name) {
        Image image = null;
        InputStream is = getClass().getResourceAsStream(name);
        if(is != null) {
            image = new Image(null, is);
            try {
                is.close();
            }
            catch(IOException ex) {
            }
        }
        return image;
    }

    void storePreferences() {
        IPreferenceStore store = JasperReportsPlugin.INSTANCE.getPreferenceStore();
        store.setValue(PREFS_LAST_FOLDER, getExportFolder().getAbsolutePath());
        store.setValue(PREFS_LAST_FILENAME, getExportFilename());
        store.setValue(PREFS_EXPORT_OPTIONS, getExportOptions());
    }
}
