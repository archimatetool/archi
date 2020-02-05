/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.editor.ui.LocalClipboard;
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
        return LocalClipboard.getDefault().getContents() == this && !getObjects().isEmpty();
    }
    
    public List<IArchimateModelObject> getObjects() {
        return objects;
    }
    
    public void clear() {
        if(LocalClipboard.getDefault().getContents() == this) {
            LocalClipboard.getDefault().setContents(""); //$NON-NLS-1$
        }
        
        getObjects().clear();
    }
    
    public void setContents(List<IArchimateModelObject> objects) {
        this.objects = objects;
        LocalClipboard.getDefault().setContents(this);
    }
}