/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;

/**
 * Utilities for {@link org.eclipse.gef.Tool Tools}.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ToolUtilities {

    /**
     * Returns a list containing the top level selected edit parts based on the
     * viewer's selection.
     * 
     * @param viewer
     *            the viewer
     * @return the selection excluding dependants
     */
    public static List getSelectionWithoutDependants(EditPartViewer viewer) {
        List selectedParts = viewer.getSelectedEditParts();
        List result = new ArrayList();
        for (int i = 0; i < selectedParts.size(); i++) {
            EditPart editpart = (EditPart) selectedParts.get(i);
            if (!isAncestorContainedIn(selectedParts, editpart))
                result.add(editpart);
        }
        return result;
    }

    /**
     * Returns a list containing the top level selected edit parts based on the
     * passed in list of selection.
     * 
     * @param selectedParts
     *            the complete selection
     * @return the selection excluding dependants
     */
    public static List getSelectionWithoutDependants(List selectedParts) {
        List result = new ArrayList();
        for (int i = 0; i < selectedParts.size(); i++) {
            EditPart editpart = (EditPart) selectedParts.get(i);
            if (!isAncestorContainedIn(selectedParts, editpart))
                result.add(editpart);
        }
        return result;
    }

    /**
     * Filters the given list of EditParts so that the list only contains the
     * EditParts that understand the given request (i.e. return
     * <code>true</code> from {@link EditPart#understandsRequest(Request)} when
     * passed the given request).
     * 
     * @param list
     *            the list of edit parts to filter
     * @param request
     *            the request
     */
    public static void filterEditPartsUnderstanding(List list, Request request) {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            EditPart ep = (EditPart) iter.next();
            if (!ep.understandsRequest(request))
                iter.remove();
        }
    }

    /**
     * Checks if collection contains any ancestor of editpart <code>ep</code>
     * 
     * @param c
     *            - collection of editparts
     * @param ep
     *            - the editparts to check ancestors for
     * @return <code>true</code> if collection contains any ancestor(s) of the
     *         editpart <code>ep</code>
     * @since 3.6
     */
    public static boolean isAncestorContainedIn(Collection c, EditPart ep) {
        ep = ep.getParent();
        while (ep != null) {
            if (c.contains(ep))
                return true;
            ep = ep.getParent();
        }
        return false;
    }

    /**
     * Returns the common parent editpart for given pair of EditParts. If the
     * two parts are identical, the result is that part. If the two parts do not
     * have a common ancestor, some form of RuntimeException will be thrown.
     * 
     * @since 3.1
     * @param ll
     *            the first editpart
     * @param rr
     *            the second editpart
     * @return the editpart which is the common ancestor.
     */
    public static EditPart findCommonAncestor(EditPart ll, EditPart rr) {
        if (ll == rr)
            return ll;
        ArrayList leftAncestors = new ArrayList();
        ArrayList rightAncestors = new ArrayList();
        EditPart l = ll;
        EditPart r = rr;
        while (l != null) {
            leftAncestors.add(l);
            l = l.getParent();
        }
        while (r != null) {
            rightAncestors.add(r);
            r = r.getParent();
        }

        int il = leftAncestors.size() - 1;
        int ir = rightAncestors.size() - 1;
        do {
            if (leftAncestors.get(il) != rightAncestors.get(ir))
                break;
            il--;
            ir--;
        } while (il >= 0 && ir >= 0);

        return (EditPart) leftAncestors.get(il + 1);
    }

}
