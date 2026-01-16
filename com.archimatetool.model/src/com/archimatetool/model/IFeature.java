/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.BasicEMap;

public interface IFeature extends BasicEMap.Entry<String,String> {
    
    String getName();

    void setName(String value);


} // IFeature
