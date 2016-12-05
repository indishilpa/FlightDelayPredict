import play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys._
import play.sbt.PlayScala

name := """scala-play-test"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

val jacksonV = "2.6.7"
val `jackson-core` = "com.fasterxml.jackson.core" % "jackson-core" % jacksonV
val `jackson-databind` = "com.fasterxml.jackson.core" % "jackson-databind" % jacksonV
val `jackson-annotations` = "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonV
val `jackson-datatype-jsr310` = "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonV


libraryDependencies ++= Seq(
    `jackson-databind`,
    `jackson-core`,
    `jackson-annotations`,
    `jackson-datatype-jsr310`,		
  "org.apache.spark" %% "spark-core" % "2.0.1",
  "org.apache.spark" %% "spark-sql" % "2.0.1",
  "org.apache.spark" %% "spark-mllib" % "2.0.1",
  "org.apache.spark" %% "spark-streaming" % "2.0.1",
 // "org.apache.spark" %% "spark-streaming-twitter" % sparkVersion,
  ws
)

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)
dependencyOverrides += `jackson-core`
dependencyOverrides += `jackson-databind`
dependencyOverrides += `jackson-annotations`
dependencyOverrides += `jackson-datatype-jsr310`

mappings in Universal ++=
  (baseDirectory.value / "data" * "*" get) map
    (x => x -> ("data/" + x.getName))

fork in run := false