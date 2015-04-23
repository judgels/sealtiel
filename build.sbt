import de.johoop.testngplugin.TestNGPlugin
import de.johoop.jacoco4sbt.JacocoPlugin.jacoco
import sbtbuildinfo.Plugin._

lazy val sealtiel = (project in file("."))
    .enablePlugins(PlayJava, SbtWeb)
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    .dependsOn(playcommons)
    .aggregate(playcommons)
    .settings(
        name := "sealtiel",
        version := IO.read(file("version.properties")).trim,
        scalaVersion := "2.11.1",
        resolvers += "IA TOKI Artifactory" at "http://artifactory.ia-toki.org/artifactory/repo",
        libraryDependencies ++= Seq(
            "org.iatoki.judgels.sealtiel" % "sealtiel-message" % "1.0.4",
            "com.rabbitmq" % "amqp-client" % "3.2.2"
        )
    )
    .settings(TestNGPlugin.testNGSettings: _*)
    .settings(
        aggregate in test := false,
        aggregate in jacoco.cover := false,
        TestNGPlugin.testNGSuites := Seq("test/resources/testng.xml")
    )
    .settings(jacoco.settings: _*)
    .settings(
        parallelExecution in jacoco.Config := false
    )
    .settings(
        LessKeys.compress := true,
        LessKeys.optimization := 3,
        LessKeys.verbose := true
    )
    .settings(
        publishArtifact in (Compile, packageDoc) := false,
        publishArtifact in packageDoc := false,
        sources in (Compile,doc) := Seq.empty
    )
    .settings(buildInfoSettings: _*)
    .settings(
        sourceGenerators in Compile <+= buildInfo,
        buildInfoKeys := Seq[BuildInfoKey](name, version),
        buildInfoPackage := "org.iatoki.judgels.sealtiel"
    )

lazy val playcommons = RootProject(file("../judgels-play-commons"))
