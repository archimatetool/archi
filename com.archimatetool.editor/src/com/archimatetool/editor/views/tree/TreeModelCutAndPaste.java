/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.archimatetool.model.IArchimateModelObject;

/**
 * TreeModel Cut and Paste objects storage
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelCutAndPaste {
    
    public static TreeModelCutAndPaste INSTANCE = new TreeModelCutAndPaste();
    
    private List<IArchimateModelObject> objects = new ArrayList<IArchimateModelObject>();

    private TreeModelCutAndPaste() {
    }
    
    public boolean hasContents() {
        return Clipboard.getDefault().getContents() == this && !getObjects().isEmpty();
    }
    
    public List<IArchimateModelObject> getObjects() {
        return objects;
    }
    
    public void clear() {
        if(Clipboard.getDefault().getContents() == this) {
            Clipboard.getDefault().setContents(""); //$NON-NLS-1$
        }
        
        getObjects().clear();
    }
    
    public void add(IArchimateModelObject object) {
        getObjects().add(object);
    }
    
    public void setContentsToClipboard() {
        Clipboard.getDefault().setContents(this);
    }
}