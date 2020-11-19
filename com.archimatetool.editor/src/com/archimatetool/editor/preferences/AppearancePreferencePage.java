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
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.utils.PlatformUtils;


/**
 * Appearance Preferences Page
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class AppearancePreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static String HELP_ID = "com.archimatetool.help.prefsAppearance"; //$NON-NLS-1$
    
    private ComboViewer fThemeComboViewer;
    
    private Button fUseRoundTabsButton;
    private Button fShowStatusLineButton;
    
    private ITheme fCurrentTheme;
    
	public AppearancePreferencePage() {
		setPreferenceStore(Preferences.STORE);
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
        
        // Themes
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
        
        // Use Round Tabs
        fUseRoundTabsButton = new Button(client, SWT.CHECK);
        fUseRoundTabsButton.setText(Messages.AppearancePreferencePage_3);
        fUseRoundTabsButton.setLayoutData(createHorizontalGridData(2));
        
        // Show Status Line
        fShowStatusLineButton = new Button(client, SWT.CHECK);
        fShowStatusLineButton.setText(Messages.AppearancePreferencePage_2);
        fShowStatusLineButton.setLayoutData(createHorizontalGridData(2));
        
        setValues();
        
        fThemeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                ITheme theme = (ITheme)((IStructuredSelection)fThemeComboViewer.getSelection()).getFirstElement();
                setTheme(theme, false);
            }
        });
        
        return client;
    }

    private void setValues() {
        // Themes list
        List<ITheme> themes = new ArrayList<ITheme>();
        
        // Get Themes for this OS
        for(ITheme theme : ThemeUtils.getThemeEngine().getThemes()) {
            if(!theme.getId().contains("linux") && !theme.getId().contains("macosx") && !theme.getId().contains("win32")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                themes.add(theme);
            }
        }

        fThemeComboViewer.setInput(themes.toArray());
        
        ITheme activeTheme = ThemeUtils.getThemeEngine().getActiveTheme();
        if(activeTheme != null) {
            fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
        }
        
        fShowStatusLineButton.setSelection(getPreferenceStore().getBoolean(SHOW_STATUS_LINE));
        fUseRoundTabsButton.setSelection(ThemeUtils.getSwtRendererPreferences().getBoolean(ThemeUtils.USE_ROUND_TABS, false));
    }
    
    @Override
    public boolean performOk() {
        // Theme
        ITheme theme = (ITheme)((IStructuredSelection)fThemeComboViewer.getSelection()).getFirstElement();
        if(theme != null) {
            setTheme(theme, true);
        }
        
        // Status line
        getPreferenceStore().setValue(SHOW_STATUS_LINE, fShowStatusLineButton.getSelection());
        
        // Round tabs
        IEclipsePreferences prefs = ThemeUtils.getSwtRendererPreferences();
        prefs.putBoolean(ThemeUtils.USE_ROUND_TABS, fUseRoundTabsButton.getSelection());
        
        try {
            // Have to do this for it to persist
            prefs.flush();
        }
        catch(BackingStoreException ex) {
        }

        return true;
    }
    
    @Override
    protected void performDefaults() {
        // Theme
        ThemeUtils.getThemeEngine().setTheme(ThemeUtils.getDefaultThemeName(), false);
        ITheme activeTheme = ThemeUtils.getThemeEngine().getActiveTheme();
        if(activeTheme != null) {
            fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
        }
        
        fUseRoundTabsButton.setSelection(false);
        fShowStatusLineButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_STATUS_LINE));
        
        super.performDefaults();
    }
    
    @Override
    public boolean performCancel() {
        // Cancel theme
        if(fCurrentTheme != ThemeUtils.getThemeEngine().getActiveTheme()) {
            ThemeUtils.getThemeEngine().setTheme(fCurrentTheme, false);
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
        fCurrentTheme = ThemeUtils.getThemeEngine().getActiveTheme();
    }
    
    private void setTheme(ITheme theme, boolean persist) {
        // Seems to be fixed
        //hideEmptyShells(true);
        
        ThemeUtils.getThemeEngine().setTheme(theme, persist);
        
        // Seems to be fixed
        //hideEmptyShells(false);
        
        if(persist) {
            fCurrentTheme = ThemeUtils.getThemeEngine().getActiveTheme();
        }
    }
    
    /**
     * Hack for e4 bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=435915
     * Changing the Appearance Theme causes any hidden Shell to momentarily appear
     * such as the one created by FigureUtilities.getGC().
     * So find each empty Shell and set its Alpha to 0 before setting the theme and set it back to 255
     * This hides the Shell and unhides it.
     */
    @SuppressWarnings("unused")
    private void hideEmptyShells(boolean set) {
        if(PlatformUtils.isWindows()) {
            for(Shell shell : Display.getCurrent().getShells()) {
                if(shell.getChildren().length == 0) {
                    shell.setAlpha(set ? 0 : 255);
                }
            }
        }
    }
}