/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.zest;

import uk.ac.bolton.archimate.editor.views.IModelView;


/**
 * Interface for Zest View
 * 
 * @author Phillip Beauvoir
 */
public interface IZestView extends IModelView {
    String ID = ArchimateZestPlugin.PLUGIN_ID + ".zestView"; //$NON-NLS-1$
    String HELP_ID = "uk.ac.bolton.archimate.help.zestViewHelp"; //$NON-NLS-1$
    String NAME = Messages.IZestView_0;
}