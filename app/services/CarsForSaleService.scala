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

    overWriteFile(carsForSale ++ carJson)
    newCar
  }

  def addCarForSale(car: Car)(implicit r: Request[AnyContent]) = {
    val newCar = writeCarToFile(car)
    saveCarPic(newCar.id.get)
  }

  def deleteCar(carId:Int) = {
    val cars = getCarsFromFileAndRemoveCurrent(carId)
    overWriteFile(cars)
  }

  def dropDirectory(carId:Int) = {
    val fPath = s"$path/car_$carId"
    val dir = new File(fPath)
    if(!dir.exists()){
      println("foo")
      dir.delete()
      true
    }
    else {
      false
    }

  }

  private[services] def overWriteFile(list: List[JsObject]):String = {
    val file = new File(s"$path/cars.json")
    val fw = new FileWriter(file)
    val carsAsString = Car.getJsonArray(list).toString()
    fw.write(carsAsString)
    fw.close()
    carsAsString
  }

  private[services] def saveCarPic(id:Int)(implicit r: Request[AnyContent]) = {
    r.body.asMultipartFormData.map { pic =>
      val f = pic.files.head
      val fPath = s"$path/car_$id/"
      checkAndCreateDirectory(fPath)
      f.ref.moveTo(new File(s"$fPath/mainPicture.jpg"))
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

  private def checkAndCreateDirectory(directory:String): Boolean = {
    val dir = new File(directory)
    if(!dir.exists()){
      dir.mkdir()
      true
    }
    else {
      false
    }
  }
}