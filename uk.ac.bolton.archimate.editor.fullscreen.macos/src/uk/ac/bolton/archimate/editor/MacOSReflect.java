/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


/**
 * Refelection methods and classes for Mac OS Cocoa
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MacOSReflect {

    /**
     * Map of selectors to id objects
     */
    private static Map<String, Number> fSelectors = new HashMap<String, Number>();
    
    /**
     * The cached class of org.eclipse.swt.internal.cocoa.OS
     */
    public static Class OS;
    
    static {
        try {
            OS = Class.forName("org.eclipse.swt.internal.cocoa.OS");
        }
        catch(ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Mac NSUInteger type
     * 
     * On 32-bit this is an integer, on 64-bit a long.
     */
    public static Class NSUInteger;
    
    static {
        boolean is64bit = System.getProperty("os.arch").equals(Platform.ARCH_X86_64);
        NSUInteger = is64bit ? long.class : int.class;
    }

    /**
     * Return the id of a Mac OS object that is of type org.eclipse.swt.internal.cocoa.id
     * @param id the id object
     * @return the long value
     */
    public static long getID(Object id) {
        return ((Number) getField(id, "id")).longValue();
    }

    /**
     * Send The OS Msg
     * @param target
     * @param sel
     * @param arg
     */
    public static void objc_msgSend(long target, long sel, long arg) {
        executeLong(OS, "objc_msgSend", new Class[] { NSUInteger, NSUInteger, NSUInteger }, target, sel, arg);
    }

    /**
     * Return the Selector long of its string value
     * @param sel
     * @return
     */
    public static long selector(String sel) {
        try {
            Number selector = fSelectors.get(sel);
            if(selector == null) {
                selector = (Number)(OS.getMethod("sel_registerName", String.class).invoke(null, sel));
                fSelectors.put(sel, selector);
            }
            return selector.longValue();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Return the NSWindow object for a Shell
     * Equivalent to NSWindow nswindow = shell.view.window();
     * @param shell
     * @return An Object that is the NSWindow object of the shell
     */
    public static Object getNSWindow(Shell shell) {
        try {
            Field fieldView = Control.class.getDeclaredField("view");
            Object nsView = fieldView.get(shell);
            Method methodWindow = fieldView.getType().getDeclaredMethod("window");
            return methodWindow.invoke(nsView, new Object[] {});
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Execute a method on object and return the long value
     * @param object
     * @param method
     * @param args
     * @return
     */
    public static long executeLong(Object object, String method, Object... args) {
        Class types[] = new Class[args.length];
        for(int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return executeLong(object, method, types, args);
    }

    /**
     * 
     * Execute a method on object and return the long value
     * @param object
     * @param method
     * @param types
     * @param args
     * @return The long value
     */
    public static long executeLong(Object object, String method, Class[] types, Object... args) {
        Class clazz = (Class)(object instanceof Class ? object : object.getClass());
        Object[] newArgs;
        try {
            if(NSUInteger == long.class) {
                newArgs = args;
            }
            else {
                newArgs = new Object[args.length];
                for(int i = 0; i < args.length; i++) {
                    newArgs[i] = new Integer(((Number)args[i]).intValue());
                }
            }
            Method m = clazz.getMethod(method, types);
            Number n = ((Number)m.invoke(object, newArgs));
            return n == null ? -1 : n.longValue();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Return a field in the object
     * @param target
     * @param field
     * @return
     */
    public static Object getField(Object object, String field) {
        try {
            return (object instanceof Class ? (Class)object : object.getClass()).getField(field).get(object);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
