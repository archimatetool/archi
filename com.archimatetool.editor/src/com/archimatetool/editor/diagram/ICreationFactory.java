/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.ui.IEditorPart;


/**
 * Extends CreationFactory
 * 
 * @author Phillip Beauvoir
 */
public interface ICreationFactory extends CreationFactory {

    /**
     * Return true if this Creation Factory is used for a certain type of editor.
     * We need to know this when dragging an entry from the palette to a different editor.
     */
    boolean isUsedFor(IEditorPart editor);

}
