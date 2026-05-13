/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.graphics.GC;
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
import com.archimatetool.model.util.RelationshipsMatrix.TargetMatrix;



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
        GridDataFactory.create(GridData.FILL_BOTH).applyTo(viewer.getControl());
        
        Grid grid = viewer.getGrid();
        
        grid.setHeaderVisible(true);
        //grid.setRowHeaderVisible(true); // Don't set this here!
        grid.setRowsResizeable(true);
        grid.setCellSelectionEnabled(true);
        
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
        
        // Measure min column width
        GC gc = new GC(grid);
        int columnWidth = gc.textExtent("acfginorstv").x + 8; //$NON-NLS-1$
        gc.dispose();
        
        // Add columns
        for(EClass eClass : getData()) {
            GridColumn column = new GridColumn(grid, SWT.NONE);
            column.setWidth(columnWidth);
            
            // Colum header
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
        
        // Set row header visible *after* setting columns so that the correct width is displayed
        grid.setRowHeaderVisible(true, columnWidth);
        
        viewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return getData().toArray();
            }
        });
        
        // Column label provider
        viewer.setLabelProvider(new MatrixLabelProvider());
        
        viewer.setInput(getData());
        
        // Relationships letter keys
        StringBuilder sb = new StringBuilder();
        for(Entry<EClass, Character> entry : RelationshipsMatrix.INSTANCE.getRelationshipsValueMap().entrySet()) {
            sb.append(entry.getValue());
            sb.append(": "); //$NON-NLS-1$
            sb.append(ArchiLabelProvider.INSTANCE.getDefaultName(entry.getKey()));
            sb.append('\n');
        }
        
        Label label = new Label(client, SWT.NULL);
        label.setText(sb.toString());
        label.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, true));
        
        return composite;
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
        Map<EClass, List<TargetMatrix>> matrixMap = RelationshipsMatrix.INSTANCE.getRelationshipsMatrix();
        Map<EClass, Character> valueMap = RelationshipsMatrix.INSTANCE.getRelationshipsValueMap();
        
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            EClass eClassRow = (EClass)element;
            EClass eClassColumn = getData().get(columnIndex);
            StringBuilder sb = new StringBuilder();
            
            matrixMap.getOrDefault(eClassRow, List.of())
                                              .stream()
                                              .filter(targetMatrix -> targetMatrix.getTargetClass() == eClassColumn)
                                              .findFirst()
                                              .ifPresent(targetMatrix -> targetMatrix.getRelationships().stream()
                                                      .map(valueMap::get)
                                                      .forEach(sb::append));
            
            return sb.toString();
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
