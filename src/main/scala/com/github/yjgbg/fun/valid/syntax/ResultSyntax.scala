package com.github.yjgbg.fun.valid.syntax

import java.util

import com.github.yjgbg.fun.valid.core.*

import scala.jdk.CollectionConverters.*

object ResultSyntax extends ResultSyntax

trait ResultSyntax:
  private val SELF = "__self__"

  import cats.syntax.all.catsSyntaxSemigroup

  extension (r: Result)
    def toMessageMap: Map[String, Set[String]] = r match {
      case Result.None => Map()
      case Result.Simple(_, msg) => Map(SELF -> Set(msg))
      case Result.Plus(a, b) => a.toMessageMap |+| b.toMessageMap
      case Result.Transformation(field, fieldResult: Result) => fieldResult.toMessageMap.map {
        case (SELF, v) => (field, v)
        case (k, v) => (s"$field.$k", v)
      }
    }
    def hasError: Boolean = ! r.isInstanceOf[Result.None.type]
    
    def mapMessage(func: String => String): Result = r match
      case Result.None => Result.None
      case Result.Simple(ro, msg) => Result.Simple(ro, func(msg))
      case Result.Plus(r0, r1) => Result.Plus(r0.mapMessage(func), r1.mapMessage(func))
      case Result.Transformation(field, error) => Result.Transformation(field, error.mapMessage(func))