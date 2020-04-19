package com.github.saint1991.akka.grpc.invoker.runtime

case class UndefinedCall(
  message: String,
  cause: Throwable,
) extends Exception(message, cause)
