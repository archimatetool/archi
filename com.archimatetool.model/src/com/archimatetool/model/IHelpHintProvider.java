/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;


/**
 * Hint Provider for general hint providers
 * 
 * @author Phillip Beauvoir
 */
public interface IHelpHintProvider {

    String getHintTitle();
    
    String getHintContent();
}
