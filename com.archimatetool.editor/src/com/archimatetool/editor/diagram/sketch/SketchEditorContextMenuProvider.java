/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;

import com.archimatetool.editor.diagram.AbstractDiagramEditorContextMenuProvider;


/**
 * Provides a context menu.
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditorContextMenuProvider extends AbstractDiagramEditorContextMenuProvider {

    public static final String ID = "SketchEditorContextMenuProvider"; //$NON-NLS-1$
    
    /**
     * Creates a new ContextMenuProvider assoicated with the given viewer
     * and action registry.
     * 
     * @param viewer the viewer
     * @param registry the action registry
     */
    public SketchEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
    }

}