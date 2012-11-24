/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.jasperreports;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * Export Model to Jasper Reports Wizard Page 2
 * 
 * @author Phillip Beauvoir
 */
public class ExportJasperReportsWizardPage2 extends WizardPage {

    static File INBUILT_REPORTS_FOLDER = JasperReportsPlugin.INSTANCE.getJasperReportsFolder();
    static File INBUILT_STANDARD_REPORT = new File(INBUILT_REPORTS_FOLDER, "standard/main.jrxml"); //$NON-NLS-1$

    private static String HELP_ID = "uk.ac.bolton.archimate.help.ExportJasperReportsWizardPage2"; //$NON-NLS-1$
    
    private ComboViewer fComboTemplateViewer;
    
    private Template fLastSelectedTemplate;
    
    private static class Template {
        String name;
        File location;
        
        Template(String name, File location) {
            this.name = name;
            this.location = location;
        }
    }
    
    private static List<Template> fTemplates = new ArrayList<Template>();
    static {
        fTemplates.add(new Template(Messages.ExportJasperReportsWizardPage2_0, INBUILT_STANDARD_REPORT));
        fTemplates.add(new Template(Messages.ExportJasperReportsWizardPage2_1, null));
    }
    
    public ExportJasperReportsWizardPage2() {
        super("ExportJasperReportsWizardPage2"); //$NON-NLS-1$
        setTitle(Messages.ExportJasperReportsWizardPage2_2);
        setDescription(Messages.ExportJasperReportsWizardPage2_3);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fieldContainer.setLayout(new GridLayout(2, false));
        
        Label label = new Label(fieldContainer, SWT.NONE);
        label.setText(Messages.ExportJasperReportsWizardPage2_4);
        
        fComboTemplateViewer = new ComboViewer(new Combo(fieldContainer, SWT.READ_ONLY | SWT.BORDER));
        fComboTemplateViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboTemplateViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Template template = (Template)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(template.location == null) { // Custom...
                    handleCustomDialog();
                }
                else {
                    fLastSelectedTemplate = template;
                }
            }
        });
        
        fComboTemplateViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object inputElement) {
                return fTemplates.toArray();
            }
        });
        
        fComboTemplateViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Template)element).name;
            }
        });
        
        fComboTemplateViewer.setInput(""); //$NON-NLS-1$
        fComboTemplateViewer.setSelection(new StructuredSelection(fTemplates.get(0)));
    }
    
    public File getMainTemplateFile() {
        Template template = (Template)((IStructuredSelection)fComboTemplateViewer.getSelection()).getFirstElement();
        return template.location;
    }
    
    private void handleCustomDialog() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.ExportJasperReportsWizardPage2_5);
        dialog.setFilterExtensions(new String[] { "main.jrxml", "*.jrxml", "*.*" } );  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        dialog.setFileName("main.jrxml"); //$NON-NLS-1$
        String path = dialog.open();
        if(path != null) {
            File file = new File(path);
            
            // Do we have it already?
            for(Template template : fTemplates) {
                if(file.equals(template.location)) {
                    fComboTemplateViewer.setSelection(new StructuredSelection(template));
                    return;
                }
            }
            
            // No, add new one
            Template template = new Template(getShortPath(file), file);
            fTemplates.add(fTemplates.size() - 1, template);
            fComboTemplateViewer.refresh();
            fComboTemplateViewer.setSelection(new StructuredSelection(template));
        }
        else {
            fComboTemplateViewer.setSelection(new StructuredSelection(fLastSelectedTemplate));
        }
    }
    
    private static String getShortPath(File file) {
        String path = file.getAbsolutePath();
        
        try {
            String pathPart = file.getParent();
            final int maxLength = 38;
            if(pathPart.length() > maxLength) {
                pathPart = pathPart.substring(0, maxLength - 3);
                pathPart += "..." + File.separator; //$NON-NLS-1$
                path = pathPart += file.getName();
            }
        }
        catch(Exception ex) { // Catch any exceptions otherwise the app won't load
            ex.printStackTrace();
        }
        
        return path;
    }
}
