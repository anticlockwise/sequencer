name := "sequencer"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.13"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.11"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.11"
