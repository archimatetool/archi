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