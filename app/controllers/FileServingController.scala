package controllers

import java.io.File

import javax.activation.MimeType
import javax.inject.Inject
import play.api.Environment
import play.api.mvc.{Action, Controller}

class FileServingControllerImpl @Inject()(val env:Environment) extends FileServingController {
  override val path: String = """/uploads/"""
}
trait FileServingController extends Controller {

  val path: String
  val env:Environment

  def returnFile(fPath:String) = Action {
    val file = env.getFile(s"$path/$fPath")
      Ok.sendFile(file)
  }
}