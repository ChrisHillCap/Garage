package services



import java.io.{File, FileWriter}

import javax.inject.Inject
import models.Car
import play.api.Environment
import play.api.libs.json.{JsArray, JsObject, Json}
import play.api.mvc.{AnyContent, Request}

import scala.io.Source


class CarsForSaleServiceImpl @Inject()(val env:Environment) extends CarsForSaleService

trait CarsForSaleService {
  val env: Environment

  val path: String = s"""${env.rootPath}/uploads/"""

  def getCarsForSale = {
    Json.parse(readFromFile).as[List[Car]]
  }

  def writeCarToFile(car: Car)(implicit r: Request[AnyContent]): JsObject = {
    val carJson = Json.toJson[Car](car).as[JsObject]
    val carsForSale = getCarsFromFileAndRemoveCurrent(car.id) :: carJson :: Nil
    val file = new File(s"${env.rootPath}/uploads/car.json")

    val fw = new FileWriter(file)
    fw.write(carsForSale.toString())
    fw.close()
    carJson
  }

  private def saveCarPic(implicit r: Request[AnyContent]) = {
    r.body.asMultipartFormData.map { pic =>
      val f = pic.files.head
      f.ref.moveTo(new File(s"${env.rootPath}/uploads/car_1/mainPicture.jpg"))
    }
  }

  def addCarForSale(car: Car)(implicit r: Request[AnyContent]) = {
    saveCarPic
    writeCarToFile(car)
  }

  def removeCarForSale: List[JsObject] = ???

  private def readFromFile:String = {
    val bufferedSource = Source.fromFile(env.getFile(path + "car.json"))
    val fileContents = bufferedSource.getLines.mkString
    bufferedSource.close
    fileContents
  }

  def getCarsFromFileAndRemoveCurrent(id: Int): List[JsObject] = {

    Json.parse(readFromFile).as[List[JsObject]]
      .filterNot(js => (js \ "id").as[Int] == id)
  }
}