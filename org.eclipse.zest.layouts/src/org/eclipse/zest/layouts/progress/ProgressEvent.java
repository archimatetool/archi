/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.progress;

/**
 * When an algorithm wants to notify everyone it has completely part of its task, it
 * throws a ProgressEvent.  The progress is a number (currentProgress) representing the
 * current steps completed out of the total number of steps (totalProgress)
 * 
 * @author Casey Best
 */
public class ProgressEvent {
    int stepsCompleted;
    int totalSteps;

    /**
     * Creates a progress event.
     * @param stepsCompleted The current progress out of the total
     * @param totalNumberOfSteps The number used to indicate when the algorithm will finish
     */
    public ProgressEvent(int stepsCompleted, int totalNumberOfSteps) {
        this.stepsCompleted = stepsCompleted;
        this.totalSteps = totalNumberOfSteps;
    }

    /**
     * Returns the number of steps already completed.
     */
    public int getStepsCompleted() {
        return stepsCompleted;
    }

    /**
     * Returns the total number of steps to complete.
     */
    public int getTotalNumberOfSteps() {
        return totalSteps;
    }
}
