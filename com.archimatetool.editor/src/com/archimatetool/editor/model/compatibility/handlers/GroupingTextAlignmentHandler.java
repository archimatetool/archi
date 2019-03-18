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
import com.archimatetool.model.IGrouping;
import com.archimatetool.model.ITextAlignment;



/**
 *  Convert Grouping Text Alignments < 4.4.0
 * 
 * @author Phillip Beauvoir
 */
public class GroupingTextAlignmentHandler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        if(isVersion(model)) {
            convertGroupingTextPosition(model);
        }
    }
    
    public boolean isVersion(IArchimateModel model) {
        String version = model.getVersion();
        return version != null && StringUtils.compareVersionNumbers(version, "4.4.0") < 0; //$NON-NLS-1$
    }
    
    private void convertGroupingTextPosition(IArchimateModel model) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateObject && ((IDiagramModelArchimateObject)eObject).getArchimateConcept() instanceof IGrouping) {
                int textAlignment = ((IDiagramModelArchimateObject)eObject).getTextAlignment();
                if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                    ((IDiagramModelArchimateObject)eObject).setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
                }
            }
        }
    }
}
