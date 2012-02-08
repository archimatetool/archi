/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.properties;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;

/**
 * Interface for Properties View
 * 
 * @author Phillip Beauvoir
 */
public interface ICustomPropertiesView extends IViewPart {
    String ID = ArchimateEditorPlugin.PLUGIN_ID + ".propertiesView"; //$NON-NLS-1$
    String HELP_ID = "uk.ac.bolton.archimate.help.propertiesViewHelp"; //$NON-NLS-1$
    String NAME = Messages.ICustomPropertiesView_0;
    ImageDescriptor IMAGE_DESCRIPTOR = IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_PROPERTIES_VIEW_ICON);
}