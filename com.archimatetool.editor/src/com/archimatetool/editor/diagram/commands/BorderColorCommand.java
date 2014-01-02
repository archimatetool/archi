/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBorderObject;



/**
 * Border Color Command
 *
 * @author Phillip Beauvoir
 */
public class BorderColorCommand extends EObjectFeatureCommand {
    
    public BorderColorCommand(IBorderObject borderObject, String rgb) {
        super(Messages.BorderColorCommand_0, borderObject, IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR, rgb);
    }
}