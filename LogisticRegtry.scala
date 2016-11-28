package Magnitude;

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.util.MLUtils
import scala.io.Source
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.feature.PCA

object LogisticRegtry {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("LogisticRegtry").setMaster("local")
    val sc = new SparkContext(conf)
    val df = sc.textFile(args(0))
    val parsedData = df.map { line =>
      val parts = line.toString().split(",")
      val label = parts(0).toString().split(' ')
      LabeledPoint(label(3).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }.cache()
    val Array(train, testing) = parsedData.randomSplit(Array(0.7, 0.3))
    
    val pca = new PCA(10).fit(parsedData.map(_.features))
    val training = train.map(p => p.copy(features = pca.transform(p.features)))
    val test = testing.map(p => p.copy(features = pca.transform(p.features))) 
    
    val model = new LogisticRegressionWithLBFGS().setNumClasses(14).run(training)
    val res = test.map(p => (model.predict(p.features), p.label))
    
    val testMSE = res.map{case(v, p) => math.pow((v - p), 2)}.mean()
    println("Test Mean Squared Error = " + testMSE)
  }
}