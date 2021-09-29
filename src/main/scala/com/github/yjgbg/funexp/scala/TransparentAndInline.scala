package com.github.yjgbg.funexp.scala

@main def TransparentAndInline = {
  class A
  class B extends A
  extension (b: B) def m = true

  transparent inline def i = 1

  val one: 1 = i
  // 可以理解为inline是在类型检查之后展开，transparent inline 是在类型检查之前展开
  type Choose = [Type <: Boolean] =>> Type match
    case true => A
    case false => B

  transparent inline def choose[T <: true | false](boolean: T): Choose[T] = inline boolean match
    case _: true => new A
    case _: false => new B

  // 普通函数为黑盒，内联函数为白盒
  val a = choose(true)
  val b = choose(false)
  b.m
  //  a.m //compile type error

  transparent inline def g(x: String | Double) = inline x match
    case x: String => (x, x)
    case x: Double => x

  val g0 = g(1.0d)
  val g1: (String, String) = g("1.0")
  val g10 = g1._1
  val g11 = g1._2
  val it = g0 + g0
  println(it.getClass)
  import scala.compiletime._
  import scala.compiletime.ops.int.S
  transparent inline def toIntC[A]: Int = inline constValue[A] match
    case 0 => 0
    case _: S[n1] => 1 + toIntC[n1]

  transparent inline def defaultValue[A] = inline erasedValue[A] match
    case _: Byte => Some(0: Byte)
    case _ => None
}