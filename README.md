# akka-grpc-invoker-codegen

string rpc name based gRPC service invoker


## Limitation

This only support unary call and server-side stream call.


## Motivation

gRPC has excellent e2e type system, but it is bound to HTTP2 protocol. i.e. invoked RPC is determined based on method name included in HTTP2 path.
This library enables to determine which RPC to invoke based on rpc name passed by other than HTTP2 path.

In my case, putting protobuf messages and rpc name as metadata to a message queue which supports AMQP protocol and consume it by task worker.
Generated code enable worker to call corresponding method to metadata.

## sbt settings

To generate invokers, add the following to project/plugins.sbt

```plugins.sbt
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % AKKA_GRPC_VERSION)

libraryDependencies ++= Seq(
  "com.github.saint1991" %% "akka-grpc-invoker-codegen" % LATEST_VERSION,
  "com.github.saint1991" %% "akka-grpc-invoker-runtime" % LATEST_VERSION,
)
```

It's an extension of Akka gRPC so enable AkkaGrpcPlugin first and add AkkaGrpcInvokerGenerator to akkaGrpcExtraGenerators setting.

```build.sbt
import com.github.saint1991.akka.grpc.invoker.codegen.AkkaGrpcInvokerGenerator

enablePlugins(AkkaGrpcPlugin)

akkaGrpcExtraGenerators += AkkaGrpcInvokerGenerator

libraryDependencies += "com.github.saint1991" %% "akka-grpc-invoker-runtime" % LATEST_VERSION,
```
