/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.diagram.directedit.MultiLineTextDirectEditManager;
import com.archimatetool.editor.diagram.figures.connections.IDiagramConnectionFigure;
import com.archimatetool.editor.diagram.policies.ManualBendpointEditPolicy;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.util.LightweightEContentAdapter;



/**
 * Diagram Connection EditPart
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionEditPart extends AbstractConnectionEditPart {

    private Adapter adapter = new LightweightEContentAdapter(this::eCoreChanged, IFeature.class);
    
    /**
     * Listen to default font change in Prefs
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
            applicationPreferencesChanged(event);
        }
    };
    
    /**
     * Figure Listener 
     */
    private PropertyChangeListener figureListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Connection Router change
            String property = evt.getPropertyName();
            if(Connection.PROPERTY_CONNECTION_ROUTER.equals(property)){
                refreshBendpoints();
                refreshBendpointEditPolicy();
            }
        }
    };
    
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    
    // Class for new Figure
    protected Class<?> figureClass;
    
    public DiagramConnectionEditPart() {
    }
    
    public DiagramConnectionEditPart(Class<?> figureClass) {
        assert(IDiagramConnectionFigure.class.isAssignableFrom(figureClass));
        this.figureClass = figureClass;
    }
    
    @Override
    protected IFigure createFigure() {
        /*
         * Create a Figure from the given class
         */
        IDiagramConnectionFigure figure = null;
        
        if(figureClass != null) {
            try {
                figure = (IDiagramConnectionFigure)figureClass.getDeclaredConstructor().newInstance();
                figure.setModelConnection(getModel());
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return figure;
    }

    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        switch(msg.getEventType()) {
            case Notification.ADD:
            case Notification.ADD_MANY:
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
            case Notification.MOVE:
            case Notification.SET:
                if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                    createEditPolicies();
                }
                else {
                    refreshVisuals();
                }
                break;

            default:
                break;
        }
    }
    
    protected Adapter getECoreAdapter() {
        return adapter;
    }
    
    /**
     * Add any Ecore Adapters
     */
    protected void addECoreAdapter() {
        if(getECoreAdapter() != null) {
            getModel().eAdapters().add(getECoreAdapter());
        }
    }
    
    /**
     * Remove any Ecore Adapters
     */
    protected void removeECoreAdapter() {
        if(getECoreAdapter() != null) {
            getModel().eAdapters().remove(getECoreAdapter());
        }
    }
    
    /**
     * Application User Preferences were changed
     * @param event
     */
    protected void applicationPreferencesChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
        String property = event.getProperty();
        
        if(IPreferenceConstants.DEFAULT_VIEW_FONT.equals(property)) {
            getFigure().refreshVisuals();
        }
        else if(property.equals(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR)) {
            getFigure().refreshVisuals();
        }
        else if(IPreferenceConstants.USE_LINE_CURVES.equals(property)) {
            getFigure().repaint();
        }
        else if(IPreferenceConstants.USE_LINE_JUMPS.equals(property)) {
            getFigure().repaint();
        }
        else if(IPreferenceConstants.CONNECTION_LABEL_STRATEGY.equals(property)) {
            getFigure().refreshVisuals();
        }
        else if(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS.equals(property)) {
            getFigure().refreshVisuals();
        }
    }
    
    @Override
    public IDiagramModelConnection getModel() {
        return (IDiagramModelConnection)super.getModel();
    }

    @Override
    public void activate() {
        if(!isActive()) {
            super.activate();
            
            // Listen to changes in Diagram Model Object
            addECoreAdapter();
            
            // Listen to Prefs changes to set default Font
            ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);
        }
    }
    
    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            
            // Remove Listener to changes in Diagram Model Object
            removeECoreAdapter();
            
            ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
        }
    }
    
    @Override
    public void activateFigure(){
        super.activateFigure();
        // Once the figure has been added to the ConnectionLayer, start listening for its router to change.
        getFigure().addPropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, figureListener);
    }
    
    @Override
    public void deactivateFigure(){
        // Remove listener
        getFigure().removePropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, figureListener);
        super.deactivateFigure();
    }
    
    @Override
    public IDiagramConnectionFigure getFigure() {
        return (IDiagramConnectionFigure)super.getFigure();
    }

    @Override
    public void performRequest(Request request) {
        // REQ_DIRECT_EDIT is Single-click when already selected or a Rename command
        // REQ_OPEN is Double-click
        if(request.getType() == RequestConstants.REQ_DIRECT_EDIT || request.getType() == RequestConstants.REQ_OPEN) {
            if(request instanceof LocationRequest) {
                // Edit the text control if we clicked on it
                if(!(getModel() instanceof ILockable && ((ILockable)getModel()).isLocked())
                            && getFigure().didClickConnectionLabel(((LocationRequest)request).getLocation().getCopy())) {
                    createDirectEditManager().show();
                }
                // Else open Properties View on double-click
                else if(request.getType() == RequestConstants.REQ_OPEN){
                    showPropertiesView();
                }
            }
            else {
                createDirectEditManager().show();
            }
        }
    }
    
    protected DirectEditManager createDirectEditManager() {
        //return new LabelDirectEditManager(this, getFigure().getConnectionLabel(), getModel().getName());
        return new MultiLineTextDirectEditManager(this, true, getFigure().getConnectionLabel());
    }
    
    @Override
    protected void createEditPolicies() {
        // Selection handle edit policy. 
        // Makes the connection show a feedback, when selected by the user.
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, isLocked() ? null : new ConnectionEndpointEditPolicy());
        
        // Add a policy to handle directly editing the connection label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, isLocked() ? null : new ConnectionDirectEditTextPolicy());
        
        // Allows the removal of the connection model element
        installEditPolicy(EditPolicy.CONNECTION_ROLE, isLocked() ? null : new ConnectionEditPolicy() {
            @Override
            protected Command getDeleteCommand(GroupRequest request) {
                return DiagramCommandFactory.createDeleteDiagramConnectionCommand(getModel());
            }
        });
        
        // Add a policy for manual bendpoints
        refreshBendpointEditPolicy();
    }

    @Override
    protected void refreshVisuals() {
        // This will need to be updated when user changes source or target end-connection
        // as well as on Property change.
        getFigure().refreshVisuals();
        
        refreshBendpoints();
    }
    
    protected boolean isLocked() {
        return getModel() instanceof ILockable && ((ILockable)getModel()).isLocked();
    }
    
    /**
     * Updates the bendpoints, based on the model
     */
    protected void refreshBendpoints() {
        if(getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter) {
            return;
        }
        
        List<Bendpoint> figureConstraint = new ArrayList<Bendpoint>();
        
        EList<IDiagramModelBendpoint> bendpoints = getModel().getBendpoints();
        for(int i = 0; i < bendpoints.size(); i++) {
            IDiagramModelBendpoint bendpoint = bendpoints.get(i);
            
            RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
            Dimension dim1 = new Dimension(bendpoint.getStartX(), bendpoint.getStartY());
            Dimension dim2 = new Dimension(bendpoint.getEndX(), bendpoint.getEndY());
            rbp.setRelativeDimensions(dim1, dim2);
            rbp.setWeight((i + 1) / ((float)bendpoints.size() + 1));
            
            figureConstraint.add(rbp);
        }
        
        getConnectionFigure().setRoutingConstraint(figureConstraint);
    }
    
    protected void refreshBendpointEditPolicy() {
        if(isLocked()) {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, null);
        }
        // Doesn't work for Manhattan Router
        else if(getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter) {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, null);
        }
        else {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new ManualBendpointEditPolicy());
        }
    }
        
    /**
     * @return True if this EditPart's Viewer is in Full Screen Mode
     */
    protected boolean isInFullScreenMode() {
        return getViewer() != null && getViewer().getProperty("full_screen") != null; //$NON-NLS-1$
    }
    
    /**
     * Show the Properties View.
     * This will have no effect if the Viewer is in Full Screen Mode.
     */
    protected void showPropertiesView() {
        if(!isInFullScreenMode()) {
            ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, false);
        }
    }

    /**
     * @return The Zoom Manager from the Viewer
     */
    protected ZoomManager getZoomManager() {
        return (ZoomManager)getViewer().getProperty(ZoomManager.class.toString());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(getModel() != null && adapter.isInstance(getModel())) {
            return getModel();
        }
        return super.getAdapter(adapter);
    }
    
    /**
     * Direct Edit Policy
     */
    protected class ConnectionDirectEditTextPolicy extends DirectEditPolicy {

        public ConnectionDirectEditTextPolicy() {
        }

        @Override
        protected Command getDirectEditCommand(DirectEditRequest request) {
            String name = (String)request.getCellEditor().getValue();
            IDiagramModelConnection connection = getModel();
            return new EObjectFeatureCommand(NLS.bind(Messages.DiagramConnectionEditPart_0, connection.getName()),
                    connection, IArchimatePackage.Literals.NAMEABLE__NAME, name);
        }

        @Override
        protected void showCurrentEditValue(DirectEditRequest request) {
//          String value = (String)request.getCellEditor().getValue();
//          
//          if(getHostFigure() instanceof IArchimateConnectionFigure) {
//              ((IArchimateConnectionFigure)getHostFigure()).setConnectionText(value);
//          }
        }
    }

}
