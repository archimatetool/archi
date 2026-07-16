/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.util.ImageGraphics;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.editor.utils.PlatformUtils;


/**
 * Utils for Figures
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FigureUtils {

    /**
     * Get the current scale from either the figure's ancestor Figure or from ImageGraphics.getScale(). 
     * 
     * If figure is in a diagram, graphics will be SWTGraphics so use getFigureScale().
     * If figure is being used in an Image graphics will be ImageGraphics so use ImageGraphics.getScale()).
     * 
     * This is because things can differ on Windows with display scales and we get different results with graphics.getAbsoluteScale() and getFigureScale().
     * 
     * For example on Windows with 200% display scale:
     * 
     * If figure is in a diagram FigureUtils.getFigureScale() == 1 (correct) and graphics.getAbsoluteScale() == 2 (wrong)
     * If figure is being exported in an Image of 100% scale FigureUtils.getFigureScale() == 2 (wrong) and graphics.getAbsoluteScale() == 1 (correct)
     */
    public static double getFigureScale(Graphics graphics, IFigure figure) {
        return graphics instanceof ImageGraphics imageGraphics ? imageGraphics.getScale() : getFigureScale(figure);
    }

    /**
     * Get the current scale from figure's ancestor ScalableFigure
     * This is only valid when the figure is in a diagram not when called from DiagramUtils image creation.
     */
    public static double getFigureScale(IFigure figure) {
        while(figure != null) {
            if(figure instanceof ScalableFigure scalableFigure) {
                return scalableFigure.getScale();
            }
            figure = figure.getParent();
        }
        
        return 1.0;
    }
    
    /**
     * Whether Draw2d scaling is enabled on Windows
     */
    public static boolean isAutoScaleEnabled() {
        return PlatformUtils.isWindows() && Boolean.parseBoolean(System.getProperty("draw2d.enableAutoscale", Boolean.TRUE.toString()));
    }

    /**
     * @return the display scaling if on Windows and draw2d enableAutoscale is true, else return 1
     */
    @SuppressWarnings("restriction")
    public static float getDisplayScale() {
        return isAutoScaleEnabled() ? DPIUtil.getDeviceZoom() / 100f : 1f;
    }

    /**
     * Gradient Direction
     */
    public static enum Direction {
        TOP, LEFT, RIGHT, BOTTOM;
        
        public static Direction get(int value) {
            switch(value) {
                default:
                case 0: return TOP;
                case 1: return LEFT;
                case 2: return RIGHT;
                case 3: return BOTTOM;
            }
        }
    }

    /**
     * Create a Gradient Pattern
     */
    public static Pattern createGradient(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1, int alpha1, Color color2, int alpha2) {
        return new Pattern(device, x1, y1, x2, y2, color1, alpha1, color2, alpha2);
    }

    /**
     * Create a Gradient Pattern
     */
    public static Pattern createGradient(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1, Color color2) {
        return new Pattern(device, x1, y1, x2, y2, color1, color2);
    }
    
    /**
     * Create a Gradient Pattern
     */
    public static Pattern createGradient(Graphics graphics, Rectangle r, Color color, Direction direction) {
        return createGradient(graphics, r, color, 255, direction);
    }

    /**
     * Create a Gradient Pattern using the given gradient direction and default gradient end color and alpha transparency
     */
    public static Pattern createGradient(Graphics graphics, Rectangle r, Color color, int alpha, Direction direction) {
        if(direction == null) {
            return null;
        }
        
        Color endColor = ColorConstants.white;
        
        // Gradienting all the way to pure white is too much, this extends the gradient area to cover that
        float deltaFactor = 0.15f;

        switch(direction) {
            case TOP:
            default:
                int delta = (int) (r.height * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.x, r.y, r.x, r.getBottom().y + delta, color, alpha, endColor, alpha);
            
            case LEFT:
                delta = (int) (r.width * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.x, r.y, r.getRight().x + delta, r.y, color, alpha, endColor, alpha);
            
            case RIGHT:
                delta = (int) (r.width * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.getRight().x, r.y, r.x - delta, r.y, color, alpha, endColor, alpha);
            
            case BOTTOM:
                delta = (int) (r.height * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.x, r.getBottom().y, r.x, r.y - delta, color, alpha, endColor, alpha);
        }
    }

    /**
     * Create a Path from a points list
     * @param points The points list
     * @return The Path - callers should dispose of it
     */
    public static Path createPathFromPoints(PointList points) {
        return createPathFromPoints(points.toIntArray());
    }
    
    /**
     * Create a Path from a points list
     * @param points The points as x,y
     * @return The Path - callers should dispose of it
     */
    public static Path createPathFromPoints(int[] points) {
        Path path = new Path(null);
        
        path.moveTo(points[0], points[1]);
        
        for(int i = 2; i < points.length; i += 2) {
            path.lineTo(points[i], points[i + 1]);
        }
        
        path.close();
        
        return path;
    }
    
    /**
     * @return The given Path's precise bounding box (as a draw2d Rectangle - SWT's own Path#getBounds() returns
     * an org.eclipse.swt.graphics.Rectangle, which this converts), disposing the Path afterwards. A building
     * block for {@link com.archimatetool.editor.ui.IIconDelegate#getBounds()} implementations: build the same
     * Path drawIcon() draws (with its origin at (0, 0)) and pass it here rather than hand-computing the extent
     * of any arcs/curves it contains - SWT already knows how to do that precisely.
     */
    public static Rectangle getAndDisposePathBounds(Path path) {
        org.eclipse.swt.graphics.Rectangle bounds = path.getBounds();
        path.dispose();
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Draw an oval using a Path
     */
    public static void drawOvalPath(Graphics graphics, Rectangle rect) {
        drawOvalPath(graphics, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Draw an oval using a Path
     */
    public static void drawOvalPath(Graphics graphics, float x, float y, float width, float height) {
        Path path = createOvalPath(x, y, width, height);
        graphics.drawPath(path);
        path.dispose();
    }
    
    /**
     * Fill an oval using a Path
     */
    public static void fillOvalPath(Graphics graphics, Rectangle rect) {
        fillOvalPath(graphics, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Fill an oval using a Path
     */
    public static void fillOvalPath(Graphics graphics, float x, float y, float width, float height) {
        Path path = createOvalPath(x, y, width, height);
        graphics.fillPath(path);
        path.dispose();
    }
    
    private static Path createOvalPath(float x, float y, float width, float height) {
        Path path = new Path(null);
        path.addArc(x, y, width, height, 0, 360);
        return path;
    }

    /**
     * Fill a rectangle with only the top-right corner rounded, the other three corners square.
     * Used for a small "badge" box in the corner of a figure that should blend into a rounded figure's own corner.
     */
    public static void fillTopRightRoundedRectangle(Graphics graphics, Rectangle rect, int radius) {
        Path path = createTopRightRoundedRectanglePath(rect, radius);
        graphics.fillPath(path);
        path.dispose();
    }

    private static Path createTopRightRoundedRectanglePath(Rectangle rect, int radius) {
        Path path = new Path(null);
        path.moveTo(rect.x, rect.y + rect.height);
        path.lineTo(rect.x, rect.y);
        path.lineTo(rect.x + rect.width - radius, rect.y);
        path.quadTo(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + radius);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.close();
        return path;
    }

    /**
     * Fill a rectangle with only the top-right corner cut off at a straight 45-degree diagonal, the other three
     * corners square. Used for a small "badge" box in the corner of a figure whose own outline has a matching
     * diagonal-cut corner (e.g. the Motivation elements' "shaved corner" shape), so the badge's own cut edge is
     * colinear with, and sits flush inside, the figure's own cut corner instead of overhanging into the empty
     * triangle the figure's outline leaves outside that cut.
     */
    public static void fillTopRightChamferedRectangle(Graphics graphics, Rectangle rect, int chamfer) {
        Path path = createTopRightChamferedRectanglePath(rect, chamfer);
        graphics.fillPath(path);
        path.dispose();
    }

    private static Path createTopRightChamferedRectanglePath(Rectangle rect, int chamfer) {
        Path path = new Path(null);
        path.moveTo(rect.x, rect.y + rect.height);
        path.lineTo(rect.x, rect.y);
        path.lineTo(rect.x + rect.width - chamfer, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + chamfer);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.close();
        return path;
    }

    /**
     * Draw a figure's small in-built icon in the "Outline" shape style: a box filled with the figure's line color
     * (or its custom icon color / greyed-out disabled color, if either applies - matching Classic mode's icon
     * coloring), with its top-right corner flush with, and rounded to match, the figure's own top-right corner
     * (its other corners are square), and the icon itself drawn as an outline in the view's background ("paper")
     * color so the box color shows through, matching the figure's own paper-colored fill.
     * <p>
     * The box is sized and the icon positioned from {@link IIconDelegate#getBounds()} - callers don't need to
     * hand-derive or pass the icon's own width/height/origin-offset.
     *
     * @param owner The figure to draw the icon on to (used for its bounds, line color and icon visibility)
     * @param iconDelegate The delegate that draws the icon glyph itself
     * @param padding The padding around the icon glyph inside its containing box
     * @param cornerRadius The corner radius of the box's rounded top-right corner
     */
    public static void drawOutlineStyleIcon(Graphics graphics, AbstractDiagramModelObjectFigure owner, IIconDelegate iconDelegate,
            int padding, int cornerRadius) {
        drawOutlineStyleIconWithBoxFill(graphics, owner, iconDelegate, padding, padding, 0,
                (g, box) -> fillTopRightRoundedRectangle(g, box, cornerRadius));
    }

    /**
     * Draw a figure's small in-built icon in the "Outline" shape style, as {@link #drawOutlineStyleIcon(Graphics,
     * AbstractDiagramModelObjectFigure, IIconDelegate, int, int)}, but with the box's top-right corner cut off at
     * a straight diagonal (see {@link #fillTopRightChamferedRectangle(Graphics, Rectangle, int)}) instead of
     * rounded - for figures whose own outline (e.g. the Motivation elements' "shaved corner" shape) has a
     * matching diagonal-cut corner rather than a rounded one. Pass the figure's own corner cut size as
     * {@code chamfer} so the badge's cut edge is colinear with the figure's own.
     * <p>
     * Unlike the rounded corner (whose radius is small relative to the box), {@code chamfer} is often a large
     * fraction of a small badge box, so the icon can't just sit at the usual {@code padding} inset - some of it
     * would fall in the cut-away corner and be invisible there (drawn in the "paper" color over the page, not
     * the box). It's nudged left by {@link #CHAMFER_LEFT_NUDGE} first, away from the figure's own sharp corner
     * point, which in turn lets it sit higher too (clearing the cut at a smaller y) than a purely top-inset fix
     * would need - see {@link #drawOutlineStyleIconWithBoxFill}.
     */
    public static void drawOutlineStyleIconChamfered(Graphics graphics, AbstractDiagramModelObjectFigure owner, IIconDelegate iconDelegate,
            int padding, int chamfer) {
        // Clearing the cut only requires the icon's top-right corner to stay left of the diagonal (whose x, for
        // a given y, is boxWidth - chamfer + y) - nudging the icon CHAMFER_LEFT_NUDGE further left than the
        // plain padded position means that's satisfied already at a smaller y, so a smaller top inset than
        // chamfer alone would need still clears it (+1 for a comfortable, non-tangent margin)
        int topInset = Math.max(padding, chamfer - padding - CHAMFER_LEFT_NUDGE + 1);
        drawOutlineStyleIconWithBoxFill(graphics, owner, iconDelegate, padding, topInset, CHAMFER_LEFT_NUDGE,
                (g, box) -> fillTopRightChamferedRectangle(g, box, chamfer));
    }

    // How far left of the plain padded position to nudge a chamfered badge's icon, so it doesn't crowd the
    // figure's own sharp corner point right up against the diagonal cut - see drawOutlineStyleIconChamfered()
    private static final int CHAMFER_LEFT_NUDGE = 2;

    // Extra width added to every Outline-style icon badge box, purely as breathing room to the left of the icon.
    // The box's right edge stays flush with the figure's own corner (unaffected), and the icon's own position
    // relative to that corner is unchanged too - see drawOutlineStyleIconWithBoxFill(). Public so
    // getOutlineIconBoxWidth() (and, for figures pinned to an older explicit-constant call, any leftover manual
    // ICON_BOX_WIDTH constant) can reference this value directly instead of hardcoding a matching literal.
    public static final int LEFT_BREATHING_ROOM = 1;

    // Icon delegates are stateless singletons shared by every instance of a figure class, and their glyph
    // geometry never changes at runtime, so their bounds are computed once (building and disposing of an SWT
    // Path) and cached forever rather than being rebuilt on every single icon paint
    private static final Map<IIconDelegate, Rectangle> ICON_BOUNDS_CACHE = new ConcurrentHashMap<>();

    private static Rectangle getIconBounds(IIconDelegate iconDelegate) {
        return ICON_BOUNDS_CACHE.computeIfAbsent(iconDelegate, IIconDelegate::getBounds);
    }

    /**
     * @return the width of the Outline-style icon badge box that {@link #drawOutlineStyleIcon} /
     * {@link #drawOutlineStyleIconChamfered} draw for the given icon delegate and padding - the value a figure's
     * getIconOffset() should reserve as its Outline-mode horizontal text margin. Derived from the same
     * {@link IIconDelegate#getBounds()} the actual badge is drawn from, so it can't drift out of sync with it.
     */
    public static int getOutlineIconBoxWidth(IIconDelegate iconDelegate, int padding) {
        return getIconBounds(iconDelegate).width + (padding * 2) + LEFT_BREATHING_ROOM;
    }

    /**
     * Shared implementation behind {@link #drawOutlineStyleIcon} and {@link #drawOutlineStyleIconChamfered}: works
     * out the badge box (from the icon delegate's own bounds) and its color, then delegates the actual corner
     * shape to {@code boxFiller}.
     * @param topInset The inset from the box's top edge to the icon's top edge - usually just {@code padding}, but
     * larger for a chamfered corner so the icon clears the cut-away triangle (see
     * {@link #drawOutlineStyleIconChamfered(Graphics, AbstractDiagramModelObjectFigure, IIconDelegate, int, int)})
     * @param leftShift How far left of the plain padded position to additionally nudge the icon (used for the
     * chamfered corner only, see {@link #CHAMFER_LEFT_NUDGE})
     */
    private static void drawOutlineStyleIconWithBoxFill(Graphics graphics, AbstractDiagramModelObjectFigure owner, IIconDelegate iconDelegate,
            int padding, int topInset, int leftShift, BiConsumer<Graphics, Rectangle> boxFiller) {
        if(!owner.isIconVisible()) {
            return;
        }

        Rectangle iconBounds = getIconBounds(iconDelegate);

        Rectangle rect = owner.getBounds();
        // LEFT_BREATHING_ROOM is added to the box only - iconBox.x moves left with it, but origin.x below adds
        // it back, so the icon itself doesn't move relative to the figure's corner; the extra width all goes to
        // the gap between the icon's left edge and the box's left edge
        int boxWidth = iconBounds.width + (padding * 2) + LEFT_BREATHING_ROOM;
        int boxHeight = topInset + iconBounds.height + padding;
        Rectangle iconBox = new Rectangle(rect.getRight().x - boxWidth, rect.y, boxWidth, boxHeight);

        // A custom icon color or the disabled/greyed-out state take priority over the outline color, matching
        // how Classic mode colors its (box-less) icon
        Color boxColor = (!owner.isEnabled() || owner.hasCustomIconColor()) ? owner.getIconColor() : owner.getLineColor();

        graphics.pushState();
        graphics.setBackgroundColor(boxColor);
        boxFiller.accept(graphics, iconBox);
        graphics.popState();

        // iconBounds.x/y is where the glyph's own top-left sits relative to whatever origin it's drawn at (may
        // be non-zero if the glyph extends left of or above its own origin point) - solve for the origin that
        // lands the glyph's top-left exactly at the box's padded top-left
        Point origin = new Point(iconBox.x + padding + LEFT_BREATHING_ROOM - leftShift - iconBounds.x,
                iconBox.y + topInset - iconBounds.y);
        // Reuse the owner's own cached "paper" color rather than re-deriving it from the theme registry here -
        // this is always the same value, since this method only ever runs while the owner is in Outline style
        iconDelegate.drawIcon(graphics, owner.getOutlineStyleFillColor(), null, origin);
    }
}
