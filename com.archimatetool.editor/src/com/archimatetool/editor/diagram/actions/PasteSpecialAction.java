/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ui.IWorkbenchPart;
import com.archimatetool.editor.actions.ArchiActionFactory;


/**
 * Paste Special Action
 * 
 * @author Phillip Beauvoir
 */
public class PasteSpecialAction extends PasteAction {
    
    public PasteSpecialAction(IWorkbenchPart part, GraphicalViewer viewer) {
        super(part, viewer);

        setText(Messages.PasteSpecialAction_0);
        setId(ArchiActionFactory.PASTE_SPECIAL.getId());
        setPasteSpecial(true);
    }
}
