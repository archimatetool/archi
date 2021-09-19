/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.util.stream.Stream;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.ArchiLabelProvider;

/**
 * Status Message
 * 
 * @author Phillip Beauvoir
 */
class StatusMessage {
    
    enum StatusMessageLevel {
        
        INFO(Messages.StatusMessage_0, new Color(0, 0, 255)),
        WARNING(Messages.StatusMessage_1, new Color(255, 0, 0));
        
        private String text;
        private Color color;
        
        StatusMessageLevel(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        Color getColor() {
            return color;
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
