package com.github.saint1991.akka.grpc.invoker.codegen

import akka.grpc.gen.Logger
import akka.grpc.gen.scaladsl.ScalaCodeGenerator
import akka.grpc.gen.scaladsl.Service
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse

import templates.txt.ServiceInvoker

import scala.collection.immutable

class AkkaGrpcInvokerGenerator extends ScalaCodeGenerator {

  override val name: String = "akka-grpc-invoker-generator"

  override def perServiceContent: Set[(Logger, Service) => immutable.Seq[CodeGeneratorResponse.File]] =
    super.perServiceContent + generateInvoker

  private val generateInvoker: (Logger, Service) => immutable.Seq[CodeGeneratorResponse.File] =
    (logger, service) => {
      val b = CodeGeneratorResponse.File.newBuilder()

      val name = s"${service.packageDir}/${service.name}ServiceInvoker.scala"
      b.setName(name)
      b.setContent(ServiceInvoker(service).body)

      logger.info(s"Generate $name...")
      b.build() :: Nil
    }
}

object AkkaGrpcInvokerGenerator extends AkkaGrpcInvokerGenerator
