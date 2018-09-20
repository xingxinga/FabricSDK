package com.chsoft.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ClientMethod {
    
    int channel() default -1; 
    
    int peer() default -1;
    
    int orderer() default -1;
}