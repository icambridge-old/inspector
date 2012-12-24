package models

import play.api.Play
import com.twitter.querulous.config._
import com.twitter.conversions.time._


trait Connection {

  val mysqlUsername = Play.current.configuration.getString("mysql.username").getOrElse("")
  val mysqlPassword = Play.current.configuration.getString("mysql.password").getOrElse("")
  val mysqlHostname = Play.current.configuration.getString("mysql.hostname").getOrElse("")
  val mysqlDatabase = Play.current.configuration.getString("mysql.database").getOrElse("")


  val config = new com.twitter.querulous.config.QueryEvaluator {
    database.pool = new ApachePoolingDatabase {
      sizeMin          = 1
      sizeMax          = 1
      maxWait          = 5.seconds
      minEvictableIdle = 60.seconds
      testIdle         = 1.second
      testOnBorrow     = false
    }
  }

  val queryEvaluatorFactory = config()

  val queryEvaluator = queryEvaluatorFactory(mysqlHostname, mysqlDatabase, mysqlUsername, mysqlPassword)


}