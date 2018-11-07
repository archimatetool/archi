/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.ICreationFactory;



/**
 * Diagram Model Factory for creating objects from the Palette in the Diagram Editor
 * This class alos acts as a placeholder for other useful information when creating a connection
 * 
 * @author Phillip Beauvoir
 */
public class MagicConnectionModelFactory implements ICreationFactory {
    
    private EClass fRelationshipTemplate;
    private EClass fElementTemplate;
    
    private boolean fSwapSourceAndTarget;
    
    @Override
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
    
    @Override
    public Object getNewObject() {
        return new ArchimateDiagramModelFactory(fRelationshipTemplate).getNewObject();
    }

    @Override
    public Object getObjectType() {
        return fRelationshipTemplate;
    }
    
    public void clear() {
        fRelationshipTemplate = null;
        fElementTemplate = null;
    }
    
    /**
     * @return True if we should swap the source and target elements when creating a connection
     */
    public boolean swapSourceAndTarget() {
        return fSwapSourceAndTarget;
    }
    
    /**
     * Set whether we should swap the source and target elements when creating a connection
     * @param set If true then we should swap the source and target elements when creating a connection
     */
    public void setSwapSourceAndTarget(boolean set) {
        fSwapSourceAndTarget = set;
    }
}
