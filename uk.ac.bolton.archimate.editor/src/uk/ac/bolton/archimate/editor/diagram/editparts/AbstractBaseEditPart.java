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
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IProperties;


/**
 * Abstract Base Edit Part
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractBaseEditPart extends AbstractGraphicalEditPart {
    
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
            
            // Listen to Prefs changes to set default Font
            Preferences.STORE.addPropertyChangeListener(prefsListener);
        }
    }

    // override deactivate to deregister with the model
    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            
            // Remove Listener to changes in Diagram Model Object
            removeECoreAdapter();
            
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