/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
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
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IIconic;



/**
 * Abstract Figure for containing a IDiagramModelObject
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramModelObjectFigure extends Figure
implements IDiagramModelObjectFigure {
    
    // Use line width offset handling
    boolean useLineOffset = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.USE_FIGURE_LINE_OFFSET);
    
    private IDiagramModelObject diagramModelObject;
    
    // Cache some values so they are not re-evaluated each time when the figure is painted to improve drawing speed.
    // These values are colors and some features that might take some processing to get from a FeaturesEList
    private Map<Object, Object> cachedValues;
    
    // Delegate to do drawing
    private IFigureDelegate figureDelegate;
    
    // UI Provider
    protected IGraphicalObjectUIProvider uiProvider;
    
    // Delegate to draw icon image
    private IconicDelegate iconicDelegate;
    
    protected AbstractDiagramModelObjectFigure() {
    }
    
    protected AbstractDiagramModelObjectFigure(IDiagramModelObject diagramModelObject){
        setDiagramModelObject(diagramModelObject);
    }

    @Override
    public void setDiagramModelObject(IDiagramModelObject diagramModelObject) {
        this.diagramModelObject = diagramModelObject;
        uiProvider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(diagramModelObject);
        setUI();
    }
    
    @Override
    public IDiagramModelObject getDiagramModelObject() {
        return diagramModelObject;
    }
    
    public IFigureDelegate getFigureDelegate() {
        return figureDelegate;
    }
    
    public void setFigureDelegate(IFigureDelegate figureDelegate) {
        this.figureDelegate = figureDelegate;
    }
    
    /**
     * Set the UI
     */
    abstract protected void setUI();
    
    @Override
    public void refreshVisuals() {
        // Clear the cached values *first* so they can be re-evaluated
        clearCachedValues();
        
        // Font
        setFont();
        
        // Font Color
        setFontColor();
        
        // Icon Image
        updateIconImage();
        
        repaint();
    }
    
    /**
     * Clear all cached values. This will force all values to be re-evaluated the next time one is retrieved in getCachedValue
     */
    protected void clearCachedValues() {
        cachedValues = null;
    }
    
    /**
     * Get a cached value from the cached value map
     * @param key key with which the specified value is to be associated
     * @param mappingFunction the mapping function to compute a value
     * @return the current (existing or computed) value associated with the specified key, or null if the computed value is null
     */
    @SuppressWarnings("unchecked")
    protected <T> T getCachedValue(Object key, Function<Object, T> mappingFunction) {
        cachedValues = cachedValues == null ? new ConcurrentHashMap<>() : cachedValues; // lazily create new map
        return (T) cachedValues.computeIfAbsent(key, mappingFunction);
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
     * Set the line width to that in the diagram model and compensate the figure bounds width and height for this line width and translate the graphics instance
     * @param graphics The graphics instance
     * @param bounds The bounds of the object
     */
    protected void setLineWidth(Graphics graphics, Rectangle bounds) {
        setLineWidth(graphics, getLineWidth(), bounds);
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
        if(bounds.width > lineWidth && bounds.height > lineWidth) { // but only if width and height are greater than linewidth
            bounds.resize(-lineWidth, -lineWidth);
        }
        
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
        //graphics.setLineDash(new int[] { 4, 3 });
    }
    
    /**
     * Set the line style
     * @param graphics
     */
    protected void setLineStyle(Graphics graphics) {
        switch(getLineStyle()) {
            case IDiagramModelObject.LINE_STYLE_SOLID -> {
                graphics.setLineStyle(Graphics.LINE_SOLID);
            }
            case IDiagramModelObject.LINE_STYLE_DASHED -> {
                final double scale = Math.min(FigureUtils.getFigureScale(this), 1.0); // only scale below 1.0
                graphics.setLineDash(new float[] { (float)(8 * scale), (float)(4 * scale) });
            }
            case IDiagramModelObject.LINE_STYLE_DOTTED -> {
                final double scale = Math.min(FigureUtils.getFigureScale(this), 1.0); // only scale below 1.0
                graphics.setLineDash(new float[] { (float)(2 * scale), (float)(4 * scale) });
            }
        }
    }

    /**
     * Set the font to that in the model, or failing that, as per user's default
     */
    protected void setFont() {
        // Set the font for the figure not the text control.
        // (Direct Edit Manager sets its font according to this font and GroupFigure uses it).
        setFont(FontFactory.getViewFont(getDiagramModelObject().getFont()));
        
        // Need to revalidate the text control after a font change
        if(getTextControl() != null) {
            getTextControl().revalidate();
        }
    }
    
    /**
     * @return The Fill Color to use
     */
    @Override
    public Color getFillColor() {
        // Cache this value
        return getCachedValue("fillColor", key -> { //$NON-NLS-1$
            Color color = ColorFactory.get(getDiagramModelObject().getFillColor());
            return color != null ? color : ColorFactory.getDefaultFillColor(getDiagramModelObject());
        });
    }
    
    /**
     * Set the font color of the text control or Figure.
     * This actually sets the foreground color of the text control or Figure to that in the model object's font color
     */
    protected void setFontColor() {
        if(getTextControl() != null) {
            Color color = ColorFactory.get(getDiagramModelObject().getFontColor());
            getTextControl().setForegroundColor(color != null ? color : ColorConstants.black); // Default to black in case of dark theme
        }
    }
    
    @Override
    public Color getLineColor() {
        // Cache this value
        return getCachedValue("lineColor", key -> { //$NON-NLS-1$
            if(getDiagramModelObject().getDeriveElementLineColor()) {
                return ColorFactory.getDerivedLineColor(getFillColor()); // Calling getFillColor() will concurrently modify the cached Map!
            }
            else {
                Color color = ColorFactory.get(getDiagramModelObject().getLineColor());
                return color != null ? color : ColorFactory.getDefaultLineColor(getDiagramModelObject());
            }
        });
    }
    
    /**
     * @deprecated Don't call this, instead call refreshVisuals() or clearCachedValues();
     */
    @Deprecated
    protected void setFillColor() {
        clearCachedValues();
    }
    
    /**
     * @deprecated Don't call this, instead call refreshVisuals() or clearCachedValues();
     */
    @Deprecated
    protected void setLineColor() {
        clearCachedValues();
    }

    public int getAlpha() {
        // No need to cache this
        int alpha = getDiagramModelObject().getAlpha();
        return isEnabled() ? alpha : Math.min(100, alpha);
    }

    public int getLineAlpha() {
        // Cache this value as it's from a FeaturesEList
        return isEnabled() ? getCachedValue(IDiagramModelObject.FEATURE_LINE_ALPHA, key -> getDiagramModelObject().getLineAlpha()) : 100;
    }
    
    public int getGradient() {
        // Cache this value as it's from a FeaturesEList
        return getCachedValue(IDiagramModelObject.FEATURE_GRADIENT, key -> getDiagramModelObject().getGradient());
    }
    
    public int getLineWidth() {
        // No need to cache this
        return getDiagramModelObject().getLineWidth();
    }
    
    public int getLineStyle() {
        // Cache this value as it's from a FeaturesEList
        return getCachedValue(IDiagramModelObject.FEATURE_LINE_STYLE,
                key -> uiProvider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE) instanceof Integer val ? val : IDiagramModelObject.LINE_STYLE_SOLID);
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
        iconicDelegate = delegate;
    }
    
    /**
     * @return The IconicDelegate if this figure draws icons, or null if not
     */
    public IconicDelegate getIconicDelegate() {
        return iconicDelegate;
    }
    
    /**
     * @return whether to show the small in-built icon - either the ArchiMate icon or the view reference icon
     */
    public boolean isIconVisible() {
        switch(getIconVisibleState()) {
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
    
    public int getIconVisibleState() {
        // Cache this value as it's from a FeaturesEList
        return getCachedValue(IDiagramModelObject.FEATURE_ICON_VISIBLE, key -> getDiagramModelObject().getIconVisibleState());
    }
    
    public Color getIconColor() {
        if(!isEnabled()) {
            return ColorConstants.lightGray;
        }
        
        // Cache this value as it's from a FeaturesEList
        return getCachedValue(IDiagramModelObject.FEATURE_ICON_COLOR,
                key -> {
                    String val = getDiagramModelObject().getIconColor();
                    return StringUtils.isSet(val) ? ColorFactory.get(val) : ColorConstants.black;
                });
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
        if(!ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEW_TOOLTIPS)) {
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
        if(getDiagramModelObject() instanceof IDiagramModelArchimateObject dmao) {
            EClass eClass = dmao.getArchimateElement().eClass();
            String type = ArchiLabelProvider.INSTANCE.getDefaultName(eClass);
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
        // If the object has an icon image and set to image fill and isn't a container with children default size is the image size
        if(getDiagramModelObject() instanceof IIconic iconic
                && iconic.getImagePosition() == IIconic.ICON_POSITION_FILL
                && hasIconImage()
                && !(getDiagramModelObject() instanceof IDiagramModelContainer dmc && !dmc.getChildren().isEmpty())) {
            org.eclipse.swt.graphics.Rectangle imageBounds = getIconicDelegate().getImage().getBounds();
            return new Dimension(imageBounds.width, imageBounds.height);
        }
        
        // Always check for nulls as getDefaultSize() can be called after the figure is disposed
        return uiProvider != null ? uiProvider.getDefaultSize() : IGraphicalObjectUIProvider.defaultSize();
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new ChopboxAnchor(this);
    }

    @Override
    public void dispose() {
        if(iconicDelegate != null) {
            iconicDelegate.dispose();
            iconicDelegate = null;
        }
        
        cachedValues = null;
    }
}