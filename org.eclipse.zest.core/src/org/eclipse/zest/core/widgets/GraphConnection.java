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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.internal.LoopAnchor;
import org.eclipse.zest.core.widgets.internal.PolylineArcConnection;
import org.eclipse.zest.core.widgets.internal.RoundedChopboxAnchor;
import org.eclipse.zest.core.widgets.internal.ZestRootLayer;
import org.eclipse.zest.layouts.LayoutBendPoint;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;

/**
 * This is the graph connection model which stores the source and destination
 * nodes and the properties of this connection (color, line width etc).
 * 
 * @author Chris Callendar
 * 
 * @author Ian Bull
 */
public class GraphConnection extends GraphItem {

    private Font font;
    private GraphNode sourceNode;
    private GraphNode destinationNode;

    private double weight;
    private Color color;
    private Color highlightColor;
    private Color foreground;
    private int lineWidth;
    private int lineStyle;
    private final Graph graphModel;

    private int connectionStyle;
    private int curveDepth;
    private boolean isDisposed = false;

    private Label connectionLabel = null;
    private PolylineArcConnection connectionFigure = null;
    private PolylineArcConnection cachedConnectionFigure = null;
    private Connection sourceContainerConnectionFigure = null;
    private Connection targetContainerConnectionFigure = null;

    /**
     * The state of visibility set by the user.
     */
    private boolean visible;

    private IFigure tooltip;

    private boolean highlighted;
    private GraphLayoutConnection layoutConnection = null;
    private boolean hasCustomTooltip;

    public GraphConnection(Graph graphModel, int style, GraphNode source,
            GraphNode destination) {
        super(graphModel, style);

        this.connectionStyle |= graphModel.getConnectionStyle();
        this.connectionStyle |= style;
        this.sourceNode = source;
        this.destinationNode = destination;
        this.visible = true;
        this.color = ColorConstants.lightGray;
        this.foreground = ColorConstants.lightGray;
        this.highlightColor = graphModel.DARK_BLUE;
        this.lineWidth = 1;
        this.lineStyle = Graphics.LINE_SOLID;
        setWeight(weight);
        this.graphModel = graphModel;
        this.curveDepth = 0;
        this.layoutConnection = new GraphLayoutConnection();
        this.font = Display.getDefault().getSystemFont();
        registerConnection(source, destination);
    }

    private void registerConnection(GraphNode source, GraphNode destination) {
        if (source.getSourceConnections().contains(this)) {
            source.removeSourceConnection(this);
        }
        if (destination.getTargetConnections().contains(this)) {
            destination.removeTargetConnection(this);
        }
        (source).addSourceConnection(this);
        (destination).addTargetConnection(this);

        if (source.getParent().getItemType() == GraphItem.CONTAINER
                && destination.getParent().getItemType() == GraphItem.CONTAINER
                && (source.getParent() == destination.getParent())) {
            // 196189: Edges should not draw on the edge layer if both the src
            // and dest are in the same container
            // https://bugs.eclipse.org/bugs/show_bug.cgi?id=196189
            graphModel.addConnection(this, ZestRootLayer.EDGES_ON_TOP);
        } else {
            graphModel.addConnection(this, true);
        }

        if ((source.getParent()).getItemType() == GraphItem.CONTAINER) {
            // If the container of the source is a container, we need to draw
            // another
            // arc on that arc layer
            sourceContainerConnectionFigure = doCreateFigure();
            ((GraphContainer) source.getParent())
                    .addConnectionFigure((PolylineConnection) sourceContainerConnectionFigure);
            this.setVisible(false);
        }

        if ((destination.getParent()).getItemType() == GraphItem.CONTAINER) { // &&
                                                                                // src_destSameContainer
                                                                                // ==
                                                                                // false)
                                                                                // {
            // If the container of the source is a container, we need to draw
            // another
            // arc on that arc layer
            targetContainerConnectionFigure = doCreateFigure();
            ((GraphContainer) destination.getParent())
                    .addConnectionFigure((PolylineConnection) targetContainerConnectionFigure);
            this.setVisible(false);
        }
        graphModel.getGraph().registerItem(this);
    }

