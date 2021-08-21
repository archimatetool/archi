/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.text;

/**
 * @since 3.1
 */
class BidiChars {

    /**
     * paragraph separator character
     */
    static final char P_SEP = '\u2029';
    /**
     * zero-width joiner character
     */
    static final char ZWJ = '\u200d';
    /**
     * left-to-right overwrite character
     */
    static final char LRO = '\u202d';
    /**
     * right-to-left overwrite character
     */
    static final char RLO = '\u202e';
    /**
     * object replacement character
     */
    static final char OBJ = '\ufffc';
    /**
     * left-to-right embedding character
     */
    static final char LRE = '\u202a';
    /**
     * right-to-left embedding character
     */
    static final char RLE = '\u202b';

}