/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Abstract Archimate Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateElementUIProvider extends AbstractGraphicalObjectUIProvider
implements IArchimateElementUIProvider {
    
    protected static Color defaultBusinessColor = new Color(255, 255, 181);
    protected static Color defaultApplicationColor = new Color(181, 255, 255);
    protected static Color defaultTechnologyColor = new Color(201, 231, 183);
    
    protected static Color defaultMotivationColor = new Color(204, 204, 255);
    protected static Color defaultStrategyColor = new Color(245, 222, 170);
    protected static Color defaultImplMigrationColor1 = new Color(255, 224, 224);
    protected static Color defaultImplMigrationColor2 = new Color(224, 255, 224);
    
    protected AbstractArchimateElementUIProvider() {
    }
    
    @Override
    public Dimension getDefaultSize() {
        // If we have an instance, get the default size for its figure type, else default user preference default size
        return instance != null ? getDefaultSizeForFigureType(((IDiagramModelArchimateObject)instance).getType()) : getDefaultUserPreferenceSize();
    }
    
    @Override
    public boolean hasAlternateFigure() {
        return true;
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT);
    }
    
    @Override
    public int getDefaultTextPosition() {
        return ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION);
    }

    @Override
    public boolean hasIcon() {
        return true;
    }
    
    /**
     * @return the default size for the given figure type
     */
    protected Dimension getDefaultSizeForFigureType(int figureType) {
        return getDefaultUserPreferenceSize();
    }
    
    /**
     * @return The default figure size from Preferences
     */
    protected static Dimension getDefaultUserPreferenceSize() {
        return new Dimension(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH),
                             ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT));
    }

    /**
     * @return a square size based on the smallest default width or height of user preferences default size
     */
    protected static Dimension getDefaultSquareSize() {
        Dimension d = getDefaultUserPreferenceSize();
        int length = Math.min(d.width, d.height);
        return new Dimension(length, length);
    }
    
    /**
     * @return a default size with a minimum width
     */
    protected static Dimension getDefaultSizeWithMinumumWidth(int minWidth) {
        Dimension d = getDefaultSquareSize();
        d.width = Math.max(d.width, minWidth);
        return d;
    }
    
}
