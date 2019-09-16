/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import org.eclipse.emf.common.notify.Notification;

/**
 * Ecore Model content notification listener
 * 
 * @author Phillip Beauvoir
 */
@FunctionalInterface
public interface IModelContentListener {

    /**
     * @param notification
     */
    void notifyChanged(Notification notification);

}