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
 * An <i>adapter</i> on an <code>EditPart</code> used to expose a descendant
 * contained in that EditPart. <code>EditPartViewer</code> uses this interface
 * to reveal a given EditPart.
 * <P>
 * An expose helper is obtained by calling {@link EditPart#getAdapter(Class)}.
 * The returned helper is responsible for modifying *only* that EditPart's
 * visuals such that the specified descendant is made visible.
 */
public interface ExposeHelper {

    /**
     * Exposes the specified descendant on the EditPart which returned this
     * helper. This is done by adjusting the EditPart's <i>visuals</i> such that
     * the descendants <i>visuals</i> are exposed. Expose is performed from the
     * bottom up, meaning that EditParts in the parent-chain between the
     * helper's EditPart and the <i>descendant</i> will already have had a
     * change to expose the descendant.
     * 
     * @param editpart
     *            the descendant to expose
     */
    void exposeDescendant(EditPart editpart);

}
