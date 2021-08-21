/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract implementation of the image figure. Implements attaching/detaching
 * mechanism for <code>ImageChangedListener</code>
 * 
 * @author aboyko
 * @since 3.6
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractImageFigure extends Figure implements
        IImageFigure {

    private List imageListeners = new ArrayList();

    @Override
    public final void addImageChangedListener(ImageChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        }
        imageListeners.add(listener);
    }

    @Override
    public final void removeImageChangedListener(ImageChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        }
        imageListeners.remove(listener);
    }

    protected final void notifyImageChanged() {
        for (Iterator itr = imageListeners.iterator(); itr.hasNext();) {
            ((ImageChangedListener) itr.next()).imageChanged();
        }
    }

}
