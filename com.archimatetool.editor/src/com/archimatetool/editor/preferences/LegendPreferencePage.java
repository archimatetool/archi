/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.ILegendOptions;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Legend Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class LegendPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    public static final String ID = "com.archimatetool.editor.prefsLegend"; //$NON-NLS-1$
    public static final String HELPID = "com.archimatetool.help.prefsDiagram"; //$NON-NLS-1$
    
    private Combo comboSortMethod;
    private Combo comboColorScheme;
    private Spinner rowsSpinner;
    
    private TableViewer tableViewer;
    
    private static final String[] comboSortOptions = {
            Messages.LegendPreferencePage_9,
            Messages.LegendPreferencePage_10
    };

    private static final String[] comboColorOptions = {
            Messages.LegendPreferencePage_5,
            Messages.LegendPreferencePage_6,
            Messages.LegendPreferencePage_7
    };
    
    // Cache
    private Map<EClass, String> cache = new HashMap<>();
    
	public LegendPreferencePage() {
		setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);
        
        Composite client = new Composite(parent, SWT.NULL);
        GridLayoutFactory.fillDefaults().applyTo(client);
        
        Group defaultsGroup = new Group(client, SWT.NULL);
        defaultsGroup.setText(Messages.LegendPreferencePage_3);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(defaultsGroup);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(defaultsGroup);
        
        Label label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.LegendPreferencePage_11);
        
        comboSortMethod = new Combo(defaultsGroup, SWT.READ_ONLY | SWT.BORDER);
        comboSortMethod.setItems(comboSortOptions);

        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.LegendPreferencePage_8);
        
        comboColorScheme = new Combo(defaultsGroup, SWT.READ_ONLY | SWT.BORDER);
        comboColorScheme.setItems(comboColorOptions);
        
        label = new Label(defaultsGroup, SWT.NULL);
        label.setText(Messages.LegendPreferencePage_4);
        
        rowsSpinner = new Spinner(defaultsGroup, SWT.BORDER);
        rowsSpinner.setMinimum(ILegendOptions.ROWS_PER_COLUMN_MIN);
        rowsSpinner.setMaximum(ILegendOptions.ROWS_PER_COLUMN_MAX);
        
        label = new Label(client, SWT.NULL);
        label.setText(Messages.LegendPreferencePage_0);
        
        // Table
        Composite tableComp = new Composite(client, SWT.BORDER);
        TableColumnLayout tableLayout = new TableColumnLayout();
        tableComp.setLayout(tableLayout);
        GridDataFactory.create(GridData.FILL_BOTH).hint(SWT.DEFAULT, 200).applyTo(tableComp);
        
        tableViewer = new TableViewer(tableComp, SWT.MULTI | SWT.FULL_SELECTION);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setLinesVisible(true);
        
        // Edit cell on double-click and add Tab key traversal
        TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer) {
            @Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                return super.isEditorActivationEvent(event) ||
                        (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION);
            }
            
        }, ColumnViewerEditor.TABBING_HORIZONTAL | 
                ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | 
                ColumnViewerEditor.TABBING_VERTICAL |
                ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
                ColumnViewerEditor.KEYBOARD_ACTIVATION);

        // Columns
        TableViewerColumn columnDefaultLabel = new TableViewerColumn(tableViewer, SWT.NONE, 0);
        columnDefaultLabel.getColumn().setText(Messages.LegendPreferencePage_1);
        tableLayout.setColumnData(columnDefaultLabel.getColumn(), new ColumnWeightData(40, true));

        TableViewerColumn columnUserLabel = new TableViewerColumn(tableViewer, SWT.NONE, 1);
        columnUserLabel.getColumn().setText(Messages.LegendPreferencePage_2);
        tableLayout.setColumnData(columnUserLabel.getColumn(), new ColumnWeightData(60, true));
        columnUserLabel.setEditingSupport(new LabelEditingSupport(tableViewer));
        
        tableViewer.setContentProvider(new IStructuredContentProvider() {
            Collator collator = Collator.getInstance();
            List<EClass> objects;
            {
                // Elements
                objects = new ArrayList<>(Arrays.asList(ArchimateModelUtils.getAllArchimateClasses()));
                objects.addAll(Arrays.asList(ArchimateModelUtils.getConnectorClasses()));
                sort(objects);
                
                // Relations
                objects.addAll(sort(Arrays.asList(ArchimateModelUtils.getRelationsClasses())));
                
                // Add prefs values to cache
                for(EClass eClass : objects) {
                    cache.put(eClass, getPreferenceStore().getString(getPrefsKey(eClass)));
                }
            }
            
            @Override
            public Object[] getElements(Object inputElement) {
                return objects.toArray();
            }
            
            private List<EClass> sort(List<EClass> list) {
                list.sort((c1, c2) -> {
                    return collator.compare(ArchiLabelProvider.INSTANCE.getDefaultName(c1), ArchiLabelProvider.INSTANCE.getDefaultName(c2));
                });
                return list;
            }
        });
        
        tableViewer.setLabelProvider(new LabelCellProvider());
        
        comboSortMethod.select(getPreferenceStore().getInt(LEGEND_SORT_DEFAULT));
        comboColorScheme.select(getPreferenceStore().getInt(LEGEND_COLORS_DEFAULT));
        rowsSpinner.setSelection(getPreferenceStore().getInt(LEGEND_ROWS_PER_COLUMN_DEFAULT));
        
        // Set table input
        getShell().getDisplay().asyncExec(() -> { // avoid table misplacement
            tableViewer.setInput(this);
            tableViewer.getControl().getParent().layout();
        });
        
        return client;
    }
    
    private String getPrefsKey(EClass eClass) {
        return IPreferenceConstants.LEGEND_LABEL_PREFIX + eClass.getName();
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(LEGEND_SORT_DEFAULT, comboSortMethod.getSelectionIndex());
        getPreferenceStore().setValue(LEGEND_COLORS_DEFAULT, comboColorScheme.getSelectionIndex());
        getPreferenceStore().setValue(LEGEND_ROWS_PER_COLUMN_DEFAULT, rowsSpinner.getSelection());
        
        tableViewer.applyEditorValue();
        
        for(Entry<EClass, String> entry : cache.entrySet()) {
            if(StringUtils.isSet(entry.getValue())) {
                getPreferenceStore().setValue(getPrefsKey(entry.getKey()), entry.getValue());
            }
            else {
                getPreferenceStore().setToDefault(getPrefsKey(entry.getKey()));
            }
        }
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        comboSortMethod.select(getPreferenceStore().getDefaultInt(LEGEND_SORT_DEFAULT));
        comboColorScheme.select(getPreferenceStore().getDefaultInt(LEGEND_COLORS_DEFAULT));
        rowsSpinner.setSelection(getPreferenceStore().getDefaultInt(LEGEND_ROWS_PER_COLUMN_DEFAULT));
        
        cache.replaceAll((key, value) -> ""); //$NON-NLS-1$
        tableViewer.refresh();
        
        super.performDefaults();
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
    
    /**
     * Label Provider
     */
    private class LabelCellProvider extends LabelProvider implements ITableLabelProvider {
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return columnIndex == 0 ? ArchiLabelProvider.INSTANCE.getImage(element) : null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            EClass eClass = (EClass)element;
            if(columnIndex == 0) {
                return ArchiLabelProvider.INSTANCE.getDefaultName(eClass);
            }
            return cache.get(eClass);
        }
    }
    
    /**
     * Table Editor
     */
    private class LabelEditingSupport extends EditingSupport {
        TextCellEditor cellEditor;

        public LabelEditingSupport(ColumnViewer viewer) {
            super(viewer);
            cellEditor = new TextCellEditor((Composite)viewer.getControl());
            
            // Filter out illegal characters
            UIUtils.applyNewlineFilter(cellEditor.getControl());
            UIUtils.applyInvalidCharacterFilter(cellEditor.getControl());
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            return cache.get(element);
        }

        @Override
        protected void setValue(Object element, Object value) {
            EClass eClass = (EClass)element;
            String newValue = (String)value;
            
            cache.put(eClass, newValue.trim());
            tableViewer.update(eClass, null);
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        cache.clear();
        cache = null;
    }
}