/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import org.eclipse.emf.common.util.EList;

import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;



/**
 * Properties Model DataSource
 * 
 * @author Phillip Beauvoir
 */
public class PropertiesModelDataSource implements JRRewindableDataSource, IDataSource {
    
    private EList<IProperty> fProperties;
    private IProperty fCurrentProperty;
    private int currentIndex = -1;

    public PropertiesModelDataSource(IProperties properties) {
        fProperties = properties.getProperties();
    }
    
    public PropertiesModelDataSource(EList<IProperty> properties) {
        fProperties = properties;
    }
    
    @Override
    public boolean next() throws JRException {
        if(currentIndex < fProperties.size() - 1) {
            fCurrentProperty = fProperties.get(++currentIndex);
        }
        else {
            fCurrentProperty = null;
        }
        
        return fCurrentProperty != null;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        String fieldName = jrField.getName();
        
        if("key".equals(fieldName)) { //$NON-NLS-1$
            return fCurrentProperty.getKey();
        }
        if("value".equals(fieldName)) { //$NON-NLS-1$
            return fCurrentProperty.getValue();
        }
        return null;
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }

    @Override
    public Object getElement() {
        return fCurrentProperty;
    }
}
