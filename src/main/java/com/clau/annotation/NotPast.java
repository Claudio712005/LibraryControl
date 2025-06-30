package com.clau.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NotPast {
    String message() default "A data não pode ser no passado.";
}
