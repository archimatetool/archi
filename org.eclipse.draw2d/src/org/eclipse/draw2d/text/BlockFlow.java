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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A <code>FlowFigure</code> represented by a single {@link BlockBox} containing
 * one or more lines. A BlockFlow is a creator of LineBoxes, which its children
 * require during layout. A BlockFlow can be thought of as a foundation for a
 * paragraph.
 * <P>
 * BlockFlows must be parented by a <code>FlowFigure</code>. {@link FlowPage}
 * can be used as a "root" block and can be parented by normal Figures.
 * <P>
 * Only {@link FlowFigure}s can be added to a BlockFlow.
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings("rawtypes")
public class BlockFlow extends FlowFigure {

    private final BlockBox blockBox;
    private int alignment = PositionConstants.NONE;
    private int orientation = SWT.NONE;
    private boolean bidiValid;

    /**
     * Constructs a new BlockFlow.
     */
    public BlockFlow() {
        blockBox = createBlockBox();
    }

    /**
     * BlockFlows contribute a paragraph separator so as to keep the Bidi state
     * of the text on either side of this block from affecting each other. Since
     * each block is like a different paragraph, it does not contribute any
     * actual text to its containing block.
     * 
     * @see org.eclipse.draw2d.text.FlowFigure#contributeBidi(org.eclipse.draw2d.text.BidiProcessor)
     */
    @Override
    protected void contributeBidi(BidiProcessor proc) {
        proc.addControlChar(BidiChars.P_SEP);
    }

    BlockBox createBlockBox() {
        return new BlockBox(this);
    }

    /**
     * @see org.eclipse.draw2d.text.FlowFigure#createDefaultFlowLayout()
     */
    @Override
    protected FlowFigureLayout createDefaultFlowLayout() {
        return new BlockFlowLayout(this);
    }

    /**
     * Returns the BlockBox associated with this.
     * 
     * @return This BlockFlow's BlockBox
     */
    protected BlockBox getBlockBox() {
        return blockBox;
    }

    int getBottomMargin() {
        int margin = 0;
        if (getBorder() instanceof FlowBorder) {
            FlowBorder border = (FlowBorder) getBorder();
            return border.getBottomMargin();
        }
        List children = getChildren();
        int childIndex = children.size() - 1;
        if (childIndex >= 0 && children.get(childIndex) instanceof BlockFlow) {
            margin = Math.max(margin,
                    ((BlockFlow) children.get(childIndex)).getBottomMargin());
        }
        return margin;
    }

    /**
     * Returns the effective horizontal alignment. This method will never return
     * {@link PositionConstants#NONE}. If the value is none, it will return the
     * inherited alignment. If no alignment was inherited, it will return the
     * default alignment ({@link PositionConstants#LEFT}).
     * 
     * @return the effective alignment
     */
    public int getHorizontalAligment() {
        if (alignment != PositionConstants.NONE)
            return alignment;
        IFigure parent = getParent();
        while (parent != null && !(parent instanceof BlockFlow))
            parent = parent.getParent();
        if (parent != null)
            return ((BlockFlow) parent).getHorizontalAligment();
        return PositionConstants.LEFT;
    }

    int getLeftMargin() {
        if (getBorder() instanceof FlowBorder)
            return ((FlowBorder) getBorder()).getLeftMargin();
        return 0;
    }

    /**
     * Returns the orientation set on this block.
     * 
     * @return LTR, RTL or NONE
     * @see #setOrientation(int)
     * @since 3.1
     */
    public int getLocalOrientation() {
        return orientation;
    }

    /**
     * Returns the horizontal alignment set on this block.
     * 
     * @return LEFT, RIGHT, ALWAYS_LEFT, ALWAYS_RIGHT, NONE
     * @see #setHorizontalAligment(int)
     * @since 3.1
     */
    public int getLocalHorizontalAlignment() {
        return alignment;
    }

    /**
     * Returns this block's Bidi orientation. If none was set on this block, it
     * will inherit the one from its containing block. If there is no containing
     * block, it will return the default orientation (SWT.RIGHT_TO_LEFT if
     * mirrored; SWT.LEFT_TO_RIGHT otherwise).
     * 
     * @return SWT.RIGHT_TO_LEFT or SWT.LEFT_TO_RIGHT
     * @see #setOrientation(int)
     * @since 3.1
     */
    public int getOrientation() {
        if (orientation != SWT.NONE)
            return orientation;
        IFigure parent = getParent();
        while (parent != null && !(parent instanceof BlockFlow))
            parent = parent.getParent();
        if (parent != null)
            return ((BlockFlow) parent).getOrientation();
        return isMirrored() ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
    }

    int getRightMargin() {
        if (getBorder() instanceof FlowBorder)
            return ((FlowBorder) getBorder()).getRightMargin();
        return 0;
    }

