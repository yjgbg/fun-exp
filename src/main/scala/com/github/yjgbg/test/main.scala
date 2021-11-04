package com.github.yjgbg.test

import cats.syntax.all.*
import cats.Order
import com.github.yjgbg.fun.valid.core.*
import com.github.yjgbg.fun.valid.syntax.all.*
import java.util.concurrent.Executor
import scala.concurrent.{ExecutionContext, Future}
private val ec:ExecutionContext = ExecutionContext.fromExecutor(_.run())
@main def main(): Unit =
  trait Id:
    def id: Long
  trait Named:
    def name:String
  case class Person(id:Long,name:String,description:String = "") extends Id,Named
  case class IdC(id:Long) extends Id
  case class NameC(name:String) extends Named
  val v1 = Validator[Id].notNull(_.id,"id为空").gt(_.id,"id错误",0L)
  val v2 = Validator[Named].and(_.name,"name小于3",_.length > 3)
  val v3 = v1 + v2 // 这个加法符合结合律，但不符合交换律
  val v4 = Validator[Person].and(_.description,"desc错误",_.length > 5)
  val v5 = v3 + v4
  val v6 = v5 |+| v5
  val person = Person(1,"alice","des")
  val it =v5.sync.failFast
  println(it(person).toMessageMap)