/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Abstract Figure for containing a IDiagramModelObject
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramModelObjectFigure extends Figure
implements IDiagramModelObjectFigure {
    
    private IDiagramModelObject fDiagramModelObject;
    
    private Color fFillColor;
    private Color fFontColor;
    private Color fLineColor;
    
    
    // Delegate to do drawing
    private IFigureDelegate fFigureDelegate;
    
    protected AbstractDiagramModelObjectFigure() {
    }
    
    protected AbstractDiagramModelObjectFigure(IDiagramModelObject diagramModelObject){
        setDiagramModelObject(diagramModelObject);
    }

    @Override
    public void setDiagramModelObject(IDiagramModelObject diagramModelObject) {
        fDiagramModelObject = diagramModelObject;
        setUI();
    }
    
    @Override
    public IDiagramModelObject getDiagramModelObject() {
        return fDiagramModelObject;
    }
    
    public IFigureDelegate getFigureDelegate() {
        return fFigureDelegate;
    }
    
    public void setFigureDelegate(IFigureDelegate figureDelegate) {
        fFigureDelegate = figureDelegate;
    }
    
    /**
     * Draw the figure.
     * The default behaviour is to delegate to the Figure Delegate if one is set.
     * @param graphics
     */
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
        }
    }
    
    /**
     * Set the drawing state when disabled
     * @param graphics
     */
    protected void setDisabledState(Graphics graphics) {
        graphics.setAlpha(100);
        graphics.setLineStyle(SWT.LINE_DOT);
    }

    /**
     * Set the UI
     */
    abstract protected void setUI();
    
    /**
     * Set the font to that in the model, or failing that, as per user's default
     */
    protected void setFont() {
        String fontName = fDiagramModelObject.getFont();
        setFont(FontFactory.get(fontName));
        
        // Need to do this after font change
        if(getTextControl() != null) {
            getTextControl().revalidate();
        }
    }
    
    @Override
    public void setFont(Font f) {
        if(PlatformUtils.isWindows()) {
            f = FontFactory.getAdjustedWindowsFont(f);
        }
        
        super.setFont(f);
    }
    
    /**
     * Set the fill color to that in the model, or failing that, as per default
     */
    protected void setFillColor() {
        String val = fDiagramModelObject.getFillColor();
        fFillColor = ColorFactory.get(val);
    }
    
    /**
     * @return The Fill Color to use
     */
    @Override
    public Color getFillColor() {
        if(fFillColor == null) {
            return ColorFactory.getDefaultFillColor(fDiagramModelObject);
        }
        return fFillColor;
    }
    
    /**
     * Set the font color to that in the model, or failing that, as per default
     */
    protected void setFontColor() {
        String val = fDiagramModelObject.getFontColor();
        Color c = ColorFactory.get(val);
        if(c == null) {
            c = ColorConstants.black; // Set to black in case of dark theme
        }
        if(c != fFontColor) {
            fFontColor = c;
            if(getTextControl() != null) {
                getTextControl().setForegroundColor(c);
            }
        }
    }
    
    
    /**
     * Set the line color to that in the model, or failing that, as per default
     */
    protected void setLineColor() {
        String val = fDiagramModelObject.getLineColor();
        fLineColor = ColorFactory.get(val);
    }
    
    /**
     * @return The Line Color to use
     */
    @Override
    public Color getLineColor() {
        // User preference to derive element line colour
        if(Preferences.STORE.getBoolean(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR)) {
            return ColorFactory.getDarkerColor(getFillColor(),
                    Preferences.STORE.getInt(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR_FACTOR) / 10f);
        }
        
        if(fLineColor == null) {
            return ColorFactory.getDefaultLineColor(getDiagramModelObject());
        }
        return fLineColor;
    }
    
    protected int getAlpha() {
        return fDiagramModelObject.getAlpha();
    }

    protected int getLineAlpha() {
        return fDiagramModelObject.getLineAlpha();
    }
    
    protected int getGradient() {
        return fDiagramModelObject.getGradient();
    }

    @Override
    public IFigure getToolTip() {
        if(!Preferences.doShowViewTooltips()) {
            return null;
        }
        
        ToolTipFigure toolTipFigure = (ToolTipFigure)super.getToolTip();
        
        if(toolTipFigure == null) {
            toolTipFigure = new ToolTipFigure();
            setToolTip(toolTipFigure);
        }
        
        // Set text to object's default text
        String text = ArchiLabelProvider.INSTANCE.getLabel(getDiagramModelObject());
        toolTipFigure.setText(text);
        
        // If an ArchiMate type, set text to element type if blank
        if(fDiagramModelObject instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)fDiagramModelObject).getArchimateElement();
            String type = ArchiLabelProvider.INSTANCE.getDefaultName(element.eClass());
            if(!StringUtils.isSet(text)) { // Name was blank
                toolTipFigure.setText(type);
            }
            toolTipFigure.setType(Messages.AbstractDiagramModelObjectFigure_0 + " " + type); //$NON-NLS-1$
        }

        return toolTipFigure;
    }

    @Override
    public boolean didClickTextControl(Point requestLoc) {
        IFigure figure = getTextControl();
        if(figure != null) {
            figure.translateToRelative(requestLoc);
            return figure.containsPoint(requestLoc);
        }
        return false;
    }
    
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return getDefaultSize();
    }
    
    @Override
    public Dimension getDefaultSize() {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(getDiagramModelObject());
        return provider != null ? provider.getUserDefaultSize() : IGraphicalObjectUIProvider.DefaultRectangularSize;
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new ChopboxAnchor(this);
    }

    @Override
    public void dispose() {
    }
}