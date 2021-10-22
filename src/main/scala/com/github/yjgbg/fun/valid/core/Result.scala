package com.github.yjgbg.fun.valid.core


type Result = Result.None.type | Result.Simple | Result.Plus | Result.Transformation

object Result:
  case object None

  case class Simple(rejectObject: Any, msg: String)

  case class Plus(error0: Result, error1: Result)

  object Plus:
    def apply(error0: Result, error1: Result): Result = (error0, error1) match {
      case (None, _) => error1
      case (_, None) => error0
      case _ => new Plus(error0, error1)
    }

  case class Transformation(field: String, error: Result)

  object Transformation:
    def apply(field: String, error: Result): None.type | Transformation = error match {
      case None => None
      case other => new Transformation(field, other)
    }

