/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;

import java.util.List;

/**
 * Constant definitions for CSV
 * 
 * @author Phillip Beauvoir
 */
public interface CSVConstants {
    
    String ARCHIMATE_MODEL_TYPE = "ArchimateModel"; //$NON-NLS-1$
    
    @SuppressWarnings("nls")
    String[] MODEL_ELEMENTS_HEADER = {
            "ID", "Type", "Name", "Documentation", "Specialization"
    };
    
    @SuppressWarnings("nls")
    String[] RELATIONSHIPS_HEADER = {
        "ID", "Type", "Name", "Documentation", "Source", "Target", "Specialization"
    };

    @SuppressWarnings("nls")
    String[] PROPERTIES_HEADER = {
        "ID", "Key", "Value"
    };
    
    String ELEMENTS_FILENAME = "elements"; //$NON-NLS-1$
    String RELATIONS_FILENAME = "relations"; //$NON-NLS-1$
    String PROPERTIES_FILENAME = "properties"; //$NON-NLS-1$
    String FILE_EXTENSION = ".csv"; //$NON-NLS-1$
    
    String CRLF = "\r\n"; //$NON-NLS-1$
    
    char[] DELIMITERS = { ',', ';', '\t' };
    String[] DELIMITER_NAMES = { Messages.CSVConstants_15, Messages.CSVConstants_16, Messages.CSVConstants_17 };
    
    @SuppressWarnings("nls")
    String[] ENCODINGS = { "UTF-8", "UTF-8 BOM", "ANSI" };
    
    String INFLUENCE_STRENGTH = Messages.CSVConstants_18;
    
    String ACCESS_TYPE = Messages.CSVConstants_19;
    List<String> ACCESS_TYPES = 
            List.of(Messages.CSVConstants_20, Messages.CSVConstants_21, Messages.CSVConstants_22, Messages.CSVConstants_23);
    
    String ASSOCIATION_DIRECTED = Messages.CSVConstants_24;
    
    String JUNCTION_TYPE = Messages.CSVConstants_25;
    String JUNCTION_OR = Messages.CSVConstants_26;
    String JUNCTION_AND = Messages.CSVConstants_27;
}
