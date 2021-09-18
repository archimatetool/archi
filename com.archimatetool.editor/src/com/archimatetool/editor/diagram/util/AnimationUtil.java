/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RoutingAnimator;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.IAnimatableCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;



/**
 * AnimationUtil
 * 
 * @author Phillip Beauvoir
 */
public final class AnimationUtil {
    
    /**
     * @return True if OS and version supports animation
     */
    public static boolean supportsAnimation() {
        return true;
    }

    public static boolean doAnimate() {
       return ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.ANIMATE_VIEW);
    }
    
    public static int animationSpeed() {
        return ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.ANIMATION_VIEW_TIME);
    }

    /**
     * Register a CommandStack for Animation on some Undo/Redo events
     * @param stack
     */
    public static void registerCommandStack(CommandStack stack) {
        if(!supportsAnimation()) {
            return;
        }
        
        stack.addCommandStackEventListener(new CommandStackEventListener() {
            @Override
            public void stackChanged(CommandStackEvent event) {
                if(doAnimate()) {
                    if(event.getDetail() == CommandStack.PRE_UNDO || event.getDetail() == CommandStack.PRE_REDO) {
                        if(isAnimatableCommand(event.getCommand())) {
                            Animation.markBegin();
                        }
                    }
                    
                    else if(event.getDetail() == CommandStack.POST_UNDO || event.getDetail() == CommandStack.POST_REDO) {
                        if(isAnimatableCommand(event.getCommand())) {
                            Animation.run(animationSpeed());
                        }
                    }
                }
            }
            
            private boolean isAnimatableCommand(Command cmd) {
                if(cmd instanceof CompoundCommand && ((CompoundCommand)cmd).canExecute()) {
                    for(Object command : ((CompoundCommand)cmd).getCommands()) {
                        if(!(command instanceof IAnimatableCommand)) {
                            return false;
                        }
                    }
                    return true;
                }
                
                return cmd instanceof IAnimatableCommand;
            }
        });
    }
    
    /**
     * Add a figure to be animated when layed out
     * @param figure
     */
    public static void addFigureForAnimation(IFigure figure) {
        if(supportsAnimation()) {
            figure.addLayoutListener(LayoutAnimator.getDefault());
        }
    }
    
    /**
     * Add a line connection to be animated when setting Routing option
     * @param connection
     */
    public static void addConnectionForRoutingAnimation(PolylineConnection connection) {
        if(supportsAnimation()) {
            connection.addRoutingListener(RoutingAnimator.getDefault());
        }
    }
}
