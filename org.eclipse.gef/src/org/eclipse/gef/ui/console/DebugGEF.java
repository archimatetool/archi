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
package org.eclipse.gef.ui.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.gef.GEF;

/**
 * View used for debugging information in GEF
 * 
 * @deprecated in 3.1
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DebugGEF extends ViewPart {

    /** Text field **/
    protected Text text;
    /** Container for all {@link DebugGEFAction}s **/
    protected List actions = null;

    /**
     * Creates a new DebugGEF ViewPart
     */
    public DebugGEF() {
        super();
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        text = new Text(parent, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
        text.setFont(new org.eclipse.swt.graphics.Font(parent.getDisplay(),
                "Arial", 7, //$NON-NLS-1$
                SWT.NATIVE));
        text.setText("GEF Debug"); //$NON-NLS-1$
        GEF.setConsole(text);
        makeActions();
        fillActions();
    }

    /**
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        GEF.setConsole(null);
        super.dispose();
    }

    /**
     * Called by createPartControl(Composite). Adds all actions to the
     * {@link IToolBarManager}.
     * 
     */
    protected void fillActions() {
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        for (int i = 0; i < actions.size(); i++) {
            mgr.add((IAction) actions.get(i));
        }
    }

    private ImageDescriptor getDescriptor(String image) {
        return ImageDescriptor.createFromFile(DebugGEF.class, image);
    }

    /**
     * Called by createPartControl(Composite). Creates, initializes, and adds
     * all actions.
     */
    protected void makeActions() {
        if (actions == null)
            actions = new ArrayList();
        DebugGEFAction action = null;
        action = new DebugGEFAction(DebugGEFAction.DEBUG_DND,
                getDescriptor("icons/debugDND.gif")); //$NON-NLS-1$
        action.setToolTipText("Drag and Drop"); //$NON-NLS-1$
        action.setChecked(GEF.DebugDND);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_STATES,
                getDescriptor("icons/debugStates.gif")); //$NON-NLS-1$
        action.setToolTipText("States"); //$NON-NLS-1$
        action.setChecked(GEF.DebugToolStates);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_FEEDBACK,
                getDescriptor("icons/debugFeedback.gif")); //$NON-NLS-1$
        action.setToolTipText("Feedback"); //$NON-NLS-1$
        action.setChecked(GEF.DebugFeedback);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_PAINTING,
                getDescriptor("icons/debugPaint.gif")); //$NON-NLS-1$
        action.setToolTipText("Painting Messages"); //$NON-NLS-1$
        action.setChecked(GEF.DebugPainting);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_EDITPARTS,
                getDescriptor("icons/debugEditParts.gif")); //$NON-NLS-1$
        action.setToolTipText("EditPart Messages"); //$NON-NLS-1$
        action.setChecked(GEF.DebugEditParts);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_EVENTS,
                getDescriptor("icons/debugEvents.gif")); //$NON-NLS-1$
        action.setToolTipText("Event Messages"); //$NON-NLS-1$
        action.setChecked(GEF.DebugEvents);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_TOOLS,
                getDescriptor("icons/debugTools.gif")); //$NON-NLS-1$
        action.setToolTipText("Tool Messages"); //$NON-NLS-1$
        action.setChecked(GEF.DebugTools);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_GLOBAL,
                getDescriptor("icons/debugGlobal.gif")); //$NON-NLS-1$
        action.setToolTipText("Global Messages"); //$NON-NLS-1$
        action.setChecked(GEF.GlobalDebug);
        actions.add(action);
        action = new DebugGEFAction(DebugGEFAction.DEBUG_CLEAR,
                getDescriptor("icons/debugClear.gif")); //$NON-NLS-1$
        action.setToolTipText("Clears Debug Messages"); //$NON-NLS-1$
        actions.add(action);
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        if (text != null)
            text.setFocus();
    }

}
