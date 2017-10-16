/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PartInitException;

import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.utils.HTMLUtils;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Wraps a StyledText Control to listen for hyperlinks and change font height
 * Some of this code is adapted from org.eclipse.ui.internal.about.AboutTextManager
 * 
 * @author Phillip Beauvoir
 */
public class StyledTextControl implements Listener, LineStyleListener {
    
    private StyledText fStyledText;
    
    private Cursor fHandCursor;
    private Cursor fCurrentCursor;
    
    private int fNormalFontHeight;
    private Font fCurrentFont;
    
    private List<int[]> fLinkRanges;
    private List<String> fLinks;
        
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
        
        fStyledText.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if(fHandCursor != null && !fHandCursor.isDisposed()) {
                    fHandCursor.dispose();
                    fHandCursor = null;
                }
                
                fStyledText.removeListener(SWT.MouseUp, StyledTextControl.this);
                fStyledText.removeListener(SWT.MouseMove, StyledTextControl.this);
                fStyledText.removeListener(SWT.MouseVerticalWheel, StyledTextControl.this);
                fStyledText.removeListener(SWT.KeyDown, StyledTextControl.this);
                fStyledText.removeListener(SWT.KeyUp, StyledTextControl.this);
                
                fStyledText.removeLineStyleListener(StyledTextControl.this);
                
                fCurrentCursor = null;
                fLinks = null;
                
