package com.clau.annotation;

import com.clau.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
  String path();
  HttpMethod method() default HttpMethod.GET;
}

