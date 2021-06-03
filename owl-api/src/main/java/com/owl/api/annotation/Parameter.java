package com.owl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String name() default "";
    String display() default "";
    String description() default "";
    String placeholder() default "";
    String[] candidates() default {};
    boolean required() default true;
    int order() default 0;
    boolean password() default false;
}
