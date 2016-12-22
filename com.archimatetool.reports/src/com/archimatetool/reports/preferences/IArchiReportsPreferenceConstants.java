/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.preferences;




/**
 * Constant definitions for plug-in preferences
 *
 * @author Tun Schlechter
 */
public interface IArchiReportsPreferenceConstants {       
    
    // PREFERENCES DENOMINATIONS 
    String VIEWS_SHOW_DOCUMENTATION = "VIEWS_SHOW_DOCUMENTATION"; //$NON-NLS-1$
    String VIEWS_SHOW_PROPERTIES= "VIEWS_SHOW_PROPERTIES";        //$NON-NLS-1$
    String VIEWS_SHOW_ELEMENTS  = "VIEWS_SHOW_ELEMENTS";          //$NON-NLS-1$

    String VIEWS_DEFAULT_TAB = "VIEWS_DEFAULT_TAB";               //$NON-NLS-1$

    String ELEMENTS_SHOW_DOCUMENTATION = "ELEMENTS_SHOW_DOCUMENTATION";  //$NON-NLS-1$
    String ELEMENTS_SHOW_PROPERTIES= "ELEMENTS_SHOW_PROPERTIES";         //$NON-NLS-1$
    String ELEMENTS_SHOW_VIEWS = "ELEMENTS_SHOW_VIEWS";                  //$NON-NLS-1$

    String ELEMENTS_DEFAULT_TAB = "ELEMENTS_DEFAULT_TAB";                //$NON-NLS-1$

    // PREFERENCES VALUES
    String DOCUMENTATION = "Documentation";  //$NON-NLS-1$
    String PROPERTIES = "Properties";        //$NON-NLS-1$
    String VIEWS = "Views";                  //$NON-NLS-1$
    String ELEMENTS = "Elements";            //$NON-NLS-1$
     
    // DEFAULT VALUES
    Boolean VIEWS_SHOW_DOCUMENTATION_DEFAULT_VALUE = true;
    Boolean VIEWS_SHOW_PROPERTIES_DEFAULT_VALUE= true;
    Boolean VIEWS_SHOW_ELEMENTS_DEFAULT_VALUE  = true;

    String VIEWS_DEFAULT_TAB_DEFAULT_VALUE = DOCUMENTATION;

    Boolean ELEMENTS_SHOW_DOCUMENTATION_DEFAULT_VALUE = true;
    Boolean ELEMENTS_SHOW_PROPERTIES_DEFAULT_VALUE= true;
    Boolean ELEMENTS_SHOW_VIEWS_DEFAULT_VALUE = true;

    String ELEMENTS_DEFAULT_TAB_DEFAULT_VALUE = DOCUMENTATION;

}
