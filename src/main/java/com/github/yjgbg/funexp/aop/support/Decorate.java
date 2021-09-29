package com.github.yjgbg.funexp.aop.support;

import java.lang.annotation.*;

@Inherited
@Repeatable(value = RepeatedDecorate.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Decorate {
  Class<? extends Decorator> value();
  String params() default "";
}
