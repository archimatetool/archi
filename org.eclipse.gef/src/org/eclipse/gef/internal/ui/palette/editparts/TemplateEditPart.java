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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.ui.IMemento;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

/**
 * @author Eric Bordeau, Pratik Shah
 */
public class TemplateEditPart extends PaletteEditPart {

    private static final String SELECTION_STATE = "selection"; //$NON-NLS-1$

    /**
     * Constructor
     * 
     * @param entry
     *            The model entry
     */
    public TemplateEditPart(PaletteTemplateEntry entry) {
        super(entry);
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#createAccessible()
     */
    @Override
    protected AccessibleEditPart createAccessible() {
        return new AccessibleGraphicalEditPart() {
            @Override
            public void getDescription(AccessibleEvent e) {
                e.result = getTemplateEntry().getDescription();
            }

            @Override
            public void getName(AccessibleEvent e) {
                e.result = getTemplateEntry().getLabel();
            }

            @Override
            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_LISTITEM;
            }
        };
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    public IFigure createFigure() {
        IFigure fig = new DetailedLabelFigure() {

            @Override
            public IFigure getToolTip() {
                return createToolTip();
            }

            @Override
            protected void paintFigure(Graphics graphics) {
                super.paintFigure(graphics);

                if (isSelected()) {
                    graphics.setBackgroundColor(PaletteColorUtil
                            .getSelectedColor());
                }
                graphics.fillRoundRectangle(ToolEntryEditPart
                        .getSelectionRectangle(getLayoutSetting(), this), 3, 3);
            }

        };
        fig.setRequestFocusEnabled(true);
        return fig;
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        ((DetailedLabelFigure) getFigure()).dispose();
        super.deactivate();
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#getDragTracker(Request)
     */
    @Override
    public DragTracker getDragTracker(Request request) {
        return new SingleSelectionTracker() {
            @Override
            protected boolean handleButtonDown(int button) {
                getFigure().requestFocus();
                return super.handleButtonDown(button);
            }
        };
    }

    private PaletteTemplateEntry getTemplateEntry() {
        return (PaletteTemplateEntry) getModel();
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#getToolTipText()
     */
    @Override
    protected String getToolTipText() {
        String result = null;
        if (getLayoutSetting() != PaletteViewerPreferences.LAYOUT_DETAILS) {
            result = super.getToolTipText();
        }
        return result;
    }

    /**
     * If this edit part's name is truncated in its label, the name should be
     * prepended to the tooltip.
     * 
     * @return whether the name needs to be included in the tooltip
     */
    @Override
    protected boolean nameNeededInToolTip() {
        DetailedLabelFigure label = (DetailedLabelFigure) getFigure();
        return label.isNameTruncated() || super.nameNeededInToolTip();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @Override
    protected void refreshVisuals() {
        DetailedLabelFigure fig = (DetailedLabelFigure) getFigure();
        PaletteEntry entry = getPaletteEntry();
        fig.setName(entry.getLabel());
        fig.setDescription(entry.getDescription());
        if (getPreferenceSource().useLargeIcons())
            setImageDescriptor(entry.getLargeIcon());
        else
            setImageDescriptor(entry.getSmallIcon());
        int layoutMode = getLayoutSetting();
        fig.setLayoutMode(layoutMode);
        if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS) {
            fig.setBorder(ToolEntryEditPart.ICON_BORDER);
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
                || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
            fig.setBorder(ToolEntryEditPart.LIST_BORDER);
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS
                && !isToolbarItem()) {
            fig.setBorder(ToolEntryEditPart.ICON_BORDER);
        } else {
            fig.setBorder(null);
        }
        super.refreshVisuals();
    }

    @Override
    public void restoreState(IMemento memento) {
        setSelected(memento.getInteger(SELECTION_STATE).intValue());
        super.restoreState(memento);
    }

    @Override
    public void saveState(IMemento memento) {
        memento.putInteger(SELECTION_STATE, getSelected());
        super.saveState(memento);
    }

    /**
     * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
     */
    @Override
    protected void setImageInFigure(Image image) {
        DetailedLabelFigure fig = (DetailedLabelFigure) getFigure();
        fig.setImage(image);
    }

    /**
     * @see org.eclipse.gef.EditPart#setSelected(int)
     */
    @Override
    public void setSelected(int value) {
        super.setSelected(value);
        DetailedLabelFigure label = (DetailedLabelFigure) getFigure();
        if (value == SELECTED_PRIMARY) {
            label.requestFocus();
            label.setSelected(true);
        } else {
            label.setSelected(false);
        }
        label.repaint();

    }

}
