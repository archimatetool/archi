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
 * Font Color Command
 *
 * @author Phillip Beauvoir
 */
public class FontColorCommand extends EObjectFeatureCommand {
    
    public FontColorCommand(IFontAttribute object, String rgb) {
        super(Messages.FontColorCommand_0, object, IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR, rgb);
    }
}