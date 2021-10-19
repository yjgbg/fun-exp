package com.github.yjgbg.fun.valid.core

import scala.concurrent.{ExecutionContext, Future}
type ErrorMsg[-A] = A => String
type FailFast = Boolean
type Constraint[-A] = A => Boolean
type Getter[A,B] = (String,A => B)
type Validator[-A] = (ExecutionContext,FailFast,A) => Future[Result]
object Validator:
  // 空校验器
  def none[A]: Validator[A] = (_, _, _) => Future.successful(Result.None)
  inline def apply[A]:Validator[A] = none
  // 简单校验器
  def simple[A](errorMsg: ErrorMsg[A], constraint: Constraint[A]): Validator[A] =
    (ec, _, a) => Future {
      if (constraint(a)) Result.None else Result.Simple(a, errorMsg(a))
    }(using ec)

  // 一类复杂校验器： 由两个校验器构造而成
  def plus[A, B](v0: Validator[A], v1: Validator[B]): Validator[A & B] = (ec, failFast, obj) =>
    (v0(ec, failFast, obj).zip(v1(ec, failFast, obj)): Future[(Result, Result)]).map {
      case (Result.None, other: Result) => other
      case (one: Result, _) if failFast => one
      case (one: Result, other: Result) => Result.Plus(one, other)
    }(using ec)

  // 二类复杂校验器 ： 由一个属性 提取器和一个校验器构造而成
  def transform[A, B](getter: Getter[A, B], validator: Validator[B]): Validator[A] =
    (ec, failFast, a) => validator(ec, failFast, getter._2(a)).map(Result.Transformation(getter._1, _))(using ec)

  // 三类复杂校验器: 由一个目标对象到校验器的function构造而成
  def func[A](f: A => Validator[A]): Validator[A] = (ec, failFast, a) => f(a)(ec, failFast, a)
