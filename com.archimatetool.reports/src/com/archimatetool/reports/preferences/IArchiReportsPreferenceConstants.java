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
    String VIEWS_SHOW_DOCUMENTATION = "VIEWS_SHOW_DOCUMENTATION";
    String VIEWS_SHOW_PROPERTIES= "VIEWS_SHOW_PROPERTIES";
    String VIEWS_SHOW_ELEMENTS  = "VIEWS_SHOW_ELEMENTS";

    String VIEWS_DEFAULT_TAB = "VIEWS_DEFAULT_TAB";

    String ELEMENTS_SHOW_DOCUMENTATION = "ELEMENTS_SHOW_DOCUMENTATION";
    String ELEMENTS_SHOW_PROPERTIES= "ELEMENTS_SHOW_PROPERTIES";
    String ELEMENTS_SHOW_VIEWS = "ELEMENTS_SHOW_VIEWS";

    String ELEMENTS_DEFAULT_TAB = "ELEMENTS_DEFAULT_TAB";

    // DEFAULT VALUES
    Boolean VIEWS_SHOW_DOCUMENTATION_DEFAULT_VALUE = true;
    Boolean VIEWS_SHOW_PROPERTIES_DEFAULT_VALUE= true;
    Boolean VIEWS_SHOW_ELEMENTS_DEFAULT_VALUE  = true;

    String VIEWS_DEFAULT_TAB_DEFAULT_VALUE = EArchiReportsTabs.Documentation.toString();

    Boolean ELEMENTS_SHOW_DOCUMENTATION_DEFAULT_VALUE = true;
    Boolean ELEMENTS_SHOW_PROPERTIES_DEFAULT_VALUE= true;
    Boolean ELEMENTS_SHOW_VIEWS_DEFAULT_VALUE = true;

    String ELEMENTS_DEFAULT_TAB_DEFAULT_VALUE = EArchiReportsTabs.Documentation.toString();

}
