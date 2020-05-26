package com.bigelectrons.equitymate.services

import com.bigelectrons.equitymate.models.FairPriceRequest

/**
  * Used for calculating metrics around a given ticker
  */
class EquityMetricsService {

  def fairPrice(fairPriceRequest: FairPriceRequest) = {
    val avgPERatio = (fairPriceRequest.peRatioHigh + fairPriceRequest.peRatioLow) / 2
  }

  def peRatio = {

  }

  def dividendYield = {

  }

  def growthRate = {

  }
}
