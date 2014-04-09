/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.factory.ElementUIFactory;
import com.archimatetool.editor.ui.factory.IElementUIProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILineObject;



/**
 * Color Factory
 * 
 * @author Phillip Beauvoir
 */
public class ColorFactory {
    
    public static final Color COLOR_BUSINESS = new Color(null, 255, 255, 181);
    public static final Color COLOR_APPLICATION = new Color(null, 181, 255, 255);
    public static final Color COLOR_TECHNOLOGY = new Color(null, 201, 231, 183);
    
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
     * @param component
     * Set user default colors as set in prefs for a model object
     */
    public static void setDefaultColors(IDiagramModelComponent component) {
        // If user Prefs is to *not* save default colours in file
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
            return;
        }
        
        // Fill color
        if(component instanceof IDiagramModelObject) {
            IDiagramModelObject dmo = (IDiagramModelObject)component;
            Color fillColor = getDefaultFillColor(dmo);
            if(fillColor != null) {
                dmo.setFillColor(convertColorToString(fillColor));
            }
        }
        
        // Line color
        if(component instanceof ILineObject) {
            ILineObject lo = (ILineObject)component;
            Color lineColor = getDefaultLineColor(lo);
            if(lineColor != null) {
                lo.setLineColor(convertColorToString(lineColor));
            }
        }
    }

    /**
     * @param object
     * @return A default fill Color for an object with reference to the user's preferences.
     * This is used when a fillColor is set to null
     */
    public static Color getDefaultFillColor(Object object) {
        Color color = getUserDefaultFillColor(object);
        if(color == null) {
            color = getInbuiltDefaultFillColor(object);
        }
        
        return color;
    }
    
    /**
     * @return A fill Color for an object with reference to the user's preferences or null if not set
     */
    public static Color getUserDefaultFillColor(Object object) {
        EClass eClass = getEClassForObject(object);
        
        if(eClass != null) {
            // User preference
            String value = Preferences.STORE.getString(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX + eClass.getName());
            if(StringUtils.isSet(value)) {
                return get(value);
            }
        }
        
        return null;
    }
    
    /**
     * @param object
     * @return A default fill Color for an object that is inbuilt in the App
     */
    public static Color getInbuiltDefaultFillColor(Object object) {
        EClass eClass = getEClassForObject(object);
        
        if(eClass != null) {
            IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
            if(provider != null) {
                return provider.getDefaultColor() == null ? ColorConstants.white : provider.getDefaultColor();
            }
        }
        
        return ColorConstants.white;
    }
    
    ///-------------------------------------------------------------------------
    
    /**
     * @param object
     * @return A default line Color for an object with reference to the user's preferences.
     */
    public static Color getDefaultLineColor(Object object) {
        Color color = getUserDefaultLineColor(object);
        if(color == null) {
            color = getInbuiltDefaultLineColor(object);
        }
        
        return color;
    }

    public static Color getUserDefaultLineColor(Object object) {
        EClass eClass = getEClassForObject(object);
        
        if(IArchimatePackage.eINSTANCE.getDiagramModelConnection().isSuperTypeOf(eClass) ||
                IArchimatePackage.eINSTANCE.getRelationship().isSuperTypeOf(eClass)) {
            // User preference
            String value = Preferences.STORE.getString(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR);
            if(StringUtils.isSet(value)) {
                return get(value);
            }
        }
        else {
            // User preference
            String value = Preferences.STORE.getString(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR);
            if(StringUtils.isSet(value)) {
                return get(value);
            }
        }
       
        return null;
    }

    /**
     * @param object
     * @return A default line Color for an object that is inbuilt in the App
     */
    public static Color getInbuiltDefaultLineColor(Object object) {
        EClass eClass = getEClassForObject(object);
        
        if(eClass != null) {
            IElementUIProvider provider = ElementUIFactory.INSTANCE.getProvider(eClass);
            if(provider != null) {
                return provider.getDefaultLineColor() == null ? ColorConstants.black : provider.getDefaultLineColor();
            }
        }
        
        return ColorConstants.black;
    }

    /*
     * Get at the EClass for an Object
     */
    private static EClass getEClassForObject(Object object) {
        EClass eClass = null;
        
        if(object instanceof EClass) {
            eClass = (EClass)object;
        }
        else if(object instanceof IDiagramModelArchimateObject) {
            eClass = ((IDiagramModelArchimateObject)object).getArchimateElement().eClass();
        }
        else if(object instanceof IDiagramModelArchimateConnection) {
            eClass = ((IDiagramModelArchimateConnection)object).getRelationship().eClass();
        }
        else if(object instanceof EObject) {
            eClass = ((EObject)object).eClass();
        }

        return eClass;
    }
    
    /**
     * @param rgb
     * @return integer pixel value of RGB
     */
    public static int getPixelValue(RGB rgb) {
        return (rgb.red << 16) | (rgb.green << 8) | rgb.blue;
    }
    
    /**
     * @param color
     * @return A String representation of color such as #00FF2D
     */
    public static String convertColorToString(Color color) {
        if(color == null) {
            return ""; //$NON-NLS-1$
        }
        return convertRGBToString(color.getRGB());
    }

    /**
     * @param rgb
     * @return A String representation of rgb such as #00FF2D
     */
    public static String convertRGBToString(RGB rgb) {
        if(rgb == null) {
            return ""; //$NON-NLS-1$
        }
        
        StringBuffer sb = new StringBuffer("#"); //$NON-NLS-1$
        String s = Integer.toHexString(rgb.red);
        if(s.length() == 1) sb.append("0"); //$NON-NLS-1$
        sb.append(s);
        s = Integer.toHexString(rgb.green);
        if(s.length() == 1) sb.append("0"); //$NON-NLS-1$
        sb.append(s);
        s = Integer.toHexString(rgb.blue);
        if(s.length() == 1) sb.append("0"); //$NON-NLS-1$
        sb.append(s);
        
        return sb.toString();
    }

    /**
     * @param string
     * @return The RGB or null for String type #00FF2D
     */
    public static RGB convertStringToRGB(String string) {
        if(string == null || !(string.length() == 7) || !(string.startsWith("#")) ) { //$NON-NLS-1$
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
        
        if(factor > 1 || factor < 0) {
            factor = 1;
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
