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
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.StringUtils;

/**
 * Fonts Preference Tab
 * 
 * @author Phillip Beauvoir
 */
public class FontsPreferenceTab implements IPreferenceConstants {

    /**
     * Font information for a control
     */
    private abstract static class FontInfo {
        protected String text;
        protected String description;
        protected FontData fontData;
        
        FontInfo(String text, String description, FontData fontData) {
            this.text = text;
            this.description = description;
            this.fontData = fontData;
        }
        
        void performDefault() {
            this.fontData = getDefaultFontData();
        }
        
        FontData getFontData() {
            return fontData;
        }
        
        abstract FontData getDefaultFontData();

        abstract void performOK();
    }
    
    /**
     * Font information for a control that is tied to a preference key
     */
    private static class FontInfoWithPreferences extends FontInfo {
        private String prefsKey;
        
        FontInfoWithPreferences(String text, String description, String prefsKey) {
            super(text, description, null);
            this.prefsKey = prefsKey;
        }

        @Override
        FontData getFontData() {
            // Get font data from Preferences store
            if(fontData == null) {
                String fontDetails = Preferences.STORE.getString(prefsKey);
                if(StringUtils.isSet(fontDetails)) {
                    fontData = new FontData(fontDetails);
                }
                else {
                    fontData = getDefaultFontData();
                }
            }
            
            return fontData;
        }
        
        @Override
        FontData getDefaultFontData() {
            return JFaceResources.getDefaultFont().getFontData()[0];
        }
        
        @Override
        void performOK() {
            Preferences.STORE.setValue(prefsKey, getFontData().equals(getDefaultFontData()) ? "" : getFontData().toString()); //$NON-NLS-1$
        }
    }

    // Table
    private TableViewer fTableViewer;

    private List<FontInfo> fontInfos = new ArrayList<>();

    private Button fEditFontButton;
    private Button fDefaultFontButton;

    private Label fDescriptionLabel;
    private Label fFontPreviewLabel;
    
    public Composite createContents(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout(2, false));
        
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
        
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(fTableViewer.getControl());
        
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
            private float tableHeight = JFaceResources.getDefaultFont().getFontData()[0].height;
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
                fd.height = tableHeight;
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
        fEditFontButton.setText(Messages.FontsPreferenceTab_2);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fEditFontButton);
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
        fDefaultFontButton.setText(Messages.FontsPreferenceTab_7);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fDefaultFontButton);
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
        group.setText(Messages.FontsPreferenceTab_11);
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
        group.setText(Messages.FontsPreferenceTab_8);
        group.setLayout(new GridLayout());
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(2, 1).applyTo(group);
        
        fFontPreviewLabel = new Label(group, SWT.NONE);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fFontPreviewLabel);
    }
    
    private void updatePreview(FontInfo fontInfo) {
        FontData fd = fontInfo.getFontData();
        
        fFontPreviewLabel.setText(fd.getName() + " " + //$NON-NLS-1$
                fd.getHeight() + " " + //$NON-NLS-1$
                ((fd.getStyle() & SWT.BOLD) == SWT.BOLD ? Messages.FontsPreferenceTab_5 : "") + " " +  //$NON-NLS-1$//$NON-NLS-2$
                ((fd.getStyle() & SWT.ITALIC) == SWT.ITALIC ? Messages.FontsPreferenceTab_6 : ""));  //$NON-NLS-1$
        
        Font font = new Font(null, fd);
        fFontPreviewLabel.setFont(font);
        disposeLabelFont();
        fFontPreviewLabel.setData(font);
        
        GC gc = new GC(new Shell());
        gc.setFont(font);
        ((GridData)fFontPreviewLabel.getLayoutData()).heightHint = gc.getFontMetrics().getHeight();
        gc.dispose();
        fFontPreviewLabel.getParent().getParent().layout();
    }

    /**
     * Add Font options for controls
     */
    private void addFontOptions() {
        // View object default font gets its font info in a convoluted way from FontFactory...
        fontInfos.add(new FontInfo(Messages.FontsPreferenceTab_1, Messages.FontsPreferenceTab_12, FontFactory.getDefaultUserViewFontData()) {
            @Override
            void performOK() {
                FontFactory.setDefaultUserViewFont(getFontData());
            }
            
            @Override
            FontData getDefaultFontData() {
                return FontFactory.getDefaultViewOSFontData();
            }
        });

        // Single line text control font
        fontInfos.add(new FontInfoWithPreferences(Messages.FontsPreferenceTab_10, Messages.FontsPreferenceTab_13, SINGLE_LINE_TEXT_FONT));
        
        // Multiline text control font
        fontInfos.add(new FontInfoWithPreferences(Messages.FontsPreferenceTab_4, Messages.FontsPreferenceTab_14, MULTI_LINE_TEXT_FONT));
        
        // Model Tree font
        fontInfos.add(new FontInfoWithPreferences(Messages.FontsPreferenceTab_0, Messages.FontsPreferenceTab_15, MODEL_TREE_FONT));
        
        // Navigator Tree font
        fontInfos.add(new FontInfoWithPreferences(Messages.FontsPreferenceTab_9, Messages.FontsPreferenceTab_16, NAVIGATOR_TREE_FONT));

        // Properties Table font
        fontInfos.add(new FontInfoWithPreferences(Messages.FontsPreferenceTab_17, Messages.FontsPreferenceTab_18, PROPERTIES_TABLE_FONT));

        // Analysis Table font
        fontInfos.add(new FontInfoWithPreferences(Messages.FontsPreferenceTab_19, Messages.FontsPreferenceTab_20, ANALYSIS_TABLE_FONT));
    }
    
    private FontData openFontDialog(FontInfo fontInfo) {
        FontDialog dialog = new FontDialog(fTableViewer.getControl().getShell());
        dialog.setText(Messages.FontsPreferenceTab_3);
        dialog.setFontList(new FontData[] { fontInfo.getFontData() });
        return dialog.open();
    }
    
    public void performDefaults() {
        for(FontInfo info : fontInfos) {
            info.performDefault();
        }
        
        fTableViewer.refresh();
        
        FontInfo fontInfo = (FontInfo)fTableViewer.getStructuredSelection().getFirstElement();
        if(fontInfo != null) {
            updatePreview((FontInfo)fTableViewer.getStructuredSelection().getFirstElement());
        }
    }
    
    public void performOK() {
        for(FontInfo info : fontInfos) {
            info.performOK();
        }
    }

    private void disposeLabelFont() {
        Font labelFont = (Font)fFontPreviewLabel.getData();
        if(labelFont != null) {
            labelFont.dispose();
        }
    }
}
