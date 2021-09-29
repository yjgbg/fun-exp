package com.github.yjgbg.funexp.scala

object LeetCodeTest {
  def climbStairs(n: Int): Int = lazyList(n-1)
  val lazyList:LazyList[Int] = 1 #:: 2 #:: lazyList.zip(lazyList.tail).map(_+_)

  def c2(n:Int):Int = n match {
    case 1 => 1
    case 2 => 2
    case _ => c2(n-1)+1
  }
}
@main def main2 = println(LeetCodeTest.climbStairs(46))