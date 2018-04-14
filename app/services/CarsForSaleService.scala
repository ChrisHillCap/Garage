package services

import javax.inject.Inject

class CarsForSaleServiceImpl @Inject()() extends CarsForSaleService {
  
}

trait CarsForSaleService {

  def getCarsForSale = ???

  def addCarForSale = ???

  def removeCarForSale = ???

}
