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
    
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentView);
    }
    
    public ViewChildrenDataSource getChildElementsDataSource() {
        return new ViewChildrenDataSource(fCurrentView);
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
