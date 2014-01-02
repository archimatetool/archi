/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * Drop Down Action
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDropDownAction extends Action implements IMenuCreator {

    protected Menu fMenu;
    protected List<IContributionItem> fItems = new ArrayList<IContributionItem>();
    
    public AbstractDropDownAction() {
        this(null);
    }
    
    public AbstractDropDownAction(String text) {
        setMenuCreator(this);
        setText(text);
        setToolTipText(text);
    }

    public Menu getMenu(Menu parent) {
        if(fMenu == null) {
            fMenu = new Menu(parent);
            fill();
        }

        return fMenu;
    }

    public Menu getMenu(Control parent) {
        if(fMenu == null) {
            fMenu = new Menu(parent);
            fill();
        }

        return fMenu;
    }
    
    /**
     * Show the menu at the bottom of a Toolbar Manager
     * @param manager
     */
    public void showMenu(ToolBarManager manager) {
    	showMenu(manager.getControl());
    }

    /**
     * Show the menu at the bottom of a Control (for example a ToolBar)
     * @param control
     */
    public void showMenu(Control control) {
        Menu menu = getMenu(control);
        Point point = control.toDisplay(new Point(0, control.getBounds().height));
        menu.setLocation(point);
        menu.setVisible(true);
    }
    
    private void fill() {
        for(IContributionItem item : fItems) {
            item.fill(fMenu, -1);
        }
    }
    
    public void add(IAction action) {
        add(new ActionContributionItem(action));
    }
    
    public void add(IContributionItem item) {
        fItems.add(item);
    }
    
    public void dispose() {
        if(fMenu != null) {
            fMenu.dispose();
            fMenu = null;
        }
        // Don't dispose of items because they might be re-used
    }
}