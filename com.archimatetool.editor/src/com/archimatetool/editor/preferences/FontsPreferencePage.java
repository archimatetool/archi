/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.StringUtils;

/**
 * Fonts Preference Page
 * 
 * @author Phillip Beauvoir
 */
public class FontsPreferencePage extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {

    public static String ID = "com.archimatetool.editor.prefsFonts"; //$NON-NLS-1$
    public static String HELPID = "com.archimatetool.help.prefsAppearance"; //$NON-NLS-1$

    /**
     * Font information for a control that is tied to a preference key
     */
    private static class FontInfo {
        String prefsKey;
        String text;
        String description;
        FontData fontData;
        
        FontInfo(String text, String description, String prefsKey) {
            this.text = text;
            this.description = description;
            this.prefsKey = prefsKey;
        }
        
        void performDefault() {
            fontData = getDefaultFontData();
        }
        
        FontData getFontData() {
            // Get font data from Preferences store
            if(fontData == null) {
                String fontDetails = ArchiPlugin.PREFERENCES.getString(prefsKey);
                if(StringUtils.isSet(fontDetails)) {
                    fontData = getSafeFontData(fontDetails);
                }
                else {
                    fontData = getDefaultFontData();
                }
            }
            
            return fontData;
        }
        
        FontData getDefaultFontData() {
            // Get default font data from Preferences store (this could be in a suppplied preference file)
            String fontDetails = ArchiPlugin.PREFERENCES.getDefaultString(prefsKey);
            if(StringUtils.isSet(fontDetails)) {
                return getSafeFontData(fontDetails);
            }
            
            return getSystemFontData();
        }
        
        FontData getSystemFontData() {
            return JFaceResources.getDefaultFont().getFontData()[0];
        }
        
        private FontData getSafeFontData(String fontDetails) {
            try {
                return new FontData(fontDetails);
            }
            catch(Exception ex) {
            }
            
            return getSystemFontData();
        }

        void performOK() {
            ArchiPlugin.PREFERENCES.setValue(prefsKey, getFontData().equals(getSystemFontData()) ? "" : getFontData().toString()); //$NON-NLS-1$
        }
    }

    // Table
    private TableViewer fTableViewer;

    private List<FontInfo> fontInfos = new ArrayList<>();

    private Button fEditFontButton;
    private Button fDefaultFontButton;

    private Label fDescriptionLabel;
    private Label fFontPreviewLabel;
    
    public FontsPreferencePage() {
        setPreferenceStore(ArchiPlugin.PREFERENCES);
        setDescription(Messages.FontsPreferencePage_21);
    }
    
