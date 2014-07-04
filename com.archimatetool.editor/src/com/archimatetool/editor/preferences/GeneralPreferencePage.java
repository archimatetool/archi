/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.utils.PlatformUtils;

/**
 * General Preferences Page
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class GeneralPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    private static String HELP_ID = "com.archimatetool.help.prefsGeneral"; //$NON-NLS-1$
    
    private Button fOpenDiagramsOnLoadButton;
    private Button fBackupOnSaveButton;
    
    private Spinner fMRUSizeSpinner;
    private Button fAnimateVisualiserNodesButton;
    
    private ComboViewer fThemeComboViewer;
    
    private IThemeEngine fThemeEngine;
    private ITheme fCurrentTheme;
    private String fDefaultTheme;
    
    private Button fShowStatusLineButton;

	public GeneralPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        GridData gd;
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.GeneralPreferencePage_0);
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Automatically open views when opening a model file
        fOpenDiagramsOnLoadButton = new Button(fileGroup, SWT.CHECK);
        fOpenDiagramsOnLoadButton.setText(Messages.GeneralPreferencePage_1);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fOpenDiagramsOnLoadButton.setLayoutData(gd);

        // Backup file on save
        fBackupOnSaveButton = new Button(fileGroup, SWT.CHECK);
        fBackupOnSaveButton.setText(Messages.GeneralPreferencePage_5);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fBackupOnSaveButton.setLayoutData(gd);
        
        // Size of recently opened file list
        Label label = new Label(fileGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_2);
        
        fMRUSizeSpinner = new Spinner(fileGroup, SWT.BORDER);
        fMRUSizeSpinner.setMinimum(3);
        fMRUSizeSpinner.setMaximum(15);
        
        // Appearance
        Group appearanceGroup = new Group(client, SWT.NULL);
        appearanceGroup.setText(Messages.GeneralPreferencePage_3);
        appearanceGroup.setLayout(new GridLayout(2, false));
        appearanceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Themes
        label = new Label(appearanceGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_4);
        fThemeComboViewer = new ComboViewer(appearanceGroup, SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fThemeComboViewer.getCombo().setLayoutData(gd);
        
        fThemeComboViewer.setContentProvider(new IStructuredContentProvider() {
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            public void dispose() {
            }
            
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
        
        fThemeComboViewer.setSorter(new ViewerSorter());
        
        // Show Status Line
        fShowStatusLineButton = new Button(appearanceGroup, SWT.CHECK);
        fShowStatusLineButton.setText(Messages.GeneralPreferencePage_9);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fShowStatusLineButton.setLayoutData(gd);
        
        label = new Label(appearanceGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_8);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);
        
        // Visualiser
        Group visualiserGroup = new Group(client, SWT.NULL);
        visualiserGroup.setText(Messages.GeneralPreferencePage_6);
        visualiserGroup.setLayout(new GridLayout(2, false));
        visualiserGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fAnimateVisualiserNodesButton = new Button(visualiserGroup, SWT.CHECK);
        fAnimateVisualiserNodesButton.setText(Messages.GeneralPreferencePage_7);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fAnimateVisualiserNodesButton.setLayoutData(gd);
        
        setValues();
        
        fThemeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                ITheme theme = (ITheme)((IStructuredSelection)fThemeComboViewer.getSelection()).getFirstElement();
                if(theme != null && theme != fThemeEngine.getActiveTheme()) {
                    setTheme(theme, false);
                }
            }
        });
        
        return client;
    }

    private void setValues() {
        setSpinnerValues();
        fBackupOnSaveButton.setSelection(getPreferenceStore().getBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fAnimateVisualiserNodesButton.setSelection(getPreferenceStore().getBoolean(ANIMATE_VISUALISER_NODES));
        fShowStatusLineButton.setSelection(getPreferenceStore().getBoolean(SHOW_STATUS_LINE));

        // Themes
        List<ITheme> themes = fThemeEngine.getThemes();
        fThemeComboViewer.setInput(themes.toArray());
        ITheme activeTheme = fThemeEngine.getActiveTheme();
        if(activeTheme != null) {
            fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
        }
    }
    
    private void setSpinnerValues() {
        fMRUSizeSpinner.setSelection(getPreferenceStore().getInt(MRU_MAX));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(BACKUP_ON_SAVE, fBackupOnSaveButton.getSelection());
        getPreferenceStore().setValue(OPEN_DIAGRAMS_ON_LOAD, fOpenDiagramsOnLoadButton.getSelection());
        getPreferenceStore().setValue(MRU_MAX, fMRUSizeSpinner.getSelection());
        getPreferenceStore().setValue(ANIMATE_VISUALISER_NODES, fAnimateVisualiserNodesButton.getSelection());
        getPreferenceStore().setValue(SHOW_STATUS_LINE, fShowStatusLineButton.getSelection());
        
        ITheme theme = (ITheme)((IStructuredSelection)fThemeComboViewer.getSelection()).getFirstElement();
        if(theme != null) {
            setTheme(theme, true);
            fCurrentTheme = theme;
        }

        return true;
    }
    
    @Override
    protected void performDefaults() {
        fBackupOnSaveButton.setSelection(getPreferenceStore().getDefaultBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getDefaultBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fMRUSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(MRU_MAX));
        fAnimateVisualiserNodesButton.setSelection(getPreferenceStore().getDefaultBoolean(ANIMATE_VISUALISER_NODES));
        fShowStatusLineButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_STATUS_LINE));
        
        setTheme(fDefaultTheme, false);
        
        ITheme activeTheme = fThemeEngine.getActiveTheme();
        if(activeTheme != null) {
            fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
        }
        
        super.performDefaults();
    }
    
    @Override
    public boolean performCancel() {
        if(fCurrentTheme != fThemeEngine.getActiveTheme()) {
            setTheme(fCurrentTheme, false);
        }
        
        return super.performCancel();
    }
    
    public void init(IWorkbench workbench) {
        // e4 method (taken from org.eclipse.ui.internal.dialogs.ViewsPreferencePage init(IWorkbench))
        MApplication application = (MApplication)workbench.getService(MApplication.class);
        IEclipseContext context = application.getContext();
        fDefaultTheme = (String)context.get("cssTheme"); // This is "org.eclipse.e4.ui.css.theme.e4_default" //$NON-NLS-1$
        fThemeEngine = context.get(IThemeEngine.class);
        fCurrentTheme = fThemeEngine.getActiveTheme();
/*
        // e3 method
        Bundle bundle = FrameworkUtil.getBundle(ArchimateEditorPlugin.class);
        BundleContext context = bundle.getBundleContext();
        ServiceReference<IThemeManager> ref = context.getServiceReference(IThemeManager.class);
        IThemeManager themeManager = context.getService(ref);
        fThemeEngine = themeManager.getEngineForDisplay(Display.getCurrent());
        fDefaultTheme = "org.eclipse.e4.ui.css.theme.e4_default"; //$NON-NLS-1$
*/
    }
    
    
    /// Hacks for e4 bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=435915
    /// Changing the Appearance Theme causes any hidden Shell to momentarily appear
    /// So find an empty Shell (the one created by FigureUtilities.getGC()) and set its Alpha to 0
    /// before setting the theme. This hides the Shell and unhides it.
    //////////////////////////////////////////////
    
    private void setTheme(ITheme theme, boolean restore) {
        if(PlatformUtils.isWindows()) {
            dissolveEmptyShells(0);
            fThemeEngine.setTheme(theme, restore); // must be true to persist
            dissolveEmptyShells(255);
        }
        else {
            fThemeEngine.setTheme(theme, restore); // must be true to persist
        }
    }
    
    private void setTheme(String themeId, boolean restore) {
        if(PlatformUtils.isWindows()) {
            dissolveEmptyShells(0);
            fThemeEngine.setTheme(themeId, restore); // must be true to persist
            dissolveEmptyShells(255);
        }
        else {
            fThemeEngine.setTheme(themeId, restore); // must be true to persist
        }
    }

    private void dissolveEmptyShells(int value) {
        for(Shell shell : Display.getCurrent().getShells()) {
            if(shell.getChildren().length == 0) {
                shell.setAlpha(value);
            }
        }
    }
}