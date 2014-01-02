/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.ConnectionTextPositionCommand;
import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.ILockable;



/**
 * Connection Property Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__TEXT_POSITION) {
                refreshTextPositionCombo();
            }
            else if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH) {
                refreshLineWidthCombo();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private IDiagramModelConnection fConnection;
    
    private Combo fComboTextPosition;
    private Combo fComboLineWidth;
    
    public static final String[] comboTextPositionItems = {
            Messages.DiagramConnectionSection_0,
            Messages.DiagramConnectionSection_1,
            Messages.DiagramConnectionSection_2
    };
    
    public static final String[] comboLineWidthItems = {
            Messages.DiagramConnectionSection_3,
            Messages.DiagramConnectionSection_4,
            Messages.DiagramConnectionSection_5
    };
    
    @Override
    protected void createControls(Composite parent) {
        createTextPositionComboControl(parent);
        createLineWidthComboControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createTextPositionComboControl(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionSection_6, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboTextPosition = new Combo(parent, SWT.READ_ONLY);
        fComboTextPosition.setItems(comboTextPositionItems);
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboTextPosition.setLayoutData(gd);
        fComboTextPosition.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new ConnectionTextPositionCommand(fConnection, fComboTextPosition.getSelectionIndex()));
                    fIsExecutingCommand = false;
                }
            }
        });
    }
    
    private void createLineWidthComboControl(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionSection_7, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboLineWidth = new Combo(parent, SWT.READ_ONLY);
        fComboLineWidth.setItems(comboLineWidthItems);
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboLineWidth.setLayoutData(gd);
        fComboLineWidth.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new LineWidthCommand(fConnection, fComboLineWidth.getSelectionIndex() + 1));
                    fIsExecutingCommand = false;
                }
            }
        });
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof IAdaptable) {
            fConnection = (IDiagramModelConnection)((IAdaptable)element).getAdapter(IDiagramModelConnection.class);
        }
        else {
            throw new RuntimeException("Should have been an Edit Part"); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        refreshTextPositionCombo();
        refreshLineWidthCombo();
    }
    
    protected void refreshTextPositionCombo() {
        if(fIsExecutingCommand) {
            return; 
        }
        int pos = fConnection.getTextPosition();
        fComboTextPosition.select(pos);
        
        boolean enabled = fConnection instanceof ILockable ? !((ILockable)fConnection).isLocked() : true;
        fComboTextPosition.setEnabled(enabled);
    }
    
    protected void refreshLineWidthCombo() {
        if(fIsExecutingCommand) {
            return; 
        }
        int lineWidth = fConnection.getLineWidth();
        fComboLineWidth.select(lineWidth - 1);
        
        boolean enabled = fConnection instanceof ILockable ? !((ILockable)fConnection).isLocked() : true;
        fComboLineWidth.setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fConnection;
    }
}
