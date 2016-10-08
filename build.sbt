name := "scala-spark"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.3.0" ,
  "org.apache.spark" %% "spark-sql" % "1.3.0" ,
  "org.apache.spark" %% "spark-mllib" % "1.3.0" ,
  "org.apache.spark" %% "spark-streaming" % "1.3.0" 
)
