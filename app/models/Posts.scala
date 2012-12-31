package models


import entities.Post

import java.text.SimpleDateFormat

class Posts extends Connection {

  protected[this] var pageCount = 1

  def getPageCount = pageCount


  def getLatest(pageNumber: Int): Seq[Post] = {

    // TODO change to application configuration
    val limitCount = 5
    val startingPoint = limitCount * (pageNumber - 1)

    val posts: Seq[Post] = queryEvaluator.select(
      "SELECT SQL_CALC_FOUND_ROWS * FROM posts ORDER BY `id` DESC LIMIT " + startingPoint + "," + limitCount) {
      row =>

        val format = new SimpleDateFormat("dd-MM-yyyy")
        val date = row.getTimestamp("posted")
        val formatedDate = format.format(date)

        new Post(
          title = row.getString("title"),
          body = row.getString("body"),
          excerpt = Some(row.getString("excerpt")),
          posted = Some(formatedDate),
          slug = row.getString("slug"),
          id = Some(row.getInt("id"))
        )
    }

    val pageCountOption = queryEvaluator.selectOne("SELECT FOUND_ROWS()") {
      row =>
        row.getInt(1)
    }

    val postCount = pageCountOption.getOrElse(1)
    pageCount = if ((postCount % limitCount) == 0) {
      postCount / limitCount
    } else {
      (postCount / limitCount) + 1
    }

    posts
  }


  def getPost(slug: String) = {
    val post = queryEvaluator.selectOne("SELECT * FROM posts WHERE slug = ? ORDER BY `id` DESC", slug) {
      row =>

        val format = new SimpleDateFormat("dd-MM-yyyy")
        val date = row.getTimestamp("posted")
        val formatedDate = format.format(date)

        new Post(
          title = row.getString("title"),
          body = row.getString("body"),
          excerpt = Some(row.getString("excerpt")),
          posted = Some(formatedDate),
          slug = row.getString("slug"),
          id = Some(row.getInt("id"))
        )
    }


    post
  }

  def getPostById(id: Int) = {
    val post = queryEvaluator.selectOne("SELECT * FROM posts WHERE id = ? ORDER BY `id` DESC", id) {
      row =>

        val format = new SimpleDateFormat("dd-MM-yyyy")
        val date = row.getTimestamp("posted")
        val formatedDate = format.format(date)

        new Post(
          title = row.getString("title"),
          body = row.getString("body"),
          excerpt = Some(row.getString("excerpt")),
          posted = Some(formatedDate),
          slug = row.getString("slug"),
          id = Some(row.getInt("id"))
        )
    }


    post
  }

  def save(post: Post) = {

    val regex = "[^A-Za-z0-9\\s-]".r
    val body = post.body

    val excerpt = post.excerpt match {
      case Some(excerpt) => excerpt
      case None => {
        val parts = body.split("\n")
        parts(0)
      }
    }
    val slug = regex.replaceAllIn(post.title, "-").toLowerCase
    val title = post.title

    post.id match {
      case Some(id) => update(title, body, excerpt, slug, id)
      case None => create(title, body, excerpt, slug)
    }


  }

  def update(title: String, body: String, excerpt: String, slug: String, id: Int) = {

    queryEvaluator.execute("UPDATE posts SET title=?, body=?, excerpt=?, slug=? WHERE id = ?",
      title, body, excerpt, slug, id)

  }

  def create(title: String, body: String, excerpt: String, slug: String) = {

    queryEvaluator.execute("INSERT INTO posts (title, body, excerpt, slug) VALUES (?, ?, ?,  ?)",
      title, body, excerpt, slug)
  }

}