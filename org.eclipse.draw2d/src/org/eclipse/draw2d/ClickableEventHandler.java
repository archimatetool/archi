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
package org.eclipse.draw2d;

class ClickableEventHandler extends MouseMotionListener.Stub implements
        MouseListener, FigureListener, ChangeListener, KeyListener,
        FocusListener {

    private MouseEvent lastEvent;

    @Override
    public void focusLost(FocusEvent fe) {
        Clickable loser = (Clickable) fe.loser;
        loser.repaint();
        loser.getModel().setArmed(false);
        loser.getModel().setPressed(false);
    }

    @Override
    public void focusGained(FocusEvent fe) {
        Clickable clickable = (Clickable) fe.gainer;
        clickable.repaint();
    }

    @Override
    public void figureMoved(IFigure source) {
        if (lastEvent == null)
            return;
        mouseDragged(lastEvent);
    }

    @Override
    public void handleStateChanged(ChangeEvent change) {
        Clickable clickable = (Clickable) change.getSource();
        if (change.getPropertyName() == ButtonModel.MOUSEOVER_PROPERTY
                && !clickable.isRolloverEnabled())
            return;
        clickable.repaint();
    }

    @Override
    public void mouseDoubleClicked(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        lastEvent = me;
        Clickable click = (Clickable) me.getSource();
        ButtonModel model = click.getModel();
        if (model.isPressed()) {
            boolean over = click.containsPoint(me.getLocation());
            model.setArmed(over);
            model.setMouseOver(over);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        Clickable click = (Clickable) me.getSource();
        click.getModel().setMouseOver(true);
        click.addFigureListener(this);
    }

    @Override
    public void mouseExited(MouseEvent me) {
        Clickable click = (Clickable) me.getSource();
        click.getModel().setMouseOver(false);
        click.removeFigureListener(this);
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (me.button != 1)
            return;
        lastEvent = me;
        Clickable click = (Clickable) me.getSource();
        ButtonModel model = click.getModel();
        click.requestFocus();
        model.setArmed(true);
        model.setPressed(true);
        me.consume();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (me.button != 1)
            return;
        ButtonModel model = ((Clickable) me.getSource()).getModel();
        if (!model.isPressed())
            return;
        model.setPressed(false);
        model.setArmed(false);
        me.consume();
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        ButtonModel model = ((Clickable) ke.getSource()).getModel();
        if (ke.character == ' ' || ke.character == '\r') {
            model.setPressed(true);
            model.setArmed(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        ButtonModel model = ((Clickable) ke.getSource()).getModel();
        if (ke.character == ' ' || ke.character == '\r') {
            model.setPressed(false);
            model.setArmed(false);
        }
    }

}
