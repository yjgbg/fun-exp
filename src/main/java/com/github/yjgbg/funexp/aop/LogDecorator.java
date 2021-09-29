package com.github.yjgbg.funexp.aop;

import com.github.yjgbg.funexp.aop.support.Decorator;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LogDecorator implements Decorator {
  @Override
  public Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
    return args -> {
      System.out.println("this is in log decorator before");
      final var res = method.apply(args);
      System.out.println("this is in log decorator after");
      return res;
    };
  }
}
