/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Custom JUnit 4 Suite Runner that takes Strings as test names
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class Suite extends org.junit.runners.Suite {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface SuiteClasses {
        /**
         * @return the classes to be run as fully qualified strings
         */
        String[] value();
    }

    public Suite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(builder, klass, getAnnotatedClasses(klass));
    }
    
    private static Class<?>[] getAnnotatedClasses(Class<?> klass) throws InitializationError {
        SuiteClasses annotation = klass.getAnnotation(SuiteClasses.class);
        if(annotation == null) {
            throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
        }
        
        String[] testNames = annotation.value();
        if(testNames.length == 0) {
            throw new InitializationError("Did not find any test classes");
        }

        Class<?>[] clazzes = new Class<?>[testNames.length];
        
        for(int i = 0; i < testNames.length; i++) {
            try {
                clazzes[i] = Class.forName(testNames[i]);
            }
            catch(ClassNotFoundException ex) {
                throw new InitializationError(ex);
            }
        }
        
        return clazzes;
    }
}
