import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf


case class Flight(dofM: String, dofW: String, uniqueCarrier: String, tailnum: String, flnum: Int,  origin: String, dest: String, crsdeptime: Double, 
		deptime: Double, depdelaymins: Double, crsarrtime: Double, arrtime: Double, arrdelay: Double, crselapsedtime: Double, dist: Int)


object SimpleApp {
	
	def toInt(str: String): Int = {
		if(str.equalsIgnoreCase("NA") || str.equalsIgnoreCase("N/A")){
			return 0
		}
		return str.toInt
	}

	def toDouble(str: String): Double = {
		if(str.equalsIgnoreCase("NA") || str.equalsIgnoreCase("N/A")){
			return 0.0
		}
		return str.toDouble
	}

	def parseFlight(str: String): Flight = {
	  val zero = 0;	
	  val line = str.split(",")
	  Flight(line(2), line(3), line(8), line(10), toInt(line(9)), line(16), line(17), line(5).toDouble, 
	  	toDouble(line(4)), toDouble(line(15)), toDouble(line(7)), toDouble(line(6)), toDouble(line(14)), toDouble(line(12)), toInt(line(18)))
	}

	def main(args: Array[String]) {
		val conf = new SparkConf().setAppName("Simple Application for testing").setMaster("local[*]")
    	val sc = new SparkContext(conf)
		var textRDD = sc.textFile(args(0))
		val header = textRDD.first()
		textRDD = textRDD.filter(row => row != header)      // this removes the header from the input data 
		val flightsRDD = textRDD.map(parseFlight).cache()
		println(flightsRDD.first())

		// converting carrier code to nums
		var carrierMap : Map[String, Int] = Map()
		var index: Int = 0
		flightsRDD.map(flight => flight.uniqueCarrier).distinct.collect.foreach(
			x => {
				carrierMap += (x ->index);
				index += 1
				})
		println(carrierMap.toString)

		// converting destination codes to nums
		var destMap : Map[String, Int] = Map()
		var index1: Int = 0
		flightsRDD.map(flight => flight.dest).distinct.collect.foreach(
			x => {
				destMap += (x ->index1);
				index1 += 1
				})
		println(destMap.toString)

		//converting origins to nums
		var originMap : Map[String, Int] = Map()
		var index2: Int = 0
		flightsRDD.map(flight => flight.origin).distinct.collect.foreach(
			x => {
				originMap += (x ->index2);
				index2 += 1
				})
		println(originMap.toString)


		var mlprep = flightsRDD.map(flight => {
			val delayed = if(flight.depdelaymins.toDouble > 40) 1.0 else 0.0
			val monthday = flight.dofM.toInt - 1
			val weekday = flight.dofW.toInt - 1
			val carrier = carrierMap(flight.uniqueCarrier)
			val origin = originMap(flight.origin)
			val dest = destMap(flight.dest)

			Array(delayed.toDouble, monthday.toDouble, weekday.toDouble, flight.crsdeptime, flight.crsarrtime, carrier.toDouble, flight.crselapsedtime, origin.toDouble, dest.toDouble)
			})
		println(mlprep.take(1).deep.mkString("\n"))

		val mldata = mlprep.map(
			x => LabeledPoint(x(0), Vectors.dense(x(1), x(2), x(3), x(4), x(5), x(6), x(7), x(8))
			))
		println(mldata.take(1).deep.mkString("\n"))

		sc.stop
	}
}