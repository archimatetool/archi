/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModel;


/**
 * IDiagramEditor
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateDiagramEditor extends IDiagramModelEditor {
    String ID = "com.archimatetool.editor.diagramEditor"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.diagramEditorHelp"; //$NON-NLS-1$
    String PALETTE_HELP_ID = "com.archimatetool.help.diagramEditorPaletteHelp"; //$NON-NLS-1$
    
    /**
     * Select the graphical objects wrapping the Archimate concepts
     * @param archimateConcepts
     */
    void selectArchimateConcepts(IArchimateConcept[] archimateConcepts);
    
    /**
     * Select the graphical objects wrapping Diagrams (Archimate, Sketch, Canvas, ...)
     * @param archimateDiagrams
     */
    void selectDiagramObject(IDiagramModel[] diagramModels);
}
