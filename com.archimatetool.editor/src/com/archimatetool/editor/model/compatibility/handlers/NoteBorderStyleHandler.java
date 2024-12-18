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
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Change a Note's border type from "None" to "Rectangle" and set its line style to "None" instead
 * 
 * @author Phillip Beauvoir
 */
public class NoteBorderStyleHandler implements ICompatibilityHandler {
    
    @Override
    public void fixCompatibility(Resource resource) throws CompatibilityHandlerException {
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        convertNoteBorders(model);
    }
    
    private void convertNoteBorders(IArchimateModel model) {
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            if((eObject instanceof IDiagramModelNote note && note.getBorderType() == IDiagramModelNote.BORDER_NONE)) {
                note.setBorderType(IDiagramModelNote.BORDER_RECTANGLE);
                note.setLineStyle(IDiagramModelObject.LINE_STYLE_NONE);
            }
        }
    }
}
