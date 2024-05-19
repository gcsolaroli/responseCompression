ThisBuild / scalaVersion := "3.4.2"
ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("snapshots")

val zio_version =         "2.1.1"
val zio_http_version =    "3.0.0-RC7"

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio"                            % zio_version,
    "dev.zio" %% "zio-http"                       % zio_http_version,
  ),
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings",
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val commonSettings = commonScalacOptions ++ Seq(
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val root = project
    .in(file("."))
    .settings(name := "responseCompression")
    .settings(commonSettings)
    .settings(dependencies)


cancelable in Global := true
fork in Global := true
