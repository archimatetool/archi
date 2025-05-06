/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.search;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
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

    private boolean filterName;
    private boolean filterDocumentation;
    private boolean filterPropertyValues;
    private boolean filterViews;
    
    private boolean showAllFolders;
    private boolean matchCase;
    private boolean useRegex;
    
    private Set<String> propertyKeyFilter = new HashSet<>();
    private Set<EClass> conceptsFilter = new HashSet<>();
    private Set<IProfile> specializationsFilter = new HashSet<>();
    
    private Matcher regexMatcher;

    SearchFilter() {
    }

    void setSearchText(String text) {
        fSearchText = text;
        createRegexMatcher();
    }

    void reset() {
        setFilterOnName(true);
        setFilterOnDocumentation(false);
        setFilterOnPropertyValues(false);
        
        resetConceptsFilter();
        resetPropertyKeyFilter();
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

            if(getShowAllFolders()) {
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
        if(isFilteringName() || isFilteringDocumentation() || isFilteringPropertyKeys() || isFilteringPropertyValues()) {
            show &= (isFilteringName() && shouldShowObjectWithName(element))
                    || (isFilteringDocumentation() && shouldShowObjectWithDocumentation(element))
                    || ((isFilteringPropertyKeys() || isFilteringPropertyValues()) && shouldShowObjectWithProperty(element));
        }
        
        return show;
    }
    
    private boolean shouldShowConcept(Object element) {
        return conceptsFilter.contains(((EObject)element).eClass());
    }

    private boolean shouldShowSpecialization(Object element) {
        if(element instanceof IArchimateConcept concept) {
            for(IProfile profile : concept.getProfiles()) {
                for(IProfile p : specializationsFilter) {
                    // Could be matching Profile name/class in different models
                    if(ArchimateModelUtils.isMatchingProfile(p, profile)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean shouldShowObjectWithName(Object element) {
        if(element instanceof INameable nameable) {
            String name = StringUtils.safeString(nameable.getName());
            return matchesString(name);
        }
        
        return false;
    }

    private boolean shouldShowObjectWithDocumentation(Object element) {
        if(element instanceof IDocumentable documentable) {
            String text = StringUtils.safeString(documentable.getDocumentation());
            return matchesString(text);
        }
        
        // "Purpose" is really "Documentation"
        if(element instanceof IArchimateModel model) {
            String text = StringUtils.safeString(model.getPurpose());
            return matchesString(text);
        }
        
        return false;
    }

    private boolean shouldShowObjectWithProperty(Object element) {
        if(element instanceof IProperties properties) {
            for(IProperty property : properties.getProperties()) {
                // If filtering on property keys
                if(isFilteringPropertyKeys()) {
                    // And this property has that filtered key
                    if(propertyKeyFilter.contains(property.getKey())) {
                        // If filtering on property value match on that else show the element
                        return isFilteringPropertyValues() ? matchesString(property.getValue()) : true;
                    }
                }
                // Else match on property value
                else if(matchesString(property.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }
    
    private boolean matchesString(String str) {
        if(str == null || str.length() == 0) {
            return false;
        }
        
        if(getUseRegex()) {
            return matchesRegexString(str);
        }
        
        if(getMatchCase()) {
            return str.contains(fSearchText);
        }
        
        return str.toLowerCase().contains(fSearchText.toLowerCase()); 
    }
    
    private boolean matchesRegexString(String searchString) {
        return regexMatcher != null ? regexMatcher.reset(searchString).find() : false;
    }
    
    private void createRegexMatcher() {
        regexMatcher = null;
        
        if(getUseRegex() && hasSearchText()) {
            try {
                // Create a Matcher from the search text Pattern that can be re-used
                Pattern pattern = Pattern.compile(fSearchText, getMatchCase() ? 0 : Pattern.CASE_INSENSITIVE);
                regexMatcher = pattern.matcher("");
            }
            catch(Exception ex) {
            }
        }
    }

    public boolean isFiltering() {
        return isFilteringName()
                || isFilteringDocumentation()
                || isFilteringConcepts()
                || isFilteringPropertyKeys()
                || isFilteringPropertyValues()
                || isFilteringSpecializations()
                || isFilteringViews();
    }

    private boolean hasSearchText() {
        return fSearchText.length() > 0;
    }
    
    boolean isValidSearchString() {
        // If we are using regex and we have a matcher then it's valid
        if(getUseRegex() && hasSearchText()) {
            return regexMatcher != null;
        }
        
        return true;
    }

    // ===== Name

    void setFilterOnName(boolean set) {
        filterName = set;
    }

    boolean getFilterOnName() {
        return filterName;
    }
    
    private boolean isFilteringName() {
        return getFilterOnName() && hasSearchText();
    }
    
    // ===== Documentation

    void setFilterOnDocumentation(boolean set) {
        filterDocumentation = set;
    }

    boolean getFilterOnDocumentation() {
        return filterDocumentation;
    }

    private boolean isFilteringDocumentation() {
        return getFilterOnDocumentation() && hasSearchText();
    }
    
    // ===== Property Values
    
    void setFilterOnPropertyValues(boolean set) {
        filterPropertyValues = set;
    }
    
    boolean getFilterOnPropertyValues() {
        return filterPropertyValues;
    }

    private boolean isFilteringPropertyValues() {
        return getFilterOnPropertyValues() && hasSearchText();
    }

    // ===== Property Keys

    void addPropertyKeyFilter(String key) {
        propertyKeyFilter.add(key);
    }

    void removePropertyKeyFilter(String key) {
        propertyKeyFilter.remove(key);
    }
    
    Set<String> getPropertyKeyFilter() {
        return propertyKeyFilter;
    }
    
    void resetPropertyKeyFilter() {
        propertyKeyFilter.clear();
    }
    
    boolean isFilteringPropertyKeys() {
        return !propertyKeyFilter.isEmpty();
    }

    // ===== Concepts

    void addConceptFilter(EClass eClass) {
        conceptsFilter.add(eClass);
    }

    void removeConceptFilter(EClass eClass) {
        conceptsFilter.remove(eClass);
    }

    void resetConceptsFilter() {
        conceptsFilter.clear();
    }

    boolean isFilteringConcepts() {
        return !conceptsFilter.isEmpty();
    }
    
    // ===== Specializations

    void addSpecializationsFilter(IProfile profile) {
        specializationsFilter.add(profile);
    }

    void removeSpecializationsFilter(IProfile profile) {
        specializationsFilter.remove(profile);
    }
    
    void resetSpecializationsFilter() {
        specializationsFilter.clear();
    }

    boolean isFilteringSpecializations() {
        return !specializationsFilter.isEmpty();
    }

    // ===== All Folders
    
    void setShowAllFolders(boolean set) {
        showAllFolders = set;
    }
    
    boolean getShowAllFolders() {
        return showAllFolders;
    }
    
    // ===== Views
    
    void setFilterViews(boolean set) {
        filterViews = set;
    }
    
    boolean isFilteringViews() {
        return filterViews;
    }
    
    // ===== Match Case
    
    void setMatchCase(boolean set) {
        matchCase = set;
        createRegexMatcher();
    }
    
    boolean getMatchCase() {
        return matchCase;
    }
    
    // ===== Regex
    
    void setUseRegex(boolean set) {
        useRegex = set;
        createRegexMatcher();
    }

    boolean getUseRegex() {
        return useRegex;
    }
}