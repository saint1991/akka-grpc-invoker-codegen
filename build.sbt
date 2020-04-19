ThisBuild / scalaVersion := "2.12.11"
ThisBuild / version := "0.1"
ThisBuild / organization := "com.github.saint1991"

val akkaGrpcVersion = "0.8.4"

lazy val runtime = (project in file("runtime"))
  .settings(
    name := "akka-grpc-invoker-runtime",
    crossScalaVersions := Seq("2.12.11", "2.13.1"),
    libraryDependencies ++= Seq(
      "com.lightbend.akka.grpc" %% "akka-grpc-runtime" % akkaGrpcVersion,
    ),
  )

lazy val codegen = (project in file("codegen"))
  .dependsOn(runtime % "compile->compile;test->test")
  .enablePlugins(SbtTwirl)
  .settings(
    name := "akka-grpc-invoker-codegen",
    libraryDependencies ++= Seq(
      "com.lightbend.akka.grpc" %% "akka-grpc-codegen" % akkaGrpcVersion,
    ),
  )
