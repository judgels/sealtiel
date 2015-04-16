import de.johoop.testngplugin.TestNGPlugin
import de.johoop.jacoco4sbt.JacocoPlugin.jacoco

lazy val sealtiel = (project in file("."))
    .enablePlugins(PlayJava, SbtWeb)
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    .dependsOn(playcommons)
    .aggregate(playcommons)
    .settings(
        name := "sealtiel",
        version := "0.2.1",
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

lazy val playcommons = RootProject(file("../judgels-play-commons"))
