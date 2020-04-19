package com.github.saint1991.akka.grpc.invoker.runtime

import akka.NotUsed
import akka.stream.scaladsl.Source
import scalapb.GeneratedMessage

import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import scala.util.Try

trait Invoker {

  def invoke(rpcName: String, message: Array[Byte]): Future[GeneratedMessage]
  def invokeStream(rpcName: String, message: Array[Byte]): Source[GeneratedMessage, NotUsed]

  private def parsed[M <: GeneratedMessage](raw: Array[Byte], parseFn: Array[Byte] => M): Try[M] = Try(parseFn(raw))

  protected def parseAndInvokeUnary[M <: GeneratedMessage, R](
    raw: Array[Byte],
  )(parseFn: Array[Byte] => M)(invokeFn: M => Future[R]): Future[R] =
    parsed(raw, parseFn).map(invokeFn) match {
      case Failure(ex) => Future.failed(ex)
      case Success(f)  => f
    }

  protected def parseAndInvokeStream[M <: GeneratedMessage, R](
    raw: Array[Byte],
  )(parseFn: Array[Byte] => M)(invokeFn: M => Source[R, NotUsed]): Source[R, NotUsed] =
    parsed(raw, parseFn).map(invokeFn) match {
      case Failure(ex) => Source.failed(ex)
      case Success(s)  => s
    }
}
