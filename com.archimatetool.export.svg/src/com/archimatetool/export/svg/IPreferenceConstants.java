/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;




/**
 * Constant definitions for Preferences
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface IPreferenceConstants {
    
    String SVG_EXPORT_PREFS_VIEWBOX_ENABLED = "viewBoxEnabled";
    String SVG_EXPORT_PREFS_VIEWBOX = "viewBox";
    String SVG_EXPORT_PREFS_TEXT_AS_SHAPES = "svgTextAsShapes";
    String PDF_EXPORT_PREFS_TEXT_AS_SHAPES = "pdfTextAsShapes";
    
    String SVG_EXPORT_PREFS_EMBED_FONTS = "svgEmbedFonts";
    String PDF_EXPORT_PREFS_EMBED_FONTS = "pdfEmbedFonts";
    
    String SVG_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND = "svgTextOffset";
    String PDF_EXPORT_PREFS_USE_TEXT_OFFSET_WORKAROUND = "pdfTextOffset";
}
