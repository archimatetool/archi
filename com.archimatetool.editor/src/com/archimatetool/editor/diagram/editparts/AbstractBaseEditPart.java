/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Abstract Base Edit Part
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractBaseEditPart extends AbstractFilteredEditPart {
    
    // Class for new Figure
    protected Class<?> figureClass;
    
    protected AbstractBaseEditPart() {
    }
    
    protected AbstractBaseEditPart(Class<?> figureClass) {
        assert(IDiagramModelObjectFigure.class.isAssignableFrom(figureClass));
        this.figureClass = figureClass;
    }

    @Override
    protected IFigure createFigure() {
        /*
         * Create a Figure from the given class
         */
        IDiagramModelObjectFigure figure = null;
        
        if(figureClass != null) {
            try {
                figure = (IDiagramModelObjectFigure)figureClass.getDeclaredConstructor().newInstance();
                figure.setDiagramModelObject(getModel());
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return figure;
    }

    @Override
    public IDiagramModelObject getModel() {
        return (IDiagramModelObject)super.getModel();
    }
    
    @Override
    public IDiagramModelObjectFigure getFigure() {
        return (IDiagramModelObjectFigure)super.getFigure();
    }

    /**
     * Application User Preferences were changed
     * @param event
     */
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Default font or colour preferences changed
        if(IPreferenceConstants.DEFAULT_VIEW_FONT.equals(event.getProperty()) ||
                event.getProperty().startsWith(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX) ||
                event.getProperty().equals(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR) ||
                event.getProperty().startsWith(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR)) {
            
            refreshFigure();
        }
    }
    
    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            
            // Dispose of figure
            getFigure().dispose();
        }
    }
    
    @Override
    protected void refreshVisuals() {
        refreshBounds();
        refreshFigure();
    }
    
    /**
     * Update any Edit Parts that may need changing as a result of for example locking an Edit Part
     */
    public void updateEditPolicies() {
    }
    
    /**
     * Refresh the Bounds
     */
    protected void refreshBounds() {
        /*
         * We need to set the bounds in the LayoutManager.
         * Tells the parent part that this part and its figure are to be constrained to the given rectangle.
         */ 
        GraphicalEditPart parentEditPart = (GraphicalEditPart)getParent();

        IDiagramModelObject object = getModel();
        Rectangle bounds = new Rectangle(object.getBounds().getX(), object.getBounds().getY(),
                object.getBounds().getWidth(), object.getBounds().getHeight());
        
        if(parentEditPart.getFigure().getLayoutManager() instanceof XYLayout) {
            parentEditPart.setLayoutConstraint(this, getFigure(), bounds);
        }
        // Content Pane Figure needs laying out
        else if(parentEditPart.getContentPane().getLayoutManager() instanceof XYLayout) {
            parentEditPart.getContentPane().setConstraint(getFigure(), bounds);
        }
    }
    
    /**
     * Refresh the figure
     */
    protected void refreshFigure() {
    }
    
    /**
     * Refresh this figure and all child figures
     */
    protected void refreshChildrenFigures() {
        // Refresh this
        refreshFigure();
        
        // Connections
        for(Object editPart : getSourceConnections()) {
            if(editPart instanceof DiagramConnectionEditPart) {
                ((DiagramConnectionEditPart)editPart).refreshVisuals();
            }
        }
        
        // Children
        for(Object editPart : getChildren()) {
            if(editPart instanceof AbstractBaseEditPart) {
                ((AbstractBaseEditPart)editPart).refreshChildrenFigures();
            }
        }
    }
    
    /**
     * @return True if this EditPart's Viewer is in Full Screen Mode
     */
    public boolean isInFullScreenMode() {
        return getViewer() != null && getViewer().getProperty("full_screen") != null; //$NON-NLS-1$
    }
    
    /**
     * @return True if this EditPart is locked
     */
    public boolean isLocked() {
        return getModel() instanceof ILockable && ((ILockable)getModel()).isLocked();
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
     * If the model object is locked don't display a drag tracker
     */
    @Override
    public DragTracker getDragTracker(Request request) {
        return isLocked() ? new SelectEditPartTracker(this) : super.getDragTracker(request);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(getModel() != null && adapter.isInstance(getModel())) {
            return getModel();
        }
        return super.getAdapter(adapter);
    }

}