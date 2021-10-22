package com.github.yjgbg.fun.valid.syntax

import java.util

import cats.syntax.all.*
import com.github.yjgbg.fun.valid.core.{ErrorMsg,Validator}
import com.github.yjgbg.fun.valid.syntax.ValidatorSyntax.*

import scala.language.implicitConversions

object ValidatorExtSyntax

trait ValidatorExtSyntax:
  extension[A] (it: Validator[A])
    inline def notNull[B](inline getter: A => B, errorMsg: ErrorMsg[B]) = it.and(getter, errorMsg, _ != null)
    inline def eqv[B: cats.Eq](inline getter: A => B, errorMsg: ErrorMsg[B], value: B) =
      it.and(getter, errorMsg, _ === value)
    inline def neqv[B: cats.Eq](inline getter: A => B, errorMsg: ErrorMsg[B], value: B) =
      it.and(getter, errorMsg, _ =!= value)
    inline def gt[B: cats.Order](inline getter: A => B, errorMsg: ErrorMsg[B], value: B) =
      it.and(getter, errorMsg, _.compare(value) > 0)
    inline def ge[B: cats.Order](inline getter: A => B, errorMsg: ErrorMsg[B], value: B) =
      it.and(getter, errorMsg, _.compare(value) >= 0)
    inline def lt[B: cats.Order](inline getter: A => B, errorMsg: ErrorMsg[B], value: B) =
      it.and(getter, errorMsg, _.compare(value) < 0)
    inline def le[B: cats.Order](inline getter: A => B, errorMsg: ErrorMsg[B], value: B) =
      it.and(getter, errorMsg, _.compare(value) <= 0)
    inline def between[B: cats.Order](inline getter:A => B,errorMsg: ErrorMsg[B],lowerBound:B,upperBound:B) =
      it.ge(getter,errorMsg,lowerBound).le(getter,errorMsg,upperBound)
    inline def notBlank[B <: CharSequence](inline getter: A => B, errorMsg: ErrorMsg[B], allowNull: Boolean) =
      it.and(getter, errorMsg, b => if (b == null) allowNull else b.length() > 0)
    inline def notEmpty[B <: util.Collection[_]](inline getter: A => B, errorMsg: ErrorMsg[B], value: Int) =
      it.and(getter, errorMsg, b => b!=null  && b.size =!= 0)