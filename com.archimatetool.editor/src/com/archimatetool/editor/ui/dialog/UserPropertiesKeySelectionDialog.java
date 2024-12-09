package com.archimatetool.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;



/**
 * User Properties Key Selection Dialog
 * 
 * @author Phillip Beauvoir
 */
public class UserPropertiesKeySelectionDialog extends ExtendedTitleAreaDialog {

    private static String HELP_ID = "com.archimatetool.help.userProperties"; //$NON-NLS-1$

    protected CheckboxTableViewer tableViewer;
    protected Button buttonSelectAll, buttonDeselectAll;
    
    protected List<String> keys;
    protected List<String> selectedKeys;

    /**
     * @param parentShell
     * @param keys The list of property keys
     * @param selected A list of property keys to select. Can be null.
     */
    public UserPropertiesKeySelectionDialog(Shell parentShell, List<String> keys, List<String> selected) {
        super(parentShell, "UserPropertiesKeySelectionDialog"); //$NON-NLS-1$
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        this.keys = keys;
        selectedKeys = selected;
    }
    
    @Override
    protected Control createButtonBar(Composite parent) {
        Control c = super.createButtonBar(parent);
        
        if(keys.size() == 0) {
            getButton(IDialogConstants.OK_ID).setEnabled(false);
            buttonSelectAll.setEnabled(false);
            buttonDeselectAll.setEnabled(false);
        }
        
        return c;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        getShell().setText(Messages.UserPropertiesKeySelectionDialog_0);
        setTitle(Messages.UserPropertiesKeySelectionDialog_1);
        setMessage(Messages.UserPropertiesKeySelectionDialog_2);

        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));

        createTableControl(client);
        createButtonPanel(client);
        
        return composite;
    }

    protected void createTableControl(Composite parent) {
        Composite tableComp = new Composite(parent, SWT.BORDER);
        TableColumnLayout tableLayout = new TableColumnLayout();
        tableComp.setLayout(tableLayout);
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        tableViewer = CheckboxTableViewer.newCheckList(tableComp, SWT.MULTI | SWT.FULL_SELECTION);
        tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

        tableViewer.getTable().setLinesVisible(true);

        // Columns
        TableViewerColumn columnKey = new TableViewerColumn(tableViewer, SWT.NONE, 0);
        tableLayout.setColumnData(columnKey.getColumn(), new ColumnWeightData(100, true));

        // Content Provider
        tableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return keys.toArray();
            }

            @Override
            public void dispose() {
            }
        });

        // Label Provider
        tableViewer.setLabelProvider(new LabelProvider());

        tableViewer.setInput(keys);
        
        if(selectedKeys != null) {
            tableViewer.setCheckedElements(selectedKeys.toArray());
        }
    }

    protected void createButtonPanel(Composite parent) {
        Composite client = new Composite(parent, SWT.NULL);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);

        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        client.setLayoutData(gd);

        buttonSelectAll = new Button(client, SWT.PUSH);
        buttonSelectAll.setText(Messages.UserPropertiesKeySelectionDialog_3);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        buttonSelectAll.setLayoutData(gd);
        buttonSelectAll.addSelectionListener(SelectionListener.widgetSelectedAdapter(e ->  {
            tableViewer.setCheckedElements(keys.toArray());
        }));

        buttonDeselectAll = new Button(client, SWT.PUSH);
        buttonDeselectAll.setText(Messages.UserPropertiesKeySelectionDialog_4);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        buttonDeselectAll.setLayoutData(gd);
        buttonDeselectAll.addSelectionListener(SelectionListener.widgetSelectedAdapter(e ->  {
            tableViewer.setCheckedElements(new Object[] {});
        }));
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        selectedKeys = new ArrayList<>();
        for(Object o : tableViewer.getCheckedElements()) {
            selectedKeys.add((String)o);
        }

        setReturnCode(buttonId);
        close();
    }
    
    public List<String> getSelectedKeys() {
        return selectedKeys;
    }
    
    @Override
    protected Point getDefaultDialogSize() {
        return new Point(400, 250);
    }
}