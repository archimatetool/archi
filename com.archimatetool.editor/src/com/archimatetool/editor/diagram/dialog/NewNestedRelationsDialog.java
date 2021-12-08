/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * New Nested Relations Dialog
 * 
 * @author Phillip Beauvoir
 */
public class NewNestedRelationsDialog extends ExtendedTitleAreaDialog implements Listener {
    
    private String HELP_ID = "com.archimatetool.help.NewNestedRelationsDialog"; //$NON-NLS-1$

    private TableViewer fTableViewer;
    
    private IDiagramModelArchimateObject fParentObject;
    private Mapping[] fMappings;

    // Keep track of Ctrl key
    private boolean fModKeyPressed;
    
    private class Mapping {
        private List<NestedConnectionInfo> validRelations;
        private String[] names;
        private int selectedIndex;
        
        Mapping(IDiagramModelArchimateObject childObject) {
            validRelations = createValidRelations(fParentObject, childObject);
            selectedIndex = 1;
        }
        
        String getSelectedRelationName() {
            return getValidRelationNames()[selectedIndex];
        }
        
        void setSelectedIndex(int value) {
            selectedIndex = value;
        }
        
        int getSelectedIndex() {
            return selectedIndex;
        }
        
        NestedConnectionInfo getSelected() {
            return validRelations.get(selectedIndex);
        }
        
        void setSelected(NestedConnectionInfo selected) {
            for(NestedConnectionInfo info : validRelations) {
                if((info.getRelationshipType() == selected.getRelationshipType()) && (info.getSourceObject() == selected.getSourceObject() || 
                        info.getTargetObject() == selected.getTargetObject())) {
                    int index = validRelations.indexOf(info);
                    selectedIndex = (index == -1) ? 0 : index;
                    break;
                }
            }
        }
        
        String[] getValidRelationNames() {
            if(names == null) {
                names = new String[validRelations.size()];
                
                names[0] = Messages.NewNestedRelationsDialog_0; // none
                
                for(int i = 1; i < validRelations.size(); i++) {
                    NestedConnectionInfo info = validRelations.get(i);
                    
                    String relationshipName = ArchiLabelProvider.INSTANCE.getDefaultName(info.getRelationshipType());
                    String reverse = info.isReverse() ? Messages.NewNestedRelationsDialog_6 : ""; //$NON-NLS-1$
                    String sentence = ArchiLabelProvider.INSTANCE.getRelationshipSentence(info.getRelationshipType(), info.getSourceObject().getArchimateConcept(),
                            info.getTargetObject().getArchimateConcept());
                    
                    names[i] = NLS.bind(Messages.NewNestedRelationsDialog_7, new Object[] { relationshipName, reverse, sentence });
                }
            }
            
            return names;
        }
        
        private List<NestedConnectionInfo> createValidRelations(IDiagramModelArchimateObject sourceObject, IDiagramModelArchimateObject targetObject) {
            List<NestedConnectionInfo> list = new ArrayList<NestedConnectionInfo>();
            
            // Entry for "none"
            list.add(new NestedConnectionInfo(sourceObject, targetObject, false, null));
            
            // Normal direction
            for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
                if(ArchimateModelUtils.isValidRelationship(sourceObject.getArchimateElement(), targetObject.getArchimateElement(), eClass)) {
                    list.add(new NestedConnectionInfo(sourceObject, targetObject, false, eClass)); 
                }
            }
            
            // Reverse direction
            for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewReverseRelations()) {
                // Reverse direction
                if(ArchimateModelUtils.isValidRelationship(targetObject.getArchimateElement(), sourceObject.getArchimateElement(), eClass)) {
                    list.add(new NestedConnectionInfo(targetObject, sourceObject, true, eClass)); 
                }
            }
            
