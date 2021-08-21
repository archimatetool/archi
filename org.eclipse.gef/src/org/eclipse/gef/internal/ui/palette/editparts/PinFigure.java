/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Toggle;

import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * This is the figure for the pinned and unpinned toggle.
 * 
 * @author crevells
 * @since 3.4
 */
public class PinFigure extends Toggle {

    private static final Color PIN_HOTSPOT_COLOR = FigureUtilities.mixColors(
            PaletteColorUtil.WIDGET_LIST_BACKGROUND,
            PaletteColorUtil.WIDGET_NORMAL_SHADOW, 0.60);

    private static final Border TOOLTIP_BORDER = new MarginBorder(0, 2, 1, 0);

    public PinFigure() {
        super(new ImageFigure(InternalImages.get(InternalImages.IMG_UNPINNED)));
        setRolloverEnabled(true);
        setRequestFocusEnabled(false);
        Label tooltip = new Label(PaletteMessages.TOOLTIP_PIN_FIGURE);
        tooltip.setBorder(TOOLTIP_BORDER);
        setToolTip(tooltip);
        setOpaque(false);

        addChangeListener(new ChangeListener() {

            @Override
            public void handleStateChanged(ChangeEvent e) {
                if (e.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY)) {
                    if (isSelected()) {
                        ((ImageFigure) (getChildren().get(0)))
                                .setImage(InternalImages
                                        .get(InternalImages.IMG_PINNED));
                        ((Label) getToolTip())
                                .setText(PaletteMessages.TOOLTIP_UNPIN_FIGURE);
                    } else {
                        ((ImageFigure) (getChildren().get(0)))
                                .setImage(InternalImages
                                        .get(InternalImages.IMG_UNPINNED));
                        ((Label) getToolTip())
                                .setText(PaletteMessages.TOOLTIP_PIN_FIGURE);
                    }
                }
            }
        });
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);

        ButtonModel model = getModel();
        if (isRolloverEnabled() && model.isMouseOver()) {
            graphics.setBackgroundColor(PIN_HOTSPOT_COLOR);
            graphics.fillRoundRectangle(getClientArea().getCopy().shrink(1, 1),
                    3, 3);
        }
    }
}
