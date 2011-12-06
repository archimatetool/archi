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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
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
 * New Nested Relation Dialog
 * 
 * @author Phillip Beauvoir
 */
public class NewNestedRelationDialog extends ExtendedTitleAreaDialog {
    
    public static String HELPID = "uk.ac.bolton.archimate.help.NewNestedRelationDialog"; //$NON-NLS-1$
    
    private TableViewer fTableViewer;
    
    private IArchimateElement fParentElement, fChildElement;
    
    private EClass[] fValidRelations;
    private EClass fSelected;

    public NewNestedRelationDialog(Shell parentShell, IArchimateElement parentElement, IArchimateElement childElement) {
        super(parentShell, "NewNestedRelationDialog");
        setTitleImage(IArchimateImages.ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fParentElement = parentElement;
        fChildElement = childElement;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("New Relationship");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);

        setTitle("Nested Elements Relationship");
        setMessage("Select a relationship type if you wish to create a new relationship between '" +
                fParentElement.getName() + "' and '" + fChildElement.getName() + "'.");
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
        
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                okPressed();
            }
        });
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                fSelected = (EClass)((IStructuredSelection)fTableViewer.getSelection()).getFirstElement();
            }
        });
        
        if(fValidRelations != null && fValidRelations.length > 0) {
            fTableViewer.setSelection(new StructuredSelection(fValidRelations[0]));
        }
        
        return composite;
    }
    
    @Override
    protected Point getDefaultDialogSize() {
        return new Point(500, 350);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                "None", false);
    }
    
    public EClass getSelectedType() {
        return fSelected;
    }
    
    private EClass[] getValidRelationships(IArchimateElement sourceElement, IArchimateElement targetElement) {
        List<EClass> list = new ArrayList<EClass>();
        
        for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
            if(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, eClass)) {
                list.add(eClass); 
            }
        }
        
        return list.toArray(new EClass[list.size()]);
    }
    
    class RelationsTableViewer extends TableViewer {
        RelationsTableViewer(Composite parent, int style) {
            super(parent, SWT.FULL_SELECTION | style);
            setColumns();
            setContentProvider(new RelationsTableViewerContentProvider());
            setLabelProvider(new RelationsTableViewerLabelCellProvider());
        }
        
        /**
         * Set up the columns
         */
        private void setColumns() {
            Table table = getTable();
            table.setHeaderVisible(false);
            
            // Use layout from parent container
            TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
            TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
            layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        }

        class RelationsTableViewerContentProvider implements IStructuredContentProvider {
            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }
            
            public void dispose() {
            }
            
            public Object[] getElements(Object parent) {
                fValidRelations = getValidRelationships(fParentElement, fChildElement);
                return fValidRelations;
            }
        }

        class RelationsTableViewerLabelCellProvider extends LabelProvider {
            @Override
            public String getText(Object element) {
                return ArchimateNames.getDefaultName((EClass)element);
            }
            
            @Override
            public Image getImage(Object element) {
                return ArchimateLabelProvider.INSTANCE.getImage(element);
            }
         }
    }
}
