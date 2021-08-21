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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import org.eclipse.ui.IMemento;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.editparts.IPinnableEditPart;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

@SuppressWarnings({"rawtypes", "deprecation"})
public class ToolEntryEditPart extends PaletteEditPart {

    class MenuTimer implements Runnable {
        public static final int MENU_TIMER_DELAY = 400;

        private boolean enabled = true;

        public void disable() {
            enabled = false;
        }

        @Override
        public void run() {
            if (enabled) {
                getButtonModel().setArmed(false);
                getButtonModel().setPressed(false);
                ((IPaletteStackEditPart) getParent()).openMenu();
                getViewer().getEditDomain().loadDefaultTool();
            }
        }
    }

    abstract class ToggleButtonTracker extends SingleSelectionTracker {
        private MenuTimer timer = null;

        protected void enableTimer() {
            timer = new MenuTimer();
            getViewer().getControl().getDisplay()
                    .timerExec(MenuTimer.MENU_TIMER_DELAY, timer);
        }

        protected void disableTimer() {
            if (timer != null) {
                timer.disable();
                timer = null;
            }
        }

        @Override
        protected boolean handleButtonDown(int button) {
            if (getParent() instanceof IPaletteStackEditPart)
                enableTimer();

            if (button == 2 && isInState(STATE_INITIAL))
                performConditionalSelection();
            super.handleButtonDown(button);
            if (button == 1) {
                getFigure().internalGetEventDispatcher().requestRemoveFocus(
                        getFigure());
                getButtonModel().setArmed(true);
                getButtonModel().setPressed(true);
            }
            return true;
        }

        @Override
        protected boolean handleDrag() {
            org.eclipse.draw2d.geometry.Point point = getLocation().getCopy();
            getFigure().translateToRelative(point);
            if (!getFigure().containsPoint(point)) {
                getButtonModel().setArmed(false);
                getButtonModel().setMouseOver(false);
                disableTimer();
            } else {
                getButtonModel().setArmed(true);
                getButtonModel().setMouseOver(true);
            }
            return true;
        }

        @Override
        protected boolean handleNativeDragFinished(DragSourceEvent event) {
            getPaletteViewer().setActiveTool(null);
            return true;
        }
    }

    class GTKToggleButtonTracker extends ToggleButtonTracker {
        int gtkState = 0;

        @Override
        public void deactivate() {
            disableTimer();
            super.deactivate();
        }

        @Override
        protected boolean handleButtonUp(int button) {
            disableTimer();

            // Workaround for Bug 96351. It should be removed when Bug 35585 is
            // fixed.
            if (gtkState == 1) {
                handleNativeDragFinished(null);
                return true;
            }

            if (button == 1) {
                getButtonModel().setPressed(false);
                getButtonModel().setArmed(false);
            }
            return super.handleButtonUp(button);
        }

        @Override
        protected boolean handleNativeDragStarted(DragSourceEvent event) {
            disableTimer();
            gtkState = 1;
            return true;
        }
    }

    class OtherToggleButtonTracker extends ToggleButtonTracker {

        private static final int WIN_THRESHOLD = 3;

        private Point mouseDownLoc = null;

        @Override
        public void deactivate() {
            disableTimer();
            getButtonModel().setPressed(false);
            getButtonModel().setArmed(false);
            super.deactivate();
        }

        @Override
        protected boolean handleButtonDown(int button) {
            mouseDownLoc = new Point(getLocation().x, getLocation().y);
            return super.handleButtonDown(button);
        }

        @Override
        protected boolean handleButtonUp(int button) {
            disableTimer();

            if (button == 1) {
                getButtonModel().setPressed(false);
                getButtonModel().setArmed(false);
            }
            return super.handleButtonUp(button);
        }

