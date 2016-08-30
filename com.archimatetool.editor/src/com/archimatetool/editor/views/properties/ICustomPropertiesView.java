/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.properties;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;

import com.archimatetool.editor.ui.IArchiImages;


/**
 * Interface for Properties View
 * 
 * @author Phillip Beauvoir
 */
public interface ICustomPropertiesView extends IViewPart {
    String ID = "com.archimatetool.editor.propertiesView"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.propertiesViewHelp"; //$NON-NLS-1$
    String NAME = Messages.ICustomPropertiesView_0;
    ImageDescriptor IMAGE_DESCRIPTOR = IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON);
}