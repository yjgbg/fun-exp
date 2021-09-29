package com.github.yjgbg.funexp.aop;

import com.github.yjgbg.funexp.aop.support.Decorator;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Benchmark implements Decorator {
  @Override
  public Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
    return args -> {
      final var start = System.currentTimeMillis();
      final var res = method.apply(args);
      final var cost = System.currentTimeMillis() - start;
      System.out.printf("用时:%s\n",cost);
      return res;
    };
  }
}
