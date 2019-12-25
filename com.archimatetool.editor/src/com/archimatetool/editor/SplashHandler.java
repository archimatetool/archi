/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

import com.archimatetool.editor.ui.ColorFactory;

/**
 * Splash Handler
 */
public class SplashHandler extends AbstractSplashHandler {
	
	public SplashHandler() {
	}
	
	@Override
    public void init(Shell shell) {
        super.init(shell);

        final String version = Messages.SplashHandler_0 + System.getProperty(Application.APPLICATION_VERSIONID);

        shell.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                e.gc.setForeground(ColorFactory.get(35, 35, 140));
                e.gc.drawString(version, 19, 166, true);
            }
        });
	}
}
