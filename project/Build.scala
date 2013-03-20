import sbt._
import Keys._

object build extends Build{

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.msgpack",
    version := "0.1.0",
    scalaVersion := "2.10.0"
    //scalaOrganization := "org.scala-lang.macro-paradise",
    //resolvers += Resolver.sonatypeRepo("snapshots")
  )

  def runMacroTest = Command.command("run")( state => {
    val subState = Command.process("project msgpack-scala-macro-test",state)
    Command.process("run",subState)
    state
  })

  lazy val root = Project(
    "root",
    file("."),
    settings = buildSettings ++ Seq(
      commands ++= Seq(runMacroTest)
    )
  ) aggregate(msgpackScala10)

  lazy val msgpackScala10 = Project(
    "msgpack-scala10",
    file("msgpack-scala10"),
    settings = buildSettings ++ Seq(
      libraryDependencies <++= scalaVersion{ v =>
        Seq(
          "org.scala-lang" % "scala-reflect" % v
        )
      }
    )
  )
  lazy val macroTest = Project(
    "msgpack-scala-macro-test",
    file("macro-test"),
    settings = buildSettings ++ Seq(
      libraryDependencies <++= scalaVersion{ v =>
        Seq(
        )
      }
    )
  ) dependsOn(msgpackScala10)
  
}