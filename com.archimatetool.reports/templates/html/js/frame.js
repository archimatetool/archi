function setRootPanelHeight() {
	$('.root-panel-body').css('height', $('.root-panel').outerHeight() - $('.root-panel-heading').outerHeight());
}

function strcmp(a, b){
	var aText = $(a).text().trim().toLowerCase();
	var bText = $(b).text().trim().toLowerCase();
	if (aText.toString() < bText.toString()) return -1;
  if (aText.toString() > bText.toString()) return 1;
  return 0;
}

// Hints URLs
var hints = {
// Hints for viewpoints
	viewpoint0: "viewpoint_total.html",
	viewpoint1: "viewpoint_actorcooperation.html",
	viewpoint2: "viewpoint_applicationbehaviour.html",
	viewpoint3: "viewpoint_applicationcooperation.html",
	viewpoint4: "viewpoint_applicationstructure.html",
	viewpoint5: "viewpoint_applicationusage.html",
	viewpoint6: "viewpoint_businessfunction.html",
	viewpoint7: "viewpoint_businessprocesscoop.html",
	viewpoint8: "viewpoint_businessprocess.html",
	viewpoint9: "viewpoint_businessproduct.html",
	viewpoint10: "viewpoint_impldeploy.html",
	viewpoint25: "viewpoint_implmigration.html",
	viewpoint11: "viewpoint_infostructure.html",
	viewpoint12: "viewpoint_infrausage.html",
	viewpoint13: "viewpoint_infra.html",
	viewpoint14: "viewpoint_layered.html",
	viewpoint15: "viewpoint_organisation.html",
	viewpoint16: "viewpoint_servicereal.html",
	viewpoint17: "viewpoint_stakeholder.html",
	viewpoint18: "viewpoint_goalrealisation.html",
	viewpoint19: "viewpoint_goalcontribution.html",
	viewpoint20: "viewpoint_principles.html",
	viewpoint21: "viewpoint_requirementsrealisation.html",
	viewpoint22: "viewpoint_motivation.html",
	viewpoint23: "viewpoint_project.html",
	viewpoint24: "viewpoint_migration.html",
// Hints for elements
	AccessRelationship: "access.html",
	AggregationRelationship: "aggregation.html",
	AndJunction: "and_junction.html",
	ApplicationCollaboration: "application_collaboration.html",
	ApplicationComponent: "application_component.html",
	ApplicationFunction: "application_function.html",
	ApplicationInteraction: "application_interaction.html",
	ApplicationInterface: "application_interface.html",
	ApplicationService: "application_service.html",
	// ArchimateComponent: ".html",
	// ArchimateDiagramModel: ".html",
	// ArchimateElement: ".html",
	// ArchimateFactory: ".html",
	// ArchimateModel: ".html",
	// ArchimatePackage: ".html",
	Artifact: "artifact.html",
	Assessment: "assessment.html",
	AssignmentRelationship: "assignment.html",
	AssociationRelationship: "association.html",
	// Bounds: ".html",
	// BusinessActivity: "business_activity.html",
	BusinessActor: "business_actor.html",
	BusinessCollaboration: "business_collaboration.html",
	BusinessEvent: "business_event.html",
	BusinessFunction: "business_function.html",
	BusinessInteraction: "business_interaction.html",
	BusinessInterface: "business_interface.html",
	BusinessObject: "business_object.html",
	BusinessProcess: "business_process.html",
	BusinessRole: "business_role.html",
	BusinessService: "business_service.html",
	CommunicationPath: "communication_path.html",
	CompositionRelationship: "composition.html",
	Constraint: "constraint.html",
	Contract: "contract.html",
	DataObject: "data_object.html",
	Deliverable: "deliverable.html",
	Device: "device.html",
	// DiagramModelArchimateConnection: ".html",
	// DiagramModelArchimateObject: ".html",
	// DiagramModelBendpoint: ".html",
	// DiagramModelComponent: ".html",
	// DiagramModelConnection: ".html",
	// DiagramModelGroup: ".html",
	// DiagramModelImage: ".html",
	// DiagramModel: ".html",
	// DiagramModelNote: ".html",
	// DiagramModelObject: ".html",
	// DiagramModelReference: ".html",
	Driver: "driver.html",
	FlowRelationship: "flow.html",
	// Folder: ".html",
	Gap: "gap.html",
	Goal: "goal.html",
	// ImplementationMigrationElement: ".html",
	InfluenceRelationship: "influence.html",
	InfrastructureFunction: "infrastructure_function.html",
	InfrastructureInterface: "infrastructure_interface.html",
	InfrastructureService: "infrastructure_service.html",
	Junction: "junction.html",
	Location: "location.html",
	Meaning: "meaning.html",
	// Metadata: ".html",
	// MotivationElement: ".html",
	Network: "network.html",
	Node: "node.html",
	OrJunction: "or_junction.html",
	Plateau: "plateau.html",
	Principle: "principle.html",
	Product: "product.html",
	// Property: ".html",
	RealisationRelationship: "realisation.html",
	// Relationship: ".html",
	Representation: "representation.html",
	Requirement: "requirement.html",
	// SketchModelActor: ".html",
	// SketchModel: ".html",
	// SketchModelSticky: ".html",
	SpecialisationRelationship: "specialisation.html",
	Stakeholder: "stakeholder.html",
	SystemSoftware: "system_software.html",
	TriggeringRelationship: "triggering.html",
	UsedByRelationship: "used_by.html",
	Value: "value.html",
	WorkPackage: "workpackage.html"
};

$(document).ready(function() {
	// Set heigh of panels the first time
	setRootPanelHeight();
	
	// Compute panel height on resize
	$(window).resize(function (e) {
		setRootPanelHeight();
		e.stopPropagation();
	});
	
	// Update documentation div and create links
	$('#doctgt').text($('#docsrc').html());
	$('#doctgt').html($('#doctgt').html()
		.replace(/(\w+:\/\/\S+)/g, '<a href="$1" target="_blank">$1</a>')
		.replace(/mailto:(\S+)/g, '<a href="mailto:$1">$1</a>')
	);
	
	// Replace Hint URL
	for (var id in hints) {
		if (document.getElementById('hint-'+id) != null)
			document.getElementById('hint-'+id).href = "../hints/"+hints[id];
	}
	
	// Sort 'elements' and 'properties' tables
	var tabElements = $('#elements > table > tbody');
	var tabElementsRows = tabElements.children('tr');
	tabElementsRows.sort(strcmp).appendTo(tabElements);
	var tabProperties = $('#elements > table > tbody');
	var tabPropertiesRows = tabProperties.children('tr');
	tabPropertiesRows.sort(strcmp).appendTo(tabProperties);
});
