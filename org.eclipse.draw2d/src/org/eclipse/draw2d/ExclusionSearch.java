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
package org.eclipse.draw2d;

import java.util.Collection;

/**
 * A <code>TreeSearch</code> that excludes figures contained in a
 * {@link java.util.Collection}.
 * 
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings("rawtypes")
public class ExclusionSearch implements TreeSearch {

    private final Collection c;

    /**
     * Constructs an Exclusion search using the given collection.
     * 
     * @param collection
     *            the exclusion set
     */
    public ExclusionSearch(Collection collection) {
        this.c = collection;
    }

    /**
     * @see org.eclipse.draw2d.TreeSearch#accept(IFigure)
     */
    @Override
    public boolean accept(IFigure figure) {
        // Prune is called before accept, so there is no reason to check the
        // collection again.
        return true;
    }

    /**
     * Returns <code>true</code> if the figure is a member of the Collection.
     * 
     * @see org.eclipse.draw2d.TreeSearch#prune(IFigure)
     */
    @Override
    public boolean prune(IFigure f) {
        return c.contains(f);
    }

}
