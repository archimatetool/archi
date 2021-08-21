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
package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.FontData;

/**
 * <code>PaletteViewerPreferences</code> is used to store/persist the various
 * settings of a GEF palette.
 * <p>
 * <EM>IMPORTANT</EM>: This interface is <EM>not</EM> intended to be implemented
 * by clients. Clients should inherit from
 * {@link DefaultPaletteViewerPreferences}. New methods may be added in the
 * future.
 * 
 * @author Pratik Shah
 */
public interface PaletteViewerPreferences {

    /**
     * This is a constant for one of the auto-collapse options. <br>
     * Indicates that containers should always auto-collapse.
     */
    int COLLAPSE_ALWAYS = 2;

    /**
     * This is a constant for one of the auto-collapse options. <br>
     * Indicates that containers should never auto-collapse.
     */
    int COLLAPSE_NEVER = 1;

    /**
     * This is a constant for one of the auto-collapse options. <br>
     * Indicates that containers should auto-collapse only when there is not
     * enough room on the palette. This is the default auto-collapse setting.
     */
    int COLLAPSE_AS_NEEDED = 0;

    /**
     * This is a constant for one of the layout options. <br>
     * Indicates that the palette should be displayed in the columns mode.
     */
    int LAYOUT_COLUMNS = 1;

    /**
     * @deprecated Use LAYOUT_COLUMNS instead.
     */
    int LAYOUT_FOLDER = LAYOUT_COLUMNS;

    /**
     * This is a constant for one of the layout options. <br>
     * Indicates that the palette should be displayed in the list mode. This is
     * the default layout setting.
     */
    int LAYOUT_LIST = 0;

    /**
     * This is a constant for one of the layout options. <br>
     * Indicates that the palette should be displayed in the icons only mode.
     */
    int LAYOUT_ICONS = 2;

    /**
     * This is a constant for one of the layout options. <br>
     * Indicates that the palette should be displayed in the details mode.
     */
    int LAYOUT_DETAILS = 3;

    /**
     * Property name for the layout setting. If the PropertyChangeEvent fired
     * has this property name, it means that the layout setting was changed.
     */
    String PREFERENCE_LAYOUT = "Layout Setting"; //$NON-NLS-1$

    /**
     * Property name for the auto-collapse setting. If the PropertyChangeEvent
     * fired has this property name, it means that the auto-collapse setting was
     * changed.
     */
    String PREFERENCE_AUTO_COLLAPSE = "Auto-Collapse Setting"; //$NON-NLS-1$

    /**
     * Property name for the large icon setting for columns layout. If the
     * PropertyChangeEvent fired has this property name, it means that the large
     * icon setting was changed for columns layout. Large icons are default.
     */
    String PREFERENCE_COLUMNS_ICON_SIZE = "Use Large Icons - Columns"; //$NON-NLS-1$

    /**
     * @deprecated Use PREFERENCE_COLUMNS_ICON_SIZE instead.
     */
    String PREFERENCE_FOLDER_ICON_SIZE = PREFERENCE_COLUMNS_ICON_SIZE;

    /**
     * Property name for the large icon setting for list layout. If the
     * PropertyChangeEvent fired has this property name, it means that the large
     * icon setting was changed for list layout. Small icons are default.
     */
    String PREFERENCE_LIST_ICON_SIZE = "Use Large Icons - List"; //$NON-NLS-1$

    /**
     * Property name for the large icon setting for icons only layout. If the
     * PropertyChangeEvent fired has this property name, it means that the large
     * icon setting was changed for icons only layout. Large icons are default.
     */
    String PREFERENCE_ICONS_ICON_SIZE = "Use Large Icons - Icons"; //$NON-NLS-1$

    /**
     * Property name for the large icon setting for details layout. If the
     * PropertyChangeEvent fired has this property name, it means that the large
     * icon setting was changed for details layout. Small icons are default.
     */
    String PREFERENCE_DETAILS_ICON_SIZE = "Use Large Icons - Details"; //$NON-NLS-1$

    /**
     * Property name for the palette font setting. If the PropertyChangeEvent
     * fired has this property name, it means that the palette font was changed.
     */
    String PREFERENCE_FONT = "Palette Font"; //$NON-NLS-1$ 

    /**
     * @param listener
     *            the PropertyChangeListener to be notified of changes
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns the current auto-collapse setting.
     * <p>
     * Possible values returned:
     * <ul>
     * <li>COLLAPSE_ALWAYS (Always collapse)</li>
     * <li>COLLAPSE_AS_NEEDED (Collapse when needed)</li>
     * <li>COLLAPSE_NEVER (Never collapse)</li>
     * </ul>
     * 
     * @return One of the above-mentioned constants
     */
    int getAutoCollapseSetting();

    /**
     * @return The FontData for the font to be used in the palette.
     */
    FontData getFontData();

