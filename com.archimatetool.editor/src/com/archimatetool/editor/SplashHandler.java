/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 * Splash Handler
 */
public class SplashHandler extends AbstractSplashHandler {
	
	public SplashHandler() {
	}
	
	@Override
    public void init(Shell shell) {
        super.init(shell);
        shell.setBackgroundMode(SWT.INHERIT_FORCE);
        
        String version = Messages.SplashHandler_0 + " " + ArchiPlugin.INSTANCE.getVersion(); //$NON-NLS-1$

        // Have to use a child composite to draw on because...yes, you guessed it, otherwise it doesn't work on Mac
        Composite parent = new Composite(shell, SWT.NONE);
        Point size = shell.getSize();
        parent.setBounds(new Rectangle(0, 0, size.x, size.y));
        
        parent.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                e.gc.setForeground(new Color(35, 35, 140));
                e.gc.drawString(version, 19, 166, true);
            }
        });
	}
}
