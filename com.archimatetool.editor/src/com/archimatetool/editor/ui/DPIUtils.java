/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.PlatformUtils;

/**
 * DPI Utils
 * 
 * @author Phillip Beauvoir
 * @since 5.11.1
 */
@SuppressWarnings({"nls", "restriction"})
public final class DPIUtils {

    /**
     * @return the device zoom for the current active Shell if possible.
     * @since 5.11.1
     */
    public static int getDeviceZoom() {
        Shell shell = getActiveShell();

        if(shell != null && !shell.isDisposed()) {
            return shell.getZoom();
        }

        // Fallback which is not reliable
        return DPIUtil.getDeviceZoom();
    }

    /**
     * @return The zoom level for creating images.
     * On Linux SCALE_IMAGE_EXPORT defaults to false and this should be 100%
     * On Mac/Windows use the workbench device zoom
     * @since 5.11.1
     */
    public static int getImageDeviceZoom() {
        boolean scaleImages = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SCALE_IMAGE_EXPORT);
        return scaleImages ? getDeviceZoom() : 100;
    }
    
    /**
     * To create Images on Windows using ImagePrintFigureOperation we need to pass a parent Shell.
     * But the Shell has to be parented by the workbench Shell in order to get the correct
     * monitor scaling for the current display.
     * 
     * @return A Shell parented by the active Shell, if any. Callers should dispose of it after use.
     * @since 5.11.1
     */
    public static Shell createShellForImage() {
        return new Shell(getActiveShell(), SWT.NO_TRIM);
    }
    
    /**
     * @return the active WorkbenchWindow Shell or else null if there is no active WorkbenchWindow or actve shell
     * @since 5.11.1
     */
    public static Shell getActiveShell() {
        // Get the active shell in case a diagram part has been detached from the workbench and moved to another monitor.
        // The drawback of this though is if the user moves a dialog box to another monitor and that becomes the active shell
        // when exporting an image.
        Display display = Display.getCurrent();
        if(display == null) {
            display = Display.getDefault();
        }
        
        Shell shell = display.getActiveShell();
        if(shell != null) {
            return shell;
        }
        
        // Try the active workbench window
        if(PlatformUI.isWorkbenchRunning() && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
            return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        }
        
        // No active shell
        return null;
    }
    
    /**
     * Whether Draw2d scaling is enabled on Windows
     */
    public static boolean isDraw2dAutoScaleEnabled() {
        return PlatformUtils.isWindows() && Boolean.parseBoolean(System.getProperty("draw2d.enableAutoscale", Boolean.TRUE.toString()));
    }

    /**
     * @return the display scaling if on Windows and draw2d enableAutoscale is true, else return 1
     */
    public static float getDraw2dDeviceZoom() {
        return isDraw2dAutoScaleEnabled() ? getDeviceZoom() / 100f : 1f;
    }
}
