/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.sketch;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.diagram.IDiagramModelEditor;


/**
 * ISketchEditor
 * 
 * @author Phillip Beauvoir
 */
public interface ISketchEditor extends IDiagramModelEditor {
    String ID = ArchimateEditorPlugin.PLUGIN_ID + ".sketchEditor"; //$NON-NLS-1$
    String HELP_ID = "uk.ac.bolton.archimate.help.sketchEditorHelp"; //$NON-NLS-1$
    
    void updateBackgroundImage();
    
    // Backgrounds
    String[] BACKGROUNDS = {
            Messages.ISketchEditor_0,
            Messages.ISketchEditor_1,
            Messages.ISketchEditor_2
    };
}
