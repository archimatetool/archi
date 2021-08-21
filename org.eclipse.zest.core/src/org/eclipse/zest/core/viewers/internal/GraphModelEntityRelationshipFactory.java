/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.zest.core.viewers.IGraphEntityRelationshipContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * A factory for the IGraphEntityRelationshipContentProvider.
 * 
 * @author Del Myers
 */
// @tag bug.154580-Content.fix
// @tag bug.160367-Refreshing.fix : updated to use new
// AbstractStylingModelFactory
@SuppressWarnings({"rawtypes", "unchecked"})
public class GraphModelEntityRelationshipFactory extends
        AbstractStylingModelFactory {

    public GraphModelEntityRelationshipFactory(
            AbstractStructuredGraphViewer viewer) {
        super(viewer);
        if (!(viewer.getContentProvider() instanceof IGraphEntityRelationshipContentProvider)) {
            throw new IllegalArgumentException(
                    "Expected IGraphEntityRelationshipContentProvider"); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.AbstractStylingModelFactory
     * #createGraphModel()
     */
    @Override
    public Graph createGraphModel(Graph model) {
        doBuildGraph(model);
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.AbstractStylingModelFactory
     * #doBuildGraph(org.eclipse.zest.core.internal.graphmodel.GraphModel)
     */
    @Override
    protected void doBuildGraph(Graph model) {
        super.doBuildGraph(model);
        Object[] nodes = getContentProvider().getElements(
                getViewer().getInput());
        nodes = filter(getViewer().getInput(), nodes);
        createModelNodes(model, nodes);
        createModelRelationships(model);
    }

    /**
     * Creates all the model relationships. Assumes that all of the model nodes
     * have been created in the graph model already. Runtime O(n^2) + O(r).
     * 
     * @param model
     *            the model to create the relationship on.
     */
    private void createModelRelationships(Graph model) {
        GraphNode[] modelNodes = getNodesArray(model);
        List listOfNodes = new ArrayList();
        for (int i = 0; i < modelNodes.length; i++) {
            listOfNodes.add(modelNodes[i]);
        }

        for (int i = 0; i < listOfNodes.size(); i++) {
            GraphNode node = (GraphNode) listOfNodes.get(i);
            if (node instanceof GraphContainer) {
                List childNodes = ((GraphContainer) node).getNodes();
                listOfNodes.addAll(childNodes);
            }
        }
        modelNodes = (GraphNode[]) listOfNodes
                .toArray(new GraphNode[listOfNodes.size()]);

        IGraphEntityRelationshipContentProvider content = getCastedContent();
        for (int i = 0; i < modelNodes.length; i++) {
            for (int j = 0; j < modelNodes.length; j++) {
                Object[] rels = content.getRelationships(
                        modelNodes[i].getData(), modelNodes[j].getData());
                if (rels != null) {
                    rels = filter(getViewer().getInput(), rels);
                    for (int r = 0; r < rels.length; r++) {
                        createConnection(model, rels[r],
                                modelNodes[i].getData(),
                                modelNodes[j].getData());
                    }
                }
            }
        }
    }

    /**
     * Creates the model nodes for the given external nodes.
     * 
     * @param model
     *            the graph model.
     * @param nodes
     *            the external nodes.
     */
    private void createModelNodes(Graph model, Object[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            createNode(model, nodes[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh
     * (org.eclipse.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
     */
    @Override
    public void refresh(Graph graph, Object element) {
        refresh(graph, element, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh
     * (org.eclipse.zest.core.internal.graphmodel.GraphModel, java.lang.Object,
     * boolean)
     */
    @Override
    public void refresh(Graph graph, Object element, boolean updateLabels) {
        // with this kind of graph, it is just as easy and cost-effective to
        // rebuild the whole thing.
        refreshGraph(graph);
    }

    private IGraphEntityRelationshipContentProvider getCastedContent() {
        return (IGraphEntityRelationshipContentProvider) getContentProvider();
    }

}
