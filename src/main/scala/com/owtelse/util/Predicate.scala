package com.owtelse.util

/**
 * Created by robertk on 26/02/15.
 */
case class Predicate[A](pred: A => Boolean) extends (A => Boolean) {
  def apply(x: A) = pred(x)

  def &&(that: A => Boolean) = new Predicate[A](x => pred(x) && that(x))
  def ||(that: A => Boolean) = new Predicate[A](x => pred(x) || that(x))
  def unary_! = new Predicate[A](x => !pred(x))
}

object Predicate {
//  def apply[A](pred: A => Boolean) = new Predicate[A](pred)
  implicit def toPredicate[A](pred: A => Boolean) = new Predicate(pred)
}
