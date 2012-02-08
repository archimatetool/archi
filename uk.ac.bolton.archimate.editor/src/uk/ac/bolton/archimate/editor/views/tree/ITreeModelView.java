/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

import org.eclipse.jface.resource.ImageDescriptor;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.views.IModelSelectionView;
import uk.ac.bolton.archimate.editor.views.IModelView;

/**
 * Interface for Tree Model View
 * 
 * @author Phillip Beauvoir
 */
public interface ITreeModelView extends IModelSelectionView, IModelView {

    String ID = ArchimateEditorPlugin.PLUGIN_ID + ".treeModelView"; //$NON-NLS-1$
    String HELP_ID = "uk.ac.bolton.archimate.help.treeModelViewHelp"; //$NON-NLS-1$
    String NAME = Messages.ITreeModelView_0;
    ImageDescriptor IMAGE_DESCRIPTOR = IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MODELS_16);
}