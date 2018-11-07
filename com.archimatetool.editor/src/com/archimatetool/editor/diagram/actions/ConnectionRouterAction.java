/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.Disposable;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.ConnectionRouterTypeCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;



/**
 * Connection Router Action
 * 
 * @author Phillip Beauvoir
 */
public abstract class ConnectionRouterAction extends Action implements Disposable {
    
    public static String CONNECTION_ROUTER_BENDPONT = Messages.ConnectionRouterAction_0;
    //public static String CONNECTION_ROUTER_SHORTEST_PATH = Messages.ConnectionRouterAction_1;
    public static String CONNECTION_ROUTER_MANHATTAN = Messages.ConnectionRouterAction_2;
    
    public static List<Integer> CONNECTION_ROUTER_TYPES = Arrays.asList(new Integer[] {
            IDiagramModel.CONNECTION_ROUTER_BENDPOINT,
            IDiagramModel.CONNECTION_ROUTER_MANHATTAN
    });

    private IWorkbenchPart part;
    private IDiagramModel diagramModel;
    
    /*
     * Adapter to listen to change made from Properties View
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE) {
                update();
            }
        }
    };
    
    public ConnectionRouterAction(IWorkbenchPart part) {
        super(null, AS_RADIO_BUTTON);
        this.part = part;
        diagramModel = part.getAdapter(IDiagramModel.class);
        diagramModel.eAdapters().add(eAdapter);
        update();
    }

    @Override
    public void run() {
        if(isChecked()) {
            CommandStack stack = part.getAdapter(CommandStack.class);
            stack.execute(new ConnectionRouterTypeCommand(diagramModel, getType()));
        }
    }
    
    protected void update() {
        int type = diagramModel.getConnectionRouterType();
        
        if(CONNECTION_ROUTER_TYPES.indexOf(type) == -1) {
            type = 0;
        }

        setChecked(type == getType());
    }
    
    protected abstract int getType();
    
    @Override
    public void dispose() {
        diagramModel.eAdapters().remove(eAdapter);
    }
    
    
    /*
     * Bend Point
     */
    public static class BendPointConnectionRouterAction extends ConnectionRouterAction  {
        public static String ID = "BendPointConnectionAction"; //$NON-NLS-1$
        
        public BendPointConnectionRouterAction(IWorkbenchPart part) {
            super(part);
            setId(ID);
            setText(CONNECTION_ROUTER_BENDPONT);
        }

        @Override
        public int getType() {
            return IDiagramModel.CONNECTION_ROUTER_BENDPOINT;
        }
    };
    
    /*
     * Shortest Path
     * Doesn't work with Connection to Connection
     */
//    public static class ShortestPathConnectionRouterAction extends ConnectionRouterAction  {
//        public static String ID = "ShortestPathConnectionRouterAction"; //$NON-NLS-1$
//        
//        public ShortestPathConnectionRouterAction(IWorkbenchPart part) {
//            super(part);
//            setId(ID);
//            setText(CONNECTION_ROUTER_SHORTEST_PATH);
//        }
//
//        @Override
//        public int getType() {
//            return IDiagramModel.CONNECTION_ROUTER_SHORTEST_PATH;
//        }
//    };
    
    /*
     * Manhattan
     */
    public static class ManhattanConnectionRouterAction extends ConnectionRouterAction  {
        public static String ID = "ManhattanConnectionAction"; //$NON-NLS-1$
        
        public ManhattanConnectionRouterAction(IWorkbenchPart part) {
            super(part);
            setId(ID);
            setText(CONNECTION_ROUTER_MANHATTAN);
        }

        @Override
        public int getType() {
            return IDiagramModel.CONNECTION_ROUTER_MANHATTAN;
        }
    };
}
