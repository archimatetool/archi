/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.ArchiPlugin;



/**
 * Title Area Dilaog with added goodness
 * 
 * @author Phillip Beauvoir
 */
public class ExtendedTitleAreaDialog extends TitleAreaDialog {
    
    private String fId;

    public ExtendedTitleAreaDialog(Shell parentShell, String id) {
        super(parentShell);
        fId = id;
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        IDialogSettings settings = ArchiPlugin.INSTANCE.getDialogSettings();
        IDialogSettings section = settings.getSection(fId);
        if(section == null) {
            section = settings.addNewSection(fId);
            Point pt = getDefaultDialogSize();
            if(pt != null) {
                section.put("DIALOG_WIDTH", pt.x); //$NON-NLS-1$
                section.put("DIALOG_HEIGHT", pt.y); //$NON-NLS-1$
            }
        }
        return section;
    }
    
    /**
     * @return A default width and height or null if none
     */
    protected Point getDefaultDialogSize() {
        return null;
    }
}
