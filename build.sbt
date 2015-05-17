name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.amazonaws" % "aws-java-sdk" % "1.9.17",
  "commons-validator" % "commons-validator" % "1.4.0",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  javaEbean
)

javaOptions ++= Seq("-Xmx256M", "-Xmx512M", "-XX:MaxPermSize=512M")


fork in run := false