/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.INameable;
import com.archimatetool.model.util.ArchimateModelUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


/**
 * Archimate Model DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelDataSource implements JRDataSource, IPropertiesDataSource {
    
    private IArchimateModel fModel;
    private boolean done;
    
    public ArchimateModelDataSource(IArchimateModel model) {
        fModel = model;
    }
    
    public IArchimateModel getModel() {
        return fModel;
    }
    
    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fModel);
    }
    
    public ViewModelDataSource getViewsDataSource() {
        return new ViewModelDataSource(fModel);
    }
    
    public ElementsDataSource getElementsDataSource(String types) {
        return new ElementsDataSource(fModel, types);
    }
    
    /**
     * @param type
     * @return true if there are elements of types to print
     */
    public boolean hasElements(String types) {
        return !new ElementsDataSource(fModel, types).fConcepts.isEmpty(); 
    }

    @Override
    public Object getElement() {
        return fModel;
    }
    
    /**
     * Convenience method to find an element in the model from its id
     * @param id The identifier
     * @return The element, or null if not found
     */
    public Object getElementByID(String id) {
        return ArchimateModelUtils.getObjectByID(fModel, id);
    }

    @Override
    public boolean next() throws JRException {
        if(!done) {
            done = true;
            return true;
        }
        return false;
    }

    /*** 
     * Working with parts we need to be able to reference the ArchiMate model.
     * If we do not 'clone' the datasource we face the challenge that the model is already forwarded to its end (using the
     * next() method above) with the consequence that a new part referencing the ArchiMate model will not
     * have access to any information and therefore not print anything.
     * It seems that rewinding next() does not work because of concurrent processing by Jasper
     * @return A clone of this ArchimateModelDataSource
     */
    @Override
    public ArchimateModelDataSource clone() {
    	return new ArchimateModelDataSource(fModel);
    }
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fModel, jrField.getName());
    }
    
    
    // ===================================== Static Utility Classes ===========================================
    
    /**
     * @param types - string that either refers to the name of the group of elements 
     *                that we are looking for or the specific type. You can specify more
     *                than one type, using the '|' as separator
     *                We need to be case sensitive for a specific EClass type - e.g BusinessActor
     * @return a Set of EClass types
     **/
    public static Set<EClass> getClasses(String types) {
        Set<EClass> eClasses = new LinkedHashSet<EClass>();
        
        for(String type : types.split("\\|")) { //$NON-NLS-1$
            switch(type.trim().toLowerCase()) {
                // All elements
                case ELEMENTS:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getAllArchimateClasses()));
                    break;

                case ELEMENTS_STRATEGY:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getStrategyClasses()));
                    break;
                    
                case ELEMENTS_BUSINESS:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getBusinessClasses()));
                    break;
                    
                case ELEMENTS_APPLICATION:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getApplicationClasses()));
                    break;
                    
                case ELEMENTS_TECHNOLOGY:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getTechnologyClasses()));
                    break;
                    
                case ELEMENTS_PHYSICAL:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getPhysicalClasses()));
                    break;

                case ELEMENTS_MOTIVATION:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getMotivationClasses()));
                    break;
                    
                case ELEMENTS_IMPLEMENTATION_MIGRATION:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getImplementationMigrationClasses()));
                    break;
                    
                case ELEMENTS_OTHER:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getOtherClasses()));
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getConnectorClasses()));
                    break;
                
                // All relations
                case RELATIONS:
                    eClasses.addAll(Arrays.asList(ArchimateModelUtils.getRelationsClasses()));
                    break;
                    
                // Specific class name
                default:
                    EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(type.trim());
                    if(eClass != null) {
                        eClasses.add(eClass);
                    }
                    break;
            }
        }
        
        return eClasses;
    }
    
    /**
     * @param dm - The diagram model
     * param types - string that either refers to the name of the group of elements 
     *                that we are looking for or the specific type. You can specify more
     *                than one type, using the '|' as separator
     *                We need to be case sensitive for a specific EClass type - e.g BusinessActor
     * @return all concepts used in a diagram that match types
     */
    public static Set<IArchimateConcept> getConceptsInDiagram(IDiagramModel dm, String types) {
        Set<IArchimateConcept> concepts = new LinkedHashSet<IArchimateConcept>();
        Set<EClass> desiredEClasses = ArchimateModelDataSource.getClasses(types);
        
        // Iterate through all diagram components and add the ones that match our desired classes
        for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateComponent) {
                IArchimateConcept concept = ((IDiagramModelArchimateComponent)eObject).getArchimateConcept();
                if(concept != null && desiredEClasses.contains(concept.eClass())) {
                    concepts.add(concept);
                }
            }
        }
        
        return concepts;
    }

    /**
     * Sort a List by Name
     */
    public static void sort(List<? extends INameable> list) {
        Collections.sort(list, new Comparator<INameable>() {
            @Override
            public int compare(INameable name1, INameable name2) {
                return compareName(name1, name2);
            }
        });
    }
    
    /**
     * Sort a List by Type
     */
    public static void sortByTypeThenName(List<? extends INameable> list) {
        Collections.sort(list, new Comparator<INameable>() {
            @Override
            public int compare(INameable o1, INameable o2) {
                // Type
                int returnValue = compareType(o1, o2);
                // Then Name
                return returnValue == 0 ? compareName(o1, o2) : returnValue;
            }
        });
    }
    
    private static int compareName(INameable o1, INameable o2) {
        String name1 = StringUtils.safeString(o1.getName()).toLowerCase().trim();
        String name2 = StringUtils.safeString(o2.getName()).toLowerCase().trim();
        return name1.compareTo(name2);
    }

    private static int compareType(EObject o1, EObject o2) {
        String class1 = o1.eClass().getName();
        String class2 = o2.eClass().getName();
        return class1.compareTo(class2);
    }
}
