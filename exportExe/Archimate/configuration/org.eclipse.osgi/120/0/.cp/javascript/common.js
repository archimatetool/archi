/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * The following function toggles between block and none display
 * for the element with a given id. It also toggles between
 * open and closed images for the folding section.
 * Returns false to stop the browser from following the link.
 */
function toggleSection(id, closedId, openId) {
	if (document.getElementById) {
   		var element = document.getElementById(id);
   		var openImage = document.getElementById(openId);
   		var closedImage = document.getElementById(closedId);
   		if (element.style.display=="block") {
   			// hide the client block
	   		element.style.display="none";
	   		// switch toggle images
   			if (openImage)
   				openImage.style.display="none";
			if (closedImage)
				closedImage.style.display="inline";
	   	}
	   	else {
	   		// turn it on and show
	   		element.style.display="block";
	   		// switch toggle images
   			if (openImage)
   				openImage.style.display="inline";
   			if (closedImage)
   				closedImage.style.display="none";
	   	}
   	}
   	return false;
}