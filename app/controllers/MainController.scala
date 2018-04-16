package controllers

import java.io.File

import forms.CarForSaleForm
import javax.inject.Inject
import play.api.Environment
import play.api.i18n.MessagesApi
import play.api.mvc._
import services.CarsForSaleService

import scala.concurrent.Future





class MainControllerImpl @Inject()(val messagesApi: MessagesApi,
                                   val env: Environment,
                                   val carsForSaleService: CarsForSaleService) extends MainController

  trait MainController extends Controller with play.api.i18n.I18nSupport {

    val env:Environment
    val carsForSaleService:CarsForSaleService

  def index: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.index("Your new application is ready.")))
  }

  def aboutUs: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.aboutus()))
  }

  def showCarsForSale: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.carsForSale(carsForSaleService.getCarsForSale)))
  }

  //admin
  def showNewCarForSale: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.newcarforsale(CarForSaleForm.form)))
  }
    def submitNewCarForSale: Action[AnyContent] = Action.async { implicit request =>
      CarForSaleForm.form.bindFromRequest().fold(
        errors => Future.successful(BadRequest(views.html.newcarforsale(errors))),
        success => { carsForSaleService.addCarForSale(success)
          Future.successful(Ok(views.html.index("foo")))
        })
    }
  }