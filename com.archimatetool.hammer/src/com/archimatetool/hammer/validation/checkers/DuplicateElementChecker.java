/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;


/**
 * Checks for possible duplicates of elements of the same type
 * 
 * @author Phillip Beauvoir
 */
public class DuplicateElementChecker implements IChecker {
    
    final String NAME = Messages.DuplicateElementChecker_0;
    final String DESCRIPTION = Messages.DuplicateElementChecker_1;
    final String EXPLANATION = Messages.DuplicateElementChecker_2;
    
    private List<IArchimateElement> fArchimateElements;
    
    public DuplicateElementChecker(List<IArchimateElement> archimateElements) {
        fArchimateElements = archimateElements;
    }

    @Override
    public List<IIssue> getIssues() {
        return findDuplicateNamesElements();
    }
    
    List<IIssue> findDuplicateNamesElements() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        Set<IArchimateElement> dupes = new LinkedHashSet<IArchimateElement>();
        
        for(int i = 0; i < fArchimateElements.size(); i++) {
            for(int j = i + 1; j < fArchimateElements.size(); j++) {
                IArchimateElement element1 = fArchimateElements.get(i);
                IArchimateElement element2 = fArchimateElements.get(j);
                if(isDuplicate(element1, element2)) {
                    dupes.add(element1);
                    dupes.add(element2);
                }
            }
        }
        
        for(IArchimateElement element : dupes) {
            String description = NLS.bind(DESCRIPTION, new Object[] { element.getName(),
                    ArchiLabelProvider.INSTANCE.getDefaultName(element.eClass()) });

            IIssue issue = new WarningType(NAME, description, EXPLANATION, element);
            issues.add(issue);
        }
        
        return issues;
    }
    
    private boolean isDuplicate(IArchimateElement element1, IArchimateElement element2) {
        // Ignore Junctions as these tend to just be called "Junction" or may well have a common name like "Or" or "And"
        if(element1.eClass() == IArchimatePackage.eINSTANCE.getJunction() &&
                element2.eClass() == IArchimatePackage.eINSTANCE.getJunction()) {
            return false;
        }
        
        String name1 = StringUtils.safeString(element1.getName());
        String name2 = StringUtils.safeString(element2.getName());
        
        return name1.equals(name2) && (element1.eClass() == element2.eClass());
    }
}
