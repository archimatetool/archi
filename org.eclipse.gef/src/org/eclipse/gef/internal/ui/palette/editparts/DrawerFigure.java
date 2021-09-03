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

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.editparts.PaletteToolbarLayout;

/**
 * @author Pratik Shah
 */
@SuppressWarnings("rawtypes")
public class DrawerFigure extends Figure {

    /** Foreground color constant **/
    protected static final Color FG_COLOR = FigureUtilities.mixColors(
            PaletteColorUtil.WIDGET_NORMAL_SHADOW,
            PaletteColorUtil.WIDGET_BACKGROUND);

    /** Scrollpane border constant for icon and column layout mode **/
    protected static final Border SCROLL_PANE_BORDER = new MarginBorder(2, 2,
            2, 2);
    /** Scrollpane border constant for list and details layout mode **/
    protected static final Border SCROLL_PANE_LIST_BORDER = new MarginBorder(2,
            0, 2, 0);
    /** Title margin border constant **/
    protected static final Border TITLE_MARGIN_BORDER = new MarginBorder(4, 2,
            2, 2);
    /** Toggle button border constant **/
    protected static final Border TOGGLE_BUTTON_BORDER = new RaisedBorder();
    /** Tooltip border constant **/
    protected static final Border TOOLTIP_BORDER = new CompoundBorder(
            new SchemeBorder(SchemeBorder.SCHEMES.RAISED), new MarginBorder(1));
    private Toggle collapseToggle;
    private Label drawerLabel, tipLabel;

    private boolean addedScrollpane = false;
    private int layoutMode = -1;
    private PinFigure pinFigure;
    private ScrollPane scrollpane;
    private boolean showPin = true, skipNextEvent;
    private EditPartTipHelper tipHelper;

    /**
     * This is the figure for the entire drawer label button.
     */
    private class CollapseToggle extends Toggle {

        public CollapseToggle(IFigure contents) {
            super(contents);
            setSelected(true);
            setRequestFocusEnabled(true);
            addChangeListener(new ChangeListener() {

                @Override
                public void handleStateChanged(ChangeEvent e) {
                    if (e.getPropertyName().equals(
                            ButtonModel.SELECTED_PROPERTY)) {
                        Animation.markBegin();
                        handleExpandStateChanged();
                        Animation.run(150);
                    } else if (e.getPropertyName().equals(
                            ButtonModel.MOUSEOVER_PROPERTY)) {
                        repaint();
                    }
                }
            });
        }

