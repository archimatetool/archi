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
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IFolderContainer;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IProperties;
import uk.ac.bolton.archimate.model.IProperty;

/**
 * Search Filter
 * 
 * @author Phillip Beauvoir
 */
public class SearchFilter extends ViewerFilter {
    private TreeViewer fViewer;
    private String fSearchText = "";
    private TreePath[] fExpanded;
    
    private boolean fFilterName;
    private boolean fFilterDocumentation;
    
    private List<EClass> fObjectFilter = new ArrayList<EClass>();
    private List<String> fPropertiesFilter = new ArrayList<String>();
    
    private boolean fShowAllFolders = false;
    
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
    
    public void clear() {
        if(isFiltering()) {
            restoreState();
        }
        fSearchText = "";
        reset();
    }
    
    public void resetFilters() {
    	reset();
    	refresh();
    }
    
    private void reset() {
    	fFilterName = false;
    	fFilterDocumentation = false;
    	fObjectFilter.clear();
    	fPropertiesFilter.clear();
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
    	if(!isFiltering()) {
    		return true;
    	}
    	
    	return checkFilter(element);
    }
    
    private boolean checkFilter(Object element) {
    	if(element instanceof IFolderContainer) {
    		for(IFolder folder : ((IFolderContainer)element).getFolders()) {
				if(checkFilter(folder)) {
					return true;
				}
			}
    	}
    	
    	if(element instanceof IFolder) {
    		for(Object o : ((IFolder)element).getElements()) {
				if(checkFilter(o)) {
					return true;
				}
			}
    		
    		if(fShowAllFolders) {
    			return true;
    		}
    	}
    	
    	// EObject Type filter - do this first as the master filter
    	if(isObjectFiltered(element)) {
    		return false;
    	}
    	
    	boolean textSearchResult = false;
    	boolean propertyKeyResult = false;
    	
    	// Properties Key filter
    	if(isFilteringPropertyKeys() && element instanceof IProperties) {
    		for(IProperty property : ((IProperties)element).getProperties()) {
    			if(fPropertiesFilter.contains(property.getKey())) {
    				propertyKeyResult = true;
    				if(hasSearchText() && property.getValue().toLowerCase().contains(fSearchText.toLowerCase())) {
    					textSearchResult = true;
    				}
    			}
    		}
    	}
    	
    	// If has search Text and no text found yet
    	if(hasSearchText()) {
        	// Name...
        	if(fFilterName && !textSearchResult && element instanceof INameable) {
        		String name = StringUtils.safeString(((INameable)element).getName());
        		if(name.toLowerCase().contains(fSearchText.toLowerCase())) {
        			textSearchResult = true;
        		}
        	}

        	// Then Documentation
        	if(fFilterDocumentation && !textSearchResult && element instanceof IDocumentable) {
        		String text = StringUtils.safeString(((IDocumentable)element).getDocumentation());
        		if(text.toLowerCase().contains(fSearchText.toLowerCase())) {
        			textSearchResult = true;
        		}
        	}
    	}

    	if((hasSearchText())) {
    		return textSearchResult;
    	}
    	
    	if(isFilteringPropertyKeys()) {
    		return propertyKeyResult;
    	}
    	
    	return !isObjectFiltered(element);
    }
    
    private boolean isObjectFiltered(Object element) {
    	return !fObjectFilter.isEmpty() && !fObjectFilter.contains(((EObject)element).eClass());
    }
    
    private boolean isFiltering() {
        return hasSearchText() || !fObjectFilter.isEmpty() || !fPropertiesFilter.isEmpty();
    }
    
    private boolean hasSearchText() {
    	return fSearchText.length() > 0;
    }
    
    private boolean isFilteringPropertyKeys() {
    	return !fPropertiesFilter.isEmpty();
    }
    
    public void setFilterOnName(boolean set) {
    	if(fFilterName != set) {
    		fFilterName = set;
    		if(isFiltering()) {
                refresh();
            }
    	}
    }
    
    public void setFilterOnDocumentation(boolean set) {
    	if(fFilterDocumentation != set) {
    		fFilterDocumentation = set;
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
    
    public void addPropertiesFilter(String key) {
        // Fresh filter
        if(!isFiltering()) {
            saveState();
        }
        fPropertiesFilter.add(key);
        refresh();
    }
    
    public void removePropertiesFilter(String key) {
    	fPropertiesFilter.remove(key);
        refresh();
    }
    
    public void setShowAllFolders(boolean set) {
    	if(set != fShowAllFolders) {
    		fShowAllFolders = set;
    		refresh();
    	}
    }
    
    void saveState() {
        fExpanded = fViewer.getExpandedTreePaths();
    }
    
    void restoreState() {
        IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection(); // first
        if(fExpanded != null) {
            fViewer.setExpandedTreePaths(fExpanded);
        }
        fViewer.setSelection(selection, true);
    }
}