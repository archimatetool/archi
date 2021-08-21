/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.algorithms.internal;

import java.util.Comparator;
import java.util.TreeSet;

import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicScreen {

    private TreeSet XCoords = null;
    private TreeSet YCoords = null;

    private DisplayIndependentRectangle screenBounds = null;

    double minX = 0.0;
    double minY = 0.0;
    double maxX = 0.0;
    double maxY = 0.0;

    public void cleanScreen() {
        minX = 0.0;
        minY = 0.0;
        maxX = 0.0;
        maxY = 0.0;
    }

    class XComparator implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            InternalNode n1 = (InternalNode) arg0;
            InternalNode n2 = (InternalNode) arg1;
            if (n1.getInternalX() > n2.getInternalX())
                return +1;
            else if (n1.getInternalX() < n2.getInternalX())
                return -1;
            else {
                return n1.toString().compareTo(n2.toString());
            }

        }
    }

    class YComparator implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            InternalNode n1 = (InternalNode) arg0;
            InternalNode n2 = (InternalNode) arg1;
            if (n1.getInternalY() > n2.getInternalY())
                return +1;
            else if (n1.getInternalY() < n2.getInternalY())
                return -1;
            else {
                return n1.toString().compareTo(n2.toString());
            }

        }
    }

    public DynamicScreen(int x, int y, int width, int height) {
        XCoords = new TreeSet(new XComparator());
        YCoords = new TreeSet(new YComparator());

        this.screenBounds = new DisplayIndependentRectangle(x, y, width, height);
    }

    public void removeNode(InternalNode node) {
        XCoords.remove(node);
        YCoords.remove(node);
    }

    public void addNode(InternalNode node) {
        XCoords.add(node);
        YCoords.add(node);
    }

    public DisplayIndependentPoint getScreenLocation(InternalNode node) {

        DisplayIndependentRectangle layoutBounds = calculateBounds();

        double x = (layoutBounds.width == 0) ? 0 : (node.getInternalX() - layoutBounds.x) / layoutBounds.width;
        double y = (layoutBounds.height == 0) ? 0 : (node.getInternalY() - layoutBounds.y) / layoutBounds.height;

        x = screenBounds.x + x * screenBounds.width;
        y = screenBounds.y + y * screenBounds.height;

        return new DisplayIndependentPoint(x, y);
    }

    public DisplayIndependentPoint getVirtualLocation(DisplayIndependentPoint point) {

        DisplayIndependentRectangle layoutBounds = calculateBounds();

        double x = (point.x / screenBounds.width) * layoutBounds.width + layoutBounds.x;
        double y = (point.y / screenBounds.height) * layoutBounds.height + layoutBounds.y;

        return new DisplayIndependentPoint(x, y);
    }

    private DisplayIndependentRectangle calculateBounds() {
        InternalNode n1 = (InternalNode) XCoords.first();
        InternalNode n2 = (InternalNode) XCoords.last();
        InternalNode n3 = (InternalNode) YCoords.first();
        InternalNode n4 = (InternalNode) YCoords.last();
        double x = n1.getInternalX();
        double width = n2.getInternalX();
        double y = n3.getInternalY();
        double height = n4.getInternalY();
        return new DisplayIndependentRectangle(x, y, width - x, height - y);
    }

}
