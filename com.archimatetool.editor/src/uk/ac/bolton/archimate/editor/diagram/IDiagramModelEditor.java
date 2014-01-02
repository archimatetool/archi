/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ui.IEditorPart;

import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * IDiagramModelEditor
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramModelEditor extends IEditorPart {
    /**
     * @return The model
     */
    IDiagramModel getModel();
    
    /**
     * @return The Graphical Viewer
     */
    GraphicalViewer getGraphicalViewer();
}
