name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.amazonaws" % "aws-java-sdk" % "1.9.17",
  javaEbean
)

javaOptions ++= Seq("-Xmx256M", "-Xmx512M", "-XX:MaxPermSize=512M")


fork in run := false