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
package org.eclipse.gef.ui.palette.customize;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.draw2d.ColorConstants;

import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;

/**
 * This class is the ILabelProvider for the
 * {@link org.eclipse.jface.viewers.TreeViewer} used in
 * {@link org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog}.
 * 
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class PaletteLabelProvider implements ILabelProvider, IColorProvider {

    private Map imageCache = new HashMap();

    /**
     * Constructor
     * 
     * @param viewer
     *            The TreeViewer for which this instance is a LabelProvider
     */
    public PaletteLabelProvider(TreeViewer viewer) {
    }

    /**
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(Object)
     */
    @Override
    public Color getBackground(Object element) {
        return null;
    }

    private Image getCachedImage(ImageDescriptor descriptor) {
        Image image = (Image) imageCache.get(descriptor);
        if (image == null) {
            image = descriptor.createImage();
            imageCache.put(descriptor, image);
        }
        return image;
    }

    /**
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(Object)
     */
    @Override
    public Color getForeground(Object element) {
        PaletteEntry entry = (PaletteEntry) element;
        if (!entry.isVisible() || !entry.getParent().isVisible()) {
            return ColorConstants.gray;
        }
        return null;
    }

    /**
     * @see org.eclipse.jface.viewers.ILabelProvider#getImage(Object)
     */
    @Override
    public Image getImage(Object element) {
        PaletteEntry entry = (PaletteEntry) element;
        ImageDescriptor descriptor = entry.getSmallIcon();
        if (descriptor == null) {
            if (entry instanceof PaletteContainer) {
                descriptor = InternalImages.DESC_FOLDER_OPEN;
            } else if (entry instanceof PaletteSeparator) {
                descriptor = InternalImages.DESC_SEPARATOR;
            } else {
                return null;
            }
        }
        return getCachedImage(descriptor);
    }

    /**
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(Object)
     */
    @Override
    public String getText(Object element) {
        return ((PaletteEntry) element).getLabel();
    }

    /**
     * Not implemented
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(ILabelProviderListener)
     */
    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    @Override
    public void dispose() {
        Iterator images = imageCache.values().iterator();
        while (images.hasNext())
            ((Image) images.next()).dispose();
        imageCache = null;
    }

    /**
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(Object,
     *      String)
     */
    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    /**
     * Not implemented
     * 
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(ILabelProviderListener)
     */
    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

}
