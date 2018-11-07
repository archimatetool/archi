/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.hammer.ArchiHammerPlugin;



/**
 * Validator Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class ValidatorPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static String HELP_ID = "com.archimatetool.help.prefsValidator"; //$NON-NLS-1$
    
    private Button[] fDoCheckButtons;
    
    String[] fCheckers = {
            PREFS_HAMMER_CHECK_EMPTY_VIEWS, Messages.ValidatorPreferencePage_0,
            PREFS_HAMMER_CHECK_INVALID_RELATIONS, Messages.ValidatorPreferencePage_1,
            PREFS_HAMMER_CHECK_NESTING, Messages.ValidatorPreferencePage_2,
            PREFS_HAMMER_CHECK_UNUSED_ELEMENTS, Messages.ValidatorPreferencePage_3,
            PREFS_HAMMER_CHECK_UNUSED_RELATIONS, Messages.ValidatorPreferencePage_4,
            PREFS_HAMMER_CHECK_VIEWPOINT, Messages.ValidatorPreferencePage_5,
            PREFS_HAMMER_CHECK_DUPLICATE_ELEMENTS, Messages.ValidatorPreferencePage_7,
            PREFS_HAMMER_CHECK_JUNCTIONS, Messages.ValidatorPreferencePage_8
    };
    
	public ValidatorPreferencePage() {
		setPreferenceStore(ArchiHammerPlugin.INSTANCE.getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        GridData gd;
        
        Group checkerGroup = new Group(client, SWT.NULL);
        checkerGroup.setText(Messages.ValidatorPreferencePage_6);
        checkerGroup.setLayout(new GridLayout(2, false));
        checkerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fDoCheckButtons = new Button[fCheckers.length / 2];
        
        for(int i = 0; i < fCheckers.length / 2; i++) {
            fDoCheckButtons[i] = new Button(checkerGroup, SWT.CHECK);
            fDoCheckButtons[i].setText(fCheckers[i * 2 + 1]);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            fDoCheckButtons[i].setLayoutData(gd);
        }
        
        setValues();
        
        return client;
    }

    private void setValues() {
        for(int i = 0; i < fCheckers.length / 2; i++) {
            fDoCheckButtons[i].setSelection(getPreferenceStore().getBoolean(fCheckers[i * 2]));
        }
    }
    
    @Override
    public boolean performOk() {
        for(int i = 0; i < fCheckers.length / 2; i++) {
            getPreferenceStore().setValue(fCheckers[i * 2], fDoCheckButtons[i].getSelection());
        }
        return true;
    }
    
    @Override
    protected void performDefaults() {
        for(int i = 0; i < fCheckers.length / 2; i++) {
            fDoCheckButtons[i].setSelection(getPreferenceStore().getDefaultBoolean(fCheckers[i * 2]));
        }
        super.performDefaults();
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}