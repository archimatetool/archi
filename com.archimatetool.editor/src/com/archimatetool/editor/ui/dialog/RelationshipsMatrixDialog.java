/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.RelationshipsMatrix;



/**
 * Relationships matrix Dialog
 * 
 * @author Phillip Beauvoir
 */
public class RelationshipsMatrixDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.RelationshipsMatrixDialog"; //$NON-NLS-1$
    
    private List<EClass> allClasses;
    
    public RelationshipsMatrixDialog(Shell parentShell) {
        super(parentShell, "RelationshipsMatrixDialog"); //$NON-NLS-1$
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.RelationshipsMatrixDialog_0);
    }
    
    private Grid grid;
    
    private boolean modKeyPressed;
    private boolean shiftKeyPressed;
    private int lastColumnSelected;

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.RelationshipsMatrixDialog_0);
        setMessage(Messages.RelationshipsMatrixDialog_1);
        
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        client.setLayout(new GridLayout(2, false));
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(client);
        
        GridTableViewer viewer = new GridTableViewer(client);
        grid = viewer.getGrid();
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(grid);
        
        grid.setHeaderVisible(true);
        grid.setRowHeaderVisible(true);
        grid.setRowsResizeable(true);
        grid.setCellSelectionEnabled(true);
        
        // Relationships letter keys
        StringBuilder sb = new StringBuilder();
        for(Entry<Character, EClass> entry : RelationshipsMatrix.INSTANCE.getRelationsKeyMap().entrySet()) {
            sb.append(entry.getKey());
            sb.append(": "); //$NON-NLS-1$
            sb.append(ArchiLabelProvider.INSTANCE.getDefaultName(entry.getValue()));
            sb.append('\n');
        }
        
        Label label = new Label(client, SWT.NULL);
        label.setText(sb.toString());
        label.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, true));
        
        // Row header label provider
        viewer.setRowHeaderLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                if(cell.getElement() == IArchimatePackage.eINSTANCE.getArchimateRelationship()) {
                    cell.setText(Messages.RelationshipsMatrixDialog_3);
                    cell.setImage(ArchiLabelProvider.INSTANCE.getImage(IArchimatePackage.eINSTANCE.getAssociationRelationship()));
                }
                else {
                    cell.setText(ArchiLabelProvider.INSTANCE.getDefaultName((EClass)cell.getElement()));
                    cell.setImage(ArchiLabelProvider.INSTANCE.getImage(cell.getElement()));
                }
            }
        });
        
        // Listen to key presses
        grid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.keyCode == SWT.MOD1) {
                    modKeyPressed = true;
                }
                if(e.keyCode == SWT.SHIFT) {
                    shiftKeyPressed = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.keyCode == SWT.MOD1) {
                    modKeyPressed = false;
                }
                if(e.keyCode == SWT.SHIFT) {
                    shiftKeyPressed = false;
                }
            }
        });
        
        // Add columns
        for(EClass eClass : getData()) {
            GridColumn column = new GridColumn(grid, SWT.LEFT);
            
            // Column header
            if(eClass == IArchimatePackage.eINSTANCE.getArchimateRelationship()) {
                column.setImage(ArchiLabelProvider.INSTANCE.getImage(IArchimatePackage.eINSTANCE.getAssociationRelationship()));
                column.setHeaderTooltip(Messages.RelationshipsMatrixDialog_3);
            }
            else {
                column.setImage(ArchiLabelProvider.INSTANCE.getImage(eClass));
                column.setHeaderTooltip(ArchiLabelProvider.INSTANCE.getDefaultName(eClass));
            }
            
            // Select column when clicking on header
            column.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
                int c = column.getCellRenderer().getColumn();
                
                if(shiftKeyPressed && lastColumnSelected >= 0) {
                    int start = Math.min(lastColumnSelected, c);
                    int end = Math.max(lastColumnSelected, c);
                    for(int i = start; i <= end; i++) {
                        grid.selectColumn(i);
                    }
                }
                else if(modKeyPressed) {
                    grid.selectColumn(c);
                } 
                else {
                    grid.deselectAll();
                    grid.selectColumn(c);
                }
                
                lastColumnSelected = c;
            }));
        }
        
        // Column label provider
        viewer.setLabelProvider(new MatrixLabelProvider());
        
        // Content Provider
        viewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return getData().toArray();
            }
        });
        
        viewer.setInput(getData());
        
        return composite;
    }
    
    @Override
    public void create() {
        super.create();
        
        // Pack columns here not in createDialogArea
        for(GridColumn column : grid.getColumns()) {
            column.pack();
        }
    }
    
    private List<EClass> getData() {
        if(allClasses == null) {
            allClasses = new ArrayList<>();
            allClasses.addAll(Arrays.asList(ArchimateModelUtils.getAllArchimateClasses()));
            allClasses.addAll(Arrays.asList(ArchimateModelUtils.getConnectorClasses()));
            allClasses.add(IArchimatePackage.eINSTANCE.getArchimateRelationship());
        }
        
        return allClasses;
    }
    
    private class MatrixLabelProvider extends BaseLabelProvider implements ITableLabelProvider {
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            return RelationshipsMatrix.INSTANCE.getRelationKeys((EClass)element, getData().get(columnIndex));
        }
    }
    
    @Override
    protected Point getDefaultDialogSize() {
        return new Point(1000, 700);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK button
        createButton(parent, IDialogConstants.OK_ID, Messages.RelationshipsMatrixDialog_2, true);
    }
}
