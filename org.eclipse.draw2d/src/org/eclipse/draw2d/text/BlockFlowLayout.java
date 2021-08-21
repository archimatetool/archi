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
package org.eclipse.draw2d.text;

import java.util.List;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;

/**
 * The layout for {@link BlockFlow} figures.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings("rawtypes")
public class BlockFlowLayout extends FlowContainerLayout {

    BlockBox blockBox;
    boolean blockInvalid = false;
    private boolean continueOnSameLine = false;
    private CompositeBox previousLine = null;

    /**
     * Creates a new BlockFlowLayout with the given BlockFlow.
     * 
     * @param blockFlow
     *            the BlockFlow
     */
    public BlockFlowLayout(BlockFlow blockFlow) {
        super(blockFlow);
    }

    private void addBelowPreviousLine(CompositeBox line) {
        if (previousLine == null)
            line.setLineTop(line.getTopMargin());
        else
            line.setLineTop(previousLine.getBaseline()
                    + previousLine.getDescent()
                    + Math.max(previousLine.getBottomMargin(),
                            line.getTopMargin()));

        int alignment = getBlockFlow().getHorizontalAligment();
        if (alignment == PositionConstants.LEFT
                || alignment == PositionConstants.RIGHT) {
            int orientation = getBlockFlow().getOrientation();
            if (alignment == PositionConstants.LEFT)
                alignment = orientation == SWT.LEFT_TO_RIGHT ? PositionConstants.ALWAYS_LEFT
                        : PositionConstants.ALWAYS_RIGHT;
            else
                alignment = orientation == SWT.LEFT_TO_RIGHT ? PositionConstants.ALWAYS_RIGHT
                        : PositionConstants.ALWAYS_LEFT;
        }
        if (alignment != PositionConstants.CENTER
                && getBlockFlow().isMirrored())
            alignment = (PositionConstants.ALWAYS_LEFT | PositionConstants.ALWAYS_RIGHT)
                    & ~alignment;

        switch (alignment) {
        case PositionConstants.ALWAYS_RIGHT:
            line.setX(blockBox.getRecommendedWidth() - line.getWidth());
            break;
        case PositionConstants.CENTER:
            line.setX((blockBox.getRecommendedWidth() - line.getWidth()) / 2);
            break;
        case PositionConstants.ALWAYS_LEFT:
            line.setX(0);
            break;
        default:
            throw new RuntimeException("Unexpected state"); //$NON-NLS-1$
        }
        blockBox.add(line);
        previousLine = line;
    }

    /**
     * Align the line horizontally and then commit it.
     */
    protected void addCurrentLine() {
        addBelowPreviousLine(currentLine);
        ((LineRoot) currentLine).commit();
    }

    /**
     * @see FlowContext#addLine(CompositeBox)
     */
    @Override
    public void addLine(CompositeBox box) {
        endLine();
        addBelowPreviousLine(box);
    }

    /**
     * Marks the blocks contents as changed. This means that children will be
     * invalidated during validation.
     * 
     * @since 3.1
     */
    public void blockContentsChanged() {
        blockInvalid = true;
    }

    /**
     * @see FlowContainerLayout#cleanup()
     */
    @Override
    protected void cleanup() {
        super.cleanup();
        previousLine = null;
    }

    /**
     * @see FlowContainerLayout#createNewLine()
     */
    @Override
    protected void createNewLine() {
        currentLine = new LineRoot(getBlockFlow().isMirrored());
        currentLine.setRecommendedWidth(blockBox.getRecommendedWidth());
    }

    /**
     * Called by flush(), adds the BlockBox associated with this BlockFlowLayout
     * to the current line and then ends the line.
     */
    protected void endBlock() {
        if (blockInvalid) {
            Insets insets = getBlockFlow().getInsets();
            blockBox.height += insets.getHeight();
            blockBox.width += insets.getWidth();
        }

        if (getContext() != null)
            getContext().addLine(blockBox);

        if (blockInvalid) {
            blockInvalid = false;
            List v = getFlowFigure().getChildren();
            for (int i = 0; i < v.size(); i++)
                ((FlowFigure) v.get(i)).postValidate();
        }
    }

    /**
     * @see FlowContext#endLine()
     */
    @Override
    public void endLine() {
        if (currentLine == null || !currentLine.isOccupied())
            return;
        addCurrentLine();
        currentLine = null;
    }

    /**
     * @see FlowContainerLayout#flush()
     */
    @Override
    protected void flush() {
        endLine();
        endBlock();
    }

    @Override
    boolean forceChildInvalidation(Figure f) {
        return blockInvalid;
    }

    /**
     * Returns the BlockFlow associated with this BlockFlowLayout
     * 
     * @return the BlockFlow
     */
    protected final BlockFlow getBlockFlow() {
        return (BlockFlow) getFlowFigure();
    }

    int getContextWidth() {
        return getContext().getRemainingLineWidth();
    }

    /**
     * @see FlowContext#getContinueOnSameLine()
     */
    @Override
    public boolean getContinueOnSameLine() {
        return continueOnSameLine;
    }

    /**
     * @see FlowContext#getWidthLookahead(FlowFigure, int[])
     */
    @Override
    public void getWidthLookahead(FlowFigure child, int result[]) {
        List children = getFlowFigure().getChildren();
        int index = -1;
        if (child != null)
            index = children.indexOf(child);

        for (int i = index + 1; i < children.size(); i++)
            if (((FlowFigure) children.get(i))
                    .addLeadingWordRequirements(result))
                return;
    }

    /**
     * @see FlowContainerLayout#preLayout()
     */
    @Override
    protected void preLayout() {
        setContinueOnSameLine(false);
        blockBox = getBlockFlow().getBlockBox();
        setupBlock();
        // Probably could setup current and previous line here, or just previous
    }

    /**
     * @see org.eclipse.draw2d.text.FlowContext#setContinueOnSameLine(boolean)
     */
    @Override
    public void setContinueOnSameLine(boolean value) {
        continueOnSameLine = value;
    }

    /**
     * sets up the single block that contains all of the lines.
     */
    protected void setupBlock() {
        int recommended = getContextWidth();
        if (recommended == Integer.MAX_VALUE)
            recommended = -1;
        BlockFlow bf = getBlockFlow();
        if (recommended > 0) {
            int borderCorrection = bf.getInsets().getWidth()
                    + bf.getLeftMargin() + bf.getRightMargin();
            recommended = Math.max(0, recommended - borderCorrection);
        }

        if (recommended != blockBox.recommendedWidth) {
            blockInvalid = true;
            blockBox.setRecommendedWidth(recommended);
        }

        if (blockInvalid) {
            blockBox.height = 0;
            blockBox.setWidth(Math.max(0, recommended));
        }
    }

}