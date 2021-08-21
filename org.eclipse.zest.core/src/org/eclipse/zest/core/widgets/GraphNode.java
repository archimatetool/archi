/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.internal.GraphLabel;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;

/**
 * Simple node class which has the following properties: color, size, location,
 * and a label. It also has a list of connections and anchors.
 * 
 * @author Chris Callendar
 * 
 * @author Del Myers
 * 
 * @author Ian Bull
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GraphNode extends GraphItem {
    public static final int HIGHLIGHT_NONE = 0;
    public static final int HIGHLIGHT_ON = 1;
    // @tag ADJACENT : Removed highlight adjacent
    // public static final int HIGHLIGHT_ADJACENT = 2;

    private int nodeStyle;

    private List /* IGraphModelConnection */sourceConnections;
    private List /* IGraphModelConnection */targetConnections;

    private Color foreColor;
    private Color backColor;
    private Color highlightColor;
    // @tag ADJACENT : Removed highlight adjacent
    // private Color highlightAdjacentColor;
    private Color borderColor;
    private Color borderHighlightColor;
    private int borderWidth;
    private Point currentLocation;
    protected Dimension size;
    private Font font;
    private boolean cacheLabel;
    private boolean visible = true;
    private LayoutEntity layoutEntity;

    protected Graph graph;
    protected IContainer parent;

    /** The internal node. */
    protected Object internalNode;
    private boolean selected;
    protected int highlighted = HIGHLIGHT_NONE;
    private IFigure tooltip;
    protected IFigure nodeFigure;

    private boolean isDisposed = false;
    private boolean hasCustomTooltip;

    public GraphNode(IContainer graphModel, int style) {
        this(graphModel, style, null);
    }

    public GraphNode(IContainer graphModel, int style, Object data) {
        this(graphModel.getGraph(), style, "" /* text */, null /* image */, data); //$NON-NLS-1$
    }

    public GraphNode(IContainer graphModel, int style, String text) {
        this(graphModel, style, text, null);
    }

    public GraphNode(IContainer graphModel, int style, String text, Object data) {
        this(graphModel.getGraph(), style, text, null /* image */, data);
    }

    public GraphNode(IContainer graphModel, int style, String text, Image image) {
        this(graphModel, style, text, image, null);
    }

    public GraphNode(IContainer graphModel, int style, String text,
            Image image, Object data) {
        super(graphModel.getGraph(), style, data);
        initModel(graphModel, text, image);
        if (nodeFigure == null) {
            initFigure();
        }

        // This is a hack because JAVA sucks!
        // I don't want to expose addNode so I can't put it in the
        // IContainer interface.
        if (this.parent.getItemType() == GRAPH) {
            ((Graph) this.parent).addNode(this);
        } else if (this.parent.getItemType() == CONTAINER) {
            ((GraphContainer) this.parent).addNode(this);
        }
        this.parent.getGraph().registerItem(this);
    }

    protected void initFigure() {
        nodeFigure = createFigureForModel();
    }

    static int count = 0;

    protected void initModel(IContainer parent, String text, Image image) {
        this.nodeStyle |= parent.getGraph().getNodeStyle();
        this.parent = parent;
        this.sourceConnections = new ArrayList();
        this.targetConnections = new ArrayList();
        this.foreColor = parent.getGraph().DARK_BLUE;
        this.backColor = parent.getGraph().LIGHT_BLUE;
        this.highlightColor = parent.getGraph().HIGHLIGHT_COLOR;
        // @tag ADJACENT : Removed highlight adjacent
        // this.highlightAdjacentColor = ColorConstants.orange;
        this.nodeStyle = SWT.NONE;
        this.borderColor = ColorConstants.lightGray;
        this.borderHighlightColor = ColorConstants.blue;
        this.borderWidth = 1;
        this.currentLocation = new PrecisionPoint(0, 0);
        this.size = new Dimension(-1, -1);
        this.font = Display.getDefault().getSystemFont();
        this.graph = parent.getGraph();
        this.cacheLabel = false;
        this.setText(text);
        this.layoutEntity = new LayoutGraphNode();
        if (image != null) {
            this.setImage(image);
        }

        if (font == null) {
            font = Display.getDefault().getSystemFont();
        }

    }

    /**
     * A simple toString that we can use for debugging
     */
    @Override
    public String toString() {
        return "GraphModelNode: " + getText(); //$NON-NLS-1$
    }

    public LayoutEntity getLayoutEntity() {
        return layoutEntity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.mylar.zest.core.widgets.GraphItem#dispose()
     */
    @Override
    public void dispose() {
        if (isFisheyeEnabled) {
            this.fishEye(false, false);
        }
        super.dispose();
        this.isDisposed = true;
        while (getSourceConnections().size() > 0) {
            GraphConnection connection = (GraphConnection) getSourceConnections()
                    .get(0);
            if (!connection.isDisposed()) {
                connection.dispose();
            } else {
                removeSourceConnection(connection);
            }
        }
        while (getTargetConnections().size() > 0) {
            GraphConnection connection = (GraphConnection) getTargetConnections()
                    .get(0);
            if (!connection.isDisposed()) {
                connection.dispose();
            } else {
                removeTargetConnection(connection);
            }
        }
        graph.removeNode(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#isDisposed()
     */
    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    /**
     * Determines if this node has a fixed size or if it is packed to the size
     * of its contents. To set a node to pack, set its size (-1, -1)
     * 
     * @return
     */
    public boolean isSizeFixed() {
        return !(this.size.width < 0 && this.size.height < 0);
    }

    /**
     * Returns a new list of the source connections (GraphModelConnection
     * objects).
     * 
     * @return List a new list of GraphModelConnect objects
     */
    public List getSourceConnections() {
        return new ArrayList(sourceConnections);
    }

    /**
     * Returns a new list of the target connections (GraphModelConnection
     * objects).
     * 
     * @return List a new list of GraphModelConnect objects
     */
    public List getTargetConnections() {
        return new ArrayList(targetConnections);
    }

    /**
     * Returns the bounds of this node. It is just the combination of the
     * location and the size.
     * 
     * @return Rectangle
     */
    Rectangle getBounds() {
        return new Rectangle(getLocation(), getSize());
    }

    /**
     * Returns a copy of the node's location.
     * 
     * @return Point
     */
    public Point getLocation() {
        return currentLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#isSelected
     * ()
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the current location for this node.
     */
    public void setLocation(double x, double y) {
        currentLocation.x = (int) x;
        currentLocation.y = (int) y;
        refreshLocation();
    }

    /**
     * Returns a copy of the node's size.
     * 
     * @return Dimension
     */
    public Dimension getSize() {
        if (size.height < 0 && size.width < 0 && nodeFigure != null) {
            return nodeFigure.getSize().getCopy();
        }
        return size.getCopy();
    }

    /**
     * Get the foreground colour for this node
     */
    public Color getForegroundColor() {
        return foreColor;
    }

    /**
     * Set the foreground colour for this node
     */
    public void setForegroundColor(Color c) {
        this.foreColor = c;
        updateFigureForModel(nodeFigure);
    }

    /**
     * Get the background colour for this node. This is the color the node will
     * be if it is not currently highlighted. This color is meaningless if a
     * custom figure has been set.
     */
    public Color getBackgroundColor() {
        return backColor;
    }

    /**
     * Permanently sets the background color (unhighlighted). This color has no
     * effect if a custom figure has been set.
     * 
     * @param c
     */
    public void setBackgroundColor(Color c) {
        backColor = c;
        updateFigureForModel(nodeFigure);
    }

    /**
     * Sets the tooltip on this node. This tooltip will display if the mouse
     * hovers over the node. Setting the tooltip has no effect if a custom
     * figure has been set.
     */
    public void setTooltip(IFigure tooltip) {
        hasCustomTooltip = true;
        this.tooltip = tooltip;
        updateFigureForModel(nodeFigure);
    }

    /**
     * Gets the current tooltip for this node. The tooltip returned is
     * meaningless if a custom figure has been set.
     */
    public IFigure getTooltip() {
        return this.tooltip;
    }

    /**
     * Sets the border color.
     * 
     * @param c
     *            the border color.
     */
    public void setBorderColor(Color c) {
        borderColor = c;
        updateFigureForModel(nodeFigure);
    }

    /**
     * Sets the highlighted border color.
     * 
     * @param c
     *            the highlighted border color.
     */
    public void setBorderHighlightColor(Color c) {
        this.borderHighlightColor = c;
        updateFigureForModel(nodeFigure);
    }

    /**
     * Get the highlight colour for this node
     */
    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * Set the highlight colour for this node
     */
    public void setHighlightColor(Color c) {
        this.highlightColor = c;
    }

    /**
     * Get the highlight adjacent colour for this node. This is the colour that
     * adjacent nodes will get
     */
    // @tag ADJACENT : Removed highlight adjacent
    /*
     * public Color getHighlightAdjacentColor() { return highlightAdjacentColor;
     * }
     */

    /**
     * Set the highlight adjacent colour for this node. This is the colour that
     * adjacent node will get.
     */
    // @tag ADJACENT : Removed highlight adjacent
    /*
     * public void setHighlightAdjacentColor(Color c) {
     * this.highlightAdjacentColor = c; }
     */

    /**
     * Highlights the node changing the background color and border color. The
     * source and destination connections are also highlighted, and the adjacent
     * nodes are highlighted too in a different color.
     */
    @Override
    public void highlight() {
        if (highlighted == HIGHLIGHT_ON) {
            return;
        }
        // @tag ADJACENT : Removed highlight adjacent
        /*
         * if (ZestStyles.checkStyle(getNodeStyle(),
         * ZestStyles.NODES_HIGHLIGHT_ADJACENT)) { for (Iterator iter =
         * sourceConnections.iterator(); iter.hasNext();) { GraphConnection conn
         * = (GraphConnection) iter.next(); conn.highlight();
         * conn.getDestination().highlightAdjacent(); } for (Iterator iter =
         * targetConnections.iterator(); iter.hasNext();) { GraphConnection conn
         * = (GraphConnection) iter.next(); conn.highlight();
         * conn.getSource().highlightAdjacent(); } }
         */
        if (parent.getItemType() == GraphItem.CONTAINER) {
            ((GraphContainer) parent).highlightNode(this);
        } else {
            ((Graph) parent).highlightNode(this);
        }
        highlighted = HIGHLIGHT_ON;
        updateFigureForModel(getNodeFigure());
    }

    /**
     * Restores the nodes original background color and border width.
     */
    @Override
    public void unhighlight() {

        // @tag ADJACENT : Removed highlight adjacent
        // boolean highlightedAdjacently = (highlighted == HIGHLIGHT_ADJACENT);
        if (highlighted == HIGHLIGHT_NONE) {
            return;
        }
        // @tag ADJACENT : Removed highlight adjacent
        /*
         * if (!highlightedAdjacently) { // IF we are highlighted as an adjacent
         * node, we don't need to deal // with our connections. if
         * (ZestStyles.checkStyle(getNodeStyle(),
         * ZestStyles.NODES_HIGHLIGHT_ADJACENT)) { // unhighlight the adjacent
         * edges for (Iterator iter = sourceConnections.iterator();
         * iter.hasNext();) { GraphConnection conn = (GraphConnection)
         * iter.next(); conn.unhighlight(); if (conn.getDestination() != this) {
         * conn.getDestination().unhighlight(); } } for (Iterator iter =
         * targetConnections.iterator(); iter.hasNext();) { GraphConnection conn
         * = (GraphConnection) iter.next(); conn.unhighlight(); if
         * (conn.getSource() != this) { conn.getSource().unhighlight(); } } } }
         */
        if (parent.getItemType() == GraphItem.CONTAINER) {
            ((GraphContainer) parent).unhighlightNode(this);
        } else {
            ((Graph) parent).unhighlightNode(this);
        }
        highlighted = HIGHLIGHT_NONE;
        updateFigureForModel(nodeFigure);

    }

    protected void refreshLocation() {
        Point loc = this.getLocation();
        Dimension size = this.getSize();
        Rectangle bounds = new Rectangle(loc, size);

        if (nodeFigure == null || nodeFigure.getParent() == null) {
            return; // node figure has not been created yet
        }
        // nodeFigure.setBounds(bounds);
        nodeFigure.getParent().setConstraint(nodeFigure, bounds);
    }

    /**
     * Highlights this node using the adjacent highlight color. This only does
     * something if highlighAdjacentNodes is set to true and if the node isn't
     * already highlighted.
     * 
     * @see #setHighlightAdjacentNodes(boolean)
     */
    // @tag ADJACENT : removed highlight adjacent
    /*
     * public void highlightAdjacent() { if (highlighted > 0) { return; }
     * highlighted = HIGHLIGHT_ADJACENT; updateFigureForModel(nodeFigure); if
     * (parent.getItemType() == GraphItem.CONTAINER) { ((GraphContainer)
     * parent).highlightNode(this); } else { ((Graph)
     * parent).highlightNode(this); } }
     */

    /**
     * Returns if the nodes adjacent to this node will be highlighted when this
     * node is selected.
     * 
     * @return GraphModelNode
     */
    // @tag ADJACENT : Removed highlight adjacent
    /*
     * public boolean isHighlightAdjacentNodes() { return
     * ZestStyles.checkStyle(nodeStyle, ZestStyles.NODES_HIGHLIGHT_ADJACENT); }
     */

    /**
     * Sets if the adjacent nodes to this one should be highlighted when this
     * node is selected.
     * 
     * @param highlightAdjacentNodes
     *            The highlightAdjacentNodes to set.
     */
    // @tag ADJACENT : Removed highlight adjacent
    /*
     * public void setHighlightAdjacentNodes(boolean highlightAdjacentNodes) {
     * if (!highlightAdjacentNodes) { this.nodeStyle |=
     * ZestStyles.NODES_HIGHLIGHT_ADJACENT; this.nodeStyle ^=
     * ZestStyles.NODES_HIGHLIGHT_ADJACENT; return; } this.nodeStyle |=
     * ZestStyles.NODES_HIGHLIGHT_ADJACENT; }
     */

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        updateFigureForModel(nodeFigure);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        updateFigureForModel(nodeFigure);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Item#setText(java.lang.String)
     */
    @Override
    public void setText(String string) {
        if (string == null) {
            string = ""; //$NON-NLS-1$
        }
        super.setText(string);

        if (nodeFigure != null) {
            updateFigureForModel(this.nodeFigure);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.widgets.Item#setImage(org.eclipse.swt.graphics.Image)
     */
    @Override
    public void setImage(Image image) {
        super.setImage(image);
        if (nodeFigure != null) {
            updateFigureForModel(nodeFigure);
        }
    }

    /**
     * Gets the graphModel that this node is contained in
     * 
     * @return The graph model that this node is contained in
     */
    @Override
    public Graph getGraphModel() {
        return this.graph;
    }

    /**
     * @return the nodeStyle
     */
    public int getNodeStyle() {
        return nodeStyle;
    }

    /**
     * @param nodeStyle
     *            the nodeStyle to set
     */
    public void setNodeStyle(int nodeStyle) {
        this.nodeStyle = nodeStyle;
        this.cacheLabel = ((this.nodeStyle & ZestStyles.NODES_CACHE_LABEL) > 0) ? true
                : false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setSize
     * (double, double)
     */
    public void setSize(double width, double height) {
        if ((width != size.width) || (height != size.height)) {
            size.width = (int) width;
            size.height = (int) height;
            refreshLocation();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#
     * getBorderHighlightColor()
     */
    public Color getBorderHighlightColor() {
        return borderHighlightColor;
    }

    public boolean cacheLabel() {
        return this.cacheLabel;
    }

    public void setCacheLabel(boolean cacheLabel) {
        this.cacheLabel = cacheLabel;
    }

    public IFigure getNodeFigure() {
        return this.nodeFigure;
    }

    @Override
    public void setVisible(boolean visible) {
        // graph.addRemoveFigure(this, visible);
        this.visible = visible;
        this.getFigure().setVisible(visible);
        List sConnections = (this).getSourceConnections();
        List tConnections = (this).getTargetConnections();
        for (Iterator iterator2 = sConnections.iterator(); iterator2.hasNext();) {
            GraphConnection connection = (GraphConnection) iterator2.next();
            connection.setVisible(visible);
        }

        for (Iterator iterator2 = tConnections.iterator(); iterator2.hasNext();) {
            GraphConnection connection = (GraphConnection) iterator2.next();
            connection.setVisible(visible);
        }
    }

    @Override
    public int getStyle() {
        return super.getStyle() | this.getNodeStyle();
    }

    /***************************************************************************
     * PRIVATE MEMBERS
     **************************************************************************/

    private IFigure fishEyeFigure = null;
    private Font fishEyeFont = null;
    private boolean isFisheyeEnabled;

    protected IFigure fishEye(boolean enable, boolean animate) {
        if (isDisposed) {
            // If a fisheyed figure is still left on the canvas, we could get
            // called once more after the dispose is called. Since we cleaned
            // up everything on dispose, we can just return null here.
            return null;
        }
        if (!checkStyle(ZestStyles.NODES_FISHEYE)) {
            return null;
        }
        if (enable) {
            // Create the fish eye label
            fishEyeFigure = createFishEyeFigure();

            // Get the current Bounds
            Rectangle rectangle = nodeFigure.getBounds().getCopy();

            FontData fontData = Display.getCurrent().getSystemFont()
                    .getFontData()[0];
            fontData.setHeight(12);
            fishEyeFont = new Font(Display.getCurrent(), fontData);
            fishEyeFigure.setFont(fishEyeFont);

            // Calculate how much we have to expand the current bounds to get to
            // the new bounds
            Dimension newSize = fishEyeFigure.getPreferredSize();
            Rectangle currentSize = rectangle.getCopy();
            nodeFigure.translateToAbsolute(currentSize);
            int expandedH = (newSize.height - currentSize.height) / 2 + 1;
            int expandedW = (newSize.width - currentSize.width) / 2 + 1;
            Dimension expandAmount = new Dimension(expandedW, expandedH);
            nodeFigure.translateToAbsolute(rectangle);
            rectangle
                    .expand(new Insets(expandAmount.height, expandAmount.width,
                            expandAmount.height, expandAmount.width));
            if (expandedH <= 0 && expandedW <= 0) {
                return null;
            }

            // Add the fisheye
            this.getGraphModel().fishEye(nodeFigure, fishEyeFigure, rectangle,
                    true);
            if (fishEyeFigure != null) {
                isFisheyeEnabled = true;
            }
            return fishEyeFigure;

        } else {
            // Remove the fisheye and dispose the font
            this.getGraphModel().removeFishEye(fishEyeFigure, nodeFigure,
                    animate);
            if (fishEyeFont != null) {
                this.fishEyeFont.dispose();
                this.fishEyeFont = null;
            }
            isFisheyeEnabled = false;
            return null;
        }
    }

    IContainer getParent() {
        return parent;
    }

    boolean isHighlighted() {
        return highlighted > 0;
    }

    void invokeLayoutListeners(LayoutConstraint constraint) {
        graph.invokeConstraintAdapters(this, constraint);
    }

    protected void updateFigureForModel(IFigure currentFigure) {
        if (currentFigure == null) {
            return;
        }

        if (!(currentFigure instanceof GraphLabel)) {
            return;
        }
        GraphLabel figure = (GraphLabel) currentFigure;
        IFigure toolTip;

        if (!checkStyle(ZestStyles.NODES_HIDE_TEXT)) {
            figure.setText(this.getText());
        }
        figure.setIcon(getImage());

        if (highlighted == HIGHLIGHT_ON) {
            figure.setForegroundColor(getForegroundColor());
            figure.setBackgroundColor(getHighlightColor());
            figure.setBorderColor(getBorderHighlightColor());
        } else {
            figure.setForegroundColor(getForegroundColor());
            figure.setBackgroundColor(getBackgroundColor());
            figure.setBorderColor(getBorderColor());
        }

        figure.setBorderWidth(getBorderWidth());

        figure.setFont(getFont());

        if (this.getTooltip() == null && hasCustomTooltip == false) {
            // if we have a custom tooltip, don't try and create our own.
            toolTip = new Label();
            ((Label) toolTip).setText(getText());
        } else {
            toolTip = this.getTooltip();
        }
        figure.setToolTip(toolTip);

        refreshLocation();

        if (isFisheyeEnabled) {
            IFigure newFisheyeFigure = createFishEyeFigure();
            if (graph.replaceFishFigure(this.fishEyeFigure, newFisheyeFigure)) {
                this.fishEyeFigure = newFisheyeFigure;
            }
        }
    }

    protected IFigure createFigureForModel() {
        GraphNode node = this;
        boolean cacheLabel = (this).cacheLabel();
        GraphLabel label = new GraphLabel(node.getText(), node.getImage(),
                cacheLabel);
        label.setFont(this.font);
        if (checkStyle(ZestStyles.NODES_HIDE_TEXT)) {
            label.setText(""); //$NON-NLS-1$
        }
        updateFigureForModel(label);
        return label;
    }

    private IFigure createFishEyeFigure() {
        GraphNode node = this;
        boolean cacheLabel = this.cacheLabel();
        GraphLabel label = new GraphLabel(node.getText(), node.getImage(),
                cacheLabel);

        if (!checkStyle(ZestStyles.NODES_HIDE_TEXT)) {
            label.setText(this.getText());
        }
        label.setIcon(getImage());

        // @tag TODO: Add border and foreground colours to highlight
        // (this.borderColor)
        if (highlighted == HIGHLIGHT_ON) {
            label.setForegroundColor(getForegroundColor());
            label.setBackgroundColor(getHighlightColor());
        } else {
            label.setForegroundColor(getForegroundColor());
            label.setBackgroundColor(getBackgroundColor());
        }

        label.setFont(getFont());
        return label;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    void addSourceConnection(GraphConnection connection) {
        this.sourceConnections.add(connection);
    }

    void addTargetConnection(GraphConnection connection) {
        this.targetConnections.add(connection);
    }

    void removeSourceConnection(GraphConnection connection) {
        this.sourceConnections.remove(connection);
    }

    void removeTargetConnection(GraphConnection connection) {
        this.targetConnections.remove(connection);
    }

    /**
     * Sets the node as selected.
     */
    void setSelected(boolean selected) {
        if (selected == isSelected()) {
            return;
        }
        if (selected) {
            highlight();
        } else {
            unhighlight();
        }
        this.selected = selected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.mylar.zest.core.widgets.IGraphItem#getItemType()
     */
    @Override
    public int getItemType() {
        return NODE;
    }

    class LayoutGraphNode implements LayoutEntity {
        Object layoutInformation = null;

        @Override
        public double getHeightInLayout() {
            return getSize().height;
        }

        @Override
        public Object getLayoutInformation() {
            return layoutInformation;
        }

        @Override
        public String toString() {
            return getText();
        }

        @Override
        public double getWidthInLayout() {
            return getSize().width;
        }

        @Override
        public double getXInLayout() {
            return getLocation().x;
        }

        @Override
        public double getYInLayout() {
            return getLocation().y;
        }

        @Override
        public void populateLayoutConstraint(LayoutConstraint constraint) {
            invokeLayoutListeners(constraint);
        }

        @Override
        public void setLayoutInformation(Object internalEntity) {
            this.layoutInformation = internalEntity;

        }

        @Override
        public void setLocationInLayout(double x, double y) {
            setLocation(x, y);
        }

        @Override
        public void setSizeInLayout(double width, double height) {
            setSize(width, height);
        }

        /**
         * Compares two nodes.
         */
        @Override
        public int compareTo(Object otherNode) {
            int rv = 0;
            if (otherNode instanceof GraphNode) {
                GraphNode node = (GraphNode) otherNode;
                if (getText() != null) {
                    rv = getText().compareTo(node.getText());
                }
            }
            return rv;
        }

        @Override
        public Object getGraphData() {
            return GraphNode.this;
        }

        @Override
        public void setGraphData(Object o) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    IFigure getFigure() {
        if (this.nodeFigure == null) {
            initFigure();
        }
        return this.getNodeFigure();
    }

    void paint() {

    }
}
