import sbt._

object Dependencies {
  lazy val specs2Version = "2.3.11"
  lazy val specs2 = Seq(
    "org.specs2" %% "specs2-core" % specs2Version % "test",
    "org.specs2" %% "specs2-matcher-extra" % specs2Version % "test",
    "org.specs2" %% "specs2-junit" % specs2Version % "test")
  lazy val owlApiVersion="3.5.0"
  lazy val owlApi= Seq("net.sourceforge.owlapi" % "owlapi-distribution" % owlApiVersion)
}