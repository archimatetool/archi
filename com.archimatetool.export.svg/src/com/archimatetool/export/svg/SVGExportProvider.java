/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.archimatetool.editor.diagram.IImageExportProvider;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.export.svg.graphiti.GraphicsToGraphics2DAdaptor;



/**
 * Export As SVG Provider
 * 
 * @author Phillip Beauvoir
 */
public class SVGExportProvider implements IImageExportProvider, IPreferenceConstants {
    
    public static final String SVG_IMAGE_EXPORT_PROVIDER = "com.archimatetool.export.svg.imageExporter"; //$NON-NLS-1$
    
    protected Button fEmbedFontsButton;
    protected Button fSetViewboxButton;
    protected Spinner fSpinner1, fSpinner2, fSpinner3, fSpinner4;
    
    protected IFigure fFigure;
    
    @Override
    public void export(String providerID, File file) throws Exception {
        // Create a DOM Document
        Document document = createDocument();
        
        // Create a context for customisation
        SVGGeneratorContext ctx = createContext(document);
        
        // Create a Batik SVGGraphics2D instance
        SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);
        
        // Get the outer bounds of the figure
        Rectangle bounds = getViewportBounds();

        // Create a Graphiti wrapper adapter
        GraphicsToGraphics2DAdaptor graphicsAdaptor = createGraphicsToGraphics2DAdaptor(svgGenerator, bounds);
        
        // Paint the figure onto the graphics instance
        fFigure.paint(graphicsAdaptor);
        
        // Get the Element root from the SVGGraphics2D instance
        Element root = svgGenerator.getRoot();
        
        // And set some attributes on the root element
        if(fSetViewboxButton.getSelection()) {
            setViewBoxAttribute(root, fSpinner1.getSelection(), fSpinner2.getSelection(), fSpinner3.getSelection(), fSpinner4.getSelection());
        }
        
        // Save the root element
        Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //$NON-NLS-1$
        svgGenerator.stream(root, out);
        
        // Close
        graphicsAdaptor.dispose();
        out.close();
        
        // Save Preferences
        savePreferences();
    }

    /**
     * Get the viewport bounds for the given figure that will be printed
     * @param figure the given figure that will be printed
     * @return The bounds
     */
    protected Rectangle getViewportBounds() {
        Rectangle rect = DiagramUtils.getMinimumBounds(fFigure);
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
     * @return The SVGGeneratorContext
     */
    protected SVGGeneratorContext createContext(Document document) {
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
        ctx.setEmbeddedFontsOn(fEmbedFontsButton.getSelection());
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
    
    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        fFigure = figure;
        
        container.setLayout(new GridLayout(8, false));
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fEmbedFontsButton = new Button(container, SWT.CHECK);
        fEmbedFontsButton.setText(Messages.SVGExportProvider_2);
        GridData gd = new GridData();
        gd.horizontalSpan = 8;
        fEmbedFontsButton.setLayoutData(gd);
        
        fSetViewboxButton = new Button(container, SWT.CHECK);
        fSetViewboxButton.setText(Messages.SVGExportProvider_0);
        gd = new GridData();
        gd.horizontalSpan = 8;
        fSetViewboxButton.setLayoutData(gd);
        
        fSetViewboxButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateControls();
            }
        });
        
        int min = -10000;
        int max = 10000;
        
        Label label = new Label(container, SWT.NONE);
        label.setText(" min_x:"); //$NON-NLS-1$
        fSpinner1 = new Spinner(container, SWT.BORDER);
        fSpinner1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fSpinner1.setMinimum(min);
        fSpinner1.setMaximum(max);
        
        label = new Label(container, SWT.NONE);
        label.setText(" min_y:"); //$NON-NLS-1$
        fSpinner2 = new Spinner(container, SWT.BORDER);
        fSpinner2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fSpinner2.setMinimum(min);
        fSpinner2.setMaximum(max);
        
        label = new Label(container, SWT.NONE);
        label.setText(" width:"); //$NON-NLS-1$
        fSpinner3 = new Spinner(container, SWT.BORDER);
        fSpinner3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fSpinner3.setMaximum(max);
        
        label = new Label(container, SWT.NONE);
        label.setText(" height:"); //$NON-NLS-1$
        fSpinner4 = new Spinner(container, SWT.BORDER);
        fSpinner4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fSpinner4.setMaximum(max);

        loadPreferences();
        
        // Set viewBox width and height to the image size
        Rectangle rect = getViewportBounds();
        fSpinner3.setSelection(rect.width);
        fSpinner4.setSelection(rect.height);
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
        IPreferenceStore store = ExportSVGPlugin.getDefault().getPreferenceStore();
        
        // Embed fonts
        boolean selected = store.getBoolean(SVG_EXPORT_PREFS_EMBED_FONTS);
        fEmbedFontsButton.setSelection(selected);
        
        // Viewbox button selected
        selected = store.getBoolean(SVG_EXPORT_PREFS_VIEWBOX_ENABLED);
        fSetViewboxButton.setSelection(selected);
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
    }
    
    /**
     * Save any user prefs
     */
    protected void savePreferences() {
        IPreferenceStore store = ExportSVGPlugin.getDefault().getPreferenceStore();
        
        // Embed fonts
        store.setValue(SVG_EXPORT_PREFS_EMBED_FONTS, fEmbedFontsButton.getSelection());
        
        // Viewbox button selected
        store.setValue(SVG_EXPORT_PREFS_VIEWBOX_ENABLED, fSetViewboxButton.getSelection());
        
        int min_x = fSpinner1.getSelection();
        int min_y = fSpinner2.getSelection();
        
        String s = min_x + " " + min_y;  //$NON-NLS-1$
        store.setValue(SVG_EXPORT_PREFS_VIEWBOX, s);
    }
    
}
