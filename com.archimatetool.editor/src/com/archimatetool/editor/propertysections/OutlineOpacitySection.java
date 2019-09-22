/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for Outline Alpha Opacity
 * 
 * @author Phillip Beauvoir
 */
public class OutlineOpacitySection extends OpacitySection {
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelObject) && shouldExposeFeature((EObject)object, IDiagramModelObject.FEATURE_LINE_ALPHA);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    @Override
    protected String getLabelString() {
        return Messages.OutlineOpacitySection_0;
    }

    @Override
    protected Command getCommand(IDiagramModelObject dmo, int newValue) {
        return new DiagramModelObjectOutlineAlphaCommand(dmo, newValue);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_LINE_ALPHA)) {
            update();
        }
        else {
            super.notifyChanged(msg);
        }
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
        
        fSpinner.setSelection(lastSelected.getLineAlpha());

        fSpinner.setEnabled(!isLocked(lastSelected));
    }

}
