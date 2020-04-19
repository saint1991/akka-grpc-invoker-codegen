# akka-grpc-invoker-codegen

gRPC Code generator for rpc name based service call.

## sbt

```plugins.sbt
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % AKKA_GRPC_VERSION)

libraryDependencies ++= Seq(
  "com.github.saint1991" %% "akka-grpc-invoker-codegen" % LATEST_VERSION,
  "com.github.saint1991" %% "akka-grpc-invoker-runtime" % LATESZT_VERSION,
)


```build.sbt
import com.github.saint1991.akka.grpc.invoker.codegen.AkkaGrpcInvokerGenerator

enablePlugins(AkkaGrpcPlugin)

akkaGrpcExtraGenerators += AkkaGrpcInvokerGenerator
```

