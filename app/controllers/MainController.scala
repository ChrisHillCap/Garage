package controllers

import java.io.File

import forms.CarForSaleForm
import javax.inject.Inject
import play.api.i18n.MessagesApi
import play.api.mvc._

import scala.concurrent.Future





class MainControllerImpl @Inject()(val messagesApi: MessagesApi) extends MainController {

}
  trait MainController extends Controller with play.api.i18n.I18nSupport {

  def index: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.index("Your new application is ready.")))
  }

  def aboutUs: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.aboutus()))
  }

  def showCarsForSale: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.carsForSale()))
  }

  //admin
  def showNewCarForSale: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.newcarforsale(CarForSaleForm.form)))
  }
    def submitNewCarForSale: Action[AnyContent] = Action.async { implicit request =>
      CarForSaleForm.form.bindFromRequest().fold(
        errors => Future.successful(BadRequest(views.html.newcarforsale(errors))),
        success => { request.body.asMultipartFormData.map { pic =>
          println("foo foo foo " + pic.files.head)
          val f = pic.files.head
          f.ref.moveTo(new File("/images/car_1"))
        }
          Future.successful(Ok(views.html.index("foo")))
        })
    }
// class fileCreator @Inject() (applicationLifecycle: ApplicationLifecycle,
//                             env: Environment)  extends TemporaryFileCreator {
//
//  override def create(prefix: String, suffix: String): File = {
//    Files.createTempFile(Paths.get(s"${env.rootPath.getAbsolutePath}/public/images"), prefix, suffix).toFile
//  }
}