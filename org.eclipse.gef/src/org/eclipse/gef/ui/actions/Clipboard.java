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
package org.eclipse.gef.ui.actions;

import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gef.dnd.SimpleObjectTransfer;

/**
 * A GEF clipboard for cut/copy/paste actions between GEF editors. It exists
 * mainly for convenience and allows clients to add graphical objects to the
 * system clipboard. It will not work between two instances of the workbench
 * (but will work between multiple windows belonging to a single instance of the
 * workbench). Setting the contents of the clipboard will erase the previous
 * contents of the clipboard.
 * 
 * @author Eric Bordeau
 * @author Pratik Shah
 */
public class Clipboard {

    private static Clipboard defaultClipboard = new Clipboard();
    private static final SimpleObjectTransfer TRANSFER = new SimpleObjectTransfer() {
        private final String TYPE_NAME = "org.eclipse.gef.clipboard.transfer"; //$NON-NLS-1$
        private final int TYPE_ID = registerType(TYPE_NAME);

        @Override
        protected int[] getTypeIds() {
            return new int[] { TYPE_ID };
        }

        @Override
        protected String[] getTypeNames() {
            return new String[] { TYPE_NAME };
        }
    };

    /**
     * Returns the default clipboard.
     * 
     * @return the default clipboard
     */
    public static Clipboard getDefault() {
        return defaultClipboard;
    }

    /**
     * Constructs a new Clipboard object.
     * 
     * @deprecated As of 3.1, the GEF Clipboard synchronizes with the system
     *             clipboard. Multiple instances of this class should not be
     *             created. Use {@link #getDefault()}. This method will be
     *             removed in future releases.
     */
    public Clipboard() {
    }

    /**
     * Returns the current contents of the clipboard.
     * 
     * @return contents of the clipboard
     */
    public Object getContents() {
        org.eclipse.swt.dnd.Clipboard cb = new org.eclipse.swt.dnd.Clipboard(
                null);
        Object contents = cb.getContents(TRANSFER);
        cb.dispose();
        return contents;
    }

    /**
     * Sets the contents of the clipboard. This will erase the previous contents
     * of this as well as the system clipboard. The provided contents will not
     * be garbage-collected until some other contents are set using this method.
     * 
     * @param contents
     *            the new contents
     */
    public void setContents(Object contents) {
        org.eclipse.swt.dnd.Clipboard cb = new org.eclipse.swt.dnd.Clipboard(
                null);
        cb.setContents(new Object[] { contents }, new Transfer[] { TRANSFER });
        cb.dispose();
    }

}