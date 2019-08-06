/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.ArchimateModelUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;



/**
 * Elements DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ElementsDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    public static final String ELEMENTS_STRATEGY = "strategy"; //$NON-NLS-1$
    public static final String ELEMENTS_BUSINESS = "business"; //$NON-NLS-1$
    public static final String ELEMENTS_APPLICATION = "application"; //$NON-NLS-1$
    public static final String ELEMENTS_TECHNOLOGY = "technology"; //$NON-NLS-1$
    public static final String ELEMENTS_MOTIVATION = "motivation"; //$NON-NLS-1$
    public static final String ELEMENTS_IMPLEMENTATION_MIGRATION = "impl_migration"; //$NON-NLS-1$
    public static final String ELEMENTS_OTHER = "other"; //$NON-NLS-1$
    public static final String ELEMENTS_RELATIONS = "relations"; //$NON-NLS-1$
    
    List<IArchimateConcept> fConcepts = new ArrayList<IArchimateConcept>();
    private IArchimateConcept fCurrentConcept;
    private int currentIndex = -1;

    public ElementsDataSource(IArchimateModel model, String type) {
        IFolder strategyFolder = model.getFolder(FolderType.STRATEGY);
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        IFolder applicationFolder = model.getFolder(FolderType.APPLICATION);
        IFolder technologyFolder = model.getFolder(FolderType.TECHNOLOGY);
        IFolder motivationFolder = model.getFolder(FolderType.MOTIVATION);
        IFolder implmigrationFolder = model.getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        IFolder otherFolder = model.getFolder(FolderType.OTHER);
        IFolder relationsFolder = model.getFolder(FolderType.RELATIONS);
        
        if(ELEMENTS_STRATEGY.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getStrategyClasses()) {
                getConcepts(strategyFolder, eClass);
            }
        }

        else if(ELEMENTS_BUSINESS.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
                getConcepts(businessFolder, eClass);
            }
        }
        
        else if(ELEMENTS_APPLICATION.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
                getConcepts(applicationFolder, eClass);
            }
        }
        
        else if(ELEMENTS_TECHNOLOGY.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
                getConcepts(technologyFolder, eClass);
            }
            for(EClass eClass : ArchimateModelUtils.getPhysicalClasses()) {
                getConcepts(technologyFolder, eClass);
            }
        }
        
        else if(ELEMENTS_MOTIVATION.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
                getConcepts(motivationFolder, eClass);
            }
        }
        
        else if(ELEMENTS_IMPLEMENTATION_MIGRATION.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
                getConcepts(implmigrationFolder, eClass);
            }
        }
        
        else if(ELEMENTS_OTHER.equals(type)) {
            for(EClass eClass : ArchimateModelUtils.getOtherClasses()) {
                getConcepts(otherFolder, eClass);
            }
            for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
                getConcepts(otherFolder, eClass);
            }
        }
        
        else if(ELEMENTS_RELATIONS.equals(type)) {
            getConcepts(relationsFolder, null);
        }
        
        // A particular Element type in any folder
        else if(type != null) {
            EClassifier classifier =  IArchimatePackage.eINSTANCE.getEClassifier(type);
            if(classifier instanceof EClass) {
                getConcepts(model, (EClass)classifier);
            }
        }
        
        ArchimateModelDataSource.sort(fConcepts);
    }

    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentConcept);
    }
    
    @Override
    public Object getElement() {
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
    
    private void getConcepts(IArchimateModel model, EClass type) {
        for(IFolder folder : model.getFolders()) {
            getConcepts(folder, type);
        }
    }

    private void getConcepts(IFolder folder, EClass type) {
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateConcept) {
                if(type == null || object.eClass() == type) {
                    fConcepts.add((IArchimateConcept)object);
                }
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getConcepts(f, type);
        }
    }
    public void setConcepts(List<IArchimateConcept> concepts ) {
    	this.fConcepts = concepts;
    }
    public List<IArchimateConcept> getConceptList() {
    	return this.fConcepts;
    }
}
