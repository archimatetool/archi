/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFontAttribute;



/**
 * Text Alignment Command
 *
 * @author Phillip Beauvoir
 */
public class TextPositionCommand extends EObjectFeatureCommand {
    
    public TextPositionCommand(IFontAttribute object, int value) {
        super(Messages.TextPositionCommand_0, object, IArchimatePackage.Literals.FONT_ATTRIBUTE__TEXT_POSITION, value);
    }
}