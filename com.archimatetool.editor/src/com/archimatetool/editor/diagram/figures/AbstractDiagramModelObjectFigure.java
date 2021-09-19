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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.ImageFactory;
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
    
    // Use line width offset handling
    boolean useLineOffset = ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.USE_FIGURE_LINE_OFFSET);
    
    private IDiagramModelObject fDiagramModelObject;
    
    private Color fFillColor;
    private Color fFontColor;
    private Color fLineColor;
    
    
    // Delegate to do drawing
    private IFigureDelegate fFigureDelegate;
    
    // Delegate to draw icon image
    private IconicDelegate fIconicDelegate;
    
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
     * Set the line width and compensate the figure bounds width and height for this line width and translate the graphics instance
     * @param graphics The graphics instance
     * @param lineWidth The line width
     * @param bounds The bounds of the object
     */
    protected void setLineWidth(Graphics graphics, int lineWidth, Rectangle bounds) {
        graphics.setLineWidth(lineWidth);
        
        final double scale = FigureUtils.getGraphicsScale(graphics);
        
        // If line width is 1 and scale is 100% and don't use offset then do nothing
        if(lineWidth == 1 && scale == 1.0 && !useLineOffset) {
            return;
        }
    
        // Width and height reduced by line width to compensate for x,y offset
        bounds.width -= lineWidth;
        bounds.height -= lineWidth;
        
        // x,y offset is half of line width
        float offset = (float)lineWidth / 2;
        
        // If this is a non hi-res device and scale == 100% round up to integer to stop anti-aliasing
        if(ImageFactory.getDeviceZoom() == 100 && scale == 1.0) {
            offset = (float)Math.ceil(offset);
        }
        
        graphics.translate(offset, offset);
    }
    
    /**
     * Set the drawing state when disabled
     * @param graphics
     */
    protected void setDisabledState(Graphics graphics) {
        //graphics.setLineStyle(SWT.LINE_DASH);
        graphics.setLineDash(new int[] { 4, 3 });
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
     * Reset the fill color
     */
    protected void setFillColor() {
        fFillColor = null;
    }
    
    /**
     * @return The Fill Color to use
     */
    @Override
    public Color getFillColor() {
        if(fFillColor == null) {
            fFillColor = ColorFactory.get(fDiagramModelObject.getFillColor());
            
            // Use default fill color
            if(fFillColor == null) {
                fFillColor = ColorFactory.getDefaultFillColor(fDiagramModelObject);
            }
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
     * Reset the line color
     */
    protected void setLineColor() {
        fLineColor = null;
    }
    
    /**
     * @return The Line Color to use
     */
    @Override
    public Color getLineColor() {
        if(fLineColor == null) {
            // User preference to derive element line colour
            if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR)) {
                fLineColor = ColorFactory.getDarkerColor(getFillColor(),
                        ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR_FACTOR) / 10f);
            }
            else {
                fLineColor = ColorFactory.get(fDiagramModelObject.getLineColor());
                
                // Use default line color
                if(fLineColor == null) {
                    fLineColor = ColorFactory.getDefaultLineColor(getDiagramModelObject());
                }
            }
        }
        
        return fLineColor;
    }
    
    protected int getAlpha() {
        return isEnabled() ? fDiagramModelObject.getAlpha() : Math.min(100, fDiagramModelObject.getAlpha());
    }

    protected int getLineAlpha() {
        return isEnabled() ? fDiagramModelObject.getLineAlpha() : 100;
    }
    
    protected int getGradient() {
        return fDiagramModelObject.getGradient();
    }
    
    @Override
    public void updateIconImage() {
        if(getIconicDelegate() != null) {
            getIconicDelegate().updateImage();
        }
    }
    
    /**
     * If there is a delegate, draw the icon image in the given area
     */
    public void drawIconImage(Graphics graphics, Rectangle drawArea) {
        if(hasIconImage()) {
            getIconicDelegate().drawIcon(graphics, drawArea); // Call this directly in case offsets are set elsewhere
        }
    }
    
    /**
     * If there is a delegate, draw the icon image in the given area with given offsets
     */
    public void drawIconImage(Graphics graphics, Rectangle drawArea, int topOffset, int rightOffset, int bottomOffset, int leftOffset) {
        drawIconImage(graphics, drawArea, drawArea, topOffset, rightOffset, bottomOffset, leftOffset);
    }

    /**
     * If there is a delegate, draw the icon image in the given area with given offsets and pass full figure bounds
     */
    public void drawIconImage(Graphics graphics, Rectangle figureBounds, Rectangle drawArea, int topOffset, int rightOffset, int bottomOffset, int leftOffset) {
        if(hasIconImage()) {
            getIconicDelegate().setOffsets(topOffset, rightOffset, bottomOffset, leftOffset);
            getIconicDelegate().drawIcon(graphics, figureBounds, drawArea);
        }
    }

    /**
     * @return true if this has a delegate and an image to draw
     */
    public boolean hasIconImage() {
        return getIconicDelegate() != null && getIconicDelegate().hasImage();
    }
    
    /**
     * Set the IconicDelegate if this figure draws icons
     */
    public void setIconicDelegate(IconicDelegate delegate) {
        fIconicDelegate = delegate;
    }
    
    /**
     * @return The IconicDelegate if this figure draws icons, or null if not
     */
    public IconicDelegate getIconicDelegate() {
        return fIconicDelegate;
    }
    
    /**
     * @return whether to show the small in-built icon - either the ArchiMate icon or the view reference icon
     */
    public boolean isIconVisible() {
        switch(getDiagramModelObject().getIconVisibleState()) {
            case IDiagramModelObject.ICON_VISIBLE_NEVER:
                return false;

            case IDiagramModelObject.ICON_VISIBLE_IF_NO_IMAGE_DEFINED:
                return !hasIconImage();

            default:
                return true;
        }
    }

    /**
     * @return The offset in pixels to adjust the text position if there is an inbuilt icon
     */
    public int getIconOffset() {
        return 0;
    }

    /**
     * Apply a gradient pattern to the given Graphics instance and bounds using the current fill color, alpha and gradient setting
     * If a gradient is applied the alpha of graphics will be set to 255 so callers should set it back if needed after
     * calling {@link #disposeGradientPattern(Graphics, Pattern)}
     * @return the Pattern if a gradient should be applied or null if not
     */
    protected Pattern applyGradientPattern(Graphics graphics, Rectangle bounds) {
        Pattern gradient = null;
        
        // Apply gradient
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            // Ensure graphics#alpha is 255
            // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=575778
            graphics.setAlpha(255);
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        return gradient;
    }
    
    /**
     * Dispose the given Pattern if not null
     */
    protected void disposeGradientPattern(Graphics graphics, Pattern gradient) {
        if(gradient != null) {
            // Must set this to null in case of calling graphics.pushState() / graphics.popState();
            // Or any further drawing that might reference the Pattern
            graphics.setBackgroundPattern(null);
            gradient.dispose();
        }
    }
    
    @Override
    public IFigure getToolTip() {
        if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEW_TOOLTIPS)) {
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
        return provider != null ? provider.getDefaultSize() : IGraphicalObjectUIProvider.defaultSize();
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new ChopboxAnchor(this);
    }

    @Override
    public void dispose() {
        if(fIconicDelegate != null) {
            fIconicDelegate.dispose();
            fIconicDelegate = null;
        }
    }
}