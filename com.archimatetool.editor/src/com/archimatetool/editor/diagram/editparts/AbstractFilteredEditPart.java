/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Abstract Filtered Edit Part
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractFilteredEditPart extends AbstractGraphicalEditPart {
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener;

    @Override
    public void activate() {
        if(!isActive()) {
            super.activate();
            
            // Listen to Model
            addECoreAdapter();
            
            // Listen to Application Prefs changes
            prefsListener = this::applicationPreferencesChanged;
            ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);
        }
    }

    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            
            // Remove Listener to changes in Model Object
            removeECoreAdapter();

            // Remove Prefs listener
            ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
        }
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
     * @return The ECore Adapter to listen to model changes
     */
    protected abstract Adapter getECoreAdapter();

    /**
     * An Application Preference Property change event occurred
     * @param event
     */
    protected abstract void applicationPreferencesChanged(PropertyChangeEvent event);
    
    @Override
    public EObject getModel() {
        return (EObject)super.getModel();
    }
    
   @Override
    protected List<?> getModelChildren() {
        return getFilteredModelChildren();
    }
    
    protected List<?> getFilteredModelChildren() {
        if(getModel() instanceof IDiagramModelContainer) {
            List<IDiagramModelObject> originalList = ((IDiagramModelContainer)getModel()).getChildren();
            
            IChildEditPartFilter[] filters = getRootEditPartFilterProvider().getEditPartFilters(IChildEditPartFilter.class);
            if(filters != null) {
                List<IDiagramModelObject> filteredList = new ArrayList<IDiagramModelObject>();
                
                for(IDiagramModelObject object : originalList) {
                    boolean add = true;
                    
                    for(IChildEditPartFilter filter : filters) {
                        add = filter.isChildElementVisible(this, object);
                        if(!add) { // no point in trying the next filter
                            break;
                        }
                    }
                    
                    if(add) {
                        filteredList.add(object);
                    }
                }
                
                return filteredList;
            }
            
            return originalList;
        }
        
        return Collections.EMPTY_LIST;
    }

    protected IEditPartFilterProvider getRootEditPartFilterProvider() {
        if(getRoot() != null && getRoot().getContents() instanceof IEditPartFilterProvider) {
            return (IEditPartFilterProvider)getRoot().getContents();
        }
        
        return null;
    }
}