    int getTopMargin() {
        int margin = 0;
        if (getBorder() instanceof FlowBorder) {
            FlowBorder border = (FlowBorder) getBorder();
            return border.getTopMargin();
        }
        List children = getChildren();
        if (children.size() > 0 && children.get(0) instanceof BlockFlow) {
            margin = Math.max(margin,
                    ((BlockFlow) children.get(0)).getTopMargin());
        }
        return margin;
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void paintBorder(Graphics graphics) {
        if (getBorder() instanceof FlowBorder) {
            Rectangle where = getBlockBox().toRectangle();
            where.crop(new Insets(getTopMargin(), getLeftMargin(),
                    getBottomMargin(), getRightMargin()));
            ((FlowBorder) getBorder()).paint(this, graphics, where, SWT.LEAD
                    | SWT.TRAIL);
        } else
            super.paintBorder(graphics);
        if (selectionStart != -1) {
            graphics.restoreState();
            graphics.setXORMode(true);
            graphics.setBackgroundColor(ColorConstants.white);
            graphics.fillRectangle(getBounds());
        }
    }

    /**
     * @see org.eclipse.draw2d.text.FlowFigure#postValidate()
     */
    @SuppressWarnings("deprecation")
    @Override
    public void postValidate() {
        Rectangle newBounds = getBlockBox().toRectangle();
        newBounds.crop(new Insets(getTopMargin(), getLeftMargin(),
                getBottomMargin(), getRightMargin()));
        setBounds(newBounds);
    }

    /**
     * @see FlowFigure#revalidate()
     */
    @Override
    public void revalidate() {
        BlockFlowLayout layout = (BlockFlowLayout) getLayoutManager();
        layout.blockContentsChanged();
        super.revalidate();
    }

    /**
     * A Block will invalidate the Bidi state of all its children, so that it is
     * re-evaluated when this block is next validated.
     * 
     * @see org.eclipse.draw2d.text.FlowFigure#revalidateBidi(org.eclipse.draw2d.IFigure)
     */
    @Override
    protected void revalidateBidi(IFigure origin) {
        if (bidiValid) {
            bidiValid = false;
            revalidate();
        }
    }

    /**
     * Sets the horitontal aligment of the block. Valid values are:
     * <UL>
     * <LI>{@link PositionConstants#NONE NONE} - (default) Alignment is
     * inherited from parent. If a parent is not found then LEFT is used.</LI>
     * <LI>{@link PositionConstants#LEFT} - Alignment is with leading edge</LI>
     * <LI>{@link PositionConstants#RIGHT} - Alignment is with trailing edge</LI>
     * <LI>{@link PositionConstants#CENTER}</LI>
     * <LI>{@link PositionConstants#ALWAYS_LEFT} - Left, irrespective of
     * orientation</LI>
     * <LI>{@link PositionConstants#ALWAYS_RIGHT} - Right, irrespective of
     * orientation</LI>
     * </UL>
     * 
     * @param value
     *            the aligment
     * @see #getHorizontalAligment()
     */
    public void setHorizontalAligment(int value) {
        value &= PositionConstants.LEFT | PositionConstants.CENTER
                | PositionConstants.RIGHT | PositionConstants.ALWAYS_LEFT
                | PositionConstants.ALWAYS_RIGHT;
        if (value == alignment)
            return;
        alignment = value;
        revalidate();
    }

    /**
     * Sets the orientation for this block. Orientation can be one of:
     * <UL>
     * <LI>{@link SWT#LEFT_TO_RIGHT}
     * <LI>{@link SWT#RIGHT_TO_LEFT}
     * <LI>{@link SWT#NONE} (default)
     * </UL>
     * <code>NONE</code> is used to indicate that orientation should be
     * inherited from the encompassing block.
     * 
     * @param orientation
     *            LTR, RTL or NONE
     * @see #getOrientation()
     * @since 3.1
     */
    public void setOrientation(int orientation) {
        orientation &= SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
        if (this.orientation == orientation)
            return;
        this.orientation = orientation;
        revalidateBidi(this);
    }

    /**
     * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
     */
    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }

    /**
     * Re-evaluate the Bidi state of all the fragments if it has been
     * invalidated.
     * 
     * @see org.eclipse.draw2d.IFigure#validate()
     */
    @Override
    public void validate() {
        if (!bidiValid) {
            BidiProcessor.INSTANCE.setOrientation(getOrientation());
            if (getOrientation() == SWT.LEFT_TO_RIGHT && isMirrored())
                BidiProcessor.INSTANCE.addControlChar(BidiChars.LRE);
            super.contributeBidi(BidiProcessor.INSTANCE);
            BidiProcessor.INSTANCE.process();
            bidiValid = true;
        }
        super.validate();
    }

}