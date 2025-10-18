/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;

/**
 * Abstract Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractGraphicalObjectUIProvider extends AbstractObjectUIProvider
implements IGraphicalObjectUIProvider {
    
    private static Color defaultColor = new Color(255, 255, 255);
    private static Color defaultLineColor = new Color(92, 92, 92);
    
    protected AbstractGraphicalObjectUIProvider() {
    }

    @Override
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    @Override
    public Color getDefaultLineColor() {
        return defaultLineColor;
    }
    
    @Override
    public Dimension getDefaultSize() {
        return new Dimension(-1, -1);
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_CENTER;
    }
    
    @Override
    public int getDefaultTextPosition() {
        return ITextPosition.TEXT_POSITION_TOP;
    }
    
    @Override
    public boolean hasIcon() {
        return false;
    }
    
    @Override
    public Object getDefaultFeatureValue(String featureName) {
        if(IDiagramModelObject.FEATURE_LINE_STYLE.equals(featureName)) {
            return IDiagramModelObject.LINE_STYLE_SOLID;
        }
        
        return super.getDefaultFeatureValue(featureName);
    }
    
    @Override
    public Object getFeatureValue(String featureName) {
        if(IDiagramModelObject.FEATURE_LINE_STYLE.equals(featureName) && getInstance() instanceof IDiagramModelObject dmo) {
            int value = dmo.getLineStyle();
            return (value == IDiagramModelObject.LINE_STYLE_DEFAULT) ? getDefaultFeatureValue(featureName) : value;
        }
        
        return super.getFeatureValue(featureName);
    }
}
