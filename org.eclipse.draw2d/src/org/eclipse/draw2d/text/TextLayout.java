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
package org.eclipse.draw2d.text;

import java.util.List;

/**
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class TextLayout extends FlowFigureLayout {

    /**
     * Creates a new TextLayout with the given TextFlow
     * 
     * @param flow
     *            The TextFlow
     */
    public TextLayout(TextFlow flow) {
        super(flow);
    }

    /**
     * Reuses an existing <code>TextFragmentBox</code>, or creates a new one.
     * 
     * @param i
     *            the index
     * @param fragments
     *            the original list of fragments
     * @return a TextFragmentBox
     */
    protected TextFragmentBox getFragment(int i, List fragments) {
        if (fragments.size() > i)
            return (TextFragmentBox) fragments.get(i);
        TextFragmentBox box = new TextFragmentBox((TextFlow) getFlowFigure());
        fragments.add(box);
        return box;
    }

}
