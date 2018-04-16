package services



import java.io.{File, FileWriter}

import javax.inject.Inject
import models.Car
import play.api.Environment
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AnyContent, Request}

import scala.io.Source


class CarsForSaleServiceImpl @Inject()(val env:Environment) extends CarsForSaleService {
  override val path: String = s"""${env.rootPath}/uploads/"""
}

trait CarsForSaleService {
  val env: Environment

  val path: String

  def getCarsForSale = {
    Json.parse(readFromFile).as[List[Car]]
  }
  def getCarsFromFile:List[JsObject] = Json.parse(readFromFile).as[List[JsObject]]

  def writeCarToFile(car: Car)(implicit r: Request[AnyContent]): Car = {
  val carsForSale:List[JsObject] = car.id.fold(getCarsFromFile){
    i => getCarsFromFileAndRemoveCurrent(i) }

    val newCar = car.id.fold(car.copy(id = Some(generateNextUniqueId(carsForSale))))(_ => car)
    val carJson = List(Json.toJson[Car](newCar).as[JsObject])

    val file = new File(s"$path/cars.json")

    val fw = new FileWriter(file)
    fw.write(Car.getJsonArray(carsForSale ++ carJson).toString())
    fw.close()
    newCar
  }

  def addCarForSale(car: Car)(implicit r: Request[AnyContent]) = {
    saveCarPic
    writeCarToFile(car)
  }

  private[services] def saveCarPic(implicit r: Request[AnyContent]) = {
    r.body.asMultipartFormData.map { pic =>
      val f = pic.files.head
      f.ref.moveTo(new File(s"$path/car_1/mainPicture.jpg"))
    }
  }

  private[services] def readFromFile:String = {
    val bufferedSource = Source.fromFile(env.getFile("uploads/cars.json"))
    val fileContents = bufferedSource.getLines.mkString
    bufferedSource.close
    fileContents
  }

  private[services] def getCarsFromFileAndRemoveCurrent(id: Int): List[JsObject] = {
  Json.parse(readFromFile).as[List[JsObject]]
    .filterNot(js => (js \ "id").as[Int] == id)
  }

  private[services] def generateNextUniqueId(list:List[JsObject]):Int = {
     list.map(js => (js \ "id").as[Int]).sortWith(_ > _).headOption.getOrElse(0) + 1
  }
}