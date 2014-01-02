/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.actions.ConnectionRouterAction;
import com.archimatetool.editor.diagram.commands.ConnectionRouterTypeCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;



/**
 * Property Section for a Diagram Model's Connection type
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelConnectionSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.diagramModelSection"; //$NON-NLS-1$

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Change made from Menu Action
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE) {
                refreshControls();
            }
        }
    };
    
    private Combo fComboRouterType;
    
    private String[] comboItems = {
            ConnectionRouterAction.CONNECTION_ROUTER_BENDPONT,
            ConnectionRouterAction.CONNECTION_ROUTER_SHORTEST_PATH,
            ConnectionRouterAction.CONNECTION_ROUTER_MANHATTAN
    };

    private IDiagramModel fDiagramModel;
    
    @Override
    protected void createControls(Composite parent) {
        createRouterTypeControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createRouterTypeControl(Composite parent) {
        // Label
        createLabel(parent, Messages.DiagramModelConnectionSection_0, ITabbedLayoutConstants.BIG_LABEL_WIDTH, SWT.CENTER);
        
        // Combo
        fComboRouterType = new Combo(parent, SWT.READ_ONLY);
        fComboRouterType.setItems(comboItems);
        fComboRouterType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new ConnectionRouterTypeCommand(fDiagramModel, fComboRouterType.getSelectionIndex()));
                    fIsExecutingCommand = false;
                }
            }
        });
        
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboRouterType.setLayoutData(gd);
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart) {
            fDiagramModel = (IDiagramModel)((EditPart)element).getModel();
        }
        else if(element instanceof IDiagramModel) {
            fDiagramModel = (IDiagramModel)element;
        }
        else {
            System.err.println("Section wants to display for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fComboRouterType.select(fDiagramModel.getConnectionRouterType());
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramModel;
    }
}
