package com.owl.web.common.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {GlobalExceptionHandler.class})
public @interface EnableExceptionHandler {
}
