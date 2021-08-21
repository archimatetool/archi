/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * A content provider that is node-relationship centric. Call-backs return model
 * nodes to the user, and ask for relationships. Both nodes and relationships are
 * represented by the user's model.
 * @author Del Myers
 *
 */
//@tag bug.154580-Content.fix : new content provider that returns relationships for the given source and destination.
public interface IGraphEntityRelationshipContentProvider extends IStructuredContentProvider {
    /**
     * Gets the relationships between the given source and destination nodes.
     * @param source the source node.
     * @param dest the destination node.
     * @return objects represtenting the different relationships between the nodes.
     */
    public Object[] getRelationships(Object source, Object dest);

}
