/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * ViewChildrenDataSource - child ArchiMate type objects of a View
 * 
 * @author Phillip Beauvoir
 */
public class ViewChildrenDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    private List<IArchimateConcept> fChildren = new ArrayList<IArchimateConcept>();
    private IArchimateConcept fCurrentElement;
    private int currentIndex = -1;
    
    /**
     * Default constructor with old behaviour, which is all elements and sorting by name only
     * @param dm - the Diagram model
     * 
     **/
    public ViewChildrenDataSource(IDiagramModel dm) {
        this(dm, false);
    }
    
    /**
     * Constructor that allows for some alternative sorting of the retrieved elements (not relations)
     * @param dm - the Diagram model
     * @param sortFirstByType - Boolean that indicates if the results should be sorted by
     *                          type first, and secondly by name. If set to false, the results
     *                          will be sorted by name only 
     **/
    public ViewChildrenDataSource(IDiagramModel dm, boolean sortFirstByType) {
        fChildren.addAll(ArchimateModelDataSource.getConceptsInDiagram(dm, ELEMENTS));
        
        if(sortFirstByType) {
            ArchimateModelDataSource.sortByTypeThenName(fChildren);
        }
        else {
            ArchimateModelDataSource.sort(fChildren);
        }
    }

    /**
     * Constructor that allows for some tweaking in the types of classes that are retrieved
     * @param dm - the Diagram model
     * @param types - String that indicates the types of elements to be retrieved. This can be
     *                from the set of string indicating either all elements ('elements') or 
     *                all relations ('relations'), a specific layer (e.g. 'business') or even a
     *                specific type (e.g. 'businessrole'). You can specify more
     *                than one type, using the '|' as separator
     **/
    public ViewChildrenDataSource(IDiagramModel dm, String types) {
        this(dm, types, false);
    }

    /**
     * Constructor that allows for some tweaking in the types of classes and their sorting that are retrieved
     * @param dm - the Diagram model
     * @param types - String that indicates the types of elements to be retrieved. This can be
     *                from the set of string indicating either all elements ('elements') or 
     *                all relations ('relations'), a specific layer (e.g. 'business') or even a
     *                specific type (e.g. 'businessrole'). You can specify more
     *                than one type, using the '|' as separator
     * @param sortFirstByType - Boolean that indicates if the results should be sorted by
     *                          type first, and secondly by name. If set to false, the results
     *                          will be sorted by name only 
     **/
    public ViewChildrenDataSource(IDiagramModel dm, String types, boolean sortFirstByType) {
        fChildren.addAll(ArchimateModelDataSource.getConceptsInDiagram(dm, types));
        
        if(sortFirstByType) {
            ArchimateModelDataSource.sortByTypeThenName(fChildren);
        }
        else {
            ArchimateModelDataSource.sort(fChildren);
        }
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fChildren.size() - 1) {
            fCurrentElement = fChildren.get(++currentIndex);
        }
        else {
            fCurrentElement = null;
        }
        
        return fCurrentElement != null;
    }

    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentElement);
    }
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fCurrentElement, jrField.getName());
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }

    @Override
    public IArchimateConcept getElement() {
        return fCurrentElement;
    }
    
    public int size() {
        return fChildren.size();
    }

}
