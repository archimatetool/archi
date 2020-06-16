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

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.AbstractFOPTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.w3c.dom.Element;



/**
 * Export As PDF Provider
 * 
 * @author Phillip Beauvoir
 */
public class PDFExportProvider extends AbstractExportProvider {
    
    public static final String PDF_IMAGE_EXPORT_PROVIDER = "com.archimatetool.export.pdf.imageExporter"; //$NON-NLS-1$
    
    @Override
    public void export(String providerID, File file) throws Exception {
        super.export(providerID, file);
        
        // Get the Element root from the SVGGraphics2D instance
        Element root = svgGraphics2D.getRoot();
        
        // And set some attributes on the root element
        setViewBoxAttribute(root, 0, 0, viewPortBounds.width, viewPortBounds.height);
        
        // Save the root element to a temp file
        File tmp = File.createTempFile("svg", null); //$NON-NLS-1$
        tmp.deleteOnExit();
        Writer out = new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"); //$NON-NLS-1$
        svgGraphics2D.stream(root, out);
        
        // Close
        svgGraphics2D.dispose();
        out.close();
        
        // PDF Transcoder
        TranscoderInput inputSVG = new TranscoderInput(tmp.toURI().toURL().toString());
        
        OutputStream outStream = new FileOutputStream(file);
        TranscoderOutput outputPDF = new TranscoderOutput(outStream);   
        
        PDFTranscoder transcoder = new PDFTranscoder();
        
        transcoder.addTranscodingHint(AbstractFOPTranscoder.KEY_AUTO_FONTS, false); // Don't create font cache
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)viewPortBounds.width);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)viewPortBounds.height);
        
        transcoder.transcode(inputSVG, outputPDF);
        
        outStream.flush();
        outStream.close();
        
        tmp.delete();
    }
}
