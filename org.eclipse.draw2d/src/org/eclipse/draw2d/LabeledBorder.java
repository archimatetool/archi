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
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Font;

/**
 * LabeledBorders have a text message somewhere on them. The Font for the text
 * can be set. LabeledBorders should not change their Insets when the label
 * changes, therefore, Figures using this Border should repaint() when updating
 * the label, and revalidate() when changing the Font.
 */
public interface LabeledBorder extends Border {

    /**
     * Returns the label for this Border.
     * 
     * @return The label for this Border
     */
    String getLabel();

    /**
     * Sets the Font for the label.
     * 
     * @param f
     *            The Font to be set
     */
    void setFont(Font f);

    /**
     * Sets the text to be displayed as the label for this Border.
     * 
     * @param l
     *            The text
     */
    void setLabel(String l);

}
