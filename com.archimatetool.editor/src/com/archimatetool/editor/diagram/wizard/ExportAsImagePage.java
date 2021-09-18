package com.archimatetool.editor.diagram.wizard;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IImageExportProvider.IExportDialogAdapter;
import com.archimatetool.editor.diagram.ImageExportProviderManager;
import com.archimatetool.editor.diagram.ImageExportProviderManager.ImageExportProviderInfo;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Export Image Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImagePage extends WizardPage {

    private static String HELP_ID = "com.archimatetool.help.ExportAsImagePage"; //$NON-NLS-1$
    
    private Text fFileTextField;
    
    private ComboViewer fComboFormatViewer;
    
    private Group fSettingsGroup;

    // Map providers to settings composites
    private Map<ImageExportProviderInfo, Composite> fSettingComposites = new HashMap<ImageExportProviderInfo, Composite>();;
    
    private List<ImageExportProviderInfo> fImageProviders = ImageExportProviderManager.getImageExportProviders();

    private ImageExportProviderInfo fSelectedProvider;
    
    /**
     * Shell to act as temporary parent when hiding the settings composite
     */
    private Shell fTempShell;
    
    /**
     * The figure to export
     */
    private IFigure fFigure;
    
    /**
     * The name
     */
    private String fName;
    
    /**
     * Provide an Interface to expose some of the functionality of this Dialog Page
     */
    private IExportDialogAdapter fExportDialogPageAdapter = new IExportDialogAdapter() {
        @Override
        public void setErrorMessage(String message) {
            ExportAsImagePage.this.setErrorMessage(message);
        }
    };
    
    private static final String PREFS_LAST_PROVIDER = "ExportImageLastProvider"; //$NON-NLS-1$
    private static final String PREFS_LAST_FOLDER = "ExportImageLastFolder"; //$NON-NLS-1$
    
    public ExportAsImagePage(IFigure figure, String name) {
        super("ExportAsImagePage"); //$NON-NLS-1$
        
        fFigure = figure;
        fName = name;
        
        // Safe name so never null or blank
        if(!StringUtils.isSet(fName)) {
            fName = "Image"; //$NON-NLS-1$
        }
        
        setTitle(Messages.ExportAsImagePage_0);
        setDescription(Messages.ExportAsImagePage_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);
        
        Group exportGroup = new Group(container, SWT.NULL);
        exportGroup.setText(Messages.ExportAsImagePage_2);
        exportGroup.setLayout(new GridLayout(3, false));
        exportGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportAsImagePage_3);
        
        fFileTextField = UIUtils.createSingleTextControl(exportGroup, SWT.BORDER, false);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Get last folder
        String lastFolder = ArchiPlugin.PREFERENCES.getString(PREFS_LAST_FOLDER);
        if(StringUtils.isSet(lastFolder)) {
            File file = new File(lastFolder);
            fFileTextField.setText(new File(file, fName).getAbsolutePath());
        }
        else {
            fFileTextField.setText(new File(System.getProperty("user.home"), fName).getAbsolutePath()); //$NON-NLS-1$
        }
        
        fFileTextField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button fileButton = new Button(exportGroup, SWT.PUSH);
        fileButton.setText(Messages.ExportAsImagePage_4);
        fileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                File file = chooseFile();
                if(file != null) {
                    fFileTextField.setText(file.getPath());
                }
            }
        });
        
        label = new Label(exportGroup, SWT.NULL);
        label.setText(Messages.ExportAsImagePage_5);
        
        fComboFormatViewer = createFormatComboViewer(exportGroup);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fComboFormatViewer.getControl().setLayoutData(gd);
        
        fSettingsGroup = new Group(container, SWT.NULL);
        fSettingsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        fSettingsGroup.setLayout(new GridLayout());
        fSettingsGroup.setText(Messages.ExportAsImagePage_6);
        
        // Validate our fields before the provider does
        validateFields();

        // Now set the combo and set to last user selected
        if(!fImageProviders.isEmpty()) {
            String selectedProviderID = ArchiPlugin.PREFERENCES.getString(PREFS_LAST_PROVIDER);
            ImageExportProviderInfo provider = getImageProviderInfoFromID(selectedProviderID);
            if(provider == null) {
                provider = fImageProviders.get(0);
            }
            
            fComboFormatViewer.setSelection(new StructuredSelection(provider));
        }
    }
    
    /**
     * Create tghe Format Combo Viewer
     */
    private ComboViewer createFormatComboViewer(Composite parent) {
        ComboViewer viewer = new ComboViewer(new Combo(parent, SWT.READ_ONLY | SWT.BORDER));
        
        viewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object inputElement) {
                return fImageProviders.toArray();
            }
        });
        
        viewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((ImageExportProviderInfo)element).getLabel();
            }
        });
        
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                ImageExportProviderInfo provider = (ImageExportProviderInfo)((IStructuredSelection)event.getSelection()).getFirstElement();
                providerChanged(provider);
            }
        });
        
        viewer.setInput(""); //$NON-NLS-1$

        return viewer;
    }
    
    private void providerChanged(ImageExportProviderInfo provider) {
        // Selected a new Export provider
        if(fSelectedProvider != provider) {
            fSelectedProvider = provider;
            
            // Be nice and add a default extension to the file name
            String filename = fFileTextField.getText();
            if(filename.length() > 0) {
                // Remove any known extensions so we can add a new one
                // But we don't want to remove any dots or extension strings that might be in the file name itself
                for(ImageExportProviderInfo info : fImageProviders) {
                    for(String ext : info.getExtensions()) {
                        if(filename.toLowerCase().endsWith("." + ext) ) { // ensure this is at the end //$NON-NLS-1$
                            int index = filename.toLowerCase().lastIndexOf("." + ext); //$NON-NLS-1$
                            if(index != -1) {
                                filename = filename.substring(0, index);
                                break;
                            }
                        }
                    }
                }
                
                fFileTextField.setText(filename + "." + fSelectedProvider.getExtensions().get(0)); //$NON-NLS-1$
            }
            
            // And update with the provider's settings composite
            
            // Remove previous settings composite (if any)
            for(Control child : fSettingsGroup.getChildren()) {
                child.setVisible(false);
                
                // Attach it to a temp parent shell
                if(fTempShell == null) {
                    fTempShell = new Shell();
                }
                child.setParent(fTempShell);
            }
            
            // Show selected
            Composite settingsComposite = null;
            
            if(fSettingComposites.containsKey(fSelectedProvider)) {
                settingsComposite = fSettingComposites.get(fSelectedProvider);
            }
            else {
                settingsComposite = new Composite(fSettingsGroup, SWT.NONE);
                fSelectedProvider.getProvider().init(fExportDialogPageAdapter, settingsComposite, fFigure);
                fSettingComposites.put(fSelectedProvider, settingsComposite);
            }
            
            fSettingsGroup.setVisible(settingsComposite.getChildren().length > 0);
            settingsComposite.setParent(fSettingsGroup); // Re-attach it here
            settingsComposite.setVisible(true);
            fSettingsGroup.layout(true, true);
        }
    }

    /**
     * @return The File to export
     */
    String getFileName() {
        return fFileTextField.getText();
    }
    
    /**
     * @return The selected provider info
     */
    ImageExportProviderInfo getSelectedProvider() {
        return fSelectedProvider;
    }
    
    private File chooseFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText(Messages.ExportAsImagePage_7);
        
        File file = new File(fFileTextField.getText());
        dialog.setFileName(file.getName());
        
        // Set filter extensions according to provider
        String extensions = "*."; //$NON-NLS-1$
        Iterator<String> iter = fSelectedProvider.getExtensions().iterator();
        extensions += iter.next();
        while(iter.hasNext()) {
            extensions += ";*." + iter.next(); //$NON-NLS-1$
        }
        
        dialog.setFilterExtensions(new String[] { extensions, "*.*" } ); //$NON-NLS-1$
        
        // Does nothing on macOS 10.15+. On Windows will work after Eclipse 4.21
        dialog.setOverwrite(false);
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        return new File(path);
    }

    private void validateFields() {
        String fileName = getFileName();
        if(!StringUtils.isSetAfterTrim(fileName)) {
            setErrorMessage(Messages.ExportAsImagePage_8);
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
        // Store current folder
        File parentFile = new File(getFileName()).getAbsoluteFile().getParentFile(); // Make sure to use absolute file
        if(parentFile != null) {
            ArchiPlugin.PREFERENCES.setValue(PREFS_LAST_FOLDER, parentFile.getAbsolutePath());
        }
        
        if(fSelectedProvider != null) {
            ArchiPlugin.PREFERENCES.setValue(PREFS_LAST_PROVIDER, fSelectedProvider.getID());
        }
    }
    
    private ImageExportProviderInfo getImageProviderInfoFromID(String id) {
        if(id == null) {
            return null;
        }
        
        for(ImageExportProviderInfo provider : fImageProviders) {
            if(id.equals(provider.getID())) {
                return provider;
            }
        }
        
        return null;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fTempShell != null) {
            fTempShell.dispose();
        }
    }
}
