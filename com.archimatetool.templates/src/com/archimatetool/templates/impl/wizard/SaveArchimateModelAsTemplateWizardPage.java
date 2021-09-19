/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.wizard;

import java.io.File;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.templates.impl.model.ArchimateTemplateManager;
import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.ModelViewsTreeViewer;
import com.archimatetool.templates.wizard.TemplateUtils;



/**
 * Save Archimate Model As Template Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class SaveArchimateModelAsTemplateWizardPage extends WizardPage {

    private static String HELP_ID = "com.archimatetool.help.SaveArchimateModelAsTemplateWizardPage"; //$NON-NLS-1$

    private IArchimateModel fModel;

    private Text fFileTextField;
    private Text fNameTextField;
    private Text fDescriptionTextField;
    private ModelViewsTreeViewer fModelViewsTreeViewer;
    private Label fPreviewLabel;
    private Button fButtonIncludeThumbs;

    private TemplateManager fTemplateManager;
    
    private static final String PREFS_LAST_FOLDER = "SaveArchimateModelAsTemplateLastFolder"; //$NON-NLS-1$
    
    public SaveArchimateModelAsTemplateWizardPage(IArchimateModel model, TemplateManager templateManager) {
        super("SaveModelAsTemplateWizardPage"); //$NON-NLS-1$
        setTitle(Messages.SaveArchimateModelAsTemplateWizardPage_2);
        setDescription(Messages.SaveArchimateModelAsTemplateWizardPage_3);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        fModel = model;
        fTemplateManager = templateManager;
    }

    @Override
    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        Group fileComposite = new Group(container, SWT.NULL);
        fileComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(3, false);
        fileComposite.setLayout(layout);
        
        label = new Label(fileComposite, SWT.NULL);
        label.setText(Messages.SaveArchimateModelAsTemplateWizardPage_4);
        
        fFileTextField = UIUtils.createSingleTextControl(fileComposite, SWT.BORDER, false);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        String defaultFileName = Messages.SaveArchimateModelAsTemplateWizardPage_5 + ArchimateTemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION;
        
        // Get last folder used
        String lastFolderName = ArchiPlugin.PREFERENCES.getString(PREFS_LAST_FOLDER);
        File lastFolder = new File(lastFolderName);
        if(lastFolder.exists() && lastFolder.isDirectory()) {
            fFileTextField.setText(new File(lastFolder, defaultFileName).getPath());
        }
        else {
            fFileTextField.setText(new File(System.getProperty("user.home"), defaultFileName).getPath()); //$NON-NLS-1$
        }

        fFileTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button fileButton = new Button(fileComposite, SWT.PUSH);
        fileButton.setText(Messages.SaveArchimateModelAsTemplateWizardPage_6);
        fileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                File file = chooseFile();
                if(file != null) {
                    fFileTextField.setText(file.getPath());
                }
            }
        });
        
        Group fieldGroup = new Group(container, SWT.NULL);
        fieldGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        layout = new GridLayout(2, false);
        fieldGroup.setLayout(layout);
        
        label = new Label(fieldGroup, SWT.NULL);
        label.setText(Messages.SaveArchimateModelAsTemplateWizardPage_7);

        fNameTextField = UIUtils.createSingleTextControl(fieldGroup, SWT.BORDER, false);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if(StringUtils.isSet(fModel.getName())) {
            fNameTextField.setText(fModel.getName());
        }

        fNameTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        label = new Label(fieldGroup, SWT.NULL);
        label.setText(Messages.SaveArchimateModelAsTemplateWizardPage_8);
        gd = new GridData(SWT.NULL, SWT.TOP, false, false);
        label.setLayoutData(gd);
        
        fDescriptionTextField = new Text(fieldGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        gd.widthHint = 550; // Stop overstretch
        fDescriptionTextField.setLayoutData(gd);
        if(StringUtils.isSet(fModel.getPurpose())) {
            fDescriptionTextField.setText(fModel.getPurpose());
        }
        
        // Thumbnails
        boolean thumbsEnabled = !fModel.getDiagramModels().isEmpty();
        
        Group thumbsGroup = new Group(container, SWT.NULL);
        thumbsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout();
        thumbsGroup.setLayout(layout);
        
        fButtonIncludeThumbs = new Button(thumbsGroup, SWT.CHECK);
        fButtonIncludeThumbs.setText(Messages.SaveArchimateModelAsTemplateWizardPage_9);
        fButtonIncludeThumbs.setSelection(thumbsEnabled);
        fButtonIncludeThumbs.setEnabled(thumbsEnabled);
        fButtonIncludeThumbs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fModelViewsTreeViewer.getControl().setEnabled(fButtonIncludeThumbs.getSelection());
                fPreviewLabel.setEnabled(fButtonIncludeThumbs.getSelection());
            }
        });
        
        label = new Label(thumbsGroup, SWT.NULL);
        label.setText(Messages.SaveArchimateModelAsTemplateWizardPage_10);
        label.setEnabled(thumbsEnabled);

        Composite thumbContainer = new Composite(thumbsGroup, SWT.NULL);
        thumbContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        thumbContainer.setLayout(layout);
        
        fModelViewsTreeViewer = new ModelViewsTreeViewer(thumbContainer, SWT.NONE);
        fModelViewsTreeViewer.setInput(fModel.getFolder(FolderType.DIAGRAMS));
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        gd.widthHint = 140;
        fModelViewsTreeViewer.getControl().setLayoutData(gd);
        fModelViewsTreeViewer.getControl().setEnabled(thumbsEnabled);
        
        fPreviewLabel = new Label(thumbContainer, SWT.BORDER);
        fPreviewLabel.setBackground(new Color(255, 255, 255));
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        gd.widthHint = 150;
        fPreviewLabel.setLayoutData(gd);
        fPreviewLabel.setAlignment(SWT.CENTER);
        
        // Dispose of the image here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this label is asked to relayout.
        fPreviewLabel.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposePreviewImage();
            }
        });
        
        fModelViewsTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IDiagramModel) {
                    TemplateUtils.createThumbnailPreviewImage((IDiagramModel)o, fPreviewLabel);
                }
                else {
                    disposePreviewImage();
                }
            }
        });
        
        // Select first Template item on tree (on a thread so that thumbnail preview is right size)
        fModelViewsTreeViewer.expandAll();
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                for(TreeItem item : fModelViewsTreeViewer.getTree().getItems()) {
                    Object o = item.getData();
                    if(o instanceof IDiagramModel) {
                        fModelViewsTreeViewer.setSelection(new StructuredSelection(o));
                        break;
                    }
                }
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
    
    public boolean includeThumbnails() {
        return fButtonIncludeThumbs.getSelection();
    }
    
    /**
     * @return The Selected Diagram Model for the key thumbnail
     */
    public IDiagramModel getSelectedDiagramModel() {
        Object o = ((IStructuredSelection)fModelViewsTreeViewer.getSelection()).getFirstElement();
        if(o instanceof IDiagramModel) {
            return (IDiagramModel)o;
        }
        return null;
    }
    
    private File chooseFile() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText(Messages.SaveArchimateModelAsTemplateWizardPage_11);
        dialog.setFilterExtensions(new String[] { "*" + fTemplateManager.getTemplateFileExtension(), "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        File file = new File(fFileTextField.getText());
        dialog.setFileName(file.getName());
        
        // Does nothing on macOS 10.15+. On Windows will work after Eclipse 4.21
        dialog.setOverwrite(false);
        
        String path = dialog.open();
        
        if(path != null) {
            // Only Windows adds the extension by default
            if(dialog.getFilterIndex() == 0 && !path.endsWith(ArchimateTemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION)) {
                path += ArchimateTemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION;
            }
            return new File(path);
        }
        
        return null;
    }
    
    private void validateFields() {
        String fileName = getFileName();
        if(!StringUtils.isSetAfterTrim(fileName)) {
            updateStatus(Messages.SaveArchimateModelAsTemplateWizardPage_14);
            return;
        }
        
        String name = getTemplateName();
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus(Messages.SaveArchimateModelAsTemplateWizardPage_15);
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
        if(fPreviewLabel != null && fPreviewLabel.getImage() != null) {
            fPreviewLabel.getImage().dispose();
            fPreviewLabel.setImage(null);
        }
    }
    
    void storePreferences() {
        // Store current folder
        File parentFile = new File(getFileName()).getAbsoluteFile().getParentFile(); // Make sure to use absolute file
        if(parentFile != null) {
            ArchiPlugin.PREFERENCES.setValue(PREFS_LAST_FOLDER, parentFile.getAbsolutePath());
        }
    }

}
