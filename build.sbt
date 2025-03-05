ThisBuild / scalaVersion := "3.3.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "br.com.flashcards"
ThisBuild / organizationName := "zio-flashcards"

val tapirVersion = "1.11.16"
val zioVersion = "2.1.16"
val zioConfigVersion = "4.0.3"
val circeVersion = "0.14.10"
val catsVersion = "2.12.0"
val doobieVersion = "1.0.0-RC6"

lazy val root = (project in file("."))
  .settings(
    name := "zio-flashcards",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-http" % "3.0.1",
      "dev.zio" %% "zio-config" % zioConfigVersion,
      "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
      "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
      "dev.zio" %% "zio-interop-cats" % "23.1.0.3",
      "com.softwaremill.sttp.tapir" %% "tapir-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-enumeratum" % tapirVersion,
      "com.zaxxer" % "HikariCP" % "6.2.1",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.scalaland" %% "chimney" % "1.7.3",
      "io.getquill" %% "quill-jdbc-zio" % "4.8.6",
      "org.liquibase" % "liquibase-core" % "4.31.1",
      "org.postgresql" % "postgresql" % "42.7.5",
      "com.typesafe" % "config" % "1.4.3",
      "io.github.gaelrenoux" %% "tranzactio" % "4.2.0",
      "dev.zio" %% "zio-test" % zioVersion % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
