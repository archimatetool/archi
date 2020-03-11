/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Clipboard wrapper for application copy and paste
 * This is based on org.eclipse.gef.ui.actions.Clipboard and org.eclipse.gef.dnd.SimpleObjectTransfer
 * 
 * @author Phillip Beauvoir
 */
public class LocalClipboard extends EventManager {

    private static LocalClipboard defaultClipboard = new LocalClipboard();
    
    private static final ByteArrayTransfer TRANSFER = new ByteArrayTransfer() {
        private final String TYPE_NAME = "com.archimatetool.editor.clipboard.transfer" + System.currentTimeMillis(); //$NON-NLS-1$
        private final int TYPE_ID = registerType(TYPE_NAME);

        private Object object;

        @Override
        protected int[] getTypeIds() {
            return new int[] { TYPE_ID };
        }

        @Override
        protected String[] getTypeNames() {
            return new String[] { TYPE_NAME };
        }
        
        @Override
        public void javaToNative(Object object, TransferData transferData) {
            this.object = object;
            super.javaToNative(TYPE_NAME.getBytes(), transferData);
        }

        @Override
        public Object nativeToJava(TransferData transferData) {
            byte bytes[] = (byte[]) super.nativeToJava(transferData);

            if(bytes != null) {
                try {
                    return TYPE_NAME.equals(new String(bytes)) ? object : null;
                }
                catch(Exception ex) {
                    // Something weird on Linux throwing NPE...
                    // See https://github.com/archimatetool/archi/issues/592
                }
            }
            
            return null;
        }
    };
    
    /**
     * @return the default clipboard
     */
    public static LocalClipboard getDefault() {
        return defaultClipboard;
    }
    
    private Clipboard clipBoard = new Clipboard(null);
    
    private LocalClipboard() {
        if(PlatformUI.isWorkbenchRunning()) {
            // The System clipboard contents may have changed and some clients may wish to check it.
            PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
                @Override
                public final void windowActivated(IWorkbenchWindow window) {
                    fireClipboardChanged(getContents());
                }

                @Override
                public final void windowClosed(IWorkbenchWindow window) {
                }

                @Override
                public final void windowDeactivated(IWorkbenchWindow window) {
                }

                @Override
                public final void windowOpened(IWorkbenchWindow window) {
                }
            });
        }
    }
    
    /**
     * @return contents of the clipboard
     */
    public Object getContents() {
        return clipBoard.getContents(TRANSFER);
    }

    /**
     * Sets the contents of the clipboard. This will erase the previous contents
     * of this as well as the system clipboard. The provided contents will not
     * be garbage-collected until some other contents are set using this method.
     * 
     * @param contents the new contents
     */
    public void setContents(Object contents) {
        clipBoard.setContents(new Object[] { contents }, new Transfer[] { TRANSFER });
        fireClipboardChanged(contents);
    }
    
    /**
     * Add a ClipboardListener
     * @param l
     */
    public void addListener(ILocalClipboardListener l) {
        addListenerObject(l);
    }

    /**
     * Remove a ClipboardListener
     * @param l
     */
    public void removeListener(ILocalClipboardListener l) {
        removeListenerObject(l);
    }

    private void fireClipboardChanged(Object contents) {
        for(Object element : getListeners()) {
            final ILocalClipboardListener l = (ILocalClipboardListener)element;
            SafeRunner.run(new SafeRunnable() {
                @Override
                public void run() {
                    l.clipBoardChanged(contents);
                }
            });
        }
    }
}
