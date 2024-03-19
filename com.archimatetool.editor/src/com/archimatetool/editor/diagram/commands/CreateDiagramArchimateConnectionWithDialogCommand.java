/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;

/**
 * Command that will create a new ArchiMate Connection from a given CreateConnectionRequest
 * A relationship will also be created, or, if one similar already exists then the user will
 * be asked if they want to re-use that one.
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramArchimateConnectionWithDialogCommand extends CreateDiagramConnectionCommand {
    
    // Flag to mark whether a new relationship was created or whether we re-used an existing one
    private boolean fUseExistingRelation;

    public CreateDiagramArchimateConnectionWithDialogCommand(CreateConnectionRequest request) {
        super(request);
    }

    @Override
    public void execute() {
        fUseExistingRelation = checkToReuseExistingRelationship();
        
        super.execute();
        
        // Now add the relationship to the model if we haven't re-used existing one
        if(!fUseExistingRelation) {
            ((IDiagramModelArchimateConnection)fConnection).addArchimateConceptToModel(null);
        }
    }

    @Override
    public void redo() {
        super.redo();
        
        // Now add the relationship to the model if we haven't re-used existing one
        if(!fUseExistingRelation) {
            ((IDiagramModelArchimateConnection)fConnection).addArchimateConceptToModel(null);
        }
    }
    
    @Override
    public void undo() {
        super.undo();
        
        // Now remove the relationship from its folder if we haven't re-used existing one
        if(!fUseExistingRelation) {
            ((IDiagramModelArchimateConnection)fConnection).removeArchimateConceptFromModel();
        }
    }
    
    private boolean checkToReuseExistingRelationship() {
        if(fSource instanceof IDiagramModelArchimateComponent && fTarget instanceof IDiagramModelArchimateComponent) {
            IDiagramModelArchimateComponent source = (IDiagramModelArchimateComponent)fSource;
            IDiagramModelArchimateComponent target = (IDiagramModelArchimateComponent)fTarget;
            EClass classType = (EClass)fRequest.getNewObjectType();

            // Find any relations of this type connecting source and target concepts
            List<IArchimateRelationship> relations = getExistingRelationshipsOfType(classType, source.getArchimateConcept(), target.getArchimateConcept());
            
            // If there is already a relation of this type connecting source and target concepts...
            if(!relations.isEmpty()) {
                // Ask user in dialog if they want to reference an existing one
                ChooseRelationDialog dialog = new ChooseRelationDialog(ArchiLabelProvider.INSTANCE.getDefaultName(classType),
                                                                       NLS.bind(Messages.CreateArchimateConnectionWithDialogCommand_1,
                                                                       ArchiLabelProvider.INSTANCE.getLabel(source),
                                                                       ArchiLabelProvider.INSTANCE.getLabel(target)),
                                                                       relations);
                
                if(dialog.open() == Window.OK) {
                   // Create a new connection
                   fConnection = createNewConnection();
                   // set the connection's relationship to the chosen relation
                   ((IDiagramModelArchimateConnection)fConnection).setArchimateRelationship(dialog.getSelected());
                   return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Swap Source and Target Elements - used by Magic Connector
     */
    public void swapSourceAndTargetConcepts() {
        IConnectable tmp = fSource;
        fSource = fTarget;
        fTarget = tmp;
    }
    
    /**
     * See if there are existing relationships of the proposed type between source and target elements.
     * If there is, we can offer to re-use one of them instead of creating a new one.
     * @return a list of existing relationships which may be empty
     */
    List<IArchimateRelationship> getExistingRelationshipsOfType(EClass classType, IArchimateConcept source, IArchimateConcept target) {
        List<IArchimateRelationship> relations = new ArrayList<IArchimateRelationship>();
        
        for(IArchimateRelationship relation : source.getSourceRelationships()) {
            if(relation.eClass().equals(classType) && relation.getTarget() == target) {
                relations.add(relation);
            }
        }
        
        return relations;
    }
    
    /**
     * Dialog to select an existing relationship from a table
     */
    private static class ChooseRelationDialog extends ExtendedTitleAreaDialog {
        private String title, message;
        private List<IArchimateRelationship> relations;
        
        private RelationsTableViewer tableViewer;
        private IArchimateRelationship selected;

        private ChooseRelationDialog(String title, String message, List<IArchimateRelationship> relations) {
            super(Display.getCurrent().getActiveShell(), "ChooseExistingRelationDialog"); //$NON-NLS-1$
            
            setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
            setShellStyle(getShellStyle() | SWT.RESIZE);
            setHelpAvailable(false);
            
            this.title = title;
            this.message = message;
            this.relations = relations;
        }
        
        @Override
        protected void configureShell(Shell shell) {
            super.configureShell(shell);
            shell.setText(Messages.CreateArchimateConnectionWithDialogCommand_0);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            setTitle(title);
            setMessage(message);
            
            Composite composite = (Composite)super.createDialogArea(parent);

            Composite client = new Composite(composite, SWT.NULL);
            GridLayout layout = new GridLayout(1, false);
            client.setLayout(layout);
            client.setLayoutData(new GridData(GridData.FILL_BOTH));
            
            Label label = new Label(client, SWT.NONE);
            label.setText(Messages.CreateDiagramArchimateConnectionWithDialogCommand_1);
            
            Composite tableComp = new Composite(client, SWT.BORDER);
            tableComp.setLayout(new TableColumnLayout());
            tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            
            tableViewer = new RelationsTableViewer(tableComp, SWT.NONE);
            tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
            tableViewer.setInput(relations);
            
            tableViewer.addSelectionChangedListener((e) -> selected = (IArchimateRelationship)((IStructuredSelection)tableViewer.getSelection()).getFirstElement());
            tableViewer.addDoubleClickListener((e) -> okPressed());
            
            tableViewer.setSelection(new StructuredSelection(relations.get(0)));
            
            return composite;
        }
        
        IArchimateRelationship getSelected() {
            return selected;
        }
        
        @Override
        protected Point getDefaultDialogSize() {
            return new Point(500, 350);
        }
        
        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            createButton(parent, IDialogConstants.OK_ID, Messages.CreateDiagramArchimateConnectionWithDialogCommand_2, true);
            createButton(parent, IDialogConstants.CANCEL_ID, Messages.CreateDiagramArchimateConnectionWithDialogCommand_0, false);
        }
        
        
        private class RelationsTableViewer extends TableViewer {
            RelationsTableViewer(Composite parent, int style) {
                super(parent, SWT.FULL_SELECTION | style);
                
                // Mac Silicon Item height
                UIUtils.fixMacSiliconItemHeight(getTable());
                
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
                return relations.toArray();
            }
        }
        
        private class RelationsTableViewerLabelCellProvider extends LabelProvider {
            @Override
            public String getText(Object element) {
                return ArchiLabelProvider.INSTANCE.getLabel(element);
            }
            
            @Override
            public Image getImage(Object element) {
                return ArchiLabelProvider.INSTANCE.getImage(element);
            }
        }
    }
}
