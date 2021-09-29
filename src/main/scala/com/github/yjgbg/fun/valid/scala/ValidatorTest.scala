package com.github.yjgbg.fun.valid.scala

import com.github.yjgbg.fun.valid.core.Validator
import com.github.yjgbg.fun.valid.ext.LbkExtValidatorsStd

import java.util.stream.Collectors

trait Named:
  def getName:String

case class Person(var name:String,var age:Int) extends Named:
  override def getName = name
import com.github.yjgbg.fun.valid.scala.CoreOps.*
@main def main() = {
  val validator:Validator[Person] = Validator.none[Person]
    .equ(_.getName,"name错误(%s)".fmt,"alice")
    .gt(_.age,"年龄错误(%s)".fmt,18)
  val error = validator(false,Person("alice",17))
  val string = LbkExtValidatorsStd.toJavaMap(error).entrySet().stream()
    .map(e => s"${e.getKey}->${e.getValue}")
    .collect(Collectors.joining("\n"))
  println(string)
//  println(validator(false,Person("alice",17)).hasError)

  def getAge(person: Person) = person.age
}

val lazyList:Stream[Int] = 0 #:: 1 #:: 2 #:: lazyList.zip(lazyList.tail).map(_+_)
def climb(n:Int):Int = lazyList(n)

@main def climbMain = println(climb(2))