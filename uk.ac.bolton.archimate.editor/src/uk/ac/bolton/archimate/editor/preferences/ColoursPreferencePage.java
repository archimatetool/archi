/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.preferences;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.FontFactory;
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
    
    private Hashtable<ColorSelector, EClass> fDefaultFillColorsLookup = new Hashtable<ColorSelector, EClass>();
    
    private Button fPersistUserDefaultFillColors;
    
	public ColoursPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout(3, false));
        
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
        
        return client;
    }
    
    private void createColorSelector(Composite parent, EClass eClass) {
        Label l = new Label(parent, SWT.NULL);
        l.setText(ArchimateLabelProvider.INSTANCE.getDefaultName(eClass));
        ColorSelector colorSelector = new ColorSelector(parent);
        fDefaultFillColorsLookup.put(colorSelector, eClass);
        
        Color color = ColorFactory.getDefaultFillColor(eClass);
        colorSelector.setColorValue(color.getRGB());
    }
    
    @Override
    public boolean performOk() {
        for(Entry<ColorSelector, EClass> entry : fDefaultFillColorsLookup.entrySet()) {
            RGB rgbNew = entry.getKey().getColorValue();
            RGB rgbOld = ColorFactory.getInbuiltDefaultColor(entry.getValue()).getRGB();
            String key = DEFAULT_FILL_COLOR_PREFIX + entry.getValue().getName();
            if(rgbNew.equals(rgbOld)) {
                getPreferenceStore().setToDefault(key);
            }
            else {
                getPreferenceStore().setValue(key, ColorFactory.convertRGBToString(rgbNew));
            }
        }
        
        getPreferenceStore().setValue(SAVE_USER_DEFAULT_FILL_COLOR, fPersistUserDefaultFillColors.getSelection());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        super.performDefaults();
        
        fPersistUserDefaultFillColors.setSelection(getPreferenceStore().getDefaultBoolean(SAVE_USER_DEFAULT_FILL_COLOR));

        for(Entry<ColorSelector, EClass> entry : fDefaultFillColorsLookup.entrySet()) {
            RGB rgb = ColorFactory.getInbuiltDefaultColor(entry.getValue()).getRGB();
            entry.getKey().setColorValue(rgb);
        }
    }
    
    public void init(IWorkbench workbench) {
    }
    
    @Override
    public void dispose() {
        super.dispose();
        fDefaultFillColorsLookup.clear();
        fDefaultFillColorsLookup = null;
    }
}