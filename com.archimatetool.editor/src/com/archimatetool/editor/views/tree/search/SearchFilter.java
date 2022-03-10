/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.search;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Search Filter
 * 
 * @author Phillip Beauvoir
 */
public class SearchFilter extends ViewerFilter {
    private TreeViewer fViewer;
    private String fSearchText = ""; //$NON-NLS-1$
    private Object[] fExpanded;

    private boolean fFilterName;
    private boolean fFilterDocumentation;

    private Set<EClass> fConceptsFilter = new HashSet<>();
    private Set<String> fPropertiesFilter = new HashSet<>();
    private Set<IProfile> fSpecializationsFilter = new HashSet<>();

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
        Display.getCurrent().asyncExec(() -> {
            try {
                fViewer.getTree().setRedraw(false);

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
        
        fConceptsFilter.clear();
        fPropertiesFilter.clear();
        fSpecializationsFilter.clear();
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if(!isFiltering()) {
            return true;
        }

        return isElementVisible(element);
    }

    /**
     * Query whether element is to be shown (or any children) when filtering
     * This will also query child elements of element if it's a container
     * @param element Any element including containers
     */
    private boolean isElementVisible(Object element) {
        if(element instanceof IFolderContainer) {
            for(IFolder folder : ((IFolderContainer)element).getFolders()) {
                if(isElementVisible(folder)) {
                    return true;
                }
            }
        }

        if(element instanceof IFolder) {
            for(Object o : ((IFolder)element).getElements()) {
                if(isElementVisible(o)) {
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
     * @return true if the element should be shown
     */
    public boolean matchesFilter(Object element) {
        boolean show = true;
        
        // Concept or Specialization
        if(isFilteringConcepts() || isFilteringSpecializations()) {
            show &= shouldShowConcept(element) || shouldShowSpecialization(element);
        }
        
        // Name or Documentation or Property
        if(isFilteringName() || isFilteringDocumentation() || isFilteringPropertyKeys()) {
            show &= (isFilteringName() && shouldShowObjectWithName(element))
                    || (isFilteringDocumentation() && shouldShowObjectWithDocumentation(element))
                    || (isFilteringPropertyKeys() && shouldShowProperty(element));
        }
        
        return show;
    }
    
    private boolean shouldShowConcept(Object element) {
        return fConceptsFilter.contains(((EObject)element).eClass());
    }

    private boolean shouldShowObjectWithName(Object element) {
        if(element instanceof INameable) {
            String name = StringUtils.safeString(((INameable)element).getName());

            // Normalise in case of multi-line text
            name = StringUtils.normaliseNewLineCharacters(name);

            return name.toLowerCase().contains(fSearchText.toLowerCase());
        }
        
        return false;
    }

    private boolean shouldShowObjectWithDocumentation(Object element) {
        if(element instanceof IDocumentable) {
            String text = StringUtils.safeString(((IDocumentable)element).getDocumentation());
            return text.toLowerCase().contains(fSearchText.toLowerCase());
        }
        
        return false;
    }

    private boolean shouldShowSpecialization(Object element) {
        if(element instanceof IArchimateConcept) {
            for(IProfile profile : ((IArchimateConcept)element).getProfiles()) {
                for(IProfile p : fSpecializationsFilter) {
                    // Could be marching Profile name/class in different models
                    if(ArchimateModelUtils.isMatchingProfile(p, profile)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean shouldShowProperty(Object element) {
        if(element instanceof IProperties) {
            for(IProperty property : ((IProperties)element).getProperties()) {
                if(fPropertiesFilter.contains(property.getKey())) {
                    return hasSearchText() ? property.getValue().toLowerCase().contains(fSearchText.toLowerCase()) : true;
                }
            }
        }

        return false;
    }

    public boolean isFiltering() {
        return isFilteringName() || isFilteringDocumentation() || isFilteringConcepts() || isFilteringPropertyKeys() || isFilteringSpecializations();
    }

    private boolean isFilteringName() {
        return fFilterName && hasSearchText();
    }
    
    private boolean isFilteringDocumentation() {
        return fFilterDocumentation && hasSearchText();
    }
    
    private boolean isFilteringConcepts() {
        return !fConceptsFilter.isEmpty();
    }
    
    private boolean isFilteringPropertyKeys() {
        return !fPropertiesFilter.isEmpty();
    }

    private boolean isFilteringSpecializations() {
        return !fSpecializationsFilter.isEmpty();
    }

    private boolean hasSearchText() {
        return fSearchText.length() > 0;
    }

    void setFilterOnName(boolean set, boolean doRefresh) {
        if(fFilterName != set) {
            fFilterName = set;
            
            if(doRefresh) {
                refresh();
            }
        }
    }

    void setFilterOnDocumentation(boolean set, boolean doRefresh) {
        if(fFilterDocumentation != set) {
            fFilterDocumentation = set;
            
            if(doRefresh) {
                refresh();
            }
        }
    }

    void addConceptFilter(EClass eClass) {
        // Fresh filter
        if(!isFiltering()) {
            saveState();
        }
        
        fConceptsFilter.add(eClass);
        refresh();
    }

    void removeConceptFilter(EClass eClass) {
        fConceptsFilter.remove(eClass);
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
    
    void resetPropertiesFilter() {
        fPropertiesFilter.clear();
        refresh();
    }

    void addSpecializationsFilter(IProfile profile) {
        // Fresh filter
        if(!isFiltering()) {
            saveState();
        }
        
        fSpecializationsFilter.add(profile);
        refresh();
    }

    void removeSpecializationsFilter(IProfile profile) {
        fSpecializationsFilter.remove(profile);
        refresh();
    }
    
    void resetSpecializationsFilter() {
        fSpecializationsFilter.clear();
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
        fExpanded = fViewer.getExpandedElements();
    }

    void restoreState() {
        IStructuredSelection selection = (IStructuredSelection)fViewer.getSelection(); // first
        
        if(fExpanded != null) {
            fViewer.setExpandedElements(fExpanded);
        }
        
        fViewer.setSelection(selection, true);
    }
}