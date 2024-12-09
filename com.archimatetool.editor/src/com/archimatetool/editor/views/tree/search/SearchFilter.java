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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModel;
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
@SuppressWarnings("nls")
public class SearchFilter extends ViewerFilter {
    private String fSearchText = "";

    private boolean fFilterName = true; // default to true
    private boolean fFilterDocumentation;

    private Set<EClass> fConceptsFilter = new HashSet<>();
    private Set<String> fPropertiesFilter = new HashSet<>();
    private Set<IProfile> fSpecializationsFilter = new HashSet<>();
    private boolean fFilterViews = false;

    private boolean fShowAllFolders = false;

    SearchFilter() {
    }

    void setSearchText(String text) {
        fSearchText = text;
    }

    void reset() {
        setFilterOnName(true);
        setFilterOnDocumentation(false);
        
        resetConceptsFilter();
        resetPropertiesFilter();
        resetSpecializationsFilter();
        setFilterViews(false);
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
        if(element instanceof IFolderContainer container) {
            for(IFolder folder : container.getFolders()) {
                if(isElementVisible(folder)) {
                    return true;
                }
            }
        }

        if(element instanceof IFolder folder) {
            for(Object o : folder.getElements()) {
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
     * @return true if the element matches the filter
     */
    public boolean matchesFilter(Object element) {
        boolean show = true;
        
        // Concepts, Specializations or View
        if(isFilteringConcepts() || isFilteringSpecializations() || isFilteringViews()) {
            show = (isFilteringConcepts() && shouldShowConcept(element))
                    || (isFilteringSpecializations() && shouldShowSpecialization(element))
                    || (isFilteringViews() && element instanceof IDiagramModel);
        }
        
        // Name, Documentation or Property
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
        if(element instanceof INameable nameable) {
            String name = StringUtils.safeString(nameable.getName());

            // Normalise in case of multi-line text
            name = StringUtils.normaliseNewLineCharacters(name);

            return name.toLowerCase().contains(fSearchText.toLowerCase());
        }
        
        return false;
    }

    private boolean shouldShowObjectWithDocumentation(Object element) {
        if(element instanceof IDocumentable documentable) {
            String text = StringUtils.safeString(documentable.getDocumentation());
            return text.toLowerCase().contains(fSearchText.toLowerCase());
        }
        
        return false;
    }

    private boolean shouldShowSpecialization(Object element) {
        if(element instanceof IArchimateConcept concept) {
            for(IProfile profile : concept.getProfiles()) {
                for(IProfile p : fSpecializationsFilter) {
                    // Could be matching Profile name/class in different models
                    if(ArchimateModelUtils.isMatchingProfile(p, profile)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean shouldShowProperty(Object element) {
        if(element instanceof IProperties properties) {
            for(IProperty property : properties.getProperties()) {
                if(fPropertiesFilter.contains(property.getKey())) {
                    return hasSearchText() ? property.getValue().toLowerCase().contains(fSearchText.toLowerCase()) : true;
                }
            }
        }

        return false;
    }

    public boolean isFiltering() {
        return isFilteringName() || isFilteringDocumentation() || isFilteringConcepts() || isFilteringPropertyKeys() || isFilteringSpecializations() || isFilteringViews();
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

    void setFilterOnName(boolean set) {
        fFilterName = set;
    }

    void setFilterOnDocumentation(boolean set) {
        fFilterDocumentation = set;
    }

    void addConceptFilter(EClass eClass) {
        fConceptsFilter.add(eClass);
    }

    void removeConceptFilter(EClass eClass) {
        fConceptsFilter.remove(eClass);
    }

    void resetConceptsFilter() {
        fConceptsFilter.clear();
    }

    void addPropertiesFilter(String key) {
        fPropertiesFilter.add(key);
    }

    void removePropertiesFilter(String key) {
        fPropertiesFilter.remove(key);
    }
    
    Set<String> getPropertiesFilter() {
        return fPropertiesFilter;
    }
    
    void resetPropertiesFilter() {
        fPropertiesFilter.clear();
    }

    void addSpecializationsFilter(IProfile profile) {
        fSpecializationsFilter.add(profile);
    }

    void removeSpecializationsFilter(IProfile profile) {
        fSpecializationsFilter.remove(profile);
    }
    
    void resetSpecializationsFilter() {
        fSpecializationsFilter.clear();
    }

    void setShowAllFolders(boolean set) {
        fShowAllFolders = set;
    }
    
    boolean isShowAllFolders() {
        return fShowAllFolders;
    }
    
    void setFilterViews(boolean set) {
        fFilterViews = set;
    }
    
    boolean isFilteringViews() {
        return fFilterViews;
    }

}