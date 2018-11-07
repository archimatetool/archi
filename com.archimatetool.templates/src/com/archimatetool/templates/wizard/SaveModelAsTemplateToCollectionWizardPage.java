/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.wizard;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateGroup;
import com.archimatetool.templates.model.TemplateManager;



/**
 * Save Model As Template Wizard Page for adding to Templates Collection
 * 
 * @author Phillip Beauvoir
 */
public abstract class SaveModelAsTemplateToCollectionWizardPage extends WizardPage {

    protected Button fDoStoreInCollectionButton;
    protected TemplateGroupsTableViewer fCategoriesTableViewer;
    protected Button fNewGroupButton;
    
    protected TemplateManager fTemplateManager;
    protected ITemplateGroup fSelectedTemplateGroup;
    
    public SaveModelAsTemplateToCollectionWizardPage(String pageName, TemplateManager templateManager) {
        super(pageName);
        fTemplateManager = templateManager;
        init();
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
    }
    
    protected abstract void init();

    @Override
    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, getHelpID());
        
        fDoStoreInCollectionButton = new Button(container, SWT.CHECK);
        fDoStoreInCollectionButton.setText(Messages.SaveModelAsTemplateToCollectionWizardPage_0);
        fDoStoreInCollectionButton.setSelection(true);
        
        fDoStoreInCollectionButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean enabled = fDoStoreInCollectionButton.getSelection();
                fCategoriesTableViewer.getControl().setEnabled(enabled);
                fNewGroupButton.setEnabled(enabled);
                // Select first group, or none
                if(enabled) {
                    Object o = fCategoriesTableViewer.getElementAt(0);
                    if(o != null) {
                        fCategoriesTableViewer.setSelection(new StructuredSelection(o));
                    }
                }
                else {
                    fCategoriesTableViewer.setSelection(new StructuredSelection());
                }
            }
        });
        
        label = new Label(container, SWT.NULL);
        label.setText(Messages.SaveModelAsTemplateToCollectionWizardPage_1);
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        fieldContainer.setLayout(layout);
        
        Composite tableComp = new Composite(fieldContainer, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        tableComp.setLayoutData(gd);
        fCategoriesTableViewer = new TemplateGroupsTableViewer(tableComp, SWT.BORDER | SWT.MULTI);
        
        fCategoriesTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                fSelectedTemplateGroup = (ITemplateGroup)((IStructuredSelection)event.getSelection()).getFirstElement();
            }
        });
        
        fCategoriesTableViewer.setInput(fTemplateManager.getUserTemplateGroups());
        
        fNewGroupButton = new Button(fieldContainer, SWT.NULL);
        fNewGroupButton.setText(Messages.SaveModelAsTemplateToCollectionWizardPage_2);
        gd = new GridData(SWT.TOP, SWT.TOP, false, false);
        fNewGroupButton.setLayoutData(gd);
        
        fNewGroupButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IInputValidator validator = new IInputValidator() {
                    @Override
                    public String isValid(String newText) {
                        return "".equals(newText) ? "" : hasGroup(newText) ? Messages.SaveModelAsTemplateToCollectionWizardPage_3 : null; //$NON-NLS-1$ //$NON-NLS-2$
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
                        Messages.SaveModelAsTemplateToCollectionWizardPage_4,
                        Messages.SaveModelAsTemplateToCollectionWizardPage_5,
                        "", //$NON-NLS-1$
                        validator);
                
                if(dialog.open() == Window.OK) {
                    String name = dialog.getValue();
                    if(StringUtils.isSetAfterTrim(name)) {
                        ITemplateGroup group = new TemplateGroup(name);
                        fTemplateManager.getUserTemplateGroups().add(group);
                        fCategoriesTableViewer.refresh();
                        fCategoriesTableViewer.setSelection(new StructuredSelection(group), true);
                    }
                }
            }
        });
        
        setPageComplete(true);
    }
    
    public boolean doStoreInCollection() {
        return fDoStoreInCollectionButton.getSelection();
    }
    
    /**
     * @return The Group for the template or null if no group selected
     */
    public ITemplateGroup getTemplateGroup() {
        return fSelectedTemplateGroup;
    }
    
    protected abstract String getHelpID();
}
