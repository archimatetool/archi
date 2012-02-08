/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.model.IArchimateElement;


/**
 * IDiagramEditor
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateDiagramEditor extends IDiagramModelEditor {
    String ID = ArchimateEditorPlugin.PLUGIN_ID + ".diagramEditor"; //$NON-NLS-1$
    String HELP_ID = "uk.ac.bolton.archimate.help.diagramEditorHelp"; //$NON-NLS-1$
    String PALETTE_HELP_ID = "uk.ac.bolton.archimate.help.diagramEditorPaletteHelp"; //$NON-NLS-1$
    
    String PROPERTY_SHOW_STRUCTURAL_CHAIN = "_prop_showStructuralChain"; //$NON-NLS-1$
    
    /**
     * Select the graphical objects wrapping the Archimate elements
     * @param elements
     */
    void selectElements(IArchimateElement[] elements);
}
