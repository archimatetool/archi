/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;




/**
 * Constant definitions for CSV
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface CSVConstants {
    
    String ARCHIMATE_MODEL_TYPE = "ArchimateModel";
    
    String[] MODEL_ELEMENTS_HEADER = {
            "ID", "Type", "Name", "Documentation"
    };
    
    String[] RELATIONSHIPS_HEADER = {
        "ID", "Type", "Name", "Documentation", "Source", "Target"
    };

    String[] PROPERTIES_HEADER = {
        "ID", "Key", "Value"
    };
    
    String ELEMENTS_FILENAME = "elements";
    String RELATIONS_FILENAME = "relations";
    String PROPERTIES_FILENAME = "properties";
    String FILE_EXTENSION = ".csv";
    
    String CRLF = "\r\n";
    
    char[] DELIMITERS = { ',', ';', '\t' };
    String[] DELIMITER_NAMES = { "comma", "semicolon", "tab" };
    
    String[] ENCODINGS = { "UTF-8", "ANSI" };
}
