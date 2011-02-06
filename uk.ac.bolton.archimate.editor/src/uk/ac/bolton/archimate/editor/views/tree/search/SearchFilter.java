/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.INameable;

/**
 * Search Filter
 * 
 * @author Phillip Beauvoir
 */
public class SearchFilter extends ViewerFilter {
    public static final int FILTER_NAME = 1;
    public static final int FILTER_DOC = 1 << 1;
    
    private TreeViewer fViewer;
    private String fSearchText = "";
    private TreePath[] fExpanded;
    
    private int fFilterFlags;
    private List<EClass> fObjectFilter = new ArrayList<EClass>();
    
    public SearchFilter(TreeViewer viewer) {
        fViewer = viewer;
    }
    
    public void setSearchText(String text) {
        // Fresh text, so store expanded state
        if(!isFiltering() && text.length() > 0) {
            saveState();
        }
        
        fSearchText = text;
        refresh();
    }
    
    private void refresh() {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                fViewer.getTree().setRedraw(false);
                
                fViewer.refresh();
                
                // Something to show
                if(isFiltering()) {
                    fViewer.expandAll();
                }
                else {
                    restoreState();
                }

                fViewer.getTree().setRedraw(true);
            }
        });
    }
    
    public void reset() {
        if(isFiltering()) {
            restoreState();
        }
        fSearchText = "";
        fObjectFilter.clear();
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if(!isFiltering()) {
            return true;
        }
        
        if(element instanceof IArchimateElement || element instanceof IDiagramModel) {
            // Object filter (only if filter list contains something)
            if(!fObjectFilter.isEmpty() && !fObjectFilter.contains(((EObject)element).eClass())) {
                return false;
            }
            
            if(fFilterFlags == 0) {
                return true;
            }
            
            // Name
            if((fFilterFlags & FILTER_NAME) != 0) {
                String name = StringUtils.safeString(((INameable)element).getName());
                if(name.toLowerCase().contains(fSearchText.toLowerCase())) {
                    return true;
                }
            }
            
            // Documentation
            if((fFilterFlags & FILTER_DOC) != 0) {
                String text = StringUtils.safeString(((IDocumentable)element).getDocumentation());
                if(text.toLowerCase().contains(fSearchText.toLowerCase())) {
                    return true;
                }
            }
            
            return false;
        }
        
        return true;
    }
    
    public boolean isFiltering() {
        return fSearchText.length() > 0 || !fObjectFilter.isEmpty();
    }
    
    public void setFilterFlags(int flags) {
        if(fFilterFlags != flags) {
            fFilterFlags = flags;
            if(isFiltering()) {
                refresh();
            }
        }
    }
    
    public void addObjectFilter(EClass eClass) {
        // Fresh filter
        if(!isFiltering()) {
            saveState();
        }
        fObjectFilter.add(eClass);
        refresh();
    }
    
    public void removeObjectFilter(EClass eClass) {
        fObjectFilter.remove(eClass);
        refresh();
    }
    
    public void clearObjectFilter() {
        if(!fObjectFilter.isEmpty()) {
            fObjectFilter.clear();
            refresh();
        }
    }

    private void saveState() {
        fExpanded = fViewer.getExpandedTreePaths();
    }
    
    private void restoreState() {
        IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection(); // first
        if(fExpanded != null) {
            fViewer.setExpandedTreePaths(fExpanded);
        }
        fViewer.setSelection(selection, true);
    }
}