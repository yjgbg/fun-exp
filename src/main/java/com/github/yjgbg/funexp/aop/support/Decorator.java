package com.github.yjgbg.funexp.aop.support;

import io.vavr.Function1;
import io.vavr.Tuple;

import java.util.function.Function;

import static io.vavr.API.*;

/**
 * 普通装饰器支持无限个参数
 * typed类型安全装饰器支持0-8个参数(Tuple限制)
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface Decorator {

  /**
   * @param method 需要被被装饰的目标方法
   * @param param  {@link Decorate#params()} 默认空字符串
   * @return 装饰后的代理方法
   */
  Function<Object[], Object> decorate(Function<Object[], Object> method, String param);

  @FunctionalInterface
  interface Typed<A extends Tuple, B> extends Decorator {
    @Override
    @SuppressWarnings("unchecked")
    default Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
      return Function1.<Object[], A>of(Decorator::typed)
          .andThen(typedDecorate(args -> (B) method.apply(args.toSeq().toJavaArray()), param));
    }

    /**
     * @param method 需要被被装饰的目标方法
     * @param param  {@link Decorate#params()} 默认空字符串
     * @return 装饰后的代理方法
     */
    Function<A, B> typedDecorate(Function<A, B> method, String param);
  }

  @FunctionalInterface
  interface Before extends Decorator {
    @Override
    default Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
      return args -> {
        before(args, param);
        return method.apply(args);
      };
    }

    /**
     * 该函数将会在目标函数调用之前调用
     *
     * @param args  目标函数的参数
     * @param param {@link Decorate#params()} 默认空字符串
     */
    void before(Object[] args, String param);

    @FunctionalInterface
    interface Typed<A extends Tuple> extends Before {
      @Override
      default void before(Object[] args, String param) {
        before(typed(args), param);
      }

      /**
       * 该函数将会在目标函数调用之前调用
       *
       * @param args  目标函数的参数
       * @param param {@link Decorate#params()} 默认空字符串
       */
      void before(A args, String param);
    }
  }

  @FunctionalInterface
  interface After extends Decorator {
    @Override
    default Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
      return args -> {
        var res = method.apply(args);
        return after(args, res, param);
      };
    }

    /**
     * 该函数将会在目标函数正常返回之后调用
     *
     * @param args  目标函数的参数
     * @param res   目标函数的返回值
     * @param param {@link Decorate#params()} 默认空字符串
     */
    Object after(Object[] args, Object res, String param);

    @FunctionalInterface
    interface Typed<A extends Tuple, B> extends After {
      @Override
      @SuppressWarnings("unchecked")
      default Object after(Object[] args, Object res, String param) {
        return after(Decorator.typed(args), (B) res, param);
      }

      /**
       * 该函数将会在目标函数正常返回之后调用
       *
       * @param args  目标函数的参数
       * @param res   目标函数的返回值
       * @param param {@link Decorate#params()} 默认空字符串
       */
      B after(A args, B res, String param);
    }
  }

  @FunctionalInterface
  interface AfterThrowing extends Decorator {
    @Override
    default Function<Object[], Object> decorate(Function<Object[], Object> method, String param) {
      return args -> {
        try {
          return method.apply(args);
        } catch (Throwable throwable) {
          return afterThrowing(args, throwable, param);
        }
      };
    }

    /**
     * 该函数将会在目标函数非正常返回时调用
     *
     * @param args      目标函数的参数
     * @param throwable 目标函数抛出的异常
     * @param param     {@link Decorate#params()} 默认空字符串
     */
    Object afterThrowing(Object[] args, Throwable throwable, String param);

    @FunctionalInterface
    interface Typed<A extends Tuple, B> extends AfterThrowing {
      @Override
      default Object afterThrowing(Object[] args, Throwable throwable, String param) {
        return afterThrowing(Decorator.typed(args), throwable, param);
      }

      /**
       * 该函数将会在目标函数非正常返回时调用
       *
       * @param args      目标函数的参数
       * @param throwable 目标函数抛出的异常
       * @param param     {@link Decorate#params()} 默认空字符串
       */
      B afterThrowing(A args, Throwable throwable, String param);
    }
  }

  @SuppressWarnings("unchecked")
  private static <A extends Tuple> A typed(Object[] args) {
    return Match(args.length).of(
        Case($(0), __ -> (A) Tuple()),
        Case($(1), __ -> (A) Tuple(args[0])),
        Case($(2), __ -> (A) Tuple(args[0], args[1])),
        Case($(3), __ -> (A) Tuple(args[0], args[1], args[2])),
        Case($(4), __ -> (A) Tuple(args[0], args[1], args[2], args[3])),
        Case($(5), __ -> (A) Tuple(args[0], args[1], args[2], args[3], args[4])),
        Case($(6), __ -> (A) Tuple(args[0], args[1], args[2], args[3], args[4], args[5])),
        Case($(7), __ -> (A) Tuple(args[0], args[1], args[2], args[3], args[4], args[5], args[6])),
        Case($(8), __ -> (A) Tuple(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]))
    );
  }
}
