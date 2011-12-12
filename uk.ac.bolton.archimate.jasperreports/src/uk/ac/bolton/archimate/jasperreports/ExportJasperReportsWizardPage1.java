/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.ui.UIUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * Export Model to Jasper Reports Wizard Page 1
 * 
 * @author Phillip Beauvoir
 */
public class ExportJasperReportsWizardPage1 extends WizardPage {

    public static String HELPID = "uk.ac.bolton.archimate.help.ExportJasperReportsWizardPage1"; //$NON-NLS-1$
    
    static String SAVE_DIR = System.getProperty("user.home");
    static String REPORT_FILENAME = "archi-report";
    static boolean IS_HTML = true, IS_PDF = true, IS_WORD = true, IS_PPT, IS_RTF, IS_ODT;
    
    private IArchimateModel fModel;

    private Text fTextOutputFolder;
    private Text fTextFilename;
    private Text fTextReportTitle;
    
    private Button fButtonExportHTML, fButtonExportPDF, fButtonExportDOCX, fButtonExportPPTX,
                   fButtonExportODT, fButtonExportRTF;
    
    private Image fImageHTML, fImagePDF, fImageDOCX, fImagePPTX, fImageODT, fImageRTF;

    public ExportJasperReportsWizardPage1(IArchimateModel model) {
        super("ExportJasperReportsWizardPage1");
        
        setTitle("Generate Jasper Reports");
        setDescription("Choose the output location, report title and which formats to generate.");
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(ImageFactory.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
        
        fModel = model;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELPID);
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fieldContainer.setLayout(new GridLayout(3, false));
        
        Label label = new Label(fieldContainer, SWT.NONE);
        label.setText("Report location:");
        
        fTextOutputFolder = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
        fTextOutputFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fTextOutputFolder.setText(SAVE_DIR);
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fTextOutputFolder);
        fTextOutputFolder.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button button = new Button(fieldContainer, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        
        label = new Label(fieldContainer, SWT.NONE);
        label.setText("Report filename:");
        fTextFilename = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fTextFilename.setLayoutData(gd);
        fTextFilename.setText(REPORT_FILENAME);
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fTextFilename);
        fTextFilename.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });

        label = new Label(fieldContainer, SWT.NONE);
        label.setText("Report title:");
        fTextReportTitle = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fTextReportTitle.setLayoutData(gd);
        fTextReportTitle.setText(fModel.getName());
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fTextReportTitle);
        fTextReportTitle.addModifyListener(new ModifyListener() {
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
        exportTypesGroup.setText("Formats to generate:");
        exportTypesGroup.setLayout(new GridLayout(4, false));
        exportTypesGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageHTML);
        fButtonExportHTML = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportHTML.setText("HTML");
        fButtonExportHTML.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportHTML.setSelection(IS_HTML);
        fButtonExportHTML.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImagePDF);
        fButtonExportPDF = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportPDF.setText("PDF");
        fButtonExportPDF.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportPDF.setSelection(IS_PDF);
        fButtonExportPDF.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageRTF);
        fButtonExportRTF = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportRTF.setText("RTF");
        fButtonExportRTF.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportRTF.setSelection(IS_RTF);
        fButtonExportRTF.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageDOCX);
        fButtonExportDOCX = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportDOCX.setText("MS Word");
        fButtonExportDOCX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportDOCX.setSelection(IS_WORD);
        fButtonExportDOCX.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImagePPTX);
        fButtonExportPPTX = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportPPTX.setText("MS Powerpoint");
        fButtonExportPPTX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportPPTX.setSelection(IS_PPT);
        fButtonExportPPTX.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                validateFields();
            }
        });
        
        label = new Label(exportTypesGroup, SWT.NONE);
        label.setImage(fImageODT);
        fButtonExportODT = new Button(exportTypesGroup, SWT.CHECK);
        fButtonExportODT.setText("Open Office");
        fButtonExportODT.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fButtonExportODT.setSelection(IS_ODT);
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
            updateStatus("Choose at least one export format");
            return;
        }
        
        String s = fTextOutputFolder.getText();
        if("".equals(s.trim())) { //$NON-NLS-1$
            updateStatus("Choose export folder");
            return;
        }
        
        s = fTextFilename.getText();
        if("".equals(s.trim())) { //$NON-NLS-1$
            updateStatus("Choose file name");
            return;
        }
        
        s = fTextReportTitle.getText();
        if("".equals(s.trim())) { //$NON-NLS-1$
            updateStatus("Choose report title");
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
        dialog.setText("Report");
        dialog.setMessage("Choose a folder in which to generate the report.");
        String path = dialog.open();
        if(path != null) {
            fTextOutputFolder.setText(path);
        }
    }
    
    private void loadImages() {
        fImageDOCX = loadImage("img/docx.png");
        fImageHTML = loadImage("img/html.png");
        fImageODT = loadImage("img/odt.png");
        fImagePDF = loadImage("img/pdf.gif");
        fImagePPTX = loadImage("img/pptx.png");
        fImageRTF = loadImage("img/rtf.png");
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

    public void saveSettings() {
        SAVE_DIR = fTextOutputFolder.getText();
        REPORT_FILENAME = fTextFilename.getText();
        IS_HTML = isExportHTML();
        IS_PDF = isExportPDF();
        IS_PPT = isExportPPT();
        IS_WORD = isExportDOCX();
        IS_RTF = isExportRTF();
        IS_ODT = isExportODT();
    }

}
