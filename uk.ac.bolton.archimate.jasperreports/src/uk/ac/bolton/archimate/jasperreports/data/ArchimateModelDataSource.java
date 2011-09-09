/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.INameable;


/**
 * Archimate Model DataSource
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelDataSource implements JRDataSource {
    
    private IArchimateModel fModel;
    private boolean done;
    
    public ArchimateModelDataSource(IArchimateModel model) {
        fModel = model;
    }
    
    public IArchimateModel getModel() {
        return fModel;
    }
    
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fModel);
    }
    
    public ViewModelDataSource getViewsDataSource() {
        return new ViewModelDataSource(fModel);
    }
    
    public ElementsDataSource getElementsDataSource(String type) {
        return new ElementsDataSource(fModel, type);
    }

    @Override
    public boolean next() throws JRException {
        if(!done) {
            done = true;
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        String fieldName = jrField.getName();
        
        if("name".equals(fieldName)) {
            return fModel.getName();
        }
        if("purpose".equals(fieldName)) {
            String s = fModel.getPurpose();
            return StringUtils.isSet(s) ? s : null;
        }
        return null;
    }

    
    /**
     * Sort a List by Name
     */
    public static void sort(List<?> list) {
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof INameable && o2 instanceof INameable) {
                    String name1 = StringUtils.safeString(((INameable)o1).getName()).toLowerCase().trim();
                    String name2 = StringUtils.safeString(((INameable)o2).getName()).toLowerCase().trim();
                    return name1.compareTo(name2);
                }
                return 0;
            }
        });
    }

}
