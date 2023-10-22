/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.utils.PlatformUtils;




/**
 * Workaround for Mac Sonoma displaying "NewAppplication" main menu instead of app name
 * 
 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/779
 * 
 * Inspired by https://github.com/knime/knime-product/commit/bb0bd4498d7844df9c897ad984f028ff80d208f0
 * 
 * TODO: Remove this after Eclipe 4.30
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MacMenuFix {

    static void apply(Display display) throws Exception {
        if(!PlatformUtils.isMac()) {
            return;
        }
        
        try {
            // Get the Display#application field
            Field applicationField = display.getClass().getDeclaredField("application");
            applicationField.setAccessible(true);
            Object application = applicationField.get(display);
            
            // NSMenu mainmenu = application.mainMenu(); 
            Method mainMenuMethod = getMethod(application, "mainMenu");
            Object mainmenu = mainMenuMethod.invoke(application);
            
            // NSMenuItem appitem = mainmenu.itemAtIndex(0);
            Method itemAtIndexMethod = getMethod(mainmenu, "itemAtIndex", long.class);
            Object appItem = itemAtIndexMethod.invoke(mainmenu, 0);
            
            if(appItem != null) {
                // NSMenu sm = appitem.submenu();
                Method subMenuMethod = getMethod(appItem, "submenu");
                Object sm = subMenuMethod.invoke(appItem);
                
                // NSString name = appItem.title();
                Method titleMethod = getMethod(appItem, "title");
                Object name = titleMethod.invoke(appItem);
                
                // sm.setTitle(name);
                Method setTitleMethod = getMethod(sm, "setTitle", name.getClass());
                setTitleMethod.invoke(sm, name);
            }
        }
        catch(Exception ex) {
            Logger.logError("Could not apply Mac Menu fix", ex);
        }
    }
    
    private static Method getMethod(Object obj, String name, Class<?>... parameters) throws NoSuchMethodException {
        return obj.getClass().getDeclaredMethod(name, parameters);
    }
}
