name := "i18n-util"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.kongming.elemental" %% "elemental-i18n" % "2.8.0-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)