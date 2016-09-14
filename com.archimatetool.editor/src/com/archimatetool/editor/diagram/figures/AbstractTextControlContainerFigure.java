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
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;

import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;



/**
 * Abstract Container Figure with Text Control
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractTextControlContainerFigure extends AbstractContainerFigure {
    
    private IFigure fTextControl;
    private int fTextControlType = TEXT_FLOW_CONTROL;
    
    public static final int TEXT_FLOW_CONTROL = 0;
    public static final int LABEL_CONTROL = 1;
    
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
            public void relocate(IFigure target) {
                Rectangle bounds = calculateTextControlBounds();
                if(bounds != null) {
                    translateFromParent(bounds);
                    target.setBounds(bounds);
                }
            }
        };
        
        fTextControl = createTextControl(textLocator);
        
        Locator mainLocator = new Locator() {
            public void relocate(IFigure target) {
                Rectangle bounds = getBounds().getCopy();
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };
        
        add(getMainFigure(), mainLocator);

        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(getMainFigure());
    }
    
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
        
        // Alignment (default is CENTER)
        if(getTextControl() instanceof TextFlow) {
            int alignment = getDiagramModelObject().getTextAlignment();
            if(alignment == ITextAlignment.TEXT_ALIGNMENT_NONE) {
                alignment = ITextAlignment.TEXT_ALIGNMENT_CENTER;
            }
            ((BlockFlow)getTextControl().getParent()).setHorizontalAligment(alignment);
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
    
    protected void setText() {
        String text = StringUtils.safeString(getDiagramModelObject().getName());
        
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
    
    public IFigure getTextControl() {
        return fTextControl;
    }

    /**
     * Calculate the Text Contrl Bounds or null if none.
     * The Default is to delegate to the Figure Delegate.
     */
    protected Rectangle calculateTextControlBounds() {
        if(getFigureDelegate() != null) {
            return getFigureDelegate().calculateTextControlBounds();
        }
        return null;
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
        
        BlockFlow block = new BlockFlow();
        block.add(textFlow);

        FlowPage page = new FlowPage();
        page.add(block);
        
        add(page, textLocator);
        
        return textFlow;
    }

    protected Label createLabelControl(Locator textLocator) {
        Label label = new Label(""); //$NON-NLS-1$
        add(label, textLocator);
        return label;
    }

}