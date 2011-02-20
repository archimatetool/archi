/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * This is a sample implementation of an outline page showing an overview of a graphical editor.
 * It's based on the one by Gunnar Wagenknecht.
 * 
 * @author Phillip Beauvoir
 */
public class OverviewOutlinePage extends Page implements IContentOutlinePage, IPartListener, IContextProvider {

    private Canvas overview;
    private ScrollableThumbnail thumbnail;
    private LightweightSystem lws;
    
    private ScalableFreeformRootEditPart fCurrentEditPart;

    public static String HELP_ID = "uk.ac.bolton.archimate.help.outlineViewHelp"; //$NON-NLS-1$
    
    /**
     * Creates a new OverviewOutlinePage instance.
     */
    public OverviewOutlinePage() {
    }
    
    @Override
    public void createControl(Composite parent) {
        // create canvas and lws
        overview = new Canvas(parent, SWT.NONE);
        lws = new LightweightSystem(overview);
        
        getSite().getPage().addPartListener(this);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(overview, HELP_ID);
    }

    @Override
    public void dispose() {
        if(thumbnail != null) {
            thumbnail.deactivate();
            thumbnail = null;
        }
        
        getSite().getPage().removePartListener(this);

        super.dispose();
    }

    @Override
    public Control getControl() {
        return overview;
    }

    public ISelection getSelection() {
        return StructuredSelection.EMPTY;
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    }

    public void setSelection(ISelection selection) {
    }
    
    @Override
    public void setFocus() {
        if(getControl() != null) {
            getControl().setFocus();
        }
    }

    /**
     * Set the EditPart to display an overview of
     * @param editPart
     */
    private void setEditPart(EditPart editPart) {
        if(thumbnail != null) {
            thumbnail.deactivate();
            thumbnail = null;
        }
        
        // If Edit part is null or not a ScalableFreeformRootEditPart
        if(editPart == null || !(editPart instanceof ScalableFreeformRootEditPart)) {
            return;
        }
        
        fCurrentEditPart = (ScalableFreeformRootEditPart)editPart;
        
        // New Thumbnail
        thumbnail = new ScrollableThumbnail((Viewport)fCurrentEditPart.getFigure());
        thumbnail.setSource(fCurrentEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
        thumbnail.setBorder(new MarginBorder(3));
        lws.setContents(thumbnail);
    }

    @Override
    public void partActivated(IWorkbenchPart part) {
        if(part instanceof IEditorPart) {
            EditPart editPart = (EditPart)part.getAdapter(EditPart.class);
            if(editPart != fCurrentEditPart) {
                setEditPart(editPart);
            }
        }
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        if(part instanceof IEditorPart) {
            EditPart editPart = (EditPart)part.getAdapter(EditPart.class);
            if(editPart == fCurrentEditPart) {
                setEditPart(null);
            }
        }
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
    }

    // =================================================================================
    //                       Contextual Help support
    // =================================================================================

    public int getContextChangeMask() {
        return NONE;
    }

    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    public String getSearchExpression(Object target) {
        return "Outline Window";
    }
}