            return list;
        }
    }

    public NewNestedRelationsDialog(IDiagramModelArchimateObject parentObject, List<IDiagramModelArchimateObject> childObjects) {
        super(Display.getCurrent().getActiveShell(), "NewNestedRelationsDialog"); //$NON-NLS-1$
        
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fParentObject = parentObject;
        
        fMappings = new Mapping[childObjects.size()];
        for(int i = 0; i < fMappings.length; i++) {
            fMappings[i] = new Mapping(childObjects.get(i));
        }
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.NewNestedRelationsDialog_1);
        
        shell.getDisplay().addFilter(SWT.KeyDown, this);
        shell.getDisplay().addFilter(SWT.KeyUp, this);
    }

    @Override
    public void handleEvent(Event event) {
        // Ctrl key pressed/released
        switch(event.type) {
            case SWT.KeyDown:
                if(event.keyCode == SWT.MOD1) {
                    fModKeyPressed = true;
                }
                break;
            case SWT.KeyUp:
                if(event.keyCode == SWT.MOD1) {
                    fModKeyPressed = false;
                }
                break;
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.NewNestedRelationsDialog_2);
        String message = NLS.bind(Messages.NewNestedRelationsDialog_3, fParentObject.getName());
        setMessage(message);
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(1, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Composite tableComp = new Composite(client, SWT.BORDER);
        tableComp.setLayout(new TableColumnLayout());
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        fTableViewer = new RelationsTableViewer(tableComp, SWT.NONE);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        fTableViewer.setInput(""); // anything will do //$NON-NLS-1$
        
        return composite;
    }
    
    public List<NestedConnectionInfo> getSelected() {
        List<NestedConnectionInfo> list = new ArrayList<NestedConnectionInfo>();
        
        for(Mapping mapping : fMappings) {
            if(mapping.getSelected().getRelationshipType() != null) {
                list.add(mapping.getSelected());
            }
        }
        
        return list;
    }
    
    @Override
    public boolean close() {
        getShell().getDisplay().removeFilter(SWT.KeyDown, this);
        getShell().getDisplay().removeFilter(SWT.KeyUp, this);
        
        // BUG on Mac: If a combo selection is not "completed" (return press or lost focus) the value is not set
        fTableViewer.applyEditorValue();
        
        return super.close();
    }
    
    @Override
    protected Point getDefaultDialogSize() {
        return new Point(600, 400);
    }
    
    private class RelationsTableViewer extends TableViewer {
        private String[] columnNames = {
            Messages.NewNestedRelationsDialog_4,
            Messages.NewNestedRelationsDialog_5
        };
        
        RelationsTableViewer(Composite parent, int style) {
            super(parent, SWT.FULL_SELECTION | style);
            
            // Mac Silicon Item height
            UIUtils.fixMacSiliconItemHeight(getTable());

            setColumns(getTable());
            setContentProvider(new RelationsTableViewerContentProvider());
            setLabelProvider(new RelationsTableViewerLabelCellProvider());
        }
        
        /**
         * Set up the columns
         */
        private void setColumns(Table table) {
            table.setHeaderVisible(true);
            
            // Use layout from parent container
            TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
            
            TableColumn c1 = new TableColumn(table, SWT.NONE, 0);
            c1.setText(columnNames[0]);
            layout.setColumnData(c1, new ColumnWeightData(50, true));
            
            TableViewerColumn c2 = new TableViewerColumn(this, SWT.NONE);
            c2.getColumn().setText(columnNames[1]);
            layout.setColumnData(c2.getColumn(), new ColumnWeightData(50, true));
            c2.setEditingSupport(new TableColumnEditingSupport(this));
            
            // Column names are properties, needed for editing
            setColumnProperties(columnNames);
        }
        
        class RelationsTableViewerContentProvider implements IStructuredContentProvider {
            @Override
            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object parent) {
                return fMappings;
            }
        }

        class RelationsTableViewerLabelCellProvider extends LabelProvider implements ITableLabelProvider {
            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                if(columnIndex == 0) {
                    return ArchiLabelProvider.INSTANCE.getImage(((Mapping)element).getSelected().getTargetObject());
                }
                return ArchiLabelProvider.INSTANCE.getImage(((Mapping)element).getSelected().getRelationshipType());
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if(columnIndex == 0) {
                    return ((Mapping)element).getSelected().getTargetObject().getName();
                }
                return ((Mapping)element).getSelectedRelationName();
            }
        }
        
        class TableColumnEditingSupport extends EditingSupport {
            ComboBoxCellEditor cellEditor;
            
            public TableColumnEditingSupport(ColumnViewer viewer) {
                super(viewer);
                cellEditor = new ComboBoxCellEditor(getTable(), new String[0], SWT.READ_ONLY);
                cellEditor.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
                ((CCombo)cellEditor.getControl()).setVisibleItemCount(11);
            }

            @Override
            protected CellEditor getCellEditor(Object element) {
                cellEditor.setItems(((Mapping)element).getValidRelationNames());
                return cellEditor;
            }

            @Override
            protected boolean canEdit(Object element) {
                return true;
            }

            @Override
            protected Object getValue(Object element) {
                return ((Mapping)element).getSelectedIndex();
            }

            @Override
            protected void setValue(Object element, Object value) {
                // Check for -1 value. On Mac this happens if the Mod key is down when selecting the same item from the conbo box
                Integer index = (Integer)value;
                if(index == -1) {
                    return;
                }
                
                Mapping mapping = (Mapping)element;
                
                mapping.setSelectedIndex(index);
                getViewer().update(mapping, null);
                
                // Ctrl key pressed, set others to same selection if possible or (none) if not
                if(fModKeyPressed) {
                    NestedConnectionInfo selectedInfo = mapping.getSelected();
                    
                    for(Mapping m : fMappings) {
                        if(m != mapping) {
                            m.setSelected(selectedInfo);
                            getViewer().update(m, null);
                        }
                    }
                }
            }
        }

    }
}
