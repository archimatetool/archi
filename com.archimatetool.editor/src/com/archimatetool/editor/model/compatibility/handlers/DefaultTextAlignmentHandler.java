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
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IGrouping;
import com.archimatetool.model.ITextAlignment;



/**
 *  Convert Text Alignments that had a default of center to left alignment < 4.4.0
 * 
 * @author Phillip Beauvoir
 */
public class DefaultTextAlignmentHandler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        if(isVersion(model)) {
            convertTextPosition(model);
        }
    }
    
    public boolean isVersion(IArchimateModel model) {
        String version = model.getVersion();
        return version != null && StringUtils.compareVersionNumbers(version, "4.4.0") < 0; //$NON-NLS-1$
    }
    
    private void convertTextPosition(IArchimateModel model) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            if((eObject instanceof IDiagramModelArchimateObject && ((IDiagramModelArchimateObject)eObject).getArchimateConcept() instanceof IGrouping)
                    || (eObject instanceof IDiagramModelGroup)) {
                
                if(((IDiagramModelObject)eObject).getTextAlignment() == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                    ((IDiagramModelObject)eObject).setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
                }
                
            }
        }
    }
}
