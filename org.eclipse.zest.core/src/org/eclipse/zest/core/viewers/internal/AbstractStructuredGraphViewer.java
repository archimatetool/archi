/*******************************************************************************
 * Copyright 2005, 2011 CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.ConstraintAdapter;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;

/**
 * Abstraction of graph viewers to implement functionality used by all of them.
 * Not intended to be implemented by clients. Use one of the provided children
 * instead.
 * 
 * @author Del Myers
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractStructuredGraphViewer extends
        AbstractZoomableViewer {
    /**
     * Contains top-level styles for the entire graph. Set in the constructor. *
     */
    private int graphStyle;

    /**
     * Contains node-level styles for the graph. Set in setNodeStyle(). Defaults
     * are used in the constructor.
     */
    private int nodeStyle;

    /**
     * Contains arc-level styles for the graph. Set in setConnectionStyle().
     * Defaults are used in the constructor.
     */
    private int connectionStyle;

    private HashMap nodesMap = new HashMap();
    private HashMap connectionsMap = new HashMap();

    /**
     * The constraint adapters
     */
    private List constraintAdapters = new ArrayList();

    /**
     * A simple graph comparator that orders graph elements based on their type
     * (connection or node), and their unique object identification.
     */
    private class SimpleGraphComparator implements Comparator {
        TreeSet storedStrings;

        /**
         * 
         */
        public SimpleGraphComparator() {
            this.storedStrings = new TreeSet();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Object arg0, Object arg1) {
            if (arg0 instanceof GraphNode && arg1 instanceof GraphConnection) {
                return 1;
            } else if (arg0 instanceof GraphConnection
                    && arg1 instanceof GraphNode) {
                return -1;
            }
            if (arg0.equals(arg1)) {
                return 0;
            }
            return getObjectString(arg0).compareTo(getObjectString(arg1));
        }

        private String getObjectString(Object o) {
            String s = o.getClass().getName() + "@" //$NON-NLS-1$
                    + Integer.toHexString(o.hashCode());
            while (storedStrings.contains(s)) {
                s = s + 'X';
            }
            return s;
        }
    }

    protected AbstractStructuredGraphViewer(int graphStyle) {
        this.graphStyle = graphStyle;
        this.connectionStyle = SWT.NONE;
        this.nodeStyle = SWT.NONE;

    }

    /**
     * Sets the default style for nodes in this graph. Note: if an input is set
     * on the viewer, a ZestException will be thrown.
     * 
     * @param nodeStyle
     *            the style for the nodes.
     * @see #ZestStyles
     */
    public void setNodeStyle(int nodeStyle) {
        if (getInput() != null) {
            throw new SWTError(SWT.ERROR_UNSPECIFIED);
        }
        this.nodeStyle = nodeStyle;
    }

    /**
     * Sets the default style for connections in this graph. Note: if an input
     * is set on the viewer, a ZestException will be thrown.
     * 
     * @param connectionStyle
     *            the style for the connections.
     * @see #ZestStyles
     */
    public void setConnectionStyle(int connectionStyle) {
        if (getInput() != null) {
            throw new SWTError(SWT.ERROR_UNSPECIFIED);
        }
        if (!ZestStyles.validateConnectionStyle(connectionStyle)) {
            throw new SWTError(SWT.ERROR_INVALID_ARGUMENT);
        }
        this.connectionStyle = connectionStyle;
    }

    /**
     * Returns the style set for the graph
     * 
     * @return The style set of the graph
     */
    public int getGraphStyle() {
        return graphStyle;
    }

    /**
     * Returns the style set for the nodes.
     * 
     * @return the style set for the nodes.
     */
    public int getNodeStyle() {
        return nodeStyle;
    }

    public Graph getGraphControl() {
        return (Graph) getControl();
    }

    /**
     * @return the connection style.
     */
    public int getConnectionStyle() {
        return connectionStyle;
    }

    /**
     * Adds a new constraint adapter to the list of constraints
     * 
     * @param constraintAdapter
     */
    public void addConstraintAdapter(ConstraintAdapter constraintAdapter) {
        this.constraintAdapters.add(constraintAdapter);
    }

    /**
     * Gets all the constraint adapters currently on the viewer
     * 
     * @return
     */
    public List getConstraintAdapters() {
        return this.constraintAdapters;
    }

    /**
     * Sets the layout algorithm for this viewer. Subclasses may place
     * restrictions on the algorithms that it accepts.
     * 
     * @param algorithm
     *            the layout algorithm
     * @param run
     *            true if the layout algorithm should be run immediately. This
     *            is a hint.
     */
    public abstract void setLayoutAlgorithm(LayoutAlgorithm algorithm,
            boolean run);

    /**
     * Gets the current layout algorithm.
     * 
     * @return the current layout algorithm.
     */
    protected abstract LayoutAlgorithm getLayoutAlgorithm();

    /**
     * Equivalent to setLayoutAlgorithm(algorithm, false).
     * 
     * @param algorithm
     */
    public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
        setLayoutAlgorithm(algorithm, false);
    }

    public Object[] getNodeElements() {
        return this.nodesMap.keySet().toArray();
    }

    public Object[] getConnectionElements() {
        return this.connectionsMap.keySet().toArray();
    }

    HashMap getNodesMap() {
        return this.nodesMap;
    }

    GraphNode addGraphModelContainer(Object element) {
        GraphNode node = this.getGraphModelNode(element);
        if (node == null) {
            node = new GraphContainer((Graph) getControl(), SWT.NONE);
            this.nodesMap.put(element, node);
            node.setData(element);
        }
        return node;
    }

    GraphNode addGraphModelNode(IContainer container, Object element) {
        GraphNode node = this.getGraphModelNode(element);
        if (node == null) {
            node = new GraphNode(container, SWT.NONE);
            this.nodesMap.put(element, node);
            node.setData(element);
        }
        return node;
    }

    GraphNode addGraphModelNode(Object element, IFigure figure) {
        GraphNode node = this.getGraphModelNode(element);
        if (node == null) {
            if (figure != null) {
                node = new CGraphNode((Graph) getControl(), SWT.NONE, figure);
                this.nodesMap.put(element, node);
                node.setData(element);
            } else {
                node = new GraphNode((Graph) getControl(), SWT.NONE);
                this.nodesMap.put(element, node);
                node.setData(element);
            }
        }
        return node;
    }

    GraphConnection addGraphModelConnection(Object element, GraphNode source,
            GraphNode target) {
        GraphConnection connection = this.getGraphModelConnection(element);
        if (connection == null) {
            connection = new GraphConnection((Graph) getControl(), SWT.NONE,
                    source, target);
            this.connectionsMap.put(element, connection);
            connection.setData(element);
        }
        return connection;

    }

    GraphConnection getGraphModelConnection(Object obj) {
        return (GraphConnection) this.connectionsMap.get(obj);
    }

    GraphNode getGraphModelNode(Object obj) {
        return (GraphNode) this.nodesMap.get(obj);
    }

    void removeGraphModelConnection(Object obj) {
        GraphConnection connection = (GraphConnection) connectionsMap.get(obj);
        if (connection != null) {
            connectionsMap.remove(obj);
            if (!connection.isDisposed()) {
                connection.dispose();
            }
        }
    }

    void removeGraphModelNode(Object obj) {
        GraphNode node = (GraphNode) nodesMap.get(obj);
        if (node != null) {
            nodesMap.remove(obj);
            if (!node.isDisposed()) {
                node.dispose();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.
     * Object)
     */
    @Override
    protected void internalRefresh(Object element) {
        if (getInput() == null) {
            return;
        }
        if (element == getInput()) {
            getFactory().refreshGraph(getGraphControl());
        } else {
            getFactory().refresh(getGraphControl(), element);
        }
        // After all the items are loaded, we call update to ensure drawing.
        // This way the damaged area does not get too big if we start
        // adding and removing more nodes
        getGraphControl().getLightweightSystem().getUpdateManager()
                .performUpdate();
    }

    @Override
    protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
        if (item == getGraphControl()) {
            getFactory().update(getNodesArray(getGraphControl()));
            getFactory().update(getConnectionsArray(getGraphControl()));
        } else if (item instanceof GraphItem) {
            getFactory().update((GraphItem) item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.
     * Object)
     */
    @Override
    protected Widget doFindInputItem(Object element) {

        if (element == getInput() && element instanceof Widget) {
            return (Widget) element;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
     */
    @Override
    protected Widget doFindItem(Object element) {
        Widget node = (Widget) nodesMap.get(element);
        Widget connection = (Widget) connectionsMap.get(element);
        return (node != null) ? node : connection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
     */
    @Override
    protected List getSelectionFromWidget() {
        List internalSelection = getWidgetSelection();
        LinkedList externalSelection = new LinkedList();
        for (Iterator i = internalSelection.iterator(); i.hasNext();) {
            Object data = ((GraphItem) i.next()).getData();
            if (data != null) {
                externalSelection.add(data);
            }
        }
        return externalSelection;
    }

    protected GraphItem[] /* GraphItem */findItems(List l) {
        if (l == null) {
            return new GraphItem[0];
        }

        ArrayList list = new ArrayList();
        Iterator iterator = l.iterator();

        while (iterator.hasNext()) {
            GraphItem w = (GraphItem) findItem(iterator.next());
            list.add(w);
        }
        return (GraphItem[]) list.toArray(new GraphItem[list.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.
     * util.List, boolean)
     */
    @Override
    protected void setSelectionToWidget(List l, boolean reveal) {
        Graph control = (Graph) getControl();
        List selection = new LinkedList();
        for (Iterator i = l.iterator(); i.hasNext();) {
            Object obj = i.next();
            GraphNode node = (GraphNode) nodesMap.get(obj);
            GraphConnection conn = (GraphConnection) connectionsMap.get(obj);
            if (node != null) {
                selection.add(node);
            }
            if (conn != null) {
                selection.add(conn);
            }
        }
        control.setSelection((GraphNode[]) selection
                .toArray(new GraphNode[selection.size()]));
    }

    /**
     * Gets the internal model elements that are selected.
     * 
     * @return
     */
    protected List getWidgetSelection() {
        Graph control = (Graph) getControl();
        return control.getSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    protected void inputChanged(Object input, Object oldInput) {
        IStylingGraphModelFactory factory = getFactory();
        factory.setConnectionStyle(getConnectionStyle());
        factory.setNodeStyle(getNodeStyle());

        // Save the old map so we can set the size and position of any nodes
        // that are the same
        Map oldNodesMap = nodesMap;
        Graph graph = (Graph) getControl();
        graph.setSelection(new GraphNode[0]);

        Iterator iterator = nodesMap.values().iterator();
        while (iterator.hasNext()) {
            GraphNode node = (GraphNode) iterator.next();
            if (!node.isDisposed()) {
                node.dispose();
            }
        }

        iterator = connectionsMap.values().iterator();
        while (iterator.hasNext()) {
            GraphConnection connection = (GraphConnection) iterator.next();
            if (!connection.isDisposed()) {
                connection.dispose();
            }
        }

        nodesMap = new HashMap();
        connectionsMap = new HashMap();

        graph = factory.createGraphModel(graph);

        ((Graph) getControl()).setNodeStyle(getNodeStyle());
        ((Graph) getControl()).setConnectionStyle(getConnectionStyle());

        // check if any of the pre-existing nodes are still present
        // in this case we want them to keep the same location & size
        for (Iterator iter = oldNodesMap.keySet().iterator(); iter.hasNext();) {
            Object data = iter.next();
            GraphNode newNode = (GraphNode) nodesMap.get(data);
            if (newNode != null) {
                GraphNode oldNode = (GraphNode) oldNodesMap.get(data);
                newNode.setLocation(oldNode.getLocation().x,
                        oldNode.getLocation().y);
                if (oldNode.isSizeFixed()) {
                    newNode.setSize(oldNode.getSize().width,
                            oldNode.getSize().height);
                }
            }
        }

        applyLayout();
    }

    /**
     * Returns the factory used to create the model. This must not be called
     * before the content provider is set.
     * 
     * @return
     */
    protected abstract IStylingGraphModelFactory getFactory();

    protected void filterVisuals() {
        if (getGraphControl() == null) {
            return;
        }
        Object[] filtered = getFilteredChildren(getInput());
        SimpleGraphComparator comparator = new SimpleGraphComparator();
        TreeSet filteredElements = new TreeSet(comparator);
        TreeSet unfilteredElements = new TreeSet(comparator);
        List connections = getGraphControl().getConnections();
        List nodes = getGraphControl().getNodes();
        if (filtered.length == 0) {
            // set everything to invisible.
            // @tag zest.bug.156528-Filters.check : should we only filter out
            // the nodes?
            for (Iterator i = connections.iterator(); i.hasNext();) {
                GraphConnection c = (GraphConnection) i.next();
                c.setVisible(false);
            }
            for (Iterator i = nodes.iterator(); i.hasNext();) {
                GraphNode n = (GraphNode) i.next();
                n.setVisible(false);
            }
            return;
        }
        for (Iterator i = connections.iterator(); i.hasNext();) {
            GraphConnection c = (GraphConnection) i.next();
            if (c.getExternalConnection() != null) {
                unfilteredElements.add(c);
            }
        }
        for (Iterator i = nodes.iterator(); i.hasNext();) {
            GraphNode n = (GraphNode) i.next();
            if (n.getData() != null) {
                unfilteredElements.add(n);
            }
        }
        for (int i = 0; i < filtered.length; i++) {
            Object modelElement = connectionsMap.get(filtered[i]);
            if (modelElement == null) {
                modelElement = nodesMap.get(filtered[i]);
            }
            if (modelElement != null) {
                filteredElements.add(modelElement);
            }
        }
        unfilteredElements.removeAll(filteredElements);
        // set all the elements that did not pass the filters to invisible, and
        // all the elements that passed to visible.
        while (unfilteredElements.size() > 0) {
            GraphItem i = (GraphItem) unfilteredElements.first();
            i.setVisible(false);
            unfilteredElements.remove(i);
        }
        while (filteredElements.size() > 0) {
            GraphItem i = (GraphItem) filteredElements.first();
            i.setVisible(true);
            filteredElements.remove(i);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.StructuredViewer#getRawChildren(java.lang.Object
     * )
     */
    @Override
    protected Object[] getRawChildren(Object parent) {
        if (parent == getInput()) {
            // get the children from the model.
            LinkedList children = new LinkedList();
            if (getGraphControl() != null) {
                List connections = getGraphControl().getConnections();
                List nodes = getGraphControl().getNodes();
                for (Iterator i = connections.iterator(); i.hasNext();) {
                    GraphConnection c = (GraphConnection) i.next();
                    if (c.getExternalConnection() != null) {
                        children.add(c.getExternalConnection());
                    }
                }
                for (Iterator i = nodes.iterator(); i.hasNext();) {
                    GraphNode n = (GraphNode) i.next();
                    if (n.getData() != null) {
                        children.add(n.getData());
                    }
                }
                return children.toArray();
            }
        }
        return super.getRawChildren(parent);
    }

    /**
     * 
     */
    @Override
    public void reveal(Object element) {
        Widget[] items = this.findItems(element);
        for (int i = 0; i < items.length; i++) {
            Widget item = items[i];
            if (item instanceof GraphNode) {
                GraphNode graphModelNode = (GraphNode) item;
                graphModelNode.highlight();
            } else if (item instanceof GraphConnection) {
                GraphConnection graphModelConnection = (GraphConnection) item;
                graphModelConnection.highlight();
            }
        }
    }

    public void unReveal(Object element) {
        Widget[] items = this.findItems(element);
        for (int i = 0; i < items.length; i++) {
            Widget item = items[i];
            if (item instanceof GraphNode) {
                GraphNode graphModelNode = (GraphNode) item;
                graphModelNode.unhighlight();
            } else if (item instanceof GraphConnection) {
                GraphConnection graphModelConnection = (GraphConnection) item;
                graphModelConnection.unhighlight();
            }
        }
    }

    /**
     * Applies the viewers layouts.
     * 
     */
    public abstract void applyLayout();

    /**
     * Removes the given connection object from the layout algorithm and the
     * model.
     * 
     * @param connection
     */
    public void removeRelationship(Object connection) {
        GraphConnection relationship = (GraphConnection) connectionsMap
                .get(connection);

        if (relationship != null) {
            // remove the relationship from the layout algorithm
            if (getLayoutAlgorithm() != null) {
                getLayoutAlgorithm().removeRelationship(
                        relationship.getLayoutRelationship());
            }
            // remove the relationship from the model
            relationship.dispose();
        }
    }

    /**
     * Creates a new node and adds it to the graph. If it already exists nothing
     * happens.
     * 
     * @param newNode
     */
    public void addNode(Object element) {
        if (nodesMap.get(element) == null) {
            // create the new node
            getFactory().createNode(getGraphControl(), element);

        }
    }

    /**
     * Removes the given element from the layout algorithm and the model.
     * 
     * @param element
     *            The node element to remove.
     */
    public void removeNode(Object element) {
        GraphNode node = (GraphNode) nodesMap.get(element);

        if (node != null) {
            // remove the node from the layout algorithm and all the connections
            if (getLayoutAlgorithm() != null) {
                getLayoutAlgorithm().removeEntity(node.getLayoutEntity());
                getLayoutAlgorithm().removeRelationships(
                        node.getSourceConnections());
                getLayoutAlgorithm().removeRelationships(
                        node.getTargetConnections());
            }
            // remove the node and it's connections from the model
            node.dispose();
        }
    }

    /**
     * Creates a new relationship between the source node and the destination
     * node. If either node doesn't exist then it will be created.
     * 
     * @param connection
     *            The connection data object.
     * @param srcNode
     *            The source node data object.
     * @param destNode
     *            The destination node data object.
     */
    public void addRelationship(Object connection, Object srcNode,
            Object destNode) {
        // create the new relationship
        IStylingGraphModelFactory modelFactory = getFactory();
        modelFactory.createConnection(getGraphControl(), connection, srcNode,
                destNode);

    }

    /**
     * Adds a new relationship given the connection. It will use the content
     * provider to determine the source and destination nodes.
     * 
     * @param connection
     *            The connection data object.
     */
    public void addRelationship(Object connection) {
        IStylingGraphModelFactory modelFactory = getFactory();
        if (connectionsMap.get(connection) == null) {
            if (modelFactory.getContentProvider() instanceof IGraphContentProvider) {
                IGraphContentProvider content = ((IGraphContentProvider) modelFactory
                        .getContentProvider());
                Object source = content.getSource(connection);
                Object dest = content.getDestination(connection);
                // create the new relationship
                modelFactory.createConnection(getGraphControl(), connection,
                        source, dest);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Converts the list of GraphModelConnection objects into an array and
     * returns it.
     * 
     * @return GraphModelConnection[]
     */
    protected GraphConnection[] getConnectionsArray(Graph graph) {
        GraphConnection[] connsArray = new GraphConnection[graph
                .getConnections().size()];
        connsArray = (GraphConnection[]) graph.getConnections().toArray(
                connsArray);
        return connsArray;
    }

    /**
     * Converts the list of GraphModelNode objects into an array an returns it.
     * 
     * @return GraphModelNode[]
     */
    protected GraphNode[] getNodesArray(Graph graph) {
        GraphNode[] nodesArray = new GraphNode[graph.getNodes().size()];
        nodesArray = (GraphNode[]) graph.getNodes().toArray(nodesArray);
        return nodesArray;
    }

}
