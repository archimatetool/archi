/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PartInitException;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.HTMLUtils;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Wraps a StyledText Control to listen for hyperlinks and change font height
 * Some of this code is adapted from org.eclipse.ui.internal.about.AboutTextManager
 * 
 * @author Phillip Beauvoir
 */
public class StyledTextControl {
    
    private StyledText fStyledText;
    
    private Cursor fHandCursor;
    private Cursor fCurrentCursor;
    
    private Color linkColor = new Color(0, 0, 255);
    private Color linkColorDark = new Color(144, 255, 255);
    
    private List<LinkInfo> fLinkInfos;
    
    private class LinkInfo {
        String link;
        int start, length, end;

        LinkInfo(String link, int start) {
            this.link = link;
            this.start = start;
            length = link.length();
            end = start + length;
        }
    }
    
    private String originalText = "", editedText = ""; //$NON-NLS-1$ //$NON-NLS-2$
    private String message;
    
    private Listener eventListener = this::handleEvent;
    private LineStyleListener lineStyleListener = this::lineGetStyle;
    private VerifyKeyListener verifyKeyListener = this::handleVerifyKey;
    
    private final int[] eventTypes = {
        SWT.MouseUp, SWT.MouseMove,
        SWT.KeyDown, SWT.KeyUp,
        SWT.FocusIn, SWT.FocusOut,
        SWT.Paint,
        SWT.Dispose
   };
        
    private IAction fActionSelectAll = new Action(Messages.StyledTextControl_0) {
        @Override
        public void run() {
            fStyledText.selectAll();
        }
    };
    
    private IAction fActionCut = new Action(Messages.StyledTextControl_1) {
        @Override
        public void run() {
            fStyledText.cut();
        }
    };
    
    private IAction fActionCopy = new Action(Messages.StyledTextControl_2) {
        @Override
        public void run() {
            fStyledText.copy();
        }
    };
    
    private IAction fActionPaste = new Action(Messages.StyledTextControl_3) {
        @Override
        public void run() {
            fStyledText.paste();
        }
    };
    
    private IAction fActionDelete = new Action(Messages.StyledTextControl_4) {
        @Override
        public void run() {
            fStyledText.invokeAction(SWT.DEL);
        }
    };

    public StyledTextControl(Composite parent, int style) {
        this(new StyledText(parent, style));
    }
    
    public StyledTextControl(StyledText styledText) {
        fStyledText = styledText;
        fStyledText.setLeftMargin(PlatformUtils.isWindows() ? 4 : 2);
        fStyledText.setKeyBinding(ST.SELECT_ALL, ST.SELECT_ALL);
        
        fHandCursor = new Cursor(styledText.getDisplay(), SWT.CURSOR_HAND);
        
        for(int type : eventTypes) {
            fStyledText.addListener(type, eventListener);
        }
        
        // Line Style
        fStyledText.addLineStyleListener(lineStyleListener);
        
        // Key presses
        fStyledText.addVerifyKeyListener(verifyKeyListener);
        
        // Filter out any illegal xml characters
        UIUtils.applyInvalidCharacterFilter(fStyledText);
        
        // Font
        UIUtils.setFontFromPreferences(fStyledText, IPreferenceConstants.MULTI_LINE_TEXT_FONT, true);
        
        hookContextMenu();
    }
    
    /**
     * Sets the widget message.
     * The message text is displayed as a hint for the user, indicating the purpose of the field.
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    private void lineGetStyle(LineStyleEvent event) {
        // Do this on any text change because it will be needed for mouse over
        scanLinks();
        
        int lineLength = event.lineText.length();
        int lineOffset = event.lineOffset;
        
        List<StyleRange> list = new ArrayList<StyleRange>();
        
        for(LinkInfo info : fLinkInfos) {
            if(info.start >= lineOffset && info.end <= (lineOffset + lineLength)) {
                StyleRange sr = new StyleRange(info.start, info.length,
                        ThemeUtils.isDarkTheme() ? linkColorDark : linkColor,
                        null);
                sr.underline = true;
                list.add(sr);
            }
        }
        
        if(!list.isEmpty()) {
            event.styles = list.toArray(new StyleRange[list.size()]);
        }
    }
    
    private void handleVerifyKey(VerifyEvent event) {
        // On Ctrl/Command + Enter we send out a SWT.DefaultSelection event instead
        if(((event.stateMask & SWT.MODIFIER_MASK) == SWT.MOD1 || (event.stateMask & SWT.MODIFIER_MASK) == SWT.CTRL)
                && (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)) {
            // Consume the key press
            event.doit = false;
            
            // Fire a SWT.DefaultSelection event
            Event e = new Event();
            e.type = SWT.DefaultSelection;
            e.widget = fStyledText;
            e.display = fStyledText.getDisplay();
            e.time = (int)System.currentTimeMillis();
            fStyledText.notifyListeners(SWT.DefaultSelection, e);
        }
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu1"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(fStyledText);
        fStyledText.setMenu(menu);
    }
    
    /**
     * Fill context menu when user right-clicks
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        int textLength = fStyledText.getText().length();
        boolean hasText = textLength > 0;
        boolean hasSelectedText = fStyledText.getSelectionText().length() > 0;
        
        // Cut
        fActionCut.setEnabled(hasSelectedText);
        manager.add(fActionCut);
        
        // Copy
        fActionCopy.setEnabled(hasSelectedText);
        manager.add(fActionCopy);
        
        // Paste
        Clipboard cb = new Clipboard(null);
        Object content = cb.getContents(TextTransfer.getInstance());
        cb.dispose();
        fActionPaste.setEnabled(content != null);
        manager.add(fActionPaste);
        
        // Delete
        fActionDelete.setEnabled(hasSelectedText);
        manager.add(fActionDelete);
        
        // Select All
        manager.add(new Separator());
        fActionSelectAll.setEnabled(hasText);
        manager.add(fActionSelectAll);
    }
    
    public StyledText getControl() {
        return fStyledText;
    }
    
    /**
     * Scan for links in the text
     */
    private void scanLinks() {
        fLinkInfos = new ArrayList<>();
        Matcher matcher = HTMLUtils.HTML_LINK_PATTERN.matcher(fStyledText.getText());
        
        while(matcher.find()) {
            LinkInfo info = new LinkInfo(matcher.group(), matcher.start());
            fLinkInfos.add(info);
        }
    }
    
