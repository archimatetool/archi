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

import com.archimatetool.editor.diagram.commands.IAnimatableCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.utils.PlatformUtils;



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
        // It doesn't work on Mac High Sierra 10.13.x (or any Mac version with Eclipse 4.7 Oxygen)
        boolean macSupported = PlatformUtils.isMac() && !System.getProperty("os.version").startsWith("10.13"); //$NON-NLS-1$ //$NON-NLS-2$
        
        return macSupported || PlatformUtils.isWindows() || PlatformUtils.isLinux();
    }

    public static boolean doAnimate() {
        return supportsAnimation() && Preferences.STORE.getBoolean(IPreferenceConstants.ANIMATE);
    }
    
    public static int animationSpeed() {
        return Preferences.STORE.getInt(IPreferenceConstants.ANIMATION_SPEED);
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
            public void stackChanged(CommandStackEvent event) {
                if(doAnimate()) {
                    if(event.getDetail() == CommandStack.PRE_UNDO || event.getDetail() == CommandStack.PRE_REDO) {
                        if(isAllowedCommand(event.getCommand())) {
                            Animation.markBegin();
                        }
                    }
                    
                    else if(event.getDetail() == CommandStack.POST_UNDO || event.getDetail() == CommandStack.POST_REDO) {
                        if(isAllowedCommand(event.getCommand())) {
                            Animation.run(animationSpeed());
                        }
                    }
                }
            }
            
            private boolean isAllowedCommand(Command cmd) {
                if(cmd instanceof CompoundCommand) {
                    for(Object command : ((CompoundCommand)cmd).getCommands()) {
                        if(!(command instanceof IAnimatableCommand)) {
                            return false;
                        }
                    }
                    return true;
                }
                else {
                    return cmd instanceof IAnimatableCommand;
                }
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
