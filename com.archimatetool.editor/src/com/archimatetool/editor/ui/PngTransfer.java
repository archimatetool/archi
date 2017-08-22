package com.archimatetool.editor.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * Custom clipboard transfer to work around SWT bug 283960 that make copy image
 * to clipboard not working on Linux 64. See
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=283960
 * 
 */
public class PngTransfer extends ByteArrayTransfer {

    private static final String IMAGE_PNG = "image/png"; //$NON-NLS-1$
    private static final int ID = registerType(IMAGE_PNG);

    private static PngTransfer _instance = new PngTransfer();

    private PngTransfer() {
    }

    public static PngTransfer getInstance() {
        return _instance;
    }

    @Override
    protected String[] getTypeNames() {
        return new String[] { IMAGE_PNG };
    }

    @Override
    protected int[] getTypeIds() {
        return new int[] { ID };
    }

    @Override
    protected void javaToNative(Object object, TransferData transferData) {
        if (object == null || !(object instanceof ImageData)) {
            return;
        }

        if (isSupportedType(transferData)) {
            ImageData image = (ImageData) object;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
                // write data to a byte array and then ask super to convert to pMedium

                ImageLoader imgLoader = new ImageLoader();
                imgLoader.data = new ImageData[] { image };
                imgLoader.save(out, SWT.IMAGE_PNG);

                byte[] buffer = out.toByteArray();
                out.close();

                super.javaToNative(buffer, transferData);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    protected Object nativeToJava(TransferData transferData) {
        if (isSupportedType(transferData)) {

            byte[] buffer = (byte[]) super.nativeToJava(transferData);
            if (buffer == null) {
                return null;
            }

            try (ByteArrayInputStream in = new ByteArrayInputStream(buffer)) {
                return new ImageData(in);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return null;
    }

}
