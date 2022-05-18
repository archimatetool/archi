package com.archimatetool.editor.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Custom clipboard transfer to work around these bugs:
 * 
 * SWT bug copy image to clipboard not working on Linux 64. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=283960
 * SWT bug pasted image is blank or parts missin on Windows. See https://github.com/eclipse-platform/eclipse.platform.swt/issues/143
 * 
 * For the Linux bug we use PNGTransfer (taken from https://bugs.eclipse.org/bugs/show_bug.cgi?id=283960#c13)
 * 
 * For the Windows bug we use two ByteArrayTransfer instances - the usual ImageTransfer and our BMPTransfer.
 * The BMPTransfer instance fixes the issue for apps like Inkscape and Viso.
 * 
 */
@SuppressWarnings("nls")
public class ClipboardImageTransfer extends ByteArrayTransfer {

    public static void copyImageDataToClipboard(ImageData imageData) {
        Clipboard cb = new Clipboard(null);
        
        try {
            // Windows
            if(PlatformUtils.isWindows()) {
                cb.setContents(new Object[] { imageData, imageData }, new Transfer[] { ImageTransfer.getInstance(), BMPTransfer });
            }
            // Linux
            else if(PlatformUtils.isLinux()) {
                cb.setContents(new Object[] { imageData }, new Transfer[] { PNGTransfer });
            }
            // Mac and everything else
            else {
                cb.setContents(new Object[] { imageData }, new Transfer[] { ImageTransfer.getInstance() });
            }
        }
        finally {
            cb.dispose();
        }
    }
    
    private static final String IMAGE_PNG = "image/png";
    private static final String IMAGE_BMP = "image/bmp";

    private static final int PNG_ID = registerType(IMAGE_PNG);
    private static final int BMP_ID = registerType(IMAGE_BMP);

    private static ClipboardImageTransfer PNGTransfer = new ClipboardImageTransfer(IMAGE_PNG, PNG_ID, SWT.IMAGE_PNG);
    private static ClipboardImageTransfer BMPTransfer = new ClipboardImageTransfer(IMAGE_BMP, BMP_ID, SWT.IMAGE_BMP);

    private String typeName;
    private int type;
    private int swtType;
    
    private ClipboardImageTransfer(String typeName, int type, int swtType) {
        this.typeName = typeName;
        this.type = type;
        this.swtType = swtType;
    }

    @Override
    protected String[] getTypeNames() {
        return new String[] { typeName };
    }

    @Override
    protected int[] getTypeIds() {
        return new int[] { type };
    }

    @Override
    protected void javaToNative(Object object, TransferData transferData) {
        if(object == null || !(object instanceof ImageData)) {
            return;
        }

        if(isSupportedType(transferData)) {
            ImageData imageData = (ImageData)object;
            
            try(ByteArrayOutputStream out = new ByteArrayOutputStream();) {
                // write data to a byte array via ImageLoader and then ask super to convert to pMedium
                ImageLoader imgLoader = new ImageLoader();
                imgLoader.data = new ImageData[] { imageData };
                imgLoader.save(out, swtType);
                super.javaToNative(out.toByteArray(), transferData);
            }
            catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    protected Object nativeToJava(TransferData transferData) {
        if(isSupportedType(transferData)) {
            byte[] buffer = (byte[])super.nativeToJava(transferData);
            if(buffer == null) {
                return null;
            }

            try(ByteArrayInputStream in = new ByteArrayInputStream(buffer)) {
                return new ImageData(in);
            }
            catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return null;
    }
}
