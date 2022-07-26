
scalaVersion := "2.11.8"

libraryDependencies ++= Seq( 
  guice
  , "org.joda" % "joda-convert" % "1.9.2"
  , "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  , "org.apache.spark" %% "spark-core" % "2.2.0"
  , "org.apache.spark" %% "spark-sql" % "2.2.0"
  , "org.apache.spark" %% "spark-mllib" % "2.2.0"
  , "org.apache.spark" %% "spark-hive" % "2.2.0"
  , "org.apache.hadoop" % "hadoop-client" % "2.7.2"
  , "com.github.nscala-time" %% "nscala-time" % "2.30.0"
  , "org.webjars" % "requirejs" % "2.1.22"
  , "org.webjars" % "jquery" % "2.1.4"
  , "org.webjars" % "underscorejs" % "1.8.3"
  , "org.webjars" % "nvd3" % "1.8.1"
  , "org.webjars" % "d3js" % "3.5.6"
  , "org.webjars" % "bootstrap" % "3.3.6"
)

// Stuff
dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5"
  , "com.google.guava" % "guava" % "19.0"
)

pipelineStages := Seq(rjs)

// The Play project itself
lazy val root = (project in file("."))
  .enablePlugins(Common, PlayScala, SbtWeb)
  .settings(
    name := """Play-with-Spark""",
  )
