/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.dialog;

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
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * New Nested Relation Dialog
 * 
 * @author Phillip Beauvoir
 */
public class NewNestedRelationDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.NewNestedRelationDialog"; //$NON-NLS-1$
    
    private RelationsTableViewer fTableViewer;
    
    private IDiagramModelArchimateObject fSourceObject, fTargetObject;
    
    private NestedConnectionInfo fSelected;
    
    // Source = parent, Target = child
    public NewNestedRelationDialog(IDiagramModelArchimateObject sourceObject, IDiagramModelArchimateObject targetObject) {
        super(Display.getCurrent().getActiveShell(), "NewNestedRelationDialog"); //$NON-NLS-1$
        
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fSourceObject = sourceObject;
        fTargetObject = targetObject;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.NewNestedRelationDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.NewNestedRelationDialog_1);
        
        String message = NLS.bind(Messages.NewNestedRelationDialog_2, fSourceObject.getName(), fTargetObject.getName());
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
        
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                okPressed();
            }
        });
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                fSelected = (NestedConnectionInfo)((IStructuredSelection)fTableViewer.getSelection()).getFirstElement();
            }
        });
        
        if(fTableViewer.validRelations != null && !fTableViewer.validRelations.isEmpty()) {
            fTableViewer.setSelection(new StructuredSelection(fTableViewer.validRelations.get(0)));
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
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, Messages.NewNestedRelationDialog_3, false);
    }
    
    public NestedConnectionInfo getSelected() {
        return fSelected;
    }
    
    private class RelationsTableViewer extends TableViewer {
        List<NestedConnectionInfo> validRelations;
        
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

        private class RelationsTableViewerContentProvider implements IStructuredContentProvider {
            @Override
            public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object parent) {
                validRelations = createValidRelationships();
                return validRelations.toArray();
            }
            
            private List<NestedConnectionInfo> createValidRelationships() {
                List<NestedConnectionInfo> list = new ArrayList<NestedConnectionInfo>();
                
                // Normal direction
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
                    if(isAllowedRelationInViewpoint(fSourceObject, eClass) && ArchimateModelUtils.isValidRelationship(fSourceObject.getArchimateElement(), fTargetObject.getArchimateElement(), eClass)) {
                        list.add(new NestedConnectionInfo(fSourceObject, fTargetObject, false, eClass)); 
                    }
                }
                
                // Reverse direction
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewReverseRelations()) {
                    if(isAllowedRelationInViewpoint(fTargetObject, eClass) && ArchimateModelUtils.isValidRelationship(fTargetObject.getArchimateElement(), fSourceObject.getArchimateElement(), eClass)) {
                        list.add(new NestedConnectionInfo(fTargetObject, fSourceObject, true, eClass)); 
                    }
                }

                return list;
            }
            
            /**
             * @return True if type is an allowed relation type for a given Viewpoint
             */
            private boolean isAllowedRelationInViewpoint(IDiagramModelArchimateComponent dmc, EClass type) {
                if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS)) {
                    return true;
                }
                
                return ViewpointManager.INSTANCE.isAllowedConceptForDiagramModel((IArchimateDiagramModel)dmc.getDiagramModel(), type);
            }
        }

        private class RelationsTableViewerLabelCellProvider extends LabelProvider {
            @Override
            public String getText(Object element) {
                NestedConnectionInfo info = (NestedConnectionInfo)element;
                
                String relationshipName = ArchiLabelProvider.INSTANCE.getDefaultName(info.getRelationshipType());
                String reverse = info.isReverse() ? Messages.NewNestedRelationDialog_4 : ""; //$NON-NLS-1$
                String sentence = ArchiLabelProvider.INSTANCE.getRelationshipSentence(info.getRelationshipType(), info.getSourceObject().getArchimateConcept(),
                        info.getTargetObject().getArchimateConcept());
                
                return NLS.bind(Messages.NewNestedRelationDialog_5, new Object[] { relationshipName, reverse, sentence });
            }
            
            @Override
            public Image getImage(Object element) {
                return ArchiLabelProvider.INSTANCE.getImage(((NestedConnectionInfo)element).getRelationshipType());
            }
         }
    }
}
