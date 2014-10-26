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
import com.archimatetool.model.IArchimateComponent;
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
    
    List<IArchimateComponent> fElements = new ArrayList<IArchimateComponent>();
    private IArchimateComponent fCurrentComponent;
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
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessActor());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessRole());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessInterface());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessCollaboration());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getLocation());
        }
        else if(ELEMENTS_BUSINESS_FUNCTIONS.equals(type)) {
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessFunction());
        }
        else if(ELEMENTS_BUSINESS_INFORMATION.equals(type)) {
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessObject());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getRepresentation());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getMeaning());
        }
        else if(ELEMENTS_BUSINESS_PROCESSES.equals(type)) {
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessEvent());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessInteraction());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessProcess());
        }
        else if(ELEMENTS_BUSINESS_PRODUCTS.equals(type)) {
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getContract());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getProduct());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getBusinessService());
            getComponents(businessFolder, IArchimatePackage.eINSTANCE.getValue());
        }
        else if(ELEMENTS_APPLICATIONS.equals(type)) {
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationComponent());
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationFunction());
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationInteraction());
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationInterface());
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getApplicationService());
        }
        else if(ELEMENTS_APPLICATION_DATA.equals(type)) {
            getComponents(applicationFolder, IArchimatePackage.eINSTANCE.getDataObject());
        }
        else if(ELEMENTS_INFRASTRUCTURES.equals(type)) {
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getArtifact());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getCommunicationPath());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getDevice());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getNode());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureFunction());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getNetwork());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getInfrastructureService());
            getComponents(technologyFolder, IArchimatePackage.eINSTANCE.getSystemSoftware());
        }
        else if(ELEMENTS_MOTIVATION.equals(type)) {
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getStakeholder());
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getDriver());
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getAssessment());
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getGoal());
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getPrinciple());
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getRequirement());
            getComponents(motivationFolder, IArchimatePackage.eINSTANCE.getConstraint());
        }
        else if(ELEMENTS_IMPLEMENTATION_MIGRATION.equals(type)) {
            getComponents(implmigrationFolder, IArchimatePackage.eINSTANCE.getWorkPackage());
            getComponents(implmigrationFolder, IArchimatePackage.eINSTANCE.getDeliverable());
            getComponents(implmigrationFolder, IArchimatePackage.eINSTANCE.getPlateau());
            getComponents(implmigrationFolder, IArchimatePackage.eINSTANCE.getGap());
        }
        else if(ELEMENTS_CONNECTORS.equals(type)) {
            getComponents(connectorsFolder, null);
        }
        else if(ELEMENTS_RELATIONS.equals(type)) {
            getComponents(relationsFolder, null);
        }
        // A particular Element type in any folder
        else if(type != null) {
            EClassifier classifier =  IArchimatePackage.eINSTANCE.getEClassifier(type);
            if(classifier instanceof EClass) {
                getComponents(model, (EClass)classifier);
            }
        }
        
        ArchimateModelDataSource.sort(fElements);
    }

    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentComponent);
    }
    
    @Override
    public Object getElement() {
        return fCurrentComponent;
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fElements.size() - 1) {
            fCurrentComponent = fElements.get(++currentIndex);
        }
        else {
            fCurrentComponent = null;
        }
        
        return fCurrentComponent != null;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fCurrentComponent, jrField.getName());
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }
    
    private void getComponents(IArchimateModel model, EClass type) {
        for(IFolder folder : model.getFolders()) {
            getComponents(folder, type);
        }
    }

    private void getComponents(IFolder folder, EClass type) {
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateComponent) {
                if(type == null || object.eClass() == type) {
                    fElements.add((IArchimateComponent)object);
                }
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getComponents(f, type);
        }
    }

}
