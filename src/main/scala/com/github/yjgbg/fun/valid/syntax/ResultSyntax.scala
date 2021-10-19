package com.github.yjgbg.fun.valid.syntax

import java.util

import com.github.yjgbg.fun.valid.core.*

import scala.jdk.CollectionConverters.*

object ResultSyntax extends ResultSyntax
trait ResultSyntax:
  extension (r:Result)
    def toMessageMap:Map[String,Set[String]] = Result.toMessageMap(r)
    def hasError:Boolean = r match {
      case Result.None => false
      case _ => true
    }
    def toMessageJavaMap:java.util.Map[String,java.util.Set[String]] = toMessageMap.map{case (k,v) => k -> v.asJava}.asJava
