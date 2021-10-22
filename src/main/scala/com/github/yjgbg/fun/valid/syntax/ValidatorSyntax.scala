package com.github.yjgbg.fun.valid.syntax

import java.util.concurrent.Executor

import com.github.yjgbg.fun.valid.Macros
import com.github.yjgbg.fun.valid.core.*

import scala.concurrent.{ExecutionContext, Future}

object ValidatorSyntax extends ValidatorSyntax
trait ValidatorSyntax:
  inline def inlined[A, B](inline getter: A => B): Getter[A, B] = (Macros.propName(getter), getter)
  private val syncExecutorContext:ExecutionContext = ExecutionContext.fromExecutor(_.run())
  extension[A] (it: Validator[A])
    def +[B](validator: Validator[B]): Validator[A & B] = Validator.plus(it, validator)
    def x[B](validator:Validator[B]):Validator[A|B] = Validator.func {
      case a:A => ec =>failFast => _ =>  it(ec)(failFast)(a)
      case b:B => ec =>failFast => _ =>  validator(ec)(failFast)(b)
    }
    def `for`[B <: A]:Validator[B] = it
    inline def and[B](inline getter: A => B, validator: Validator[B]): Validator[A] =
      Validator.plus(it, Validator.transform(inlined(getter), validator))
    inline def and[B](inline getter: A => B, errorMsg: ErrorMsg[B], constraint: Constraint[B]): Validator[A] =
      Validator.plus(it,Validator.transform(inlined(getter),Validator.simple(errorMsg,constraint)))
    // 将A类型的校验器转换为A容器的校验器
    def iter: Validator[Iterable[A]] = Validator.func {
      case null => Validator.none
      case iterable => iterable.zipWithIndex
        .map(tuple => Validator.transform[Iterable[A], A]((tuple._2.toString, _ => tuple._1), it))
        .foldLeft(Validator.none)(Validator.plus)
    }
    inline def andIter[B](inline getter: A => Iterable[B], errorMsg: ErrorMsg[B], constraint: Constraint[B]): Validator[A] =
      Validator.plus(it,Validator.transform(inlined(getter),Validator.simple(errorMsg,constraint).iter))
    def sync:FailFast => A => Result = failFast => obj => it(syncExecutorContext)(failFast)(obj).value.get.get
  extension[A] (it:FailFast => A)
    def failFast:A = it(true)
    def nonFailFast:A = it(false)