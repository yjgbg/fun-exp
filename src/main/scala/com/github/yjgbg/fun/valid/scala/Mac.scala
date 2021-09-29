package com.github.yjgbg.fun.valid.scala

object Mac:
  inline def propName[A](inline getter: A => Any): String = ${getterName('getter)}

  import scala.quoted.*

  def getterName[A](expr: Expr[A => Any])(using Quotes):Expr[String] =
    val quotes = summon[Quotes]
    import quotes.reflect.*
    Expr(inlineBody(quotes,expr.asTerm)
      .asInstanceOf[Block].statements.head
      .asInstanceOf[DefDef].rhs.get
      .asInstanceOf[Select].name)

  private def inlineBody(quotes: Quotes, term: quotes.reflect.Term): quotes.reflect.Term = term match {
    case it: quotes.reflect.Inlined => inlineBody(quotes, it.body)
    case _ => term
  }