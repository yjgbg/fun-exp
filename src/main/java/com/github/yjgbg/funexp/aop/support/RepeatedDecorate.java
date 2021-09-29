package com.github.yjgbg.funexp.aop.support;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatedDecorate {
  Decorate[] value();
}
