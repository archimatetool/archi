/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * This is a sample implementation of an outline page showing an overview of a graphical editor.
 * It's based on the one by Gunnar Wagenknecht.
 * 
 * It only works for Editors that provide a ScalableFreeformRootEditPart as their Root Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class OverviewOutlinePage extends Page implements IContentOutlinePage, IContextProvider {

    private ScalableFreeformRootEditPart editPart;
    private Canvas canvas;
    private ScrollableThumbnail thumbnail;

    public static String HELP_ID = "com.archimatetool.help.outlineViewHelp"; //$NON-NLS-1$
    
    /**
     * Creates a new OverviewOutlinePage instance for editPart
     */
    public OverviewOutlinePage(ScalableFreeformRootEditPart editPart) {
        this.editPart = editPart;
    }
    
    @Override
    public void createControl(Composite parent) {
        // Create Canvas and LWS
        canvas = new Canvas(parent, SWT.NONE);
        LightweightSystem lws = new LightweightSystem(canvas);
        
        // Thumbnail is contents of LWS
        thumbnail = new ScrollableThumbnail((Viewport)editPart.getFigure());
        thumbnail.setUseScaledGraphics(false);
        thumbnail.setSource(editPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
        thumbnail.setBorder(new MarginBorder(3));
        lws.setContents(thumbnail);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(canvas, HELP_ID);
    }

    @Override
    public void dispose() {
        if(thumbnail != null) {
            thumbnail.deactivate();
            thumbnail = null;
        }
        super.dispose();
    }
    
    @Override
    public Control getControl() {
        return canvas;
    }

    @Override
    public ISelection getSelection() {
        return StructuredSelection.EMPTY;
    }

    @Override
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
    }

    @Override
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    }

    @Override
    public void setSelection(ISelection selection) {
    }
    
    @Override
    public void setFocus() {
        if(getControl() != null) {
            getControl().setFocus();
        }
    }

    // =================================================================================
    //                       Contextual Help support
    // =================================================================================

    @Override
    public int getContextChangeMask() {
        return NONE;
    }

    @Override
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    @Override
    public String getSearchExpression(Object target) {
        return Messages.OverviewOutlinePage_0;
    }
}
