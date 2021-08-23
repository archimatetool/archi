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

function toggleTreeEntry(listItem) {
	// Sort folder content (done here only once for performance reasons)
	if (! $(listItem).hasClass('sorted')) {
		$(listItem).find(' > ul').each(function(){
			// Sort folders first
			$(this).children('li.tree-folder').sort(strcmp).appendTo($(this));
			// Sort views after
			$(this).children('li.tree-element').sort(strcmp).appendTo($(this));
		});
		$(listItem).addClass('sorted');
	}
	
	if (isTreeFiltered()) {
		return;
	} else {
		var children = $(listItem).find(' > ul > li');
		if (children.is(":visible")) {
			children.hide('fast');
			// Toggle arrow icon
			$(listItem).find('> span > i').addClass('glyphicon-triangle-right').removeClass('glyphicon-triangle-bottom');
		} else {
			children.show('fast');
			// Toggle arrow icon
			$(listItem).find('> span > i').addClass('glyphicon-triangle-bottom').removeClass('glyphicon-triangle-right');
		}
	}
}

$(document).ready(function() {
	// Set jQuery UI Layout panes
  $('body').layout({
    minSize: 45,
    maskContents: true,
    north: {
      size: 45,
      spacing_open: 0,
      closable: false,
      resizable: false
    },
    west: {
			size: 350,
			spacing_open: 8
		},
    west__childOptions: {
      maskContents: true,
      south: {
	      minSize: 50,
				size: 250,
				spacing_open: 8
			},
			center: {
				minSize: 50,
				onresize: "setRootPanelHeight"
			}
    }
  });
	
	// Set heigh of panels the first time
	setRootPanelHeight();
	
	// Remove hidden nodes from the model tree
	$('.hide-true').remove();
	let topTreeFolders = $('.tree > li');
	topTreeFolders.each(function(index) {
		if (! $(this).find(' > ul > li').size()) {
			$(this).remove();
		}
	});
	
	
	// Setup modeltree
	$('.tree li:has(ul)').addClass('parent_li').find(' > ul > li').hide();

	// Add show/hide function on modeltree
	$('.tree li.parent_li > span').on('click', function (e) {
		toggleTreeEntry($(this).parent('li.parent_li'));
		e.stopPropagation();
	});
	
	// *** SEARCH ***
	appendSearchBar();
	
	
	// *** DEEP LINKS ***

	// Register a new onClick function
	let $viewLinks = $("a[href][target='view']");
	$viewLinks.on('click', function (event) {
		const id = getIdFromHref(event.currentTarget.href);
		setLocationForView(id);
		openViewFromLocation(false);
		event.stopPropagation();
		return false;
	});

	function setLocationForView(id) {
		const url = new URL(window.location);
		url.searchParams.set('view', id);
		window.history.pushState({}, '', url);
	}

	function getIdFromHref(href) {
		return href.split("/").pop().slice(0, -5);
	}

	function getIdFromLocation() {
		const url = new URL(window.location);
		return url.searchParams.get('view');
	}
	
	function openViewFromLocation(expandModelTree) {
		// Find matching view in model tree...
		const targetId = getIdFromLocation();
		const matchingLinks = $viewLinks.filter(function (index, element) {
			return getIdFromHref(element.href) === targetId;
		});
		const link = matchingLinks[0];

		if (link) {
			// View found in model tree. Loading it in frame
			const $link = $(link);
			$("iframe[name='view']").attr('src', $link.attr('href'));

			if (expandModelTree) {
				let spans = [];
				let $parentListItem = $link.parent().parent().parent();
				while ($parentListItem[0].tagName === 'LI') {
					spans.push($parentListItem.children().first());
					$parentListItem = $parentListItem.parent().parent();
				}
				while (spans.length) {
					spans.pop().click();
				}
			}
		}
	}

	$(window).on('message', function (e) {
		const id = e.originalEvent.data.split('=').pop();
		setLocationForView(id);
		//openViewFromLocation(true); 
	});
	
	// Load initial view id on page load
	openViewFromLocation(true); 

});


function appendSearchBar() {
	let newSearchDiv = '<div id="searchBox"><input type="text" id="tree-search" placeholder="Search..." /></div>';

	document.getElementsByClassName("panel-heading")[0].innerHTML += newSearchDiv;

	document.querySelector('#tree-search').onkeyup = function(e) {
		if (e.key !== 'Enter' && e.keyCode !== 13)
			return;
		else
			searchInViews();
	};
}

function isTreeFiltered() {
	return $('#tree-search').hasClass('filtered');
}

function searchInViews() {
	const filter = $('#tree-search').val();

	// Hide all entries
	listItems = $('.tree li');
	listItems.hide();
	listItems.find(' > span > i').addClass('glyphicon-triangle-right').removeClass('glyphicon-triangle-bottom');

	// Is a filter set?
	if (filter.length === 0) {
		// No: show the top level entries ('Model Content' and 'Views') and stop here
		$('.tree > li').show();
		$('#tree-search').removeClass('filtered');
		document.querySelector('#tree-search').title = "";
		return;
	}
	
	// Yes: set the 'filtered' flag and filter the model tree
	$('#tree-search').addClass('filtered');
	document.querySelector('#tree-search').title = "To clear filter, empty this field and press ENTER";

	// Get model tree
	let modelTree = $('.tree');

	// Case insensitive search (a 'li' matches if itself or its children match)
	let foundItems = modelTree.find("li").filter(function () {
		let reg = new RegExp(filter, "ig");
		let content = $(this).hasClass('tree-element') ? $(this) : $(this).find('li.tree-element');
		return reg.test(content.text());
	});

	// Show matching entries
	foundItems.show();
	foundItems.parent("ul").parent("li").find("> span > i").addClass('glyphicon-triangle-bottom').removeClass('glyphicon-triangle-right');
}