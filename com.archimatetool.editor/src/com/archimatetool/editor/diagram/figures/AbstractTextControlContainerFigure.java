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

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
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
            
            // Update Grid Layout
            GridLayout layout = (GridLayout)getTextControl().getParent().getParent().getLayoutManager();
            layout.marginWidth = getTextControlMarginWidth();
            layout.marginHeight = getTextControlMarginHeight();
        }

        // Icon Image
        updateIconImage();
        
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
        
        int wordWrapStyle = ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.ARCHIMATE_FIGURE_WORD_WRAP_STYLE);
        textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, wordWrapStyle));
        
        FlowPage page = new FlowPage();
        page.add(textFlow);
        
        Figure textWrapperFigure = new Figure();
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = getTextControlMarginWidth();
        layout.marginHeight = getTextControlMarginHeight();
        textWrapperFigure.setLayoutManager(layout);

        textWrapperFigure.add(page, new GridData(SWT.CENTER, SWT.TOP, true, true));
        
        if(getDiagramModelObject() instanceof ITextPosition) {
            fTextPositionDelegate = new TextPositionDelegate(textWrapperFigure, page, (ITextPosition)getDiagramModelObject());
        }
        
        add(textWrapperFigure, textLocator);
        //add(page, textLocator);
        
        return textFlow;
    }
    
    /**
     * Adjust the figure's position in relation to the position of the text control
     * Assumes a square target working area.
     * @param rect the working area of the figure to modify
     */
    protected void setFigurePositionFromTextPosition(Rectangle rect) {
    	setFigurePositionFromTextPosition(rect, 1);
    }
    
    /**
     * Adjust the figure's position in relation to the position of the text control
     * @param rect the working area of the figure to modify
     * @param ratio the width/height ratio of the target working area
     */
    protected void setFigurePositionFromTextPosition(Rectangle rect, double ratio) {
        if(StringUtils.isSetAfterTrim(getText())) { // If there is text to display...
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            int textAlignment = getDiagramModelObject().getTextAlignment();
            
            // Try to fit an icon of the same height into rect. If not possible then use the same width.
            Rectangle newIconPosition = rect.getCopy();
            if((rect.height * ratio) <= rect.width) {
            	// Same height, so some room on left/right
            	newIconPosition.width = (int) (rect.height * ratio);
            	
            	switch(textAlignment) {
	            	// By default, icon will be on the (top) left which matches TEXT_ALIGNMENT_RIGHT
	                case ITextAlignment.TEXT_ALIGNMENT_CENTER:
	                	newIconPosition.x += (rect.width - newIconPosition.width) / 2;
	                	break;
	                case ITextAlignment.TEXT_ALIGNMENT_LEFT:
	                	newIconPosition.x += rect.width - newIconPosition.width;
	                    break;
	                default:
	                    break;
	            }
            } else {
            	// Same width, so some room on top/below
            	newIconPosition.height = (int) (rect.width / ratio);
            	
            	switch(textPosition) {
	            	case ITextPosition.TEXT_POSITION_TOP:
	            		newIconPosition.y += rect.height - newIconPosition.height;
	                	break;
	                // By default, icon will be on the (top) left which matches TEXT_POSITION_BOTTOM
	                case ITextPosition.TEXT_POSITION_CENTRE:
	                	newIconPosition.y += (rect.height - newIconPosition.height) / 2;
	                	break;
	                default:
	                    break;
	            }
            }
                
            rect.setBounds(newIconPosition);
        }
    }
    
    /**
     * @return true if the figure size should be constrained by the position of the text
     */
    protected boolean shouldConstrainFigureForTextPosition(int textPosition) {
        return true;
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
        return 4;
    }
    
    /**
     * @return the top and bottom margin height for text
     */
    protected int getTextControlMarginHeight() {
        return 4;
    }
    
    /**
     * Calculate the Text Control Bounds or null if none.
     */
    protected Rectangle calculateTextControlBounds() {
        // Check Delegate first
        if(getFigureDelegate() != null) {
            Rectangle rect = getFigureDelegate().calculateTextControlBounds();
            if(rect != null) {
                return rect;
            }
        }
        
        // We will adjust for any internal icons...
        
        // If there is no icon offset or the icon is not visible we don't need to do any offsets
        if(getIconOffset() == 0 || !isIconVisible()) {
            return null;
        }
        
        // Adjust for icon offset and left/right margins
        int iconOffset = getIconOffset() - getTextControlMarginWidth();

        Rectangle rect = getBounds().getCopy();

        // Text position is Top
        if(((ITextPosition)getDiagramModelObject()).getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
            int textAlignment = getDiagramModelObject().getTextAlignment();

            if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                rect.x += iconOffset;
                rect.width = rect.width - (iconOffset * 2);
            }
            else if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                rect.width -= iconOffset;
            }
        }
        
        return rect;
    }
}