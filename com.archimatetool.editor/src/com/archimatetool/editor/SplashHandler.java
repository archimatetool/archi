/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Splash Handler
 */
public class SplashHandler extends AbstractSplashHandler {
	
	public SplashHandler() {
	}
	
	@Override
    public void init(Shell shell) {
        super.init(shell);

        // Check for Mac bug
        checkMacBackgroundImageInverted(shell);
        
        shell.setBackgroundMode(SWT.INHERIT_FORCE);
        
        String version = Messages.SplashHandler_0 + " " + ArchiPlugin.INSTANCE.getVersion(); //$NON-NLS-1$

        // Have to use a child composite to draw on because...yes, you guessed it, otherwise it doesn't work on Mac
        Composite parent = new Composite(shell, SWT.NONE);
        Point size = shell.getSize();
        parent.setBounds(new Rectangle(0, 0, size.x, size.y));
        
        parent.addPaintListener(e -> {
            e.gc.setForeground(new Color(35, 35, 140));
            e.gc.drawString(version, 19, 166, true);
        });
	}
	
	/**
	 * macOS Sonoma 14 flips background images on Control#setBackgroundImage
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/772
	 */
	private void checkMacBackgroundImageInverted(Shell shell) {
        if(PlatformUtils.isMac() && PlatformUtils.compareOSVersion("14.0") >= 0) { //$NON-NLS-1$
	        // Flip the image
            shell.setBackgroundImage(flipImage(shell.getDisplay(), shell.getBackgroundImage()));
            
            // We are now responsible for disposing the new image
            shell.addDisposeListener(e -> {
                shell.getBackgroundImage().dispose();
            });
        }
	}
	
    /**
     * From https://github.com/knime/knime-product/commit/095c88e22ba6d84e22a480f50734141836098169
     */
    private Image flipImage(Display display, Image srcImage) {
        Rectangle bounds = srcImage.getBounds();
        Image newImage = new Image(display, bounds.width, bounds.height);
        
        GC gc = new GC(newImage);
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        
        Transform transform = new Transform(display);
        // flip down
        transform.setElements(1, 0, 0, -1, 0, 0);
        // move up
        transform.translate(0, -bounds.height);
        gc.setTransform(transform);
        
        gc.drawImage(srcImage, 0, 0, bounds.width, bounds.height,
                               0, 0, bounds.width, bounds.height);
        
        gc.dispose();
        transform.dispose();
        
        return newImage;
    }
}
