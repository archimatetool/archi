/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
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
@SuppressWarnings("nls")
public class ViewModelDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    private List<IDiagramModel> fViews;
    private IDiagramModel fCurrentView;
    private int currentIndex = -1;
    
    public ViewModelDataSource(IArchimateModel model) {
        this(model.getDiagramModels());
    }
    
    public ViewModelDataSource(List<IDiagramModel> diagramModels) {
        // Use a *copy* of the List
        fViews = new ArrayList<IDiagramModel>(diagramModels);
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
            return name == null ? "" : NLS.bind(Messages.ViewModelDataSource_0, name);
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
    
    public ViewChildrenDataSource getChildElementsDataSourceForTypes(String types) {
        return new ViewChildrenDataSource(fCurrentView, types);
    }

    public ViewChildrenDataSource getChildElementsDataSourceSortedByType(boolean sortFirstByType) {
        return new ViewChildrenDataSource(fCurrentView, sortFirstByType);
    }

    public ViewChildrenDataSource getChildElementsDataSourceForTypesSortedByType(String types, boolean sortFirstByType) {
        return new ViewChildrenDataSource(fCurrentView, types, sortFirstByType);
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
        
        if(FieldDataFactory.IMAGE_PATH.equals(fieldName)) {
            return getImagePath();
        }
        if(FieldDataFactory.VIEWPOINT.equals(fieldName) && fCurrentView instanceof IArchimateDiagramModel) {
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
        String diagramName = fCurrentView.getId() + ".png";
        return System.getProperty("JASPER_IMAGE_PATH") + "/" + diagramName;
    }

    @Override
    public IDiagramModel getElement() {
        return fCurrentView;
    }
    
    public int size() {
        return fViews.size();
    }

}