        @Override
        public IFigure getToolTip() {
            return buildTooltip();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void paintFigure(Graphics g) {
            super.paintFigure(g);
            Rectangle r = Rectangle.SINGLETON;
            r.setBounds(getBounds());

            // draw top border of drawer figure
            g.setForegroundColor(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
            g.drawLine(r.getTopLeft(), r.getTopRight());
            g.setForegroundColor(ColorConstants.white);
            g.drawLine(r.getTopLeft().getTranslated(0, 1), r.getTopRight()
                    .getTranslated(0, 1));
            r.crop(new Insets(2, 0, 0, 0));
            if (isExpanded()) {
                g.setForegroundColor(PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_65);
                g.drawLine(r.getLocation(), r.getTopRight());
                r.crop(new Insets(1, 0, 0, 0));
            }

            // draw bottom border of drawer figure
            if (!isExpanded()) {
                g.setForegroundColor(ColorConstants.white);
                g.drawLine(r.getBottomLeft().getTranslated(0, -1), r
                        .getBottomRight().getTranslated(0, -1));
                r.crop(new Insets(0, 0, 1, 0));
            }

            paintToggleGradient(g, r);

        }
    }

    /**
     * Constructor
     * 
     * @param control
     *            The Control of the LWS to which this Figure belongs (it is
     *            used to display the drawer header with an EditPartTipHelper,
     *            if the header is not completely visible). It can be
     *            <code>null</code> (the tip won't be displayed).
     */
    public DrawerFigure(final Control control) {
        /*
         * A PaletteToolbarLayout is being used here instead of a ToolbarLayout
         * so that the ScrollPane can be stretched to take up vertical space.
         * This affects selection and appearance (background color).
         */
        setLayoutManager(new PaletteToolbarLayout() {
            @Override
            protected boolean isChildGrowing(IFigure child) {
                int wHint = child.getBounds().width;
                return child.getPreferredSize(wHint, -1).height != child
                        .getMinimumSize(wHint, -1).height;
            }
        });

        Figure title = new Figure();
        title.setBorder(TITLE_MARGIN_BORDER);
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHorizontalSpacing(2);
        title.setLayoutManager(borderLayout);

        drawerLabel = new Label();
        drawerLabel.setLabelAlignment(PositionConstants.LEFT);

        pinFigure = new PinFigure();

        title.add(pinFigure, BorderLayout.RIGHT);
        title.add(drawerLabel, BorderLayout.CENTER);

        collapseToggle = new CollapseToggle(title);

        /*
         * @TODO:Pratik
         * 
         * There is a bug here. Right-click on the name pop-up for the header of
         * a drawer figure in the palette. This will hide the pop-up.
         * Right-click again, this time on the collapse toggle, to bring up the
         * drawer's context menu. Now, left-click on the collapse toggle. The
         * context menu will disappear, the name pop-up will re-appear, and the
         * drawer will collapse/expand. If the drawer was in such a position,
         * that collapsing/expanding it will cause its header to move, the name
         * pop-up will now be floating over where the collapse toggle used to
         * be, but is not anymore. To fix this, you can detect the left mouse
         * click on the collapse toggle and hide the name pop-up then. The
         * problem is that when you click on the collapseToggle after the
         * context menu has been brought up, it does not fire a mouse pressed
         * event. The listener below, that is commented out for now, is never
         * notified.
         */
        // collapseToggle.addMouseListener(new MouseListener.Stub(){
        // public void mousePressed(MouseEvent me) {
        // System.out.println("AAA");
        // }
        // });

        add(collapseToggle);
        createScrollpane();
        createHoverHelp(control);
    }

    /**
     * Paints the background gradient on the drawer toggle figure.
     * 
     * @param g
     *            the graphics object
     * @param rect
     *            the rectangle which the background gradient should cover
     */
    private void paintToggleGradient(Graphics g, Rectangle rect) {
        if (isExpanded()) {
            g.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
            g.fillRectangle(rect);
        } else if (collapseToggle.getModel().isMouseOver()) {
            Color color1 = PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_60;
            Color color2 = PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_90;
            Color color3 = PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_95;
            Color color4 = PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_90;

            g.setForegroundColor(color1);
            g.setBackgroundColor(color2);
            g.fillGradient(rect.x, rect.y, rect.width, rect.height - 4, true);

            g.setForegroundColor(color2);
            g.setBackgroundColor(color3);
            g.fillGradient(rect.x, rect.bottom() - 4, rect.width, 2, true);

            g.setForegroundColor(color3);
            g.setBackgroundColor(color4);
            g.fillGradient(rect.x, rect.bottom() - 2, rect.width, 2, true);
        } else {
            g.setForegroundColor(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
            g.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_45);
            g.fillGradient(rect, true);
        }
    }

    private void createHoverHelp(final Control control) {
        if (control == null) {
            return;
        }
        // If a control was provided, create the tipLabel -- if the text in the
        // header is
        // truncated, it will display it as a tooltip.
        tipLabel = new Label() {
            /**
             * @see org.eclipse.draw2d.Figure#getToolTip()
             */
            @Override
            public IFigure getToolTip() {
                return buildTooltip();
            }

            @Override
            protected void paintFigure(Graphics graphics) {
                Rectangle r = Rectangle.SINGLETON;
                r.setBounds(getBounds());
                graphics.pushState();
                paintToggleGradient(graphics, getBounds());
                graphics.popState();
                super.paintFigure(graphics);
            }
        };
        tipLabel.setOpaque(false);
        tipLabel.setBorder(TOOLTIP_BORDER);
        collapseToggle.addMouseMotionListener(new MouseMotionListener.Stub() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!drawerLabel.getBounds().contains(e.getLocation()))
                    return;
                if (skipNextEvent) {
                    skipNextEvent = false;
                    return;
                }
                if (drawerLabel.isTextTruncated()
                        && !EditPartTipHelper.isCurrent(tipHelper)) {
                    tipLabel.setText(drawerLabel.getText());
                    tipLabel.setIcon(drawerLabel.getIcon());
                    tipLabel.setFont(drawerLabel.getFont());
                    tipHelper = new EditPartTipHelper(control);
                    Rectangle bounds = drawerLabel.getBounds()
                            .getExpanded(2, 2);
                    drawerLabel.translateToAbsolute(bounds);
                    org.eclipse.swt.graphics.Rectangle loc = new org.eclipse.swt.graphics.Rectangle(
                            bounds.x, bounds.y, bounds.width, bounds.height);
                    loc = Display.getCurrent().map(control, null, loc);
                    tipHelper.displayToolTipAt(tipLabel, loc.x, loc.y);
                }
            }
        });
        tipLabel.addMouseListener(new MouseListener.Stub() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.button == 1) {
                    Rectangle original = getCollapseToggle().getBounds()
                            .getCopy();
                    getCollapseToggle().requestFocus();
                    setExpanded(!isExpanded());
                    // Hide the tip if expanding the drawer causes the collapse
                    // toggle to move
                    if (!original.equals(getCollapseToggle().getBounds())) {
                        tipHelper.hide();
                    }
                } else {
                    tipHelper.hide();
                    if (e.button == 3) {
                        skipNextEvent = true;
                    }
                }
            }
        });
    }

    private void createScrollpane() {
        scrollpane = new ScrollPane();
        scrollpane.getViewport().setContentsTracksWidth(true);
        scrollpane.setMinimumSize(new Dimension(0, 0));
        scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        scrollpane.setVerticalScrollBar(new PaletteScrollBar());
        scrollpane.getVerticalScrollBar().setStepIncrement(20);
        scrollpane.setLayoutManager(new OverlayScrollPaneLayout());
        scrollpane.setContents(new Figure());
        scrollpane.getContents().setOpaque(true);
        // Added by Phillipus - use CSS theme defined color
        //scrollpane.getContents().setBackgroundColor(
        //        PaletteColorUtil.WIDGET_LIST_BACKGROUND);
        scrollpane.getContents().setBackgroundColor(
                PaletteColorUtil.PALETTE_BACKGROUND);
    }

    IFigure buildTooltip() {
        return null;
    }

    /**
     * @return The Clickable that is used to expand/collapse the drawer.
     */
    public Clickable getCollapseToggle() {
        return collapseToggle;
    }

    /**
     * @return The content pane for this figure, i.e. the Figure to which
     *         children can be added.
     */
    public IFigure getContentPane() {
        return scrollpane.getContents();
    }

    /**
     * @see Figure#getMinimumSize(int, int)
     */
    @Override
    public Dimension getMinimumSize(int wHint, int hHint) {
        /*
         * Fix related to Bug #35176 The figure returns a minimum size that is
         * of at least a certain height, so as to prevent each drawer from
         * getting too small (in which case, the scrollbars cover up the entire
         * available space).
         */
        if (isExpanded()) {
            List children = getContentPane().getChildren();
            if (!children.isEmpty()) {
                Dimension result = collapseToggle
                        .getPreferredSize(wHint, hHint).getCopy();
                result.height += getContentPane().getInsets().getHeight();
                IFigure child = (IFigure) children.get(0);
                result.height += Math.min(80,
                        child.getPreferredSize(wHint, -1).height + 9);
                return result.intersect(getPreferredSize(wHint, hHint));
            }
        }

        return super.getMinimumSize(wHint, hHint);
    }

    /**
     * Returns the ScrollPane associated with this DrawerFigure
     * 
     * @return the ScrollPane
     */
    public ScrollPane getScrollpane() {
        return scrollpane;
    }

    protected void handleExpandStateChanged() {
        if (isExpanded()) {
            if (scrollpane.getParent() != this)
                add(scrollpane);
        } else {
            if (scrollpane.getParent() == this)
                remove(scrollpane);

            // collapse all pinnable palette stack children that aren't pinned
            for (Iterator iterator = getContentPane().getChildren().iterator(); iterator
                    .hasNext();) {
                Object child = iterator.next();
                if (child instanceof PinnablePaletteStackFigure
                        && !((PinnablePaletteStackFigure) child).isPinnedOpen()) {
                    ((PinnablePaletteStackFigure) child).setExpanded(false);
                }
            }

        }

        if (pinFigure != null) {
            pinFigure.setVisible(isExpanded() && showPin);
        }
    }

    /**
     * @return <code>true</code> if the drawer is expanded
     */
    public boolean isExpanded() {
        return collapseToggle.isSelected();
    }

    /**
     * @return <code>true</code> if the drawer is expanded and is pinned (i.e.,
     *         it can't be automatically collapsed)
     */
    public boolean isPinnedOpen() {
        return isExpanded() && pinFigure.isVisible() && pinFigure.isSelected();
    }

    /**
     * @return <code>true</code> if the drawer is expanded and its pin is
     *         showing
     */
    public boolean isPinShowing() {
        return isExpanded() && showPin;
    }

    public void setAnimating(boolean isAnimating) {
        if (isAnimating) {
            if (scrollpane.getParent() != this) {
                addedScrollpane = true;
                add(scrollpane);
            }
            scrollpane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        } else {
            scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
            if (addedScrollpane) {
                remove(scrollpane);
                addedScrollpane = false;
            }
        }
    }

    public void setExpanded(boolean value) {
        collapseToggle.setSelected(value);
    }

    public void setLayoutMode(int layoutMode) {
        if (this.layoutMode == layoutMode) {
            return;
        }

        this.layoutMode = layoutMode;

        LayoutManager manager;
        if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS) {
            manager = new ColumnsLayout();
            getContentPane().setBorder(SCROLL_PANE_BORDER);
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS) {
            PaletteContainerFlowLayout fl = new PaletteContainerFlowLayout();
            fl.setMinorSpacing(0);
            fl.setMajorSpacing(0);
            manager = fl;
            getContentPane().setBorder(SCROLL_PANE_BORDER);
        } else {
            manager = new ToolbarLayout();
            getContentPane().setBorder(SCROLL_PANE_LIST_BORDER);
        }
        getContentPane().setLayoutManager(manager);
    }

    /**
     * Pins or unpins the drawer. The drawer can be pinned open only when it is
     * expanded. Attempts to pin it when it is collapsed will do nothing.
     * 
     * @param pinned
     *            <code>true</code> if the drawer is to be pinned
     */
    public void setPinned(boolean pinned) {
        if (!isExpanded() || !showPin) {
            return;
        }

        pinFigure.setSelected(pinned);
    }

    /**
     * Displays the given text in the drawer's header as its title.
     * 
     * @param s
     *            The title of the drawer
     */
    public void setTitle(String s) {
        drawerLabel.setText(s);
    }

    /**
     * Displays the given image in the header as the drawer's icon.
     * 
     * @param icon
     *            The icon for this drawer.
     */
    public void setTitleIcon(Image icon) {
        drawerLabel.setIcon(icon);
    }

    public void showPin(boolean show) {
        showPin = show;
        handleExpandStateChanged();
    }

}
