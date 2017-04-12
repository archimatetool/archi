package com.archimatetool.model.relationships;

import com.archimatetool.model.IArchimateRelationship;

/**
 * IRelationship
 * 
 * @author Stig B. Dørmænen
 */

public interface IRelationship {
	
    /**
     * @return The internal ID of the relationship
     */
    String getID();

    /**
     * @return The name of the relationship
     */
    String getName();

    /**
     * @return True if the relationship is filtered
     */
	boolean isFiltered(IArchimateRelationship relationship);

}
