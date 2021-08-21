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
 * A simple CreationFactory that takes a Class in the constructor and creates a
 * new instance of this Class in {@link #getNewObject()}.
 * 
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public class SimpleFactory implements CreationFactory {

    private Class type;

    /**
     * Creates a SimpleFactory.
     * 
     * @param aClass
     *            The class to be instantiated using this factory.
     */
    public SimpleFactory(Class aClass) {
        type = aClass;
    }

    /**
     * Create the new object.
     * 
     * @return The newly created object.
     */
    @Override
    public Object getNewObject() {
        try {
            return type.newInstance();
        } catch (Exception exc) {
            return null;
        }
    }

    /**
     * Returns the type of object this factory creates.
     * 
     * @return The type of object this factory creates.
     */
    @Override
    public Object getObjectType() {
        return type;
    }

}
