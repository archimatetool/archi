/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.archimatetool.model.IDiagramModel;



/**
 * Export As PDF Provider
 * 
 * @author Phillip Beauvoir
 */
public class PDFExportProvider extends AbstractExportProvider implements IPreferenceConstants {
    
    public static final String PDF_IMAGE_EXPORT_PROVIDER = "com.archimatetool.export.pdf.imageExporter"; //$NON-NLS-1$
    
    Button fTextAsShapesButton;
    Button fEmbedFontsButton;
    Button fTextOffsetWorkaroundButton;
    
    @Override
    public void export(String providerID, File file) throws Exception {
        // Set Text as Shapes
        setDrawTextAsShapes(fTextAsShapesButton.getSelection());
        
        // Embed fonts
        setEmbedFonts(fEmbedFontsButton.getSelection());
        
        // Workaround text offset
        setTextOffsetWorkaround(fTextOffsetWorkaroundButton.getSelection());
        
        initialiseGraphics();
        
        // Get the Element root from the SVGGraphics2D instance
        Element root = svgGraphics2D.getRoot();
        
        // And set some attributes on the root element
        setViewBoxAttribute(root, 0, 0, viewPortBounds.width, viewPortBounds.height);
        
        // Set width and height
        setSizeAttributes(root, viewPortBounds.width, viewPortBounds.height);
        
        // Set any other attributes on the root element
        setRootAttributes(root);
        
        // Save the root element
        writeElementToFile(root, file);
        
        // Save Preferences
        savePreferences();
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
    private void writeElementToFile(Element root, File file) throws IOException, TranscoderException {
        // Make sure parent folder exists
        File parent = file.getParentFile();
        if(parent != null) {
            parent.mkdirs();
        }
        
        // Get SVG as String
        String svgString = getSVGString(root);
        
        // PDF Transcoder
        PDFTranscoder transcoder = new PDFTranscoder();
        
        // Not needed in latest FOP
        // transcoder.addTranscodingHint(AbstractFOPTranscoder.KEY_AUTO_FONTS, false);
        
        // Not needed if we set the width and height root element attributes
        // transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)viewPortBounds.width);
        // transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)viewPortBounds.height);

        TranscoderInput input = new TranscoderInput(new StringReader(svgString));
        
        // PDFTranscoder needs an OutputStream
        try(OutputStream out = new FileOutputStream(file)) {
            TranscoderOutput output = new TranscoderOutput(out);
            transcoder.transcode(input, output);
        }
        finally {
            svgGraphics2D.dispose();
        }
    }

    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        setFigure(figure);
        
        container.setLayout(new GridLayout(1, false));
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(container);
        
        fTextAsShapesButton = new Button(container, SWT.CHECK);
        fTextAsShapesButton.setText(Messages.PDFExportProvider_0);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fTextAsShapesButton);
        
        fEmbedFontsButton = new Button(container, SWT.CHECK);
        fEmbedFontsButton.setText(Messages.PDFExportProvider_1);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fEmbedFontsButton);
        
        fTextOffsetWorkaroundButton = new Button(container, SWT.CHECK);
        fTextOffsetWorkaroundButton.setText(Messages.PDFExportProvider_2);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(fTextOffsetWorkaroundButton);
        
        loadPreferences();
    }

    /**
     * Load any user prefs
     */
    protected void loadPreferences() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        
        // Text as Shapes selected
        fTextAsShapesButton.setSelection(store.getBoolean(PDF_EXPORT_PREFS_TEXT_AS_SHAPES));
        
        // Embed fonts selected
        fEmbedFontsButton.setSelection(store.getBoolean(PDF_EXPORT_PREFS_EMBED_FONTS));
        
        // Workaround text offset
        fTextOffsetWorkaroundButton.setSelection(store.getBoolean(PDF_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND));
    }
    
    /**
     * Save any user prefs
     */
    protected void savePreferences() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        
        // Text as shapes button selected
        store.setValue(PDF_EXPORT_PREFS_TEXT_AS_SHAPES, fTextAsShapesButton.getSelection());
        
        // Embed fonts selected
        store.setValue(PDF_EXPORT_PREFS_EMBED_FONTS, fEmbedFontsButton.getSelection());
        
        // Workaround text offset
        store.setValue(PDF_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND, fTextOffsetWorkaroundButton.getSelection());
    }
}
