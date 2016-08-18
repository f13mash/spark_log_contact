name := """hello-scala"""

version := "1.0"

scalaVersion := "2.11.7"



libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"


// https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.11
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.0.0-preview" force()
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.0-preview" force()
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.0-preview" force()
/*
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.6.0" force()
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.0"  force()
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.0"  force()
libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.6.0"  force()
libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.6.0"  force()
*/
libraryDependencies += "com.databricks" % "spark-csv_2.11" % "1.4.0"
libraryDependencies += "com.google.inject" % "guice" % "3.0"
libraryDependencies += "joda-time" % "joda-time" % "2.9.4"
libraryDependencies += "com.databricks" % "spark-csv_2.11" % "1.4.0"
libraryDependencies += "commons-io" % "commons-io" % "2.5"

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

libraryDependencies += "RedisLabs" % "spark-redis" % "0.3.2"




fork in run := true