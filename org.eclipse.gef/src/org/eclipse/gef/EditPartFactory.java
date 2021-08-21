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
package org.eclipse.gef;

/**
 * A factory for creating new EditParts. {@link EditPartViewer EditPartViewers}
 * can be configured with an <code>EditPartFactory</code>. Whenever an
 * <code>EditPart</code> in that viewer needs to create another EditPart, it can
 * use the Viewer's factory. The factory is also used by the viewer whenever
 * {@link EditPartViewer#setContents(Object)} is called.
 * 
 * @since 2.0
 */
public interface EditPartFactory {

    /**
     * Creates a new EditPart given the specified <i>context</i> and
     * <i>model</i>.
     * 
     * @param context
     *            The context in which the EditPart is being created, such as
     *            its parent.
     * @param model
     *            the model of the EditPart being created
     * @return EditPart the new EditPart
     */
    EditPart createEditPart(EditPart context, Object model);

}
