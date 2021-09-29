package com.github.yjgbg.fun.valid.scala

object CoreOps :
  extension[A] (inline func:A => Any)
    inline def propName:String = Mac.propName(func)
  import com.github.yjgbg.fun.valid.core.{Errors, Validator}
  private inline def transform[A, B](inline getter: A => B, validator: Validator[B]): Validator[A] =
    (exe, failFast, obj) => validator.apply(exe, failFast, getter(obj)).map(Errors.transform(getter.propName, _))
  extension(string:String)
    def fmt[A]:A => String = a => String.format(string,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a)
  extension[A] (v: Validator[A])
    inline def and(v2: Validator[A]):Validator[A] = Validator.plus(v, v2)

    inline def and[B](inline getter: A => B, message: B => String, constraint: B => Boolean):Validator[A] =
      and(transform(getter, Validator.simple(constraint(_), message(_))))

    inline def equ[B](inline getter: A => B, message: B => String, value: B):Validator[A] =
      and(transform(getter, Validator.simple(_ == value, message(_))))

    inline def gt[B : Ordering](inline getter: A => B, message: B => String, value: B): Validator[A] =
      import scala.math.Ordered.orderingToOrdered
      and(transform(getter, Validator.simple[B](it => it > value, message(_))))
