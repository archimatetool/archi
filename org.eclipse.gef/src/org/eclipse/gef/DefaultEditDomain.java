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
package org.eclipse.gef;

import org.eclipse.ui.IEditorPart;

/**
 * A default implementation of {@link EditDomain}. An {@link IEditorPart} is
 * required in the constructor, but it can be <code>null</code>.
 * <P>
 * A {@link org.eclipse.gef.tools.SelectionTool} will be the active Tool until:
 * <UL>
 * <LI>A {@link org.eclipse.gef.palette.PaletteRoot} is provided which contains
 * a default entry which is a {@link org.eclipse.gef.palette.ToolEntry}. In
 * which case that entry's tool is made the active Tool.
 * </UL>
 * <P>
 * DefaultEditDomain can be configured with a
 * {@link org.eclipse.gef.ui.palette.PaletteViewer}. When provided, the
 * DefaultEditDomain will listen for PaletteEvents, and will switch the active
 * Tool automatically in response.
 */
public class DefaultEditDomain extends EditDomain {

    private IEditorPart editorPart;

    /**
     * Constructs a DefaultEditDomain with the specified IEditorPart
     * 
     * @param editorPart
     *            <code>null</code> or an IEditorPart
     */
    public DefaultEditDomain(IEditorPart editorPart) {
        setEditorPart(editorPart);
    }

    /**
     * @return the IEditorPart for this EditDomain
     */
    public IEditorPart getEditorPart() {
        return editorPart;
    }

    /**
     * Sets the IEditorPart for this EditDomain.
     * 
     * @param editorPart
     *            the editor
     */
    protected void setEditorPart(IEditorPart editorPart) {
        this.editorPart = editorPart;
    }

}
