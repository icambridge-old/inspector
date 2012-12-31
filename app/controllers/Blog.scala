package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import models.Posts
import entities.Post
import util.Location

object Blog extends Controller {

  val postModel = new Posts


  val postForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "body" -> nonEmptyText,
      "expcert" -> optional(text),
      "slug" -> nonEmptyText, // TODO make optional
      "posted" -> optional(text),
      "id" -> optional(number)
    )(Post.apply)(Post.unapply))

  Location.set("Blog")

  def index = page("1")


  def page(pageNumStr: String) = Action { request =>
    val pageNum: Int = pageNumStr.toInt
    val posts = postModel.getLatest(pageNum)
    val postCount = postModel.getPageCount
    Ok(views.html.blog.index(posts, pageNum,postCount))
  }

  def post(slug: String) = Action {

    val blogPostOption = postModel.getPost(slug)

    blogPostOption match {
      case Some(blogPost) => Ok(views.html.blog.post(blogPost))
      case None => Ok(views.html.blog.notfound())
    }

  }

}