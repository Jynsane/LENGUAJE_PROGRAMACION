// ============================================================
//  build.sbt — Configuración del proyecto Scala
//  Sistema Experto de Diagnóstico de Enfermedades
// ============================================================

ThisBuild / version      := "1.0.0"
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization := "edu.proyecto"

lazy val root = (project in file("."))
  .settings(
    name := "diagnostico-enfermedades",

    // Opciones del compilador
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked"
    ),

    // Punto de entrada
    Compile / mainClass := Some("app.Main"),

    // Dependencias (sin Python por ahora)
    libraryDependencies ++= Seq(
      // ScalaTest para pruebas unitarias
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      // JSON ligero para leer/escribir datos de pacientes
      "com.lihaoyi" %% "ujson" % "2.0.0"
    )
  )
