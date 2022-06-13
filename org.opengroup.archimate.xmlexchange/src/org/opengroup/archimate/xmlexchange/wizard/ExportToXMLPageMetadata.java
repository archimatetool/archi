/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange.wizard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.opengroup.archimate.xmlexchange.XMLExchangePlugin;

import com.archimatetool.editor.ui.IArchiImages;



/**
 * Export to XML Wizard Page for Metadata
 * 
 * @author Phillip Beauvoir
 */
public class ExportToXMLPageMetadata extends WizardPage {

    private static String HELP_ID = "com.archimatetool.help.ExportToXMLPageMetadata"; //$NON-NLS-1$
    
    private static final String PREFS_LAST_VALUE = "ExportXMLExchangeLastMD_"; //$NON-NLS-1$
    
    private static String[] dcNames = {
            "title", //$NON-NLS-1$
            "creator", //$NON-NLS-1$
            "subject", //$NON-NLS-1$
            "description", //$NON-NLS-1$
            "publisher", //$NON-NLS-1$
            "contributor", //$NON-NLS-1$
            "date", //$NON-NLS-1$
            "type", //$NON-NLS-1$
            "format", //$NON-NLS-1$
            "identifier", //$NON-NLS-1$
            "source", //$NON-NLS-1$
            "language", //$NON-NLS-1$
            "relation", //$NON-NLS-1$
            "coverage", //$NON-NLS-1$
            "rights" //$NON-NLS-1$
    };
    
    private static String[] dcTitles = {
            Messages.ExportToXMLPageMetadata_0,
            Messages.ExportToXMLPageMetadata_1,
            Messages.ExportToXMLPageMetadata_2,
            Messages.ExportToXMLPageMetadata_3,
            Messages.ExportToXMLPageMetadata_4,
            Messages.ExportToXMLPageMetadata_5,
            Messages.ExportToXMLPageMetadata_6,
            Messages.ExportToXMLPageMetadata_7,
            Messages.ExportToXMLPageMetadata_8,
            Messages.ExportToXMLPageMetadata_9,
            Messages.ExportToXMLPageMetadata_10,
            Messages.ExportToXMLPageMetadata_11,
            Messages.ExportToXMLPageMetadata_12,
            Messages.ExportToXMLPageMetadata_13,
            Messages.ExportToXMLPageMetadata_14
    };
    
    private TableViewer fTableViewer;
    private Button fClearAllButton;
    
    private Map<String, String> fNames;
    private Map<String, String> fData;
    
    public ExportToXMLPageMetadata() {
        super("ExportToXMLPageMetadata"); //$NON-NLS-1$
        
        setTitle(Messages.ExportToXMLPageMetadata_15);
        setDescription(Messages.ExportToXMLPageMetadata_16);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_EXPORT_DIR_WIZARD));
        
        fNames = new LinkedHashMap<String, String>();
        fData = new LinkedHashMap<String, String>();
        
        for(int i = 0; i < dcNames.length; i++) {
            fNames.put(dcNames[i], dcTitles[i]);
        }
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        Composite tableComp = new Composite(container, SWT.BORDER);
        TableColumnLayout tableLayout = new TableColumnLayout();
        tableComp.setLayout(tableLayout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 300; // stop overstretch
        tableComp.setLayoutData(gd);
        
        fTableViewer = new TableViewer(tableComp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        fTableViewer.getTable().setHeaderVisible(true);
        fTableViewer.getTable().setLinesVisible(true);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Edit cell on double-click and Tab key traversal
        TableViewerEditor.create(fTableViewer, new ColumnViewerEditorActivationStrategy(fTableViewer) {
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

        // Help ID on table
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELP_ID);

        // Content Provider
        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return fNames.keySet().toArray();
            }
        });

        // Columns
        TableViewerColumn columnName = new TableViewerColumn(fTableViewer, SWT.NONE, 0);
        columnName.getColumn().setText(Messages.ExportToXMLPageMetadata_17);
        tableLayout.setColumnData(columnName.getColumn(), new ColumnWeightData(20, true));

        TableViewerColumn columnValue = new TableViewerColumn(fTableViewer, SWT.NONE, 1);
        columnValue.getColumn().setText(Messages.ExportToXMLPageMetadata_18);
        tableLayout.setColumnData(columnValue.getColumn(), new ColumnWeightData(80, true));
        columnValue.setEditingSupport(new ValueEditingSupport(fTableViewer));
        
        fTableViewer.setLabelProvider(new LabelCellProvider());
        
        for(int i = 0; i < dcNames.length; i++) {
            IPreferenceStore store = XMLExchangePlugin.INSTANCE.getPreferenceStore();
            String lastValue = store.getString(PREFS_LAST_VALUE + dcNames[i]);
            fData.put(dcNames[i], lastValue);
        }
        
        fClearAllButton = new Button(container, SWT.PUSH);
        fClearAllButton.setText(Messages.ExportToXMLPageMetadata_19);
        fClearAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for(Entry<String, String> entry : fData.entrySet()) {
                    entry.setValue(""); //$NON-NLS-1$
                }
                fTableViewer.setInput(""); //$NON-NLS-1$
            }
        });

        fTableViewer.setInput(""); //$NON-NLS-1$
    }
    
    Map<String, String> getMetadata() {
        return fData;
    }
    
    void storePreferences() {
        IPreferenceStore store = XMLExchangePlugin.INSTANCE.getPreferenceStore();
        for(Entry<String, String> entry : fData.entrySet()) {
            store.setValue(PREFS_LAST_VALUE + entry.getKey(), entry.getValue());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    //
    // Table functions
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Label Provider
     */
    private class LabelCellProvider extends LabelProvider implements ITableLabelProvider {
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return fNames.get(element);

                case 1:
                    return fData.get(element);

                default:
                    return null;
            }
        }

    }

    /**
     * Key Editor
     */
    private class ValueEditingSupport extends EditingSupport {
        TextCellEditor cellEditor;

        public ValueEditingSupport(ColumnViewer viewer) {
            super(viewer);
            cellEditor = new TextCellEditor((Composite)viewer.getControl());
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
            return fData.get(element);
        }

        @Override
        protected void setValue(Object element, Object value) {
            fData.put((String)element, (String)value);
            fTableViewer.update(element, null);
        }
    }

}
