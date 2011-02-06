/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;

import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Abstract Figure with Editable Label
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractEditableLabelFigure extends AbstractDiagramModelObjectFigure
implements IEditableLabelFigure {
    
    private Label fLabel;

    public AbstractEditableLabelFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new DelegatingLayout());
        
        Locator locator = new Locator() {
            public void relocate(IFigure target) {
                target.setBounds(calculateLabelBounds());
            }
        };
        
        add(getLabel(), locator);
    }
    
    public void refreshVisuals() {
        // Text
        setText();
        
        // Tooltip
        setToolTip();
        
        // Font
        setFont();
        
        // Fill Color
        setFillColor();
        
        // Font Color
        setFontColor();
    }
    
    protected void setText() {
        String text = StringUtils.safeString(fDiagramModelObject.getName());
        getLabel().setText(text);
    }
    
    public Label getLabel() {
        if(fLabel == null) {
            fLabel = new Label(""); //$NON-NLS-1$
        }
        return fLabel;
    }

    public boolean didClickLabel(Point requestLoc) {
        Label nameLabel = getLabel();
        nameLabel.translateToRelative(requestLoc);
        return nameLabel.containsPoint(requestLoc);
    }
    
    @Override
    public IFigure getTextControl() {
        return getLabel();
    }
}