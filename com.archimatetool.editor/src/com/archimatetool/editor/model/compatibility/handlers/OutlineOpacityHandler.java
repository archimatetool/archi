/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ICompatibilityHandler;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelObject;



/**
 *  In ModelVersion number 4.6.0 we introduced outline opacity on figures in addition to fill opacity.
 *  This should default to the figure's fill opacity (which was introduced in ModelVersion 4.0.1)
 * 
 * @author Phillip Beauvoir
 */
public class OutlineOpacityHandler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        // Check all widths and heights
        if(isVersion(model)) {
            setDefaultOutlineOpacity(model);
        }
    }
    
    boolean isVersion(IArchimateModel model) {
        String version = model.getVersion();
        return version != null && (StringUtils.compareVersionNumbers(version, "4.0.1") == 0 || //$NON-NLS-1$
                StringUtils.compareVersionNumbers(version, "4.4.0") == 0); //$NON-NLS-1$
    }

    void setDefaultOutlineOpacity(IArchimateModel model) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelObject) {
                IDiagramModelObject dmo = (IDiagramModelObject)eObject;
                dmo.setLineAlpha(dmo.getAlpha());
            }
        }
    }
    
}
