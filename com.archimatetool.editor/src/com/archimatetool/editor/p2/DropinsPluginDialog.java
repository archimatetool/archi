/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;

/**
 * Dropins Plugin Dialog
 * 
 * @author Phillip Beauvoir
 */
public class DropinsPluginDialog extends ExtendedTitleAreaDialog {

    private static final String HELP_ID = "com.archimatetool.help.ManagePluginsDialog"; //$NON-NLS-1$
    
    public static final int INSTALL = 1025;
    public static final int UNINSTALL = 1026;
    
    private TableViewer viewer;
    
    public DropinsPluginDialog(Shell parentShell) {
        super(parentShell, "PluginsManagerDialog"); //$NON-NLS-1$
        
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.DropinsPluginDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.DropinsPluginDialog_1);
        setMessage(Messages.DropinsPluginDialog_2);

        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        container.setLayout(new GridLayout(1, false));
        
        Composite tableComp = new Composite(container, SWT.NONE);
        TableColumnLayout layout = new TableColumnLayout();
        tableComp.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        tableComp.setLayoutData(gd);

        viewer = new TableViewer(tableComp, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(viewer.getTable());
        
        viewer.getTable().setHeaderVisible(true);
        
        TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(10, false));
        column.getColumn().setText(Messages.DropinsPluginDialog_3);
        
        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(8, false));
        column.getColumn().setText(Messages.DropinsPluginDialog_4);

        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(10, false));
        column.getColumn().setText(Messages.DropinsPluginDialog_5);
        
        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(10, false));
        column.getColumn().setText(Messages.DropinsPluginDialog_6);
        
        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(10, false));
        column.getColumn().setText(Messages.DropinsPluginDialog_14);

        DropinsPluginHandler handler = new DropinsPluginHandler();

        viewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                try {
                    return handler.getInstalledPlugins().toArray();
                }
                catch(IOException ex) {
                    Logger.logError("Error getting installed plug-in", ex); //$NON-NLS-1$
                }
                
                return new Object[0];
            }
        });
        
        class LabelCellProvider extends LabelProvider implements ITableLabelProvider {
            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if(element instanceof Bundle) {
                    Bundle bundle = (Bundle)element;
                    
                    switch(columnIndex) {
                        case 0:
                            return bundle.getHeaders().get("Bundle-Name"); //$NON-NLS-1$

                        case 1:
                            return bundle.getVersion().toString();

                        case 2:
                            return bundle.getSymbolicName();

                        case 3:
                            return bundle.getHeaders().get("Bundle-Vendor"); //$NON-NLS-1$

                        case 4:
                            try {
                                File file = handler.getDropinsBundleFile(bundle);
                                return file == null ? Messages.DropinsPluginDialog_13 : file.getParentFile().getPath();
                            }
                            catch(IOException ex) {
                                return Messages.DropinsPluginDialog_13;
                            }

                        default:
                            break;
                    }
                }
                
                return element.toString();
            }
        }
        
        viewer.setComparator(new ViewerComparator() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                Bundle b1 = (Bundle)e1;
                Bundle b2 = (Bundle)e2;
                return b1.getHeaders().get("Bundle-Name").compareToIgnoreCase(b2.getHeaders().get("Bundle-Name")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        });
        
        viewer.setLabelProvider(new LabelCellProvider());
        
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                getButton(UNINSTALL).setEnabled(!event.getSelection().isEmpty());
            }
        });
        
        viewer.setInput(""); // anything will do //$NON-NLS-1$
        
        return area;
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        switch(buttonId) {
            case INSTALL:
                try {
                    DropinsPluginHandler handler = new DropinsPluginHandler();
                    if(handler.install(getShell()) == DropinsPluginHandler.RESTART) {
                        restart();
                    }
                }
                catch(IOException ex) {
                    Logger.logError("Error installing plug-in", ex); //$NON-NLS-1$
                }
                break;

            case UNINSTALL:
                try {
                    @SuppressWarnings("unchecked")
                    List<Bundle> selected = ((IStructuredSelection)viewer.getSelection()).toList();
                    DropinsPluginHandler handler = new DropinsPluginHandler();
                    if(handler.uninstall(getShell(), selected) == DropinsPluginHandler.RESTART) {
                        restart();
                    }
                }
                catch(IOException ex) {
                    Logger.logError("Error uninstalling plug-in", ex); //$NON-NLS-1$
                }
                break;

            default:
                super.buttonPressed(buttonId);
        }
    }
    
    private void restart() {
        okPressed();
        
        boolean ok = MessageDialog.openQuestion(getShell(),
                Messages.DropinsPluginDialog_7,
                Messages.DropinsPluginDialog_9);
        
        if(ok) {
            PlatformUI.getWorkbench().restart();
        }
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, INSTALL, Messages.DropinsPluginDialog_10, false);
        createButton(parent, UNINSTALL, Messages.DropinsPluginDialog_11, false).setEnabled(false);
        createButton(parent, IDialogConstants.OK_ID, Messages.DropinsPluginDialog_12, true);
    }

    @Override
    protected Point getDefaultDialogSize() {
        return new Point(670, 350);
    }
}
