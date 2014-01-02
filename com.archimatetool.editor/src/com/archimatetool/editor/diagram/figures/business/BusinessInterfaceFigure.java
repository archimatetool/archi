/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigure;
import com.archimatetool.editor.diagram.figures.EllipseFigureDelegate;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IInterfaceElement;


/**
 * Figure for a Business Interface
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceFigure
extends AbstractTextFlowFigure {
    
    protected IFigureDelegate fRectangleDelegate, fEllipseDelegate;
    
    public BusinessInterfaceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fRectangleDelegate = new RectangleFigureDelegate(this) {
            @Override
            public Image getImage() {
                IInterfaceElement element = (IInterfaceElement)((IDiagramModelArchimateObject)getDiagramModelObject()).getArchimateElement();
                return element.getInterfaceType() == IInterfaceElement.PROVIDED ? IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_INTERFACE_16)
                        : IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_INTERFACE_REQUIRED_16);
            }
        };
        
        fEllipseDelegate = new EllipseFigureDelegate(this);
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        repaint(); // redraw icon and delegate
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = ((IDiagramModelArchimateObject)getDiagramModelObject()).getType();
        return type == 0 ? fRectangleDelegate : fEllipseDelegate;
    }
    
    @Override
    public Dimension getDefaultSize() {
        int type = ((IDiagramModelArchimateObject)getDiagramModelObject()).getType();
        return type == 0 ? super.getDefaultSize() : new Dimension(60, 60);
    }
}