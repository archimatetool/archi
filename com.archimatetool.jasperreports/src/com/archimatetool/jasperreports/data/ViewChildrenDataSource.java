/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.impl.Property;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * ViewChildrenDataSource - child ArchiMate type objects of a View
 * 
 * @author Phillip Beauvoir
 */
public class ViewChildrenDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    private List<IArchimateElement> fChildren = new ArrayList<IArchimateElement>();
    private IArchimateElement fCurrentElement;
    private int currentIndex = -1;

    public ViewChildrenDataSource(IDiagramModel dm) {
        getAllChildObjects(dm);
        ArchimateModelDataSource.sort(fChildren);
    }
    
    private void getAllChildObjects(IDiagramModelContainer container) {
        for(IDiagramModelObject child : container.getChildren()) {
            if(child instanceof IDiagramModelArchimateObject) {
                IArchimateElement element = ((IDiagramModelArchimateObject)child).getArchimateElement();
                if(element != null && !fChildren.contains(element)) {
                    fChildren.add(element);
                }
            }
            
            if(child instanceof IDiagramModelContainer) {
                getAllChildObjects((IDiagramModelContainer)child);
            }
        }
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fChildren.size() - 1) {
            fCurrentElement = fChildren.get(++currentIndex);
        }
        else {
            fCurrentElement = null;
        }
        
        return fCurrentElement != null;
    }

    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
    	return new PropertiesModelDataSource(fCurrentElement);
    }
    
    public EList<IProperty> getPropertiesDataSourceWithRelations() {
    	EList<IProperty> propList = new BasicEList<>();
    	for (int i = 0; i < fChildren.size(); i++) {
    		IArchimateElement elem = (IArchimateElement)fChildren.get(i);
    		EList<IArchimateRelationship> relations = elem.getSourceRelationships();
    		for (int x = 0; x < relations.size(); x++) {
    			propList.add(new Property("Source: "+relations.get(x).getSource().getName(),"Target: "+relations.get(x).getTarget().getName()));
    		}
		}
    	return propList;
    }
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fCurrentElement, jrField.getName());
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }

    @Override
    public Object getElement() {
        return fCurrentElement;
    }

}
