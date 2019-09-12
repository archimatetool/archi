/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.search;

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

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;


/**
 * Search Filter
 * 
 * @author Phillip Beauvoir
 */
public class SearchFilter extends ViewerFilter {
    private TreeViewer fViewer;
    private String fSearchText = ""; //$NON-NLS-1$
    private TreePath[] fExpanded;

    private boolean fFilterName;
    private boolean fFilterDocumentation;

    private List<EClass> fObjectFilter = new ArrayList<EClass>();
    private List<String> fPropertiesFilter = new ArrayList<String>();

    private boolean fShowAllFolders = false;

    public SearchFilter(TreeViewer viewer) {
        fViewer = viewer;
    }

    void setSearchText(String text) {
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

                try {
                    // If we do this first fViewer.refresh() is then faster
                    if(!isFiltering()) {
                        restoreState();
                    }
                    
                    // This has to be called before expandAll
                    fViewer.refresh();
                    
                    // If we have something to show expand all nodes
                    if(isFiltering()) {
                        fViewer.expandAll();
                    }
                    else {
                        restoreState(); // Yes, do call this again.
                    }
                }
                finally {
                    fViewer.getTree().setRedraw(true);
                }
            }
        });
    }

    public void clear() {
        if(isFiltering()) {
            restoreState();
        }
        fSearchText = ""; //$NON-NLS-1$
        resetFilters();
        fExpanded = null;
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

        return isElementVisible(parentElement, element);
    }

    /**
     * Query whether element is to be shown (or any children) when filtering
     * This will also query child elements of element if it's a container
     * @param element Any element including containers
     * @return
     */
    private boolean isElementVisible(Object parentElement, Object element) {
        if(element instanceof IFolderContainer) {
            for(IFolder folder : ((IFolderContainer)element).getFolders()) {
                if(isElementVisible(parentElement, folder)) {
                    return true;
                }
            }
        }

        if(element instanceof IFolder) {
            for(Object o : ((IFolder)element).getElements()) {
                if(isElementVisible(parentElement, o)) {
                    return true;
                }
            }

            if(isShowAllFolders()) {
                return true;
            }
        }
        
        return matchesFilter(element);
    }
    
    /**
     * Query whether element matches filter criteria when filtering on node/leaf elements
     * @param element Any element, children will not be queried.
     * @return
     */
    public boolean matchesFilter(Object element) {
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
                
                // Normalise in case of multi-line text
                name = StringUtils.normaliseNewLineCharacters(name);
                
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

    public boolean isFiltering() {
        return hasSearchText() || !fObjectFilter.isEmpty() || !fPropertiesFilter.isEmpty();
    }

    private boolean hasSearchText() {
        return fSearchText.length() > 0;
    }

    private boolean isFilteringPropertyKeys() {
        return !fPropertiesFilter.isEmpty();
    }

    void setFilterOnName(boolean set) {
        if(fFilterName != set) {
            fFilterName = set;
            if(isFiltering()) {
                refresh();
            }
        }
    }

    void setFilterOnDocumentation(boolean set) {
        if(fFilterDocumentation != set) {
            fFilterDocumentation = set;
            if(isFiltering()) {
                refresh();
            }
        }
    }

    void addObjectFilter(EClass eClass) {
        // Fresh filter
        if(!isFiltering()) {
            saveState();
        }
        fObjectFilter.add(eClass);
        refresh();
    }

    void removeObjectFilter(EClass eClass) {
        fObjectFilter.remove(eClass);
        refresh();
    }

    void addPropertiesFilter(String key) {
        // Fresh filter
        if(!isFiltering()) {
            saveState();
        }
        fPropertiesFilter.add(key);
        refresh();
    }

    void removePropertiesFilter(String key) {
        fPropertiesFilter.remove(key);
        refresh();
    }

    void setShowAllFolders(boolean set) {
        if(set != fShowAllFolders) {
            fShowAllFolders = set;
            refresh();
        }
    }
    
    boolean isShowAllFolders() {
        return fShowAllFolders;
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