                if(fCurrentFont != null && !fCurrentFont.isDisposed()) {
                    fCurrentFont.dispose();
                    fCurrentFont = null;
                }
            }
        });
        
        fStyledText.addListener(SWT.MouseUp, this);
        fStyledText.addListener(SWT.MouseMove, this);
        fStyledText.addListener(SWT.MouseVerticalWheel, this);
        fStyledText.addListener(SWT.KeyDown, this);
        fStyledText.addListener(SWT.KeyUp, this);
        
        fStyledText.addLineStyleListener(this);
        
        // Filter out any illegal xml characters
        UIUtils.applyInvalidCharacterFilter(fStyledText);
        
        hookContextMenu();
        
        setInitialFontHeight();
    }
    
    @Override
    public void lineGetStyle(LineStyleEvent event) {
        // Do this on any text change because it will be needed for mouse over
        scanLinks(fStyledText.getText());
        
        int lineLength = event.lineText.length();
        if(lineLength < 8) {
            return; // optimise
        }
        
        int lineOffset = event.lineOffset;
        
        List<StyleRange> list = new ArrayList<StyleRange>();
        
        for(int[] linkRange : fLinkRanges) {
            int start = linkRange[0];
            int length = linkRange[1];
            if(start >= lineOffset && (start + length) <= (lineOffset + lineLength)) {
                StyleRange sr = new StyleRange(start, length, ColorConstants.blue, null);
                sr.underline = true;
                list.add(sr);
            }
        }
        
        if(!list.isEmpty()) {
            event.styles = list.toArray(new StyleRange[list.size()]);
        }
    }
    
    private void setInitialFontHeight() {
        FontData fd = fStyledText.getFont().getFontData()[0];
        fNormalFontHeight = fd.getHeight();
        
        // Save in Prefs
        IPreferenceStore store = Preferences.STORE;
        int savedFontHeight = store.getInt("StyledTextFontHeight"); //$NON-NLS-1$
        
        if(savedFontHeight != 0 && savedFontHeight != fNormalFontHeight) {
            fd.setHeight(savedFontHeight);
            Font newFont = new Font(fStyledText.getDisplay(), fd);
            fStyledText.setFont(newFont);
            fCurrentFont = newFont;
        }
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu1"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
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
    
    public String setText() {
        return fStyledText.getText();
    }
    
    /**
     * Scan links using method 1
     */
    @SuppressWarnings("unused")
    private void scanLinksOld(String s) {
        fLinkRanges = new ArrayList<int[]>();
        fLinks = new ArrayList<String>();
        
        int urlSeparatorOffset = s.indexOf("://"); //$NON-NLS-1$
        while(urlSeparatorOffset >= 0) {
            // URL protocol (left to "://")
            int urlOffset = urlSeparatorOffset;
            char ch;
            do {
                urlOffset--;
                ch = ' ';
                if(urlOffset > -1) {
                    ch = s.charAt(urlOffset);
                }
            }
            while(Character.isUnicodeIdentifierStart(ch));
            urlOffset++;

            // Right to "://"
            StringTokenizer tokenizer = new StringTokenizer(s.substring(urlSeparatorOffset + 3), " \t\n\r\f<>", false); //$NON-NLS-1$
            if(!tokenizer.hasMoreTokens()) {
                return;
            }

            int urlLength = tokenizer.nextToken().length() + 3 + urlSeparatorOffset - urlOffset;

            fLinkRanges.add(new int[]{urlOffset, urlLength});
            fLinks.add(s.substring(urlOffset, urlOffset + urlLength));

            urlSeparatorOffset = s.indexOf("://", urlOffset + urlLength + 1); //$NON-NLS-1$
        }
    }

    /**
     * Scan links using a better method
     */
    private void scanLinks(String s) {
        fLinkRanges = new ArrayList<int[]>();
        fLinks = new ArrayList<String>();
        
        Matcher matcher = HTMLUtils.HTML_LINK_PATTERN.matcher(s);
        while(matcher.find()) {
            String group = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            
            fLinkRanges.add(new int[]{start, end - start});
            fLinks.add(group);
        }
    }
    
    /**
     * Returns true if a link is present at the given character location
     */
    private boolean isLinkAt(int offset) {
        for(int[] linkRange : fLinkRanges) {
            int start = linkRange[0];
            int length = linkRange[1];
            if(offset >= start && offset < start + length) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Returns the link at the given offset (if there is one),
     * otherwise returns null
     */
    private String getLinkAt(int offset) {
        for(int i = 0; i < fLinkRanges.size(); i++) {
            int start = fLinkRanges.get(i)[0];
            int length = fLinkRanges.get(i)[1];
            if(offset >= start && offset < start + length) {
                return fLinks.get(i);
            }
        }
        return null;
    }

    @Override
    public void handleEvent(Event event) {
        switch(event.type) {
            case SWT.MouseUp:
                doMouseUp(event);
                break;
            case SWT.MouseMove:
                doMouseMove(event);
                break;
            case SWT.MouseVerticalWheel:
                doMouseWheel(event);
                break;
            case SWT.KeyDown:
                doKeyDown(event);
                break;
            case SWT.KeyUp:
                doKeyUp(event);
                break;
        }
    }

    /**
     * Mouse Up - Open link if Mode Key is pressed and on link
     */
    private void doMouseUp(Event e) {
        if(isModKeyPressed(e)) {
            int offset;
            try {
                offset = fStyledText.getOffsetAtLocation(new Point(e.x, e.y));
            }
            catch(IllegalArgumentException ex) {
                return;
            }
            
            // Open link in Browser
            if(isLinkAt(offset)) {
                try {
                    HTMLUtils.openLinkInBrowser(getLinkAt(offset));
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
            int offset;
            try {
                offset = fStyledText.getOffsetAtLocation(new Point(e.x, e.y));
            }
            catch(IllegalArgumentException ex) {
                setCursor(null); // need this
                return;
            }
            
            if(isLinkAt(offset)) {
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
    
    private void doMouseWheel(Event e) {
        // Mod key pressed, check font zoom
        if(isModKeyPressed(e)) {
            // Frig it...
            if(e.count < 0) {
                e.character = '-';
            }
            else {
                e.character = '+';
            }
            
            checkZoomKeys(e);
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
            int offset;
            try {
                offset = fStyledText.getOffsetAtLocation(pt);
            }
            catch(IllegalArgumentException ex) {
                return;
            }
            if(isLinkAt(offset)) {
                setCursor(fHandCursor);
            }
        }
        
        // Zoom keys when Ctrl/Command key pressed and +, =, - , 0
        if(isModKeyPressed(e)) {
            checkZoomKeys(e);
        }
    }
    
    /**
     * Check for zoom keys
     * @param e
     */
    private void checkZoomKeys(Event e) {
        int factor = 0;
        
        switch(e.character) {
            case '=':
            case '+':
                factor = 2;
                break;

            case '-':
                factor = -2;
                break;
                
            case '0':
                factor = 0;
                break;

            default:
                return;
        }
        
        Font previousFont = fStyledText.getFont();
        FontData fd = previousFont.getFontData()[0];
        
        if(factor == 0) {
            if(fd.getHeight() == fNormalFontHeight) {
                return;
            }
            fd.setHeight(fNormalFontHeight);
        }
        else {
            fd.setHeight(fd.getHeight() + factor);
            
            if(fd.getHeight() < 8 || fd.getHeight() > 64) {
                return;
            }
        }
        
        Font newFont = new Font(fStyledText.getDisplay(), fd);
        fStyledText.setFont(newFont);
        
        if(fCurrentFont != null && !fCurrentFont.isDisposed()) {
            fCurrentFont.dispose();
        }
        
        fCurrentFont = newFont;
        
        // Save in Prefs
        IPreferenceStore store = Preferences.STORE;
        store.setValue("StyledTextFontHeight", fd.getHeight()); //$NON-NLS-1$
    }
    
    /**
     * Key up
     */
    private void doKeyUp(Event e) {
        if(e.keyCode == SWT.MOD1) {
            setCursor(null);
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
        return (e.stateMask & SWT.MOD1) != 0;
    }
}
