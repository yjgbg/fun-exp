package com.github.yjgbg.funexp.aop;

import com.github.yjgbg.funexp.aop.support.Decorator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Component
public class CounterDecorator implements Decorator {
  @Override
  public Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    return args -> {
      System.out.println("counterDecorator before");
      method.apply(args);
      System.out.println("counterDecorator after");
      return atomicInteger.getAndAdd(1);
    };
  }
}
