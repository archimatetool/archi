/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.templates.wizard;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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

import uk.ac.bolton.archimate.canvas.model.ICanvasModel;
import uk.ac.bolton.archimate.canvas.templates.model.CanvasTemplateManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.ui.UIUtils;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.archimate.templates.wizard.TemplateUtils;


/**
 * Save Canvas As Template Wizard Page 1
 * 
 * @author Phillip Beauvoir
 */
public class SaveCanvasAsTemplateWizardPage extends WizardPage {

    public static String HELPID = "uk.ac.bolton.archimate.help.SaveCanvasAsTemplateWizardPage"; //$NON-NLS-1$

    private ICanvasModel fCanvasModel;

    private Text fFileTextField;
    private Text fNameTextField;
    private Text fDescriptionTextField;
    private Label fPreviewLabel;
    private Button fButtonIncludeThumbnail;

    private TemplateManager fTemplateManager;
    
    static File CURRENT_FOLDER = new File(System.getProperty("user.home"));
    
    public SaveCanvasAsTemplateWizardPage(ICanvasModel canvasModel, TemplateManager templateManager) {
        super("SaveCanvasAsTemplateWizardPage");
        setTitle("Save Canvas As Template");
        setDescription("Provide the Canvas' file location, name, description and key thumbnail.");
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        fCanvasModel = canvasModel;
        fTemplateManager = templateManager;
    }

    @Override
    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELPID);
        
        Group fileComposite = new Group(container, SWT.NULL);
        fileComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(3, false);
        fileComposite.setLayout(layout);
        
        label = new Label(fileComposite, SWT.NULL);
        label.setText("File:");
        
        fFileTextField = new Text(fileComposite, SWT.BORDER | SWT.SINGLE);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        File newFile = new File(CURRENT_FOLDER, "New Template" + CanvasTemplateManager.CANVAS_TEMPLATE_FILE_EXTENSION);
        fFileTextField.setText(newFile.getPath());
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fFileTextField);
        fFileTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button fileButton = new Button(fileComposite, SWT.PUSH);
        fileButton.setText("Choose...");
        fileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                File file = chooseFile();
                if(file != null) {
                    fFileTextField.setText(file.getPath());
                    CURRENT_FOLDER = file.getParentFile();
                }
            }
        });
        
        Group fieldGroup = new Group(container, SWT.NULL);
        fieldGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        layout = new GridLayout(2, false);
        fieldGroup.setLayout(layout);
        
        label = new Label(fieldGroup, SWT.NULL);
        label.setText("Name:");

        fNameTextField = new Text(fieldGroup, SWT.BORDER | SWT.SINGLE);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if(StringUtils.isSet(fCanvasModel.getName())) {
            fNameTextField.setText(fCanvasModel.getName());
        }
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fNameTextField);
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        label = new Label(fieldGroup, SWT.NULL);
        label.setText("Description:");
        gd = new GridData(SWT.NULL, SWT.TOP, false, false);
        label.setLayoutData(gd);
        
        fDescriptionTextField = new Text(fieldGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        fDescriptionTextField.setLayoutData(gd);
        if(StringUtils.isSet(fCanvasModel.getDocumentation())) {
            fDescriptionTextField.setText(fCanvasModel.getDocumentation());
        }
        
        // Thumbnail
        
        Group thumbsGroup = new Group(container, SWT.NULL);
        thumbsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout(2, false);
        thumbsGroup.setLayout(layout);
        
        fButtonIncludeThumbnail = new Button(thumbsGroup, SWT.CHECK);
        fButtonIncludeThumbnail.setText("Include thumbnail");
        fButtonIncludeThumbnail.setSelection(true);
        fButtonIncludeThumbnail.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fPreviewLabel.setEnabled(fButtonIncludeThumbnail.getSelection());
            }
        });
        
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fButtonIncludeThumbnail.setLayoutData(gd);
        
        label = new Label(thumbsGroup, SWT.NULL);
        label.setText("Preview:   ");
        gd = new GridData(SWT.NULL, SWT.TOP, false, false);
        label.setLayoutData(gd);

        fPreviewLabel = new Label(thumbsGroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        gd.widthHint = 150;
        fPreviewLabel.setLayoutData(gd);
        
        // Dispose of the image here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this label is asked to relayout.
        fPreviewLabel.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposePreviewImage();
            }
        });
        
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                TemplateUtils.createThumbnailPreviewImage(fCanvasModel, fPreviewLabel);
            }
        });
        
        fPreviewLabel.addControlListener(new ControlAdapter() {
            int oldTime;
            
            @Override
            public void controlResized(ControlEvent e) {
                if(e.time - oldTime > 10) {
                    disposePreviewImage();
                    TemplateUtils.createThumbnailPreviewImage(fCanvasModel, fPreviewLabel);
                }
                oldTime = e.time;
            }
        });
        
        validateFields();
    }
    
    /**
     * @return The File for the template
     */
    public String getFileName() {
        return fFileTextField.getText();
    }

    /**
     * @return The Name for the template
     */
    public String getTemplateName() {
        return fNameTextField.getText();
    }
    
    /**
     * @return The Name for the template
     */
    public String getTemplateDescription() {
        return fDescriptionTextField.getText();
    }
    
    public boolean includeThumbnail() {
        return fButtonIncludeThumbnail.getSelection();
    }
    
    private File chooseFile() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText("Choose a file name");
        dialog.setFilterExtensions(new String[] { "*" + fTemplateManager.getTemplateFileExtension(), "*.*" } );
        String path = dialog.open();
        if(path != null) {
            // Only Windows adds the extension by default
            if(dialog.getFilterIndex() == 0 && !path.endsWith(CanvasTemplateManager.CANVAS_TEMPLATE_FILE_EXTENSION)) {
                path += CanvasTemplateManager.CANVAS_TEMPLATE_FILE_EXTENSION;
            }
            return new File(path);
        }
        return null;
    }
    
    private void validateFields() {
        String fileName = getFileName();
        if(!StringUtils.isSetAfterTrim(fileName)) {
            updateStatus("Provide a file name");
            return;
        }
        
        String name = getTemplateName();
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus("Provide a template name");
            return;
        }
        
        updateStatus(null);
    }

    /**
     * Update the page status
     */
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    private void disposePreviewImage() {
        if(fPreviewLabel != null && fPreviewLabel.getImage() != null && !fPreviewLabel.getImage().isDisposed()) {
            fPreviewLabel.getImage().dispose();
        }
    }
}
