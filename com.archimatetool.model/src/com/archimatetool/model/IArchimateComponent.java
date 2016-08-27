/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.archimatetool.model.IArchimatePackage#getArchimateComponent()
 * @model abstract="true"
 * @generated
 */
public interface IArchimateComponent extends IArchimateModelElement, IIdentifier, ICloneable, INameable, IDocumentable, IProperties {
    
    /**
     * @return A list of source relationships connected to this component - this is a live list so don't change it!
     */
    EList<IRelationship> getSourceRelationships();

    /**
     * @return A list of target relationships connected to this component - this is a live list so don't change it!
     */
    EList<IRelationship> getTargetRelationships();

} // IArchimateComponent
