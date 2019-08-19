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
import org.eclipse.jface.viewers.ViewerComparator;
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
    
    private ComboViewer fThemeComboViewer;
    
    private IThemeEngine fThemeEngine;
    private ITheme fCurrentTheme;
    private String fDefaultTheme;
    
    private Button fShowStatusLineButton;
    
    private Button fShowUnusedElementsInModelTreeButton;
    private Button fAutoSearchButton;
    
    private Button fScaleImagesButton;

	public GeneralPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        Group fileGroup = new Group(client, SWT.NULL);
        fileGroup.setText(Messages.GeneralPreferencePage_0);
        fileGroup.setLayout(new GridLayout(2, false));
        fileGroup.setLayoutData(createHorizontalGridData(1));
        
        // Automatically open views when opening a model file
        fOpenDiagramsOnLoadButton = new Button(fileGroup, SWT.CHECK);
        fOpenDiagramsOnLoadButton.setText(Messages.GeneralPreferencePage_1);
        fOpenDiagramsOnLoadButton.setLayoutData(createHorizontalGridData(2));

        // Backup file on save
        fBackupOnSaveButton = new Button(fileGroup, SWT.CHECK);
        fBackupOnSaveButton.setText(Messages.GeneralPreferencePage_5);
        fBackupOnSaveButton.setLayoutData(createHorizontalGridData(2));
        
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
        appearanceGroup.setLayoutData(createHorizontalGridData(1));
        
        // Themes
        label = new Label(appearanceGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_4);
        fThemeComboViewer = new ComboViewer(appearanceGroup, SWT.READ_ONLY);
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
        
        // Show Status Line
        fShowStatusLineButton = new Button(appearanceGroup, SWT.CHECK);
        fShowStatusLineButton.setText(Messages.GeneralPreferencePage_9);
        fShowStatusLineButton.setLayoutData(createHorizontalGridData(2));
        
        label = new Label(appearanceGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_8);
        label.setLayoutData(createHorizontalGridData(2));
        
        // Model Tree
        Group modelTreeGroup = new Group(client, SWT.NULL);
        modelTreeGroup.setText(Messages.GeneralPreferencePage_10);
        modelTreeGroup.setLayout(new GridLayout(2, false));
        modelTreeGroup.setLayoutData(createHorizontalGridData(1));
        
        fShowUnusedElementsInModelTreeButton = new Button(modelTreeGroup, SWT.CHECK);
        fShowUnusedElementsInModelTreeButton.setText(Messages.GeneralPreferencePage_11);
        fShowUnusedElementsInModelTreeButton.setLayoutData(createHorizontalGridData(2));
        
        fAutoSearchButton = new Button(modelTreeGroup, SWT.CHECK);
        fAutoSearchButton.setText(Messages.GeneralPreferencePage_6);
        fAutoSearchButton.setLayoutData(createHorizontalGridData(2));
        
        label = new Label(modelTreeGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_7);
        label.setLayoutData(createHorizontalGridData(2));
        
        // Other
        Group otherGroup = new Group(client, SWT.NULL);
        otherGroup.setText(Messages.GeneralPreferencePage_12);
        otherGroup.setLayout(new GridLayout(2, false));
        otherGroup.setLayoutData(createHorizontalGridData(1));
        
        fScaleImagesButton = new Button(otherGroup, SWT.CHECK);
        fScaleImagesButton.setText(Messages.GeneralPreferencePage_13);
        fScaleImagesButton.setLayoutData(createHorizontalGridData(2));
        label = new Label(otherGroup, SWT.NULL);
        label.setText(Messages.GeneralPreferencePage_14);
        label.setLayoutData(createHorizontalGridData(2));
        
        setValues();
        
        fThemeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
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
        
        fShowStatusLineButton.setSelection(getPreferenceStore().getBoolean(SHOW_STATUS_LINE));
        
        fShowUnusedElementsInModelTreeButton.setSelection(getPreferenceStore().getBoolean(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE));
        fAutoSearchButton.setSelection(getPreferenceStore().getBoolean(TREE_SEARCH_AUTO));

        // Themes
        List<ITheme> themes = fThemeEngine.getThemes();
        fThemeComboViewer.setInput(themes.toArray());
        ITheme activeTheme = fThemeEngine.getActiveTheme();
        if(activeTheme != null) {
            fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
        }
        
        fScaleImagesButton.setSelection(getPreferenceStore().getBoolean(SCALE_IMAGE_EXPORT));
    }
    
    private void setSpinnerValues() {
        fMRUSizeSpinner.setSelection(getPreferenceStore().getInt(MRU_MAX));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(BACKUP_ON_SAVE, fBackupOnSaveButton.getSelection());
        getPreferenceStore().setValue(OPEN_DIAGRAMS_ON_LOAD, fOpenDiagramsOnLoadButton.getSelection());
        getPreferenceStore().setValue(MRU_MAX, fMRUSizeSpinner.getSelection());
        
        getPreferenceStore().setValue(SHOW_STATUS_LINE, fShowStatusLineButton.getSelection());
        
        getPreferenceStore().setValue(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE, fShowUnusedElementsInModelTreeButton.getSelection());
        getPreferenceStore().setValue(TREE_SEARCH_AUTO, fAutoSearchButton.getSelection());
        
        ITheme theme = (ITheme)((IStructuredSelection)fThemeComboViewer.getSelection()).getFirstElement();
        if(theme != null) {
            setTheme(theme, true);
            fCurrentTheme = theme;
        }

        getPreferenceStore().setValue(SCALE_IMAGE_EXPORT, fScaleImagesButton.getSelection());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fBackupOnSaveButton.setSelection(getPreferenceStore().getDefaultBoolean(BACKUP_ON_SAVE));
        fOpenDiagramsOnLoadButton.setSelection(getPreferenceStore().getDefaultBoolean(OPEN_DIAGRAMS_ON_LOAD));
        fMRUSizeSpinner.setSelection(getPreferenceStore().getDefaultInt(MRU_MAX));
        
        fShowStatusLineButton.setSelection(getPreferenceStore().getDefaultBoolean(SHOW_STATUS_LINE));
        
        fShowUnusedElementsInModelTreeButton.setSelection(getPreferenceStore().getDefaultBoolean(HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE));
        fAutoSearchButton.setSelection(getPreferenceStore().getDefaultBoolean(TREE_SEARCH_AUTO));
        
        setTheme(fDefaultTheme, false);
        
        ITheme activeTheme = fThemeEngine.getActiveTheme();
        if(activeTheme != null) {
            fThemeComboViewer.setSelection(new StructuredSelection(activeTheme));
        }
        
        fScaleImagesButton.setSelection(getPreferenceStore().getDefaultBoolean(SCALE_IMAGE_EXPORT));
        
        super.performDefaults();
    }
    
    @Override
    public boolean performCancel() {
        if(fCurrentTheme != fThemeEngine.getActiveTheme()) {
            setTheme(fCurrentTheme, false);
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
        // e4 method (taken from org.eclipse.ui.internal.dialogs.ViewsPreferencePage init(IWorkbench))
        MApplication application = workbench.getService(MApplication.class);
        IEclipseContext context = application.getContext();
        fDefaultTheme = (String)context.get("cssTheme"); // This is "org.eclipse.e4.ui.css.theme.e4_default" //$NON-NLS-1$
        fThemeEngine = context.get(IThemeEngine.class);
        fCurrentTheme = fThemeEngine.getActiveTheme();
/*
        // e3 method
        Bundle bundle = FrameworkUtil.getBundle(ArchiPlugin.class);
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