/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.gef.tools.PanningSelectionTool;

import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Extend the PanningSelectionTool so that panning occurs on middle mouse button
 * 
 * @author Phillip Beauvoir
 */
public class PanningSelectionExtendedTool extends PanningSelectionTool {

    @Override
    protected boolean handleButtonDown(int which) {
        // Middle mouse button down so set to panning state
        if(which == 2 && stateTransition(STATE_INITIAL, PAN)) {
            refreshCursor();
            return super.handleButtonDown(1); // Set to left mouse
        }

        /*
         * A right-click on the canvas will briefly show the plus cursor for the marquee selection tool before showing the context menu.
         * On macOS Sonoma and greater this cursor persists while the context menu is shown.
         * This is a general problem with Mac, see https://github.com/eclipse-platform/eclipse.platform.swt/issues/773
         * As right-click is only used for showing the context menu we don't need to see this cursor when right-clicking at all, so trap this here.
         */
        if(PlatformUtils.isMac() && which == 3 || (which == 1 && getCurrentInput().isControlKeyDown())) {
            boolean result = super.handleButtonDown(which);
            setCursor(null);
            return result;
        }

        return super.handleButtonDown(which);
    }

    @Override
    protected boolean handleButtonUp(int which) {
        // Middle mouse button up and panning in progress
        if(which == 2 && isInState(PAN_IN_PROGRESS)) {
            return super.handleButtonUp(1);   // Set to left mouse
        }

        return super.handleButtonUp(which);
    }

}