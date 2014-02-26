/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import java.io.File;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.archimatetool.editor.diagram.IImageExportProvider;
import com.archimatetool.editor.diagram.util.DiagramUtils;



/**
 * Export As SVG Provider
 * 
 * @author Phillip Beauvoir
 */
public class SVGExportProvider implements IImageExportProvider {
    
    public void export(String providerID, File file, IFigure figure) throws Exception {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg"; //$NON-NLS-1$
        Document document = domImpl.createDocument(svgNS, "svg", null); //$NON-NLS-1$
        
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        
        Rectangle rect = DiagramUtils.getMinimumBounds(figure);
        if(rect == null) {
            rect = new Rectangle(0, 0, 100, 100); // At least a minimum for a blank image
        }
        else {
            rect.expand(10, 10); // margins
        }

        ExtendedGraphicsToGraphics2DAdaptor graphicsAdaptor = new ExtendedGraphicsToGraphics2DAdaptor(svgGenerator, rect);
        graphicsAdaptor.translate(rect.x * -1, rect.y * -1);
        graphicsAdaptor.setClip(rect); // need to do this
        graphicsAdaptor.setAdvanced(true);
        
        figure.paint(graphicsAdaptor);
        
        svgGenerator.stream(file.getPath());
        graphicsAdaptor.dispose();
    }

    @Override
    public void contributeSettings(Composite container) {
    }
    
}
