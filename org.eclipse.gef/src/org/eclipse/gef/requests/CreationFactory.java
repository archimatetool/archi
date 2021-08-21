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
package org.eclipse.gef.requests;

/**
 * A factory used to create new objects.
 * 
 * @author hudsonr
 * @since 2.1
 */
public interface CreationFactory {

    /**
     * Returns the new object.
     * 
     * @return the new object
     */
    Object getNewObject();

    /**
     * Returns the new object's type.
     * 
     * @return the type
     */
    Object getObjectType();

}
