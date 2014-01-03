/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import com.archimatetool.editor.diagram.IDiagramModelEditor;


/**
 * ISketchEditor
 * 
 * @author Phillip Beauvoir
 */
public interface ISketchEditor extends IDiagramModelEditor {
    String ID = "uk.ac.bolton.archimate.editor.sketchEditor"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.sketchEditorHelp"; //$NON-NLS-1$
    
    void updateBackgroundImage();
    
    // Backgrounds
    String[] BACKGROUNDS = {
            Messages.ISketchEditor_0,
            Messages.ISketchEditor_1,
            Messages.ISketchEditor_2
    };
}
