/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.gef.tools.PanningSelectionTool;

/**
 * Extend the PanningSelectionTool so that Panning occurs on middle mouse button
 * 
 * @author Phillip Beauvoir
 */
public class PanningSelectionExtendedTool extends PanningSelectionTool {

    @Override
    protected boolean handleButtonDown(int which) {
        if(which == 2) {
            if(stateTransition(STATE_INITIAL, PAN)) {
                refreshCursor();
            }
            which = 1;
        }

        /*
         * A right-click on the canvas will show the plus cursor for the marquee selection tool.
         * On macOS Sonoma the current cursor persists onto the context menu.
         * This is a general problem with Sonoma, see https://github.com/eclipse-platform/eclipse.platform.swt/issues/773
         * As the right-click is only for showing the context menu we don't need to see this cursor when right-clicking at all, so trap this here.
         */
        if(which == 3) {
            return true;
        }

        return super.handleButtonDown(which);
    }

    @Override
    protected boolean handleButtonUp(int which) {
        if(which == 2) {
            which = 1;
        }

        return super.handleButtonUp(which);
    }

}