package com.github.yjgbg.funexp.svc;

import com.github.yjgbg.funexp.aop.CounterDecorator;
import com.github.yjgbg.funexp.aop.LogDecorator;
import com.github.yjgbg.funexp.aop.support.Decorate;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
  @Decorate(LogDecorator.class)
  @Decorate(CounterDecorator.class)
  public Integer hello1() {
    return null;
  }

  @Decorate(LogDecorator.class)
  @Decorate(CounterDecorator.class)
  public Integer hello1(String args) {
    return null;
  }

  @Decorate(LogDecorator.class)
  @Decorate(CounterDecorator.class)
  public Integer counter() {
    return null;
  }
}
