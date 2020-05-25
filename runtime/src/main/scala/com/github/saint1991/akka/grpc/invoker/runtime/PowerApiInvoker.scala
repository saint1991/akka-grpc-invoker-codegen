package com.github.saint1991.akka.grpc.invoker.runtime

import akka.NotUsed
import akka.grpc.scaladsl.Metadata
import akka.stream.scaladsl.Source
import scalapb.GeneratedMessage

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait PowerApiInvoker {

  def invoke(rpcName: String, message: Array[Byte], metadata: Metadata): Future[GeneratedMessage]
  def invokeStream(rpcName: String, message: Array[Byte], metadata: Metadata): Source[GeneratedMessage, NotUsed]

  private def parsed[M <: GeneratedMessage](raw: Array[Byte], parseFn: Array[Byte] => M): Try[M] = Try(parseFn(raw))

  protected def parseAndInvokeUnary[M <: GeneratedMessage, R](
    raw: Array[Byte], metadata: Metadata
  )(parseFn: Array[Byte] => M)(invokeFn: (M, Metadata) => Future[R]): Future[R] =
    parsed(raw, parseFn).map(parsed => invokeFn(parsed, metadata)) match {
      case Failure(ex) => Future.failed(ex)
      case Success(f)  => f
    }

  protected def parseAndInvokeStream[M <: GeneratedMessage, R](
    raw: Array[Byte], metadata: Metadata
  )(parseFn: Array[Byte] => M)(invokeFn: (M, Metadata) => Source[R, NotUsed]): Source[R, NotUsed] =
    parsed(raw, parseFn).map(parsed => invokeFn(parsed, metadata)) match {
      case Failure(ex) => Source.failed(ex)
      case Success(s)  => s
    }
}
