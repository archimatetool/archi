/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.bolton.archimate.editor.model.commands.NonNotifyingCompoundCommand;
import uk.ac.bolton.archimate.editor.views.tree.TreeModelView;

/**
 * This Compound Command stores an element so that when elements are deleted the tree node can be re-selected.
 * It turns off refresh in the Tree, saving on slow redraws.
 * 
 * @author Phillip Beauvoir
 */
public class DeleteElementsCompoundCommand extends NonNotifyingCompoundCommand {

    // Select this one
    private Object fObjectToSelect;

    public DeleteElementsCompoundCommand(List<?> selected) {
        // Find previous sibling object to reselect
        Object o = selected.get(0);
        
        fObjectToSelect = findPreviousSiblingObject(o);
        
        // Was null so select parent
        if(fObjectToSelect == null && o instanceof EObject) {
            fObjectToSelect = ((EObject)o).eContainer();
        }
    }

    @Override
    public String getLabel() {
        return getCommands().size() > 1 ? "Delete Elements" : super.getLabel();
    }

    @Override
    public void execute() {
        super.execute();

        // Select object
        if(fObjectToSelect != null && TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fObjectToSelect), true);
        }
    }
    
    /**
     * Find the previous sibling object
     * @param element the element to to find the sibling of
     * @return the previous sibling object or null if not found
     */
    private Object findPreviousSiblingObject(Object element) {
        if(TreeModelView.INSTANCE == null) {
            return null;
        }
        
        TreeItem item = TreeModelView.INSTANCE.getViewer().findTreeItem(element);
        if(item == null) {
            return null;
        }
        
        TreeItem parentTreeItem = item.getParentItem();
        
        // Parent Item not found so must be at top level
        if(parentTreeItem == null) {
            Tree tree = item.getParent();
            int index = tree.indexOf(item);
            if(index < 1) { // At root or not found
                return null;
            }
            return tree.getItem(index - 1).getData();
        }

        // Item not found
        int index = parentTreeItem.indexOf(item);
        if(index < 1) {
            return null;
        }

        // Return Previous item
        return parentTreeItem.getItem(index - 1).getData();
    }


    @Override
    public void dispose() {
        super.dispose();
        fObjectToSelect = null;
    }
}