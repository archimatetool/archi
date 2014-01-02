/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.model.viewpoints.ViewpointsManager;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;

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
    
    // Viewpoint names
    static Map<Integer, String> viewpointsMap = new HashMap<Integer, String>();
    static {
        for(IViewpoint vp : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            viewpointsMap.put(vp.getIndex(), vp.getName());
        }
    }
    
    public ViewModelDataSource(IArchimateModel model) {
        // Sort a *copy* of the List
        fViews = new ArrayList<IDiagramModel>(model.getDiagramModels());
        ArchimateModelDataSource.sort(fViews);
    }
    
    public String getViewpointName() {
        if(fCurrentView instanceof IArchimateDiagramModel) {
            int index = ((IArchimateDiagramModel)fCurrentView).getViewpoint();
            return NLS.bind(Messages.ViewModelDataSource_0, viewpointsMap.get(index));
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
