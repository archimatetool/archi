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
package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.gef.palette.PaletteEntry;

/**
 * An <code>EntryPage</code> displays properties of a <code>PaletteEntry</code>.
 * Listeners can be added to a page
 * 
 * @author Pratik Shah
 */
public interface EntryPage {

    /**
     * Sets the page container for this page. This page will report its errors
     * to the given page container.
     * 
     * @param pageContainer
     *            The <code>EntryPageContainer</code> to which this page can
     *            report errors
     */
    void setPageContainer(EntryPageContainer pageContainer);

    /**
     * This method is called when changes made to properties need to be
     * reflected in the model.
     */
    void apply();

    /**
     * Creates the Control that displays the properties of the given entry. This
     * method will only be called once. The parent Composite's Font is set to
     * the Workbench Dialog Font. The page's Controls should use the Workbench
     * Dialog Font where appropriate.
     * 
     * @param parent
     *            The Composite in which the Control has to be created
     * @param entry
     *            The entry whose properties have to be displayed
     */
    void createControl(Composite parent, PaletteEntry entry);

    /**
     * Returns the Panel (Control) that displays the properties of the entry.
     * This is the same Control that was created in
     * {@link #createControl(Composite,PaletteEntry)}.
     * 
     * @return the Control that displays the properties of the entry
     * @see #createControl(Composite, PaletteEntry)
     */
    Control getControl();

}
