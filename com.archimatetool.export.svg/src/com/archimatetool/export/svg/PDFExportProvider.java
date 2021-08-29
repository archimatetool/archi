/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.AbstractFOPTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.archimatetool.model.IDiagramModel;



/**
 * Export As PDF Provider
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class PDFExportProvider extends AbstractExportProvider {
    
    public static final String PDF_IMAGE_EXPORT_PROVIDER = "com.archimatetool.export.pdf.imageExporter";
    
    @Override
    public void export(String providerID, File file) throws Exception {
        initialiseGraphics();
        
        // Get the Element root from the SVGGraphics2D instance
        Element root = svgGraphics2D.getRoot();
        
        // And set some attributes on the root element
        setViewBoxAttribute(root, 0, 0, viewPortBounds.width, viewPortBounds.height);
        
        // Save the root element
        writeElementToFile(root, file);
    }

    /**
     * Save the diagram model image to file in PDF format
     * @param diagramModel The diagram model 
     * @param file The file to save to
     * @throws Exception
     */
    public void export(IDiagramModel diagramModel, File file) throws Exception {
        Element root = createElementForView(diagramModel, true);
        writeElementToFile(root, file);
    }
    
    /**
     * Write the DOM element to file
     */
    private void writeElementToFile(Element root, File file) throws Exception {
        // PDF Transcoder
        PDFTranscoder transcoder = new PDFTranscoder();
        transcoder.addTranscodingHint(AbstractFOPTranscoder.KEY_AUTO_FONTS, false); // Don't create font cache
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)viewPortBounds.width);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)viewPortBounds.height);

        String svgString;
        
        // Get SVG as String
        try(StringWriter out = new StringWriter()) {
            svgGraphics2D.stream(root, out);
            svgString = out.toString();
        }
        finally {
            svgGraphics2D.dispose();
        }
        
        // Stream to PDF
        try(OutputStream outStream = new FileOutputStream(file)) {
            TranscoderOutput outputPDF = new TranscoderOutput(outStream);
            
            try(InputStream svgStringStream = new ByteArrayInputStream(svgString.getBytes("UTF-8"));) {
                TranscoderInput inputSVG = new TranscoderInput(svgStringStream);
                transcoder.transcode(inputSVG, outputPDF);
                outStream.flush();
            }
        }
    }

    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        setFigure(figure);
    }
}
