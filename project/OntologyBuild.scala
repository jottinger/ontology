import sbt._
import sbt.Keys._

object OntologyBuild extends Build {
  lazy val ontology= Project(
    id = "ontology",
    base = file("."),
    settings = Seq(
      libraryDependencies ++= Dependencies.specs2
        ++ Dependencies.owlApi
    )
  )
}