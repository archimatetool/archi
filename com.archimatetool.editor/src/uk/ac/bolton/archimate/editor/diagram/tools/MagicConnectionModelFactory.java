/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.tools;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.ui.IEditorPart;

import uk.ac.bolton.archimate.editor.diagram.ArchimateDiagramModelFactory;
import uk.ac.bolton.archimate.editor.diagram.ICreationFactory;
import uk.ac.bolton.archimate.editor.diagram.IArchimateDiagramEditor;


/**
 * Diagram Model Factory for creating objects from the Palette in the Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class MagicConnectionModelFactory implements ICreationFactory {
    
    private EClass fRelationshipTemplate;
    private EClass fElementTemplate;
    
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof IArchimateDiagramEditor;
    }
    
    public void setRelationshipType(EClass type) {
        fRelationshipTemplate = type;
    }
    
    public void setElementType(EClass type) {
        fElementTemplate = type;
    }
    
    public EClass getRelationshipType() {
        return fRelationshipTemplate;
    }
    
    public EClass getElementType() {
        return fElementTemplate;
    }
    
    public Object getNewObject() {
        return new ArchimateDiagramModelFactory(fRelationshipTemplate).getNewObject();
    }

    public Object getObjectType() {
        return fRelationshipTemplate;
    }
    
    public void clear() {
        fRelationshipTemplate = null;
        fElementTemplate = null;
    }
}
