/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.impl.ArchimateRelationship;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * View Model DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ViewModelDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    private List<IDiagramModel> fViews;
    private IDiagramModel fCurrentView;
    private int currentIndex = -1;
    
    public ViewModelDataSource(IArchimateModel model) {
        // Sort a *copy* of the List
        fViews = new ArrayList<IDiagramModel>(model.getDiagramModels());
        ArchimateModelDataSource.sort(fViews);
    }
    
    public String getViewpointName() {
        if(fCurrentView instanceof IArchimateDiagramModel) {
            String id = ((IArchimateDiagramModel)fCurrentView).getViewpoint();
            
            IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint(id);
            
            if(vp == ViewpointManager.NONE_VIEWPOINT) {
                return Messages.ViewModelDataSource_1;
            }
            
            String name = vp.getName();
            return name == null ? "" : NLS.bind(Messages.ViewModelDataSource_0, name); //$NON-NLS-1$
        }
        
        return null;
    }
    
    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentView);
    }
    
    public ViewChildrenDataSource getChildElementsDataSource() {
        return new ViewChildrenDataSource(fCurrentView);
    }
    
    //extract relations from current view
    public ElementsDataSource getRelationsOfCurrentView() {
    	ElementsDataSource relationsElementsDataSource = new ElementsDataSource(fCurrentView.getArchimateModel(), "relations");
    	ViewChildrenDataSource viewChildren = new ViewChildrenDataSource(fCurrentView);
    	EList<IProperty> relationsProps = viewChildren.getPropertiesDataSourceWithRelations();
    	
    	List<IArchimateConcept> relations = new ArrayList<IArchimateConcept>();
    	for (int i = 0; i < relationsElementsDataSource.getConceptList().size(); i++) {
    		for (int j = 0; j < relationsProps.size(); j++) {
    			String relSource = ((ArchimateRelationship)relationsElementsDataSource.getConceptList().get(i)).getSource().getName();
    			String relTarget = ((ArchimateRelationship)relationsElementsDataSource.getConceptList().get(i)).getTarget().getName();
    			if(relSource.equals(relationsProps.get(j).getKey())) {
    				for (int k = 0; k < relationsProps.size(); k++) {
						if(relTarget.equals(relationsProps.get(k).getValue())) {
							//both source and target match: we want to print this relation 
							if(!relations.contains(relationsElementsDataSource.getConceptList().get(i))) {
								relations.add(relationsElementsDataSource.getConceptList().get(i));
								System.out.println(relSource + " -> " +relTarget);
							}
						}
					}
    				
    			}
			}
			
			
		}
    	//override the current list of concepts
    	relationsElementsDataSource.setConcepts(relations);
        return relationsElementsDataSource;
    }
    
    @Override
    public boolean next() throws JRException {
        if(currentIndex < fViews.size() - 1) {
            fCurrentView = fViews.get(++currentIndex);
        }
        else {
            fCurrentView = null;
        }
        
        return fCurrentView != null;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        String fieldName = jrField.getName();
        
        if("imagePath".equals(fieldName)) { //$NON-NLS-1$
            return getImagePath();
        }
        if("viewpoint".equals(fieldName) && fCurrentView instanceof IArchimateDiagramModel) { //$NON-NLS-1$
            return getViewpointName();
        }

        return FieldDataFactory.getFieldValue(fCurrentView, fieldName);
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }

    /**
     * Return the path to the diagram image
     */
    private String getImagePath() {
        String diagramName = fCurrentView.getId() + ".png"; //$NON-NLS-1$
        return System.getProperty("JASPER_IMAGE_PATH") + "/" + diagramName; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public Object getElement() {
        return fCurrentView;
    }
}
