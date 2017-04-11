package com.archimatetool.model.relationships;

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

}
