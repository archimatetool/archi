/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import org.eclipse.jface.resource.ImageDescriptor;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.views.IModelSelectionView;
import com.archimatetool.editor.views.IModelView;


/**
 * Interface for Tree Model View
 * 
 * @author Phillip Beauvoir
 */
public interface ITreeModelView extends IModelSelectionView, IModelView {

    String ID = "com.archimatetool.editor.treeModelView"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.treeModelViewHelp"; //$NON-NLS-1$
    String NAME = Messages.ITreeModelView_0;
    ImageDescriptor IMAGE_DESCRIPTOR = IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MODELS);
}