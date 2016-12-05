package models

case class Stock(departuredate: String, departureairport: String,destinationairport: String,
    departuretime : String, arrivaltime: String, carrier : String, flightnumber: String)

object Stock {
    import play.api.libs.json._
    implicit object StockFormat extends Format[Stock] {

        // convert from JSON string to a Stock object (de-serializing from JSON)
        def reads(json: JsValue): JsResult[Stock] = {
//            val symbol = (json \ "symbol").as[String]
//            val price = (json \ "price").as[Double]
            val departuredate = (json \"departuredate").as[String]
            val departureairport = (json \"departureairport").as[String]
            val destinationairport = (json \"destinationairport").as[String]
            val departuretime = (json \"departuretime").as[String]
            val arrivaltime = (json \"arrivaltime").as[String]
            val carrier = (json \"carrier").as[String]
            val flightnumber = (json \"flightnumber").as[String]
            
            JsSuccess(Stock(departuredate, departureairport,destinationairport,departuretime,arrivaltime,carrier,flightnumber))
        }

        // convert from Stock object to JSON (serializing to JSON)
        def writes(s: Stock): JsValue = {
            // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
            val stockAsList = Seq("departuredate" -> JsString(s.departuredate),
                                  "departureairport" -> JsString(s.departureairport))
            JsObject(stockAsList)
        }
    }
}