        @Override
        protected boolean handleNativeDragStarted(DragSourceEvent event) {
            disableTimer();

            // win hack because button down is delayed
            if (getParent() instanceof IPaletteStackEditPart
                    && SWT.getPlatform().equals("win32")) { //$NON-NLS-1$
                Point nds = getPaletteViewer().getControl().toControl(
                        event.display.getCursorLocation());
                if (mouseDownLoc != null
                        && (Math.abs(nds.x - mouseDownLoc.x) + Math.abs(nds.y
                                - mouseDownLoc.y)) < WIN_THRESHOLD) {
                    getButtonModel().setArmed(false);
                    getButtonModel().setPressed(false);
                    ((IPaletteStackEditPart) getParent()).openMenu();
                    getViewer().getEditDomain().loadDefaultTool();
                    event.doit = false;
                    return false;
                }
            }

            getButtonModel().setPressed(false);
            getButtonModel().setArmed(false);
            return true;
        }
    }

    /**
     * The figure for for a <code>ToolEntryEditPart</code>.
     */
    class ToolEntryToggle extends Toggle {

        private boolean showHoverFeedback = false;

        ToolEntryToggle(IFigure contents) {
            super(contents);
            setOpaque(false);
            setEnabled(true);
            if (isToolbarItem()) {
                setStyle(Clickable.STYLE_BUTTON | Clickable.STYLE_TOGGLE);
                setBorder(TOOLBAR_ITEM_BORDER);
            }
        }

        @Override
        public boolean containsPoint(int x, int y) {
            Rectangle rect = getBounds().getCopy();
            if (customLabel.getBorder() == ICON_BORDER) {
                rect.width -= PinnablePaletteStackFigure.ARROW_WIDTH;
            } else if (customLabel.getBorder() == LIST_BORDER) {
                rect.width -= PinnablePaletteStackFigure.ARROW_WIDTH;
                rect.x += PinnablePaletteStackFigure.ARROW_WIDTH;
            }
            return rect.contains(x, y);
        }

        @Override
        public IFigure findMouseEventTargetAt(int x, int y) {
            return null;
        }

        @Override
        public IFigure getToolTip() {
            return createToolTip();
        }

        @Override
        public void setEnabled(boolean value) {
            super.setEnabled(value);
            if (isEnabled()) {
                setRolloverEnabled(true);
                if (getFlag(STYLE_BUTTON)) {
                    setBorder(TOOLBAR_ITEM_BORDER);
                }
                setForegroundColor(null);
            } else {
                if (getFlag(STYLE_BUTTON)) {
                    setBorder(null);
                }
                setRolloverEnabled(false);
                setForegroundColor(ColorConstants.gray);
            }
        }

        @Override
        protected void paintFigure(Graphics graphics) {
            super.paintFigure(graphics);

            if (!isToolbarItem() && isEnabled() && isRolloverEnabled()) {
                ButtonModel model = getModel();

                if (model.isSelected()) {
                    graphics.setBackgroundColor(PaletteColorUtil
                            .getSelectedColor());
                    graphics.fillRoundRectangle(
                            getSelectionRectangle(getLayoutSetting(),
                                    customLabel), 3, 3);
                } else if (model.isMouseOver() || showHoverFeedback) {
                    graphics.setBackgroundColor(PaletteColorUtil
                            .getHoverColor());
                    graphics.fillRoundRectangle(
                            getSelectionRectangle(getLayoutSetting(),
                                    customLabel), 3, 3);
                }
            }
        }

        @Override
        protected void paintBorder(Graphics graphics) {
            if (isEnabled()) {

                if (getBorder() != null)
                    getBorder().paint(this, graphics, NO_INSETS);
                if (hasFocus()) {
                    graphics.setForegroundColor(ColorConstants.black);
                    graphics.setBackgroundColor(ColorConstants.white);

                    Rectangle area = isToolbarItem() ? getClientArea()
                            : getSelectionRectangle(getLayoutSetting(),
                                    customLabel);
                    if (isStyle(STYLE_BUTTON))
                        graphics.drawFocus(area.x, area.y, area.width,
                                area.height);
                    else
                        graphics.drawFocus(area.x, area.y, area.width - 1,
                                area.height - 1);
                }
            } else {
                super.paintBorder(graphics);
            }
        }

