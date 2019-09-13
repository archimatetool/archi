/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateGroup;
import com.archimatetool.templates.model.TemplateManager;



/**
 * Template Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class TemplateManagerDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.TemplateManagerDialog"; //$NON-NLS-1$

    protected TemplatesTableViewer fTableViewer;
    protected TemplatesTreeViewer fTreeViewer;

    protected Button fButtonAddTemplate;
    protected Button fButtonNewGroup;
    protected Button fButtonRemove;
    
    protected Label fFileLabel;
    protected Label fNameLabel;
    protected Label fDescriptionLabel;
    protected Text fFileTextField;
    protected Text fNameTextField;
    protected Text fDescriptionTextField;
    
    protected Object fSelectedControl;
    protected ITemplate fSelectedTemplate;
    protected ITemplateGroup fSelectedTemplateGroup;
    
    protected List<ITemplate> fModifiedTemplates = new ArrayList<ITemplate>();

    protected TemplateManager fTemplateManager;
    
    protected boolean fIsSettingFields;
    
    public TemplateManagerDialog(Shell parentShell, TemplateManager templateManager) {
        super(parentShell, "TemplateManagerDialog"); //$NON-NLS-1$
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fTemplateManager = templateManager;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.TemplateManagerDialog_1);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, getHelpID());

        setTitle(Messages.TemplateManagerDialog_2);
        setMessage(Messages.TemplateManagerDialog_3);
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        SashForm sash = new SashForm(client, SWT.HORIZONTAL);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 580;
        gd.heightHint = 300;
        sash.setLayoutData(gd);
        
        // Templates Table
        Composite tableComp = new Composite(sash, SWT.BORDER);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        tableComp.setLayout(layout);
        
        CLabel label = new CLabel(tableComp, SWT.NULL);
        label.setText(Messages.TemplateManagerDialog_4);
        
        Composite tableComp2 = new Composite(tableComp, SWT.NULL);
        tableComp2.setLayout(new TableColumnLayout());
        tableComp2.setLayoutData(new GridData(GridData.FILL_BOTH));
        fTableViewer = new TemplatesTableViewer(tableComp2, SWT.MULTI);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        fTableViewer.setInput(fTemplateManager.getUserTemplates());
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                fSelectedControl = fTableViewer;
                updateControls(o);
            }
        });
        
        // Groups Tree
        Composite treeComp = new Composite(sash, SWT.BORDER);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        treeComp.setLayout(layout);
        
        label = new CLabel(treeComp, SWT.NULL);
        label.setText(Messages.TemplateManagerDialog_5);
        //label.setImage(IArchiImages.ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_FOLDER));
        
        fTreeViewer = new TemplatesTreeViewer(treeComp, SWT.MULTI);
        fTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        fTreeViewer.setInput(fTemplateManager.getUserTemplateGroups());
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                fSelectedControl = fTreeViewer;
                updateControls(o);
            }
        });
        
        new TemplateManagerDialogDragDropHandler(fTableViewer, fTreeViewer);
        
        // Buttons
        Composite buttonBar = new Composite(client, SWT.NULL);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        buttonBar.setLayout(layout);
        buttonBar.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        
        fButtonAddTemplate = new Button(buttonBar, SWT.NULL);
        fButtonAddTemplate.setText(Messages.TemplateManagerDialog_6);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonAddTemplate.setLayoutData(gd);
        fButtonAddTemplate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                openTemplate();
            }
        });
        
        fButtonNewGroup = new Button(buttonBar, SWT.NULL);
        fButtonNewGroup.setText(Messages.TemplateManagerDialog_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonNewGroup.setLayoutData(gd);
        fButtonNewGroup.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newGroup();
            }
        });
        
        fButtonRemove = new Button(buttonBar, SWT.NULL);
        fButtonRemove.setText(Messages.TemplateManagerDialog_8);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonRemove.setLayoutData(gd);
        fButtonRemove.setEnabled(false);
        fButtonRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelectedObjects();
            }
        });
        
        sash.setWeights(new int[] { 30, 70 });
        
        Composite fieldContainer = new Composite(composite, SWT.NULL);
        layout = new GridLayout(2, false);
        fieldContainer.setLayout(layout);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fNameLabel = new Label(fieldContainer, SWT.NULL);
        fNameLabel.setText(Messages.TemplateManagerDialog_9);
        fNameLabel.setEnabled(false);
        
        fNameTextField = UIUtils.createSingleTextControl(fieldContainer, SWT.BORDER, false);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fNameTextField.setEnabled(false);

        fNameTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                String text = fNameTextField.getText();
                if(fIsSettingFields || !StringUtils.isSet(text)) {
                    return;
                }
                        
                // Edit Template name
                if(fSelectedTemplate != null) {
                    fSelectedTemplate.setName(text);
                    if(!fModifiedTemplates.contains(fSelectedTemplate)) {
                        fModifiedTemplates.add(fSelectedTemplate);
                    }
                    fTableViewer.refresh();
                    fTreeViewer.refresh();
                }
                // Edit Group name
                else if(fSelectedTemplateGroup != null) {
                    fSelectedTemplateGroup.setName(text);
                    fTreeViewer.refresh();
                }
            }
        });

        fDescriptionLabel = new Label(fieldContainer, SWT.NULL);
        fDescriptionLabel.setText(Messages.TemplateManagerDialog_10);
        fDescriptionLabel.setEnabled(false);
        
        fDescriptionTextField = new Text(fieldContainer, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        fDescriptionTextField.setEnabled(false);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 100;
        fDescriptionTextField.setLayoutData(gd);
        fDescriptionTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                String text = fDescriptionTextField.getText();
                if(fIsSettingFields || !StringUtils.isSet(text)) {
                    return;
                }
                if(fSelectedTemplate != null) {
                    fSelectedTemplate.setDescription(fDescriptionTextField.getText());
                    if(!fModifiedTemplates.contains(fSelectedTemplate)) {
                        fModifiedTemplates.add(fSelectedTemplate);
                    }
                }
            }
        });

        fFileLabel = new Label(fieldContainer, SWT.NULL);
        fFileLabel.setText(Messages.TemplateManagerDialog_11);
        fFileLabel.setEnabled(false);
        
        fFileTextField = new Text(fieldContainer, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fFileTextField.setEnabled(false);

        return composite;
    }
    
    @Override
    protected void okPressed() {
        super.okPressed();

        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                try {
                    // Save main manifest
                    fTemplateManager.saveUserTemplatesManifest();

                    // Save changes
                    for(ITemplate template : fModifiedTemplates) {
                        template.save();
                    }
                    
                    // Dispose
                    fTemplateManager.dispose();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(null, Messages.TemplateManagerDialog_12, ex.getMessage());
                }
            }
        });
    }
    
    @Override
    protected void cancelPressed() {
        fTemplateManager.dispose();
        super.cancelPressed();
    }
    
    /**
     * Add a Template file
     */
    protected void openTemplate() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.TemplateManagerDialog_13);
        dialog.setFilterExtensions(new String[] { "*" + fTemplateManager.getTemplateFileExtension(), "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        final File file = new File(path);
        
        if(hasUserTemplate(file)) {
            MessageDialog.openInformation(getShell(), Messages.TemplateManagerDialog_16, Messages.TemplateManagerDialog_17);
            return;
        }
        
        BusyIndicator.showWhile(null, new Runnable()  {
            @Override
            public void run() {
                try {
                    ITemplate template = fTemplateManager.createTemplate(file);
                    template.setFile(file);
                    fTemplateManager.addUserTemplate(template);
                    fTableViewer.refresh();
                }
                catch(IOException ex) {
                    MessageDialog.openError(getShell(), Messages.TemplateManagerDialog_18, ex.getMessage());
                }
            }
        });
    }
    
    /**
     * Add a new group
     */
    protected void newGroup() {
        IInputValidator validator = new IInputValidator() {
            @Override
            public String isValid(String newText) {
                return "".equals(newText) ? "" : hasGroup(newText) ? Messages.TemplateManagerDialog_21 : null; //$NON-NLS-1$ //$NON-NLS-2$
            }
            
            boolean hasGroup(String name) {
                for(ITemplateGroup group : fTemplateManager.getUserTemplateGroups()) {
                    if(name.equals(group.getName())) {
                        return true;
                    }
                }
                return false;
            }
        };

        InputDialog dialog = new InputDialog(getShell(),
                Messages.TemplateManagerDialog_22,
                Messages.TemplateManagerDialog_23,
                "", //$NON-NLS-1$
                validator);
        
        if(dialog.open() == Window.OK) {
            String name = dialog.getValue();
            if(StringUtils.isSetAfterTrim(name)) {
                ITemplateGroup group = new TemplateGroup(name);
                fTemplateManager.getUserTemplateGroups().add(group);
                fTreeViewer.refresh();
                fTreeViewer.setSelection(new StructuredSelection(group), true);
            }
        }
    }
    
    /**
     * Remove selected object
     */
    protected void deleteSelectedObjects() {
        // Table
        if(fSelectedControl == fTableViewer) {
            for(Object o : ((IStructuredSelection)fTableViewer.getSelection()).toArray()) {
                if(o instanceof ITemplate) {
                    fTemplateManager.getUserTemplates().remove(o);
                    for(ITemplateGroup group : fTemplateManager.getUserTemplateGroups()) {
                        group.removeTemplate((ITemplate)o);
                    }
                }
            }
            fTableViewer.refresh();
            fTreeViewer.refresh();
        }
        // Tree
        else if(fSelectedControl == fTreeViewer) {
            // Do it this way because we can't get template parents
            for(TreeItem item : fTreeViewer.getTree().getSelection()) {
                if(item.getData() instanceof ITemplate) {
                    ITemplate template = (ITemplate)item.getData();
                    TreeItem parent = item.getParentItem();
                    if(parent.getData() instanceof ITemplateGroup) {
                        ((ITemplateGroup)parent.getData()).removeTemplate(template);
                    }
                }
                else if(item.getData() instanceof ITemplateGroup) {
                    fTemplateManager.getUserTemplateGroups().remove(item.getData());
                }
            }
            fTreeViewer.refresh();
        }
    }
    
    protected void updateControls(Object o) {
        fIsSettingFields = true;
        
        // Labels
        fNameLabel.setEnabled(o instanceof ITemplate || o instanceof ITemplateGroup);
        fDescriptionLabel.setEnabled(o instanceof ITemplate);
        fFileLabel.setEnabled(o instanceof ITemplate);
        
        // Fields
        fNameTextField.setText(""); //$NON-NLS-1$
        fDescriptionTextField.setText(""); //$NON-NLS-1$
        fFileTextField.setText(""); //$NON-NLS-1$
        
        fNameTextField.setEnabled(o instanceof ITemplate || o instanceof ITemplateGroup);
        fDescriptionTextField.setEnabled(o instanceof ITemplate);
        
        // Buttons
        fButtonRemove.setEnabled(o instanceof ITemplate || o instanceof ITemplateGroup);
        
        if(o instanceof ITemplate) {
            fSelectedTemplate = (ITemplate)o;
            fSelectedTemplateGroup = null;
            fNameTextField.setText(StringUtils.safeString(fSelectedTemplate.getName()));
            fDescriptionTextField.setText(StringUtils.safeString(fSelectedTemplate.getDescription()));
            fFileTextField.setText(StringUtils.safeString(fSelectedTemplate.getFile().getAbsolutePath()));
        }
        else if(o instanceof ITemplateGroup) {
            fSelectedTemplate = null;
            fSelectedTemplateGroup = (ITemplateGroup)o;
            fNameTextField.setText(StringUtils.safeString(fSelectedTemplateGroup.getName()));
        }
        
        fIsSettingFields = false;
    }
    
    /**
     * @return true if there is already a user template with file
     */
    protected boolean hasUserTemplate(File file) {
        if(file == null) {
            return false;
        }
        
        for(ITemplate template : fTemplateManager.getUserTemplates()) {
            if(file.equals(template.getFile())) {
                return true;
            }
        }
        
        return false;
    }
    
    protected String getHelpID() {
        return HELP_ID;
    }
}
