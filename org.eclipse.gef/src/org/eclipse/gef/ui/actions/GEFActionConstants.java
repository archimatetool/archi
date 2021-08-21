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
package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Defines the names of those actions which are preregistered with the
 * {@link org.eclipse.gef.ui.parts.GraphicalEditor}. This interface extends the
 * set of names available from <code>IWorkbenchActionConstants</code>. It also
 * defines the names of the menu groups in a graphical editor's context menu.
 */
public final class GEFActionConstants implements IWorkbenchActionConstants {

    /**
     * The ID for the GEF Text Context.
     */
    public static final String CONTEXT_TEXT = "org.eclipse.gef.textContext"; //$NON-NLS-1$

    /**
     * Align bottom action id. Value:
     * <code>"org.eclipse.gef.align_bottom"</code>
     */
    public static final String ALIGN_BOTTOM = "org.eclipse.gef.align_bottom";//$NON-NLS-1$

    /**
     * Align center (horizontal) action id. Value:
     * <code>"org.eclipse.gef.align_center"</code>
     */
    public static final String ALIGN_CENTER = "org.eclipse.gef.align_center";//$NON-NLS-1$

    /**
     * Align left action id. Value: <code>"org.eclipse.gef.align_left"</code>
     */
    public static final String ALIGN_LEFT = "org.eclipse.gef.align_left";//$NON-NLS-1$

    /**
     * Align middle (vertical) action id. Value:
     * <code>"org.eclipse.gef.align_middle"</code>
     */
    public static final String ALIGN_MIDDLE = "org.eclipse.gef.align_middle";//$NON-NLS-1$

    /**
     * Align right action id. Value: <code>"org.eclipse.gef.align_right"</code>
     */
    public static final String ALIGN_RIGHT = "org.eclipse.gef.align_right";//$NON-NLS-1$

    /**
     * Align top action id. Value: <code>"org.eclipse.gef.align_top"</code>
     */
    public static final String ALIGN_TOP = "org.eclipse.gef.align_top";//$NON-NLS-1$

    /**
     * Direct edit action id. Value: <code>"org.eclipse.gef.direct_edit"</code>
     */
    public static final String DIRECT_EDIT = "org.eclipse.gef.direct_edit";//$NON-NLS-1$

    /**
     * Context menu group for copy/paste related actions. Value:
     * <code>"org.eclipse.gef.group.copy"</code>
     */
    public static final String GROUP_COPY = "org.eclipse.gef.group.copy"; //$NON-NLS-1$

    /**
     * Context menu group for EditPart manipulation actions. Value:
     * <code>"org.eclipse.gef.group.edit"</code>
     */
    public static final String GROUP_EDIT = "org.eclipse.gef.group.edit"; //$NON-NLS-1$

    /**
     * Context menu group for find/replace related actions. Value:
     * <code>"org.eclipse.gef.group.find"</code>
     */
    public static final String GROUP_FIND = "org.eclipse.gef.group.find"; //$NON-NLS-1$

    /**
     * Context menu group for print related actions. Value:
     * <code>"org.eclipse.gef.group.print"</code>
     */
    public static final String GROUP_PRINT = "org.eclipse.gef.group.print"; //$NON-NLS-1$

    /**
     * Context menu group for actions which do not fit in one of the other
     * categories. Value: <code>"org.eclipse.gef.group.rest"</code>
     */
    public static final String GROUP_REST = "org.eclipse.gef.group.rest"; //$NON-NLS-1$    

    /**
     * Context menu group for save related actions. Value:
     * <code>"org.eclipse.gef.group.save"</code>
     */
    public static final String GROUP_SAVE = "org.eclipse.gef.group.save"; //$NON-NLS-1$

    /**
     * Context menu group for undo/redo related actions. Value:
     * <code>"org.eclipse.gef.group.undo"</code>
     */
    public static final String GROUP_UNDO = "org.eclipse.gef.group.undo"; //$NON-NLS-1$

    /**
     * Context menu group for view related actions. Value:
     * <code>"org.eclipse.gef.group.view"</code>
     */
    public static final String GROUP_VIEW = "org.eclipse.gef.group.view"; //$NON-NLS-1$

    /**
     * Value: <code>"org.eclipse.gef.match.size"</code>
     * 
     * @since 3.7
     */
    public static final String MATCH_SIZE = "org.eclipse.gef.match.size"; //$NON-NLS-1$

    /**
     * Value: <code>"org.eclipse.gef.match.width"</code>
     */
    public static final String MATCH_WIDTH = "org.eclipse.gef.match.width"; //$NON-NLS-1$

    /**
     * Value: <code>"org.eclipse.gef.match.height"</code>
     */
    public static final String MATCH_HEIGHT = "org.eclipse.gef.match.height"; //$NON-NLS-1$

    /**
     * Zoom contribution id. Value: <code>"org.eclipse.gef.zoom"</code>
     */
    public static final String ZOOM_TOOLBAR_WIDGET = "org.eclipse.gef.zoom_widget"; //$NON-NLS-1$

    /**
     * Zoom in contribution id. Value: <code>"org.eclipse.gef.zoom_in"</code>
     */
    public static final String ZOOM_IN = "org.eclipse.gef.zoom_in"; //$NON-NLS-1$

    /**
     * Zoom out contribution id. Value: <code>"org.eclipse.gef.zoom_out"</code>
     */
    public static final String ZOOM_OUT = "org.eclipse.gef.zoom_out"; //$NON-NLS-1$

    /**
     * Toggle grid visibility contribution id<br>
     * Value: <code>"org.eclipse.gef.snap.grid.visibility"</code>
     */
    public static final String TOGGLE_GRID_VISIBILITY = "org.eclipse.gef.toggle_grid_visibility"; //$NON-NLS-1$

    /**
     * Toggle ruler visibility contribution id<br>
     * Value: <code>"org.eclipse.gef.rulers.visibility"</code>
     */
    public static final String TOGGLE_RULER_VISIBILITY = "org.eclipse.gef.toggle_ruler_visibility"; //$NON-NLS-1$

    /**
     * Toggle snap to geometry contribution id<br>
     * Value: <code>"org.eclipse.gef.snap.geometry"</code>
     */
    public static final String TOGGLE_SNAP_TO_GEOMETRY = "org.eclipse.gef.toggle_snapto_geometry"; //$NON-NLS-1$

    /**
     * Adds standard group separators to the given MenuManager.
     * 
     * @param menu
     *            the MenuManager
     */
    public static void addStandardActionGroups(IMenuManager menu) {
        menu.add(new Separator(GROUP_UNDO));
        menu.add(new Separator(GROUP_COPY));
        menu.add(new Separator(GROUP_PRINT));
        menu.add(new Separator(GROUP_EDIT));
        menu.add(new Separator(GROUP_VIEW));
        menu.add(new Separator(GROUP_FIND));
        menu.add(new Separator(GROUP_ADD));
        menu.add(new Separator(GROUP_REST));
        menu.add(new Separator(MB_ADDITIONS));
        menu.add(new Separator(GROUP_SAVE));
    }

}
