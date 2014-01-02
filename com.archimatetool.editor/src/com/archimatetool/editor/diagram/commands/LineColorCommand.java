/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILineObject;



/**
 * Line Color Command
 *
 * @author Phillip Beauvoir
 */
public class LineColorCommand extends EObjectFeatureCommand {
    
    public LineColorCommand(ILineObject lineObject, String rgb) {
        super(Messages.LineColorCommand_0, lineObject, IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR, rgb);
    }
}