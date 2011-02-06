/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * MultiLineCellEditor
 * 
 * @author Phillip Beauvoir
 */
public class MultiLineCellEditor extends TextCellEditor {

	public MultiLineCellEditor(Composite composite, int style) {
        super(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | style);
    }
}
