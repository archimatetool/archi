/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * This layout shifts overlapping nodes to the right.
 * @author Ian Bull
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class HorizontalShift extends AbstractLayoutAlgorithm {

    private static final double DELTA = 10;
    private static final double VSPACING = 2;

    public HorizontalShift(int styles) {
        super(styles);
    }

    @Override
    protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {

        ArrayList row = new ArrayList();
        for (int i = 0; i < entitiesToLayout.length; i++) {
            addToRowList(entitiesToLayout[i], row);
        }

        int heightSoFar = 0;

        Collections.sort(row, new Comparator() {

            @Override
            public int compare(Object arg0, Object arg1) {
                // TODO Auto-generated method stub
                List a0 = (List) arg0;
                List a1 = (List) arg1;
                LayoutEntity node0 = ((InternalNode) a0.get(0)).getLayoutEntity();
                LayoutEntity node1 = ((InternalNode) a1.get(0)).getLayoutEntity();
                return (int) (node0.getYInLayout() - (node1.getYInLayout()));
            }

        });

        Iterator iterator = row.iterator();
        while (iterator.hasNext()) {
            List currentRow = (List) iterator.next();
            Collections.sort(currentRow, new Comparator() {
                @Override
                public int compare(Object arg0, Object arg1) {
                    return (int) (((InternalNode) arg1).getLayoutEntity().getYInLayout() - ((InternalNode) arg0).getLayoutEntity().getYInLayout());
                }
            });
            Iterator iterator2 = currentRow.iterator();
            int i = 0;
            int width = (int) ((boundsWidth / 2) - currentRow.size() * 75);

            heightSoFar += ((InternalNode) currentRow.get(0)).getLayoutEntity().getHeightInLayout() + VSPACING * 8;
            while (iterator2.hasNext()) {
                InternalNode currentNode = (InternalNode) iterator2.next();

                double location = width + 10 * ++i;
                currentNode.setLocation(location, heightSoFar);
                width += currentNode.getLayoutEntity().getWidthInLayout();
            }
        }
    }

    private void addToRowList(InternalNode node, ArrayList list) {
        double layoutY = node.getLayoutEntity().getYInLayout();

        for (int i = 0; i < list.size(); i++) {
            List currentRow = (List) list.get(i);
            InternalNode currentRowNode = (InternalNode) currentRow.get(0);
            double currentRowY = currentRowNode.getLayoutEntity().getYInLayout();
            //double currentRowHeight = currentRowNode.getLayoutEntity().getHeightInLayout();
            if (layoutY >= (currentRowY - DELTA) && layoutY <= currentRowY + DELTA) {
                currentRow.add(node);
                //list.add(i, currentRow);
                return;
            }
        }
        List newRow = new ArrayList();
        newRow.add(node);
        list.add(newRow);
    }

    @Override
    protected int getCurrentLayoutStep() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected int getTotalNumberOfLayoutSteps() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLayoutArea(double x, double y, double width, double height) {
        // TODO Auto-generated method stub
    }
}
