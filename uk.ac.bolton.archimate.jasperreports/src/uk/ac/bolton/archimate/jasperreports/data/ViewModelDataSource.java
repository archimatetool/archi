/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import uk.ac.bolton.archimate.editor.model.viewpoints.IViewpoint;
import uk.ac.bolton.archimate.editor.model.viewpoints.ViewpointsManager;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * View Model DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ViewModelDataSource implements JRRewindableDataSource {
    
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
            return viewpointsMap.get(index) + " viewpoint";
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
        
        if("name".equals(fieldName)) {
            return fCurrentView.getName();
        }
        if("imagePath".equals(fieldName)) {
            return getImagePath();
        }
        if("viewpoint".equals(fieldName) && fCurrentView instanceof IArchimateDiagramModel) {
            return getViewpointName();
        }
        if("documentation".equals(fieldName)) {
            String s = fCurrentView.getDocumentation();
            return StringUtils.isSet(s) ? s : null;
        }
        return null;
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
}
