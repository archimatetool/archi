/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.List;

import com.archimatetool.hammer.validation.Validator;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IRelationship;


/**
 * AbstractChecker
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractChecker implements IChecker {

    protected IArchimateModel model;
    protected List<IArchimateElement> archimateElements;
    protected List<IRelationship> archimateRelations;
    protected List<IArchimateDiagramModel> archimateViews;
    
    protected AbstractChecker(Validator validator) {
        model = validator.getModel();
        archimateElements = validator.getArchimateElements();
        archimateRelations = validator.getArchimateRelationships();
        archimateViews = validator.getArchimateViews();
    }
    
    
}
