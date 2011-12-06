/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.dialog;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.ui.components.ExtendedTitleAreaDialog;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * New Nested Relations Dialog
 * 
 * @author Phillip Beauvoir
 */
public class NewNestedRelationsDialog extends ExtendedTitleAreaDialog implements Listener {
    
    public static String HELPID = "uk.ac.bolton.archimate.help.NewNestedRelationsDialog"; //$NON-NLS-1$

    private TableViewer fTableViewer;
    
    private IArchimateElement fParentElement;
    private Mapping[] fMappings;

    // Keep track of Ctrl key
    private boolean fModKeyPressed;
    
    private class Mapping {
        private IArchimateElement element;
        private List<EClass> validRelations;
        private String[] names;
        private int selectedIndex;
        
        Mapping(IArchimateElement element) {
            this.element = element;
            validRelations = createValidRelations(fParentElement, element);
            selectedIndex = 1;
        }
        
        IArchimateElement getElement() {
            return element;
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
        
        EClass getSelectedType() {
            return validRelations.get(selectedIndex);
        }
        
        void setSelectedType(EClass type) {
            int index = validRelations.indexOf(type);
            selectedIndex = (index == -1) ? 0 : index;
        }
        
        String[] getValidRelationNames() {
            if(names == null) {
                names = new String[validRelations.size()];
                names[0] = "(none)";
                for(int i = 1; i < validRelations.size(); i++) {
                    names[i] = ArchimateNames.getDefaultName(validRelations.get(i));
                }
            }
            return names;
        }
        
        private List<EClass> createValidRelations(IArchimateElement sourceElement, IArchimateElement targetElement) {
            List<EClass> list = new ArrayList<EClass>();
            // Entry for "none"
            list.add(null);
            for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
                if(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, eClass)) {
                    list.add(eClass); 
                }
            }
            return list;
        }
    }

    public NewNestedRelationsDialog(Shell parentShell, IArchimateElement parentElement, List<IArchimateElement> childElements) {
        super(parentShell, "NewNestedRelationsDialog");
        setTitleImage(IArchimateImages.ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fParentElement = parentElement;
        
        fMappings = new Mapping[childElements.size()];
        for(int i = 0; i < fMappings.length; i++) {
            fMappings[i] = new Mapping(childElements.get(i));
        }
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("New Relationships");
        
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
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);

        setTitle("Nested Elements Relationship");
        setMessage("Select a relationship type if you wish to create new relationships between '" +
                fParentElement.getName() + "' and the following elements.");
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
        fTableViewer.setInput(""); // anything will do
        
        return composite;
    }
    
    public EClass[] getSelectedTypes() {
        List<EClass> list = new ArrayList<EClass>();
        
        for(Mapping mapping : fMappings) {
            if(mapping.getSelectedType() != null) {
                list.add(mapping.getSelectedType());
            }
        }
        
        return list.isEmpty() ? null : list.toArray(new EClass[list.size()]);
    }
    
    public IArchimateElement[] getSelectedElements() {
        List<IArchimateElement> list = new ArrayList<IArchimateElement>();
        
        for(Mapping mapping : fMappings) {
            if(mapping.getSelectedType() != null) {
                list.add(mapping.getElement());
            }
        }
        
        return list.isEmpty() ? null : list.toArray(new IArchimateElement[list.size()]);
    }
    
    @Override
    public boolean close() {
        getShell().getDisplay().removeFilter(SWT.KeyDown, this);
        getShell().getDisplay().removeFilter(SWT.KeyUp, this);
        
        return super.close();
    }
    
    @Override
    protected Point getDefaultDialogSize() {
        return new Point(600, 400);
    }
    
    private class RelationsTableViewer extends TableViewer {
        private String[] columnNames = {
            "Child Element",
            "Relationship"
        };
        
        RelationsTableViewer(Composite parent, int style) {
            super(parent, SWT.FULL_SELECTION | style);
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
            layout.setColumnData(c1, new ColumnWeightData(60, true));
            
            TableViewerColumn c2 = new TableViewerColumn(this, SWT.NONE);
            c2.getColumn().setText(columnNames[1]);
            layout.setColumnData(c2.getColumn(), new ColumnWeightData(40, true));
            c2.setEditingSupport(new TableColumnEditingSupport(this));
            
            // Column names are properties, needed for editing
            setColumnProperties(columnNames);
        }
        
        class RelationsTableViewerContentProvider implements IStructuredContentProvider {
            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }
            
            public void dispose() {
            }
            
            public Object[] getElements(Object parent) {
                return fMappings;
            }
        }

        class RelationsTableViewerLabelCellProvider extends LabelProvider implements ITableLabelProvider {
            public Image getColumnImage(Object element, int columnIndex) {
                if(columnIndex == 0) {
                    return ArchimateLabelProvider.INSTANCE.getImage(((Mapping)element).getElement());
                }
                return ArchimateLabelProvider.INSTANCE.getImage(((Mapping)element).getSelectedType());
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if(columnIndex == 0) {
                    return ((Mapping)element).getElement().getName();
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
                ((Mapping)element).setSelectedIndex((Integer)value);
                getViewer().update(element, null);
                
                // Ctrl key pressed, set others to same if possible or (none) if not
                if(fModKeyPressed) {
                    EClass selectedClass = ((Mapping)element).getSelectedType();
                    for(Mapping mapping : fMappings) {
                        if(mapping != element) {
                            mapping.setSelectedType(selectedClass);
                            getViewer().update(mapping, null);
                        }
                    }
                }
            }
        }

    }
}
