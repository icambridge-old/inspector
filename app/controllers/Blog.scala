package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import models.Posts
import entities.Post

object Blog extends Controller {

  val postModel = new Posts

  val postForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "body" -> nonEmptyText,
      "expcert" -> optional(text),
      "slug" -> nonEmptyText,
      "posted" -> optional(text),
      "id" -> optional(number)
    )(Post.apply)(Post.unapply) )

  def index = Action {
          //s
    val posts = postModel.getLatest
    Ok(views.html.blog.index(posts))
  }

  def post(slug: String) = Action {

    val blogPostOption = postModel.getPost(slug)

    blogPostOption match {
      case Some(blogPost) => Ok(views.html.blog.post(blogPost))
      case None           => Ok(views.html.blog.notfound())
    }

  }

  def create = Action {

    Ok(views.html.admin.blog.edit(postForm))

  }

  def edit(slug: String) = Action {

    val blogPostOption = postModel.getPost(slug)

    blogPostOption match {
      case Some(blogPost) => {
        val filled = postForm.fill(blogPost)
        Ok(views.html.admin.blog.edit(filled))
      }
      case None           => Ok(views.html.blog.notfound())
    }

  }

 def save = Action { implicit request =>

  val result = postForm.bindFromRequest.fold(
      {formFail => Ok(views.html.admin.blog.edit(formFail))},
      {post => postModel.save(post);Ok(views.html.blog.post(post)) }
  )

  result
 }

  def formFailure(form: Form[Post]) = {
    println("Failure")
    println(form.hasErrors)

  }

  def formSuccess(post: Post) = {
    Ok(views.html.blog.post(post))
  }
}