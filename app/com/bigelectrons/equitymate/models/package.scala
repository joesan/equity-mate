package com.bigelectrons.equitymate

package object models {

  case class Percent(dbl: Double) {

    def round: Double =
      BigDecimal(dbl).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

  }

  sealed trait Industry
  case object Consumer extends Industry
  case object ITAndTechnology extends Industry
  case object Telecommunications extends Industry
  case object EnergyAndUtilities extends Industry
  case object OilAndGas extends Industry
  case object Banking extends Industry
  case object Insurance extends Industry


  case class Symbol(ticker: String, name: String, description: String, industry: Industry)
}
