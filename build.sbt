name := """sealtiel"""

version := "0.1.0"

lazy val sealtiel = (project in file("."))
                  .enablePlugins(PlayJava)
                  .disablePlugins(plugins.JUnitXmlReportPlugin)
                  .dependsOn(playcommons)
                  .aggregate(playcommons)

lazy val playcommons = RootProject(file("../judgels-play-commons"))

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  cache,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.7.Final",
//  "org.hibernate" % "hibernate-jpamodelgen" % "4.3.7.Final",
  "org.iatoki.judgels.sealtiel" % "sealtiel-message" % "1.0.4",
  "commons-io" % "commons-io" % "2.4",
  "com.rabbitmq" % "amqp-client" % "3.2.2",
  "com.google.code.gson" % "gson" % "2.2.4",
  "mysql" % "mysql-connector-java" % "5.1.26"
)

resolvers += "IA TOKI Artifactory" at "http://artifactory.ia-toki.org/artifactory/repo"
