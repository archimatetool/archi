package com.archimatetool.model.relationships;

public class Relationship implements IRelationship {
    
	private String id;
    private String name;
    
	
    Relationship(String id, String name) {
        this.id = id;
        this.name = name;
    }

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
