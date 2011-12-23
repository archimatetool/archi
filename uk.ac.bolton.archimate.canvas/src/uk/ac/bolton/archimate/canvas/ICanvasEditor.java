/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas;

import uk.ac.bolton.archimate.editor.diagram.IDiagramModelEditor;


/**
 * Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public interface ICanvasEditor extends IDiagramModelEditor {
    String ID = CanvasEditorPlugin.PLUGIN_ID + ".canvasEditor"; //$NON-NLS-1$
    String HELP_ID = "uk.ac.bolton.archimate.help.canvasEditorHelp"; //$NON-NLS-1$
}
