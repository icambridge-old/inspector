package models


import entities.Post

class Posts extends Connection {

  def getLatest: Seq[Post] = {

    val posts: Seq[Post] = queryEvaluator.select("SELECT * FROM posts ORDER BY `id` DESC") { row =>
      new Post(
        title = row.getString("title"),
        body = row.getString("body"),
        excerpt = Some(row.getString("excerpt")),
        posted = Some(row.getString("posted")),
        slug = row.getString("slug"),
        id = Some(row.getInt("id"))
      )
    }
    posts
  }


  def getPost(slug: String) = {
    val post = queryEvaluator.selectOne("SELECT * FROM posts WHERE slug = ? ORDER BY `id` DESC", slug) { row =>
      new Post(
        title = row.getString("title"),
        body = row.getString("body"),
        excerpt = Some(row.getString("excerpt")),
        posted = Some(row.getString("posted")),
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