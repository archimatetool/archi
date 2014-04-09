/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;



/**
 * User Properties Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class UserPropertiesManagerDialog extends ExtendedTitleAreaDialog {

    private static String HELP_ID = "com.archimatetool.help.userProperties"; //$NON-NLS-1$

    private static class KeyEntry {
        String newName;
        int usedTimes = 1;

        KeyEntry(String name) {
            newName = name;
        }
    }

    private TableViewer fTableViewer;

    private IArchimateModel fArchimateModel;

    private Hashtable<String, KeyEntry> fKeysTable = new Hashtable<String, KeyEntry>();

    private Button fButtonDelete, fButtonRename;
    private IAction fActionDelete, fActionRename;

    public UserPropertiesManagerDialog(Shell parentShell, IArchimateModel model) {
        super(parentShell, "UserPropertiesManagerDialog"); //$NON-NLS-1$
        setTitleImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);

        fArchimateModel = model;

        getAllUniquePropertyKeysForModel();
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.UserPropertiesManagerDialog_0);
    }

    @Override
    protected Control createButtonBar(Composite parent) {
        Control c = super.createButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        return c;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.UserPropertiesManagerDialog_1);
        setMessage(Messages.UserPropertiesManagerDialog_2);

        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));

        createTableControl(client);
        createButtonPanel(client);

        // Delete Action
        fActionDelete = new Action(Messages.UserPropertiesManagerDialog_3) {
            @Override
            public void run() {
                deleteSelectedPropertyKeys();
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
            };
        };
        fActionDelete.setEnabled(false);

        // Rename Action
        fActionRename = new Action(Messages.UserPropertiesManagerDialog_4) {
            @Override
            public void run() {
                renameSelectedPropertyKey();
            }

            @Override
            public String getToolTipText() {
                return Messages.UserPropertiesManagerDialog_5;
            }
        };
        fActionRename.setEnabled(false);

        hookContextMenu();

        return composite;
    }

    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PropertiesManagerPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(fTableViewer.getControl());
        fTableViewer.getControl().setMenu(menu);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(fActionDelete);
        manager.add(fActionRename);
    }

    private void createTableControl(Composite parent) {
        Composite tableComp = new Composite(parent, SWT.BORDER);
        TableColumnLayout tableLayout = new TableColumnLayout();
        tableComp.setLayout(tableLayout);
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        fTableViewer = new TableViewer(tableComp, SWT.MULTI | SWT.FULL_SELECTION);
        fTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

        fTableViewer.getTable().setHeaderVisible(true);
        fTableViewer.getTable().setLinesVisible(true);

        fTableViewer.setSorter(new ViewerSorter());

        // Columns
        TableViewerColumn columnOldKey = new TableViewerColumn(fTableViewer, SWT.NONE, 0);
        columnOldKey.getColumn().setText(Messages.UserPropertiesManagerDialog_6);
        tableLayout.setColumnData(columnOldKey.getColumn(), new ColumnWeightData(40, true));

        TableViewerColumn columnNewKey = new TableViewerColumn(fTableViewer, SWT.NONE, 1);
        columnNewKey.getColumn().setText(Messages.UserPropertiesManagerDialog_7);
        tableLayout.setColumnData(columnNewKey.getColumn(), new ColumnWeightData(40, true));
        columnNewKey.setEditingSupport(new KeyEditingSupport(fTableViewer));

        TableViewerColumn columnUsedNumber = new TableViewerColumn(fTableViewer, SWT.NONE, 2);
        columnUsedNumber.getColumn().setText("# " + Messages.UserPropertiesManagerDialog_8); //$NON-NLS-1$
        tableLayout.setColumnData(columnUsedNumber.getColumn(), new ColumnWeightData(20, true));

        // Content Provider
        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            public void dispose() {
            }

            public Object[] getElements(Object inputElement) {
                return fKeysTable.entrySet().toArray();
            }
        });

        // Label Provider
        fTableViewer.setLabelProvider(new LabelCellProvider());

        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                fActionDelete.setEnabled(!event.getSelection().isEmpty());
                fButtonDelete.setEnabled(!event.getSelection().isEmpty());

                fActionRename.setEnabled(!event.getSelection().isEmpty());
                fButtonRename.setEnabled(!event.getSelection().isEmpty());
            }
        });

        fTableViewer.setInput(""); // anything will do //$NON-NLS-1$
    }

    private void createButtonPanel(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);

        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        client.setLayoutData(gd);

        fButtonDelete = new Button(client, SWT.PUSH);
        fButtonDelete.setText(Messages.UserPropertiesManagerDialog_9);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonDelete.setLayoutData(gd);
        fButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelectedPropertyKeys();
            }
        });
        fButtonDelete.setEnabled(false);

        fButtonRename = new Button(client, SWT.PUSH);
        fButtonRename.setText(Messages.UserPropertiesManagerDialog_10);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fButtonRename.setLayoutData(gd);
        fButtonRename.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                renameSelectedPropertyKey();
            }
        });
        fButtonRename.setEnabled(false);
    }

    private void deleteSelectedPropertyKeys() {
        for(Object o : ((IStructuredSelection)fTableViewer.getSelection()).toList()) {
            fKeysTable.entrySet().remove(o);
        }
        fTableViewer.refresh();
        getButton(IDialogConstants.OK_ID).setEnabled(true);
    }

    private void renameSelectedPropertyKey() {
        Object selected = ((IStructuredSelection)fTableViewer.getSelection()).getFirstElement();
        if(selected != null) {
            fTableViewer.editElement(selected, 1);
        }
    }

    private void getAllUniquePropertyKeysForModel() {
        for(Iterator<EObject> iter = fArchimateModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
                String key = ((IProperty)element).getKey();
                if(key != null) {
                    if(fKeysTable.containsKey(key)) {
                        fKeysTable.get(key).usedTimes++;
                    }
                    else {
                        fKeysTable.put(key, new KeyEntry(key));
                    }
                }
            }
        }
    }

    @Override
    protected void okPressed() {
        super.okPressed();

        CompoundCommand compoundCmd = new CompoundCommand(Messages.UserPropertiesManagerDialog_11) {
            @Override
            public void execute() {
                BusyIndicator.showWhile(null, new Runnable() {
                    public void run() {
                        doSuperExecute();
                    }
                });
            }

            @Override
            public void undo() {
                BusyIndicator.showWhile(null, new Runnable() {
                    public void run() {
                        doSuperUndo();
                    }
                });
            }

            @Override
            public void redo() {
                BusyIndicator.showWhile(null, new Runnable() {
                    public void run() {
                        doSuperRedo();
                    }
                });
            }

            void doSuperExecute() {
                super.execute();
            }

            void doSuperUndo() {
                super.undo();
            }

            void doSuperRedo() {
                super.redo();
            }
        };

        checkRenames(compoundCmd);
        checkDeletions(compoundCmd);

        CommandStack stack = (CommandStack)fArchimateModel.getAdapter(CommandStack.class);
        stack.execute(compoundCmd.unwrap());
    }

    /**
     * Check for deletions
     */
    private void checkDeletions(CompoundCommand compoundCmd) {
        for(Iterator<EObject> iter = fArchimateModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
                IProperty property = (IProperty)element;
                String key = property.getKey();
                if(key != null && !fKeysTable.containsKey(key)) {
                    Command cmd = new DeletePropertyKeyCommand(((IProperties)property.eContainer()).getProperties(), property);
                    compoundCmd.add(cmd);
                }
            }
        }
    }

    /**
     * Check for renames
     */
    private void checkRenames(CompoundCommand compoundCmd) {
        for(Entry<String, KeyEntry> entry : fKeysTable.entrySet()) {
            String oldName = entry.getKey();
            String newName = entry.getValue().newName;
            if(!oldName.equals(newName)) {
                addKeyNameChangeCommands(compoundCmd, oldName, newName);
            }
        }
    }

    /**
     * Change all instances of key to new name
     */
    private void addKeyNameChangeCommands(CompoundCommand compoundCmd, String oldName, String newName) {
        for(Iterator<EObject> iter = fArchimateModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
                String key = ((IProperty)element).getKey();
                if(key != null && key.equals(oldName)) {
                    Command cmd = new RenamePropertyKeyCommand((IProperty)element, oldName, newName);
                    compoundCmd.add(cmd);
                }
            }
        }
    }

    @Override
    protected Point getDefaultDialogSize() {
        return new Point(500, 350);
    }

    /**
     * Label Provider
     */
    private static class LabelCellProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            @SuppressWarnings("unchecked")
            Entry<String, KeyEntry> entry = (Entry<String, KeyEntry>)element;

            switch(columnIndex) {
                case 0:
                    return entry.getKey();

                case 1:
                    return entry.getValue().newName;

                case 2:
                    return "" + entry.getValue().usedTimes; //$NON-NLS-1$

                default:
                    return null;
            }
        }

        @Override
        public Color getForeground(Object element, int columnIndex) {
            @SuppressWarnings("unchecked")
            Entry<String, KeyEntry> entry = (Entry<String, KeyEntry>)element;

            if(columnIndex == 1) {
                if(!entry.getKey().equals(entry.getValue().newName)) {
                    return ColorConstants.red;
                }
            }
            return null;
        }

        @Override
        public Color getBackground(Object element, int columnIndex) {
            return null;
        }
    }

    /**
     * Key Editor
     */
    private class KeyEditingSupport extends EditingSupport {
        TextCellEditor cellEditor;

        public KeyEditingSupport(ColumnViewer viewer) {
            super(viewer);
            cellEditor = new TextCellEditor((Composite)viewer.getControl());
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return cellEditor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            @SuppressWarnings("unchecked")
            Entry<String, KeyEntry> entry = (Entry<String, KeyEntry>)element;
            return entry.getValue().newName;
        }

        @Override
        protected void setValue(Object element, Object value) {
            @SuppressWarnings("unchecked")
            Entry<String, KeyEntry> entry = (Entry<String, KeyEntry>)element;
            if(!value.equals(entry.getValue().newName)) {
                entry.getValue().newName = (String)value;
                fTableViewer.update(entry, null);
                getButton(IDialogConstants.OK_ID).setEnabled(true);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    //
    // Commands
    //
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Rename Property Key Command
     */
    private static class RenamePropertyKeyCommand extends Command {
        private IProperty property;
        private String oldName, newName;

        RenamePropertyKeyCommand(IProperty property, String oldName, String newName) {
            this.property = property;
            this.oldName = oldName;
            this.newName = newName;
            setLabel(Messages.UserPropertiesManagerDialog_12);
        }

        @Override
        public void execute() {
            property.setKey(newName);
        }

        @Override
        public void undo() {
            property.setKey(oldName);
        }

        @Override
        public void dispose() {
            property = null;
        }
    }

    /**
     * Delete Property Key Command
     */
    private static class DeletePropertyKeyCommand extends Command {
        private EList<IProperty> properties;
        private IProperty property;
        private int index;

        DeletePropertyKeyCommand(EList<IProperty> properties, IProperty property) {
            this.properties = properties;
            this.property = property;
            setLabel(Messages.UserPropertiesManagerDialog_13);
        }

        @Override
        public void execute() {
            // Ensure index is stored just before execute because if this is part of a composite action
            // then the index positions will have changed
            index = properties.indexOf(property); 
            if(index != -1) {
                properties.remove(property);
            }
        }

        @Override
        public void undo() {
            if(index != -1) {
                properties.add(index, property);
            }
        }

        @Override
        public void dispose() {
            properties = null;
            property = null;
        }
    }
}
