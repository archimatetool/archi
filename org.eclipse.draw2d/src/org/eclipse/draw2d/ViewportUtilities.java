/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to support working with {@link Viewport}s.
 * 
 * @author Philip Ritzkopf
 * @author Alexander Nyssen
 * 
 * @since 3.6
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class ViewportUtilities {

    private ViewportUtilities() {
        // provides only static utility functions and should not be accessed in
        // any other way than static.
    }

    /**
     * Returns all enclosing {@link Viewport}s for a given {@link IFigure},
     * beginning with its direct enclosing {@link Viewport} up the root
     * {@link Viewport} in the figure's parent hierarchy.
     * 
     * @param figure
     * @return A list of {@link Viewport}s representing the figure's enclosing
     *         {@link Viewport} path, where the nearest enclosing
     *         {@link Viewport} as the first element and the root
     *         {@link Viewport} as the last element. In case there is no
     *         enclosing {@link Viewport}, an empty list is returned.
     */
    public static List getEnclosingViewportsPath(IFigure figure) {
        Viewport nearestEnclosingViewport = getNearestEnclosingViewport(figure);
        if (nearestEnclosingViewport == null) {
            return new ArrayList();
        }
        Viewport rootViewport = getRootViewport(figure);
        return getViewportsPath(nearestEnclosingViewport, rootViewport, true);
    }

    /**
     * Returns a list containing the provided leaf {@link Viewport} as the first
     * element, and all its enclosing {@link Viewport}s up to the root
     * {@link Viewport}, where the root {@link Viewport} forms the last element
     * of the list.
     * 
     * @param leafViewport
     *            The {@link Viewport}, whose parent hierarchy is processed.
     * @param rootViewport
     *            an ancestor of the given leafViewport, which marks the end
     *            point of the hierarchy to be processed.
     * @return A list of {@link Viewport}s containing the leaf {@link Viewport}
     *         as the first element, the root {@link Viewport} as the last and
     *         in between all enclosing {@link Viewport}s of the leaf
     *         {@link Viewport} up to the root. Returns an empty list in case
     *         leaf or root {@link Viewport} are null or in case the root
     *         viewport is not an ancestor of the leaf {@link Viewport}.
     */
    public static List getViewportsPath(final Viewport leafViewport,
            final Viewport rootViewport) {
        return getViewportsPath(leafViewport, rootViewport, true);
    }

    /**
     * Returns a list containing the provided leaf {@link Viewport} as the first
     * element, and all its enclosing {@link Viewport}s up to the root
     * {@link Viewport}. The root {@link Viewport} forms the last element of the
     * list, in case includeRootViewport is set to true, otherwise the viewport
     * directly nested below the root viewport will be the last in the list.
     * 
     * @param leafViewport
     *            The {@link Viewport}, whose parent hierarchy is processed.
     * @param rootViewport
     *            an ancestor of the given leafViewport, which marks the end
     *            point of the hierarchy to be processed.
     * @param includeRootViewport
     *            whether the provided rootViewport should be included in the
     *            list of returned viewports (as the last one) or not.
     * @return A list of {@link Viewport}s containing the leaf {@link Viewport}
     *         as the first element, the root {@link Viewport} as the last and
     *         in between all enclosing {@link Viewport}s of the leaf
     *         {@link Viewport} up to the root. Returns an empty list in case
     *         leaf or root {@link Viewport} are null or in case the root
     *         viewport is not an ancestor of the leaf {@link Viewport}.
     */
    public static List getViewportsPath(final Viewport leafViewport,
            final Viewport rootViewport, boolean includeRootViewport) {
        if (leafViewport == null || rootViewport == null) {
            return Collections.EMPTY_LIST;
        }

        // search all enclosing viewports of leaf viewport up to root viewport
        // (or until no enclosing viewport can be found)
        List nestedViewports = new ArrayList();
        Viewport currentViewport = leafViewport;
        do {
            nestedViewports.add(currentViewport);
            currentViewport = ViewportUtilities
                    .getNearestEnclosingViewport(currentViewport);
        } while (currentViewport != null && currentViewport != rootViewport);

        // check if root viewport is an ancestor of the given leaf viewport
        if (currentViewport != null) {
            if (includeRootViewport) {
                nestedViewports.add(currentViewport);
            }
            return nestedViewports;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns the nearest common enclosing {@link Viewport} for two given
     * {@link Figure}s.
     * 
     * @param firstFigure
     * @param secondFigure
     * @return The nearest common {@link Viewport} of the two given figures, or
     *         null if no common enclosing {@link Viewport} could be found.
     */
    public static Viewport getNearestCommonViewport(IFigure firstFigure,
            IFigure secondFigure) {
        return getNearestViewport(FigureUtilities.findCommonAncestor(
                firstFigure, secondFigure));
    }

    /**
     * Returns the upper most enclosing {@link Viewport} for the given
     * {@link IFigure}.
     * 
     * @param figure
     * @return The upper most enclosing {@link Viewport} or null if there is no
     *         enclosing {@link Viewport} for the given {@link IFigure},
     */
    public static Viewport getRootViewport(final IFigure figure) {
        Viewport currentViewport = getNearestViewport(figure);
        while (getNearestEnclosingViewport(currentViewport) != null) {
            currentViewport = getNearestEnclosingViewport(currentViewport);
        }
        return currentViewport;
    }

    /**
     * Returns the given figure in case it is a {@link Viewport} itself,
     * otherwise its nearest enclosing {@link Viewport}.
     * 
     * @param figure
     * @return The given figure in case it is a {@link Viewport} itself,
     *         otherwise the nearest enclosing {@link Viewport} or null if there
     *         is no nearest enclosing {@link Viewport}.
     */
    public static Viewport getNearestViewport(final IFigure figure) {
        if (figure == null) {
            return null;
        }
        if (figure instanceof Viewport) {
            return (Viewport) figure;
        } else {
            return getNearestEnclosingViewport(figure);
        }
    }

    /**
     * Returns the nearest enclosing {@link Viewport} of a given {@link IFigure}
     * by walking up the figure's hierarchy.
     * 
     * @param figure
     * @return The nearest enclosing {@link Viewport} of the given figure, or
     *         null if none could be found.
     */
    public static Viewport getNearestEnclosingViewport(final IFigure figure) {
        if (figure == null) {
            return null;
        }
        Viewport viewport = null;
        IFigure currentFigure = figure;
        while (currentFigure.getParent() != null) {
            if (currentFigure.getParent() instanceof Viewport) {
                viewport = (Viewport) currentFigure.getParent();
                break;
            }
            currentFigure = currentFigure.getParent();
        }
        return viewport;
    }
}