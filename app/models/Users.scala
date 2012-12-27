package models

import ua.t3hnar.bcrypt._
import entities.User

class Users extends Connection {

  def authenticate(username: String, password: String) = {

    val user = queryEvaluator.selectOne("SELECT * FROM `users` WHERE username = ?", username) { row =>
      new User(row.getString("username"), row.getString("password"))
    }

    val authenticated = user match {
      case Some(user) => password.isBcrypted(user.password)
      case None => false
    }

    authenticated
  }

  def save(user: User) = {

    val passwordHash = user.password.bcrypt
    queryEvaluator.execute("INSERT INTO `users` (username, password) VALUES (?,?)", user.username, passwordHash)

  }

}