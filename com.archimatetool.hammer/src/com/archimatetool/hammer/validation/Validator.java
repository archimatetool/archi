/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.hammer.ArchiHammerPlugin;
import com.archimatetool.hammer.preferences.IPreferenceConstants;
import com.archimatetool.hammer.validation.checkers.DuplicateElementChecker;
import com.archimatetool.hammer.validation.checkers.EmptyViewsChecker;
import com.archimatetool.hammer.validation.checkers.IChecker;
import com.archimatetool.hammer.validation.checkers.InvalidRelationsChecker;
import com.archimatetool.hammer.validation.checkers.JunctionsChecker;
import com.archimatetool.hammer.validation.checkers.NestedElementsChecker;
import com.archimatetool.hammer.validation.checkers.UnusedElementsChecker;
import com.archimatetool.hammer.validation.checkers.UnusedRelationsChecker;
import com.archimatetool.hammer.validation.checkers.ViewpointChecker;
import com.archimatetool.hammer.validation.issues.AdviceCategory;
import com.archimatetool.hammer.validation.issues.AdviceType;
import com.archimatetool.hammer.validation.issues.ErrorType;
import com.archimatetool.hammer.validation.issues.ErrorsCategory;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.IIssueCategory;
import com.archimatetool.hammer.validation.issues.OKType;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.hammer.validation.issues.WarningsCategory;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;


/**
 * Validator
 * 
 * @author Phillip Beauvoir
 */
public class Validator {
    
    private IArchimateModel fModel;
    
    private List<IArchimateElement> fElements;
    private List<IArchimateRelationship> fRelations;
    private List<IArchimateDiagramModel> fViews;
    
    private List<ErrorType> fErrorList;
    private List<WarningType> fWarningList;
    private List<AdviceType> fAdviceList;

    
    public Validator(IArchimateModel model) {
        fModel = model;
    }

    /**
     * @return The list of Issue Categories and Issues
     */
    public List<Object> validate() {
        if(fModel == null) {
            return null;
        }
        
        // Collect interesting objects
        fElements = new ArrayList<IArchimateElement>();
        fRelations = new ArrayList<IArchimateRelationship>();
        fViews = new ArrayList<IArchimateDiagramModel>();
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            if(eObject instanceof IArchimateRelationship) {
                fRelations.add((IArchimateRelationship)eObject);
            }
            else if(eObject instanceof IArchimateElement) {
                fElements.add((IArchimateElement)eObject);
            }
            else if(eObject instanceof IArchimateDiagramModel) {
                fViews.add((IArchimateDiagramModel)eObject);
            }
        }
        
        // Analyse
        List<Object> result = new ArrayList<Object>();
        
        fErrorList = new ArrayList<ErrorType>();
        fWarningList = new ArrayList<WarningType>();
        fAdviceList = new ArrayList<AdviceType>();
        
        // ------------------ Checkers -----------------------------
        
        IPreferenceStore store = ArchiHammerPlugin.INSTANCE.getPreferenceStore();
        
        // Invalid Relations
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_INVALID_RELATIONS)) {
            collectIssues(new InvalidRelationsChecker(getArchimateRelationships()));
        }
        
        // Unused Elements
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_UNUSED_ELEMENTS)) {
            collectIssues(new UnusedElementsChecker(getArchimateElements()));
        }
        
        // Unused Relations
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_UNUSED_RELATIONS)) {
            collectIssues(new UnusedRelationsChecker(getArchimateRelationships()));
        }
        
        // Empty Views
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_EMPTY_VIEWS)) {
            collectIssues(new EmptyViewsChecker(getArchimateViews()));
        }
        
        // Components in wrong Viewpoints
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_VIEWPOINT)) {
            collectIssues(new ViewpointChecker(getArchimateViews()));
        }
        
        // Nested elements
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_NESTING)) {
            collectIssues(new NestedElementsChecker(getArchimateViews()));
        }

        // Possible Duplicates
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_DUPLICATE_ELEMENTS)) {
            collectIssues(new DuplicateElementChecker(getArchimateElements()));
        }
        
        // Junctions
        if(store.getBoolean(IPreferenceConstants.PREFS_HAMMER_CHECK_JUNCTIONS)) {
            collectIssues(new JunctionsChecker(getArchimateElements()));
        }

        // ----------------------------------------------------------

        if(!fErrorList.isEmpty()) {
            IIssueCategory category = new ErrorsCategory(fErrorList);
            result.add(category);
        }

        if(!fWarningList.isEmpty()) {
            IIssueCategory category = new WarningsCategory(fWarningList);
            result.add(category);
        }
        
        if(!fAdviceList.isEmpty()) {
            IIssueCategory category = new AdviceCategory(fAdviceList);
            result.add(category);
        }

        if(result.isEmpty()) {
            result.add(new OKType());
        }
        
        return result;
    }
    
    void collectIssues(IChecker checker) {
        for(IIssue issue : checker.getIssues()) {
            if(issue instanceof ErrorType) {
                fErrorList.add((ErrorType)issue);
            }
            if(issue instanceof WarningType) {
                fWarningList.add((WarningType)issue);
            }
            if(issue instanceof AdviceType) {
                fAdviceList.add((AdviceType)issue);
            }
        }
    }
    
    public IArchimateModel getModel() {
        return fModel;
    }
    
    public List<IArchimateElement> getArchimateElements() {
        return new ArrayList<IArchimateElement>(fElements); // copy
    }
    
    public List<IArchimateRelationship> getArchimateRelationships() {
        return new ArrayList<IArchimateRelationship>(fRelations); // copy
    }
    
    public List<IArchimateDiagramModel> getArchimateViews() {
        return new ArrayList<IArchimateDiagramModel>(fViews); // copy
    }
}
