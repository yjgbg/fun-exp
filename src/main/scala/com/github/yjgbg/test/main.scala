package com.github.yjgbg.test

import cats.kernel.Semigroup

import java.util.concurrent.Executor

import com.github.yjgbg.fun.valid.core.*

import scala.concurrent.ExecutionContext

@main def main = {
  trait Id:
    def id: Long
  trait Named:
    def name:String
  case class Person(id:Long,name:String,description:String = "") extends Id,Named
  case class Idx(id:Long) extends Id
  case class Namex(name:String) extends Named
  import com.github.yjgbg.fun.valid.syntax.AllSyntax.*
  val exe:Executor = _.run()
  val v1 = Validator[Id].and(_.id,id => s"id错误($id)",_ > 0)
  val v2 = Validator[Named]
    .and(_.name,name => s"name大于6($name)",_.length > 6)
    .and(_.name,name => s"name小于3($name)",_.length < 3)
  val v3= v1 + v2
  val v4 = v3.`for`[Person].and(_.description,desc => s"desc错误($desc)",_.length > 5)
  val result = v3(ExecutionContext.fromExecutor(exe),false,Person(-1,"alice"))
  val map:Map[String,Set[String]] = result.value.get.get.toMessageMap
  println(map)
  val r2:Result = v3(failFast = false,Person(-1,"alice"))
  val map2:Map[String,Set[String]] = r2.toMessageMap
  println(map2)
  v1(ExecutionContext.fromExecutor(_.run()),true,Idx(1))
//  v2(ExecutionContext.fromExecutor(_.run()),true,Idx(1))
//  v3(ExecutionContext.fromExecutor(_.run()),true,Idx(1))
//  v1(ExecutionContext.fromExecutor(_.run()),true,Namex("alice"))
  v2(ExecutionContext.fromExecutor(_.run()),true,Namex("alice"))
//  v3(ExecutionContext.fromExecutor(_.run()),true,Namex("alice"))
  v1(ExecutionContext.fromExecutor(_.run()),true,Person(1,"alice"))
  v2(ExecutionContext.fromExecutor(_.run()),true,Person(1,"alice"))
  v3(ExecutionContext.fromExecutor(_.run()),true,Person(1,"alice"))
  println(v4(Person(-1, "alice", "desc")).toMessageMap)
}