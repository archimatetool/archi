/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.ArchimateEditorPlugin;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.DerivedRelationsUtils;
import com.archimatetool.model.util.DerivedRelationsUtils.TooComplicatedException;



/**
 * Create Derived Relation Action
 * 
 * @author Phillip Beauvoir
 */
public class CreateDerivedRelationAction extends SelectionAction {
    
    public static final String ID = "CreateDerivedRelationAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.CreateDerivedRelationAction_0;

    public CreateDerivedRelationAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setSelectionProvider((ISelectionProvider)part.getAdapter(GraphicalViewer.class));
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DERIVED_16));
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> selection = getSelectedObjects();
        
        if(selection.size() != 2) {
            return false;
        }
        
        for(Object object : selection) {
            if(!(object instanceof EditPart)) {
                return false;
            }
            EditPart part = (EditPart)object;
            if(!(part.getModel() instanceof IDiagramModelArchimateObject)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        
        EditPart editPart = (EditPart)selection.get(0);
        IDiagramModelArchimateObject diagramModelObject1 = (IDiagramModelArchimateObject)editPart.getModel();
        editPart = (EditPart)selection.get(1);
        IDiagramModelArchimateObject diagramModelObject2 = (IDiagramModelArchimateObject)editPart.getModel();
        
        ChainList chainList1 = new ChainList(diagramModelObject1, diagramModelObject2);
        ChainList chainList2 = new ChainList(diagramModelObject2, diagramModelObject1);
        
        // Already has a direct relationship in both directions
        if(chainList1.hasExistingDirectRelationship() && chainList2.hasExistingDirectRelationship()) {
            MessageDialog.openInformation(getWorkbenchPart().getSite().getShell(),
                    Messages.CreateDerivedRelationAction_1,
                    Messages.CreateDerivedRelationAction_2);
            return;
        }
        
        // Both chains are too complicated
        if(chainList1.isTooComplicated && chainList2.isTooComplicated) {
            MessageDialog.openInformation(getWorkbenchPart().getSite().getShell(),
                    Messages.CreateDerivedRelationAction_1,
                    Messages.CreateDerivedRelationAction_3);
            return;
        }
        
        // No chains found, although perhaps one was too complicated...
        if(chainList1.getChains() == null && chainList2.getChains() == null) {
            if(chainList1.isTooComplicated || chainList2.isTooComplicated) {
                MessageDialog.openInformation(getWorkbenchPart().getSite().getShell(),
                        Messages.CreateDerivedRelationAction_1,
                        Messages.CreateDerivedRelationAction_4);
            }
            else {
                MessageDialog.openInformation(getWorkbenchPart().getSite().getShell(),
                        Messages.CreateDerivedRelationAction_1,
                        Messages.CreateDerivedRelationAction_5);
            }
            return;
        }
        
        CreateDerivedConnectionDialog dialog = new CreateDerivedConnectionDialog(getWorkbenchPart().getSite().getShell(),
                chainList1, chainList2);
        
        if(dialog.open() == IDialogConstants.OK_ID) {
            List<IRelationship> chain = dialog.getSelectedChain();
            if(chain != null) {
                ChainList chainList = dialog.getSelectedChainList();
                EClass relationshipClass = DerivedRelationsUtils.getWeakestType(chain);
                IRelationship relation = (IRelationship)IArchimateFactory.eINSTANCE.create(relationshipClass);
                CommandStack stack = (CommandStack)getWorkbenchPart().getAdapter(CommandStack.class);
                stack.execute(new CreateDerivedConnectionCommand(chainList.srcDiagramObject, chainList.tgtDiagramObject, relation));
            }
        }
    }
    
    // ================================ Helper Classes =====================================
    
    /**
     * Convenience class to group things together
     */
    private static class ChainList {
        IDiagramModelArchimateObject srcDiagramObject;
        IDiagramModelArchimateObject tgtDiagramObject;
        IArchimateElement srcElement;
        IArchimateElement tgtElement;
        List<List<IRelationship>> chains;
        boolean isTooComplicated;
        
        ChainList(IDiagramModelArchimateObject srcDiagramObject, IDiagramModelArchimateObject tgtDiagramObject) {
            this.srcDiagramObject = srcDiagramObject;
            this.tgtDiagramObject = tgtDiagramObject;
            srcElement = srcDiagramObject.getArchimateElement();
            tgtElement = tgtDiagramObject.getArchimateElement();
            
            if(!hasExistingDirectRelationship()) {
                findChains();
            }
        }
        
        boolean hasExistingDirectRelationship() {
            return DerivedRelationsUtils.hasDirectStructuralRelationship(srcElement, tgtElement);
        }
        
        private void findChains() {
            try {
                chains = DerivedRelationsUtils.getDerivedRelationshipChains(srcElement, tgtElement);
            }
            catch(TooComplicatedException ex) {
                isTooComplicated = true;
            }
        }
        
        List<List<IRelationship>> getChains() {
            if(hasExistingDirectRelationship()) {
                return null;
            }
            
            return chains;
        }
    }
    
    /**
     * Dialog window
     */
    private static class CreateDerivedConnectionDialog extends Dialog implements ISelectionChangedListener, IDoubleClickListener {
        // For persisting dialog position and size
        private static final String DIALOG_SETTINGS_SECTION = "CreateDerivedConnectionDialog"; //$NON-NLS-1$

        private ChainList chainList1, chainList2;
        private List<IRelationship> selectedChain;
        private ChainList selectedChainList;
        
        public CreateDerivedConnectionDialog(Shell parentShell, ChainList chainList1, ChainList chainList2) {
            super(parentShell);
            setShellStyle(getShellStyle() | SWT.RESIZE);
            this.chainList1 = chainList1;
            this.chainList2 = chainList2;
        }
        
        @Override
        protected void configureShell(Shell shell) {
            super.configureShell(shell);
            shell.setText(Messages.CreateDerivedRelationAction_6);
            shell.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DERIVED_16));
        }
        
        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite)super.createDialogArea(parent);
            composite.setBackground(ColorConstants.white);
            composite.setBackgroundMode(SWT.INHERIT_DEFAULT);

            GridLayout layout = new GridLayout();
            layout.marginWidth = 10;
            composite.setLayout(layout);
            GridData gd = new GridData(GridData.FILL_BOTH);
            gd.widthHint = 700;
            composite.setLayoutData(gd);
            
            if(chainList1.getChains() != null) {
                createTable(composite, chainList1);
            }
            else if(chainList1.isTooComplicated) {
                createTooComplicatedMessage(composite, chainList1);
            }
            
            if(chainList2.getChains() != null) {
                createTable(composite, chainList2);
            }
            else if(chainList2.isTooComplicated) {
                createTooComplicatedMessage(composite, chainList2);
            }
            
            return composite;
        }
        
        private void createTable(Composite parent, ChainList chainList) {
            createLabel(parent, chainList);
            
            Composite c = new Composite(parent, SWT.NULL);
            c.setLayout(new TableColumnLayout());
            GridData gd = new GridData(GridData.FILL_BOTH);
            gd.heightHint = 200;
            c.setLayoutData(gd);

            DerivedConnectionsTableViewer viewer = new DerivedConnectionsTableViewer(c);
            
            viewer.setInput(chainList);
            viewer.addSelectionChangedListener(this);
            viewer.addDoubleClickListener(this);
        }
        
        private void createTooComplicatedMessage(Composite parent, ChainList chainList) {
            CLabel label = createLabel(parent, chainList);
            label.setText(label.getText() + "  " + Messages.CreateDerivedRelationAction_7); //$NON-NLS-1$
            label.setImage(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION));
        }
        
        private CLabel createLabel(Composite parent, ChainList chainList) {
            CLabel label = new CLabel(parent, SWT.NULL);
            String text = NLS.bind(Messages.CreateDerivedRelationAction_8, chainList.srcElement.getName(), chainList.tgtElement.getName());
            label.setText(text);
            label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            return label;
        }
        
        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            super.createButtonsForButtonBar(parent);
            getButton(IDialogConstants.OK_ID).setEnabled(false);
        }
        
        public List<IRelationship> getSelectedChain() {
            return selectedChain;
        }
        
        public ChainList getSelectedChainList() {
            return selectedChainList;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection)event.getSelection();
            selectedChain = (List<IRelationship>)selection.getFirstElement();
            selectedChainList = (ChainList)((TableViewer)event.getSource()).getInput();
            getButton(IDialogConstants.OK_ID).setEnabled(!selection.isEmpty());
        }

        @Override
        public void doubleClick(DoubleClickEvent event) {
            okPressed();
        }
        
        @Override
        protected IDialogSettings getDialogBoundsSettings() {
            IDialogSettings settings = ArchimateEditorPlugin.INSTANCE.getDialogSettings();
            IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
            if(section == null) {
                section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
            } 
            return section;
        }

    }
    
    /**
     * Table Viewer
     */
    private static class DerivedConnectionsTableViewer extends TableViewer {
        public DerivedConnectionsTableViewer(Composite parent) {
            super(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.BORDER);
            
            Table table = getTable();
            
            table.setHeaderVisible(true);
            table.setLinesVisible(true);
            
            TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
            
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(Messages.CreateDerivedRelationAction_9);
            layout.setColumnData(column, new ColumnWeightData(80, true));
            
            column = new TableColumn(table, SWT.NONE);
            column.setText(Messages.CreateDerivedRelationAction_10);
            layout.setColumnData(column, new ColumnWeightData(20, true));
            
            setContentProvider(new DerivedConnectionsContentProvider());
            setLabelProvider(new DerivedConnectionsLabelProvider());
        }
        
        /**
         * Table Content Provider
         */
        private class DerivedConnectionsContentProvider implements IStructuredContentProvider {
            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object input) {
                if(input instanceof ChainList) {
                    return ((ChainList)input).getChains().toArray();
                }
                return null;
            }
        }

        /**
         * Table Lable Provider
         */
        private class DerivedConnectionsLabelProvider extends LabelProvider implements ITableLabelProvider {
            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if(element == null) {
                    return ""; //$NON-NLS-1$
                }

                @SuppressWarnings("unchecked")
                List<IRelationship> chain = (List<IRelationship>)element;
                ChainList chainList = (ChainList)getInput();

                switch(columnIndex) {
                    // Chain
                    case 0:
                        String s = chainList.srcElement.getName();
                        s += " --> "; //$NON-NLS-1$
                        for(int i = 1; i < chain.size(); i++) {
                            IRelationship relation = chain.get(i);
                            s += getRelationshipText(chain, relation);
                            if(DerivedRelationsUtils.isBidirectionalRelationship(relation)) {
                                s += " <-> "; //$NON-NLS-1$
                            }
                            else {
                                s += " --> "; //$NON-NLS-1$
                            }
                        }
                        s += chainList.tgtElement.getName();
                        
                        return s; 

                        // Weakest
                    case 1:
                        return DerivedRelationsUtils.getWeakestType(chain).getName(); 
                }

                return ""; //$NON-NLS-1$
            }
            
            private String getRelationshipText(List<IRelationship> chain, IRelationship relation) {
                if(DerivedRelationsUtils.isBidirectionalRelationship(relation)) {
                    int index = chain.indexOf(relation);
                    if(index > 0) {
                        IRelationship previous = chain.get(index - 1);
                        if(relation.getTarget() == previous.getTarget()) {
                            return relation.getTarget().getName();
                        }
                    }
                    return relation.getSource().getName();
                }
                else {
                    return relation.getSource().getName();
                }
            }
        }
    }
    
    
    /**
     * Command Stack Command
     */
    private static class CreateDerivedConnectionCommand extends Command {
        private IRelationship fRelation;
        private IDiagramModelArchimateConnection fConnection;
        private IDiagramModelArchimateObject fSource;
        private IDiagramModelArchimateObject fTarget;
        private boolean fDerivedFolderWasCreated;
        
        public CreateDerivedConnectionCommand(IDiagramModelArchimateObject source, IDiagramModelArchimateObject target,
                IRelationship relation) {
            fSource = source;
            fTarget = target;
            fRelation = relation;
            setLabel(Messages.CreateDerivedRelationAction_11);
        }

        @Override
        public void execute() {
            fConnection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            fConnection.setRelationship(fRelation);
            fConnection.connect(fSource, fTarget);
            addToModel();
        }
        
        @Override
        public void redo() {
            fConnection.reconnect();
            addToModel();
        }
        
        private void addToModel() {
            IFolder folder = fConnection.getDiagramModel().getArchimateModel().getFolder(FolderType.DERIVED);
            // We need to create the Derived Relations folder
            if(folder == null) {
                folder = fConnection.getDiagramModel().getArchimateModel().addDerivedRelationsFolder();
                fDerivedFolderWasCreated = true;
            }
            fConnection.addRelationshipToModel(folder);
        }
        
        @Override
        public void undo() {
            // Remove the model relationship from its model folder
            fConnection.removeRelationshipFromModel();
            
            // If the Derived Relations folder was created, remove it
            if(fDerivedFolderWasCreated) {
                fConnection.getDiagramModel().getArchimateModel().removeDerivedRelationsFolder();
            }
            
            // Disconnect last because we needed access to fConnection.getDiagramModel()
            fConnection.disconnect();
        }
        
        @Override
        public void dispose() {
            fConnection = null;
            fSource = null;
            fTarget = null;
            fRelation = null;
        }
    }
    
}
