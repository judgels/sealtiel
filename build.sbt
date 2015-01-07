name := """sealtiel"""

version := "1.0-SNAPSHOT"

lazy val main = (project in file("."))
                  .enablePlugins(PlayJava)
                  .disablePlugins(plugins.JUnitXmlReportPlugin)
                  .dependsOn(commons)

lazy val commons = RootProject(file("../judgels-play-commons"))

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  cache,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.7.Final",
//  "org.hibernate" % "hibernate-jpamodelgen" % "4.3.7.Final",
  "org.iatoki.judgels.sealtiel" % "sealtielMessage" % "1.0.0",
  "commons-io" % "commons-io" % "2.4",
  "com.rabbitmq" % "amqp-client" % "3.2.2",
  "com.google.code.gson" % "gson" % "2.2.4",
  "mysql" % "mysql-connector-java" % "5.1.26"
)

resolvers += "IA TOKI Artifactory" at "http://artifactory.ia-toki.org/artifactory/repo"
