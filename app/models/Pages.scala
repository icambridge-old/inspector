package models


import entities.Page

class Pages extends Connection {

  def getPage(pageSlug: String) = {
    val page = queryEvaluator.selectOne("SELECT * FROM `pages` WHERE slug = ?", pageSlug) { row =>
      new Page(
        title = row.getString("title"),
        body = row.getString("body"),
        slug = row.getString("slug"),
        id = Some(row.getInt("id"))
      )
    }
    page
  }

  def getAll = {
    val pages = queryEvaluator.select("SELECT * FROM `pages` ORDER BY `id` DESC") { row =>
      new Page(
        title = row.getString("title"),
        body = row.getString("body"),
        slug = row.getString("slug"),
        id = Some(row.getInt("id"))
      )
    }
    pages
  }

  def save(page: Page) = {

    val regex = "[^A-Za-z0-9\\s-]".r
    val body = page.body
    val slug = regex.replaceAllIn(page.title, "-").toLowerCase
    val title = page.title

    page.id match {
      case Some(id) => update(title, body, slug, id)
      case None => create(title, body, slug)
    }

  }

  def update(title: String, body: String, slug: String, id: Int) = {
    queryEvaluator.execute("UPDATE `pages` SET title=?, body=?, slug=? WHERE id = ?",
      title, body, slug, id)

  }

  def create(title: String, body: String, slug: String) = {

    queryEvaluator.execute("INSERT INTO `pages` (title, body, slug) VALUES (?, ?,  ?)",
      title, body,  slug)
  }

}