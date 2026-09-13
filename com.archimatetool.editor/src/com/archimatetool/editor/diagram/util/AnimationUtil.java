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
       return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.ANIMATE_VIEW);
    }
    
    public static int animationSpeed() {
        return ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.ANIMATION_VIEW_TIME);
    }

    /**
     * Register a CommandStack for Animation on some Undo/Redo events
     */
    public static void registerCommandStack(CommandStack stack) {
        if(!supportsAnimation()) {
            return;
        }
        
        stack.addCommandStackEventListener(new CommandStackEventListener() {
            Command animatedCmd = null;

            @Override
            public void stackChanged(CommandStackEvent event) {
                if(doAnimate()) {
                    int detail = event.getDetail();
                    
                    if((detail == CommandStack.PRE_UNDO || detail == CommandStack.PRE_REDO) && isAnimatableCommand(event.getCommand())) {
                        animatedCmd = event.getCommand();
                        Animation.markBegin();
                    }
                    else if(animatedCmd == event.getCommand() && (detail == CommandStack.POST_UNDO || detail == CommandStack.POST_REDO)) {
                        animatedCmd = null;
                        Animation.run(animationSpeed());
                    }
                }
            }
        });
    }
    
    private static boolean isAnimatableCommand(Command cmd) {
        if(cmd instanceof IAnimatableCommand) {
            return true;
        }
        
        // If a CompoundCommand all sub-commands must be IAnimatableCommand
        if(cmd instanceof CompoundCommand compoundCmd) {
            return !compoundCmd.isEmpty() && compoundCmd.getCommands().stream()
                                            .allMatch(subCmd -> isAnimatableCommand(subCmd)); // recurse
        }
        
        return false;
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
