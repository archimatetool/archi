/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;



/**
 * Elements DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ElementsDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    List<IArchimateConcept> fConcepts = new ArrayList<IArchimateConcept>();
    private IArchimateConcept fCurrentConcept;
    private int currentIndex = -1;

    
    /**
     * Default constructor with old behaviour, which is all elements and sorting by name only
     **/
    public ElementsDataSource(IArchimateModel model, String types) {
        this(model, types, false);
    }

    public ElementsDataSource(IArchimateModel model, String types, boolean sortFirstByType) {
        Set<EClass> desiredEClasses = ArchimateModelDataSource.getClasses(types);
        getConcepts(model, desiredEClasses);

        if(sortFirstByType) {
            ArchimateModelDataSource.sortByTypeThenName(fConcepts);
        }
        else {
            ArchimateModelDataSource.sort(fConcepts);
        }
    }

    /**
     * @param dm - the Diagram model
     **/
    public ElementsDataSource(IDiagramModel dm) {
        this(dm, false);
    }
    
    /**
     * Constructor that allows for some alternative sorting of the retrieved elements (not relations)
     * @param dm - the Diagram model
     * @param sortFirstByType - Boolean that indicates if the results should be sorted by
     *                          type first, and secondly by name. If set to false, the results
     *                          will be sorted by name only 
     **/
    public ElementsDataSource(IDiagramModel dm, boolean sortFirstByType) {
    	this(dm, ELEMENTS, sortFirstByType);
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
    public ElementsDataSource(IDiagramModel dm, String types) {
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
    public ElementsDataSource(IDiagramModel dm, String types, boolean sortFirstByType) {
        fConcepts.addAll(ArchimateModelDataSource.getConceptsInDiagram(dm, types));
        
        if(sortFirstByType) {
            ArchimateModelDataSource.sortByTypeThenName(fConcepts);
        }
        else {
            ArchimateModelDataSource.sort(fConcepts);
        }
    }

    public int size() {
    	return fConcepts.size();
    }
    
    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentConcept);
    }
    
    @Override
    public IArchimateConcept getElement() {
        return fCurrentConcept;
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fConcepts.size() - 1) {
            fCurrentConcept = fConcepts.get(++currentIndex);
        }
        else {
            fCurrentConcept = null;
        }
        
        return fCurrentConcept != null;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fCurrentConcept, jrField.getName());
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }
    
    /**
     * @return all diagram views that this concept appears in
     */
    public ViewModelDataSource getReferencedViews() {
        if(fCurrentConcept == null) {
            return null;
        }
        
        List<IDiagramModel> views = DiagramModelUtils.findReferencedDiagramsForArchimateConcept(fCurrentConcept);
        return new ViewModelDataSource(views);
    }

    private void getConcepts(IArchimateModel model, Set<EClass> desiredEClasses) {
        for(IFolder folder : model.getFolders()) {
            getConcepts(folder, desiredEClasses);
        }
    }

    private void getConcepts(IFolder folder, Set<EClass> desiredEClasses) {
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateConcept) {
                if(desiredEClasses.contains(object.eClass()) && !fConcepts.contains(object)) {
                	fConcepts.add((IArchimateConcept)object);
                }
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getConcepts(f, desiredEClasses);
        }
    }
}
