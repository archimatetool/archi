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
package org.eclipse.gef.ui.stackview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * Internal class used for a debug view.
 * 
 * @deprecated this class will be deleted
 */
public class CommandStackInspector extends PageBookView {

    /**
     * @see PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
     */
    @Override
    protected IPage createDefaultPage(PageBook book) {
        Page page = new Page() {
            Control control;

            @Override
            public void createControl(Composite parent) {
                control = new Canvas(parent, SWT.NONE);
            }

            @Override
            public Control getControl() {
                return control;
            }

            @Override
            public void setFocus() {
            }
        };

        page.createControl(book);
        return page;
    }

    /**
     * @see PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    protected PageRec doCreatePage(IWorkbenchPart part) {
        // Try to get a custom command stack page.
        Object obj = part.getAdapter(CommandStackInspectorPage.class);
        if (obj instanceof IPage) {
            IPage page = (IPage) obj;
            page.createControl(getPageBook());
            return new PageRec(part, page);
        }

        // Use the default page
        return null;
    }

    /**
     * Destroys a page in the pagebook.
     * <p>
     * Subclasses of PageBookView must implement the creation and destruction of
     * pages in the view. This method should be implemented by the subclass to
     * destroy a page for the given part.
     * </p>
     * 
     * @param part
     *            the input part
     * @param rec
     *            a page record for the part
     */
    @Override
    protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
        rec.page.dispose();
    }

    /**
     * @see PageBookView#getBootstrapPart()
     */
    @Override
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage persp = getSite().getWorkbenchWindow().getActivePage();
        if (persp != null)
            return persp.getActiveEditor();
        else
            return null;
    }

    /**
     * @see PageBookView#isImportant(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    protected boolean isImportant(IWorkbenchPart part) {
        return part instanceof IEditorPart;
    }

}
