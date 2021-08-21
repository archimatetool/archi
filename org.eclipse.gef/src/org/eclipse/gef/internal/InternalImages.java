/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class InternalImages {

    public static final ImageDescriptor DESC_ZOOM_IN;
    public static final ImageDescriptor DESC_ZOOM_OUT;

    public static final ImageDescriptor DESC_MATCH_SIZE;
    public static final ImageDescriptor DESC_MATCH_WIDTH;
    public static final ImageDescriptor DESC_MATCH_HEIGHT;

    public static final ImageDescriptor DESC_MATCH_SIZE_DIS;
    public static final ImageDescriptor DESC_MATCH_WIDTH_DIS;
    public static final ImageDescriptor DESC_MATCH_HEIGHT_DIS;

    public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER;
    public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT;
    public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT;

    public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE;
    public static final ImageDescriptor DESC_VERT_ALIGN_TOP;
    public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM;

    public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER_DIS;
    public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT_DIS;
    public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT_DIS;

    public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE_DIS;
    public static final ImageDescriptor DESC_VERT_ALIGN_TOP_DIS;
    public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM_DIS;

    public static final ImageDescriptor ICON_MATCH_WIDTH;
    public static final ImageDescriptor ICON_MATCH_HEIGHT;

    public static final ImageDescriptor DESC_SEPARATOR;
    public static final ImageDescriptor DESC_FOLDER_OPEN;
    public static final ImageDescriptor DESC_FOLDER_CLOSED;

    public static final ImageDescriptor DESC_BOLD;
    public static final ImageDescriptor DESC_ITALIC;
    public static final ImageDescriptor DESC_UNDERLINE;

    public static final ImageDescriptor DESC_BLOCK_LTR;
    public static final ImageDescriptor DESC_BLOCK_RTL;

    public static final ImageDescriptor DESC_BLOCK_ALIGN_LEFT;
    public static final ImageDescriptor DESC_BLOCK_ALIGN_CENTER;
    public static final ImageDescriptor DESC_BLOCK_ALIGN_RIGHT;

    public static final ImageDescriptor DESC_PINNED;
    public static final ImageDescriptor DESC_UNPINNED;

    public static final ImageDescriptor DESC_PALETTE;

    /**
     * Can be used to access the cached pinned image by using
     * {@link #get(String)}.
     */
    public static final String IMG_PINNED = "icons/pinned.gif";//$NON-NLS-1$

    /**
     * Can be used to access the cached pinned image by using
     * {@link #get(String)}.
     */
    public static final String IMG_UNPINNED = "icons/unpinned.gif";//$NON-NLS-1$

    /**
     * Can be used to access the cached pinned image by using
     * {@link #get(String)}.
     */
    public static final String IMG_PALETTE = "icons/palette_view.gif";//$NON-NLS-1$

    static {
        DESC_BOLD = createDescriptor("icons/style_bold.gif"); //$NON-NLS-1$
        DESC_ITALIC = createDescriptor("icons/style_italic.gif"); //$NON-NLS-1$
        DESC_UNDERLINE = createDescriptor("icons/style_underline.gif"); //$NON-NLS-1$

        DESC_BLOCK_LTR = createDescriptor("icons/style_paragraph_ltr.gif"); //$NON-NLS-1$
        DESC_BLOCK_RTL = createDescriptor("icons/style_paragraph_rtl.gif"); //$NON-NLS-1$

        DESC_BLOCK_ALIGN_LEFT = createDescriptor("icons/style_paragraph_left.gif"); //$NON-NLS-1$
        DESC_BLOCK_ALIGN_CENTER = createDescriptor("icons/style_paragraph_center.gif"); //$NON-NLS-1$
        DESC_BLOCK_ALIGN_RIGHT = createDescriptor("icons/style_paragraph_right.gif"); //$NON-NLS-1$

        DESC_ZOOM_IN = createDescriptor("icons/zoom_in.gif"); //$NON-NLS-1$
        DESC_ZOOM_OUT = createDescriptor("icons/zoom_out.gif"); //$NON-NLS-1$

        DESC_MATCH_SIZE = createDescriptor("icons/matchsize.gif"); //$NON-NLS-1$
        DESC_MATCH_WIDTH = createDescriptor("icons/matchwidth.gif"); //$NON-NLS-1$
        DESC_MATCH_HEIGHT = createDescriptor("icons/matchheight.gif"); //$NON-NLS-1$

        DESC_MATCH_SIZE_DIS = createDescriptor("icons/matchsize_d.gif"); //$NON-NLS-1$
        DESC_MATCH_WIDTH_DIS = createDescriptor("icons/matchwidth_d.gif"); //$NON-NLS-1$
        DESC_MATCH_HEIGHT_DIS = createDescriptor("icons/matchheight_d.gif"); //$NON-NLS-1$

        DESC_VERT_ALIGN_BOTTOM = createDescriptor("icons/alignbottom.gif"); //$NON-NLS-1$
        DESC_HORZ_ALIGN_CENTER = createDescriptor("icons/aligncenter.gif"); //$NON-NLS-1$
        DESC_HORZ_ALIGN_LEFT = createDescriptor("icons/alignleft.gif"); //$NON-NLS-1$
        DESC_VERT_ALIGN_MIDDLE = createDescriptor("icons/alignmid.gif"); //$NON-NLS-1$
        DESC_HORZ_ALIGN_RIGHT = createDescriptor("icons/alignright.gif"); //$NON-NLS-1$
        DESC_VERT_ALIGN_TOP = createDescriptor("icons/aligntop.gif"); //$NON-NLS-1$

        DESC_VERT_ALIGN_BOTTOM_DIS = createDescriptor("icons/alignbottom_d.gif"); //$NON-NLS-1$
        DESC_HORZ_ALIGN_CENTER_DIS = createDescriptor("icons/aligncenter_d.gif"); //$NON-NLS-1$
        DESC_HORZ_ALIGN_LEFT_DIS = createDescriptor("icons/alignleft_d.gif"); //$NON-NLS-1$
        DESC_VERT_ALIGN_MIDDLE_DIS = createDescriptor("icons/alignmid_d.gif"); //$NON-NLS-1$
        DESC_HORZ_ALIGN_RIGHT_DIS = createDescriptor("icons/alignright_d.gif"); //$NON-NLS-1$
        DESC_VERT_ALIGN_TOP_DIS = createDescriptor("icons/aligntop_d.gif"); //$NON-NLS-1$

        DESC_SEPARATOR = createDescriptor("icons/separator.gif"); //$NON-NLS-1$
        DESC_FOLDER_OPEN = PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
        DESC_FOLDER_CLOSED = createDescriptor("icons/folder_closed.gif"); //$NON-NLS-1$

        ICON_MATCH_WIDTH = createDescriptor("icons/sizehz.gif"); //$NON-NLS-1$
        ICON_MATCH_HEIGHT = createDescriptor("icons/sizevt.gif"); //$NON-NLS-1$

        DESC_PINNED = createAndCache(IMG_PINNED);
        DESC_UNPINNED = createAndCache(IMG_UNPINNED);

        DESC_PALETTE = createAndCache(IMG_PALETTE);

    }

    private static ImageDescriptor createDescriptor(String filename) {
        return ImageDescriptor.createFromFile(InternalImages.class, filename);
    }

    /**
     * Creates the image descriptor from the filename given and caches it in the
     * plugin's image registry.
     * 
     * @param imageName
     *            the full filename of the image
     * @return the new image descriptor
     */
    private static ImageDescriptor createAndCache(String imageName) {
        ImageDescriptor result = createDescriptor(imageName);
        InternalGEFPlugin.getDefault().getImageRegistry()
                .put(imageName, result);
        return result;
    }

    /**
     * Gets an image from the image registry. This image should not be disposed
     * of, that is handled in the image registry. The image descriptor must have
     * previously been cached in the image registry. The cached images for the
     * public image names defined in this file can be retrieved using this
     * method.
     * 
     * @param imageName
     *            the full filename of the image
     * @return the image or null if it has not been cached in the registry
     */
    public static Image get(String imageName) {
        return InternalGEFPlugin.getDefault().getImageRegistry().get(imageName);
    }

}
