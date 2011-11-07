/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import uk.ac.bolton.archimate.model.IApplicationLayerElement;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IBusinessLayerElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.ISketchModelActor;
import uk.ac.bolton.archimate.model.ITechnologyLayerElement;


/**
 * Color Factory
 * 
 * @author Phillip Beauvoir
 */
public class ColorFactory {
    
    public static final Color COLOR_BUSINESS = new Color(null, 255, 255, 181);
    public static final Color COLOR_APPLICATION = new Color(null, 181, 255, 255);
    public static final Color COLOR_TECHNOLOGY = new Color(null, 201, 231, 183);
    
    public static final Color COLOR_GROUP = new Color(null, 210, 215, 215);
    public static final Color COLOR_NOTE = ColorConstants.white;
    public static final Color COLOR_DIAGRAM_MODEL_REF = new Color(null, 220, 235, 235);
    
    /**
     * Color Registry
     */
    private static ColorRegistry ColorRegistry = new ColorRegistry();
    
    public static Color get(int red, int green, int blue) {
        return get(new RGB(red, green, blue));
    }
    
    public static Color get(RGB rgb) {
        String rgbValue = convertRGBToString(rgb);
        return get(rgbValue);
    }
    
    public static Color get(String rgbValue) {
        if(rgbValue == null) {
            return null;
        }
        
        if(!ColorRegistry.hasValueFor(rgbValue)) {
            RGB rgb = convertStringToRGB(rgbValue);
            if(rgb != null) {
                ColorRegistry.put(rgbValue, rgb);
            }
        }
        
        return ColorRegistry.get(rgbValue);
    }
    
    /**
     * @param object
     * @return A defult Color for a diagram model object
     */
    public static Color getDefaultColor(Object object) {
        if(object instanceof IDiagramModelNote) {
            return COLOR_NOTE;
        }
        if(object instanceof IDiagramModelGroup) {
            return COLOR_GROUP;
        }
        if(object instanceof IDiagramModelReference) {
            return COLOR_DIAGRAM_MODEL_REF;
        }
        if(object instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)object).getArchimateElement();
            if(element instanceof IBusinessLayerElement) {
                return COLOR_BUSINESS;
            }
            if(element instanceof IApplicationLayerElement) {
                return COLOR_APPLICATION;
            }
            if(element instanceof ITechnologyLayerElement) {
                return COLOR_TECHNOLOGY;
            }
        }
        if(object instanceof ISketchModelActor) {
            return ColorConstants.black;
        }
        if(object instanceof IDiagramModelConnection) {
            return ColorConstants.black;
        }
        
        return ColorConstants.white;
    }

    /**
     * @param rgb
     * @return A String representation of RGB such as #00FF2D
     */
    public static String convertRGBToString(RGB rgb) {
        if(rgb == null) {
            return "";
        }
        
        StringBuffer sb = new StringBuffer("#");
        String s = Integer.toHexString(rgb.red);
        if(s.length() == 1) sb.append("0");
        sb.append(s);
        s = Integer.toHexString(rgb.green);
        if(s.length() == 1) sb.append("0");
        sb.append(s);
        s = Integer.toHexString(rgb.blue);
        if(s.length() == 1) sb.append("0");
        sb.append(s);
        
        return sb.toString();
    }

    /**
     * @param string
     * @return The RGB or null for String type #00FF2D
     */
    public static RGB convertStringToRGB(String string) {
        if(string == null || !(string.length() == 7) || !(string.startsWith("#")) ) {
            return null;
        }
        
        try {
            int red = Integer.parseInt(string.substring(1, 3), 16);
            int green = Integer.parseInt(string.substring(3, 5), 16);
            int blue = Integer.parseInt(string.substring(5, 7), 16);
            return new RGB(red, green, blue);
        }
        catch(NumberFormatException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    public static Color getDarkerColor(Color color) {
        return getDarkerColor(color, 0.9f);
    }
    
    public static Color getDarkerColor(Color color, float factor) {
        if(color == null) {
            return null;
        }
        RGB rgb = new RGB((int)(color.getRed() * factor), (int)(color.getGreen() * factor), (int)(color.getBlue() * factor));
        return get(convertRGBToString(rgb));
    }

    public static Color getLighterColor(Color color) {
        return getLighterColor(color, 0.9f);
    }
    
    public static Color getLighterColor(Color color, float factor) {
        if(color == null) {
            return null;
        }
        
        RGB rgb = new RGB(Math.max(2,
                Math.min((int) (color.getRed() / factor), 255)), Math.max(2,
                Math.min((int) (color.getGreen() / factor), 255)), Math.max(2,
                Math.min((int) (color.getBlue() / factor), 255)));
        
        return get(convertRGBToString(rgb));
    }
}
