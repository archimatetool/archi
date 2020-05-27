/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.viewpoints.ViewpointManager;

/**
 * Viewpoint renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ViewpointRenderer extends AbstractTextRenderer {
    
    private static final String VIEWPOINT = "${viewpoint}";

    @Override
    public String render(IArchimateModelObject object, String text) {
        String replacement = "";
        
        // Object is a digram model component or diagram model that is an ArchiMate diagram model
        if(object instanceof IDiagramModelComponent && ((IDiagramModelComponent)object).getDiagramModel() instanceof IArchimateDiagramModel) {
            replacement = ViewpointManager.INSTANCE.getViewpoint(((IArchimateDiagramModel)((IDiagramModelComponent)object).getDiagramModel()).getViewpoint()).getName();
        }
        
        return text.replace(VIEWPOINT, replacement);
    }
}