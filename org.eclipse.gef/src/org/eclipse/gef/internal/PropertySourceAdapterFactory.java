/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Requel Wermelinger (reguel.wermelinger@ivyteam.ch) - Fix for bug #462235  
 *******************************************************************************/

package org.eclipse.gef.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gef.editparts.AbstractEditPart;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PropertySourceAdapterFactory implements IAdapterFactory {

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        AbstractEditPart part = (AbstractEditPart) adaptableObject;
        Object model = part.getModel();
        // model can be null
        if (model == null) {
            return null;
        }
        // check if model is already of the desired adapter type
        if (adapterType.isInstance(model)) {
            return model;
        }
        // check if model is adaptable and does provide an adapter of the
        // desired type
        if (model instanceof IAdaptable) {
            Object adapter = ((IAdaptable) model).getAdapter(adapterType);
            if (adapter != null) {
                return adapter;
            }
        }
        // fall back to platform's adapter manager
        return Platform.getAdapterManager().getAdapter(model, adapterType);
    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }

}
