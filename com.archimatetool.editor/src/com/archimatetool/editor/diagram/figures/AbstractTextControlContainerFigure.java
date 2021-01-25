/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Abstract Container Figure with Text Control
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractTextControlContainerFigure extends AbstractContainerFigure implements ITextFigure {
    
    private IFigure fTextControl;
    private int fTextControlType = TEXT_FLOW_CONTROL;
    
    public static final int TEXT_FLOW_CONTROL = 0;
    public static final int LABEL_CONTROL = 1;
    
    private TextPositionDelegate fTextPositionDelegate;
    
    protected AbstractTextControlContainerFigure(int textControlType) {
        fTextControlType = textControlType;
    }
    
    protected AbstractTextControlContainerFigure(IDiagramModelObject diagramModelObject, int textControlType) {
        fTextControlType = textControlType;
        setDiagramModelObject(diagramModelObject);
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new DelegatingLayout());
        
        Locator textLocator = new Locator() {
            @Override
            public void relocate(IFigure target) {
                Rectangle bounds = calculateTextControlBounds();
                if(bounds == null) {
                    bounds = getBounds().getCopy();
                }
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };
        
        fTextControl = createTextControl(textLocator);
        
        Locator mainLocator = new Locator() {
            @Override
            public void relocate(IFigure target) {
                Rectangle bounds = getBounds().getCopy();
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };
        
        add(getMainFigure(), mainLocator);
        
        // If the model object is IIconic
        if(getDiagramModelObject() instanceof IIconic) {
            setIconicDelegate(new IconicDelegate((IIconic)getDiagramModelObject()));
        }
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
        
        // Line Color
        setLineColor();
        
        // Text Position
        if(getTextControl() instanceof TextFlow) {
            int alignment = getDiagramModelObject().getTextAlignment();
            ((FlowPage)getTextControl().getParent()).setHorizontalAligment(alignment);
            
            if(fTextPositionDelegate != null) {
                fTextPositionDelegate.updateTextPosition();
            }
        }
        
        repaint(); // repaint when figure changes
    }
    
    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        getTextControl().setEnabled(value);
        
        if(getFigureDelegate() != null) {
            getFigureDelegate().setEnabled(value);
        }
    }
    
    @Override
    public void setText() {
        String text = TextRenderer.getDefault().render(getDiagramModelObject(),
                                                       StringUtils.safeString(getDiagramModelObject().getName()));
                
        if(getTextControl() instanceof TextFlow) {
            ((TextFlow)getTextControl()).setText(text);
        }
        else if(getTextControl() instanceof Label) {
            ((Label)getTextControl()).setText(text);
        }
    }
    
    public String getText() {
        if(getTextControl() instanceof TextFlow) {
            return ((TextFlow)getTextControl()).getText();
        }
        else {
            return ((Label)getTextControl()).getText();
        }
    }
    
    @Override
    public IFigure getTextControl() {
        return fTextControl;
    }

    protected IFigure createTextControl(Locator textLocator) {
        if(fTextControlType == TEXT_FLOW_CONTROL) {
            return createTextFlowControl(textLocator);
        }
        else {
            return createLabelControl(textLocator);
        }
    }
    
    protected TextFlow createTextFlowControl(Locator textLocator) {
        TextFlow textFlow = new TextFlow();
        
        int wordWrapStyle = Preferences.STORE.getInt(IPreferenceConstants.ARCHIMATE_FIGURE_WORD_WRAP_STYLE);
        textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, wordWrapStyle));
        
        FlowPage page = new FlowPage();
        page.add(textFlow);
        
        Figure textWrapperFigure = new Figure();
        GridLayout layout = new GridLayout();
        layout.marginWidth = getTextControlMarginWidth();
        layout.marginHeight = 5;
        textWrapperFigure.setLayoutManager(layout);
        GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, true);
        textWrapperFigure.add(page, gd);
        
        if(getDiagramModelObject() instanceof ITextPosition) {
            fTextPositionDelegate = new TextPositionDelegate(textWrapperFigure, page, (ITextPosition)getDiagramModelObject());
        }
        
        add(textWrapperFigure, textLocator);
        //add(page, textLocator);
        
        return textFlow;
    }

    protected Label createLabelControl(Locator textLocator) {
        Label label = new Label(""); //$NON-NLS-1$
        add(label, textLocator);
        return label;
    }

    /**
     * @return the left and right margin width for text
     */
    protected int getTextControlMarginWidth() {
        return 5;
    }
    
    protected int getIconOffset() {
        return 0;
    }

    /**
     * Calculate the Text Control Bounds or null if none.
     */
    protected Rectangle calculateTextControlBounds() {
        // Delegate
        if(getFigureDelegate() != null) {
            return getFigureDelegate().calculateTextControlBounds();
        }
        
        // If there is no icon we don't need to do any offsets
        if(getIconOffset() == 0) {
            return null;
        }
        
        Rectangle bounds = getBounds().getCopy();

        // Adjust for left/right margin
        int iconOffset = getIconOffset() - getTextControlMarginWidth();

        int textpos = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        int textAlignment = getDiagramModelObject().getTextAlignment();
        
        if(textpos == ITextPosition.TEXT_POSITION_TOP) {
            if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                bounds.x += iconOffset;
                bounds.width = bounds.width - (iconOffset * 2);
            }
            else if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                bounds.width -= iconOffset;
            }
        }
        
        return bounds;
    }
    

}