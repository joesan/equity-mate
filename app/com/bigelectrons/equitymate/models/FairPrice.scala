package com.bigelectrons.equitymate.models

import java.time.Year

case class FairPriceRequest(
  ticker: Symbol,
  earningsPerShareTTM: Double,
  growthRate: Percent,
  minimumRoR: Percent,
  marginOfSafety: Percent,
  peRatioLow: Double,
  peRatioHigh: Double,
  timeFrameInYearsFromNow: Int
)

case class FairPriceResponse(
  ticker: Symbol,
  fairPrice: Map[Year, Double]
)