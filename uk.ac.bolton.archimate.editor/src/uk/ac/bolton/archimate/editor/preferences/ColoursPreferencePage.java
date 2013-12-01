/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.preferences;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;

/**
 * Default Colours Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class ColoursPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    public static String HELPID = "uk.ac.bolton.archimate.help.prefsColours"; //$NON-NLS-1$
    
    // Cache of objects' colours
    private Hashtable<Object, Color> fColorsCache = new Hashtable<Object, Color>();
    
    // Image Registry for Tree colors
    private ImageRegistry fImageRegistry;
    
    // Buttons
    private Button fShowUserDefaultFillColorsInApplication;
    private Button fEditFillColorButton;
    private Button fResetFillColorButton;

    // Tree
    private TreeViewer fTreeViewer;
    
    // Convenience model class for Tree
    private static class TreeGrouping {
        public String title;
        public Object[] children;
 
        public TreeGrouping(String title, Object[] children) {
            this.title = title;
            this.children = children;
        }
    }
    
	public ColoursPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);
        
        // Reset everything
        resetColorsCache(false);
        fImageRegistry = new ImageRegistry();
        
        // Layout client
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout(2, false));
        
        Label label = new Label(client, SWT.NULL);
        label.setText(Messages.ColoursPreferencePage_0);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);
        
        // Tree
        fTreeViewer = new TreeViewer(client);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 80; // need this to set a smaller height
        fTreeViewer.getTree().setLayoutData(gd);
        
        // Tree Double-click listener
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                Object[] selected = ((IStructuredSelection)fTreeViewer.getSelection()).toArray();
                if(isValidTreeSelection(selected)) {
                    RGB newRGB = openColorDialog(selected[0]);
                    if(newRGB != null) {
                        for(Object object : selected) {
                            setColor(object, newRGB);
                        }
                    }
                }
            }
        });
        
        // Tree Selection Changed Listener
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() { 
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object[] selected = ((IStructuredSelection)event.getSelection()).toArray();
                fEditFillColorButton.setEnabled(isValidTreeSelection(selected));
                fResetFillColorButton.setEnabled(isValidTreeSelection(selected));
            }
        });
        
        // Tree Content Provider
        fTreeViewer.setContentProvider(new ITreeContentProvider() {

            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                if(inputElement instanceof String) {
                    return new Object[] {
                            new TreeGrouping(Messages.ColoursPreferencePage_7, ArchimateModelUtils.getBusinessClasses()),
                            new TreeGrouping(Messages.ColoursPreferencePage_8, ArchimateModelUtils.getApplicationClasses()),
                            new TreeGrouping(Messages.ColoursPreferencePage_9, ArchimateModelUtils.getTechnologyClasses()),
                            new TreeGrouping(Messages.ColoursPreferencePage_10, ArchimateModelUtils.getMotivationClasses()),
                            new TreeGrouping(Messages.ColoursPreferencePage_11, ArchimateModelUtils.getImplementationMigrationClasses()),
                            new TreeGrouping(Messages.ColoursPreferencePage_1,
                                    new Object[] { IArchimatePackage.eINSTANCE.getDiagramModelNote(),
                                                   IArchimatePackage.eINSTANCE.getDiagramModelGroup() } ),
                            DEFAULT_ELEMENT_LINE_COLOR
                    };
                }
                
                return null;
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                if(parentElement instanceof TreeGrouping) {
                    return ((TreeGrouping)parentElement).children;
                }
                
                return null;
            }

            @Override
            public Object getParent(Object element) {
                return null;
            }

            @Override
            public boolean hasChildren(Object element) {
                return element instanceof TreeGrouping;
            }
            
        });
        
        // Tree Label Provider
        fTreeViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                if(element instanceof EClass) {
                    return ArchimateLabelProvider.INSTANCE.getDefaultName((EClass)element);
                }
                if(element instanceof TreeGrouping) {
                    return ((TreeGrouping)element).title;
                }
                if(element instanceof String) {
                    String s = (String)element;
                    if(s.equals(DEFAULT_ELEMENT_LINE_COLOR)) {
                        return Messages.ColoursPreferencePage_12;
                    }
                }
                
                return null;
            }
            
            @Override
            public Image getImage(Object element) {
                if(element instanceof TreeGrouping) {
                    return IArchimateImages.ImageFactory.getImage(IArchimateImages.ECLIPSE_IMAGE_FOLDER);
                }

                return getColorSwatch(element);
            }
            
            // Create a coloured image based on colour and add to the image registry
            private Image getColorSwatch(Object object) {
                String key = getColorKey(object);
                Image image = fImageRegistry.get(key);
                if(image == null) {
                    image = new Image(Display.getCurrent(), 16, 16);
                    GC gc = new GC(image);
                    SWTGraphics graphics = new SWTGraphics(gc);
                    graphics.setBackgroundColor(fColorsCache.get(object));
                    graphics.fillRectangle(0, 0, 15, 15);
                    graphics.drawRectangle(0, 0, 15, 15);
                    gc.dispose();
                    graphics.dispose();
                    fImageRegistry.put(key, image);
                }
                
                return image;
            }
            
        });
        
        //fTreeViewer.setAutoExpandLevel(2);

        // Set Content in Tree
        fTreeViewer.setInput(""); //$NON-NLS-1$
        
        // Buttons
        Composite buttonClient = new Composite(client, SWT.NULL);
        gd = new GridData(SWT.TOP, SWT.TOP, false, false);
        buttonClient.setLayoutData(gd);
        buttonClient.setLayout(new GridLayout());
        
        // Edit...
        fEditFillColorButton = new Button(buttonClient, SWT.PUSH);
        fEditFillColorButton.setText(Messages.ColoursPreferencePage_13);
        fEditFillColorButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fEditFillColorButton.setEnabled(false);
        fEditFillColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                Object[] selected = ((IStructuredSelection)fTreeViewer.getSelection()).toArray();
                if(isValidTreeSelection(selected)) {
                    RGB newRGB = openColorDialog(selected[0]);
                    if(newRGB != null) {
                        for(Object object : selected) {
                            setColor(object, newRGB);
                        }
                    }
                }
            }
        });

        // Reset
        fResetFillColorButton = new Button(buttonClient, SWT.PUSH);
        fResetFillColorButton.setText(Messages.ColoursPreferencePage_14);
        fResetFillColorButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fResetFillColorButton.setEnabled(false);
        fResetFillColorButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                Object[] selected = ((IStructuredSelection)fTreeViewer.getSelection()).toArray();
                if(isValidTreeSelection(selected)) {
                    for(Object object : selected) {
                        resetColorToInbuiltDefault(object);
                    }
                }
            }
        });
        
        // Import Scheme
        Button importButton = new Button(buttonClient, SWT.PUSH);
        importButton.setText(Messages.ColoursPreferencePage_2);
        importButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        importButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    importUserColors();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Export Scheme
        Button exportButton = new Button(buttonClient, SWT.PUSH);
        exportButton.setText(Messages.ColoursPreferencePage_3);
        exportButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        exportButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    exportUserColors();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        fShowUserDefaultFillColorsInApplication = new Button(client, SWT.CHECK);
        fShowUserDefaultFillColorsInApplication.setText(Messages.ColoursPreferencePage_6);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fShowUserDefaultFillColorsInApplication.setLayoutData(gd);
        fShowUserDefaultFillColorsInApplication.setSelection(getPreferenceStore().getBoolean(SHOW_FILL_COLORS_IN_GUI));
        
        return client;
    }
    
    /**
     * @param object
     * @return The string key associated with a object
     */
    private String getColorKey(Object object) {
        if(object instanceof String) {
            return (String)object;
        }
        if(object instanceof EClass) {
            return ((EClass)object).getName();
        }
        return "x"; //$NON-NLS-1$
    }
    
    /**
     * @param object Selected object
     * @return the RGB value or null
     * Open the color dialog to edit color for an object
     */
    private RGB openColorDialog(Object object) {
        ColorDialog colorDialog = new ColorDialog(getShell());
        colorDialog.setRGB(fColorsCache.get(object).getRGB());
        return colorDialog.open();
    }
    
    /**
     * @param selected
     * @return true if selected tree objects are valid
     */
    private boolean isValidTreeSelection(Object[] selected) {
        if(selected == null || selected.length == 0) {
            return false;
        }
        
        for(Object o : selected) {
            if(o instanceof TreeGrouping) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Reset the cached colour to the inbuilt default
     * @param eClass
     */
    private void resetColorToInbuiltDefault(Object object) {
        RGB defaultRGB = null;

        // Element line color - use any object eClass as there is only one
        if(object.equals(DEFAULT_ELEMENT_LINE_COLOR)) {
            defaultRGB = ColorFactory.getInbuiltDefaultLineColor(null).getRGB();
        }
        // Fill color
        else if(object instanceof EClass) {
            EClass eClass = (EClass)object;
            defaultRGB = ColorFactory.getInbuiltDefaultFillColor(eClass).getRGB();
        }
        
        setColor(object, defaultRGB);
    }
    
    /**
     * @param object
     * @param rgb
     * Set a cached color for an object
     */
    private void setColor(Object object, RGB rgb) {
        // Dispose of old one
        Color oldColor = fColorsCache.get(object);
        if(oldColor != null) {
            oldColor.dispose();
        }
        
        fColorsCache.put(object, new Color(Display.getCurrent(), rgb));
        fImageRegistry.remove(getColorKey(object)); // remove from image registry so we can generate a new image
        fTreeViewer.update(object, null);
    }
    
    /**
     * @param useInbuiltDefaults if true reset to inbuilt defaults
     * Reset the color cache to user or inbuilt defaults
     */
    private void resetColorsCache(boolean useInbuiltDefaults) {
        for(Entry<Object, Color> entry : fColorsCache.entrySet()) {
            entry.getValue().dispose();
        }

        fColorsCache.clear();
        
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            Color color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
            fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        }
        
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            Color color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
            fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        }
       
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            Color color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
            fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        }

        for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
            Color color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
            fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        }
        
        for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
            Color color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
            fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        }
        
        // Note Fill Color
        EClass eClass = IArchimatePackage.eINSTANCE.getDiagramModelNote();
        Color color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
        fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        
        // Group Fill Color
        eClass = IArchimatePackage.eINSTANCE.getDiagramModelGroup();
        color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultFillColor(eClass) : ColorFactory.getDefaultFillColor(eClass);
        fColorsCache.put(eClass, new Color(color.getDevice(), color.getRGB()));
        
        // Element line color - use any object eClass as there is only one line color pref
        color = useInbuiltDefaults ? ColorFactory.getInbuiltDefaultLineColor(null) : ColorFactory.getDefaultLineColor(null);
        fColorsCache.put(DEFAULT_ELEMENT_LINE_COLOR, new Color(color.getDevice(), color.getRGB()));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(SHOW_FILL_COLORS_IN_GUI, fShowUserDefaultFillColorsInApplication.getSelection());
        saveColors(getPreferenceStore());        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        super.performDefaults();
        
        fShowUserDefaultFillColorsInApplication.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_FILL_COLORS_IN_GUI));

        // Set color cache to inbuilt defaults
        resetColorsCache(true);
        
        // Clear tree image registry
        fImageRegistry.dispose();
        fImageRegistry = new ImageRegistry();
        
        // Update tree
        for(Entry<Object, Color> entry : fColorsCache.entrySet()) {
            fTreeViewer.update(entry.getKey(), null);
        }
    }
    
    /**
     * @throws IOException
     * Import a User color scheme
     */
    private void importUserColors() throws IOException {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.ColoursPreferencePage_4);
        dialog.setFileName("ArchiColours.prefs"); //$NON-NLS-1$
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        PreferenceStore store = new PreferenceStore(path);
        store.load();

        // Fill Colors
        for(Entry<Object, Color> entry : fColorsCache.entrySet()) {
            String key = DEFAULT_FILL_COLOR_PREFIX + getColorKey(entry.getKey());
            String value = store.getString(key);
            
            if(StringUtils.isSet(value)) {
                setColor(entry.getKey(), ColorFactory.convertStringToRGB(value));
            }
        }
        
        // Line Color
        String key = DEFAULT_ELEMENT_LINE_COLOR;
        String value = store.getString(key);
        if(StringUtils.isSet(value)) {
            setColor(DEFAULT_ELEMENT_LINE_COLOR, ColorFactory.convertStringToRGB(value));
        }
    }
    
    /**
     * @throws IOException
     * Export a user color scheme
     */
    private void exportUserColors() throws IOException {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText(Messages.ColoursPreferencePage_5);
        dialog.setFileName("ArchiColours.prefs"); //$NON-NLS-1$
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        // Make sure the file does not already exist
        File file = new File(path);
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(getShell(),
                    Messages.ColoursPreferencePage_15,
                    NLS.bind(Messages.ColoursPreferencePage_16, file));
            if(!result) {
                return;
            }
        }

        PreferenceStore store = new PreferenceStore(path);
        saveColors(store);
        store.save();
    }
    
    /**
     * @param store
     * Save colors to preference store
     */
    private void saveColors(IPreferenceStore store) {
        for(Entry<Object, Color> entry : fColorsCache.entrySet()) {
            Color colorNew = entry.getValue();
            Color colorDefault;
            String key;
            
            // Element line color
            if(entry.getKey().equals(DEFAULT_ELEMENT_LINE_COLOR)) {
                // Outline color - use any object eClass as there is only one
                colorDefault = ColorFactory.getInbuiltDefaultLineColor(null);
                key = DEFAULT_ELEMENT_LINE_COLOR;
            }
            // Fill color
            else {
                colorDefault = ColorFactory.getInbuiltDefaultFillColor(entry.getKey());
                key = DEFAULT_FILL_COLOR_PREFIX + getColorKey(entry.getKey());               
            }
                     
            // Default color
            if(colorNew.equals(colorDefault)) {
                store.setToDefault(key);
            }
            // User color
            else {
                store.setValue(key, ColorFactory.convertColorToString(colorNew));
            }
        }
    }
    
    public void init(IWorkbench workbench) {
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        for(Entry<Object, Color> entry : fColorsCache.entrySet()) {
            entry.getValue().dispose();
        }

        fColorsCache.clear();
        fColorsCache = null;
        
        fImageRegistry.dispose();
        fImageRegistry = null;
    }
}