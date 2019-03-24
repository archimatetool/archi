/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.AbstractFOPTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.archimatetool.export.svg.graphiti.GraphicsToGraphics2DAdaptor;



/**
 * Export As PDF Provider
 * 
 * @author Phillip Beauvoir
 */
public class PDFExportProvider extends AbstractExportProvider {
    
    public static final String PDF_IMAGE_EXPORT_PROVIDER = "com.archimatetool.export.pdf.imageExporter"; //$NON-NLS-1$
    
    protected IFigure fFigure;
    
    @Override
    public void export(String providerID, File file) throws Exception {
        // Create a DOM Document
        Document document = createDocument();
        
        // Create a context for customisation
        SVGGeneratorContext ctx = createContext(document, true); // Must embed fonts for this version of Batik
        
        // Create a Batik SVGGraphics2D instance
        SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);
        
        // Get the outer bounds of the figure
        Rectangle bounds = getViewportBounds(fFigure);

        // Create a Graphiti wrapper adapter
        GraphicsToGraphics2DAdaptor graphicsAdaptor = createGraphicsToGraphics2DAdaptor(svgGenerator, bounds);
        
        // Paint the figure onto the graphics instance
        fFigure.paint(graphicsAdaptor);
        
        // Get the Element root from the SVGGraphics2D instance
        Element root = svgGenerator.getRoot();
        
        // And set some attributes on the root element
        setViewBoxAttribute(root, 0, 0, bounds.width, bounds.height);
        
        // Save the root element to temp file
        File tmp = File.createTempFile("svg", null); //$NON-NLS-1$
        tmp.deleteOnExit();
        Writer out = new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"); //$NON-NLS-1$
        svgGenerator.stream(root, out);
        
        // Close
        graphicsAdaptor.dispose();
        out.close();
        
        // PDF Transcoder
        TranscoderInput inputSVG = new TranscoderInput(tmp.toURI().toURL().toString());
        
        OutputStream outStream = new FileOutputStream(file);
        TranscoderOutput outputPDF = new TranscoderOutput(outStream);   
        
        PDFTranscoder transcoder = new PDFTranscoder();
        
        transcoder.addTranscodingHint(AbstractFOPTranscoder.KEY_AUTO_FONTS, false); // Don't create font cache
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)bounds.width);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)bounds.height);
        
        transcoder.transcode(inputSVG, outputPDF);
        
        outStream.flush();
        outStream.close();
        
        tmp.delete();
    }

    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        fFigure = figure;
    }
    
}
