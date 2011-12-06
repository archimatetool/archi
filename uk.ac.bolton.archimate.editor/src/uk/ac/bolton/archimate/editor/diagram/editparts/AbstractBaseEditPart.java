/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.services.ViewManager;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.ILockable;
import uk.ac.bolton.archimate.model.IProperties;


/**
 * Abstract Base Edit Part
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractBaseEditPart extends AbstractFilteredEditPart {
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            applicationPreferencesChanged(event);
        }
    };
    
    /**
     * Application User Preferences were changed
     * @param event
     */
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        if(IPreferenceConstants.DEFAULT_VIEW_FONT.equals(event.getProperty())) {
            refreshFigure();
        }
    }
    
    @Override
    public void activate() {
        if(!isActive()) {
            super.activate();
            
            // Listen to changes in Diagram Model Object
            addECoreAdapter();
            
            // Listen to Prefs changes
            Preferences.STORE.addPropertyChangeListener(prefsListener);
        }
    }

    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            
            // Remove Listener to changes in Diagram Model Object
            removeECoreAdapter();
            
            // Remove Prefs listener
            Preferences.STORE.removePropertyChangeListener(prefsListener);

            // Dispose of figure
            if(getFigure() instanceof IDiagramModelObjectFigure) {
                ((IDiagramModelObjectFigure)getFigure()).dispose();
            }
        }
    }
    
    /**
     * Add any Ecore Adapters
     */
    protected void addECoreAdapter() {
        if(getECoreAdapter() != null) {
            ((IDiagramModelObject)getModel()).eAdapters().add(getECoreAdapter());
        }
    }
    
    /**
     * Remove any Ecore Adapters
     */
    protected void removeECoreAdapter() {
        if(getECoreAdapter() != null) {
            ((IDiagramModelObject)getModel()).eAdapters().remove(getECoreAdapter());
        }
    }
    
    /**
     * @return The ECore Adapter to listen to model changes
     */
    protected abstract Adapter getECoreAdapter();

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

        IDiagramModelObject object = (IDiagramModelObject)getModel();
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
        refreshFigure();
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
        return getViewer() != null && getViewer().getProperty("full_screen") != null;
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
            ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
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
        if(adapter == IDiagramModelObject.class) {
            return getModel();
        }
        if(adapter == IProperties.class && getModel() instanceof IProperties) {
            return getModel();
        }
        return super.getAdapter(adapter);
    }

}