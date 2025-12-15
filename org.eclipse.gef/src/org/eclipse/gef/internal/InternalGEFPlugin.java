/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class InternalGEFPlugin extends AbstractUIPlugin {

    private static BundleContext context;
    private static AbstractUIPlugin singleton;

    public InternalGEFPlugin() {
        singleton = this;
    }

    @Override
    public void start(BundleContext bc) throws Exception {
        super.start(bc);
        context = bc;
    }

    public static BundleContext getContext() {
        return context;
    }

    public static AbstractUIPlugin getDefault() {
        return singleton;
    }

    /**
     * This method attempts to create the cursor using a constructor introduced in
     * SWT 3.131.0 that takes an {@link ImageDataProvider}. If this constructor is
     * not available (SWT versions prior to 3.131.0), it falls back to using the
     * older constructor that accepts {@link ImageData}.
     */
    public static Cursor createCursor(ImageDescriptor source, int hotspotX, int hotspotY) {
        try {
            Constructor<Cursor> ctor = Cursor.class.getConstructor(Device.class, ImageDataProvider.class, int.class,
                    int.class);
            return ctor.newInstance(null, (ImageDataProvider) source::getImageData, hotspotX, hotspotY);
        } catch (NoSuchMethodException e) {
            // SWT version < 3.131.0 (no ImageDataProvider-based constructor)
            return new Cursor(null, source.getImageData(100), hotspotX, hotspotY); // older constructor
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate Cursor", e); //$NON-NLS-1$
        }
    }
}
