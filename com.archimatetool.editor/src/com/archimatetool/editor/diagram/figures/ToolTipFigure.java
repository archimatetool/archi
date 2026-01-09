/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.resource.JFaceResources;


/**
 * Tooltip Figure
 * 
 * @author Phillip Beauvoir
 */
public class ToolTipFigure extends Figure {
    
    private TextFlow nameText, rubricText;
    private Label typeLabel;

    public ToolTipFigure() {
        setBorder(new MarginBorder(3));
        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(false);
        setLayoutManager(layout);
        nameText = createTextFlow();
    }
    
    public ToolTipFigure(String text) {
        this();
        setText(text);
    }

    public void setText(String text) {
        nameText.setText(text);
    }

    public void setType(String type) {
        if(typeLabel == null) {
            typeLabel = new Label();
            add(typeLabel);
            nameText.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT)); // Name becomes bold if type is set
        }
        typeLabel.setText(type);
    }
    
    public void setRubric(String text) {
        if(rubricText == null) {
            rubricText = createTextFlow();
            rubricText.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));
        }
        rubricText.setText(text);
    }
    
    private TextFlow createTextFlow() {
        TextFlow textFlow = new TextFlow();
        textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_HARD));
        
        FlowPage page = new FlowPage() {
            @Override
            public Dimension getPreferredSize(int wHint, int hHint) {
                if(textFlow.getText().length() == 0) {
                    return new Dimension();
                }
                
                Dimension d = FigureUtilities.getTextExtents(textFlow.getText(), textFlow.getFont());
                if(d.width > 400) {
                    d = super.getPreferredSize(400, -1);
                }
                return d;
            }
        };
        
        page.add(textFlow);
        add(page);
        
        return textFlow;
    }
}