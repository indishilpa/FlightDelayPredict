package Classify;

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.util.MLUtils
import scala.io.Source
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.feature.PCA

object SVMtry {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SVMtry").setMaster("local")
    val sc = new SparkContext(conf)
    val df = sc.textFile(args(0))
    val parsedData = df.map { line =>
      val parts = line.toString().split(",")
      val label = parts(0).toString().split(' ')
      LabeledPoint(label(2).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }.cache()
    val Array(training, test) = parsedData.randomSplit(Array(0.85, 0.15))
    
    val model = SVMWithSGD.train(training, 200,1.0,0.1,1.0)
    val res = test.map(p => (model.predict(p.features), p.label))
    
    val TP = res.filter(x => x._1==1.0 && x._2==1.0).count() 
    val FP = res.filter(x => x._1==1.0 && x._2==0.0).count()
    val P  = res.filter(x => x._1==1.0).count() 
    
    val TN = res.filter(x => x._1==0.0 && x._2==0.0).count() 
    val FN = res.filter(x => x._1==0.0 && x._2==1.0).count()
    val N  = res.filter(x => x._1==0.0).count() 
   
    val accuracy = 1.0 * (TP+TN)/(P+N)
    val recall = 1.0 * TP/(TP+FN)
    val spec = 1.0 * TN/(FP+TN)
    
    println("TP: "+TP)
    println("FP: "+FP)
    println("P: "+P)
    println("TN: "+TN)
    println("FN: "+FN)
    println("N: "+N)
    
    println("Accuracy: "+accuracy)
    println("Recall: "+recall)
    println("Specificity: "+spec)
  }
}