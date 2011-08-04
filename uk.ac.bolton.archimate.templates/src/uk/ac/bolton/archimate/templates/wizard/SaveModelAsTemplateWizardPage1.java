/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.wizard;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.util.DiagramUtils;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * Save Model As Template Wizard Page 1
 * 
 * @author Phillip Beauvoir
 */
public class SaveModelAsTemplateWizardPage1 extends WizardPage {

    public static String HELPID = "uk.ac.bolton.archimate.help.SaveModelAsTemplateWizardPage1"; //$NON-NLS-1$

    private IArchimateModel fModel;

    private Text fNameTextField;
    private Text fDescriptionTextField;
    private ModelViewsTreeViewer fModelViewsTreeViewer;
    private Image fPreviewImage;
    private Label fPreviewLabel;
    private Button fButtonIncludeThumbs;
    
    public SaveModelAsTemplateWizardPage1(IArchimateModel model) {
        super("SaveModelAsTemplateWizardPage1");
        setTitle("Save Model As Template");
        setDescription("Provide the Template's name, description and key thumbnail.");
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        fModel = model;
    }

    @Override
    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELPID);
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
        
        label = new Label(fieldContainer, SWT.NULL);
        label.setText("Name:");

        fNameTextField = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if(StringUtils.isSet(fModel.getName())) {
            fNameTextField.setText(fModel.getName());
        }
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        label = new Label(container, SWT.NULL);
        label.setText("Description:");
        
        fDescriptionTextField = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        fDescriptionTextField.setLayoutData(gd);
        if(StringUtils.isSet(fModel.getPurpose())) {
            fDescriptionTextField.setText(fModel.getPurpose());
        }
        
        // Thumbnails
        boolean thumbsEnabled = !fModel.getDiagramModels().isEmpty();
        
        fButtonIncludeThumbs = new Button(container, SWT.CHECK);
        fButtonIncludeThumbs.setText("Include thumbnails");
        fButtonIncludeThumbs.setSelection(thumbsEnabled);
        fButtonIncludeThumbs.setEnabled(thumbsEnabled);
        fButtonIncludeThumbs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fModelViewsTreeViewer.getControl().setEnabled(fButtonIncludeThumbs.getSelection());
                fPreviewLabel.setEnabled(fButtonIncludeThumbs.getSelection());
            }
        });
        
        label = new Label(container, SWT.NULL);
        label.setText("Key thumbnail:");
        label.setEnabled(thumbsEnabled);

        Composite thumbContainer = new Composite(container, SWT.NULL);
        thumbContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        thumbContainer.setLayout(layout);
        
        fModelViewsTreeViewer = new ModelViewsTreeViewer(thumbContainer, SWT.NONE);
        fModelViewsTreeViewer.setInput(fModel.getFolder(FolderType.DIAGRAMS));
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        //gd.widthHint = 140;
        fModelViewsTreeViewer.getControl().setLayoutData(gd);
        fModelViewsTreeViewer.getControl().setEnabled(thumbsEnabled);
        
        fPreviewLabel = new Label(thumbContainer, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        gd.widthHint = 150;
        fPreviewLabel.setLayoutData(gd);
        
        // Dispose of the image here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this label is asked to relayout.
        fPreviewLabel.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if(fPreviewImage != null && !fPreviewImage.isDisposed()) {
                    fPreviewImage.dispose();
                }
            }
        });
        
        fModelViewsTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IDiagramModel) {
                    if(fPreviewImage != null && !fPreviewImage.isDisposed()) {
                        fPreviewImage.dispose();
                    }
                    
                    // Generate Preview
                    Shell shell = new Shell();
                    GraphicalViewer diagramViewer = DiagramUtils.createViewer((IDiagramModel)o, shell);
                    Rectangle bounds = DiagramUtils.getDiagramExtents(diagramViewer);
                    double ratio = Math.min(1, Math.min((double)fPreviewLabel.getBounds().width / bounds.width,
                            (double)fPreviewLabel.getBounds().height / bounds.height));
                    fPreviewImage = DiagramUtils.createScaledImage(diagramViewer, ratio);
                    fPreviewLabel.setImage(fPreviewImage);
                    shell.dispose();
                }
                else {
                    fPreviewLabel.setImage(null);
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

    private void validateFields() {
        String name = getTemplateName();
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus("Provide a name");
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
}
