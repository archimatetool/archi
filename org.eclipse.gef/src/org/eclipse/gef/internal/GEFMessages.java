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
package org.eclipse.gef.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Internal Messages
 * 
 * @author hudsonr
 * @since 2.0
 */
public class GEFMessages extends NLS {

    public static String AlignBottomAction_Label;
    public static String AlignBottomAction_Tooltip;
    public static String AlignCenterAction_Label;
    public static String AlignCenterAction_Tooltip;
    public static String AlignLeftAction_Label;
    public static String AlignLeftAction_Tooltip;
    public static String AlignMiddleAction_Label;
    public static String AlignMiddleAction_Tooltip;
    public static String AlignRightAction_Label;
    public static String AlignRightAction_Tooltip;
    public static String AlignTopAction_Label;
    public static String AlignTopAction_Tooltip;

    public static String CopyAction_ActionDeleteCommandName;
    public static String CopyAction_Label;
    public static String CopyAction_Tooltip;

    public static String DeleteAction_ActionDeleteCommandName;
    public static String DeleteAction_Label;
    public static String DeleteAction_Tooltip;

    public static String PasteAction_Label;
    public static String PasteAction_Tooltip;

    public static String PrintAction_ActionDeleteCommandName;
    public static String PrintAction_Label;
    public static String PrintAction_Tooltip;

    public static String RedoAction_Label;
    public static String RedoAction_Tooltip;
    public static String RenameAction_Label;
    public static String RenameAction_Tooltip;
    public static String SaveAction_Label;
    public static String SaveAction_Tooltip;
    public static String SetPropertyValueCommand_Label;

    public static String SelectAllAction_Label;
    public static String SelectAllAction_Tooltip;

    public static String MatchSizeAction_Label;
    public static String MatchSizeAction_Tooltip;

    public static String MatchWidthAction_Label;
    public static String MatchWidthAction_Tooltip;

    public static String MatchHeightAction_Label;
    public static String MatchHeightAction_Tooltip;

    public static String SelectionTool_Label;
    public static String MarqueeTool_Label;
    public static String MarqueeTool_Connections_Touched_Desc;
    public static String MarqueeTool_Connections_Contained_Desc;
    public static String MarqueeTool_Nodes_Touched_Desc;
    public static String MarqueeTool_Nodes_Contained_Desc;
    public static String MarqueeTool_Nodes_Touched_And_Related_Connections_Desc;
    public static String MarqueeTool_Nodes_Contained_And_Related_Connections_Desc;
    public static String UndoAction_Label;
    public static String UndoAction_Tooltip;

    // Zoom strings
    public static String ZoomIn_Label;
    public static String ZoomIn_Tooltip;
    public static String ZoomOut_Label;
    public static String ZoomOut_Tooltip;
    public static String FitAllAction_Label;
    public static String FitWidthAction_Label;
    public static String FitHeightAction_Label;

    // View menu actions
    public static String ToggleRulerVisibility_Label;
    public static String ToggleRulerVisibility_Tooltip;
    public static String ToggleSnapToGeometry_Label;
    public static String ToggleSnapToGeometry_Tooltip;
    public static String ToggleGrid_Label;
    public static String ToggleGrid_Tooltip;

    // Palette view Strings
    public static String Palette_Label;
    public static String Palette_Not_Available;

    // Rulers and guides
    public static String Ruler_Horizontal_Label;
    public static String Ruler_Vertical_Label;
    public static String Ruler_Desc;
    public static String Guide_Label;
    public static String Guide_Desc;
    public static String Create_Guide_Label;
    public static String Create_Guide_Tooltip;

    static {
        NLS.initializeMessages(
                "org.eclipse.gef.internal.messages", GEFMessages.class); //$NON-NLS-1$
    }

}