package controllers

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.util.MLUtils
import scala.io.Source
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.feature.PCA


import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.Stock
import org.apache.spark.SparkContext._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
  //  Ok
    Ok(views.html.index("Your new application is ready."))
  }

  def getStock = Action {
    val stock = Stock("GOOG","","","","","","")
    Ok(Json.toJson(stock))
  }
  
  def handleForm = Action(parse.tolerantFormUrlEncoded) { implicit request =>
//    println("first")
  val departuredate = request.body.get("departuredate").map(_.head).getOrElse("");
  val departureairport = request.body.get("departureairport").map(_.head).getOrElse("");
  val destinationairport = request.body.get("destinationairport").map(_.head).getOrElse("");
  val departuretime = request.body.get("departuretime").map(_.head).getOrElse("");
  val arrivaltime = request.body.get("arrivaltime").map(_.head).getOrElse("");
  val carrier = request.body.get("carrier").map(_.head).getOrElse("");
  val flightnumber = request.body.get("flightnumber").map(_.head).getOrElse("");
  
  println(departuredate, departureairport,destinationairport,
      departuretime,arrivaltime,carrier,flightnumber)
      
  import play.api.libs.json._
  val daysofweek = """{"Sunday": 7, "Monday": 1,"Tuesday":2,"Wednesday":3,"Thursday":4, "Friday":5, "Saturday":6}"""
  val result = Json.parse(daysofweek)
  
  val dayofweek = departuredate.split(",").map(_.trim); 
  
  /* day of the week parameter
   * parameter : dow
   * */ 
  val dow = result.\(dayofweek(0)).as[Int]
  
  val yearformat = dayofweek(1).split("/").map(_.trim)
  /* date 
   * param : 	dayofmonth 
   * 					monthofyear
   * 					year
   * */
  val dayofmonth = yearformat(0).toInt
  val monthofyear = yearformat(1).toInt
  val year = yearformat(2).toInt
  
  
  val airports = """{
  "JAX": 94,
  "RAP": 105,
  "TVC": 149,
  "KTN": 119,
  "BRW": 191,
  "HNL": 118,
  "STL": 11,
  "STT": 130,
  "SDF": 6,
  "DFW": 29,
  "PNS": 93,
  "CDV": 151,
  "STX": 12,
  "LWB": 204,
  "MHT": 84,
  "LFT": 201,
  "OMA": 51,
  "MIA": 45,
  "LGB": 41,
  "OME": 177,
  "LGA": 82,
  "MYR": 176,
  "IAD": 34,
  "SEA": 7,
  "HOU": 33,
  "IAH": 35,
  "RST": 107,
  "CVG": 27,
  "RSW": 4,
  "SUX": 145,
  "BTR": 160,
  "YAK": 152,
  "ABE": 63,
  "BTV": 68,
  "FLL": 76,
  "HPN": 79,
  "BDL": 66,
  "ABQ": 15,
  "BUF": 69,
  "ONT": 52,
  "SFO": 8,
  "BUR": 21,
  "RDU": 89,
  "SWF": 146,
  "MKE": 46,
  "ISP": 159,
  "LIH": 155,
  "TYS": 144,
  "BET": 197,
  "SGF": 126,
  "LIT": 42,
  "ATL": 17,
  "ICT": 36,
  "ITH": 80,
  "CHA": 200,
  "MLB": 164,
  "HRL": 110,
  "ELM": 74,
  "ELP": 31,
  "BFL": 203,
  "GPT": 161,
  "MLI": 143,
  "FNT": 172,
  "JFK": 38,
  "ADQ": 196,
  "CHS": 189,
  "BWI": 22,
  "PBI": 86,
  "MLU": 95,
  "AUS": 18,
  "CID": 138,
  "SHV": 127,
  "PSC": 98,
  "PSG": 100,
  "BGM": 67,
  "HSV": 165,
  "PSP": 102,
  "BGR": 103,
  "SYR": 13,
  "GRB": 174,
  "AVL": 199,
  "OAK": 49,
  "AVP": 65,
  "DLG": 185,
  "SIT": 129,
  "ORD": 53,
  "DLH": 170,
  "ORF": 85,
  "GRR": 77,
  "BHM": 117,
  "SJC": 58,
  "WRG": 101,
  "TLH": 147,
  "HDN": 141,
  "VPS": 150,
  "SJU": 9,
  "MOB": 96,
  "GSO": 78,
  "BIL": 131,
  "PDX": 54,
  "GSP": 154,
  "GST": 205,
  "RIC": 1,
  "AGS": 111,
  "BIS": 169,
  "FAI": 116,
  "MOT": 166,
  "FAR": 171,
  "BZN": 132,
  "GTF": 134,
  "FAT": 182,
  "CLE": 23,
  "PVD": 87,
  "FAY": 192,
  "LNK": 142,
  "CLT": 24,
  "SLC": 10,
  "OTZ": 178,
  "GUC": 183,
  "FSD": 140,
  "CMH": 25,
  "FCA": 133,
  "PWM": 88,
  "GEG": 97,
  "SMF": 59,
  "TOL": 92,
  "MAF": 157,
  "AZO": 168,
  "ERI": 75,
  "TPA": 14,
  "SNA": 60,
  "GFK": 173,
  "PHL": 2,
  "MRY": 124,
  "KOA": 122,
  "MBS": 158,
  "PHX": 16,
  "AKN": 184,
  "DAB": 190,
  "COS": 26,
  "MSO": 136,
  "MSN": 167,
  "PIA": 202,
  "MSP": 47,
  "DAL": 109,
  "OGG": 125,
  "MCI": 43,
  "MSY": 48,
  "BNA": 19,
  "ALB": 64,
  "MCO": 44,
  "LAN": 187,
  "PIT": 55,
  "DAY": 72,
  "LAS": 39,
  "DRO": 181,
  "LAX": 40,
  "MTJ": 175,
  "TRI": 148,
  "ILM": 195,
  "LBB": 153,
  "JNU": 99,
  "FWA": 186,
  "RNO": 56,
  "AMA": 180,
  "EUG": 121,
  "MDT": 83,
  "BOI": 106,
  "DSM": 139,
  "LSE": 156,
  "MDW": 104,
  "DCA": 73,
  "ROA": 90,
  "CAE": 198,
  "ROC": 3,
  "BOS": 20,
  "CAK": 70,
  "MEM": 108,
  "ANC": 114,
  "IND": 37,
  "CRP": 193,
  "SAN": 5,
  "DTW": 30,
  "SAT": 57,
  "CRW": 71,
  "SAV": 112,
  "HLN": 135,
  "MFE": 162,
  "SBA": 113,
  "SRQ": 137,
  "MFR": 123,
  "SBN": 91,
  "EWR": 32,
  "EGE": 120,
  "DUT": 188,
  "OKC": 50,
  "TUL": 61,
  "DEN": 28,
  "JAC": 179,
  "SCC": 115,
  "MGM": 163,
  "TUS": 62,
  "JAN": 128,
  "LEX": 81,
  "BRO": 194
}"""
  
  val airport = Json.parse(airports)
  /*airports 
   * params :departAir
   * 				 arrvAir 
   * 
   * */
  val departAir = airport.\(departureairport).as[Int]
  val arrvAir = airport.\(destinationairport).as[Int]
 
  /* time  params :
   * 								departime
   * 								arrivtime
   * 
   * */
  val toRemove = ":".toSet
  
  val departime = departuretime.filterNot(toRemove).toInt
  val arrivtime = arrivaltime.filterNot(toRemove).toInt
  
  
  val carriers = """{
  "AA": 3,
  "AS": 10,
  "TW": 5,
  "WN": 7,
  "DL": 4,
  "HP": 6,
  "NW": 9,
  "UA": 2,
  "CO": 8,
  "US": 1
}"""
  
  val carr = Json.parse(carriers)
  /*
   * carrier mappings 
   * parm : carriero
   * */
  val carriero = carr.\(carrier).as[Int]
  
  val flightnu = flightnumber.toInt
  
  val input = year+" "+monthofyear+" "+dayofmonth+" "+dow+" "+departime+" "+
  " "+arrivtime+" "+carriero+" "+flightnu+" "+departAir+" "+arrvAir
  
  println(input)
  
//   var z:Array[String] = new Array[String](1)
//   z(0) = input 
//   results.server.main(z)
  
    val conf = new SparkConf().setAppName("server").setMaster("local")
   //  val sc = new SparkContext("local", "Application", "/path/to/spark-0.9.0-incubating", List("target/scala-2.11/playsparkapp_2.11-1.0-SNAPSHOT.jar"))

    val sc = new SparkContext(conf)
    
    val NBModel = NaiveBayesModel.load(sc, "public/models/classify/NB")
    val SVMmodel = SVMModel.load(sc, "public/models/classify/SVM")
    val RFModel = RandomForestModel.load(sc, "public/models/magnitude/RF")
    val LRModel = LogisticRegressionModel.load(sc, "public/models/magnitude/LR")
  
    val dv: Vector = Vectors.dense(input.split("\\s+").map(_.toDouble))
    
    var NBres = NBModel.predict(dv)
    var SVMres = SVMmodel.predict(dv)
    var RFres = RFModel.predict(dv)
    var LRres = LRModel.predict(dv)
    
    println(dv)
    println("NB: "+NBres)
    println("SVM: "+SVMres)
    println("RF: "+RFres)
    println("LR: "+LRres)
    sc.stop()    
    
    var resulto : String = " "
    val rf = (RFres * 5).toString()
    if( RFres * 5 >= 15)
      resulto =  "your flight is delayed by" + rf 
    else
      resulto = "your flight is not delayed"  
      
       /*Ok("Submitted Successfully Our Predictor says your flight:"+ resulto )*/
    
        Ok(views.html.showConfirmation(resulto))
 
  
}

 
}
