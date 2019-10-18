/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;


/**
 * Data Source
 * 
 * @author Phillip Beauvoir
 */
public interface IDataSource extends IDataConstants {

    /**
     * @return The current element
     */
    Object getElement();
}
