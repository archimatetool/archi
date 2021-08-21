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
package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IMemento;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gef.internal.ui.palette.PaletteSelectionTool;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.PaletteStackEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

/**
 * Graphical viewer for the GEF palette.
 * 
 * @author Randy Hudson
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class PaletteViewer extends ScrollingGraphicalViewer {

    private class PreferenceListener implements PropertyChangeListener {
        /**
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();
            EditPart root = getRootEditPart().getContents();
            if (property.equals(PaletteViewerPreferences.PREFERENCE_FONT)) {
                updateFont();
                refreshAllEditParts(root);
            } else if (property
                    .equals(PaletteViewerPreferences.PREFERENCE_LAYOUT)
                    || property
                            .equals(PaletteViewerPreferences.PREFERENCE_AUTO_COLLAPSE)
                    || property
                            .equals(DefaultPaletteViewerPreferences
                                    .convertLayoutToPreferenceName(getPaletteViewerPreferences()
                                            .getLayoutSetting()))) {
                refreshAllEditParts(root);
            }
        }

        private void refreshAllEditParts(EditPart part) {
            part.refresh();
            List children = part.getChildren();
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                EditPart child = (EditPart) iter.next();
                refreshAllEditParts(child);
            }
        }
    }

    private static final PaletteViewerPreferences PREFERENCE_STORE = new DefaultPaletteViewerPreferences();
    private ToolEntry activeEntry = null;
    private PaletteCustomizer customizer = null;
    private PaletteCustomizerDialog customizerDialog = null;
    private boolean globalScrollbar = false;
    private List paletteListeners = new ArrayList();
    private PaletteRoot paletteRoot = null;
    private PreferenceListener prefListener = new PreferenceListener();
    private PaletteViewerPreferences prefs = PREFERENCE_STORE;
    private Font font = null;

    /**
     * Constructor
     */
    public PaletteViewer() {
        EditDomain domain = new EditDomain();
        domain.setDefaultTool(new PaletteSelectionTool());
        domain.loadDefaultTool();
        setEditDomain(domain);
        setKeyHandler(new PaletteViewerKeyHandler(this));
        setEditPartFactory(new PaletteEditPartFactory());
    }

    /**
     * Adds the given PaletteListener as the one to be notified when the active
     * tool on the palette changes.
     * 
     * @param paletteListener
     *            The listener that needs to be notified of active tool changes
     *            on the palette
     */
    public void addPaletteListener(PaletteListener paletteListener) {
        if (paletteListeners != null)
            paletteListeners.add(paletteListener);
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#createDefaultRoot()
     */
    @Override
    protected void createDefaultRoot() {
        setRootEditPart(new SimpleRootEditPart());
    }

    private void disposeFont() {
        if (font != null) {
            font.dispose();
            font = null;
        }
    }

    /**
     * Indicates that the palette should scroll using a native vertical
     * scrollbar as opposed to individual lightweight buttons that appear
     * dynamically on each drawer. The default settings is <code>false</code>.
     * Enabling this setting requires additional horizontal screen space for the
     * scrollbar. Therefore, its use is discouraged.
     * <p>
     * This setting must be changed prior to calling
     * {@link ScrollingGraphicalViewer#createControl(org.eclipse.swt.widgets.Composite)}
     * . After the control is created, changing this setting will have no
     * effect.
     * </p>
     * 
     * @param value
     *            <code>true</code> if a vertical scrollbar should be displayed
     */
    public void enableVerticalScrollbar(boolean value) {
        this.globalScrollbar = value;
    }

    /**
     * @return the DrawerEditPart that has the given part; part, if part is a
     *         drawer; null, if part is not in a drawer
     */
    private DrawerEditPart findContainingDrawer(EditPart part) {
        if (part == null)
            return null;
        if (part instanceof DrawerEditPart)
            return (DrawerEditPart) part;
        return findContainingDrawer(part.getParent());
    }

    /**
     * Notifies registered listeners of change in the active tool on the palette
     */
    protected void fireModeChanged() {
        if (paletteListeners == null)
            return;
        for (int listener = 0; listener < paletteListeners.size(); listener++)
            ((PaletteListener) paletteListeners.get(listener))
                    .activeToolChanged(this, activeEntry);
    }

    /**
     * @return the customizer
     */
    public PaletteCustomizer getCustomizer() {
        return customizer;
    }

    /**
     * NOTE: A PaletteCustomizer must be set for this viewer using the
     * {@link #setCustomizer(PaletteCustomizer)} method before this method is
     * invoked.
     * 
     * @return The dialog that can be used to customize entries on the palette
     */
    public PaletteCustomizerDialog getCustomizerDialog() {
        if (customizerDialog == null) {
            customizerDialog = new PaletteCustomizerDialog(getControl()
                    .getShell(), getCustomizer(), getPaletteRoot());
        }
        return customizerDialog;
    }

    /**
     * @return the entry for the currently active tool
     */
    public ToolEntry getActiveTool() {
        return activeEntry;
    }

    /**
     * Returns the palette's root model.
     * 
     * @return the palette root
     */
    public PaletteRoot getPaletteRoot() {
        return paletteRoot;
    }

    /**
     * @return The PaletteViewerPreferences that this palette is using to store
     *         its preferences (if none has been set, it returns the default
     *         one, which uses the GEF preference store)
     */
    public PaletteViewerPreferences getPaletteViewerPreferences() {
        return prefs;
    }

    private ToolEntryEditPart getToolEntryEditPart(ToolEntry entry) {
        return (ToolEntryEditPart) getEditPartRegistry().get(entry);
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#handleDispose(org.eclipse.swt.events.DisposeEvent)
     */
    @Override
    protected void handleDispose(DisposeEvent e) {
        super.handleDispose(e);
        disposeFont();
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#handleFocusGained(FocusEvent)
     */
    @Override
    protected void handleFocusGained(FocusEvent fe) {
        super.handleFocusGained(fe);
        /*
         * Fix for Bug# 63297 When the palette gets focus, the LWS will take
         * care of setting focus on the correct figure. However, when the user
         * clicks on an entry, the focus is "thrown away." In that case, the LWS
         * would set focus on the first focusable figure. We override that here
         * and set focus on the correct/selected figure. Invoking
         * getFocusEditPart().setFocus(true) does not work because that editpart
         * thinks it already has focus (it's not aware of focus being thrown
         * away) and does nothing.
         */
        if (focusPart == null) {
            IFigure fig = ((GraphicalEditPart) getFocusEditPart()).getFigure();
            fig.internalGetEventDispatcher().requestFocus(fig);
        }
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#hookControl()
     */
    @Override
    protected void hookControl() {
        super.hookControl();
        FigureCanvas canvas = getFigureCanvas();
        canvas.getViewport().setContentsTracksWidth(true);
        canvas.getViewport().setContentsTracksHeight(!globalScrollbar);
        canvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
        canvas.setVerticalScrollBarVisibility(globalScrollbar ? FigureCanvas.ALWAYS
                : FigureCanvas.AUTOMATIC);
        if (prefs != null)
            prefs.addPropertyChangeListener(prefListener);
        updateFont();
    }

    /**
     * Returns true if the given PaletteDrawer is expanded
     * 
     * @param drawer
     *            the PaletteDrawer
     * @return true if expanded
     */
    public boolean isExpanded(PaletteDrawer drawer) {
        EditPart ep = (EditPart) getEditPartRegistry().get(drawer);
        if (ep instanceof DrawerEditPart)
            return ((DrawerEditPart) ep).isExpanded();
        return false;
    }

    /**
     * Returns true if the given PaletteDrawer is pinned
     * 
     * @param drawer
     *            the PaletteDrawer
     * @return true if pinned
     */
    public boolean isPinned(PaletteDrawer drawer) {
        EditPart ep = (EditPart) getEditPartRegistry().get(drawer);
        if (ep instanceof DrawerEditPart)
            return ((DrawerEditPart) ep).isPinnedOpen();
        return false;
    }

    /**
     * The given PaletteListener will not be notified of active tool changes in
     * the palette.
     * 
     * @param paletteListener
     *            the PaletteListener which doesn't want to be notified of
     *            active tool changes in the palette anymore
     */
    public void removePaletteListener(PaletteListener paletteListener) {
        paletteListeners.remove(paletteListener);
    }

    /**
     * Tries to apply the state of the given IMemento to the contents of this
     * viewer. It fails silently, i.e. no exceptions are thrown if the given
     * state could not be applied.
     * 
     * @param memento
     *            The memento that has the state to be applied to the contents
     *            of this viewer
     * @return a boolean indicating whether or not the given memento was
     *         successfully applied
     * @since 3.0
     */
    public boolean restoreState(IMemento memento) {
        try {
            PaletteEditPart part = (PaletteEditPart) getEditPartRegistry().get(
                    getPaletteRoot());
            if (part != null)
                part.restoreState(memento);
        } catch (RuntimeException re) {
            /*
             * @TODO:Pratik Perhaps you should log this exception. Or not catch
             * it at all.
             */
            return false;
        }
        return true;
    }

    /**
     * @see ScrollingGraphicalViewer#reveal(EditPart)
     */
    @Override
    public void reveal(EditPart part) {
        // If the given part is a drawer, we don't need to expand it. Hence,
        // when invoking
        // findContainingDrawer(), we use part.getParent()
        DrawerEditPart drawer = findContainingDrawer(part.getParent());
        if (drawer != null && !drawer.isExpanded())
            drawer.setExpanded(true);
        // if the part is inside a stack, set it to be the top level item of the
        // stack.
        if (part.getParent() != null
                && part.getParent() instanceof PaletteStackEditPart)
            ((PaletteStack) part.getParent().getModel())
                    .setActiveEntry((PaletteEntry) part.getModel());
        super.reveal(part);
    }

    /**
     * Captures the state of the contents of this viewer in the given memento
     * 
     * @param memento
     *            the IMemento in which the state is to be saved
     * @since 3.0
     */
    public void saveState(IMemento memento) {
        // Bug# 69026 - The PaletteRoot can be null initially for VEP
        PaletteEditPart base = (PaletteEditPart) getEditPartRegistry().get(
                getPaletteRoot());
        if (base != null)
            base.saveState(memento);
    }

    /**
     * Sets the customizer.
     * 
     * @param customizer
     *            the customizer to be set
     */
    public void setCustomizer(PaletteCustomizer customizer) {
        this.customizer = customizer;
    }

    /**
     * Sets the active entry for this palette. The Editpart for the given entry
     * will be activated (selected).
     * 
     * @param newMode
     *            the ToolEntry whose EditPart has to be set as the active tool
     *            in this palette
     */
    public void setActiveTool(ToolEntry newMode) {
        if (newMode == null)
            newMode = getPaletteRoot().getDefaultEntry();
        if (activeEntry != null)
            getToolEntryEditPart(activeEntry).setToolSelected(false);
        activeEntry = newMode;
        if (activeEntry != null) {
            ToolEntryEditPart editpart = getToolEntryEditPart(activeEntry);
            if (editpart != null) {
                editpart.setToolSelected(true);
            }
        }
        fireModeChanged();
    }

    /**
     * Sets the root for this palette.
     * 
     * @param root
     *            the PaletteRoot for this palette
     */
    public void setPaletteRoot(PaletteRoot root) {
        if (root == paletteRoot)
            return;
        paletteRoot = root;
        if (paletteRoot != null) {
            EditPart palette = getEditPartFactory().createEditPart(
                    getRootEditPart(), root);
            getRootEditPart().setContents(palette);
        }
    }

    /**
     * This palette will use the given PaletteViewerPreferences to store all its
     * preferences.
     * <p>
     * NOTE: This method should be invoked by a client only once (before the
     * first time {@link #getPaletteViewerPreferences()} is invoked). Trying to
     * invoke this method after that could lead to problems where some
     * preferences would still be stored in the old preference store.
     * </p>
     * 
     * @param prefs
     *            the PaletteViewerPreferences that is to be used to store all
     *            the preferences for this palette
     */
    public void setPaletteViewerPreferences(PaletteViewerPreferences prefs) {
        if (this.prefs != null)
            this.prefs.removePropertyChangeListener(prefListener);
        this.prefs = prefs;
        if (getControl() != null && !getControl().isDisposed())
            this.prefs.addPropertyChangeListener(prefListener);
    }

    /**
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
     */
    @Override
    protected void unhookControl() {
        super.unhookControl();
        disposeFont();
        if (prefs != null)
            prefs.removePropertyChangeListener(prefListener);
    }

    private void updateFont() {
        disposeFont();

        if (getControl() == null || getControl().isDisposed())
            return;

        font = new Font(Display.getCurrent(), getPaletteViewerPreferences()
                .getFontData());
        getControl().setFont(font);
        getFigureCanvas().getViewport().invalidateTree();
        getFigureCanvas().getViewport().revalidate();
        getFigureCanvas().redraw();
    }

}
