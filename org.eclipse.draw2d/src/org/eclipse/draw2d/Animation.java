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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Display;

/**
 * A utility for coordinating figure animations. During animation, multiple
 * <i>animators</i> are employed to capture the <em>initial</em> and
 * <em>final</em> states for one or more figures. The animators then playback
 * the animation by interpolating the intermediate states for each figure using
 * the initial and final "keyframes".
 * <P>
 * An animator is usually stateless and represents an specific technique. Any
 * state information is stored by the Animation utility. Therefore, one instance
 * can be used with multiple figures. Animators hook into the validation
 * mechanism for figures and connections. These hooks are used to capture the
 * states, and to intercept the typical layout process to insert the
 * interpolated state.
 * <P>
 * To indicate that animation is desired, clients must call {@link #markBegin()}
 * prior to invalidating any figures that are to be included in the animation.
 * After this method is called, changes are made, and {@link #run()} is invoked.
 * The run method will force a validation pass to capture the final states, and
 * then commence the animation. The animation is synchronous and the method does
 * not return until the animation has completed.
 * 
 * @see LayoutAnimator
 * @since 3.2
 */
public class Animation {

    static class AnimPair {

        final Animator animator;
        final IFigure figure;

        AnimPair(Animator animator, IFigure figure) {
            this.animator = animator;
            this.figure = figure;
        }

        @Override
        public boolean equals(Object obj) {
            AnimPair pair = (AnimPair) obj;
            return pair.animator == animator && pair.figure == figure;
        }

        @Override
        public int hashCode() {
            return animator.hashCode() ^ figure.hashCode();
        }
    }

    private static final int DEFAULT_DELAY = 250;
    private static Set<AnimPair> figureAnimators;
    private static Map<AnimPair, Object> finalStates;

    private static Map<AnimPair, Object> initialStates;
    private static final int PLAYBACK = 3;
    private static float progress;
    private static final int RECORD_FINAL = 2;

    private static final int RECORD_INITIAL = 1;
    private static long startTime;
    private static int state;
    private static Set<AnimPair> toCapture;

    private static UpdateManager updateManager;

    private static void capture() {
        Iterator<AnimPair> keys = figureAnimators.iterator();
        while (keys.hasNext()) {
            AnimPair pair = keys.next();
            if (toCapture.contains(pair))
                pair.animator.capture(pair.figure);
            else
                keys.remove();
        }
    }

    static void cleanup() {
        if (figureAnimators != null) {
            Iterator<AnimPair> keys = figureAnimators.iterator();
            while (keys.hasNext()) {
                AnimPair pair = keys.next();
                pair.animator.tearDown(pair.figure);
            }
        }

        state = 0;
        step();
        // Allow layout to occur normally
        // updateManager.performUpdate();

        initialStates = null;
        finalStates = null;
        figureAnimators = null;
        updateManager = null;
        toCapture = null;
        state = 0;
    }

    private static void doRun(int duration) {
        state = RECORD_FINAL;
        findUpdateManager();
        updateManager.performValidation();
        capture();
        state = PLAYBACK;
        progress = 0.1f;
        startTime = System.currentTimeMillis();

        notifyPlaybackStarting();

        while (progress != 0) {
            step();
            
            // Fix animation not working
            // There might be a better fix, but this will have to do until one is found
            // updateManager.performUpdate();
            Display.getDefault().readAndDispatch();
            
            if (progress == 1.0)
                progress = 0;
            else {
                int delta = (int) (System.currentTimeMillis() - startTime);
                if (delta >= duration)
                    progress = 1f;
                else
                    progress = 0.1f + 0.9f * delta / duration;
            }
        }
    }

    private static void findUpdateManager() {
        AnimPair pair = figureAnimators.iterator().next();
        updateManager = pair.figure.getUpdateManager();
    }

    /**
     * Returns the final animation state for the given figure.
     * 
     * @param animator
     *            the animator for the figure
     * @param figure
     *            the figure being animated
     * @return the final state
     * @since 3.2
     */
    public static Object getFinalState(Animator animator, IFigure figure) {
        return finalStates.get(new AnimPair(animator, figure));
    }

    /**
     * Returns the initial animation state for the given animator and figure. If
     * no state was recorded, <code>null</code> is returned.
     * 
     * @param animator
     *            the animator for the figure
     * @param figure
     *            the figure being animated
     * @return the initial state
     * @since 3.2
     */
    public static Object getInitialState(Animator animator, IFigure figure) {
        return initialStates.get(new AnimPair(animator, figure));
    }

    /**
     * Returns the animation progress, where 0.0 < progress &#8804; 1.0.
     * 
     * @return the progress of the animation
     * @since 3.2
     */
    public static float getProgress() {
        return progress;
    }

    static void hookAnimator(IFigure figure, Animator animator) {
        AnimPair pair = new AnimPair(animator, figure);
        if (figureAnimators.add(pair))
            animator.init(figure);
    }

    static void hookNeedsCapture(IFigure figure, Animator animator) {
        AnimPair pair = new AnimPair(animator, figure);
        if (figureAnimators.contains(pair))
            toCapture.add(pair);
    }

    static boolean hookPlayback(IFigure figure, Animator animator) {
        if (toCapture.contains(new AnimPair(animator, figure)))
            return animator.playback(figure);
        return false;
    }

    /**
     * Returns <code>true</code> if animation is in progress.
     * 
     * @return <code>true</code> when animating
     * @since 3.2
     */
    public static boolean isAnimating() {
        return state == PLAYBACK;
    }

    static boolean isFinalRecording() {
        return state == RECORD_FINAL;
    }

    static boolean isInitialRecording() {
        return state == RECORD_INITIAL;
    }

    /**
     * Marks the beginning of the animation process. If the beginning has
     * already been marked, this has no effect.
     * 
     * @return returns <code>true</code> if beginning was not previously marked
     * @since 3.2
     */
    public static boolean markBegin() {
        if (state == 0) {
            state = RECORD_INITIAL;
            initialStates = new HashMap<>();
            finalStates = new HashMap<>();
            figureAnimators = new HashSet<>();
            toCapture = new HashSet<>();
            return true;
        }
        return false;
    }

    private static void notifyPlaybackStarting() {
        Iterator<AnimPair> keys = figureAnimators.iterator();
        while (keys.hasNext()) {
            AnimPair pair = keys.next();
            pair.animator.playbackStarting(pair.figure);
        }
    }

    static void putFinalState(Animator animator, IFigure key, Object state) {
        finalStates.put(new AnimPair(animator, key), state);
    }

    static void putInitialState(Animator animator, IFigure key, Object state) {
        initialStates.put(new AnimPair(animator, key), state);
    }

    /**
     * Runs animation using the recommended duration: 250 milliseconds.
     * 
     * @see #run(int)
     * @since 3.2
     */
    public static void run() {
        run(DEFAULT_DELAY);
    }

    /**
     * Captures the final states for the animation and then plays the animation.
     * 
     * @param duration
     *            the length of animation in milliseconds
     * @since 3.2
     */
    public static void run(int duration) {
        // Because our fix calls Display.getDefault().readAndDispatch();
        // run() can be called again whilst playing back
        // This guards against that
        if(state == PLAYBACK) {
            return;
        }

        if (state == 0)
            return;
        try {
            if (!figureAnimators.isEmpty())
                doRun(duration);
        } finally {
            cleanup();
        }
    }

    private static void step() {
        Iterator<AnimPair> iter = initialStates.keySet().iterator();
        while (iter.hasNext())
            iter.next().figure.revalidate();
    }

}
