/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.io.IOException;

import com.archimatetool.model.IArchimateModel;


/**
 * Exporter interface
 * 
 * @author Phillip Beauvoir
 */
public interface IModelExporter {

    void export(IArchimateModel model) throws IOException;

}