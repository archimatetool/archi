/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * License which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;



/**
 * Handles Exit of Mac OS X Lion Full Screen
 * 
 * @author Phillip Beauvoir
 */
public class EscapeFullScreenCommandHandler extends FullScreenCommandHandler {

    @Override
    public boolean isEnabled() {
        Object[] nsWindows = getNSWindows();
        return super.isEnabled() && nsWindows != null && nsWindows.length >= 1 && isFullScreen(nsWindows[0]);
    }
}
