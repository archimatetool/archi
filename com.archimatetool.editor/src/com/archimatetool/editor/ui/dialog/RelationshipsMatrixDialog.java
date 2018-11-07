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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.internal.CellSelection;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
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
    
    private List<EClass> fAllClasses;
    
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

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.RelationshipsMatrixDialog_0);
        setMessage(Messages.RelationshipsMatrixDialog_1);
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        GridData gd;
        
        GridTableViewer viewer = new GridTableViewer(client);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 800;
        gd.heightHint = 500;
        viewer.getControl().setLayoutData(gd);
        
        viewer.getGrid().setHeaderVisible(true);
        viewer.getGrid().setRowHeaderVisible(true);
        viewer.getGrid().setRowsResizeable(true);
        viewer.getGrid().setCellSelectionEnabled(true);
        
        //viewer.setColumnProperties(new String[] {"1", "2", "3"});
        
        viewer.setRowHeaderLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                cell.setText(ArchiLabelProvider.INSTANCE.getDefaultName((EClass)cell.getElement()));
                cell.setImage(ArchiLabelProvider.INSTANCE.getImage(cell.getElement()));
            }
        });
        
        for(EClass eClass : getData()) {
            GridColumn column = new GridColumn(viewer.getGrid(), SWT.NONE);
            column.setWidth(70);
            column.setImage(ArchiLabelProvider.INSTANCE.getImage(eClass));
            column.setHeaderTooltip(ArchiLabelProvider.INSTANCE.getDefaultName(eClass));
        }
        
        viewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return getData().toArray();
            }
        });
        
        viewer.setLabelProvider(new MyLabelProvider());
        
        //hookContextMenu(viewer);
        
        viewer.setInput(getData());
        
        String text = ""; //$NON-NLS-1$
        for(Entry<EClass, Character> entry : RelationshipsMatrix.INSTANCE.getRelationshipsValueMap().entrySet()) {
            text += entry.getValue() + ": " + ArchiLabelProvider.INSTANCE.getDefaultName(entry.getKey()) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        Label label = new Label(client, SWT.NULL);
        label.setText(text);
        label.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, true));
        
        return composite;
    }
    
    @SuppressWarnings("unused")
    private void hookContextMenu(final GridTableViewer viewer) {
        MenuManager menuMgr = new MenuManager("#GridPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                final CellSelection selection = ((CellSelection)viewer.getSelection());
                if(!selection.isEmpty()) {
                    manager.add(new Action("Restore to default") { //$NON-NLS-1$
                        @Override
                        public void run() {
                            for(Object element : selection.toArray()) {
                                EClass eClassRow = (EClass)element;
                                for(Object o : selection.getIndices(element)) {
                                    int columnIndex = (Integer)o;
                                    EClass eClassColumn = getData().get(columnIndex);
                                    restoreDefaultValue(eClassRow, eClassColumn);
                                }
                            }
                        };
                    });
                }
            }
        });
        
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }
    
    private void restoreDefaultValue(EClass sourceClass, EClass targetClass) {
        //Map<EClass, List<TargetMatrix>> matrixMap = RelationshipsMatrix.INSTANCE.getRelationshipsMatrix();
        
    }
    
    private List<EClass> getData() {
        if(fAllClasses == null) {
            fAllClasses = new ArrayList<EClass>();
            fAllClasses.addAll(Arrays.asList(ArchimateModelUtils.getAllArchimateClasses()));
            fAllClasses.add(IArchimatePackage.eINSTANCE.getJunction());
        }
        
        return fAllClasses;
    }
    
    private class MyLabelProvider extends LabelProvider implements ITableLabelProvider {

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            EClass eClassRow = (EClass)element;
            EClass eClassColumn = getData().get(columnIndex);
            
            String text = ""; //$NON-NLS-1$
            
            Map<EClass, List<TargetMatrix>> matrixMap = RelationshipsMatrix.INSTANCE.getRelationshipsMatrix();
            Map<EClass, Character> valueMap = RelationshipsMatrix.INSTANCE.getRelationshipsValueMap();
            
            List<TargetMatrix> list = matrixMap.get(eClassRow);
            if(list != null) {
                for(TargetMatrix targetMatrix : list) {
                    if(targetMatrix.getTargetClass() == eClassColumn) {
                        for(EClass relation : targetMatrix.getRelationships()) {
                            text += valueMap.get(relation);
                        }
                        break;
                    }
                }
            }
            
            return text;
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
