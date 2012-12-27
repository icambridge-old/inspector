import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "inspector"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,

      "mysql" % "mysql-connector-java" % "5.1.18",
      "com.twitter" %% "querulous-core" % "2.7.0",
      "ua.t3hnar.bcrypt" % "scala-bcrypt" % "1.4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "Scala tools releases" at "https://oss.sonatype.org/content/groups/scala-tools/",
      resolvers += "Twitter's cool stuff" at "http://maven.twttr.com/",
      resolvers += "The New Motion Repository" at "http://nexus.thenewmotion.com/content/repositories/releases-public"

    )


}
