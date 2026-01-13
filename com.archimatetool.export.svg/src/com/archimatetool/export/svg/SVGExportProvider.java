/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.w3c.dom.Element;

import com.archimatetool.model.IDiagramModel;



/**
 * Export As SVG Provider
 * 
 * @author Phillip Beauvoir
 */
public class SVGExportProvider extends AbstractExportProvider implements IPreferenceConstants {
    
    public static final String SVG_IMAGE_EXPORT_PROVIDER = "com.archimatetool.export.svg.imageExporter"; //$NON-NLS-1$
    
    Button fSetViewboxButton;
    Spinner fSpinner1, fSpinner2, fSpinner3, fSpinner4;
    
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
        
        // Set width and height
        setSizeAttributes(root, viewPortBounds.width, viewPortBounds.height);
        
        // And set some attributes on the root element from user options
        if(fSetViewboxButton.getSelection()) {
            setViewBoxAttribute(root, fSpinner1.getSelection(), fSpinner2.getSelection(), fSpinner3.getSelection(), fSpinner4.getSelection());
        }
        
        // Set any other attributes on the root element
        setRootAttributes(root);
        
        // Save the root element
        writeElementToFile(root, file);
        
        // Save Preferences
        savePreferences();
    }
    
    /**
     * Save the diagram model image to file in SVG format
     * @param diagramModel The diagram model 
     * @param file The file to save to
     * @param setViewBox if true sets the viewbox bounds to the bounds of the diagram
     * @throws Exception
     */
    public void export(IDiagramModel diagramModel, File file, boolean setViewBox) throws Exception {
        Element root = createElementForView(diagramModel, setViewBox);
        writeElementToFile(root, file);
    }
    
    /**
     * Return the given diagram image to SVG string
     * @param diagramModel The diagram model 
     * @return a String of the SVG representation
     * @param setViewBox if true sets the viewbox bounds to the bounds of the diagram
     * @throws Exception
     */
    public String getSVGString(IDiagramModel diagramModel, boolean setViewBox) throws Exception {
        Element root = createElementForView(diagramModel, setViewBox);
        
        try(StringWriter out = new StringWriter()) {
            writeElement(root, out);
            return out.toString();
        }
        finally {
            svgGraphics2D.dispose();
        }
    }
    
    /**
     * Write the root element to file
     */
    private void writeElementToFile(Element root, File file) throws IOException, TranscoderException {
        // Make sure parent folder exists
        File parent = file.getParentFile();
        if(parent != null) {
            parent.mkdirs();
        }
        
        try(Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) { //$NON-NLS-1$
            writeElement(root, out);
        }
        finally {
            svgGraphics2D.dispose();
        }
    }
    
    /**
     * Output the root element to Writer using a SVGTranscoder so we can set transcoding hints
     */
    private void writeElement(Element root, Writer out) throws IOException, TranscoderException {
        // TranscoderInput input from SVG as string
        String svgString = getSVGString(root);
        TranscoderInput input = new TranscoderInput(new StringReader(svgString));
        
        SVGTranscoder transcoder = new SVGTranscoder();
        
        // Don't save DocType
        transcoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_REMOVE);
        
        //transcoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
        
        // TranscoderOutput to Writer
        TranscoderOutput output = new TranscoderOutput(out);
        
        transcoder.transcode(input, output);
    }
    
    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        setFigure(figure);
        
        final int numColumns = 8;
        
        container.setLayout(new GridLayout(numColumns, false));
        GridDataFactory.create(GridData.FILL_HORIZONTAL).applyTo(container);
        
        fSetViewboxButton = new Button(container, SWT.CHECK);
        fSetViewboxButton.setText(Messages.SVGExportProvider_0);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(numColumns, 1).applyTo(fSetViewboxButton);
        
        fSetViewboxButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            updateControls();
        }));
        
        int min = -100000;
        int max = 100000;
        
        Label label = new Label(container, SWT.NONE);
        label.setText(" " + Messages.SVGExportProvider_2); //$NON-NLS-1$
        fSpinner1 = new Spinner(container, SWT.BORDER);
        fSpinner1.setMinimum(min);
        fSpinner1.setMaximum(max);
        
        label = new Label(container, SWT.NONE);
        label.setText(" " + Messages.SVGExportProvider_3); //$NON-NLS-1$
        fSpinner2 = new Spinner(container, SWT.BORDER);
        fSpinner2.setMinimum(min);
        fSpinner2.setMaximum(max);
        
        label = new Label(container, SWT.NONE);
        label.setText(" " + Messages.SVGExportProvider_4); //$NON-NLS-1$
        fSpinner3 = new Spinner(container, SWT.BORDER);
        fSpinner3.setMaximum(max);
        
        label = new Label(container, SWT.NONE);
        label.setText(" " + Messages.SVGExportProvider_5); //$NON-NLS-1$
        fSpinner4 = new Spinner(container, SWT.BORDER);
        fSpinner4.setMaximum(max);
        
        fTextAsShapesButton = new Button(container, SWT.CHECK);
        fTextAsShapesButton.setText(Messages.SVGExportProvider_6);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(numColumns, 1).applyTo(fTextAsShapesButton);
        
        fEmbedFontsButton = new Button(container, SWT.CHECK);
        fEmbedFontsButton.setText(Messages.SVGExportProvider_7);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(numColumns, 1).applyTo(fEmbedFontsButton);
        
        fTextOffsetWorkaroundButton = new Button(container, SWT.CHECK);
        fTextOffsetWorkaroundButton.setText(Messages.SVGExportProvider_8);
        GridDataFactory.create(GridData.FILL_HORIZONTAL).span(numColumns, 1).applyTo(fTextOffsetWorkaroundButton);
        
        loadPreferences();
        
        // Set viewBox width and height to the image size
        fSpinner3.setSelection(viewPortBounds.width);
        fSpinner4.setSelection(viewPortBounds.height);
    }
    
    private void updateControls() {
        fSpinner1.setEnabled(fSetViewboxButton.getSelection());
        fSpinner2.setEnabled(fSetViewboxButton.getSelection());
        fSpinner3.setEnabled(fSetViewboxButton.getSelection());
        fSpinner4.setEnabled(fSetViewboxButton.getSelection());
    }
    
    /**
     * Load any user prefs
     */
    protected void loadPreferences() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        
        // Viewbox button selected
        fSetViewboxButton.setSelection(store.getBoolean(SVG_EXPORT_PREFS_VIEWBOX_ENABLED));
        updateControls();
        
        int min_x = 0;
        int min_y = 0;
        
        // Value of viewBox
        String s = store.getString(SVG_EXPORT_PREFS_VIEWBOX);
        if(s != null) {
            String[] parts = s.split(" "); //$NON-NLS-1$
            if(parts.length >= 2) {
                try {
                    min_x = Integer.valueOf(parts[0]);
                    min_y = Integer.valueOf(parts[1]);
                }
                catch(NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        fSpinner1.setSelection(min_x);
        fSpinner2.setSelection(min_y);
        
        // Text as Shapes selected
        fTextAsShapesButton.setSelection(store.getBoolean(SVG_EXPORT_PREFS_TEXT_AS_SHAPES));
        
        // Embed fonts selected
        fEmbedFontsButton.setSelection(store.getBoolean(SVG_EXPORT_PREFS_EMBED_FONTS));
        
        // Workaround text offset
        fTextOffsetWorkaroundButton.setSelection(store.getBoolean(SVG_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND));
    }
    
    /**
     * Save any user prefs
     */
    protected void savePreferences() {
        IPreferenceStore store = ExportSVGPlugin.getInstance().getPreferenceStore();
        
        // Viewbox button selected
        store.setValue(SVG_EXPORT_PREFS_VIEWBOX_ENABLED, fSetViewboxButton.getSelection());
        
        int min_x = fSpinner1.getSelection();
        int min_y = fSpinner2.getSelection();
        
        String s = min_x + " " + min_y;  //$NON-NLS-1$
        store.setValue(SVG_EXPORT_PREFS_VIEWBOX, s);
        
        // Text as shapes button selected
        store.setValue(SVG_EXPORT_PREFS_TEXT_AS_SHAPES, fTextAsShapesButton.getSelection());
        
        // Embed fonts selected
        store.setValue(SVG_EXPORT_PREFS_EMBED_FONTS, fEmbedFontsButton.getSelection());
        
        // Workaround text offset
        store.setValue(SVG_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND, fTextOffsetWorkaroundButton.getSelection());
    }
    
}