    /**
     * Returns the current layout setting.
     * <p>
     * Possible values returned:
     * <ul>
     * <li>LAYOUT_COLUMNS (columns View)</li>
     * <li>LAYOUT_LIST (List View)</li>
     * <li>LAYOUT_ICONS (Icons Only View)</li>
     * <li>LAYOUT_DETAILS (Details View)</li>
     * </ul>
     * 
     * @return One of the above-mentioned constants
     */
    int getLayoutSetting();

    /**
     * Returns the layout modes that are supported. All four layout modes --
     * LAYOUT_COLUMNS, LAYOUT_LIST, LAYOUT_ICONS, LAYOUT_DETAILS -- are
     * supported by default.
     * 
     * @return The layout modes that are supported
     * @see #setSupportedLayoutModes(int[])
     */
    int[] getSupportedLayoutModes();

    /**
     * This is a convenience method. Instead of getting the supported layout
     * modes and checking to see if a certain layout is supported, you can call
     * this method.
     * 
     * @param layout
     *            LAYOUT_COLUMNS, LAYOUT_LIST, LAYOUT_ICONS, or LAYOUT_DETAILS
     * @return <code>true</code> if the given layout is a supported mode
     */
    boolean isSupportedLayoutMode(int layout);

    /**
     * @param listener
     *            the PropertyChangeListener that should not be notified
     *            hereafter
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Sets the auto-collapse setting.
     * <p>
     * Possible values:
     * <ul>
     * <li>COLLAPSE_ALWAYS (Always collapse)</li>
     * <li>COLLAPSE_AS_NEEDED (Collapse when needed)</li>
     * <li>COLLAPSE_NEVER (Never collapse)</li>
     * </ul>
     * 
     * @param newVal
     *            One of the above-mentioned constants
     */
    void setAutoCollapseSetting(int newVal);

    /**
     * Sets the FontData for the palette.
     * 
     * @param data
     *            The FontData for the font to be used in the palette
     */
    void setFontData(FontData data);

    /**
     * Sets the given setting as the current layout.
     * <p>
     * Possible values:
     * <ul>
     * <li>LAYOUT_COLUMNS (Columns View)</li>
     * <li>LAYOUT_LIST (List View)</li>
     * <li>LAYOUT_ICONS (Icons Only View)</li>
     * <li>LAYOUT_DETAILS (Details View)</li>
     * </ul>
     * 
     * @param newVal
     *            One of the above-mentioned constants
     */
    void setLayoutSetting(int newVal);

    /**
     * Sets the "Use Large Icons" option for the currently active layout.
     * 
     * @param newVal
     *            <code>true</code> if large icons are to be used with the
     *            current layout setting
     */
    void setCurrentUseLargeIcons(boolean newVal);

    /**
     * The client can restrict the modes that the palette supports using this
     * method. By default, the palette will support all layout modes:
     * LAYOUT_ICONS, LAYOUT_DETAILS, LAYOUT_COLUMNS, LAYOUT_LIST. Should the
     * client wish to not support all these modes, they can call this method
     * with an array of the desired modes. This method should be called during
     * set-up as soon as the preferences are created, and not later.
     * <p>
     * If the default layout mode and/or the current layout mode are not in the
     * given array, the first layout mode in the given array will be set to be
     * the default/current layout.
     * </p>
     * <p>
     * NOTE: The given array of layout modes should have at least one, and is
     * recommended to have at least two, of the recognized layout modes.
     * </p>
     * 
     * @param modes
     *            an array of layout modes desired
     */
    void setSupportedLayoutModes(int[] modes);

    /**
     * Sets the "Use Large Icons" option for the given layout. <br>
     * The defaults are as follows:
     * <ul>
     * <li>LAYOUT_COLUMNS - <code>true</code></li>
     * <li>LAYOUT_LIST - <code>false</code></li>
     * <li>LAYOUT_ICONS - <code>true</code></li>
     * <li>LAYOUT_DETAILS - <code>false</code></li>
     * </ul>
     * 
     * @param layout
     *            any of the above-mentioned constants
     * @param newVal
     *            <code>true</code> if large icons are to be used with the given
     *            layout
     */
    void setUseLargeIcons(int layout, boolean newVal);

    /**
     * Indicated whether large icons should be used with the given layout mode. <br>
     * The defaults are as follows:
     * <ul>
     * <li>LAYOUT_COLUMNS - <code>true</code></li>
     * <li>LAYOUT_LIST - <code>false</code></li>
     * <li>LAYOUT_ICONS - <code>true</code></li>
     * <li>LAYOUT_DETAILS - <code>false</code></li>
     * </ul>
     * 
     * @param layout
     *            any of the above-mentioned constants
     * @return <code>true</code> if large icons are to be used with the given
     *         layout
     */
    boolean useLargeIcons(int layout);

    /**
     * @return <code>true</code> if large icons are to be used with the
     *         currently active layout
     */
    boolean useLargeIcons();

}