    void removeFigure() {
        if (connectionFigure.getParent() != null) {
            if (connectionFigure.getParent() instanceof ZestRootLayer) {
                ((ZestRootLayer) connectionFigure.getParent())
                        .removeConnection(connectionFigure);
            } else {
                connectionFigure.getParent().remove(connectionFigure);
            }
        }
        connectionFigure = null;
        if (sourceContainerConnectionFigure != null) {
            sourceContainerConnectionFigure.getParent().remove(
                    sourceContainerConnectionFigure);
            sourceContainerConnectionFigure = null;
        }
        if (targetContainerConnectionFigure != null) {
            targetContainerConnectionFigure.getParent().remove(
                    targetContainerConnectionFigure);
            targetContainerConnectionFigure = null;
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        this.isDisposed = true;
        (getSource()).removeSourceConnection(this);
        (getDestination()).removeTargetConnection(this);
        graphModel.removeConnection(this);
        if (sourceContainerConnectionFigure != null) {
            sourceContainerConnectionFigure.getParent().remove(
                    sourceContainerConnectionFigure);
        }
        if (targetContainerConnectionFigure != null) {
            targetContainerConnectionFigure.getParent().remove(
                    targetContainerConnectionFigure);
        }
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    public Connection getConnectionFigure() {
        if (connectionFigure == null) {
            connectionFigure = createFigure();
        }
        return connectionFigure;
    }

    /**
     * Gets a proxy to this connection that can be used with the Zest layout
     * engine
     * 
     * @return
     */
    public LayoutRelationship getLayoutRelationship() {
        return this.layoutConnection;
    }

    /**
     * Gets the external connection object.
     * 
     * @return Object
     */
    public Object getExternalConnection() {
        return this.getData();
    }

    /**
     * Returns a string like 'source -> destination'
     * 
     * @return String
     */
    @Override
    public String toString() {
        String arrow = (isBidirectionalInLayout() ? " <--> " : " --> "); //$NON-NLS-1$ //$NON-NLS-2$
        String src = (sourceNode != null ? sourceNode.getText() : "null"); //$NON-NLS-1$
        String dest = (destinationNode != null ? destinationNode.getText()
                : "null"); //$NON-NLS-1$
        String weight = "  (weight=" + getWeightInLayout() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        return ("GraphModelConnection: " + src + arrow + dest + weight); //$NON-NLS-1$
    }

    /**
     * Returns the style of this connection. Valid styles are those that begin
     * with CONNECTION in ZestStyles.
     * 
     * @return the style of this connection.
     * @see #ZestStyles
     */
    public int getConnectionStyle() {
        return connectionStyle;
    }

    /**
     * Returns the style of this connection. Valid styles are those that begin
     * with CONNECTION in ZestStyles.
     * 
     * @return the style of this connection.
     * @see #ZestStyles
     */
    public void setConnectionStyle(int style) {
        this.connectionStyle = style;
        updateFigure(this.connectionFigure);
    }

    /**
     * Gets the weight of this connection. The weight must be in {-1, [0-1]}. A
     * weight of -1 means that there is no force/tension between the nodes. A
     * weight of 0 results in the maximum spring length being used (farthest
     * apart). A weight of 1 results in the minimum spring length being used
     * (closest together).
     * 
     * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getWeightInLayout()
     * @return the weight: {-1, [0 - 1]}.
     */
    public double getWeightInLayout() {
        return weight;
    }

    /**
     * Gets the font for the label on this connection
     * 
     * @return
     */
    public Font getFont() {
        return this.font;
    }

    /**
     * Sets the font for the label on this connection.
     * 
     */
    public void setFont(Font f) {
        this.font = f;
    }

    /**
     * Sets the weight for this connection. The weight must be in {-1, [0-1]}. A
     * weight of -1 means that there is no force/tension between the nodes. A
     * weight of 0 results in the maximum spring length being used (farthest
     * apart). A weight of 1 results in the minimum spring length being used
     * (closest together).
     * 
     */
    public void setWeight(double weight) {
        if (weight < 0) {
            this.weight = -1;
        } else if (weight > 1) {
            this.weight = 1;
        } else {
            this.weight = weight;
        }
    }

    /**
     * Returns the color of this connection.
     * 
     * @return Color
     */
    public Color getLineColor() {
        return color;
    }

    /**
     * Sets the highlight color.
     * 
     * @param color
     *            the color to use for highlighting.
     */
    public void setHighlightColor(Color color) {
        this.highlightColor = color;
    }

    /**
     * @return the highlight color
     */
    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * Perminently sets the color of this line to the given color. This will
     * become the color of the line when it is not highlighted. If you would
     * like to temporarily change the color of the line, use changeLineColor.
     * 
     * @param color
     *            the color to be set.
     * @see changeLineColor(Color color)
     */
    public void setLineColor(Color color) {
        this.foreground = color;
        changeLineColor(foreground);
    }

    /**
     * Sets the connection color.
     * 
     * @param color
     */
    public void changeLineColor(Color color) {
        this.color = color;
        updateFigure(connectionFigure);
    }

    /**
     * Sets the tooltip on this node. This tooltip will display if the mouse
     * hovers over the node. Setting the tooltip has no effect if a custom
     * figure has been set.
     */
    public void setTooltip(IFigure tooltip) {
        hasCustomTooltip = true;
        this.tooltip = tooltip;
        updateFigure(connectionFigure);
    }

    /**
     * Gets the current tooltip for this node. The tooltip returned is
     * meaningless if a custom figure has been set.
     */
    public IFigure getTooltip() {
        return this.tooltip;
    }

    /**
     * Returns the connection line width.
     * 
     * @return int
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * Sets the connection line width.
     * 
     * @param lineWidth
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        updateFigure(connectionFigure);
    }

    /**
     * Returns the connection line style.
     * 
     * @return int
     */
    public int getLineStyle() {
        return lineStyle;
    }

    /**
     * Sets the connection line style.
     * 
     * @param lineStyle
     */
    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
        updateFigure(connectionFigure);
    }

    /**
     * Gets the source node for this relationship
     * 
     * @return GraphModelNode
     */
    public GraphNode getSource() {
        return this.sourceNode;
    }

    /**
     * Gets the target node for this relationship
     * 
     * @return GraphModelNode
     */
    public GraphNode getDestination() {
        return this.destinationNode;
    }

    /**
     * Highlights this node. Uses the default highlight color.
     */
    @Override
    public void highlight() {
        if (highlighted) {
            return;
        }
        highlighted = true;
        updateFigure(connectionFigure);
        graphModel.highlightEdge(this);
    }

    /**
     * Unhighlights this node. Uses the default color.
     */
    @Override
    public void unhighlight() {
        if (!highlighted) {
            return;
        }
        highlighted = false;
        updateFigure(connectionFigure);
        graphModel.unhighlightEdge(this);
    }

    /**
     * Returns true if this connection is highlighted, false otherwise
     * 
     * @return
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Gets the graph model that this connection is in
     * 
     * @return The graph model that this connection is contained in
     */
    @Override
    public Graph getGraphModel() {
        return this.graphModel;
    }

    /**
     * Sets the curve depth of the arc. The curve depth is defined as the
     * maximum distance from any point on the chord (i.e. a vector normal to the
     * chord with magnitude d).
     * 
     * If 0 is set, a Polyline Connection will be used, otherwise a
     * PolylineArcConnectoin will be used. Negative depths are also supported.
     * 
     * @param depth
     *            The depth of the curve
     */
    public void setCurveDepth(int depth) {
        if (this.curveDepth == 0 && depth != 0 || this.curveDepth != 0
                && depth == 0) {
            // There is currently no curve, so we have to create
            // a curved connection
            this.cachedConnectionFigure = connectionFigure;
            graphModel.removeConnection(this);
            this.curveDepth = depth;
            this.connectionFigure = createFigure();
            registerConnection(sourceNode, destinationNode);
            updateFigure(this.connectionFigure);
        } else {
            this.curveDepth = depth;
            updateFigure(this.connectionFigure);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.mylar.zest.core.widgets.IGraphItem#getItemType()
     */
    @Override
    public int getItemType() {
        return CONNECTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem#setVisible(
     * boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        // graphModel.addRemoveFigure(this, visible);
        if (getSource().isVisible() && getDestination().isVisible() && visible) {
            this.getFigure().setVisible(visible);
            if (sourceContainerConnectionFigure != null) {
                sourceContainerConnectionFigure.setVisible(visible);
            }
            if (targetContainerConnectionFigure != null) {
                targetContainerConnectionFigure.setVisible(visible);
            }
            this.visible = visible;
        } else {
            this.getFigure().setVisible(false);
            if (sourceContainerConnectionFigure != null) {
                sourceContainerConnectionFigure.setVisible(false);
            }
            if (targetContainerConnectionFigure != null) {
                targetContainerConnectionFigure.setVisible(false);
            }
            this.visible = false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.mylar.zest.core.widgets.IGraphItem#isVisible()
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Item#setText(java.lang.String)
     */
    @Override
    public void setText(String string) {
        super.setText(string);

        if (this.connectionFigure != null) {
            updateFigure(this.connectionFigure);
        }
    }

    PolylineConnection getSourceContainerConnectionFigure() {
        return (PolylineConnection) sourceContainerConnectionFigure;
    }

    PolylineConnection getTargetContainerConnectionFigure() {
        return (PolylineConnection) targetContainerConnectionFigure;
    }

    private void updateFigure(Connection connection) {
        if (sourceContainerConnectionFigure != null) {
            doUpdateFigure(sourceContainerConnectionFigure);
        }
        if (targetContainerConnectionFigure != null) {
            doUpdateFigure(targetContainerConnectionFigure);
        }
        doUpdateFigure(connection);
    }

    private void doUpdateFigure(Connection connection) {
        if (connection == null || this.isDisposed()) {
            return;
        }
        Shape connectionShape = (Shape) connection;

        connectionShape.setLineStyle(getLineStyle());

        if (this.getText() != null || this.getImage() != null) {
            // Label l = new Label(this.getText(), this.getImage());
            if (this.getImage() != null) {
                this.connectionLabel.setIcon(this.getImage());
            }
            if (this.getText() != null) {
                this.connectionLabel.setText(this.getText());
            }
            this.connectionLabel.setFont(this.getFont());
        }

        if (highlighted) {
            connectionShape.setForegroundColor(getHighlightColor());
            connectionShape.setLineWidth(getLineWidth() * 2);
        } else {
            connectionShape.setForegroundColor(getLineColor());
            connectionShape.setLineWidth(getLineWidth());
        }

        if (connection instanceof PolylineArcConnection) {
            PolylineArcConnection arcConnection = (PolylineArcConnection) connection;
            arcConnection.setDepth(curveDepth);
        }
        if ((connectionStyle & ZestStyles.CONNECTIONS_DIRECTED) > 0) {
            PolygonDecoration decoration = new PolygonDecoration();
            if (getLineWidth() < 3) {
                decoration.setScale(9, 3);
            } else {
                double logLineWith = getLineWidth() / 2.0;
                decoration.setScale(7 * logLineWith, 3 * logLineWith);
            }
            ((PolylineConnection) connection).setTargetDecoration(decoration);
        }

        IFigure toolTip;
        if (this.getTooltip() == null && getText() != null
                && getText().length() > 0 && hasCustomTooltip == false) {
            toolTip = new Label();
            ((Label) toolTip).setText(getText());
        } else {
            toolTip = this.getTooltip();
        }
        connection.setToolTip(toolTip);
    }

    private PolylineArcConnection createFigure() {
        /*
         * if ((sourceNode.getParent()).getItemType() == GraphItem.CONTAINER) {
         * GraphContainer container = (GraphContainer) sourceNode.getParent();
         * sourceContainerConnectionFigure = doCreateFigure();
         * container.addConnectionFigure((PolylineConnection)
         * sourceContainerConnectionFigure); } if
         * ((destinationNode.getParent()).getItemType() == GraphItem.CONTAINER)
         * { GraphContainer container = (GraphContainer)
         * destinationNode.getParent(); targetContainerConnectionFigure =
         * doCreateFigure(); container.addConnectionFigure((PolylineConnection)
         * targetContainerConnectionFigure); }
         */

        return doCreateFigure();

    }

    private PolylineArcConnection doCreateFigure() {
        PolylineArcConnection connectionFigure = cachedOrNewConnectionFigure();
        ChopboxAnchor sourceAnchor = null;
        ChopboxAnchor targetAnchor = null;
        this.connectionLabel = new Label();
        Locator labelLocator = null;

        if (getSource() == getDestination()) {
            // If this is a self loop, create a looped arc and put the locator
            // at the top
            // of the connection
            sourceAnchor = new LoopAnchor(getSource().getNodeFigure());
            targetAnchor = new LoopAnchor(getDestination().getNodeFigure());
            labelLocator = new MidpointLocator(connectionFigure, 0) {
                @Override
                protected Point getReferencePoint() {
                    Point p = Point.SINGLETON;
                    p.x = getConnection().getPoints().getPoint(getIndex()).x;
                    p.y = (int) (getConnection().getPoints().getPoint(
                            getIndex()).y - (curveDepth * 1.5));
                    getConnection().translateToAbsolute(p);
                    return p;
                }
            };
        } else {
            if (curveDepth != 0) {
                connectionFigure.setDepth(this.curveDepth);
            }
            sourceAnchor = new RoundedChopboxAnchor(
                    getSource().getNodeFigure(), 8);
            targetAnchor = new RoundedChopboxAnchor(getDestination()
                    .getNodeFigure(), 8);
            labelLocator = new MidpointLocator(connectionFigure, 0);
        }

        connectionFigure.setSourceAnchor(sourceAnchor);
        connectionFigure.setTargetAnchor(targetAnchor);
        connectionFigure.add(this.connectionLabel, labelLocator);

        doUpdateFigure(connectionFigure);
        return connectionFigure;
    }

    private PolylineArcConnection cachedOrNewConnectionFigure() {
        return cachedConnectionFigure == null ? new PolylineArcConnection()
                : cachedConnectionFigure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.mylar.zest.layouts.LayoutRelationship#isBidirectionalInLayout
     * ()
     */
    private boolean isBidirectionalInLayout() {
        return !ZestStyles.checkStyle(connectionStyle,
                ZestStyles.CONNECTIONS_DIRECTED);
    }

    class GraphLayoutConnection implements LayoutRelationship {

        Object layoutInformation = null;

        @Override
        public void clearBendPoints() {
            // @tag TODO : add bendpoints
        }

        @Override
        public LayoutEntity getDestinationInLayout() {
            return getDestination().getLayoutEntity();
        }

        @Override
        public Object getLayoutInformation() {
            return layoutInformation;
        }

        @Override
        public LayoutEntity getSourceInLayout() {
            return getSource().getLayoutEntity();
        }

        @Override
        public void populateLayoutConstraint(LayoutConstraint constraint) {
            graphModel.invokeConstraintAdapters(GraphConnection.this,
                    constraint);
        }

        @Override
        public void setBendPoints(LayoutBendPoint[] bendPoints) {
            // @tag TODO : add bendpoints
        }

        @Override
        public void setLayoutInformation(Object layoutInformation) {
            this.layoutInformation = layoutInformation;
        }

        @Override
        public Object getGraphData() {
            return GraphConnection.this;
        }

        @Override
        public void setGraphData(Object o) {

        }

    }

    @Override
    IFigure getFigure() {
        return this.getConnectionFigure();
    }
}
