/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.io.IOException;

import com.archimatetool.model.IArchimateModel;

/**
 * Model Importer interface
 * 
 * @author Phillip Beauvoir
 * @deprecated As of 5.7 - Use commands, handlers and menus in plugin.xml.
 *                         For example see https://github.com/archimatetool/archi/tree/master/other/com.archimatetool.importexportexample
 */
public interface ISelectedModelImporter {

    /**
     * Notify the client that the Import action has been invoked.
     * Clients will import model data from an external source *into* the selected model.
     * @param model The selected model to import into. This might already exist or will need to be created by the client.
     * @throws IOException
     */
    void doImport(IArchimateModel model) throws IOException;
}