        /**
         * Should hover feedback be shown? Allows other palette entities to
         * control when the hover feedback should be shown on this tool entry.
         * 
         * @param showHoverFeedback
         *            true if the hover feedback is to be shown; false
         *            otherwise.
         */
        public void setShowHoverFeedback(boolean showHoverFeedback) {
            this.showHoverFeedback = showHoverFeedback;
            repaint();
        }
    }

    private static final String ACTIVE_STATE = "active"; //$NON-NLS-1$
    private DetailedLabelFigure customLabel;

    public ToolEntryEditPart(PaletteEntry paletteEntry) {
        super(paletteEntry);
    }

    @Override
    public Object getAdapter(Class key) {
        if (key == IPinnableEditPart.class) {
            if ((getParent() instanceof PinnablePaletteStackEditPart)
                    && ((PinnablePaletteStackEditPart) getParent())
                            .canBePinned()
                    && ((PaletteStack) getParent().getModel()).getActiveEntry()
                            .equals(getModel())) {
                return getParent();
            }
        }
        return super.getAdapter(key);
    }

    @Override
    protected AccessibleEditPart createAccessible() {
        return new AccessibleGraphicalEditPart() {
            @Override
            public void getDescription(AccessibleEvent e) {
                e.result = getPaletteEntry().getDescription();
            }

            @Override
            public void getName(AccessibleEvent e) {
                e.result = getPaletteEntry().getLabel();
            }

            @Override
            public void getRole(AccessibleControlEvent e) {
                if (getParent() instanceof IPaletteStackEditPart
                        && (ToolEntryEditPart.this == ((IPaletteStackEditPart) getParent())
                                .getActiveEntry())) {
                    e.detail = ACC.ROLE_COMBOBOX;
                } else {
                    e.detail = ACC.ROLE_PUSHBUTTON;
                }
            }

            @Override
            public void getState(AccessibleControlEvent e) {
                super.getState(e);
                if (getButtonModel().isSelected())
                    e.detail |= ACC.STATE_CHECKED;
            }
        };
    }

    static final Border TOOLBAR_ITEM_BORDER = new ButtonBorder(
            ButtonBorder.SCHEMES.TOOLBAR);

    // The following are the insets that the bounds of the label figure should
    // be
    // cropped to paint the blue/orange select and hover feedback rectangles.
    static final Insets LIST_HIGHLIGHT_INSETS = new Insets(1, 5, 2, 0);
    static final Insets ICON_HIGHLIGHT_INSETS = new Insets(2, 1, 2, 1);

    // The following are the borders that go around the entire tool figure to
    // provide room to draw the arrow and outline of the palette stack figure if
    // this tool happens to appear as the active tool of a stack.
    static final Border LIST_BORDER = new MarginBorder(3,
            PinnablePaletteStackFigure.ARROW_WIDTH + 7, 4, 0);
    static final Border ICON_BORDER = new MarginBorder(4, 4, 3,
            PinnablePaletteStackFigure.ARROW_WIDTH + 4);

