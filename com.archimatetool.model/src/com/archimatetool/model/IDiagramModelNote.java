/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Note</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelNote()
 * @model extendedMetaData="name='Note'"
 * @generated
 */
@SuppressWarnings("nls")
public interface IDiagramModelNote extends IDiagramModelObject, ITextContent, ITextPosition, IProperties, IBorderType, IIconic {

    int BORDER_DOGEAR = 0; // Default
    int BORDER_RECTANGLE = 1;
    int BORDER_NONE = 2;
    
    String LEGEND_MODEL_NAME = "DiagramModelLegend";
    String FEATURE_LEGEND = "legend";
    
    /**
     * @return true if this note is used to display a legend
     */
    boolean isLegend();
    
    /**
     * Set this note to be a legend or not a legend
     */
    void setIsLegend(boolean isLegend);
    
    /**
     * Set Legend Options and set the Feature
     */
    void setLegendOptions(ILegendOptions options);
    
    /**
     * @return Legend Options from the Feature
     */
    ILegendOptions getLegendOptions();

} // IDiagramModelNote
