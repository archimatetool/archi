/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.views.palette;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

import org.eclipse.gef.internal.GEFMessages;

/**
 * The GEF palette view
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public class PaletteView extends PageBookView {

    private boolean viewInPage = true;

    /**
     * The ID for this view. This is the same as the String used to register
     * this view with the platform's extension point.
     */
    public static final String ID = "org.eclipse.gef.ui.palette_view"; //$NON-NLS-1$

    private IPerspectiveListener perspectiveListener = new IPerspectiveListener() {
        @Override
        public void perspectiveChanged(IWorkbenchPage page,
                IPerspectiveDescriptor perspective, String changeId) {
        }

        // fix for bug 109245 and 69098 - fake a partActivated when the
        // perpsective is switched
        @Override
        public void perspectiveActivated(IWorkbenchPage page,
                IPerspectiveDescriptor perspective) {
            viewInPage = page.findViewReference(ID) != null;
            // getBootstrapPart could return null; but isImportant() can handle
            // null
            partActivated(getBootstrapPart());
        }
    };

    /**
     * Creates a default page saying that a palette is not available.
     * 
     * @see org.eclipse.ui.part.PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
     */
    @Override
    protected IPage createDefaultPage(PageBook book) {
        MessagePage page = new MessagePage();
        initPage(page);
        page.createControl(book);
        page.setMessage(GEFMessages.Palette_Not_Available);
        return page;
    }

    /**
     * Add a perspective listener so the palette view can be updated when the
     * perspective is switched.
     * 
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        getSite().getPage().getWorkbenchWindow()
                .addPerspectiveListener(perspectiveListener);
    }

    /**
     * Remove the perspective listener.
     * 
     * @see org.eclipse.ui.IWorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        getSite().getPage().getWorkbenchWindow()
                .removePerspectiveListener(perspectiveListener);
        super.dispose();
    }

    /**
     * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    protected PageRec doCreatePage(IWorkbenchPart part) {
        // Try to get a custom palette page
        Object obj = part.getAdapter(PalettePage.class);

        if (obj != null && obj instanceof IPage) {
            IPage page = (IPage) obj;
            page.createControl(getPageBook());
            initPage((IPageBookViewPage) page);
            return new PageRec(part, page);
        }
        // Use the default page by returning null
        return null;
    }

    /**
     * @see PageBookView#doDestroyPage(org.eclipse.ui.IWorkbenchPart,
     *      org.eclipse.ui.part.PageBookView.PageRec)
     */
    @Override
    protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
        rec.page.dispose();
    }

    /**
     * The view shows the palette associated with the active editor.
     * 
     * @see PageBookView#getBootstrapPart()
     */
    @Override
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null)
            return page.getActiveEditor();
        return null;
    }

    /**
     * Only editors in the same perspective as the view are important.
     * 
     * @see PageBookView#isImportant(org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    protected boolean isImportant(IWorkbenchPart part) {
        // Workaround for Bug# 69098 -- This should be removed when/if Bug#
        // 70510 is fixed
        // We only want a palette page to be created when this view is visible
        // in the current
        // perspective, i.e., when both this view and the given editor are on
        // the same page.
        return viewInPage && part instanceof IEditorPart;
    }

}
