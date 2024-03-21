/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.util.List;

import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;

/**
 * P2 Plugin Dialog
 * 
 * @author Phillip Beauvoir
 */
public class P2PluginDialog extends ExtendedTitleAreaDialog {

    private static final String HELP_ID = "com.archimatetool.help.ManageP2PluginDialog"; //$NON-NLS-1$
    
    public static final int INSTALL = 1025;
    public static final int UNINSTALL = 1026;
    
    private TableViewer viewer;
    
    private boolean needsRestart = false;

    public P2PluginDialog(Shell parentShell) {
        super(parentShell, "PluginsManagerDialog"); //$NON-NLS-1$
        
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.P2PluginDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.P2PluginDialog_1);
        setMessage(Messages.P2PluginDialog_2);

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
        
        viewer.getTable().setHeaderVisible(true);
        
        TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(25, false));
        column.getColumn().setText(Messages.P2PluginDialog_3);
        
        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(25, false));
        column.getColumn().setText(Messages.P2PluginDialog_4);

        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(25, false));
        column.getColumn().setText(Messages.P2PluginDialog_5);
        
        column = new TableViewerColumn(viewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(25, false));
        column.getColumn().setText(Messages.P2PluginDialog_6);

        viewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                try {
                    return P2Handler.getInstance().getInstalledFeatures().toArray();
                }
                catch(ProvisionException ex) {
                    ex.printStackTrace();
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
                if(element instanceof IInstallableUnit) {
                    IInstallableUnit iu = (IInstallableUnit)element;
                    
                    switch(columnIndex) {
                        case 0:
                            return iu.getProperty(IInstallableUnit.PROP_NAME, null);

                        case 1:
                            return iu.getVersion().toString();

                        case 2:
                            return iu.getId();

                        case 3:
                            return iu.getProperty(IInstallableUnit.PROP_PROVIDER, null);

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
                IInstallableUnit u1 = (IInstallableUnit)e1;
                IInstallableUnit u2 = (IInstallableUnit)e2;
                String name1 = u1.getProperty(IInstallableUnit.PROP_NAME, null);
                String name2 = u2.getProperty(IInstallableUnit.PROP_NAME, null);
                if(name1 != null && name2 != null) {
                    return name1.compareToIgnoreCase(name2);
                }
                return 0;
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
        boolean result = false;
        
        switch(buttonId) {
            case INSTALL:
                P2InstallHandler installHandler = new P2InstallHandler();
                result = installHandler.execute(getShell());
                if(installHandler.needsRestart()) { // Don't negate previous setting
                    needsRestart = true;
                }
                break;

            case UNINSTALL:
                @SuppressWarnings("unchecked")
                List<IInstallableUnit> selected = ((IStructuredSelection)viewer.getSelection()).toList();
                P2UninstallHandler uninstallHandler = new P2UninstallHandler();
                result = uninstallHandler.execute(getShell(), selected);
                if(uninstallHandler.needsRestart()) { // Don't negate previous setting
                    needsRestart = true;
                }
                break;

            case IDialogConstants.OK_ID:
                super.buttonPressed(buttonId);
                
                if(needsRestart) {
                    boolean restart = MessageDialog.openQuestion(getShell(),
                            Messages.P2PluginDialog_7,
                            Messages.P2PluginDialog_8);
                    if(restart) {
                        Display.getDefault().asyncExec(new Runnable() {
                            @Override
                            public void run() {
                                PlatformUI.getWorkbench().restart();
                            }
                        });
                    }
                }

            default:
                super.buttonPressed(buttonId);
        }
        
        if(result) {
            viewer.refresh();
        }
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, INSTALL, Messages.P2PluginDialog_9, false);
        createButton(parent, UNINSTALL, Messages.P2PluginDialog_10, false).setEnabled(false);
        createButton(parent, IDialogConstants.OK_ID, Messages.P2PluginDialog_11, true);
    }

    @Override
    protected Point getDefaultDialogSize() {
        return new Point(670, 350);
    }
}
