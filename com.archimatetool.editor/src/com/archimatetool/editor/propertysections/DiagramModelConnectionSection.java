/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
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
public class DiagramModelConnectionSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.diagramModelSection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModel;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModel.class;
        }
    }

    private Combo fComboRouterType;
    
    private String[] comboItems = {
            ConnectionRouterAction.CONNECTION_ROUTER_BENDPONT,
            // Doesn't work with C2C
            //ConnectionRouterAction.CONNECTION_ROUTER_SHORTEST_PATH,
            ConnectionRouterAction.CONNECTION_ROUTER_MANHATTAN
    };
    
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
        getWidgetFactory().adapt(fComboRouterType, true, true);
        fComboRouterType.setItems(comboItems);
        
        fComboRouterType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject dm : getEObjects()) {
                    if(isAlive(dm)) {
                        Command cmd = new ConnectionRouterTypeCommand((IDiagramModel)dm,
                                ConnectionRouterAction.CONNECTION_ROUTER_TYPES.get(fComboRouterType.getSelectionIndex()));
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
        
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboRouterType.setLayoutData(gd);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();

            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        int type = ((IDiagramModel)getFirstSelectedObject()).getConnectionRouterType();
        
        if(ConnectionRouterAction.CONNECTION_ROUTER_TYPES.indexOf(type) == -1) {
            type = 0;
        }
        
        fComboRouterType.select(ConnectionRouterAction.CONNECTION_ROUTER_TYPES.indexOf(type));
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
