package com.archimatetool.model.relationships;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IFlowRelationship;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IRealizationRelationship;
import com.archimatetool.model.IServingRelationship;
import com.archimatetool.model.ISpecializationRelationship;
import com.archimatetool.model.ITriggeringRelationship;

public class Relationship implements IRelationship {
    
	private String id;
    private String name;
    
	
    Relationship(String id, String name) {
        this.id = id;
        this.name = name;
    }

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isFiltered(IArchimateRelationship relationship) {
		if (this.name.contains(RelationshipManager.NONE_RELATIONSHIPS.getName())) {
			return false;
		}
		return checkIfFiltered(relationship);
	}

	private boolean checkIfFiltered(IArchimateRelationship rel) {
		if (this.id.contains("AccessRelationship") && rel instanceof IAccessRelationship) {
			return false;
		} else if (this.id.contains("CompositionRelationship") && rel instanceof ICompositionRelationship) {
			return false;
		} else if (this.id.contains("FlowRelationship") && rel instanceof IFlowRelationship) {
			return false;
		} else if (this.id.contains("AggregationRelationship") && rel instanceof IAggregationRelationship) {
			return false;
		} else if (this.id.contains("AssignmentRelationship") && rel instanceof IAssignmentRelationship) {
			return false;
		} else if (this.id.contains("InfluenceRelationship") && rel instanceof IInfluenceRelationship) {
			return false;
		} else if (this.id.contains("AssociationRelationship") && rel instanceof IAssociationRelationship) {
			return false;
		} else if (this.id.contains("RealizationRelationship") && rel instanceof IRealizationRelationship) {
			return false;
		} else if (this.id.contains("SpecializationRelationship") && rel instanceof ISpecializationRelationship) {
			return false;
		} else if (this.id.contains("TriggeringRelationship") && rel instanceof ITriggeringRelationship) {
			return false;
		} else if (this.id.contains("ServingRelationship") && rel instanceof IServingRelationship) {
			return false;
		}
		return true;
	}
}
