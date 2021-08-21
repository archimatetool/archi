/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * Provides a mechanism to style nodes and edges when they are created. 
 * 
 * After each node or edge is created, the self styling method will be called with both 
 * the element and the widget. 
 */
public interface ISelfStyleProvider {

    /**
     * Styles a connection
     */
    public void selfStyleConnection(Object element, GraphConnection connection);

    /**
     * Styles a node
     */
    public void selfStyleNode(Object element, GraphNode node);

}
