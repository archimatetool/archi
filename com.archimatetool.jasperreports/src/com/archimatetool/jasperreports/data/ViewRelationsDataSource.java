/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;


/**
 * ViewRelationsDataSource - child ArchiMate type relations in a View
 * 
 * @author Phillip Beauvoir
 */
public class ViewRelationsDataSource implements JRRewindableDataSource, IPropertiesDataSource {
    
    private List<IArchimateRelationship> fRelations = new ArrayList<IArchimateRelationship>();
    private IArchimateRelationship fCurrentRelation;
    private int currentIndex = -1;

    public ViewRelationsDataSource(IDiagramModel dm) {
        getAllRelationsInView(dm);
        ArchimateModelDataSource.sort(fRelations);
    }
    
    private void getAllRelationsInView(IDiagramModel dm) {
        for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateConnection) {
                IArchimateRelationship relation = ((IDiagramModelArchimateConnection)eObject).getArchimateRelationship();
                if(!fRelations.contains(relation)) {
                    fRelations.add(relation);
                }
            }
        }
    }

    @Override
    public boolean next() throws JRException {
        if(currentIndex < fRelations.size() - 1) {
            fCurrentRelation = fRelations.get(++currentIndex);
        }
        else {
            fRelations = null;
        }
        
        return fRelations != null;
    }

    @Override
    public PropertiesModelDataSource getPropertiesDataSource() {
        return new PropertiesModelDataSource(fCurrentRelation);
    }
    
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return FieldDataFactory.getFieldValue(fCurrentRelation, jrField.getName());
    }

    @Override
    public void moveFirst() throws JRException {
        currentIndex = -1;
    }

    @Override
    public Object getElement() {
        return fCurrentRelation;
    }

}
