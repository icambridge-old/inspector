package entities

case class Post(title: String, body: String, excerpt: Option[String], slug: String, posted: Option[String], id: Option[Int])