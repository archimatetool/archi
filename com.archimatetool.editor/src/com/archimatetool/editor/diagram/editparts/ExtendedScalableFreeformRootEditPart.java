/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;

/**
 * Extension of ScalableFreeformRootEditPart so we can hack into it
 * 
 * @author Phillip Beauvoir
 */
public class ExtendedScalableFreeformRootEditPart extends ScalableFreeformRootEditPart {
    
    public ExtendedScalableFreeformRootEditPart() {
        // Don't use scaled graphics
        super(false);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == AutoexposeHelper.class) {
            return new ExtendedViewportAutoexposeHelper(this, new Insets(50), false);
        }
        return super.getAdapter(adapter);
    }
}
