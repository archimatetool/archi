/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import com.archimatetool.model.IArchimateElement;


/**
 * IDiagramEditor
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateDiagramEditor extends IDiagramModelEditor {
    String ID = "uk.ac.bolton.archimate.editor.diagramEditor"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.diagramEditorHelp"; //$NON-NLS-1$
    String PALETTE_HELP_ID = "com.archimatetool.help.diagramEditorPaletteHelp"; //$NON-NLS-1$
    
    String PROPERTY_SHOW_STRUCTURAL_CHAIN = "_prop_showStructuralChain"; //$NON-NLS-1$
    
    /**
     * Select the graphical objects wrapping the Archimate elements
     * @param elements
     */
    void selectElements(IArchimateElement[] elements);
}
