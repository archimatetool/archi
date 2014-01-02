/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;



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
    
    List<IArchimateElement> fElements = new ArrayList<IArchimateElement>();
    private IArchimateElement fCurrentElement;
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
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessActor());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessRole());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessInterface());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessCollaboration());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getLocation());
        }
        else if(ELEMENTS_BUSINESS_FUNCTIONS.equals(type)) {
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessFunction());
        }
        else if(ELEMENTS_BUSINESS_INFORMATION.equals(type)) {
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessObject());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getRepresentation());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getMeaning());
        }
        else if(ELEMENTS_BUSINESS_PROCESSES.equals(type)) {
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessEvent());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessInteraction());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessProcess());
        }
        else if(ELEMENTS_BUSINESS_PRODUCTS.equals(type)) {
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getContract());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getProduct());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getBusinessService());
            getElements(businessFolder, IArchimatePackage.eINSTANCE.getValue());
        }
        else if(ELEMENTS_APPLICATIONS.equals(type)) {
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationComponent());
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationFunction());
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationInteraction());
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationInterface());
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationService());
        }
        else if(ELEMENTS_APPLICATION_DATA.equals(type)) {
            getElements(applicationFolder, IArchimatePackage.eINSTANCE.getDataObject());
        }
        else if(ELEMENTS_INFRASTRUCTURES.equals(type)) {
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getArtifact());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getCommunicationPath());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getDevice());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getNode());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureFunction());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getNetwork());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureService());
            getElements(technologyFolder, IArchimatePackage.eINSTANCE.getSystemSoftware());
        }
        else if(ELEMENTS_MOTIVATION.equals(type)) {
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getStakeholder());
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getDriver());
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getAssessment());
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getGoal());
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getPrinciple());
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getRequirement());
            getElements(motivationFolder, IArchimatePackage.eINSTANCE.getConstraint());
        }
        else if(ELEMENTS_IMPLEMENTATION_MIGRATION.equals(type)) {
            getElements(implmigrationFolder, IArchimatePackage.eINSTANCE.getWorkPackage());
            getElements(implmigrationFolder, IArchimatePackage.eINSTANCE.getDeliverable());
            getElements(implmigrationFolder, IArchimatePackage.eINSTANCE.getPlateau());
            getElements(implmigrationFolder, IArchimatePackage.eINSTANCE.getGap());
        }
        else if(ELEMENTS_CONNECTORS.equals(type)) {
            getElements(connectorsFolder, null);
        }
        else if(ELEMENTS_RELATIONS.equals(type)) {
            getElements(relationsFolder, null);
        }
        // A particular Element type in any folder
        else if(type != null) {
            EClassifier classifier =  IArchimatePackage.eINSTANCE.getEClassifier(type);
            if(classifier instanceof EClass) {
                getElements(model, (EClass)classifier);
            }
        }
        
        ArchimateModelDataSource.sort(fElements);
    }

    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentElement);
    }
    
    @Override
    public Object getElement() {
        return fCurrentElement;
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fElements.size() - 1) {
            fCurrentElement = fElements.get(++currentIndex);
        }
        else {
            fCurrentElement = null;
        }
        
        return fCurrentElement != null;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fCurrentElement, jrField.getName());
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }
    
    private void getElements(IArchimateModel model, EClass type) {
        for(IFolder folder : model.getFolders()) {
            getElements(folder, type);
        }
    }

    private void getElements(IFolder folder, EClass type) {
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateElement) {
                if(type == null || object.eClass() == type) {
                    fElements.add((IArchimateElement)object);
                }
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getElements(f, type);
        }
    }

}
