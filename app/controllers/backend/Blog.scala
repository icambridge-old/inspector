package controllers.admin

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

  def list = Action {
    request =>
      request.session.get("username").map {
        user =>
          val posts = postModel.getLatest
          Ok(views.html.admin.blog.list(posts, user))
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def create = Action {
    request =>
      request.session.get("username").map {
        user =>
          Ok(views.html.admin.blog.edit(postForm, user))
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def edit(slug: String) = Action {
    request =>
      request.session.get("username").map {
        user =>

          val blogPostOption = postModel.getPost(slug)

          blogPostOption match {
            case Some(blogPost) => {
              val filled = postForm.fill(blogPost)
              Ok(views.html.admin.blog.edit(filled, user))
            }
            case None => Ok(views.html.blog.notfound())
          }

      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def save = Action {
    implicit request =>

      request.session.get("username").map {
        user =>

          val result = postForm.bindFromRequest.fold(
          {
            formFail => Ok(views.html.admin.blog.edit(formFail, user))
          }, {
            post => postModel.save(post); Ok(views.html.blog.post(post))
          }
          )

          result

      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }


}