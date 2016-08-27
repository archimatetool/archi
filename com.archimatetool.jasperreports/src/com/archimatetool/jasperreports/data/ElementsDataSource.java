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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;



/**
 * Elements DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ElementsDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    public static final String ELEMENTS_BUSINESS_ACTORS = "business_actors"; //$NON-NLS-1$
    public static final String ELEMENTS_BUSINESS_FUNCTIONS = "business_functions"; //$NON-NLS-1$
    public static final String ELEMENTS_BUSINESS_INFORMATION = "business_information"; //$NON-NLS-1$
    public static final String ELEMENTS_BUSINESS_PROCESSES = "business_processes"; //$NON-NLS-1$
    public static final String ELEMENTS_BUSINESS_PRODUCTS = "business_products"; //$NON-NLS-1$
    
    public static final String ELEMENTS_APPLICATIONS = "applications"; //$NON-NLS-1$
    public static final String ELEMENTS_APPLICATION_DATA = "application_data"; //$NON-NLS-1$

    public static final String ELEMENTS_INFRASTRUCTURES = "infrastructures"; //$NON-NLS-1$

    public static final String ELEMENTS_MOTIVATION = "motivation"; //$NON-NLS-1$
    public static final String ELEMENTS_IMPLEMENTATION_MIGRATION = "impl_migration"; //$NON-NLS-1$
    
    public static final String ELEMENTS_CONNECTORS = "connectors"; //$NON-NLS-1$
    public static final String ELEMENTS_RELATIONS = "relations"; //$NON-NLS-1$
    
    List<IArchimateConcept> fConcepts = new ArrayList<IArchimateConcept>();
    private IArchimateConcept fCurrentConcept;
    private int currentIndex = -1;

    public ElementsDataSource(IArchimateModel model, String type) {
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        IFolder applicationFolder = model.getFolder(FolderType.APPLICATION);
        IFolder technologyFolder = model.getFolder(FolderType.TECHNOLOGY);
        IFolder motivationFolder = model.getFolder(FolderType.MOTIVATION);
        IFolder implmigrationFolder = model.getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        IFolder connectorsFolder = model.getFolder(FolderType.CONNECTORS);
        IFolder relationsFolder = model.getFolder(FolderType.RELATIONS);

        if(ELEMENTS_BUSINESS_ACTORS.equals(type)) { 
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessActor());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessRole());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessInterface());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessCollaboration());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getLocation());
        }
        else if(ELEMENTS_BUSINESS_FUNCTIONS.equals(type)) {
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessFunction());
        }
        else if(ELEMENTS_BUSINESS_INFORMATION.equals(type)) {
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessObject());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getRepresentation());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getMeaning());
        }
        else if(ELEMENTS_BUSINESS_PROCESSES.equals(type)) {
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessEvent());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessInteraction());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessProcess());
        }
        else if(ELEMENTS_BUSINESS_PRODUCTS.equals(type)) {
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getContract());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getProduct());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getBusinessService());
            getConcepts(businessFolder, IArchimatePackage.eINSTANCE.getValue());
        }
        else if(ELEMENTS_APPLICATIONS.equals(type)) {
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationComponent());
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationFunction());
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationInteraction());
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationInterface());
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationService());
        }
        else if(ELEMENTS_APPLICATION_DATA.equals(type)) {
            getConcepts(applicationFolder, IArchimatePackage.eINSTANCE.getDataObject());
        }
        else if(ELEMENTS_INFRASTRUCTURES.equals(type)) {
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getArtifact());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getCommunicationPath());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getDevice());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getNode());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureFunction());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getNetwork());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureService());
            getConcepts(technologyFolder, IArchimatePackage.eINSTANCE.getSystemSoftware());
        }
        else if(ELEMENTS_MOTIVATION.equals(type)) {
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getStakeholder());
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getDriver());
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getAssessment());
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getGoal());
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getPrinciple());
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getRequirement());
            getConcepts(motivationFolder, IArchimatePackage.eINSTANCE.getConstraint());
        }
        else if(ELEMENTS_IMPLEMENTATION_MIGRATION.equals(type)) {
            getConcepts(implmigrationFolder, IArchimatePackage.eINSTANCE.getWorkPackage());
            getConcepts(implmigrationFolder, IArchimatePackage.eINSTANCE.getDeliverable());
            getConcepts(implmigrationFolder, IArchimatePackage.eINSTANCE.getPlateau());
            getConcepts(implmigrationFolder, IArchimatePackage.eINSTANCE.getGap());
        }
        else if(ELEMENTS_CONNECTORS.equals(type)) {
            getConcepts(connectorsFolder, null);
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

}
