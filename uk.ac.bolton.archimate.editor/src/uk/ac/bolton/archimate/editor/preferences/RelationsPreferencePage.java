/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.preferences;

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
import org.eclipse.jface.preference.PreferencePage;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;
import uk.ac.bolton.archimate.model.util.RelationshipsMatrix;
import uk.ac.bolton.archimate.model.util.RelationshipsMatrix.TargetMatrix;

/**
 * Relations Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class RelationsPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    public static String HELPID = "uk.ac.bolton.archimate.help.prefsRelations"; //$NON-NLS-1$
    
    private List<EClass> fAllClasses;
    
	public RelationsPreferencePage() {
		setPreferenceStore(Preferences.STORE);
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);
        
        Label rubric = new Label(parent, SWT.NULL);
        rubric.setText(Messages.RelationsPreferencePage_0);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout(2, false));
        
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
                cell.setText(ArchimateLabelProvider.INSTANCE.getDefaultName((EClass)cell.getElement()));
                cell.setImage(ArchimateLabelProvider.INSTANCE.getImage(cell.getElement()));
            }
        });
        
        for(EClass eClass : getData()) {
            GridColumn column = new GridColumn(viewer.getGrid(), SWT.NONE);
            column.setWidth(60);
            column.setImage(ArchimateLabelProvider.INSTANCE.getImage(eClass));
            column.setHeaderTooltip(ArchimateLabelProvider.INSTANCE.getDefaultName(eClass));
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
            text += entry.getValue() + ": " + ArchimateLabelProvider.INSTANCE.getDefaultShortName(entry.getKey()) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        Label label = new Label(client, SWT.NULL);
        label.setText(text);
        label.setLayoutData(new GridData(SWT.TOP, SWT.TOP, false, true));
        
        return client;
    }
    
    @SuppressWarnings("unused")
    private void hookContextMenu(final GridTableViewer viewer) {
        MenuManager menuMgr = new MenuManager("#GridPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
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
            fAllClasses.addAll(Arrays.asList(ArchimateModelUtils.getBusinessClasses()));
            fAllClasses.addAll(Arrays.asList(ArchimateModelUtils.getApplicationClasses()));
            fAllClasses.addAll(Arrays.asList(ArchimateModelUtils.getTechnologyClasses()));
            fAllClasses.addAll(Arrays.asList(ArchimateModelUtils.getMotivationClasses()));
            fAllClasses.addAll(Arrays.asList(ArchimateModelUtils.getImplementationMigrationClasses()));
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
    public boolean performOk() {
        return true;
    }
    
    @Override
    protected void performDefaults() {
        super.performDefaults();
    }
    
    public void init(IWorkbench workbench) {
    }
}