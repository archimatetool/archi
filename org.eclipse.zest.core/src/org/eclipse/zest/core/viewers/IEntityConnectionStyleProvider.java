/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.services.IDisposable;

/**
 * An extension for label providers which allows users to set styles for connections
 * that are based on entity end points.
 * @author Del Myers
 *
 */
//@tag bug(151327-Styles) : fix
public interface IEntityConnectionStyleProvider extends IDisposable {

    /**
     * Returns the style flags for this connection. Valid flags are those
     * that begin with CONNECTION in @see org.eclipse.zest.core.ZestStyles. Check
     * ZestStyles for legal combinations.
     * @param src the source entity.
     * @param dest the destination entity.
     * @return the style flags for this connection.
     * @see org.eclipse.zest.core.widgets.ZestStyles
     */
    public int getConnectionStyle(Object src, Object dest);

    /**
     * Returns the color for the connection. Null for default.
     * @param src the source entity.  Any resources created by this class must be disposed by
     * this class.
     * @param dest the destination entity.
     * @return the color.
     * @see #dispose()
     */
    public Color getColor(Object src, Object dest);

    /**
     * Returns the highlighted color for this connection. Null for default.
     * @param src the source entity.  Any resources created by this class must be disposed by
     * this class.
     * @param dest the destination entity.
     * @return the highlighted color. Null for default.
     * @see #dispose()
     */
    public Color getHighlightColor(Object src, Object dest);

    /**
     * Returns the line width of the connection. -1 for default.
     * @param src the source entity.
     * @param dest the destination entity.
     * @return the line width for the connection. -1 for default.
     */
    public int getLineWidth(Object src, Object dest);

    /**
     * Returns the tooltop for this node. If null is returned Zest will simply
     * use the default tooltip.
     * 
     * @param entity
     * @return
     */
    public IFigure getTooltip(Object entity);
}
