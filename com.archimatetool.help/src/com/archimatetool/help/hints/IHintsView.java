/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.hints;

import org.eclipse.ui.IViewPart;

import com.archimatetool.help.ArchiHelpPlugin;


/**
 * Interface for Hints View
 * 
 * @author Phillip Beauvoir
 */
public interface IHintsView extends IViewPart {

    String ID = "com.archimatetool.help.hintsView"; //$NON-NLS-1$
    String HELP_ID = ArchiHelpPlugin.PLUGIN_ID + ".hintsViewHelp"; //$NON-NLS-1$
    String NAME = Messages.IHintsView_0;
    
    String EXTENSION_POINT_ID = "com.archimatetool.help.hints"; //$NON-NLS-1$
}