import sbtrelease.Version
import ReleaseTransformations._

ThisBuild / scalaVersion := "2.12.11"
ThisBuild / organization := "com.github.saint1991"
ThisBuild / description := "string rpc name based gRPC service invoker"
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/saint1991/akka-grpc-invoker-codegen"),
    "scm:git@github.com:saint1991:akka-grpc-invoker-codegen.git",
  ),
)

val akkaGrpcVersion = "0.8.4"

lazy val sonatypePassword = sys.env.get("SONATYPE_PASSWORD")

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false,
)

lazy val publishSettings = Seq(
  publishArtifact := true,
  Test / publishArtifact := true,
  publishMavenStyle := true,
  credentials ++= Seq(sonatypePassword match {
    case None => Credentials(Path.userHome / ".sbt" / "sonatype")
    case Some(password) => Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", "saint1991", password)
  }),
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  pomIncludeRepository := { _ =>
    false
  },
  publishTo in ThisBuild := Some(
    if (version.value.trim.endsWith("SNAPSHOT") || isSnapshot.value) Opts.resolver.sonatypeSnapshots
    else Opts.resolver.sonatypeStaging
  ),
  pomExtra :=
    <developers>
      <developer>
        <id>saint1991</id>
        <name>Seiya Mizuno</name>
      </developer>
    </developers>
)

lazy val releaseSettings = Seq(
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  releaseCrossBuild := true,
  releaseVersion := { ver =>
    Version(ver).map(_.withoutQualifier.string).getOrElse(throw new IllegalArgumentException("Version format error"))
  },
  releaseNextVersion := { ver =>
    Version(ver).map(_.bumpMinor.asSnapshot.string).getOrElse(throw new IllegalArgumentException("Version format error"))
  },
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    pushChanges,
    ReleaseStep(action = Command.process("sonatypeRelease", _))
  ),
)

lazy val runtime = (project in file("runtime"))
  .settings(publishSettings)
  .settings(releaseSettings)
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
  .settings(publishSettings)
  .settings(releaseSettings)
  .settings(
    name := "akka-grpc-invoker-codegen",
    libraryDependencies ++= Seq(
      "com.lightbend.akka.grpc" %% "akka-grpc-codegen" % akkaGrpcVersion,
    ),
  )
