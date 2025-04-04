/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.utils.PlatformUtils;


/**
 * Appearance Preferences Page
 * 
 * Based on org.eclipse.ui.internal.dialogs.ViewsPreferencePage
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class AppearancePreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static String HELP_ID = "com.archimatetool.help.prefsAppearance"; //$NON-NLS-1$
    
    private ComboViewer fThemeComboViewer;
    
    private Button fUseThemes;
    private Button fUseRoundTabsButton;
    private Button fShowStatusLineButton;
    
    private Button fMacNativeItemHeightButton;
    
    private IThemeEngine themeEngine;
    private ITheme lastActiveTheme;
    
    // OS High Contrast mode
    private boolean highContrastMode;
    
    /**
     * Pseudo theme to set automatic light/dark on startup
     */
    private static ITheme AUTOMATIC_THEME = new ITheme() {
        @Override
        public String getId() {
            return Display.isSystemDarkTheme() ? ThemeUtils.E4_DARK_THEME_ID : ThemeUtils.E4_DEFAULT_THEME_ID;
        }

        @Override
        public String getLabel() {
            return Messages.AppearancePreferencePage_7;
        }
    };

    
	public AppearancePreferencePage() {
		setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.AppearancePreferencePage_0);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        // Enable Theming
        fUseThemes = new Button(client, SWT.CHECK);
        fUseThemes.setText(Messages.AppearancePreferencePage_4);
        fUseThemes.setLayoutData(createHorizontalGridData(2));
        
        // Get system high contrast mode
        highContrastMode = parent.getDisplay().getHighContrast();
        
        // Themes, if enabled
        if(themeEngine != null) {
            Label label = new Label(client, SWT.NULL);
            label.setText(Messages.AppearancePreferencePage_1);
            fThemeComboViewer = new ComboViewer(client, SWT.READ_ONLY);
            fThemeComboViewer.getCombo().setLayoutData(createHorizontalGridData(1));
            
            fThemeComboViewer.setContentProvider(new IStructuredContentProvider() {
                @Override
                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                }
                
                @Override
                public void dispose() {
                }
                
                @Override
                public Object[] getElements(Object inputElement) {
                    return (Object[])inputElement;
                }
            });
            
            fThemeComboViewer.setLabelProvider(new LabelProvider() {
                @Override
                public String getText(Object element) {
                    return ((ITheme)element).getLabel();
                }
            });
            
            fThemeComboViewer.setComparator(new ViewerComparator());
            
            // Disable if we are in high contrast mode, and ony show that theme
            fThemeComboViewer.getCombo().setEnabled(!highContrastMode);
        }
        
        // Use Round Tabs
        fUseRoundTabsButton = new Button(client, SWT.CHECK);
        fUseRoundTabsButton.setText(Messages.AppearancePreferencePage_3);
        fUseRoundTabsButton.setLayoutData(createHorizontalGridData(2));
        
        // Show Status Line
        fShowStatusLineButton = new Button(client, SWT.CHECK);
        fShowStatusLineButton.setText(Messages.AppearancePreferencePage_2);
        fShowStatusLineButton.setLayoutData(createHorizontalGridData(2));
        
        // Mac item height
        if(PlatformUtils.isMac()) {
            fMacNativeItemHeightButton = new Button(client, SWT.CHECK);
            fMacNativeItemHeightButton.setText(Messages.AppearancePreferencePage_5);
            fMacNativeItemHeightButton.setToolTipText(Messages.AppearancePreferencePage_6);
            fMacNativeItemHeightButton.setLayoutData(createHorizontalGridData(2));
        }
        
        setValues();
        
        if(themeEngine != null) {
            fThemeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
                @Override
                public void selectionChanged(SelectionChangedEvent event) {
                    ITheme theme = (ITheme)fThemeComboViewer.getStructuredSelection().getFirstElement();
                    themeEngine.setTheme(theme.getId(), false); // set theme id, not theme in case of automatic theme
                }
            });
        }
        
        return client;
    }

    private void setValues() {
        // Themes list
        if(themeEngine != null) {
            List<ITheme> themes = new ArrayList<>();
            themes.add(AUTOMATIC_THEME);
            
            for(ITheme theme : themeEngine.getThemes()) {
                /*
                 * When we have Win32 OS - when the high contrast mode is enabled on the
                 * platform, we display the 'high-contrast' special theme only. If not, we don't
                 * want to mess the themes combo with the theme since it is the special
                 * variation of the 'classic' one
                 *
                 * When we have GTK - we have to display the entire list of the themes since we
                 * are not able to figure out if the high contrast mode is enabled on the
                 * platform. The user has to manually select the theme if they need it
                 */
                if(!highContrastMode && !Util.isGtk() && theme.getId().equals(ThemeUtils.HIGH_CONTRAST_THEME_ID)) {
                    continue;
                }
                
                themes.add(theme);
            }
            
            fThemeComboViewer.setInput(themes.toArray());
            
            // If themeid is not present the ThemeEngine will try to set light/dark theme based on OS theme
            if(!highContrastMode && ThemeUtils.getThemePreferences().get(ThemeUtils.THEMEID_KEY, null) == null) {
                fThemeComboViewer.setSelection(new StructuredSelection(AUTOMATIC_THEME));
            }
            else {
                ITheme activeTheme = themeEngine.getActiveTheme();
                if(activeTheme != null) {
                    fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
                }
            }
        }
        
        fUseThemes.setSelection(themeEngine != null);
        fShowStatusLineButton.setSelection(getPreferenceStore().getBoolean(SHOW_STATUS_LINE));
        fUseRoundTabsButton.setSelection(ThemeUtils.getSwtRendererPreferences().getBoolean(ThemeUtils.USE_ROUND_TABS, false));
        
        // Mac native item height
        if(fMacNativeItemHeightButton != null) {
            boolean useNativeItemHeights = getPreferenceStore().getBoolean(MAC_ITEM_HEIGHT_PROPERTY_KEY);
            fMacNativeItemHeightButton.setSelection(useNativeItemHeights);
        }
    }
    
    @Override
    public boolean performOk() {
        // Theme
        if(themeEngine != null) {
            ITheme theme = (ITheme)fThemeComboViewer.getStructuredSelection().getFirstElement();
            
            if(theme == AUTOMATIC_THEME) {
                // Just remove the "themeid" key and then the ThemeEngine will use dark/light depending on OS theme on startup
                IEclipsePreferences themePrefs = ThemeUtils.getThemePreferences();
                themePrefs.remove(ThemeUtils.THEMEID_KEY);
                try {
                    themePrefs.flush();
                }
                catch(BackingStoreException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                themeEngine.setTheme(theme, !highContrastMode); // Don't persist theme if on high contrast mode
            }
            
            // Store this in both cases
            lastActiveTheme = themeEngine.getActiveTheme();
        }
        
        // Status line
        getPreferenceStore().setValue(SHOW_STATUS_LINE, fShowStatusLineButton.getSelection());
        
        IEclipsePreferences swtPrefs = ThemeUtils.getSwtRendererPreferences();
        
        // Enable Theming
        swtPrefs.putBoolean(ThemeUtils.THEME_ENABLED, fUseThemes.getSelection());
        
        // Round tabs
        swtPrefs.putBoolean(ThemeUtils.USE_ROUND_TABS, fUseRoundTabsButton.getSelection());
        
        try {
            // Have to do this for it to persist
            swtPrefs.flush();
        }
        catch(BackingStoreException ex) {
            ex.printStackTrace();
        }
        
        // Mac native item heights
        if(fMacNativeItemHeightButton != null) {
            getPreferenceStore().setValue(MAC_ITEM_HEIGHT_PROPERTY_KEY, fMacNativeItemHeightButton.getSelection());
        }

        return true;
    }
    
    @Override
    protected void performDefaults() {
        if(themeEngine != null) {
            themeEngine.setTheme(ThemeUtils.getDefaultThemeName(), false);
            ITheme activeTheme = themeEngine.getActiveTheme();
            if(activeTheme != null) {
                fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
            }
        }
        
        fUseThemes.setSelection(true);
        fUseRoundTabsButton.setSelection(false);
        fShowStatusLineButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_STATUS_LINE));
        
        if(fMacNativeItemHeightButton != null) {
            fMacNativeItemHeightButton.setSelection(false);
        }
        
        super.performDefaults();
    }
    
    @Override
    public boolean performCancel() {
        if(themeEngine != null && lastActiveTheme != null && lastActiveTheme != themeEngine.getActiveTheme()) {
            themeEngine.setTheme(lastActiveTheme, false);
        }
        
        return super.performCancel();
    }
    
    private GridData createHorizontalGridData(int span) {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = span;
        return gd;
    }
    
    @Override
    public void init(IWorkbench workbench) {
        themeEngine = ThemeUtils.getThemeEngine();
        lastActiveTheme = themeEngine == null ? null : themeEngine.getActiveTheme();
    }
}