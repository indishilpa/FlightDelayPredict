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
		val conf = new SparkConf().setAppName("Simple Application for testing")
    	val sc = new SparkContext(conf)
		var textRDD = sc.textFile(args(0))
		val header = textRDD.first()
		textRDD = textRDD.filter(row => row != header)      // this removes the header from the input data 
		val flightsRDD = textRDD.map(parseFlight).cache()	
		flightsRDD.first()	
	}
}