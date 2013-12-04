/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ILineObject;


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