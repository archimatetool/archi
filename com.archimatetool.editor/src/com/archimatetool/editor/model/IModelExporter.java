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
 * @deprecated As of 5.7 - Use commands, handlers and menus in plugin.xml.
 *                         For example see https://github.com/archimatetool/archi/tree/master/other/com.archimatetool.importexportexample
 */
public interface IModelExporter {

    void export(IArchimateModel model) throws IOException;

}