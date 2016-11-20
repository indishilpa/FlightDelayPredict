import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

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
		//setMaster("local[*]").set("spark.executor.memory", "4g")
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

		// 0 == not delayed , 1 == delayed
		val mldata = mlprep.map(
			x => LabeledPoint(x(0), Vectors.dense(x(1), x(2), x(3), x(4), x(5), x(6), x(7), x(8))
			))
		println(mldata.take(1).deep.mkString("\n"))
		
		val mldata0 = mldata.filter(x => x.label == 0).randomSplit(Array(0.85, 0.15))(1)				// mldata0 is %85 not delayed flights
		val mldata1 = mldata.filter(x => x.label != 0)							// mldata1 is %100 delayed flights
		val mldata2 = mldata0 ++ mldata1										// mldata2 is delayed and not delayed

		val splits = mldata2.randomSplit(Array(0.8, 0.2))
		val trainingRDD = splits(0).cache()
		val testingRDD = splits(1).cache()

		println(testingRDD.take(1).deep.mkString("\n"))

		// Build the SVM model
		val svmAlg = new SVMWithSGD()
		svmAlg.optimizer.setNumIterations(100)
		                .setRegParam(1.0)
		                .setStepSize(1.0)
		val model_svm = svmAlg.run(trainingRDD)

		// Predict
		val labelsAndScores = testingRDD.map { point =>
		        val pred = model_svm.predict(point.features)
		        ( point.label, pred)
		}
		val metrics = new BinaryClassificationMetrics(labelsAndScores)
	

		// Precision by threshold
		val precision = metrics.precisionByThreshold
		precision.foreach { case (t, p) =>
		  println(s"Threshold: $t, Precision: $p")
		}

		// Recall by threshold
		val recall = metrics.recallByThreshold
		recall.foreach { case (t, r) =>
		  println(s"Threshold: $t, Recall: $r")
		}

		// Precision-Recall Curve
		val PRC = metrics.pr

		// F-measure
		val f1Score = metrics.fMeasureByThreshold
		f1Score.foreach { case (t, f) =>
		  println(s"Threshold: $t, F-score: $f, Beta = 1")
		}

		val beta = 0.5
		val fScore = metrics.fMeasureByThreshold(beta)
		f1Score.foreach { case (t, f) =>
		  println(s"Threshold: $t, F-score: $f, Beta = 0.5")
		}

		// AUPRC
		val auPRC = metrics.areaUnderPR
		println("Area under precision-recall curve = " + auPRC)

		// Compute thresholds used in ROC and PR curves
		val thresholds = precision.map(_._1)

		// ROC Curve
		val roc = metrics.roc

		// AUROC
		val auROC = metrics.areaUnderROC
		println("Area under ROC = " + auROC)

		sc.stop
	}
}