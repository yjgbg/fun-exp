package com.github.yjgbg.funexp.ctl;

import com.github.yjgbg.funexp.aop.CounterDecorator;
import com.github.yjgbg.funexp.aop.LogDecorator;
import com.github.yjgbg.funexp.aop.Benchmark;
import com.github.yjgbg.funexp.aop.support.Decorate;
import com.github.yjgbg.funexp.svc.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleCtl {
  private final SampleService sampleService;

  @Decorate(LogDecorator.class)
  @GetMapping("hello1")
  public Integer hello1() {
    return sampleService.hello1();
  }

  @Decorate(Benchmark.class)
  @Decorate(LogDecorator.class)
  @Decorate(CounterDecorator.class)
  @GetMapping("hello2")
  public Integer hello2() {
    return sampleService.hello1("asd");
  }

  @Decorate(LogDecorator.class)
  @GetMapping("counter")
  public Integer counter() {
    return sampleService.counter();
  }
}
