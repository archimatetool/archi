/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Abstract Figure with Label
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractLabelFigure extends AbstractDiagramModelObjectFigure {
    
    private Label fLabel;

    protected AbstractLabelFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new DelegatingLayout());
        
        Locator labelLocator = new Locator() {
            @Override
            public void relocate(IFigure target) {
                Rectangle bounds = calculateTextControlBounds();
                if(bounds != null) {
                    translateFromParent(bounds);
                    target.setBounds(bounds);
                }
            }
        };
        
        add(getLabel(), labelLocator);
    }
    
    @Override
    public void refreshVisuals() {
        // Text
        setText();
        
        // Font
        setFont();
        
        // Fill Color
        setFillColor();
        
        // Font Color
        setFontColor();
        
        repaint(); // repaint when figure changes
    }
    
    protected void setText() {
        String text = StringUtils.safeString(getDiagramModelObject().getName());
        getLabel().setText(text);
    }
    
    public Label getLabel() {
        if(fLabel == null) {
            fLabel = new Label(""); //$NON-NLS-1$
        }
        return fLabel;
    }

    @Override
    public IFigure getTextControl() {
        return getLabel();
    }
    
    /**
     * Calculate the Text Control Bounds or null if none.
     * The Default is to delegate to the Figure Delegate.
     */
    protected Rectangle calculateTextControlBounds() {
        if(getFigureDelegate() != null) {
            return getFigureDelegate().calculateTextControlBounds();
        }
        return null;
    }
}