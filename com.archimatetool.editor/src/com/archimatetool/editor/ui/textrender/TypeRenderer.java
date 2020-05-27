/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateModelObject;

/**
 * Type renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TypeRenderer extends AbstractTextRenderer {
    
    private static final String TYPE = "${type}";

    @Override
    public String render(IArchimateModelObject object, String text) {
        return text.replace(TYPE, ArchiLabelProvider.INSTANCE.getDefaultName(getActualObject(object).eClass()));
    }
}