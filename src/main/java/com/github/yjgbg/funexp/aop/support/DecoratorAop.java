package com.github.yjgbg.funexp.aop.support;

import io.vavr.CheckedFunction1;
import io.vavr.collection.Stream;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Aspect
@Component
@RequiredArgsConstructor
public class DecoratorAop  {
  private final ApplicationContext applicationContext;
  @Around("@annotation(decorate)")
  public Object proceed(ProceedingJoinPoint joinPoint, Decorate decorate) {
    return fun(joinPoint,decorate).apply(joinPoint.getArgs());
  }

  @Around("@annotation(repeatedDecorate)")
  public Object proceed(ProceedingJoinPoint joinPoint, RepeatedDecorate repeatedDecorate) {
    return fun(joinPoint,repeatedDecorate.value()).apply(joinPoint.getArgs());
  }

  private Function<Object[], Object> fun(ProceedingJoinPoint joinPoint, Decorate... decorates) {
    final var key = joinPoint.getSignature().toLongString();
    return CACHE.computeIfAbsent(key,__ -> fun0(joinPoint,decorates));
  }

  // 以缓存的方式保证装饰器只执行一遍
  private static final Map<String, Function<Object[], Object>> CACHE = new HashMap<>();

  private Function<Object[], Object> fun0(ProceedingJoinPoint joinPoint, Decorate[] decorates) {
    final var fun = CheckedFunction1.<Object[],Object>of(joinPoint::proceed).unchecked();
    return Stream.of(decorates).foldRight(fun,this::proxy);
  }

  private Function<Object[], Object> proxy(Decorate decorate,Function<Object[], Object> function) {
    return applicationContext.getBean(decorate.value()).decorate(function, decorate.params());
  }
}
