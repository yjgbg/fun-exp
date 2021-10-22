package com.github.yjgbg.test

import cats.Order
import java.util.concurrent.Executor

import com.github.yjgbg.fun.valid.core.*

import scala.concurrent.{ExecutionContext, Future}
private val ec:ExecutionContext = ExecutionContext.fromExecutor(_.run())
@main def main(): Unit =
  trait Id:
    def id: Long
  trait Named:
    def name:String
  case class Person(id:Long,name:String,description:String = "") extends Id,Named
  case class Idx(id:Long) extends Id
  case class Namex(name:String) extends Named
  val v1 = Validator[Id]
    .notNull(_.id,"id为空")
    .gt(_.id,"id错误",0L)
  val v2 = Validator[Named]
    .and(_.name,_.toString,_.length > 6)
    .and(_.name,name => s"name小于3($name)",_.length < 3)
  val v3 = v1 + v2 // 这个加法符合结合律，不符合交换律
  val v4 = Validator[Person].and(_.description,desc => s"desc错误($desc)",_.length > 5)
  val v5 = v3 + v4
  val person = Person(-1,"alice","des")
  val it =v5.sync.nonFailFast
  println(it(person))