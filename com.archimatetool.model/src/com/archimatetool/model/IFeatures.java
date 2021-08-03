/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Features</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.archimatetool.model.IFeatures#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see com.archimatetool.model.IArchimatePackage#getFeatures()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IFeatures extends EObject {
    
    /**
     * Returns the value of the '<em><b>Features</b></em>' containment reference list.
     * The list contents are of type {@link com.archimatetool.model.IFeature}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Features</em>' containment reference list.
     * @see com.archimatetool.model.IArchimatePackage#getFeatures_Features()
     * @model containment="true"
     *        extendedMetaData="name='feature' kind='element'"
     * @generated NOT
     */
    IFeaturesEList getFeatures();
    
    /**
     * @param msg The Notification message
     * @return true if the Notification message is a general IFeatures notification
     */
    public static boolean isFeatureNotification(Notification msg) {
        return msg.getFeature() == IArchimatePackage.Literals.FEATURES__FEATURES ||
                msg.getFeature() == IArchimatePackage.Literals.FEATURE__VALUE;
    }

    /**
     * @param msg The Notification message
     * @param name The name of the feature to check
     * @return true if the Notification message is an IFeatures notification of the given name
     */
    public static boolean isFeatureNotification(Notification msg, String name) {
        // Feature added or removed
        if(msg.getFeature() == IArchimatePackage.Literals.FEATURES__FEATURES) {
            // Added
            if(msg.getNewValue() instanceof IFeature) {
                return name.equals(((IFeature)msg.getNewValue()).getName());
            }
            // Removed
            if(msg.getOldValue() instanceof IFeature) {
                return name.equals(((IFeature)msg.getOldValue()).getName());
            }
        }
        
        // Feature value changed
        return msg.getFeature() == IArchimatePackage.Literals.FEATURE__VALUE
            && name.equals(((IFeature)msg.getNotifier()).getName());
    }

} // IFeatures
