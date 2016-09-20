package com.archimatetool.editor.ui.factory;

import com.archimatetool.editor.ui.IGraphicsIcon;


/**
 * Interface for Diagram UI Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramModelUIProvider extends IObjectUIProvider {
    
    /**
     * @return A Graphics Icon that can be used to depict the Diagaram in a Figure
     */
    IGraphicsIcon getGraphicsIcon();

}
