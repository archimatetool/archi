/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;

/**
 * Generate View Dialog
 * 
 * @author Phillip Beauvoir
 */
public class GenerateViewDialog extends ExtendedTitleAreaDialog {
   
    private static String HELP_ID = "com.archimatetool.help.GenerateViewDialog"; //$NON-NLS-1$
    private static String DIALOG_ID = "GenerateViewDialog"; //$NON-NLS-1$

    private static final String PREFS_ALLCONNECTIONS = "GenerateView_AllConnections"; //$NON-NLS-1$
    private static final String PREFS_LASTVIEWPOINT = "GenerateView_LastViewpoint"; //$NON-NLS-1$
    
    private List<IArchimateElement> fSelectedElements;
    
    private ComboViewer fComboViewer;
    private Button fAddAllConnectionsButton;
    private Text fNameText;
    
    private IViewpoint fSelectedViewpoint;
    private List<IViewpoint> fValidViewPoints = new ArrayList<IViewpoint>();
    private String fViewName;
    
    private boolean fAddAllConnections;

    public GenerateViewDialog(Shell parentShell, List<IArchimateElement> selectedElements) {
        super(parentShell, DIALOG_ID);
        
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
        
        fSelectedElements = selectedElements;
    }
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.GenerateViewDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        setTitle(Messages.GenerateViewDialog_1);
        
        String message = ""; //$NON-NLS-1$
        for(Iterator<IArchimateElement> iter = fSelectedElements.iterator(); iter.hasNext();) {
            message += ArchiLabelProvider.INSTANCE.getLabel(iter.next());
            if(iter.hasNext()) {
                message += ", "; //$NON-NLS-1$
            }
            else {
                message += "."; //$NON-NLS-1$
            }
        }
        setMessage(message);
        
        Composite composite = (Composite)super.createDialogArea(parent);

        Composite client = new Composite(composite, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.verticalSpacing = 20;
        client.setLayout(layout);
        client.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Label label = new Label(client, SWT.NONE);
        label.setText(Messages.GenerateViewDialog_3);
        
        fComboViewer = new ComboViewer(new Combo(client, SWT.READ_ONLY | SWT.BORDER));
        fComboViewer.getCombo().setVisibleItemCount(12);
        fComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        for(IViewpoint viewpoint : ViewpointManager.INSTANCE.getAllViewpoints()) {
            boolean allowed = true;
            
            for(IArchimateElement element : fSelectedElements) {
                if(!viewpoint.isAllowedConcept(element.eClass())) {
                    allowed = false;
                    break;
                }
            }
            
            if(allowed && !fValidViewPoints.contains(viewpoint)) {
                fValidViewPoints.add(viewpoint);
            }
        }
        
        fComboViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object inputElement) {
                return fValidViewPoints.toArray();
            }
        });
        
        fComboViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((IViewpoint)element).getName();
            }
        });
        
        fComboViewer.setInput(""); //$NON-NLS-1$
        
        label = new Label(client, SWT.NONE);
        label.setText(Messages.GenerateViewDialog_2);
        
        fNameText = new Text(client, SWT.BORDER);
        fNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fNameText.setText(Messages.GenerateViewDialog_6 + " " + ArchiLabelProvider.INSTANCE.getLabel(fSelectedElements.get(0))); //$NON-NLS-1$
        
        Group groupOptions = new Group(client, SWT.NONE);
        groupOptions.setText(Messages.GenerateViewDialog_4);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        groupOptions.setLayoutData(gd);
        groupOptions.setLayout(new GridLayout(2, false));
        
        label = new Label(groupOptions, SWT.NONE);
        label.setText(Messages.GenerateViewDialog_5);
        fAddAllConnectionsButton = new Button(groupOptions, SWT.CHECK);
        
        loadPreferences();
        
        return composite;
    }
    
    @Override
    protected void okPressed() {
        fSelectedViewpoint = (IViewpoint)((IStructuredSelection)fComboViewer.getSelection()).getFirstElement();
        fAddAllConnections = fAddAllConnectionsButton.getSelection();
        fViewName = fNameText.getText();
        
        savePreferences();
        
        super.okPressed();
    };
    
    IViewpoint getSelectedViewpoint() {
        return fSelectedViewpoint;
    }
    
    boolean isAddAllConnections() {
        return fAddAllConnections;
    }
    
    String getViewName() {
        return fViewName;
    }

    @Override
    protected Point getDefaultDialogSize() {
        return new Point(500, 350);
    }
    
    void savePreferences() {
        IPreferenceStore store = ArchiPlugin.PREFERENCES;
        
        store.setValue(PREFS_ALLCONNECTIONS, fAddAllConnections);
        store.setValue(PREFS_LASTVIEWPOINT, fSelectedViewpoint.getID());
    }

    void loadPreferences() {
        IPreferenceStore store = ArchiPlugin.PREFERENCES;
        
        fAddAllConnectionsButton.setSelection(store.getBoolean(PREFS_ALLCONNECTIONS));
        
        String id = store.getString(PREFS_LASTVIEWPOINT);
        IViewpoint lastViewpoint = ViewpointManager.INSTANCE.getViewpoint(id);
        
        if(!fValidViewPoints.contains(lastViewpoint)) {
            lastViewpoint = ViewpointManager.NONE_VIEWPOINT;
        }
        
        fComboViewer.setSelection(new StructuredSelection(lastViewpoint));
    }
}