/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.application;

import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.AbstractElementUIProvider;



/**
 * Abstract Application UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractApplicationUIProvider extends AbstractElementUIProvider {
    
    @Override
    public Color getDefaultColor() {
        return ColorFactory.COLOR_APPLICATION;
    }
}
