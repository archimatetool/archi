/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import com.archimatetool.model.IArchimateConcept;


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
     * Deprecated use selectObjects(Object[] objects)
     * @param archimateConcepts
     */
    @Deprecated
    void selectArchimateConcepts(IArchimateConcept[] archimateConcepts);
}
