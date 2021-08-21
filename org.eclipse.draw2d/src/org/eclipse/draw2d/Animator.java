/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

/**
 * Animates some aspect of a figure. Each animator will capture some of the
 * effects of validation of the figures.
 * <P>
 * Animators must be hooked to figure in special ways. Refer to each
 * implementation for the specific requirements. Animators are generally
 * stateless, which allows them to be shared and prevents them from leaking
 * memory.
 * 
 * @since 3.2
 */
public abstract class Animator {

    Animator() {
    }

    /**
     * Captures the final state of the given figure. This method is called once
     * after the update manager has completed validation of all invalid figures.
     * 
     * @param figure
     *            the container
     * @since 3.2
     */
    public void capture(IFigure figure) {
        recordFinalState(figure);
    }

    /**
     * Returns an object encapsulating the current state of the figure. This
     * method is called to capture both the initial and final states.
     * 
     * @param figure
     *            the figure
     * @return the current state
     * @since 3.2
     */
    protected abstract Object getCurrentState(IFigure figure);

    /**
     * Plays back the animation for the given figure and returns
     * <code>true</code> if successful. This method does nothing by default and
     * return <code>false</code>.
     * 
     * @param figure
     *            the figure being animated
     * @return <code>true</code> if playback was successful
     * @since 3.2
     */
    protected boolean playback(IFigure figure) {
        return false;
    }

    /**
     * Sent as playback is starting for a given figure.
     * 
     * @param figure
     *            the figure
     * @since 3.2
     */
    public void playbackStarting(IFigure figure) {
    }

    /**
     * Records the final state information for a figure.
     * 
     * @param figure
     *            the figure
     * @since 3.2
     */
    protected void recordFinalState(IFigure figure) {
        Animation.putFinalState(this, figure, getCurrentState(figure));
    }

    /**
     * Records initial state information for the given figure.
     * 
     * @param figure
     *            the container.
     * @since 3.2
     */
    protected void recordInitialState(IFigure figure) {
        Animation.putInitialState(this, figure, getCurrentState(figure));
    }

    /**
     * Sets up the animator for the given figure to be animated. This method is
     * called exactly once time prior to any layouts happening. The animator can
     * capture the figure's current state, and set any animation-time settings
     * for the figure. Changes made to the figure should be reverted in
     * {@link #tearDown(IFigure)}.
     * 
     * @param figure
     *            the animated figure
     * @since 3.2
     */
    public void init(IFigure figure) {
        recordInitialState(figure);
    }

    /**
     * Reverts any temporary changes made to the figure during animation. This
     * method is called exactly once after all animation has been completed.
     * Subclasses should extend this method to revert any changes.
     * 
     * @param figure
     *            the animated figure
     * @since 3.2
     * @see #init(IFigure)
     */
    public void tearDown(IFigure figure) {
    }

}
