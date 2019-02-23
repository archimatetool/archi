/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.archimatetool.editor.ui.findreplace.AbstractFindReplaceProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.editor.views.tree.commands.RenameCommandHandler;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;




/**
 * Find/Replace Provider for Model Tree Viewer
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewerFindReplaceProvider extends AbstractFindReplaceProvider {
    
    private TreeModelViewer fTreeModelViewer;

    /**
     * If this is true then when user has selected items in the tree and chooses "Replace", these elements
     * are the ones for the replace operation (if they match the criteria).
     * If this is false then the classic "Replace" behaviour is used selecting the next/previous match and then replacing that one.
     */
    boolean replaceSelection = false;
    
    public TreeModelViewerFindReplaceProvider(TreeModelViewer viewer) {
        fTreeModelViewer = viewer;
    }
    
    @Override
    public boolean find(String toFind) {
        // Find All
        if(isAll()) {
            List<INameable> elements = getAllMatchingElements(toFind);
            fTreeModelViewer.setSelection(new StructuredSelection(elements), true);
            return !elements.isEmpty();
        }
        // Find Next/Previous
        else {
            INameable element = findNextElement(getFirstSelectedObject(), toFind);
            if(element != null) {
                fTreeModelViewer.setSelection(new StructuredSelection(element), true);
            }
            return (element != null);
        }
    }
    
    @Override
    public boolean replace(String toFind, String toReplaceWith) {
        // Replace All
        if(isAll()) {
            List<INameable> elements = getAllMatchingElements(toFind);
            if(!elements.isEmpty()) {
                List<String> newNames = new ArrayList<String>();
                
                for(INameable nameable : elements) {
                    String newName = getReplacedString(nameable.getName(), toFind, toReplaceWith);
                    newNames.add(newName);
                }
                
                RenameCommandHandler.doRenameCommands(elements, newNames);
                fTreeModelViewer.setSelection(new StructuredSelection(elements), true);
            }
            return !elements.isEmpty();
        }
        // Replace Next/Previous
        else {
            // Replace on selected elements
            if(replaceSelection) {
                List<Object> selected = getSelectedObjects();
                if(!selected.isEmpty()) {
                    List<INameable> elements = new ArrayList<INameable>();
                    List<String> newNames = new ArrayList<String>();
                    
                    for(Object object : selected) {
                        if(matches(object, toFind)) {
                            INameable nameable = (INameable)object;
                            elements.add(nameable);
                            
                            String newName = getReplacedString(nameable.getName(), toFind, toReplaceWith);
                            newNames.add(newName);
                        }
                    }
                    
                    if(!elements.isEmpty()) {
                        RenameCommandHandler.doRenameCommands(elements, newNames);
                        return true;
                    }
                }
            }
            // Replace on next single selection
            else {
                Object object = getFirstSelectedObject();
                if(matches(object, toFind)) {
                    RenameCommandHandler.doRenameCommand((INameable)object, getReplacedString(((INameable)object).getName(), toFind, toReplaceWith));
                    return true;
                }
            }
            
            // Else move forward
            return find(toFind);
        }
    }
    
    /**
     * Find the next/previous element in the tree that matches the find criteria
     * @param startElement The element to start the search from for next/previous
     * @param toFind The string to find
     * @return The next/previous element if found, or null
     */
    INameable findNextElement(Object startElement, String toFind) {
        // Get *all* elements in the viewer
        List<INameable> elements = getAllNameableElements();
        
        if(elements.isEmpty()) {
            return null;
        }
        
        // Increment for forward/backward
        int increment = isForward() ? 1 : -1;
        
        // Starting index (defaults for a null startElement)
        int startIndex = isForward() ? 0 : elements.size() - 1;
        
        // Find starting point from startElement, if we have one
        if(startElement != null) {
            startIndex = elements.indexOf(startElement) + increment;
        }
        
        // Iterate through all elements forwards or backwards until we find the next matching element
        for(int i = startIndex; isForward() ? (i < elements.size()) : (i >= 0); i += increment) {
            Object element = elements.get(i);
            if(matches(element, toFind)) {
                return (INameable)element;
            }
        }
        
        return null;
    }
    
    /**
     * @param toFind The string to find a match on. If this is null, then collect all elements in the tree viewer.
     * @return All elements in the TreeViewer that match the string, in sorted and filtered order
     *          If a model is in scope use that as the starting/end point
     */
    List<INameable> getAllMatchingElements(String toFind) {
        if(isAllModels()) {
            return getMatchingElements(fTreeModelViewer.getInput(), toFind);
        }
        else {
            return getMatchingElements(getModelInScope(), toFind);
        }
    }
    
    /**
     * @param element The element to start searching from.
     * @param toFind The string to find a match on. If this is null, then collect all elements in the tree viewer.
     * @return A list of all matching elements in the viewer model as sorted and filtered by the TreeViewer model
     */
    private List<INameable> getMatchingElements(Object element, String toFind) {
        List<INameable> list = new ArrayList<INameable>();
        
        if(element != null) {
            if(toFind == null && element instanceof INameable) { // collect all
                list.add((INameable)element);
            }
            else if(matches(element, toFind)) { // collect on match
                list.add((INameable)element);
            }
            
            for(Object object : fTreeModelViewer.getSortedChildren(element)) {
                list.addAll(getMatchingElements(object, toFind));
            }
        }
        
        return list;
    }
    
    /**
     * @return *all* elements in the TreeViewer of type INameable, in sorted and filtered order
     */
    private List<INameable> getAllNameableElements() {
        return getAllMatchingElements(null);
    }
    
    /**
     * @return True if object is a matching type and toFind is found in the object's name
     */
    private boolean matches(Object object, String toFind) {
        if(object instanceof IFolder && !isIncludeFolders()) { // folders not included
            return false;
        }
        
        if(object instanceof IArchimateRelationship && !isIncludeRelations()) { // relations not included
            return false;
        }
        
        return (object instanceof INameable)
                && RenameCommandHandler.canRename(object)
                && StringUtils.isSet(toFind)
                && ((INameable)object).getName() != null
                && ((INameable)object).getName().matches(getSearchStringPattern(toFind));
    }
    
    /**
     * @return The first selected object in the tree, or null
     */
    private Object getFirstSelectedObject() {
        IStructuredSelection selection = (IStructuredSelection)fTreeModelViewer.getSelection();
        return selection.getFirstElement();
    }
    
    @SuppressWarnings("unchecked")
    private List<Object> getSelectedObjects() {
        IStructuredSelection selection = (IStructuredSelection)fTreeModelViewer.getSelection();
        return selection.toList();
    }
    
    /**
     * @return The model in scope of the selected element
     */
    private IArchimateModel getModelInScope() {
        IStructuredSelection selection = (IStructuredSelection)fTreeModelViewer.getSelection();
        Object o = selection.getFirstElement();
        if(o instanceof IArchimateModelObject) {
            return ((IArchimateModelObject)o).getArchimateModel();
        }
        
        return null;
    }
}
