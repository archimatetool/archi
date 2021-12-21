/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;

/**
 * The FlyoutPaletteComposite is used to show a flyout palette alongside another
 * control. The flyout palette auto-hides (thus maximizing space) when not in
 * use, but can also be pinned open if so desired. It will only be visible when
 * the PaletteView is not.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class FlyoutPaletteComposite extends Composite {

    private static final FontManager FONT_MGR = new FontManager();

    private static final String PROPERTY_PALETTE_WIDTH = "org.eclipse.gef.ui.palette.fpa.paletteWidth"; //$NON-NLS-1$
    private static final String PROPERTY_STATE = "org.eclipse.gef.ui.palette.fpa.state"; //$NON-NLS-1$
    private static final String PROPERTY_DOCK_LOCATION = "org.eclipse.gef.ui.palette.fpa.dock"; //$NON-NLS-1$

    private static final int DEFAULT_PALETTE_SIZE = 125;
    private static final int MIN_PALETTE_SIZE = 20;
    private static final int MAX_PALETTE_SIZE = 500;

    private static final int STATE_HIDDEN = 8;
    private static final int STATE_EXPANDED = 1;

    private static final Dimension ARROW_SIZE = new Dimension(6, 11);
    private static final int SASH_BUTTON_WIDTH = 11;

    /**
     * One of the two possible initial states of the flyout palette. This is the
     * default one. When in this state, only the flyout palette's sash is
     * visible.
     */
    public static final int STATE_COLLAPSED = 2;
    /**
     * One of the two possible initial states of the flyout palette. When in
     * this state, the flyout palette is completely visible and pinned open so
     * that it doesn't disappear when the user wanders away from the flyout.
     */
    public static final int STATE_PINNED_OPEN = 4;

    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private Composite paletteContainer;
    private PaletteViewer pViewer, externalViewer;
    private IMemento capturedPaletteState;
    private Control graphicalControl;
    private Composite sash;
    private PaletteViewerProvider provider;
    private FlyoutPreferences prefs;
    private Point cachedBounds = new Point(0, 0);
    /*
     * Fix for Bug# 71525 transferFocus is used to transfer focus from the
     * button in the vertical sash title to the button in the horizontal
     * paletteComposite title. When either button is pressed it is set to true,
     * and when either the sash or the paletteComposite gets notified of the
     * change in state, they transfer the focus to their button if this flag is
     * set to true and if that button is visible.
     */
    private boolean transferFocus = false;
    private int dock = PositionConstants.EAST;
    private int paletteState = STATE_HIDDEN;
    private int paletteWidth = DEFAULT_PALETTE_SIZE;
    private int minWidth = MIN_PALETTE_SIZE;
    private int cachedSize = -1, cachedState = -1, cachedLocation = -1;
    private int cachedTitleHeight = 24; // give it a default value

    private IPerspectiveListener perspectiveListener = new IPerspectiveListener() {
        @Override
        public void perspectiveActivated(IWorkbenchPage page,
                IPerspectiveDescriptor perspective) {
            handlePerspectiveActivated(page, perspective);
        }

        @Override
        public void perspectiveChanged(IWorkbenchPage page,
                IPerspectiveDescriptor perspective, String changeId) {
            handlePerspectiveChanged(page, perspective, changeId);
        }
    };

    /**
     * Constructor
     * 
     * @param parent
     *            The parent Composite
     * @param style
     *            The style of the widget to construct; only SWT.BORDER is
     *            allowed
     * @param page
     *            The current workbench page
     * @param pvProvider
     *            The provider that is to be used to create the flyout palette
     * @param preferences
     *            To save/retrieve the preferences for the flyout
     */
    public FlyoutPaletteComposite(Composite parent, int style,
            IWorkbenchPage page, PaletteViewerProvider pvProvider,
            FlyoutPreferences preferences) {
        super(parent, style | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE
                | SWT.DOUBLE_BUFFERED);
        provider = pvProvider;
        prefs = preferences;
        sash = createSash();
        paletteContainer = createPaletteContainer();
        hookIntoWorkbench(page.getWorkbenchWindow());

        // Initialize the state properly
        if (prefs.getPaletteWidth() <= 0)
            prefs.setPaletteWidth(DEFAULT_PALETTE_SIZE);
        setPaletteWidth(prefs.getPaletteWidth());
        setDockLocation(prefs.getDockLocation());
        updateState(page);

        addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle area = getClientArea();
                /*
                 * @TODO:Pratik Sometimes, the editor is resized to 1,1 or 0,0
                 * (depending on the platform) when the editor is closed or
                 * maximized. We have to ignore such resizes. See Bug# 62748
                 */
                if (area.width > minWidth)
                    layout(true);
            }
        });

        listeners.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String property = evt.getPropertyName();
                if (property.equals(PROPERTY_PALETTE_WIDTH))
                    prefs.setPaletteWidth(paletteWidth);
                else if (property.equals(PROPERTY_DOCK_LOCATION))
                    prefs.setDockLocation(dock);
                else if (property.equals(PROPERTY_STATE))
                    if (paletteState == STATE_COLLAPSED
                            || paletteState == STATE_PINNED_OPEN)
                        prefs.setPaletteState(paletteState);
            }
        });
    }

    private void addListenerToCtrlHierarchy(Control parent, int eventType,
            Listener listener) {
        parent.addListener(eventType, listener);
        if (!(parent instanceof Composite))
            return;
        Control[] children = ((Composite) parent).getChildren();
        for (int i = 0; i < children.length; i++) {
            addListenerToCtrlHierarchy(children[i], eventType, listener);
        }
    }

    private IMemento capturePaletteState(PaletteViewer viewer) {
        IMemento memento = XMLMemento.createWriteRoot("paletteState"); //$NON-NLS-1$
        try {
            viewer.saveState(memento);
        } catch (RuntimeException re) {
            // Bug 74843 -- See comment #1
            // If there's a problem with saving the palette's state, it simply
            // won't be
            // transferred to the new palette
            memento = null;
            /*
             * @TODO:Pratik You should log this exception.
             */
        }
        return memento;
    }

    private Control createFlyoutControlButton(Composite parent) {
        return new ButtonCanvas(parent);
    }

    /**
     * This is a convenient method to get a default FlyoutPreferences object.
     * The returned FlyoutPreferences does not save any changes made to the
     * given {@link Preferences Preferences}. It's upto the owner plugin to
     * {@link Plugin#savePluginPreferences() save} the changes before it
     * {@link Plugin#stop(org.osgi.framework.BundleContext) stops}.
     * 
     * @param prefs
     *            {@link Plugin#getPluginPreferences() a plugin's Preferences}
     * @return a default implementation of FlyoutPreferences that stores the
     *         settings in the given Preferences
     * @since 3.2
     */
    public static FlyoutPreferences createFlyoutPreferences(Preferences prefs) {
        return new DefaultFlyoutPreferences(prefs);
    }

    private Composite createPaletteContainer() {
        return new PaletteComposite(this, SWT.NO_BACKGROUND
                | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED);
    }

    private Composite createSash() {
        return new Sash(this, SWT.NONE);
    }

    private Control createTitle(Composite parent, boolean isHorizontal) {
        return new TitleCanvas(parent, isHorizontal);
    }

    private Control getPaletteViewerControl() {
        Control result = null;
        if (pViewer != null)
            result = pViewer.getControl();
        // Fix for bug 101703 -- pViewer.getControl().getParent() might be
        // parented
        // by paletteContainer
        if (result != null && !result.isDisposed()
                && result.getParent() != paletteContainer)
            result = result.getParent();
        return result;
    }

    private void handlePerspectiveActivated(IWorkbenchPage page,
            IPerspectiveDescriptor perspective) {
        updateState(page);
    }

    private void handlePerspectiveChanged(IWorkbenchPage page,
            IPerspectiveDescriptor perspective, String changeId) {
        if (changeId.equals(IWorkbenchPage.CHANGE_VIEW_SHOW)
                || changeId.equals(IWorkbenchPage.CHANGE_VIEW_HIDE))
            updateState(page);
    }

    // Will return false if the ancestor or descendant is null
    private boolean isDescendantOf(Control ancestor, Control descendant) {
        if (ancestor == null || descendant == null)
            return false;
        while (descendant != null) {
            if (ancestor == descendant)
                return true;
            descendant = descendant.getParent();
        }
        return false;
    }

    private boolean isInState(int state) {
        return (paletteState & state) != 0;
    }

    private boolean isMirrored() {
        return (getStyle() & SWT.MIRRORED) != 0;
    }

    /**
     * @see Composite#layout(boolean)
     */
    @Override
    public void layout(boolean changed) {
        if (graphicalControl == null || graphicalControl.isDisposed())
            return;

        Rectangle area = getClientArea();
        if (area.width == 0 || area.height == 0)
            return;

        int sashWidth = sash.computeSize(-1, -1).x;
        int pWidth = paletteWidth;
        int maxWidth = Math.min(area.width / 2, MAX_PALETTE_SIZE);
        maxWidth = Math.max(maxWidth, minWidth);
        pWidth = Math.max(pWidth, minWidth);
        pWidth = Math.min(pWidth, maxWidth);

        /*
         * Fix for Bug# 65892 Laying out only when necessary helps reduce
         * flicker on GTK in the case where the flyout palette is being resized
         * past its maximum size.
         */
        if (paletteState == cachedState && pWidth == cachedSize
                && cachedLocation == dock && cachedBounds == getSize())
            return;
        cachedState = paletteState;
        cachedSize = pWidth;
        cachedLocation = dock;
        cachedBounds = getSize();

        // #65892 on Mac Cocoa, the redraw causes great flickering, therefore we
        // skip it there
        if (!Platform.getWS().equals(Platform.WS_COCOA)) {
            setRedraw(false);
        }
        if (isInState(STATE_HIDDEN)) {
            sash.setVisible(false);
            paletteContainer.setVisible(false);
            graphicalControl.setBounds(area);
        } else if (dock == PositionConstants.EAST)
            layoutComponentsEast(area, sashWidth, pWidth);
        else
            layoutComponentsWest(area, sashWidth, pWidth);
        sash.layout();
        // #65892 see above
        if (!Platform.getWS().equals(Platform.WS_COCOA)) {
            setRedraw(true);
        }
        update();
    }

    private void layoutComponentsEast(Rectangle area, int sashWidth, int pWidth) {
        if (isInState(STATE_COLLAPSED)) {
            paletteContainer.setVisible(false);
            sash.setBounds(area.x + area.width - sashWidth, area.y, sashWidth,
                    area.height);
            sash.setVisible(true);
            graphicalControl.setBounds(area.x, area.y, area.width - sashWidth,
                    area.height);
        } else if (isInState(STATE_EXPANDED)) {
            paletteContainer.moveAbove(graphicalControl);
            sash.moveAbove(paletteContainer);
            sash.setBounds(area.x + area.width - pWidth - sashWidth, area.y,
                    sashWidth, area.height);
            paletteContainer.setBounds(area.x + area.width - pWidth, area.y,
                    pWidth, area.height);
            sash.setVisible(true);
            paletteContainer.setVisible(true);
            graphicalControl.setBounds(area.x, area.y, area.width - sashWidth,
                    area.height);
        } else if (isInState(STATE_PINNED_OPEN)) {
            sash.setBounds(area.x + area.width - pWidth - sashWidth, area.y,
                    sashWidth, area.height);
            paletteContainer.setBounds(area.x + area.width - pWidth, area.y,
                    pWidth, area.height);
            sash.setVisible(true);
            paletteContainer.setVisible(true);
            graphicalControl.setBounds(area.x, area.y, area.width - sashWidth
                    - pWidth, area.height);
        }
    }

    private void layoutComponentsWest(Rectangle area, int sashWidth, int pWidth) {
        if (isInState(STATE_COLLAPSED)) {
            paletteContainer.setVisible(false);
            sash.setBounds(area.x, area.y, sashWidth, area.height);
            sash.setVisible(true);
            graphicalControl.setBounds(area.x + sashWidth, area.y, area.width
                    - sashWidth, area.height);
        } else if (isInState(STATE_EXPANDED)) {
            paletteContainer.setVisible(true);
            paletteContainer.moveAbove(graphicalControl);
            sash.moveAbove(paletteContainer);
            sash.setBounds(area.x + pWidth, area.y, sashWidth, area.height);
            paletteContainer.setBounds(area.x, area.y, pWidth, area.height);
            sash.setVisible(true);
            graphicalControl.setBounds(area.x + sashWidth, area.y, area.width
                    - sashWidth, area.height);
        } else if (isInState(STATE_PINNED_OPEN)) {
            paletteContainer.setVisible(true);
            sash.setBounds(area.x + pWidth, area.y, sashWidth, area.height);
            paletteContainer.setBounds(area.x, area.y, pWidth, area.height);
            sash.setVisible(true);
            graphicalControl.setBounds(area.x + pWidth + sashWidth, area.y,
                    area.width - sashWidth - pWidth, area.height);
        }
    }

    private void hookIntoWorkbench(final IWorkbenchWindow window) {
        window.addPerspectiveListener(perspectiveListener);
        addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                window.removePerspectiveListener(perspectiveListener);
                perspectiveListener = null;
            }
        });
    }

    private boolean restorePaletteState(PaletteViewer newPalette, IMemento state) {
        if (state != null) {
            try {
                return newPalette.restoreState(state);
            } catch (RuntimeException re) {
                /*
                 * @TODO:Pratik You should log this exception
                 */
            }
        }
        return false;
    }

    /**
     * If an external palette viewer is provided, palette state (that is
     * captured in {@link PaletteViewer#saveState(IMemento)} -- active tool,
     * drawer expansion state, drawer pin state, etc.) will be maintained when
     * switching between the two viewers. Providing an external viewer, although
     * recommended, is optional.
     * 
     * @param viewer
     *            The palette viewer used in the PaletteView
     */
    public void setExternalViewer(PaletteViewer viewer) {
        if (viewer == null && externalViewer != null)
            capturedPaletteState = capturePaletteState(externalViewer);
        externalViewer = viewer;
        if (externalViewer != null && pViewer != null)
            transferState(pViewer, externalViewer);
    }

    private void setDockLocation(int position) {
        if (position != PositionConstants.EAST
                && position != PositionConstants.WEST)
            return;
        if (position != dock) {
            int oldPosition = dock;
            dock = position;
            listeners.firePropertyChange(PROPERTY_DOCK_LOCATION, oldPosition,
                    dock);
            if (pViewer != null)
                layout(true);
        }
    }

    private void setPaletteWidth(int newSize) {
        if (paletteWidth != newSize) {
            int oldValue = paletteWidth;
            paletteWidth = newSize;
            listeners.firePropertyChange(PROPERTY_PALETTE_WIDTH, oldValue,
                    paletteWidth);
            if (pViewer != null)
                layout(true);
        }
    }

    /**
     * Sets the control along the side of which the palette is to be displayed.
     * The given Control should be a child of this Composite. This method should
     * only be invoked once.
     * 
     * @param graphicalViewer
     *            the control of the graphical viewer; cannot be
     *            <code>null</code>
     */
    public void setGraphicalControl(Control graphicalViewer) {
        Assert.isTrue(graphicalViewer != null);
        Assert.isTrue(graphicalViewer.getParent() == this);
        Assert.isTrue(graphicalControl == null);
        graphicalControl = graphicalViewer;
        addListenerToCtrlHierarchy(graphicalControl, SWT.MouseEnter,
                new Listener() {
                    @Override
                    public void handleEvent(Event event) {
                        if (!isInState(STATE_EXPANDED))
                            return;
                        Display.getCurrent().timerExec(250, new Runnable() {
                            @Override
                            public void run() {
                                if (isDescendantOf(graphicalControl, Display
                                        .getCurrent().getCursorControl())
                                        && isInState(STATE_EXPANDED))
                                    setState(STATE_COLLAPSED);
                            }
                        });
                    }
                });
    }

    /*
     * @TODO:Pratik For 4.0, change the parameter of this method to be
     * EditpartViewer instead of GraphicalViewer.
     */
    /**
     * This method hooks a DropTargetListener that collapses the flyout patette
     * when the user drags something from the palette and moves the cursor to
     * the primary viewer's control. If the auto-hide feature of the palette is
     * to work properly when dragging, this method should be called before any
     * other drop target listeners are added to the primary viewer.
     * 
     * @param viewer
     *            the primary viewer
     */
    public void hookDropTargetListener(GraphicalViewer viewer) {
        viewer.addDropTargetListener(new TransferDropTargetListener() {
            @Override
            public void dragEnter(DropTargetEvent event) {
            }

            @Override
            public void dragLeave(DropTargetEvent event) {
            }

            @Override
            public void dragOperationChanged(DropTargetEvent event) {
            }

            @Override
            public void dragOver(DropTargetEvent event) {
            }

            @Override
            public void drop(DropTargetEvent event) {
            }

            @Override
            public void dropAccept(DropTargetEvent event) {
            }

            @Override
            public Transfer getTransfer() {
                return TemplateTransfer.getInstance();
            }

            @Override
            public boolean isEnabled(DropTargetEvent event) {
                if (isInState(STATE_EXPANDED))
                    setState(STATE_COLLAPSED);
                return false;
            }
        });
    }

    /*
     * If the given state is invalid (as could be the case when
     * FlyoutPreferences.getPaletteState() is invoked for the first time), it
     * will be defaulted to STATE_COLLAPSED.
     */
    private void setState(int newState) {
        /*
         * Fix for Bug# 69617 and Bug# 81248 FlyoutPreferences.getPaletteState()
         * could return an invalid state if none is stored. In that case, we use
         * the default state: STATE_COLLAPSED.
         */
        if (newState != STATE_HIDDEN && newState != STATE_PINNED_OPEN
                && newState != STATE_EXPANDED)
            newState = STATE_COLLAPSED;
        if (paletteState == newState)
            return;
        int oldState = paletteState;
        paletteState = newState;
        switch (paletteState) {
        case STATE_EXPANDED:
        case STATE_COLLAPSED:
        case STATE_PINNED_OPEN:
            if (pViewer == null) {
                pViewer = provider.createPaletteViewer(paletteContainer);
                if (externalViewer != null)
                    transferState(externalViewer, pViewer);
                else
                    restorePaletteState(pViewer, capturedPaletteState);
                capturedPaletteState = null;
                minWidth = Math.max(pViewer.getControl().computeSize(0, 0).x,
                        MIN_PALETTE_SIZE);
            }
            break;
        case STATE_HIDDEN:
            if (pViewer == null)
                break;
            if (externalViewer != null) {
                provider.getEditDomain().setPaletteViewer(externalViewer);
                transferState(pViewer, externalViewer);
            }
            if (provider.getEditDomain().getPaletteViewer() == pViewer)
                provider.getEditDomain().setPaletteViewer(null);
            Control pViewerCtrl = getPaletteViewerControl();
            if (pViewerCtrl != null && !pViewerCtrl.isDisposed())
                pViewerCtrl.dispose();
            pViewer = null;
        }
        /*
         * Fix for Bug# 63901 When the flyout collapses, if the palette has
         * focus, throw focus to the graphical control. That way, hitting ESC
         * will still deactivate the current tool and load the default one. Note
         * that focus is being set on RulerComposite and not GraphicalViewer's
         * control. But this is okay since RulerComposite passes the focus on to
         * its first child, which is the graphical viewer's control.
         */
        if (paletteState == STATE_COLLAPSED
                && pViewer.getControl().isFocusControl())
            graphicalControl.setFocus();
        layout(true);
        listeners.firePropertyChange(PROPERTY_STATE, oldState, newState);
    }

    private void transferState(PaletteViewer src, PaletteViewer dest) {
        restorePaletteState(dest, capturePaletteState(src));
    }

    private void updateState(IWorkbenchPage page) {
        IViewReference view = page.findViewReference(PaletteView.ID);
        if (view == null && isInState(STATE_HIDDEN))
            setState(prefs.getPaletteState());
        if (view != null && !isInState(STATE_HIDDEN))
            setState(STATE_HIDDEN);
    }

    /**
     * FlyoutPreferences is used to save/load the preferences for the flyout
     * palette.
     * 
     * @author Pratik Shah
     * @since 3.0
     */
    public interface FlyoutPreferences {
        /**
         * Should return {@link PositionConstants#EAST} or
         * {@link PositionConstants#WEST}. Any other int will be ignored and the
         * default dock location (EAST) will be used instead.
         * 
         * @return the saved dock location of the Palette
         */
        int getDockLocation();

        /**
         * When there is no saved state, this method can return any non-positive
         * int (which will result in the palette using the default state --
         * collapsed), or {@link FlyoutPaletteComposite#STATE_COLLAPSED}, or
         * {@link FlyoutPaletteComposite#STATE_PINNED_OPEN}
         * 
         * @return the saved state of the palette
         */
        int getPaletteState();

        /**
         * When there is no saved width, this method can return any int
         * (preferrably a non-positive int). Returning a non-positive int will
         * cause the palette to be sized to the default size, whereas returning
         * a postive int will find the closest match in the valid range (>=
         * minimum and <= maximum)
         * 
         * @return the saved width of the flyout palette
         */
        int getPaletteWidth();

        /**
         * This method is invoked when the flyout palette's dock location is
         * changed. The provided dock location should be persisted and returned
         * in {@link #getDockLocation()}.
         * 
         * @param location
         *            {@link PositionConstants#EAST} or
         *            {@link PositionConstants#WEST}
         */
        void setDockLocation(int location);

        /**
         * This method is invoked when the flyout palette's state is changed
         * (the new state becomes the default). The provided state should be
         * persisted and returned in {@link #getPaletteState()}.
         * 
         * @param state
         *            {@link FlyoutPaletteComposite#STATE_COLLAPSED} or
         *            {@link FlyoutPaletteComposite#STATE_PINNED_OPEN}
         */
        void setPaletteState(int state);

        /**
         * This method is invoked when the flyout palette is resized. The
         * provided width should be persisted and returned in
         * {@link #getPaletteWidth()}.
         * 
         * @param width
         *            the new size of the flyout palette
         */
        void setPaletteWidth(int width);
    }

    private class Sash extends Composite {
        private Control button;

        public Sash(Composite parent, int style) {
            super(parent, style);
            button = createFlyoutControlButton(this);
            new SashDragManager();

            addMouseTrackListener(new MouseTrackAdapter() {
                @Override
                public void mouseHover(MouseEvent e) {
                    if (isInState(STATE_COLLAPSED))
                        setState(STATE_EXPANDED);
                }
            });

            addListener(SWT.Paint, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    paintSash(event.gc);
                }
            });

            addListener(SWT.Resize, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    layout(true);
                }
            });

            listeners.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(PROPERTY_STATE))
                        updateState();
                }
            });
        }

        @Override
        public Point computeSize(int wHint, int hHint, boolean changed) {
            if (isInState(STATE_PINNED_OPEN))
                return new Point(3, 3);

            // button size plus two pixels for the two lines to be drawn
            return new Point(SASH_BUTTON_WIDTH + 2, cachedTitleHeight);
        }

        private void handleSashDragged(int shiftAmount) {
            int newSize = paletteContainer.getBounds().width
                    + (dock == PositionConstants.EAST ? -shiftAmount
                            : shiftAmount);
            setPaletteWidth(newSize);
        }

        @Override
        public void layout(boolean changed) {
            if (button == null)
                return;

            if (isInState(STATE_PINNED_OPEN)) {
                button.setVisible(false);
                return;
            }

            button.setVisible(true);
            Rectangle area = getClientArea();
            button.setBounds(area.x + 1, area.y + 1, SASH_BUTTON_WIDTH,
                    cachedTitleHeight - 1);

            if (transferFocus) {
                transferFocus = false;
                button.setFocus();
            }
        }

        private void paintSash(GC gc) {
            Rectangle bounds = getBounds();
            if (isInState(STATE_PINNED_OPEN)) {
                gc.setBackground(PaletteColorUtil.WIDGET_BACKGROUND);
                gc.fillRectangle(0, 0, bounds.width, bounds.height);

                gc.setForeground(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
                gc.drawLine(0, 0, bounds.width, 0);
                gc.setForeground(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
                gc.drawLine(0, bounds.height - 1, bounds.width - 1,
                        bounds.height - 1);
                gc.setForeground(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
                gc.drawLine(0, 0, 0, bounds.height);
                gc.setForeground(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
                gc.drawLine(bounds.width - 1, 0, bounds.width - 1,
                        bounds.height - 1);
            } else {
                gc.setForeground(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
                gc.drawLine(0, 0, 0, bounds.height);
                gc.drawLine(bounds.width - 1, 0, bounds.width - 1,
                        bounds.height);

                gc.setForeground(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
                gc.drawLine(1, 0, 1, bounds.height);

                gc.setForeground(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
                gc.drawLine(2, 0, 2, bounds.height);
            }
        }

        private void updateState() {
            setCursor(isInState(STATE_EXPANDED | STATE_PINNED_OPEN) ? Cursors.SIZEWE
                    : null);
        }

        private class SashDragManager extends MouseAdapter implements
                MouseMoveListener {
            protected boolean dragging = false;
            protected boolean correctState = false;
            protected boolean mouseDown = false;
            protected int origX;
            protected Listener keyListener = new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (event.keyCode == SWT.ALT || event.keyCode == SWT.ESC) {
                        dragging = false;
                        Display.getCurrent().removeFilter(SWT.KeyDown, this);
                    }
                    event.doit = false;
                    event.type = SWT.None;
                }
            };

            public SashDragManager() {
                Sash.this.addMouseMoveListener(this);
                Sash.this.addMouseListener(this);
            }

            @Override
            public void mouseDown(MouseEvent me) {
                if (me.button != 1)
                    return;
                mouseDown = true;
                correctState = isInState(STATE_EXPANDED | STATE_PINNED_OPEN);
                origX = me.x;
                Display.getCurrent().addFilter(SWT.KeyDown, keyListener);
            }

            @Override
            public void mouseMove(MouseEvent me) {
                if (mouseDown)
                    dragging = true;
                if (dragging && correctState)
                    handleSashDragged(me.x - origX);
            }

            @Override
            public void mouseUp(MouseEvent me) {
                Display.getCurrent().removeFilter(SWT.KeyDown, keyListener);
                if (!dragging && me.button == 1) {
                    if (isInState(STATE_COLLAPSED))
                        setState(STATE_EXPANDED);
                    else if (isInState(STATE_EXPANDED))
                        setState(STATE_COLLAPSED);
                }
                dragging = false;
                correctState = false;
                mouseDown = false;
            }
        }
    }

    private class ResizeAction extends Action {
        public ResizeAction() {
            super(PaletteMessages.RESIZE_LABEL);
        }

        @Override
        public boolean isEnabled() {
            return !isInState(STATE_COLLAPSED);
        }

        @Override
        public void run() {
            final Tracker tracker = new Tracker(FlyoutPaletteComposite.this,
                    SWT.RIGHT | SWT.LEFT);
            Rectangle[] rects = new Rectangle[1];
            rects[0] = sash.getBounds();
            tracker.setCursor(Cursors.SIZEE);
            tracker.setRectangles(rects);
            if (tracker.open()) {
                int deltaX = sash.getBounds().x - tracker.getRectangles()[0].x;
                if (dock == PositionConstants.WEST)
                    deltaX = -deltaX;
                setPaletteWidth(paletteContainer.getBounds().width + deltaX);
            }
            tracker.dispose();
        }
    }

    private class TitleDragManager extends MouseAdapter implements Listener,
            MouseTrackListener {
        protected boolean switchDock = false;
        @SuppressWarnings("unused")
        protected boolean dragging = false;
        protected int threshold;

        public TitleDragManager(Control ctrl) {
            ctrl.addListener(SWT.DragDetect, this);
            ctrl.addMouseListener(this);
            ctrl.addMouseTrackListener(this);
        }

        @Override
        public void handleEvent(Event event) {
            dragging = true;
            switchDock = false;
            threshold = dock == PositionConstants.EAST ? Integer.MAX_VALUE / 2
                    : -1;
            final Composite flyout = FlyoutPaletteComposite.this;
            final Rectangle flyoutBounds = flyout.getBounds();
            final int switchThreshold = flyoutBounds.x
                    + (flyoutBounds.width / 2);
            Rectangle bounds = sash.getBounds();
            if (paletteContainer.getVisible())
                bounds = bounds.union(paletteContainer.getBounds());
            final Rectangle origBounds = Display.getCurrent().map(flyout, null,
                    bounds);
            final Tracker tracker = new Tracker(Display.getDefault(), SWT.NULL);
            tracker.setRectangles(new Rectangle[] { origBounds });
            tracker.setStippled(true);
            tracker.addListener(SWT.Move, new Listener() {
                @Override
                public void handleEvent(final Event evt) {
                    Display.getCurrent().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            Control ctrl = Display.getCurrent()
                                    .getCursorControl();
                            Point pt = flyout.toControl(evt.x, evt.y);
                            switchDock = isDescendantOf(graphicalControl, ctrl)
                                    && ((dock == PositionConstants.WEST && pt.x > threshold - 10) || (dock == PositionConstants.EAST && pt.x < threshold + 10));
                            boolean invalid = false;
                            if (!switchDock)
                                invalid = !isDescendantOf(
                                        FlyoutPaletteComposite.this, ctrl);
                            if (switchDock) {
                                if (dock == PositionConstants.WEST) {
                                    threshold = Math.max(threshold, pt.x);
                                    threshold = Math.min(threshold,
                                            switchThreshold);
                                } else {
                                    threshold = Math.min(threshold, pt.x);
                                    threshold = Math.max(threshold,
                                            switchThreshold);
                                }
                            }
                            Rectangle placeHolder = origBounds;
                            if (switchDock) {
                                if (dock == PositionConstants.EAST)
                                    placeHolder = new Rectangle(0, 0,
                                            origBounds.width, origBounds.height);
                                else
                                    placeHolder = new Rectangle(
                                            flyoutBounds.width
                                                    - origBounds.width, 0,
                                            origBounds.width, origBounds.height);
                                placeHolder = Display.getCurrent().map(flyout,
                                        null, placeHolder);
                            }
                            // update the cursor
                            int cursor;
                            if (invalid)
                                cursor = DragCursors.INVALID;
                            else if ((!switchDock && dock == PositionConstants.EAST)
                                    || (switchDock && dock == PositionConstants.WEST))
                                cursor = DragCursors.RIGHT;
                            else
                                cursor = DragCursors.LEFT;
                            if (isMirrored()) {
                                if (cursor == DragCursors.RIGHT)
                                    cursor = DragCursors.LEFT;
                                else if (cursor == DragCursors.LEFT)
                                    cursor = DragCursors.RIGHT;
                            }
                            tracker.setCursor(DragCursors.getCursor(cursor));
                            // update the rectangle only if it has changed
                            if (!tracker.getRectangles()[0].equals(placeHolder))
                                tracker.setRectangles(new Rectangle[] { placeHolder });
                        }
                    });
                }
            });
            if (tracker.open()) {
                if (switchDock)
                    setDockLocation(PositionConstants.EAST_WEST & ~dock);
                // mouse up is received by the tracker and by this listener, so
                // we set dragging
                // to be false
                dragging = false;
            }
            tracker.dispose();
        }

        @Override
        public void mouseEnter(MouseEvent e) {
        }

        @Override
        public void mouseExit(MouseEvent e) {
        }

        @Override
        public void mouseHover(MouseEvent e) {
            /*
             * @TODO:Pratik Mouse hover events are received if the hover occurs
             * just before you finish or cancel the drag. Open a bugzilla about
             * it?
             */
            if (isInState(STATE_COLLAPSED))
                setState(STATE_EXPANDED);
        }

        @Override
        public void mouseUp(MouseEvent me) {
            if (me.button != 1)
                return;
            if (isInState(STATE_COLLAPSED))
                setState(STATE_EXPANDED);
            else if (isInState(STATE_EXPANDED))
                setState(STATE_COLLAPSED);
        }
    }

    private class PaletteComposite extends Composite {
        protected Control button, title;

        public PaletteComposite(Composite parent, int style) {
            super(parent, style);
            createComponents();

            listeners.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(PROPERTY_STATE))
                        updateState();
                    else if (evt.getPropertyName().equals(
                            PROPERTY_DOCK_LOCATION))
                        if (getVisible())
                            layout(true);
                }
            });

            addListener(SWT.Resize, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    layout(true);
                }
            });

            updateState();
        }

        protected void createComponents() {
            title = createTitle(this, true);
            button = createFlyoutControlButton(this);
        }

        @Override
        public void layout(boolean changed) {
            Control pCtrl = getPaletteViewerControl();
            if (pCtrl == null || pCtrl.isDisposed())
                return;

            Rectangle area = getClientArea();
            
            // Added by Phillipus
            // On Mac opening a new diagram of a different type than one already open causes the arrow button not drawn
            if(area.isEmpty()) {
                return;
            }
            
            boolean buttonVisible = button.getVisible();
            Point titleSize = title.computeSize(-1, -1);
            Point buttonSize = buttonVisible ? button.computeSize(-1, -1)
                    : new Point(0, 0);
            cachedTitleHeight = Math.max(titleSize.y, buttonSize.y);
            if (buttonVisible) {
                buttonSize.x = Math.max(cachedTitleHeight, buttonSize.x);
            }
            if (dock == PositionConstants.EAST) {
                int buttonX = area.width - buttonSize.x;
                button.setBounds(buttonX, 0, buttonSize.x, cachedTitleHeight);
                title.setBounds(0, 0, buttonX, cachedTitleHeight);
            } else {
                int titleX = buttonSize.x;
                button.setBounds(0, 0, buttonSize.x, cachedTitleHeight);
                title.setBounds(titleX, 0, area.width - titleX,
                        cachedTitleHeight);
            }
            area.y += cachedTitleHeight;
            area.height -= cachedTitleHeight;
            pCtrl.setBounds(area);
        }

        protected void updateState() {
            button.setVisible(isInState(STATE_PINNED_OPEN));
            if (transferFocus && button.getVisible()) {
                transferFocus = false;
                button.setFocus();
            }
            layout(true);
        }
    }

    private static class TitleLabel extends Label {
        protected static final Border BORDER = new MarginBorder(4, 3, 4, 3);
        protected static final Border TOOL_TIP_BORDER = new MarginBorder(0, 2,
                0, 2);

        public TitleLabel(boolean isHorizontal) {
            super(GEFMessages.Palette_Label, InternalImages
                    .get(InternalImages.IMG_PALETTE));
            setLabelAlignment(PositionConstants.LEFT);
            setBorder(BORDER);
            Label tooltip = new Label(getText());
            tooltip.setBorder(TOOL_TIP_BORDER);
            setToolTip(tooltip);
            
            // Added by Phillipus
            //setForegroundColor(ColorConstants.listForeground);
            setForegroundColor(PaletteColorUtil.PALETTE_TITLE_LABEL_FOREGROUND);
        }

        @Override
        public IFigure getToolTip() {
            if (isTextTruncated())
                return super.getToolTip();
            return null;
        }

        @Override
        protected void paintFigure(Graphics graphics) {

            // paint the gradient
            graphics.pushState();
            org.eclipse.draw2d.geometry.Rectangle r = org.eclipse.draw2d.geometry.Rectangle.SINGLETON;
            r.setBounds(getBounds());
            
            // Added by Phillipus
            //graphics.setForegroundColor(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
            //graphics.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND);
            graphics.setForegroundColor(PaletteColorUtil.PALETTE_TITLE_LABEL_BACKGROUND_START);
            graphics.setBackgroundColor(PaletteColorUtil.PALETTE_TITLE_LABEL_BACKGROUND_END);
            
            graphics.fillGradient(r, true);

            // draw bottom border
            graphics.setForegroundColor(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
            graphics.drawLine(r.getBottomLeft().getTranslated(0, -1), r
                    .getBottomRight().getTranslated(0, -1));

            graphics.popState();

            // paint the text and icon
            super.paintFigure(graphics);

            // paint the focus rectangle around the text
            if (hasFocus()) {
                org.eclipse.draw2d.geometry.Rectangle textBounds = getTextBounds();
                // We reduce the width by 1 because FigureUtilities grows it by
                // 1 unnecessarily
                textBounds.width--;
                graphics.drawFocus(bounds.getResized(-1, -1).intersect(
                        textBounds.getExpanded(getInsets())));
            }
        }
    }

    private class ButtonCanvas extends Canvas {
        private LightweightSystem lws;

        public ButtonCanvas(Composite parent) {
            super(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
            init();
            provideAccSupport();
        }

        @Override
        public Point computeSize(int wHint, int hHint, boolean changed) {
            Dimension size = lws.getRootFigure().getPreferredSize(wHint, hHint);
            size.union(new Dimension(wHint, hHint));
            return new org.eclipse.swt.graphics.Point(size.width, size.height);
        }

        private int getArrowDirection() {
            int direction = PositionConstants.EAST;
            if (isInState(STATE_EXPANDED | STATE_PINNED_OPEN))
                direction = dock == PositionConstants.WEST ? PositionConstants.WEST
                        : PositionConstants.EAST;
            else
                direction = dock == PositionConstants.WEST ? PositionConstants.EAST
                        : PositionConstants.WEST;
            if (isMirrored()) {
                if (direction == PositionConstants.WEST)
                    direction = PositionConstants.EAST;
                else
                    direction = PositionConstants.WEST;
            }
            return direction;
        }

        private String getButtonTooltipText() {
            if (isInState(STATE_COLLAPSED))
                return PaletteMessages.PALETTE_SHOW;
            return PaletteMessages.PALETTE_HIDE;
        }

        private void init() {
            setCursor(Cursors.ARROW);
            lws = new LightweightSystem();
            lws.setControl(this);
            final ArrowButton b = new ArrowButton(getArrowDirection());
            b.setRolloverEnabled(true);
            b.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    transferFocus = true;
                    if (isInState(STATE_COLLAPSED))
                        setState(STATE_PINNED_OPEN);
                    else
                        setState(STATE_COLLAPSED);
                }
            });
            listeners.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(PROPERTY_STATE)) {
                        b.setDirection(getArrowDirection());
                        setToolTipText(getButtonTooltipText());
                    } else if (evt.getPropertyName().equals(
                            PROPERTY_DOCK_LOCATION))
                        b.setDirection(getArrowDirection());
                }
            });
            lws.setContents(b);
        }

        private void provideAccSupport() {
            getAccessible().addAccessibleListener(new AccessibleAdapter() {
                @Override
                public void getDescription(AccessibleEvent e) {
                    e.result = PaletteMessages.ACC_DESC_PALETTE_BUTTON;
                }

                @Override
                public void getHelp(AccessibleEvent e) {
                    getDescription(e);
                }

                @Override
                public void getName(AccessibleEvent e) {
                    e.result = getToolTipText();
                }
            });
            getAccessible().addAccessibleControlListener(
                    new AccessibleControlAdapter() {
                        @Override
                        public void getRole(AccessibleControlEvent e) {
                            e.detail = ACC.ROLE_PUSHBUTTON;
                        }
                    });
        }

        private class ArrowButton extends Button {

            private Triangle triangle;

            /**
             * Creates a new instance
             * 
             * @param direction
             *            the direction the arrow should face
             *            (PositionConstants.RIGHT or PositionConstants.LEFT)
             */
            public ArrowButton(int direction) {
                super();
                setDirection(direction);

                triangle = new Triangle();
                triangle.setOutline(true);
                
                // Added by Phillipus
                //triangle.setBackgroundColor(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
                //triangle.setForegroundColor(PaletteColorUtil.WIDGET_DARK_SHADOW);
                triangle.setBackgroundColor(PaletteColorUtil.PALETTE_ARROW_BUTTON_BACKGROUND);
                triangle.setForegroundColor(PaletteColorUtil.PALETTE_ARROW_BUTTON_FOREGROUND);
                
                setContents(triangle);
            }

            public void setDirection(int direction) {
                if (triangle != null) {
                    triangle.setDirection(direction);
                }
            }

            @Override
            protected void layout() {
                org.eclipse.draw2d.geometry.Rectangle clientArea = getBounds();

                triangle.setBounds(new org.eclipse.draw2d.geometry.Rectangle(
                        clientArea.getCenter().getTranslated(
                                -ARROW_SIZE.width / 2, -ARROW_SIZE.height / 2),
                        ARROW_SIZE));
            }

            @Override
            protected void paintFigure(Graphics graphics) {
                super.paintFigure(graphics);

                // paint the gradient
                graphics.pushState();
                org.eclipse.draw2d.geometry.Rectangle r = org.eclipse.draw2d.geometry.Rectangle.SINGLETON;
                r.setBounds(getBounds());
                
                // Added by Phillipus
                //graphics.setForegroundColor(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
                //graphics.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND);
                graphics.setForegroundColor(PaletteColorUtil.PALETTE_TITLE_LABEL_BACKGROUND_START);
                graphics.setBackgroundColor(PaletteColorUtil.PALETTE_TITLE_LABEL_BACKGROUND_END);
                
                graphics.fillGradient(r, true);
                graphics.popState();

                // draw bottom border
                graphics.setForegroundColor(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
                graphics.drawLine(r.getBottomLeft().getTranslated(0, -1), r
                        .getBottomRight().getTranslated(0, -1));
            }
        }
    }

    private class TitleCanvas extends Canvas {
        private LightweightSystem lws;

        public TitleCanvas(Composite parent, boolean horizontal) {
            super(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
            init(horizontal);
            provideAccSupport();
        }

        /**
         * @see org.eclipse.swt.widgets.Control#computeSize(int, int, boolean)
         */
        @Override
        public Point computeSize(int wHint, int hHint, boolean changed) {
            Dimension size = lws.getRootFigure().getPreferredSize(wHint, hHint);
            size.union(new Dimension(wHint, hHint));
            return new org.eclipse.swt.graphics.Point(size.width, size.height);
        }

        private void init(boolean isHorizontal) {
            final IFigure contents = new TitleLabel(true);
            contents.setRequestFocusEnabled(true);
            contents.setFocusTraversable(true);
            contents.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent fe) {
                    fe.gainer.repaint();
                }

                @Override
                public void focusLost(FocusEvent fe) {
                    fe.loser.repaint();
                }
            });

            lws = new LightweightSystem();
            lws.setControl(this);
            lws.setContents(contents);
            setCursor(Cursors.SIZEALL);
            FONT_MGR.register(this);
            new TitleDragManager(this);
            final MenuManager manager = new MenuManager();
            MenuManager mgr = new MenuManager(PaletteMessages.DOCK_LABEL);
            mgr.add(new ChangeDockAction(PaletteMessages.LEFT_LABEL,
                    PositionConstants.WEST));
            mgr.add(new ChangeDockAction(PaletteMessages.RIGHT_LABEL,
                    PositionConstants.EAST));
            manager.add(new ResizeAction());
            manager.add(mgr);
            setMenu(manager.createContextMenu(this));
            mgr.addMenuListener(new IMenuListener() {
                @Override
                public void menuAboutToShow(IMenuManager menuMgr) {
                    IContributionItem[] items = menuMgr.getItems();
                    for (int i = 0; i < items.length; i++) {
                        ((ActionContributionItem) items[i]).update();
                    }
                }
            });

            addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    FONT_MGR.unregister(TitleCanvas.this);
                    manager.dispose();
                }
            });
        }

        private void provideAccSupport() {
            getAccessible().addAccessibleListener(new AccessibleAdapter() {
                @Override
                public void getDescription(AccessibleEvent e) {
                    e.result = PaletteMessages.ACC_DESC_PALETTE_TITLE;
                }

                @Override
                public void getHelp(AccessibleEvent e) {
                    getDescription(e);
                }

                @Override
                public void getName(AccessibleEvent e) {
                    e.result = GEFMessages.Palette_Label;
                }
            });
            getAccessible().addAccessibleControlListener(
                    new AccessibleControlAdapter() {
                        @Override
                        public void getRole(AccessibleControlEvent e) {
                            e.detail = ACC.ROLE_LABEL;
                        }
                    });
        }

        @Override
        public void setFont(Font font) {
            ((IFigure) lws.getRootFigure().getChildren().get(0)).setFont(font);
            if (isVisible()) {
                /*
                 * If this canvas is in the sash, we want the
                 * FlyoutPaletteComposite to layout (which will cause the sash
                 * to be resized and laid out). However, if this canvas is in
                 * the paletteContainer, the paletteContainer's bounds won't
                 * change, and hence it won't layout. Thus, we also invoke
                 * getParent().layout().
                 */
                FlyoutPaletteComposite.this.layout(true);
                getParent().layout(true);
            }
        }
    }

    private class ChangeDockAction extends Action {
        private int position;

        /**
         * Constructor
         * 
         * @param text
         *            this action's text
         * @param position
         *            the dock side that this action represents:
         *            PositionConstants.EAST or PositionConstants.WEST
         */
        public ChangeDockAction(String text, int position) {
            super(text, IAction.AS_RADIO_BUTTON);
            this.position = position;
        }

        /**
         * This Action is checked when the palette is docked on the side this
         * action represents
         * 
         * @see org.eclipse.jface.action.IAction#isChecked()
         */
        @Override
        public boolean isChecked() {
            return dock == position;
        }

        /**
         * Changes the palette's dock location to the side this action
         * represents
         * 
         * @see org.eclipse.jface.action.IAction#run()
         */
        @Override
        public void run() {
            setDockLocation(position);
        }
    }

    private static class FontManager {
        private final String fontName = getFontType();
        private List registrants = new ArrayList();
        private Font titleFont;
        private final IPropertyChangeListener fontListener = new IPropertyChangeListener() {
            @Override
            public void propertyChange(
                    org.eclipse.jface.util.PropertyChangeEvent event) {
                if (fontName.equals(event.getProperty()))
                    handleFontChanged();
            }
        };

        private FontManager() {
        }

        protected final Font createTitleFont() {
            return JFaceResources.getFont(fontName);
        }

        protected void dispose() {
            titleFont = null;
            JFaceResources.getFontRegistry().removeListener(fontListener);
        }

        protected String getFontType() {
            return JFaceResources.DIALOG_FONT;
        }

        protected void handleFontChanged() {
            if (titleFont == null)
                return;
            Font oldFont = titleFont;
            titleFont = createTitleFont();
            for (Iterator iter = registrants.iterator(); iter.hasNext();)
                ((Control) iter.next()).setFont(titleFont);
            oldFont.dispose();
        }

        protected void init() {
            titleFont = createTitleFont();
            JFaceResources.getFontRegistry().addListener(fontListener);
        }

        public void register(Control ctrl) {
            if (titleFont == null)
                init();
            ctrl.setFont(titleFont);
            registrants.add(ctrl);
        }

        public void unregister(Control ctrl) {
            registrants.remove(ctrl);
            if (registrants.isEmpty())
                dispose();
        }
    }

    /**
     * Default implementation of FlyoutPreferences that stores the flyout
     * palette settings in the given Preferences.
     * 
     * @author Pratik Shah
     * @since 3.2
     */
    private static class DefaultFlyoutPreferences implements FlyoutPreferences {
        /*
         * There's no need to set the default for these properties since the
         * default-default of 0 for ints will suffice.
         */
        private static final String PALETTE_DOCK_LOCATION = "org.eclipse.gef.pdock"; //$NON-NLS-1$
        private static final String PALETTE_SIZE = "org.eclipse.gef.psize"; //$NON-NLS-1$
        private static final String PALETTE_STATE = "org.eclipse.gef.pstate"; //$NON-NLS-1$

        private Preferences prefs;

        private DefaultFlyoutPreferences(Preferences preferences) {
            prefs = preferences;
        }

        @Override
        public int getDockLocation() {
            return prefs.getInt(PALETTE_DOCK_LOCATION);
        }

        @Override
        public int getPaletteState() {
            return prefs.getInt(PALETTE_STATE);
        }

        @Override
        public int getPaletteWidth() {
            return prefs.getInt(PALETTE_SIZE);
        }

        @Override
        public void setDockLocation(int location) {
            prefs.setValue(PALETTE_DOCK_LOCATION, location);
        }

        @Override
        public void setPaletteState(int state) {
            prefs.setValue(PALETTE_STATE, state);
        }

        @Override
        public void setPaletteWidth(int width) {
            prefs.setValue(PALETTE_SIZE, width);
        }
    }

    private static class DragCursors {
        public static final int INVALID = 0;

        public static final int LEFT = 1;

        public static final int RIGHT = 2;

        private final static Cursor cursors[] = new Cursor[3];

        /**
         * Return the cursor for a drop scenario, as identified by code. Code
         * must be one of INVALID, LEFT, RIGHT. If the code is not found default
         * to INVALID. Note that since these three cursors are static, they will
         * only be created once for the lifetime of the eclipse session and
         * shared (i.e this is not an image leak).
         * 
         * @param code
         *            the code
         * @return the cursor
         */
        public static Cursor getCursor(int code) {
            Display display = Display.getCurrent();
            if (cursors[code] == null) {
                ImageDescriptor source = null;
                ImageDescriptor mask = null;
                switch (code) {
                case LEFT:
                    source = PlatformUI
                            .getWorkbench()
                            .getSharedImages()
                            .getImageDescriptor(
                                    ISharedImages.IMG_OBJS_DND_LEFT_SOURCE);
                    mask = PlatformUI
                            .getWorkbench()
                            .getSharedImages()
                            .getImageDescriptor(
                                    ISharedImages.IMG_OBJS_DND_LEFT_MASK);
                    cursors[LEFT] = new Cursor(display, source.getImageData(),
                            mask.getImageData(), 16, 16);
                    break;
                case RIGHT:
                    source = PlatformUI
                            .getWorkbench()
                            .getSharedImages()
                            .getImageDescriptor(
                                    ISharedImages.IMG_OBJS_DND_RIGHT_SOURCE);
                    mask = PlatformUI
                            .getWorkbench()
                            .getSharedImages()
                            .getImageDescriptor(
                                    ISharedImages.IMG_OBJS_DND_RIGHT_MASK);
                    cursors[RIGHT] = new Cursor(display, source.getImageData(),
                            mask.getImageData(), 16, 16);
                    break;
                default:
                case INVALID:
                    source = PlatformUI
                            .getWorkbench()
                            .getSharedImages()
                            .getImageDescriptor(
                                    ISharedImages.IMG_OBJS_DND_INVALID_SOURCE);
                    mask = PlatformUI
                            .getWorkbench()
                            .getSharedImages()
                            .getImageDescriptor(
                                    ISharedImages.IMG_OBJS_DND_INVALID_MASK);
                    cursors[INVALID] = new Cursor(display,
                            source.getImageData(), mask.getImageData(), 16, 16);
                    break;
                }
            }
            return cursors[code];
        }

    }
}