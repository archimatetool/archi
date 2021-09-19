/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.GraphContainer;

public class ExpandGraphLabel extends Figure implements ActionListener {

    public static final int OPEN = 1;
    public static final int CLOSED = 2;
    private int state = CLOSED;
    private Expander expander = null;

    class Expander extends Clickable {
        private Triangle triangle;

        public Expander() {
            setStyle(Clickable.STYLE_TOGGLE);
            triangle = new Triangle();
            triangle.setSize(10, 10);
            triangle.setBackgroundColor(ColorConstants.black);
            triangle.setForegroundColor(ColorConstants.black);
            triangle.setFill(true);
            triangle.setDirection(PositionConstants.EAST);
            triangle.setLocation(new Point(5, 3));
            this.setLayoutManager(new FreeformLayout());
            this.add(triangle);
            this.setPreferredSize(15, 15);
            this.addActionListener(ExpandGraphLabel.this);
        }

        public void open() {
            triangle.setDirection(PositionConstants.SOUTH);
        }

        public void close() {
            triangle.setDirection(PositionConstants.EAST);
        }

    }

    /**
     * Sets the expander state (the little triangle) to ExpanderGraphLabel.OPEN or ExpanderGraphLabel.CLOSED
     * @param state
     */
    public void setExpandedState(int state) {
        if (state == OPEN) {
            expander.open();
        } else {
            expander.close();
        }
        this.state = state;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.draw2d.ActionListener#actionPerformed(org.eclipse.draw2d.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (state == OPEN) {
            container.close(true);

        } else {
            container.open(true);
        }
    }

    private int arcWidth;
    private Label label = null;
    private final GraphContainer container;
    private ToolbarLayout layout;

    public ExpandGraphLabel(GraphContainer container, boolean cacheLabel) {
        this(container, "", null, cacheLabel); //$NON-NLS-1$
    }

    public ExpandGraphLabel(GraphContainer container, Image i, boolean cacheLabel) {
        this(container, "", i, cacheLabel); //$NON-NLS-1$
    }

    public ExpandGraphLabel(GraphContainer container, String text, boolean cacheLabel) {
        this(container, text, null, cacheLabel);
    }

    public ExpandGraphLabel(GraphContainer container, String text, Image image, boolean cacheLabel) {
        this.label = new Label(text) {

            /*
             * This method is overwritten so that the text is not truncated.
             * (non-Javadoc)
             * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
             */
            @Override
            protected void paintFigure(Graphics graphics) {
                if (isOpaque()) {
                    super.paintFigure(graphics);
                }
                Rectangle bounds = getBounds();
                graphics.translate(bounds.x, bounds.y);
                if (getIcon() != null) {
                    graphics.drawImage(getIcon(), getIconLocation());
                }
                if (!isEnabled()) {
                    graphics.translate(1, 1);
                    graphics.setForegroundColor(ColorConstants.buttonLightest);
                    graphics.drawText(getSubStringText(), getTextLocation());
                    graphics.translate(-1, -1);
                    graphics.setForegroundColor(ColorConstants.buttonDarker);
                }
                graphics.drawText(getText(), getTextLocation());
                graphics.translate(-bounds.x, -bounds.y);
            }
        };
        this.setText(text);
        this.setImage(image);
        this.container = container;
        this.expander = new Expander();
        this.arcWidth = 8;
        this.setFont(Display.getDefault().getSystemFont());
        layout = new ToolbarLayout(true);
        layout.setSpacing(5);
        layout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        this.setLayoutManager(layout);
        this.add(this.expander);
        this.add(this.label);
        //this.remove(this.label);
    }

    /**
     * Adjust the bounds to make the text fit without truncation.
     */
    protected void adjustBoundsToFit() {
        String text = getText();
        if ((text != null) && (text.length() > 0)) {
            Font font = getFont();
            if (font != null) {
                Dimension minSize = FigureUtilities.getTextExtents(text, font);
                if (getIcon() != null) {
                    org.eclipse.swt.graphics.Rectangle imageRect = getIcon().getBounds();
                    int expandHeight = Math.max(imageRect.height - minSize.height, 0);
                    minSize.expand(imageRect.width + 4, expandHeight);
                }
                minSize.expand(10 + (2 * 1) + 100, 4 + (2 * 1));
                label.setBounds(new Rectangle(getLocation(), minSize));
            }
        }
    }

    private Image getIcon() {
        return this.label.getIcon();
    }

    private String getText() {
        return this.label.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    public void paint(Graphics graphics) {

        int blue = getBackgroundColor().getBlue();
        blue = (int) (blue - (blue * 0.20));
        blue = blue > 0 ? blue : 0;

        int red = getBackgroundColor().getRed();
        red = (int) (red - (red * 0.20));
        red = red > 0 ? red : 0;

        int green = getBackgroundColor().getGreen();
        green = (int) (green - (green * 0.20));
        green = green > 0 ? green : 0;

        Color lightenColor = new Color(Display.getCurrent(), new RGB(red, green, blue));
        graphics.setForegroundColor(lightenColor);
        graphics.setBackgroundColor(getBackgroundColor());

        graphics.pushState();

        // fill in the background
        Rectangle bounds = getBounds().getCopy();
        Rectangle r = bounds.getCopy();
        //r.x += arcWidth / 2;
        r.y += arcWidth / 2;
        //r.width -= arcWidth;
        r.height -= arcWidth;

        Rectangle top = bounds.getCopy();
        top.height /= 2;
        //graphics.setForegroundColor(lightenColor);
        //graphics.setBackgroundColor(lightenColor);
        graphics.setForegroundColor(getBackgroundColor());
        graphics.setBackgroundColor(getBackgroundColor());
        graphics.fillRoundRectangle(top, arcWidth, arcWidth);

        top.y = top.y + top.height;
        //graphics.setForegroundColor(getBackgroundColor());
        //graphics.setBackgroundColor(getBackgroundColor());
        graphics.setForegroundColor(lightenColor);
        graphics.setBackgroundColor(lightenColor);
        graphics.fillRoundRectangle(top, arcWidth, arcWidth);

        //graphics.setForegroundColor(lightenColor);
        //graphics.setBackgroundColor(getBackgroundColor());
        graphics.setBackgroundColor(lightenColor);
        graphics.setForegroundColor(getBackgroundColor());
        graphics.fillGradient(r, true);

        super.paint(graphics);
        graphics.popState();
        graphics.setForegroundColor(lightenColor);
        graphics.setBackgroundColor(lightenColor);
        // paint the border
        bounds.setSize(bounds.width - 1, bounds.height - 1);
        graphics.drawRoundRectangle(bounds, arcWidth, arcWidth);
    }

//    public Dimension getPreferredSize(int hint, int hint2) {
//    return this.label.getPreferredSize();
//}

    public void setTextT(String string) {
        this.setPreferredSize(null);
        this.label.setText(string);
        this.add(label);
        //System.out.println(this.label.getPreferredSize());
        this.layout.layout(this);
        this.invalidate();
        this.revalidate();
        this.validate();
        //this.remove(label);
    }

    public void setText(String string) {
        this.label.setText(string);
        //this.label.setPreferredSize(500, 30);
        //adjustBoundsToFit();
    }

    public void setImage(Image image) {
        this.label.setIcon(image);
        //adjustBoundsToFit();
    }

    @Override
    public void setLocation(Point p) {
        // TODO Auto-generated method stub
        super.setLocation(p);
    }

    @Override
    public void setBounds(Rectangle rect) {
        // TODO Auto-generated method stub
        super.setBounds(rect);
    }

}
