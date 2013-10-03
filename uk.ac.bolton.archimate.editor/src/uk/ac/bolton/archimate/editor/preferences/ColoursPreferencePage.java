/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.preferences;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.FontFactory;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;

/**
 * Default Colours Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class ColoursPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants, Listener {
    
    public static String HELPID = "uk.ac.bolton.archimate.help.prefsColours"; //$NON-NLS-1$
    
    private Hashtable<ColorSelector, EClass> fDefaultFillColorsLookup = new Hashtable<ColorSelector, EClass>();
    
    private Button fPersistUserDefaultFillColors;
    private Button fShowUserDefaultFillColorsInApplication;
    
    // Keep track of Ctrl/Command key down/up
    private boolean fModKeyPressed;
    
	public ColoursPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout(3, true));
        
        Label label = new Label(client, SWT.NULL);
        label.setText(Messages.ColoursPreferencePage_0);
        label.setFont(FontFactory.SystemFontBold);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        label.setLayoutData(gd);
        
        fPersistUserDefaultFillColors = new Button(client, SWT.CHECK);
        fPersistUserDefaultFillColors.setText(Messages.ColoursPreferencePage_1);
        fPersistUserDefaultFillColors.setLayoutData(gd);
        fPersistUserDefaultFillColors.setSelection(getPreferenceStore().getBoolean(SAVE_USER_DEFAULT_FILL_COLOR));
        
        fShowUserDefaultFillColorsInApplication = new Button(client, SWT.CHECK);
        fShowUserDefaultFillColorsInApplication.setText(Messages.ColoursPreferencePage_6);
        fShowUserDefaultFillColorsInApplication.setLayoutData(gd);
        fShowUserDefaultFillColorsInApplication.setSelection(getPreferenceStore().getBoolean(SHOW_FILL_COLORS_IN_GUI));
        
        Composite client1 = new Composite(client, SWT.NULL);
        client1.setLayout(new GridLayout(2, false));
        client1.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, false));
        
        for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
            createColorSelector(client1, eClass);
        }
        
        Composite client2 = new Composite(client, SWT.NULL);
        client2.setLayout(new GridLayout(2, false));
        client2.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, false));
        
        for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
            createColorSelector(client2, eClass);
        }
       
        for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
            createColorSelector(client2, eClass);
        }

        Composite client3 = new Composite(client, SWT.NULL);
        client3.setLayout(new GridLayout(2, false));
        client3.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, false));
        
        for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
            createColorSelector(client3, eClass);
        }
        
        for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
            createColorSelector(client3, eClass);
        }
        
        Label label2 = new Label(parent, SWT.NULL);
        label2.setText(Messages.ColoursPreferencePage_7);

        Composite client4 = new Composite(parent, SWT.NULL);
        client4.setLayout(new GridLayout(2, false));
        gd = new GridData(SWT.RIGHT, SWT.NULL, false, false);
        client4.setLayoutData(gd);
        
        Button importButton = new Button(client4, SWT.PUSH);
        importButton.setText(Messages.ColoursPreferencePage_2);
        importButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    importUserDefaults();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        Button exportButton = new Button(client4, SWT.PUSH);
        exportButton.setText(Messages.ColoursPreferencePage_3);
        exportButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    exportUserDefaults();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Listen to Mod key press
        getShell().getDisplay().addFilter(SWT.KeyDown, this);
        getShell().getDisplay().addFilter(SWT.KeyUp, this);
        
        return client;
    }
    
    private void createColorSelector(Composite parent, EClass eClass) {
        Label l = new Label(parent, SWT.NULL);
        l.setText(ArchimateLabelProvider.INSTANCE.getDefaultName(eClass));
        
        ColorSelector colorSelector = new ColorSelector(parent) {
            @Override
            public void open() {
                if(fModKeyPressed) {
                    EClass eClass = fDefaultFillColorsLookup.get(this);
                    RGB defaultValue = ColorFactory.getInbuiltDefaultColor(eClass).getRGB();
                    setColorValue(defaultValue);
                }
                else {
                    super.open();
                }
            }
        };
        
        fDefaultFillColorsLookup.put(colorSelector, eClass);
        
        Color color = ColorFactory.getDefaultFillColor(eClass);
        colorSelector.setColorValue(color.getRGB());
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(SAVE_USER_DEFAULT_FILL_COLOR, fPersistUserDefaultFillColors.getSelection());
        getPreferenceStore().setValue(SHOW_FILL_COLORS_IN_GUI, fShowUserDefaultFillColorsInApplication.getSelection());
        saveColors(getPreferenceStore());        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        super.performDefaults();
        
        fPersistUserDefaultFillColors.setSelection(getPreferenceStore().getDefaultBoolean(SAVE_USER_DEFAULT_FILL_COLOR));
        fShowUserDefaultFillColorsInApplication.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_FILL_COLORS_IN_GUI));

        for(Entry<ColorSelector, EClass> entry : fDefaultFillColorsLookup.entrySet()) {
            RGB rgb = ColorFactory.getInbuiltDefaultColor(entry.getValue()).getRGB();
            entry.getKey().setColorValue(rgb);
        }
    }
    
    private void importUserDefaults() throws IOException {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setText(Messages.ColoursPreferencePage_4);
        dialog.setFileName("ArchiColours.prefs"); //$NON-NLS-1$
        String path = dialog.open();
        if(path == null) {
            return;
        }
        
        PreferenceStore store = new PreferenceStore(path);
        store.load();

        for(Entry<ColorSelector, EClass> entry : fDefaultFillColorsLookup.entrySet()) {
            String key = IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX + entry.getValue().getName();
            String value = store.getString(key);
            
            if(StringUtils.isSet(value)) {
                RGB rgb = ColorFactory.convertStringToRGB(value);
                if(rgb != null) { // might be null if value is duff
                    entry.getKey().setColorValue(rgb);
                }
            }
        }
    }
    
    private void exportUserDefaults() throws IOException {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText(Messages.ColoursPreferencePage_5);
        dialog.setFileName("ArchiColours.prefs"); //$NON-NLS-1$
        String path = dialog.open();
        if(path == null) {
            return;
        }

        PreferenceStore store = new PreferenceStore(path);
        saveColors(store);
        store.save();
    }
    
    private void saveColors(IPreferenceStore store) {
        for(Entry<ColorSelector, EClass> entry : fDefaultFillColorsLookup.entrySet()) {
            RGB rgbNew = entry.getKey().getColorValue();
            RGB rgbDefault = ColorFactory.getInbuiltDefaultColor(entry.getValue()).getRGB();
            String key = DEFAULT_FILL_COLOR_PREFIX + entry.getValue().getName();
            if(rgbNew.equals(rgbDefault)) {
                store.setToDefault(key);
            }
            else {
                store.setValue(key, ColorFactory.convertRGBToString(rgbNew));
            }
        }
    }
    
    public void init(IWorkbench workbench) {
    }
    
    @Override
    public void handleEvent(Event event) {
        // Mod key pressed/released
        switch(event.type) {
            case SWT.KeyDown:
                if(event.keyCode == SWT.MOD1) {
                    fModKeyPressed = true;
                }
                break;
            case SWT.KeyUp:
                if(event.keyCode == SWT.MOD1) {
                    fModKeyPressed = false;
                }
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        
        fDefaultFillColorsLookup.clear();
        fDefaultFillColorsLookup = null;
        
        getShell().getDisplay().removeFilter(SWT.KeyDown, this);
        getShell().getDisplay().removeFilter(SWT.KeyUp, this);
    }
}