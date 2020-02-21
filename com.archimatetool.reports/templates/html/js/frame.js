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

function createLinks(text) {
	return text.replace(/(\w+:\/\/\S+)/g, '<a href="$1" target="_blank">$1</a>')
          .replace(/mailto:(\S+)/g, '<a href="mailto:$1">$1</a>');
}

// Hints URLs
var hints = {
// Hints for viewpoints
    viewpoint_: "view.html",
	viewpoint_application_cooperation: "vp_application_cooperation.html",
    viewpoint_application_usage: "vp_application_usage.html",
    viewpoint_business_process_cooperation: "vp_business_process_cooperation.html",
    viewpoint_capability: "vp_capability.html",
    viewpoint_goal_realization: "vp_goal_realization.html",
    viewpoint_implementation_deployment: "vp_implementation_deployment.html",
    viewpoint_implementation_migration: "vp_implementation_migration.html",
    viewpoint_information_structure: "vp_information_structure.html",
    viewpoint_layered: "vp_layered.html",
    viewpoint_migration: "vp_migration.html",
    viewpoint_motivation: "vp_motivation.html",
    viewpoint_organization: "vp_organization.html",
    viewpoint_outcome_realization: "vp_outcome_realization.html",
    viewpoint_physical: "vp_physical.html",
    viewpoint_product: "vp_product.html",
    viewpoint_project: "vp_project.html",
    viewpoint_requirements_realization: "vp_requirements_realization.html",
    viewpoint_resource: "vp_resource.html",
    viewpoint_service_realization: "vp_service_realization.html",
    viewpoint_stakeholder: "vp_stakeholder.html",
    viewpoint_strategy: "vp_strategy.html",
    viewpoint_technology: "vp_technology.html",
	viewpoint_technology_usage: "vp_technology_usage.html",
	viewpoint_value_stream: "vp_value_stream.html",
// Hints for concepts
    ArchimateModel: "model.html",
	AccessRelationship: "access.html",
	AggregationRelationship: "aggregation.html",
	ApplicationCollaboration: "application_collaboration.html",
	ApplicationComponent: "application_component.html",
    ApplicationEvent: "application_event.html",
	ApplicationFunction: "application_function.html",
	ApplicationInteraction: "application_interaction.html",
	ApplicationInterface: "application_interface.html",
    ApplicationProcess: "application_process.html",
	ApplicationService: "application_service.html",
	Artifact: "artifact.html",
	Assessment: "assessment.html",
	AssignmentRelationship: "assignment.html",
	AssociationRelationship: "association.html",
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
	Capability: "capability.html",
    CommunicationNetwork: "communication_network.html",
	CompositionRelationship: "composition.html",
	Constraint: "constraint.html",
	Contract: "contract.html",
	CourseOfAction: "course_of_action.html",
	DataObject: "data_object.html",
	Deliverable: "deliverable.html",
	Device: "device.html",
	DistributionNetwork: "distribution_network.html",
	Driver: "driver.html",
	Equipment: "equipment.html",
	Facility: "facility.html",
	FlowRelationship: "flow.html",
	Gap: "gap.html",
	Goal: "goal.html",
	Grouping: "grouping.html",
	ImplementationEvent: "implementation_event.html",
	InfluenceRelationship: "influence.html",
    Junction: "junction.html",
	Location: "location.html",
	Material: "material.html",
	Meaning: "meaning.html",
	Node: "node.html",
	Outcome: "outcome.html",
    Path: "path.html",
	Plateau: "plateau.html",
	Principle: "principle.html",
	Product: "product.html",
	RealizationRelationship: "realization.html",
	Representation: "representation.html",
	Requirement: "requirement.html",
	Resource: "resource.html",
    ServingRelationship: "serving.html",
	SpecializationRelationship: "specialization.html",
	Stakeholder: "stakeholder.html",
	SystemSoftware: "system_software.html",
    TechnologyCollaboration: "technology_collaboration.html",
    TechnologyEvent: "technology_event.html",
    TechnologyFunction: "technology_function.html",
    TechnologyInteraction: "technology_interaction.html",
    TechnologyInterface: "technology_interface.html",
    TechnologyProcess: "technology_process.html",
    TechnologyService: "technology_service.html",
	TriggeringRelationship: "triggering.html",
	Value: "value.html",
	ValueStream: "value_stream.html",
	WorkPackage: "workpackage.html",
// Hints for graphical objects
	DiagramModelNote: "note.html",
	DiagramModelGroup: "group.html",
// Hints for sketch objects
	SketchModel: "sketch.html",
	SketchModelSticky: "sketch_sticky.html",
	SketchModelActor: "sketch_actor.html",
// Hints for canvas objects
	CanvasModel: "canvas_diagram.html",
	CanvasModelBlock: "canvas_block.html",
	CanvasModelImage: "canvas_image.html",
	CanvasModelSticky: "canvas_sticky.html",
};

$(document).ready(function() {
	// Set heigh of panels the first time
	setRootPanelHeight();
	
	// Compute panel height on resize
	$(window).resize(function (e) {
		setRootPanelHeight();
		e.stopPropagation();
	});
	
	// Create links in this class
    $('.convertlinks').each(function() {
        $(this).html(createLinks($(this).text()));
    });

	// Replace Hint URL
	for (var id in hints) {
		if (document.getElementById('hint-'+id) != null)
			document.getElementById('hint-'+id).href = "../../hints/"+hints[id];
	}
	
	// Sort 'elements' and 'properties' tables
	var tabElements = $('#elements > table > tbody');
	var tabElementsRows = tabElements.children('tr');
	tabElementsRows.sort(strcmp).appendTo(tabElements);
	var tabProperties = $('#properties > table > tbody');
	var tabPropertiesRows = tabProperties.children('tr');
	tabPropertiesRows.sort(strcmp).appendTo(tabProperties);
});
