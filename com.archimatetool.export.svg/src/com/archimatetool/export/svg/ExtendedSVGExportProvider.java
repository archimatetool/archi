/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;

/**
 * Export As SVG Provider with extended options
 * 
 * @author Phillip Beauvoir
 */
public class ExtendedSVGExportProvider extends SVGExportProvider {
    
    @Override
    protected SVGGraphics2D getSVGGraphics2D(SVGGeneratorContext ctx) {
        return new ExtendedSVGGraphics2D(ctx, textAsShapes);
    }
    
}
