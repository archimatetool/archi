/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.bolton.archimate.editor.views.tree.commands.RenameCommandHandler;
import uk.ac.bolton.archimate.model.INameable;


/**
 * Tree Cell Editor
 * This is basically Snippet111 from http://www.eclipse.org/swt/snippets/ (unattributed author)
 * 
 * @author Phillip Beauvoir
 */
public class TreeCellEditor {
    
    private Tree fTree;
    
    private TreeItem fLastItem;
    private TreeItem fCurrentItem;
    
    private boolean showBorder = true;
    private TreeEditor fEditor;
    private Composite fComposite;
    
    private String fOldText;
    
    public TreeCellEditor(Tree tree) {
        fTree = tree;
        
        // This is OK on Windows, but doesn't work on Mac Cocoa, and gives a dispose error on Linux
        fTree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                //_editItem((TreeItem)event.item);
            }
        });
        
        // This is safer
        fTree.addListener(SWT.MouseUp, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(event.button == 1) {
                    TreeItem item = fTree.getItem(new Point(event.x, event.y));
                    _editItem(item);
                }
            }
        });
        
        fEditor = new TreeEditor(fTree);
    }
    
    public void editItem(TreeItem item) {
        fLastItem = item; // Ensure we are convinced
        _editItem(item);
    }

    private void _editItem(final TreeItem item) {
        if(item != null && item == fLastItem && RenameCommandHandler.canRename(item.getData())) {
            final INameable element = (INameable)item.getData();
            
            fOldText = item.getText();
            fCurrentItem = item;
            
            fComposite = new Composite(fTree, SWT.NONE);
            if(showBorder) {
                fComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
            }
            
            final Text text = new Text(fComposite, SWT.NONE);
            final int inset = showBorder ? 1 : 0;
            
            fComposite.addListener(SWT.Resize, new Listener() {
                public void handleEvent(Event e) {
                    Rectangle rect = fComposite.getClientArea();
                    text.setBounds(rect.x + inset, rect.y + inset, rect.width - inset * 2, rect.height - inset * 2);
                }
            });
            
            Listener textListener = new Listener() {
                @Override
                public void handleEvent(Event event) {
                    switch(event.type) {
                        case SWT.FocusOut:
                            String updatedText = text.getText();
                            if(!updatedText.equals(element.getName())) {
                                disposeEditor();
                                RenameCommandHandler.doRenameCommand(element, updatedText);
                            }
                            else {
                                cancelEditing();
                            }
                            break;

                        case SWT.Verify:
                            String newText = text.getText();
                            String leftText = newText.substring(0, event.start);
                            String rightText = newText.substring(event.end, newText.length());
                            GC gc = new GC(text);
                            Point size = gc.textExtent(leftText + event.text + rightText);
                            gc.dispose();
                            size = text.computeSize(size.x, SWT.DEFAULT);
                            fEditor.horizontalAlignment = SWT.LEFT;
                            //Rectangle itemRect = item.getBounds(), rect = fTree.getClientArea();
                            //fEditor.minimumWidth = Math.max(size.x, itemRect.width) + inset * 2;
                            //int left = itemRect.x, right = rect.x + rect.width;
                            //fEditor.minimumWidth = Math.min(fEditor.minimumWidth, right - left);
                            fEditor.minimumWidth = size.x + 20; // Added this
                            fEditor.minimumHeight = size.y + inset * 2; 
                            fEditor.layout();
                            break;
                            
                        case SWT.Traverse:
                            switch(event.detail) {
                                case SWT.TRAVERSE_RETURN:
                                    updatedText = text.getText();
                                    if(!updatedText.equals(element.getName())) {
                                        disposeEditor();
                                        RenameCommandHandler.doRenameCommand(element, updatedText);
                                    }
                                    else {
                                        cancelEditing();
                                    }
                                    event.doit = false;
                                    break;
                                    
                                case SWT.TRAVERSE_ESCAPE:
                                    cancelEditing();
                                    event.doit = false;
                                    break;
                            }
                            break;
                    }
                }
            };
            
            text.addListener(SWT.FocusOut, textListener);
            text.addListener(SWT.Traverse, textListener);
            text.addListener(SWT.Verify, textListener);
            
            fEditor.setEditor(fComposite, item);
            
            text.setText(element.getName());
            text.selectAll();
            text.setFocus();

            // Clear item
            item.setText("");
        }
        
        fLastItem = item;
    }
    
    public void cancelEditing() {
        disposeEditor();
        
        if(fCurrentItem != null && !fCurrentItem.isDisposed()) {
            fCurrentItem.setText(fOldText);
            fCurrentItem = null;
        }
    }
    
    private void disposeEditor() {
        if(fComposite != null && !fComposite.isDisposed()) {
            fComposite.dispose();
            fComposite = null;
        }
        
        if(fEditor != null) {
            fEditor.setEditor(null, null);
        }
    }
}