    /**
     * Returns the link at the given offset (if there is one),
     * otherwise returns null
     */
    private String getLinkAt(int offset) {
        for(LinkInfo info : fLinkInfos) {
            if(offset >= info.start && offset < info.end) {
                return info.link;
            }
        }
        return null;
    }

    private void handleEvent(Event event) {
        switch(event.type) {
            case SWT.MouseUp:
                doMouseUp(event);
                break;
            case SWT.MouseMove:
                doMouseMove(event);
                break;
            case SWT.KeyDown:
                doKeyDown(event);
                break;
            case SWT.KeyUp:
                doKeyUp(event);
                break;
            case SWT.FocusIn:
                // Store original text in case of local Undo
                originalText = fStyledText.getText();
                editedText = originalText;
                // Fall through
            case SWT.FocusOut:
                // Redraw hint
                if(StringUtils.isSet(message) && getControl().getContent().getCharCount() == 0) {
                    getControl().redraw(); 
                }
                break;
            case SWT.Paint:
                doPaint(event);
                break;
            case SWT.Dispose:
                dispose();
                break;
        }
    }

    /**
     * Mouse Up - Open link if Mod Key is pressed and on link
     */
    private void doMouseUp(Event e) {
        if(isModKeyPressed(e)) {
            // Open link in Browser
            int offset = fStyledText.getOffsetAtPoint(new Point(e.x, e.y));
            String link = getLinkAt(offset);
            if(link != null) {
                try {
                    HTMLUtils.openLinkInBrowser(link);
                }
                catch(PartInitException | MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Mouse Move - Update cursor if Mod Key is pressed
     */
    private void doMouseMove(Event e) {
        if(isModKeyPressed(e)) {
            int offset = fStyledText.getOffsetAtPoint(new Point(e.x, e.y));
            if(getLinkAt(offset) != null) {
                setCursor(fHandCursor);
            }
            else {
                setCursor(null);
            }
        }
        else {
            setCursor(null);
        }
    }
    
    /**
     * Key down
     */
    private void doKeyDown(Event e) {
        // Hand Cursor
        if(e.keyCode == SWT.MOD1) {
            Point pt = fStyledText.getDisplay().getCursorLocation();
            pt = fStyledText.toControl(pt);
            int offset = fStyledText.getOffsetAtPoint(pt);
            if(getLinkAt(offset) != null) {
                setCursor(fHandCursor);
            }
        }
        
        if(isModKeyPressed(e)) {
            checkUndoPressed(e);
        }
    }
    
    /**
     * Undo pressed
     */
    private void checkUndoPressed(Event e) {
        if(e.keyCode == 'z') {
            String text = fStyledText.getText();
            
            // Toggle between original text and edited text
            if(text.equals(originalText)) {
                if(!originalText.equals(editedText)) {
                    fStyledText.setText(editedText);
                    fStyledText.setTopIndex(fStyledText.getLineCount() - 1); 
                    fStyledText.setCaretOffset(fStyledText.getText().length());
                }
            }
            else {
                fStyledText.setText(originalText);
                fStyledText.setTopIndex(fStyledText.getLineCount() - 1);
                fStyledText.setCaretOffset(fStyledText.getText().length());
            }
            
            editedText = text;
        }
    }

    /**
     * Key up
     */
    private void doKeyUp(Event e) {
        if(e.keyCode == SWT.MOD1) {
            setCursor(null);
        }
    }
    
    private void doPaint(Event e) {
        // If we have some hint text and text control is blank and not in focus
        if(StringUtils.isSet(message) && getControl().getContent().getCharCount() == 0 && !getControl().isFocusControl()) {
            e.gc.setForeground(getControl().getDisplay().getSystemColor(PlatformUtils.isWindows() ? SWT.COLOR_DARK_GRAY : SWT.COLOR_GRAY));
            e.gc.drawText(message, 3, 1);
        }
    }
    
    /**
     * Optimise setting cursor 1000s of times on mouse motions
     */
    private void setCursor(Cursor cursor) {
        if(fCurrentCursor != cursor) {
            fStyledText.setCursor(cursor);
            fCurrentCursor = cursor;
        }
    }
    
    /**
     * @param e
     * @return true if Mod key is pressed
     */
    private boolean isModKeyPressed(Event e) {
        return (e.stateMask & SWT.MODIFIER_MASK) == SWT.MOD1;
    }
    
    private void dispose() {
        if(fHandCursor != null && !fHandCursor.isDisposed()) {
            fHandCursor.dispose();
            fHandCursor = null;
        }

        for(int type : eventTypes) {
            fStyledText.removeListener(type, eventListener);
        }

        fStyledText.removeLineStyleListener(lineStyleListener);
        
        fStyledText.removeVerifyKeyListener(verifyKeyListener);
        
        fCurrentCursor = null;
        fLinkInfos = null;
        fStyledText = null;
    }
}
