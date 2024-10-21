/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Line Style Command
 *
 * @author Phillip Beauvoir
 */
public class DiagramModelObjectLineStyleCommand extends FeatureCommand {
    
    public DiagramModelObjectLineStyleCommand(IDiagramModelObject object, int style) {
        super("Change Line Style", object,
                IDiagramModelObject.FEATURE_LINE_STYLE, style, IDiagramModelObject.FEATURE_LINE_STYLE_DEFAULT);
    }
}