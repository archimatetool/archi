/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.ui.components.CustomColorDialog;
import com.archimatetool.editor.ui.factory.model.FolderUIProvider;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Colours Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class ColoursPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    public static final String ID = "com.archimatetool.editor.prefsColours"; //$NON-NLS-1$
    public static final String HELPID = "com.archimatetool.help.prefsAppearance"; //$NON-NLS-1$
    
    // Color information for objects
    private Map<Object, ColorInfo> fColorInfoMap = new HashMap<>();
    
    // Image Registry for Tree colors
    private ImageRegistry fImageRegistry = new ImageRegistry();
    
    // Buttons
    private Button fPersistUserDefaultColors;
    private Button fEditFillColorButton;
    private Button fResetFillColorButton;
    private Button fDeriveElementLineColorsButton;
    
    // Tree
    private TreeViewer fTreeViewer;

    private IPropertyChangeListener themeChangeListener;
    
    private static List<String> themeColors = List.of(VIEW_BACKGROUND_COLOR,
                                                      VISUALISER_BACKGROUND_COLOR);
    
    // Convenience record for Tree
    private record TreeGrouping(String title, Object[] children) {};
    
    /**
     * Color Info class for an object
     */
    private static class ColorInfo {
        private Object object;
        private String label;
        private boolean isFill;
        
        private Color currentColor;
        private Color userColor;
        private Color inbuiltColor;
        
        ColorInfo(Object object, String label, boolean isFill) {
            this.object = object;
            this.label = label;
            this.isFill = isFill;
            currentColor = getUserColor();
        }
        
        Object getObject() {
            return object;
        }
        
        String getLabel() {
            return isUserSet() ? label + " *" : label; //$NON-NLS-1$
        }
        
        Color getCurrentColor() {
            return currentColor;
        }
        
        boolean setColor(RGB rgb) {
            Color newColor = rgb == null ? getInBuiltColor() : new Color(rgb);
            if(Objects.equals(newColor, currentColor)) {
                return false;
            }
            currentColor = newColor;
            return true;
        }
        
        Color getUserColor() {
            if(userColor == null) {
                userColor = isFill ? ColorFactory.getDefaultFillColor(object) : ColorFactory.getDefaultLineColor(object);
            }
            return userColor;
        }
        
        Color getInBuiltColor() {
            if(inbuiltColor == null) {
                inbuiltColor = isFill ? ColorFactory.getInbuiltDefaultFillColor(object) : ColorFactory.getInbuiltDefaultLineColor(object);
            }
            return inbuiltColor;
        }
        
        /**
         * @return true if the current color doesn't equal the inbuilt color
         */
        boolean isUserSet() {
            return !Objects.equals(getCurrentColor(), getInBuiltColor());
        }
        
        String getPreferenceKey() {
            if(object instanceof String s && isFill) {
                return DEFAULT_FILL_COLOR_PREFIX + s;
            }
            return object != null ? object.toString() : null;
        }
    }
    
    /**
     * Color Info class for an EClass
     */
    private static class EClassColorInfo extends ColorInfo {
        EClassColorInfo(EClass eClass) {
            super(eClass, ArchiLabelProvider.INSTANCE.getDefaultName(eClass), true);
        }
        
        @Override
        EClass getObject() {
            return (EClass)super.getObject();
        }
        
        @Override
        String getPreferenceKey() {
            return DEFAULT_FILL_COLOR_PREFIX + getObject().getName();
        }
    }

    /**
     * Color Info class for a Folder
     */
    private static class FolderColorInfo extends ColorInfo {
        FolderColorInfo(FolderType folderType) {
            super(folderType, folderType.getLabel(), true);
        }
        
        @Override
        FolderType getObject() {
            return (FolderType)super.getObject();
        }
        
        @Override
        Color getUserColor() {
            return FolderUIProvider.getFolderColor(getObject());
        }
        
        @Override
        Color getInBuiltColor() {
            return FolderUIProvider.getDefaultFolderColor(getObject());
        }
        
        @Override
        String getPreferenceKey() {
            return FOLDER_COLOUR_PREFIX + getObject().getName();
        }
    }
    
    /**
     * Color Info class for a Theme
     */
    private static class ThemeColorInfo extends ColorInfo {
        ThemeColorInfo(String themeName) {
            super(themeName, ThemeUtils.getColorDefinitionName(themeName), true);
        }
        
        @Override
        String getObject() {
            return (String)super.getObject();
        }

        @Override
        Color getUserColor() {
            return new Color(ThemeUtils.getCurrentThemeColor(getObject()));
        }
        
        @Override
        Color getInBuiltColor() {
            return new Color(ThemeUtils.getDefaultThemeColor(getObject()));
        }
        
        @Override
        String getPreferenceKey() {
            return getObject();
        }
    }
    
	public ColoursPreferencePage() {
		setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);
        
        Composite client = new Composite(parent, SWT.NULL);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(client);
        
        Label label = new Label(client, SWT.NULL);
        label.setText(Messages.ColoursPreferencePage_0);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(label);
        
        // Tree
        fTreeViewer = new TreeViewer(client);
        GridDataFactory.create(GridData.FILL_BOTH).hint(SWT.DEFAULT, 200).applyTo(fTreeViewer.getTree());
        
        // Tree Double-click listener
        fTreeViewer.addDoubleClickListener(event -> {
            editSelected();
        });
        
        // Tree Selection Changed Listener
        fTreeViewer.addSelectionChangedListener(event -> { 
            fEditFillColorButton.setEnabled(isEditEnabled());
            fResetFillColorButton.setEnabled(isResetEnabled());
        });
        
        // Tree Right-click
        hookContextMenu();
        
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
                // Other and Junction
                List<EClass> other = new ArrayList<>();
                other.addAll(Arrays.asList(ArchimateModelUtils.getOtherClasses()));
                other.addAll(Arrays.asList(ArchimateModelUtils.getConnectorClasses()));
                
                return new Object[] {
                    new TreeGrouping(Messages.ColoursPreferencePage_1, ArchimateModelUtils.getStrategyClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_2, ArchimateModelUtils.getBusinessClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_3, ArchimateModelUtils.getApplicationClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_4, ArchimateModelUtils.getTechnologyClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_5, ArchimateModelUtils.getPhysicalClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_6, ArchimateModelUtils.getMotivationClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_7, ArchimateModelUtils.getImplementationMigrationClasses()),
                    new TreeGrouping(Messages.ColoursPreferencePage_8, other.toArray() ),

                    new TreeGrouping(Messages.ColoursPreferencePage_9,
                            new Object[] { IArchimatePackage.eINSTANCE.getDiagramModelNote(),
                                           IArchimatePackage.eINSTANCE.getDiagramModelGroup(),
                                           IDiagramModelNote.LEGEND_MODEL_NAME} ),

                    new TreeGrouping(Messages.ColoursPreferencePage_10, new FolderType[] {
                            FolderType.STRATEGY,
                            FolderType.BUSINESS,
                            FolderType.APPLICATION,
                            FolderType.TECHNOLOGY,
                            FolderType.MOTIVATION,
                            FolderType.IMPLEMENTATION_MIGRATION,
                            FolderType.OTHER,
                            FolderType.RELATIONS,
                            FolderType.DIAGRAMS }),

                    new TreeGrouping(Messages.ColoursPreferencePage_25, themeColors.toArray()),

                    DEFAULT_ELEMENT_LINE_COLOR,
                    DEFAULT_CONNECTION_LINE_COLOR
                };
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                if(parentElement instanceof TreeGrouping grouping) {
                    return grouping.children();
                }
                
                return new Object[0];
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
                // Grouping
                if(element instanceof TreeGrouping grouping) {
                    return grouping.title();
                }
                
                // Color Info
                ColorInfo colorInfo = fColorInfoMap.get(element);
                return colorInfo != null ? colorInfo.getLabel() : null;
            }
            
            @Override
            public Image getImage(Object element) {
                if(element == null) {
                    return null;
                }
                
                // TreeGrouping
                if(element instanceof TreeGrouping) {
                    return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_FOLDER_DEFAULT);
                }
                
                Image image = null;
                
                // Create a coloured rectangle image and add to the image registry
                ColorInfo colorInfo = fColorInfoMap.get(element);
                if(colorInfo != null && colorInfo.getCurrentColor() != null) {
                    image = fImageRegistry.get(colorInfo.toString());
                    if(image == null) {
                        image = new Image(getShell().getDisplay(), (ImageDataProvider) zoom -> {
                            // Draw the color rectangle onto a temp Image
                            Image img = new Image(getShell().getDisplay(), 16, 16);

                            GC gc = new GC(img);
                            gc.setBackground(colorInfo.getCurrentColor());
                            gc.fillRectangle(0, 0, 15, 15);
                            gc.drawRectangle(0, 0, 15, 15);
                            gc.dispose();

                            ImageData imageData = img.getImageData(zoom);
                            img.dispose();
                            return imageData;
                        });

                        fImageRegistry.put(colorInfo.toString(), image);
                    }
                }
                
                return image;
            }
        });
        
        //fTreeViewer.setAutoExpandLevel(2);

        // Buttons
        Composite buttonClient = new Composite(client, SWT.NULL);
        GridData gd = new GridData(SWT.TOP, SWT.TOP, false, false);
        buttonClient.setLayoutData(gd);
        buttonClient.setLayout(new GridLayout());
        
        // Edit...
        fEditFillColorButton = new Button(buttonClient, SWT.PUSH);
        fEditFillColorButton.setText(Messages.ColoursPreferencePage_13);
        setButtonLayoutData(fEditFillColorButton);
        fEditFillColorButton.setEnabled(false);
        fEditFillColorButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(event -> {
            editSelected();
        }));

        // Reset
        fResetFillColorButton = new Button(buttonClient, SWT.PUSH);
        fResetFillColorButton.setText(Messages.ColoursPreferencePage_14);
        setButtonLayoutData(fResetFillColorButton);
        fResetFillColorButton.setEnabled(false);
        fResetFillColorButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(event -> {
            resetSelected();
        }));
        
        // Import Scheme
        Button importButton = new Button(buttonClient, SWT.PUSH);
        importButton.setText(Messages.ColoursPreferencePage_15);
        setButtonLayoutData(importButton);
        importButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        importButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(event -> {
            try {
                importUserColors();
            }
            catch(IOException ex) {
                Logger.error("Error importing", ex); //$NON-NLS-1$
            }
        }));
        
        // Export Scheme
        Button exportButton = new Button(buttonClient, SWT.PUSH);
        exportButton.setText(Messages.ColoursPreferencePage_16);
        setButtonLayoutData(exportButton);
        exportButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(event -> {
            try {
                exportUserColors();
            }
            catch(IOException ex) {
                Logger.error("Error exporting", ex); //$NON-NLS-1$
            }
        }));
        
        // Derive element line colours
        fDeriveElementLineColorsButton = new Button(client, SWT.CHECK);
        fDeriveElementLineColorsButton.setText(Messages.ColoursPreferencePage_18);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDeriveElementLineColorsButton.setLayoutData(gd);
        fDeriveElementLineColorsButton.setSelection(getPreferenceStore().getBoolean(DERIVE_ELEMENT_LINE_COLOR));
        
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;

        // Persist user default colours
        fPersistUserDefaultColors = new Button(client, SWT.CHECK);
        fPersistUserDefaultColors.setText(Messages.ColoursPreferencePage_21);
        fPersistUserDefaultColors.setLayoutData(gd);
        fPersistUserDefaultColors.setSelection(getPreferenceStore().getBoolean(SAVE_USER_DEFAULT_COLOR));
        
        // Create Color Infos
        createColorInfos();
        
        // Set tree input
        fTreeViewer.setInput(this);
        
        return client;
    }
    
    /**
     * Create color infos
     */
    private void createColorInfos() {
        // ArchiMate elements
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            fColorInfoMap.put(eClass, new EClassColorInfo(eClass));
        }
        
        // Connectors (Junction)
        for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
            fColorInfoMap.put(eClass, new EClassColorInfo(eClass));
        }
        
        // Note
        EClass eClass = IArchimatePackage.eINSTANCE.getDiagramModelNote();
        fColorInfoMap.put(eClass, new EClassColorInfo(eClass));
        
        // Group
        eClass = IArchimatePackage.eINSTANCE.getDiagramModelGroup();
        fColorInfoMap.put(eClass, new EClassColorInfo(eClass));
        
        // Legend
        fColorInfoMap.put(IDiagramModelNote.LEGEND_MODEL_NAME, new ColorInfo(IDiagramModelNote.LEGEND_MODEL_NAME, Messages.ColoursPreferencePage_17, true));
        
        // Element line color
        fColorInfoMap.put(DEFAULT_ELEMENT_LINE_COLOR, new ColorInfo(DEFAULT_ELEMENT_LINE_COLOR, Messages.ColoursPreferencePage_11, false));
        
        // Connection line color
        fColorInfoMap.put(DEFAULT_CONNECTION_LINE_COLOR, new ColorInfo(DEFAULT_CONNECTION_LINE_COLOR, Messages.ColoursPreferencePage_12, false));
        
        // Folders
        for(FolderType folderType : FolderType.VALUES) {
            if(folderType != FolderType.USER) { // This is not used
                fColorInfoMap.put(folderType, new FolderColorInfo(folderType));
            }
        }
        
        // Themes
        for(String themeId : themeColors) {
            fColorInfoMap.put(themeId, new ThemeColorInfo(themeId));
        }
    }
    
    private void editSelected() {
        RGB newRGB = openColorDialog(fTreeViewer.getStructuredSelection().getFirstElement());
        if(newRGB != null) {
            for(Object object : fTreeViewer.getStructuredSelection()) {
                setColor(object, newRGB);
            }
        }

        fResetFillColorButton.setEnabled(isResetEnabled());
    }
    
    private boolean isEditEnabled() {
        for(Object object : fTreeViewer.getStructuredSelection().toArray()) {
            if(fColorInfoMap.get(object) != null) {
                return true;
            }
        }
        
        return false;
    }
    
    private void resetSelected() {
        for(Object object : fTreeViewer.getStructuredSelection().toArray()) {
            setColor(object, null);
        }
        
        fResetFillColorButton.setEnabled(isResetEnabled());
    }
    
    private boolean isResetEnabled() {
        for(Object object : fTreeViewer.getStructuredSelection().toArray()) {
            ColorInfo colorInfo = fColorInfoMap.get(object);
            if(colorInfo != null && colorInfo.isUserSet()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("ColoursPreferencePagePopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(manager -> {
            manager.add(new Action(Messages.ColoursPreferencePage_13) {
                @Override
                public void run() {
                    editSelected();
                }
                
                @Override
                public boolean isEnabled() {
                    return isEditEnabled();
                }
            });
            
            manager.add(new Action(Messages.ColoursPreferencePage_14) {
                @Override
                public void run() {
                    resetSelected();
                }
                
                @Override
                public boolean isEnabled() {
                    return isResetEnabled();
                }
            });
            
            manager.add(new Separator());
            
            manager.add(new Action(Messages.ColoursPreferencePage_19) {
                @Override
                public void run() {
                    fTreeViewer.expandAll();
                }
            });
            
            manager.add(new Action(Messages.ColoursPreferencePage_20) {
                @Override
                public void run() {
                    fTreeViewer.collapseAll();
                }
            });
        });
        
        Menu menu = menuMgr.createContextMenu(fTreeViewer.getControl());
        fTreeViewer.getControl().setMenu(menu);
    }

    /**
     * @param object Selected object
     * @return the RGB value or null
     * Open the color dialog to edit color for an object
     */
    private RGB openColorDialog(Object object) {
        CustomColorDialog colorDialog = new CustomColorDialog(getShell());

        ColorInfo colorInfo = fColorInfoMap.get(object);
        if(colorInfo != null) {
            Color color = colorInfo.getCurrentColor();
            if(color != null) {
                colorDialog.setRGB(color.getRGB());
            }
        }
        
        return colorDialog.open();
    }
    
    /**
     * Set a color for an object
     */
    private void setColor(Object object, RGB rgb) {
        if(object != null) {
            ColorInfo colorInfo = object instanceof ColorInfo ? (ColorInfo)object : fColorInfoMap.get(object);
            if(colorInfo != null && colorInfo.setColor(rgb)) {
                fImageRegistry.remove(colorInfo.toString()); // remove from image registry so we can generate a new image
                fTreeViewer.update(colorInfo.getObject(), null);
            }
        }
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(DERIVE_ELEMENT_LINE_COLOR, fDeriveElementLineColorsButton.getSelection());
        getPreferenceStore().setValue(SAVE_USER_DEFAULT_COLOR, fPersistUserDefaultColors.getSelection());
        
        saveColors(getPreferenceStore(), true);
        saveThemeColors();
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fDeriveElementLineColorsButton.setSelection(getPreferenceStore().getDefaultBoolean(DERIVE_ELEMENT_LINE_COLOR));
        fPersistUserDefaultColors.setSelection(getPreferenceStore().getDefaultBoolean(SAVE_USER_DEFAULT_COLOR));
        
        // Set colors to inbuilt defaults
        for(ColorInfo colorInfo : fColorInfoMap.values()) {
            setColor(colorInfo, null);
        }
        
        super.performDefaults();
    }
    
    /**
     * Import a User color scheme
     */
    private void importUserColors() throws IOException {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.ColoursPreferencePage_22);
        dialog.setFilterExtensions(new String[] { "ArchiColours.prefs", "*.prefs", "*.*" } );  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        dialog.setFileName("ArchiColours.prefs"); //$NON-NLS-1$
        
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        PreferenceStore store = new PreferenceStore(path);
        store.load();
        
        for(String prefKey : store.preferenceNames()) {
            RGB rgb = ColorFactory.convertStringToRGB(store.getString(prefKey));
            if(rgb != null) {
                for(ColorInfo colorInfo : fColorInfoMap.values()) {
                    if(Objects.equals(prefKey, colorInfo.getPreferenceKey())) {
                        setColor(colorInfo, rgb);
                    }
                }
            }
        }
    }
    
    /**
     * Export a user color scheme
     */
    private void exportUserColors() throws IOException {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText(Messages.ColoursPreferencePage_23);
        dialog.setFileName("ArchiColours.prefs"); //$NON-NLS-1$
        
        // Set to true for consistency on all OSs
        dialog.setOverwrite(true);
        
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        PreferenceStore store = new PreferenceStore(path);
        saveColors(store, false);
        store.save();
    }
    
    /**
     * @param store The preference store
     * @param useDefaults If true then a color is not saved if it matches the default color
     * Save colors to preference store
     */
    private void saveColors(IPreferenceStore store, boolean useDefaults) {
        for(ColorInfo colorInfo : fColorInfoMap.values().stream().filter(ci -> !(ci instanceof ThemeColorInfo)).toList()) { // Not theme colors
            // If the new color equals the inbuilt color set pref to default if useDefaults is true
            if(useDefaults && !colorInfo.isUserSet()) {
                store.setToDefault(colorInfo.getPreferenceKey());
            }
            // Else store color anyway
            else {
                store.setValue(colorInfo.getPreferenceKey(), ColorFactory.convertColorToString(colorInfo.getCurrentColor()));
            }
        }
    }
    
    /**
     * Save any workbench theme colors
     */
    private void saveThemeColors() {
        try {
            // Remove listener so we are not notified
            PlatformUI.getWorkbench().getThemeManager().removePropertyChangeListener(themeChangeListener);
            
            boolean themeColorChanged = false;
            
            for(String colorId : themeColors) {
                ColorInfo colorInfo = fColorInfoMap.get(colorId);
                if(colorInfo != null && !Objects.equals(colorInfo.getCurrentColor(), colorInfo.getUserColor())) { // Compare on User Color, not Inbuilt
                    ThemeUtils.setCurrentThemeColor(colorId, colorInfo.getCurrentColor().getRGB());
                    themeColorChanged = true;
                }
            }
            
            if(themeColorChanged) {
                PrefUtils.saveInstancePrefs(PrefUtils.ORG_ECLIPSE_UI_WORKBENCH);
                ThemeUtils.resetCurrentTheme();
            }
        }
        finally {
            // Add listener again
            PlatformUI.getWorkbench().getThemeManager().addPropertyChangeListener(themeChangeListener);
        }
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // Listen to external theme/color changes to update our theme colors to match current theme in case user changed the theme in prefs.
        // A side effect is that we trigger this event when setting a theme color so remove our listener when setting theme colors.
        themeChangeListener = event -> {
            if(themeColors.contains(event.getProperty())) {
                RGB newValue = ThemeUtils.getCurrentThemeColor(event.getProperty());
                setColor(event.getProperty(), newValue);
            }
        };

        workbench.getThemeManager().addPropertyChangeListener(themeChangeListener);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        fColorInfoMap.clear();
        fColorInfoMap = null;
        
        fImageRegistry.dispose();
        fImageRegistry = null;
        
        PlatformUI.getWorkbench().getThemeManager().removePropertyChangeListener(themeChangeListener);
    }
}