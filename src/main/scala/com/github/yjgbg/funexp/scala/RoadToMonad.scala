package com.github.yjgbg.funexp.scala

object RoadToMonad {
  trait Semigroup[A] {
    def combine(a0:A,a1:A):A
  }
  trait Monoid[A] extends Semigroup[A] {
    def zero :A
  }


  sealed trait Maybe[+A]
  case object MyNone extends Maybe[Nothing]
  case class Just[A](value:A) extends Maybe[A]
  // Functor
  trait Functor[F[_]] {
    def fmap[A,B](mapper:A =>  B)(fa:F[A]):F[B]
  }
  given  Functor[Maybe] with {
    override def fmap[A, B](mapper: A => B)(fa: Maybe[A]): Maybe[B] = fa match {
      case MyNone => MyNone
      case Just(head)=> Just(mapper(head))
    }
  }
  //Applicative
  trait Applicative[F[_]] extends Functor[F]{
    def pure[A](a:A):F[A]
    def `<*>`[A,B](mapper:F[A=>B])(fa:F[A]):F[B]
    //  此之为证明：如果一个容器类型为Applicative，则其必为Functor
    override def fmap[A, B](mapper: A => B)(fa: F[A]): F[B] = `<*>`(pure(mapper))(fa)
  }
  given  Applicative[Maybe] with {
    override def `<*>`[A, B](mapper: Maybe[A => B])(fa: Maybe[A]): Maybe[B] = (mapper,fa) match {
      case (MyNone,_) => MyNone
      case (_,MyNone) => MyNone
      case (Just(m),Just(a))=> Just(m(a))
    }

    override def pure[A](a: A): Maybe[A] = Just(a)
  }

  trait Monad[F[_]] extends Applicative[F] {
    override def pure[A](a: A): F[A] = ???
    override def `<*>`[A,B](mapper:F[A=>B])(fa:F[A]):F[B] = ???
    def bind[A,B](mapper:A => F[B])(fa:F[A]):F[B]
  }
  given cats.Monad[Maybe] with{
    override def pure[A](x: A): Maybe[A] = Just(x)

    override def flatMap[A, B](fa: Maybe[A])(f: A => Maybe[B]): Maybe[B] = fa match {
      case MyNone => MyNone
      case Just(value) => f(value)
    }

    override def tailRecM[A, B](a: A)(f: A => Maybe[Either[A, B]]): Maybe[B] = ???
  }

  extension [M[_]:Monad,A](a:M[A]) {
    def flatMap[B](mapper:A => M[B]):M[B] = summon[Monad[M]].bind(mapper)(a)
  }
  extension [M[_]:Monad,A](a:M[M[A]]) {
    def flatten:M[A] = a.flatMap(it =>  it)
  }
}
