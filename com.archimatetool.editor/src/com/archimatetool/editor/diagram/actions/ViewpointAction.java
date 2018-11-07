/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.Disposable;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.viewpoints.IViewpoint;



/**
 * Viewpoint Action
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointAction extends Action implements Disposable {
    
    private IWorkbenchPart part;
    private IArchimateDiagramModel diagramModel;
    private IViewpoint viewPoint;
    
    /*
     * Adapter to listen to change from Model
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
                update();
            }
        }
    };
    
    public ViewpointAction(IWorkbenchPart part, IViewpoint viewPoint) {
        super(viewPoint.getName(), AS_RADIO_BUTTON);
        setId(viewPoint.toString());
        
        this.part = part;
        this.viewPoint = viewPoint;
        
        diagramModel = (IArchimateDiagramModel)part.getAdapter(IDiagramModel.class);
        diagramModel.eAdapters().add(eAdapter);
        update();
    }

    @Override
    public void run() {
        if(isChecked()) {
            CommandStack stack = part.getAdapter(CommandStack.class);
            stack.execute(new EObjectFeatureCommand(Messages.ViewpointAction_0,
                                diagramModel, IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT,
                                viewPoint.getID()));
        }
    }
    
    protected void update() {
        setChecked(diagramModel.getViewpoint().equals(viewPoint.getID()));
    }
    
    @Override
    public void dispose() {
        diagramModel.eAdapters().remove(eAdapter);
        viewPoint = null;
        part = null;
        diagramModel = null;
    }
}
