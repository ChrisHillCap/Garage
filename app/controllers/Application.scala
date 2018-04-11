package controllers

import play.api.mvc._

import scala.concurrent.Future

class Application extends Controller {

  def index: Action[AnyContent] = Action.async {
    Future.successful(Ok(views.html.index("Your new application is ready.")))
  }

  def aboutUs: Action[AnyContent] = Action.async {
    Future.successful(Ok(views.html.aboutus()))
  }
}