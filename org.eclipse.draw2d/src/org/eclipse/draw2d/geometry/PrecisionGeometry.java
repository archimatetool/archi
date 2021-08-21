/*******************************************************************************
 * Copyright (c) 2010 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.geometry;

import java.math.BigDecimal;

/**
 * A Utilities class for precise geometry calculations.
 * 
 * @author Alexander Nyssen
 * @since 3.7
 */
@SuppressWarnings("deprecation")
public class PrecisionGeometry {

    /*
     * Precise calculations on doubles are performed based on BigDecimals,
     * converting to 16 digits scale, so there are no undesired rounding
     * effects.
     */
    private static final int ROUNDING = BigDecimal.ROUND_HALF_EVEN;
    private static final int SCALE = 16;

    protected static final double preciseAdd(double d1, double d2) {
        return doubleToBigDecimal(d1).add(doubleToBigDecimal(d2))
                .setScale(SCALE, ROUNDING).doubleValue();
    }

    protected static final double preciseSubtract(double d1, double d2) {
        return doubleToBigDecimal(d1).subtract(doubleToBigDecimal(d2))
                .setScale(SCALE, ROUNDING).doubleValue();
    }

    protected static final double preciseMultiply(double d1, double d2) {
        return doubleToBigDecimal(d1).multiply(doubleToBigDecimal(d2))
                .setScale(SCALE, ROUNDING).doubleValue();
    }

    protected static final double preciseDivide(double d1, double d2) {
        return doubleToBigDecimal(d1).divide(doubleToBigDecimal(d2), SCALE,
                ROUNDING).doubleValue();
    }

    protected static final double preciseNegate(double d) {
        return doubleToBigDecimal(d).negate().setScale(SCALE, ROUNDING)
                .doubleValue();
    }

    protected static final double preciseAbs(double d) {
        return doubleToBigDecimal(d).abs().setScale(SCALE, ROUNDING)
                .doubleValue();
    }

    protected static final BigDecimal doubleToBigDecimal(double d) {
        // may not use BigDecimal.valueOf due to J2SE-1.4 backwards
        // compatibility
        return new BigDecimal(Double.toString(d));
    }

    /**
     * Converts a double value into an integer value, avoiding rounding effects.
     * 
     * @param doubleValue
     *            the double value to convert
     * @return the integer value for the double.
     */
    protected static final int doubleToInteger(double doubleValue) {
        return (int) Math.floor(doubleValue + 0.000000001);
    }
}
