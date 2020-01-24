/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.util.stream.Stream;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;

/**
 * Status Message
 * 
 * @author Phillip Beauvoir
 */
class StatusMessage {
    
    enum StatusMessageLevel {
        
        INFO(Messages.StatusMessage_0, new RGB(0, 0, 255)),
        WARNING(Messages.StatusMessage_1, new RGB(255, 0, 0));
        
        private String text;
        private RGB rgb;
        
        StatusMessageLevel(String text, RGB rgb) {
            this.text = text;
            this.rgb = rgb;
        }

        Color getColor() {
            return ColorFactory.get(rgb);
        }

        String getText() {
            return text;
        }
    }
    
    private StatusMessageLevel level;
    private String message;
    private Object[] objs;
    
    StatusMessage(StatusMessageLevel level, String message, Object...objs) {
        this.level = level;
        this.message = message;
        this.objs = objs;
    }
    
    StatusMessageLevel getLevel() {
        return level;
    }
    
    String getMessage() {
        Object[] objsList = Stream.of(objs)
                                      .map(obj -> ArchiLabelProvider.INSTANCE.getLabel(obj))
                                      .toArray();
        
        return getLevel().getText() + " " + NLS.bind(message, objsList); //$NON-NLS-1$
    }
    
    @Override
    public String toString() {
        return getMessage();
    }
}