    @Override
    public Composite createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);

        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
        client.addDisposeListener((e) -> {
            disposeLabelFont();
        });
        
        // Table
        createTable(client);
        
        // Buttons
        createButtonPanel(client);
        
        // Description
        createDescriptionPanel(client);
        
        // Preview
        createPreviewPanel(client);

        // Add font options
        addFontOptions();
        
        // Set table input
        fTableViewer.setInput(fontInfos);
        
        return client;
    }
    
    private void createTable(Composite parent) {
        fTableViewer = new TableViewer(parent);
        
        GridDataFactory.create(GridData.FILL_BOTH).hint(SWT.DEFAULT, 200).applyTo(fTableViewer.getTable());
        
        // Table Double-click listener
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = ((IStructuredSelection)event.getSelection());
                if(!selection.isEmpty()) {
                    FontInfo fontInfo = (FontInfo)selection.getFirstElement();
                    FontData fd = openFontDialog(fontInfo);
                    if(fd != null) {
                        fontInfo.fontData = fd;
                        fTableViewer.update(fontInfo, null);
                        updatePreview(fontInfo);
                    }
                }
            }
        });

        // Table Selection Changed Listener
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() { 
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = event.getStructuredSelection();
                fEditFontButton.setEnabled(!selection.isEmpty());
                fDefaultFontButton.setEnabled(!selection.isEmpty());
                if(!selection.isEmpty()) {
                    FontInfo fontInfo = (FontInfo)selection.getFirstElement();
                    updateDescription(fontInfo);
                    updatePreview(fontInfo);
                }
            }
        });
        
        // Table Content Provider
        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return fontInfos.toArray();
            }
        });
        
        // Table Label Provider
        fTableViewer.setLabelProvider(new CellLabelProvider() {
            private int tableHeight = fTableViewer.getTable().getFont().getFontData()[0].getHeight();
            private Map<FontInfo, Font> fontCache = new HashMap<>();
            
            @Override
            public void update(ViewerCell cell) {
                FontInfo fontInfo = (FontInfo)cell.getElement();
                cell.setText(fontInfo.text);
                cell.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_FONT));
                cell.setFont(getFont(fontInfo));
            }

            private Font getFont(FontInfo fontInfo) {
                Font font = fontCache.get(fontInfo);
                if(font != null) {
                    font.dispose();
                }
                
                FontData fd = new FontData(fontInfo.getFontData().toString());
                fd.setHeight(tableHeight);
                font = new Font(null, fd);
                fontCache.put(fontInfo, font);
                
                return font;
            }
            
            @Override
            public void dispose() {
                for(Entry<FontInfo, Font> entry : fontCache.entrySet()) {
                    entry.getValue().dispose();
                }
                super.dispose();
            }
        });
    }
    
    private void createButtonPanel(Composite parent) {
        Composite buttonClient = new Composite(parent, SWT.NULL);
        GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).applyTo(buttonClient);
        buttonClient.setLayout(new GridLayout());

        // Edit...
        fEditFontButton = new Button(buttonClient, SWT.PUSH);
        fEditFontButton.setText(Messages.FontsPreferencePage_2);
        setButtonLayoutData(fEditFontButton);
        fEditFontButton.setEnabled(false);
        fEditFontButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                IStructuredSelection selection = fTableViewer.getStructuredSelection();
                if(!selection.isEmpty()) {
                    FontData fd = openFontDialog((FontInfo)selection.getFirstElement());
                    if(fd != null) {
                        for(Object object : selection.toList()) {
                            ((FontInfo)object).fontData = fd;
                        }
                        updatePreview((FontInfo)selection.getFirstElement());
                        fTableViewer.refresh();
                    }
                }
            }
        });

        // Default
        fDefaultFontButton = new Button(buttonClient, SWT.PUSH);
        fDefaultFontButton.setText(Messages.FontsPreferencePage_7);
        setButtonLayoutData(fDefaultFontButton);
        fDefaultFontButton.setEnabled(false);
        fDefaultFontButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                IStructuredSelection selection = fTableViewer.getStructuredSelection();
                if(!selection.isEmpty()) {
                    for(Object object :  selection.toList()) {
                        ((FontInfo)object).performDefault();
                    }
                    updatePreview((FontInfo)selection.getFirstElement());
                    fTableViewer.refresh();
                }
            }
        });
    }
    
    private void createDescriptionPanel(Composite parent) {
        Group group = new Group(parent, SWT.NULL);
        group.setText(Messages.FontsPreferencePage_11);
        group.setLayout(new GridLayout());
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(group);
        
        fDescriptionLabel = new Label(group, SWT.WRAP);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fDescriptionLabel);
    }
    
    private void updateDescription(FontInfo fontInfo) {
        fDescriptionLabel.setText(fontInfo.description);
    }
    
    private void createPreviewPanel(Composite parent) {
        Group group = new Group(parent, SWT.NULL);
        group.setText(Messages.FontsPreferencePage_8);
        group.setLayout(new GridLayout());
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(group);
        
        fFontPreviewLabel = new Label(group, SWT.NONE);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fFontPreviewLabel);
    }
    
    private void updatePreview(FontInfo fontInfo) {
        FontData fd = fontInfo.getFontData();
        
        fFontPreviewLabel.setText(fd.getName() + " " + //$NON-NLS-1$
                fd.getHeight() + " " + //$NON-NLS-1$
                ((fd.getStyle() & SWT.BOLD) == SWT.BOLD ? Messages.FontsPreferencePage_5 : "") + " " +  //$NON-NLS-1$//$NON-NLS-2$
                ((fd.getStyle() & SWT.ITALIC) == SWT.ITALIC ? Messages.FontsPreferencePage_6 : ""));  //$NON-NLS-1$
        
        Font font = new Font(null, fd);
        fFontPreviewLabel.setFont(font);
        disposeLabelFont();
        fFontPreviewLabel.setData(font);
        
        GC gc = new GC(getControl());
        gc.setFont(font);
        ((GridData)fFontPreviewLabel.getLayoutData()).heightHint = gc.getFontMetrics().getHeight();
        gc.dispose();
        fFontPreviewLabel.getParent().getParent().layout();
    }

    /**
     * Add Font options for controls
     */
    private void addFontOptions() {
        // View object/connection default font gets and sets its font info in a special way in FontFactory
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_1, Messages.FontsPreferencePage_12, DEFAULT_VIEW_FONT) {
            @Override
            void performOK() {
                FontFactory.setDefaultUserViewFont(getFontData());
            }
            
            @Override
            FontData getSystemFontData() {
                return FontFactory.getDefaultViewOSFontData();
            }
        });

        // Single line text control font
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_10, Messages.FontsPreferencePage_13, SINGLE_LINE_TEXT_FONT));
        
        // Multiline text control font
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_4, Messages.FontsPreferencePage_14, MULTI_LINE_TEXT_FONT));
        
        // Model Tree font
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_0, Messages.FontsPreferencePage_15, MODEL_TREE_FONT));
        
        // Navigator Tree font
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_9, Messages.FontsPreferencePage_16, NAVIGATOR_TREE_FONT));

        // Properties Table font
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_17, Messages.FontsPreferencePage_18, PROPERTIES_TABLE_FONT));

        // Analysis Table font
        fontInfos.add(new FontInfo(Messages.FontsPreferencePage_19, Messages.FontsPreferencePage_20, ANALYSIS_TABLE_FONT));
    }
    
    private FontData openFontDialog(FontInfo fontInfo) {
        FontDialog dialog = new FontDialog(fTableViewer.getControl().getShell());
        dialog.setEffectsVisible(false); // Don't allow underline/strikeout on Windows. See https://github.com/archimatetool/archi/issues/851
        dialog.setText(Messages.FontsPreferencePage_3);
        dialog.setFontList(new FontData[] { fontInfo.getFontData() });
        return dialog.open();
    }
    
    @Override
    public void performDefaults() {
        for(FontInfo info : fontInfos) {
            info.performDefault();
        }
        
        fTableViewer.refresh();
        
        FontInfo fontInfo = (FontInfo)fTableViewer.getStructuredSelection().getFirstElement();
        if(fontInfo != null) {
            updatePreview((FontInfo)fTableViewer.getStructuredSelection().getFirstElement());
        }
        
        super.performDefaults();
    }
    
    @Override
    public boolean performOk() {
        for(FontInfo info : fontInfos) {
            info.performOK();
        }
        
        return true;
    }

    private void disposeLabelFont() {
        Font labelFont = (Font)fFontPreviewLabel.getData();
        if(labelFont != null) {
            labelFont.dispose();
        }
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
    
}