    @Override
    public IFigure createFigure() {

        customLabel = new DetailedLabelFigure();
        Clickable button = new ToolEntryToggle(customLabel);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                getPaletteViewer().setActiveTool(getToolEntry());
            }
        });

        return button;
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        customLabel.dispose();
        super.deactivate();
    }

    /**
     * @see org.eclipse.gef.EditPart#eraseTargetFeedback(Request)
     */
    @Override
    public void eraseTargetFeedback(Request request) {
        if (RequestConstants.REQ_SELECTION.equals(request.getType()))
            getButtonModel().setMouseOver(false);
        super.eraseTargetFeedback(request);
    }

    private ButtonModel getButtonModel() {
        Clickable c = (Clickable) getFigure();
        return c.getModel();
    }

    /**
     * @see PaletteEditPart#getDragTracker(Request)
     */
    @Override
    public DragTracker getDragTracker(Request request) {
        if (SWT.getPlatform().equals("gtk")) //$NON-NLS-1$
            return new GTKToggleButtonTracker();
        else
            return new OtherToggleButtonTracker();
    }

    private ToolEntry getToolEntry() {
        return (ToolEntry) getPaletteEntry();
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#getToolTipText()
     */
    @Override
    protected String getToolTipText() {
        String result = null;
        if (getLayoutSetting() != PaletteViewerPreferences.LAYOUT_DETAILS) {
            result = super.getToolTipText();
        }
        return result;
    }

    /**
     * If this edit part's name is truncated in its label, the name should be
     * prepended to the tooltip.
     * 
     * @return whether the name needs to be included in the tooltip
     */
    @Override
    protected boolean nameNeededInToolTip() {
        DetailedLabelFigure label = (DetailedLabelFigure) getFigure()
                .getChildren().get(0);
        return label.isNameTruncated() || super.nameNeededInToolTip();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @Override
    protected void refreshVisuals() {
        PaletteEntry entry = getPaletteEntry();

        customLabel.setName(entry.getLabel());
        customLabel.setDescription(entry.getDescription());
        if (getPreferenceSource().useLargeIcons())
            setImageDescriptor(entry.getLargeIcon());
        else
            setImageDescriptor(entry.getSmallIcon());
        int layoutMode = getLayoutSetting();
        customLabel.setLayoutMode(layoutMode);
        if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS) {
            customLabel.setBorder(ICON_BORDER);
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
                || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
            customLabel.setBorder(LIST_BORDER);
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS
                && !isToolbarItem()) {
            customLabel.setBorder(ICON_BORDER);
        } else {
            customLabel.setBorder(null);
        }

        super.refreshVisuals();
    }

    /**
     * @see org.eclipse.gef.EditPart#removeNotify()
     */
    @Override
    public void removeNotify() {
        if (getButtonModel().isSelected())
            getPaletteViewer().setActiveTool(null);
        super.removeNotify();
    }

    public void setToolSelected(boolean value) {
        getButtonModel().setSelected(value);
    }

    @Override
    public void restoreState(IMemento memento) {
        if (new Boolean(memento.getString(ACTIVE_STATE)).booleanValue())
            getPaletteViewer().setActiveTool(getToolEntry());
        super.restoreState(memento);
    }

    @Override
    public void saveState(IMemento memento) {
        memento.putString(ACTIVE_STATE, new Boolean(getPaletteViewer()
                .getActiveTool() == getToolEntry()).toString());
        super.saveState(memento);
    }

    /**
     * @see PaletteEditPart#setImageInFigure(Image)
     */
    @Override
    protected void setImageInFigure(Image image) {
        DetailedLabelFigure fig = (DetailedLabelFigure) (getFigure()
                .getChildren().get(0));
        fig.setImage(image);
    }

    /**
     * @see org.eclipse.gef.EditPart#setSelected(int)
     */
    @Override
    public void setSelected(int value) {
        super.setSelected(value);
        if (value == SELECTED_PRIMARY
                && getPaletteViewer().getControl() != null
                && !getPaletteViewer().getControl().isDisposed()
                && getPaletteViewer().getControl().isFocusControl())
            getFigure().requestFocus();
    }

    /**
     * @see org.eclipse.gef.EditPart#showTargetFeedback(Request)
     */
    @Override
    public void showTargetFeedback(Request request) {
        if (RequestConstants.REQ_SELECTION.equals(request.getType()))
            getButtonModel().setMouseOver(true);
        super.showTargetFeedback(request);
    }

    static Rectangle getSelectionRectangle(int layoutMode,
            DetailedLabelFigure labelFigure) {
        Rectangle rect = Rectangle.SINGLETON;
        rect.setBounds(labelFigure.getBounds());
        if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
                || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {

            rect.x += PinnablePaletteStackFigure.ARROW_WIDTH;
            rect.width -= PinnablePaletteStackFigure.ARROW_WIDTH;
            int newWidth = labelFigure.getPreferredSize().width + 17;
            if (newWidth < rect.width) {
                rect.width = newWidth;
            }
            rect.crop(LIST_HIGHLIGHT_INSETS);
        } else {
            rect.width -= PinnablePaletteStackFigure.ARROW_WIDTH;
            rect.crop(ICON_HIGHLIGHT_INSETS);
        }
        return rect;
    }

}
