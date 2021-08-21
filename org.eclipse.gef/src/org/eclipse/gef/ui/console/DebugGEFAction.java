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
package org.eclipse.gef.ui.console;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.GEF;

/**
 * Actions associated with viewing debugging data in GEF
 * 
 * @deprecated in 3.1
 */
public class DebugGEFAction extends Action {

    /** Feedback ID **/
    public static String DEBUG_FEEDBACK = "DFeedback"; //$NON-NLS-1$
    /** Painting ID **/
    public static String DEBUG_PAINTING = "DPainting"; //$NON-NLS-1$
    /** EditPart ID **/
    public static String DEBUG_EDITPARTS = "DEditParts"; //$NON-NLS-1$
    /** Events ID **/
    public static String DEBUG_EVENTS = "DEvents"; //$NON-NLS-1$
    /** Tools ID **/
    public static String DEBUG_TOOLS = "DTools"; //$NON-NLS-1$
    /** Global ID **/
    public static String DEBUG_GLOBAL = "DGlobal"; //$NON-NLS-1$
    /** Clear ID **/
    public static String DEBUG_CLEAR = "DClear"; //$NON-NLS-1$
    /** States ID **/
    public static String DEBUG_STATES = "DStates"; //$NON-NLS-1$
    /** DND ID **/
    public static String DEBUG_DND = "DDND"; //$NON-NLS-1$

    /**
     * Creates a new DebugGEFAction with the given String and ImageDescriptor
     * 
     * @param label
     *            the label
     * @param desc
     *            the ImageDescriptor
     */
    public DebugGEFAction(String label, ImageDescriptor desc) {
        super(label, desc);
    }

    /**
     * @see Action#run()
     */
    @Override
    public void run() {
        String type = getText();
        if (type.compareTo(DEBUG_STATES) == 0) {
            GEF.DebugToolStates = !GEF.DebugToolStates;
        }
        if (type.compareTo(DEBUG_FEEDBACK) == 0) {
            GEF.DebugFeedback = !GEF.DebugFeedback;
        }
        if (type.compareTo(DEBUG_PAINTING) == 0) {
            GEF.DebugPainting = !GEF.DebugPainting;
        }
        if (type.compareTo(DEBUG_EDITPARTS) == 0) {
            GEF.DebugEditParts = !GEF.DebugEditParts;
        }
        if (type.compareTo(DEBUG_EVENTS) == 0) {
            GEF.DebugEvents = !GEF.DebugEvents;
        }
        if (type.compareTo(DEBUG_TOOLS) == 0) {
            GEF.DebugTools = !GEF.DebugTools;
        }
        if (type.compareTo(DEBUG_GLOBAL) == 0) {
            GEF.GlobalDebug = !GEF.GlobalDebug;
        }
        if (type.compareTo(DEBUG_CLEAR) == 0) {
            GEF.clearConsole();
        }
        if (type.compareTo(DEBUG_DND) == 0) {
            GEF.DebugDND = !GEF.DebugDND;
        }
    }

}
