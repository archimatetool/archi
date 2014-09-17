/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.awt.Graphics2D;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.archimatetool.editor.diagram.IImageExportProvider;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.export.svg.graphiti.GraphicsToGraphics2DAdaptor;



/**
 * Abstract Export As SVG Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractExportProvider implements IImageExportProvider {
    
    /**
     * Get the viewport bounds for the given figure that will be printed
     * @param figure the given figure that will be printed
     * @return The bounds
     */
    protected Rectangle getViewportBounds(IFigure figure) {
        Rectangle rect = DiagramUtils.getMinimumBounds(figure);
        if(rect == null) {
            rect = new Rectangle(0, 0, 100, 100); // At least a minimum for a blank image
        }
        else {
            rect.expand(10, 10); // margins
        }
        return rect;
    }
    
    /**
     * Create the DOM SDocument with root namespace and root element name
     * @return The DOM Document to save to
     */
    protected Document createDocument() {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg"; //$NON-NLS-1$
        return domImpl.createDocument(svgNS, "svg", null); //$NON-NLS-1$
    }
    
    /**
     * Create a SVGGeneratorContext and set its attributes
     * @param document The DOM Document
     * @param embeddedFonts If true will embed fonts
     * @return The SVGGeneratorContext
     */
    protected SVGGeneratorContext createContext(Document document, boolean embedFonts) {
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
        ctx.setEmbeddedFontsOn(embedFonts);
        ctx.setComment(Messages.SVGExportProvider_1); // Add a comment
        return ctx;
    }
    
    /**
     * Create the Graphiti Graphics2D adapter with its Viewport
     * @param graphics2d The Batick AWT Graphics2D to wrap
     * @param viewPort The Viewport of the figure to print
     * @return The GraphicsToGraphics2DAdaptor
     */
    protected GraphicsToGraphics2DAdaptor createGraphicsToGraphics2DAdaptor(Graphics2D graphics2d, Rectangle viewPort) {
        ExtendedGraphicsToGraphics2DAdaptor graphicsAdaptor = new ExtendedGraphicsToGraphics2DAdaptor(graphics2d, viewPort);
        graphicsAdaptor.translate(viewPort.x * -1, viewPort.y * -1);
        graphicsAdaptor.setClip(viewPort); // need to do this
        graphicsAdaptor.setAdvanced(true);
        return graphicsAdaptor;
    }

    /**
     * Set the "viewBox" attribute of the DOM root Element from the SVGGraphics2D instance.
     * See http://www.justinmccandless.com/blog/Making+Sense+of+SVG+viewBox%27s+Madness
     *     http://www.w3.org/TR/SVG/coords.html#ViewBoxAttribute
     * @param root The DOM root element
     * @param min_x the x origin of the viewBox within the parent
     * @param min_y the y origin of the viewBox within the parent
     * @param width
     * @param height
     */
    protected void setViewBoxAttribute(Element root, int min_x, int min_y, int width, int height) {
        root.setAttributeNS(null, "viewBox", min_x + " " + min_y + " " + width + " " + height);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
}
