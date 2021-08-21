/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.viewers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.zest.core.viewers.internal.AbstractStructuredGraphViewer;
import org.eclipse.zest.core.viewers.internal.GraphModelEntityFactory;
import org.eclipse.zest.core.viewers.internal.GraphModelEntityRelationshipFactory;
import org.eclipse.zest.core.viewers.internal.GraphModelFactory;
import org.eclipse.zest.core.viewers.internal.IStylingGraphModelFactory;
import org.eclipse.zest.core.viewers.internal.ZoomManager;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.layouts.LayoutAlgorithm;

/**
 * This view is used to represent a static graph. Static graphs can be layed
 * out, but do not continually update their layout locations.
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GraphViewer extends AbstractStructuredGraphViewer implements
        ISelectionProvider {

    protected Graph graph = null;
    private IStylingGraphModelFactory modelFactory = null;
    private List selectionChangedListeners = null;
    ZoomManager zoomManager = null;

    /**
     * Initializes the viewer.
     * 
     * @param composite
     *            The parent composite.
     * @param style
     *            The style for the viewer and the related Graph.
     * @see SWT#V_SCROLL
     * @see SWT#H_SCROLL
     */
    public GraphViewer(Composite composite, int style) {
        super(style);
        this.graph = new Graph(composite, style);
        hookControl(this.graph);
    }

    public void setControl(Graph graphModel) {
        this.graph = graphModel;
        hookControl(this.graph);
    }

    @Override
    protected void hookControl(Control control) {
        super.hookControl(control);

        selectionChangedListeners = new ArrayList();
        getGraphControl().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Iterator iterator = selectionChangedListeners.iterator();

                ISelection structuredSelection = getSelection();
                SelectionChangedEvent event = new SelectionChangedEvent(
                        GraphViewer.this, structuredSelection);

                while (iterator.hasNext()) {
                    ISelectionChangedListener listener = (ISelectionChangedListener) iterator
                            .next();
                    listener.selectionChanged(event);
                }
                firePostSelectionChanged(event);
            }

        });

        control.addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                DoubleClickEvent doubleClickEvent = new DoubleClickEvent(
                        GraphViewer.this, getSelection());
                fireDoubleClick(doubleClickEvent);
            }

            @Override
            public void mouseDown(MouseEvent e) {

            }

            @Override
            public void mouseUp(MouseEvent e) {

            }

        });
    }

    /**
     * Gets the styles for this structuredViewer
     * 
     * @return
     */
    public int getStyle() {
        return this.graph.getStyle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.viewer.internal.AbstractStructuredGraphViewer#
     * getGraphControl()
     */
    @Override
    public Graph getGraphControl() {
        return super.getGraphControl();
    };

    /**
     * Sets the layout algorithm to use for this viewer.
     * 
     * @param algorithm
     *            the algorithm to layout the nodes
     * @param runLayout
     *            if the layout should be run
     */
    @Override
    public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean runLayout) {
        graph.setLayoutAlgorithm(algorithm, runLayout);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.viewer.internal.AbstractStructuredGraphViewer#
     * setLayoutAlgorithm(org.eclipse.zest.layouts.LayoutAlgorithm)
     */
    @Override
    public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
        super.setLayoutAlgorithm(algorithm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.viewers.AbstractStructuredGraphViewer#setNodeStyle
     * (int)
     */
    @Override
    public void setNodeStyle(int nodeStyle) {
        super.setNodeStyle(nodeStyle);
        this.graph.setNodeStyle(nodeStyle);
    }

    @Override
    public void setContentProvider(IContentProvider contentProvider) {
        if (contentProvider instanceof IGraphContentProvider) {
            modelFactory = null;
            super.setContentProvider(contentProvider);
        } else if (contentProvider instanceof IGraphEntityContentProvider) {
            modelFactory = null;
            super.setContentProvider(contentProvider);
        } else if (contentProvider instanceof IGraphEntityRelationshipContentProvider) {
            modelFactory = null;
            super.setContentProvider(contentProvider);
        } else {
            throw new IllegalArgumentException(
                    "Invalid content provider, only IGraphContentProvider, IGraphEntityContentProvider, or IGraphEntityRelationshipContentProvider are supported."); //$NON-NLS-1$
        }
    }

    /**
     * Finds the graph widget item for a given user model item.
     * 
     * Note: This method returns an internal interface (GraphItem). You should
     * be able to cast this to either a IGraphModelNode or IGraphModelConnection
     * (which are also internal). These are internal because this API is not
     * stable. If use this method (to access internal nodes and edges), your
     * code may not compile between versions.
     * 
     * @param The
     *            user model node.
     * @return An IGraphItem. This should be either a IGraphModelNode or
     *         IGraphModelConnection
     */
    public GraphItem findGraphItem(Object element) {
        Widget[] result = findItems(element);
        return (result.length == 0 || !(result[0] instanceof GraphItem)) ? null
                : (GraphItem) result[0];
    }

    /**
     * Applys the current layout to the viewer
     */
    @Override
    public void applyLayout() {
        graph.applyLayout();
    }

    @Override
    protected void setSelectionToWidget(List l, boolean reveal) {
        GraphItem[] listOfItems = findItems(l);
        graph.setSelection(listOfItems);
    }

    @Override
    public Control getControl() {
        return graph;
    }

    @Override
    public Object[] getNodeElements() {
        return super.getNodeElements();
    }

    @Override
    public Object[] getConnectionElements() {
        return super.getConnectionElements();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.viewer.internal.AbstractStructuredGraphViewer#reveal
     * (java.lang.Object)
     */
    @Override
    public void reveal(Object element) {
        super.reveal(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.viewer.internal.AbstractStructuredGraphViewer#
     * setConnectionStyle(int)
     */
    @Override
    public void setConnectionStyle(int connectionStyle) {
        super.setConnectionStyle(connectionStyle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.viewer.internal.AbstractStructuredGraphViewer#unReveal
     * (java.lang.Object)
     */
    @Override
    public void unReveal(Object element) {
        super.unReveal(element);
    }

    @Override
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (!selectionChangedListeners.contains(listener)) {
            selectionChangedListeners.add(listener);
        }
    }

    @Override
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        if (selectionChangedListeners.contains(listener)) {
            selectionChangedListeners.remove(listener);
        }
    }

    // @tag zest.bug.156286-Zooming.fix.experimental : expose the zoom manager
    // for new actions.
    @Override
    protected ZoomManager getZoomManager() {
        if (zoomManager == null) {
            zoomManager = new ZoomManager(getGraphControl().getRootLayer(),
                    getGraphControl().getViewport());
        }
        return zoomManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.viewers.AbstractStructuredGraphViewer#getFactory()
     */
    @Override
    protected IStylingGraphModelFactory getFactory() {
        if (modelFactory == null) {
            if (getContentProvider() instanceof IGraphContentProvider) {
                modelFactory = new GraphModelFactory(this);
            } else if (getContentProvider() instanceof IGraphEntityContentProvider) {
                modelFactory = new GraphModelEntityFactory(this);
            } else if (getContentProvider() instanceof IGraphEntityRelationshipContentProvider) {
                modelFactory = new GraphModelEntityRelationshipFactory(this);
            }
        }
        return modelFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.viewers.AbstractStructuredGraphViewer#
     * getLayoutAlgorithm()
     */
    @Override
    protected LayoutAlgorithm getLayoutAlgorithm() {
        return graph.getLayoutAlgorithm();
    }

}
