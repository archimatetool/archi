/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.archimatetool.editor.diagram.IImageExportProvider;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.export.svg.graphiti.GraphicsToGraphics2DAdaptor;
import com.archimatetool.model.IDiagramModel;



/**
 * Abstract Export As SVG Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractExportProvider implements IImageExportProvider {
    
    private IFigure figure;
    protected SVGGraphics2D svgGraphics2D;
    protected Rectangle viewPortBounds;
    
    protected void initialiseGraphics() {
        // Ensure user fonts are loaded into AWT for Windows
        loadUserFontsIntoAWT();
        
        // Create a DOM Document
        Document document = createDocument();
        
        // Create a context for customisation
        SVGGeneratorContext ctx = createContext(document, false); // Don't embed fonts
        
        // Create a Batik SVGGraphics2D instance
        svgGraphics2D = new SVGGraphics2D(ctx, true); // Text is drawn as shapes
        
        // Create a Graphiti wrapper adapter
        GraphicsToGraphics2DAdaptor graphicsAdaptor = createGraphicsToGraphics2DAdaptor(svgGraphics2D, viewPortBounds);
        
        // Paint the figure onto the graphics instance
        figure.paint(graphicsAdaptor);
        
        // Dispose of this
        graphicsAdaptor.dispose();
    }
    
    protected void setFigure(IFigure figure) {
        this.figure = figure;
        
        // Get the outer bounds of the figure
        viewPortBounds = getViewportBounds(figure);
    }
    
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
    
    /**
     * Create a DOM element for the given IDiagramModel
     */
    protected Element createElementForView(IDiagramModel diagramModel, boolean setViewBox) {
        Shell shell = new Shell();
        
        try {
            // Create Draw2d figure
            GraphicalViewerImpl viewer = DiagramUtils.createViewer(diagramModel, shell);
            LayerManager layerManager = (LayerManager)viewer.getEditPartRegistry().get(LayerManager.ID);
            IFigure figure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
            setFigure(figure);

            // Initialise
            initialiseGraphics();
        }
        finally {
            // Do this *after* creating the image
            shell.dispose();
        }
        
        // Get the Element root from the SVGGraphics2D instance
        Element root = svgGraphics2D.getRoot();

        // Set the Viewbox
        if(setViewBox) {
            setViewBoxAttribute(root, 0, 0, viewPortBounds.width, viewPortBounds.height);
        }
        
        return root;
    }

    /**
     * Load Windows fonts into AWT installed for the user, not "for all users" - we need to do this to register fonts for Windows
     */
    private static boolean awtFontsLoaded;
    
    private static void loadUserFontsIntoAWT() {
        if(PlatformUtils.isWindows() && !awtFontsLoaded) {
            File fontsFolder = new File(System.getProperty("user.home"), "AppData/Local/Microsoft/Windows/Fonts"); //$NON-NLS-1$ //$NON-NLS-2$
            if(fontsFolder.exists()) {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                
                for(File file : fontsFolder.listFiles()) {
                    if(file.isFile()) {
                        try {
                            Font font = Font.createFont(Font.TRUETYPE_FONT, file);
                            ge.registerFont(font);
                        }
                        catch(Exception ex) { // Ignore
                        }
                    }
                }
                
                awtFontsLoaded = true;
            }
        }
    }
}
