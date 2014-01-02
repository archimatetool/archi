/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.navigator;

import org.eclipse.jface.resource.ImageDescriptor;

import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.views.IModelView;


/**
 * Interface for Navigator View
 * 
 * @author Phillip Beauvoir
 */
public interface INavigatorView extends IModelView {

    String ID = "uk.ac.bolton.archimate.editor.navigatorView"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.navigatorViewHelp"; //$NON-NLS-1$
    String NAME = Messages.INavigatorView_0;
    ImageDescriptor IMAGE_DESCRIPTOR = IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NAVIGATOR_16);
    
}