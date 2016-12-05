
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

object server {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("server").setMaster("local")
    val sc = new SparkContext(conf)
    val NBModel = NaiveBayesModel.load(sc, "models/classify/NB")
    val SVMmodel = SVMModel.load(sc, "models/classify/SVM")
    val RFModel = RandomForestModel.load(sc, "models/magnitude/RF")
    val LRModel = LogisticRegressionModel.load(sc, "models/magnitude/LR")
    
    var year = 1999
    var month = 1
    var date = 5
    var day = 2 
    var deptime = 1500
    var arrtime = 1600
    var carrier = 1 
    var flightno = 297
    var origin = 3
    var dest = 2
    
    val str = year+" "+month+" "+date+" "+day+" "+deptime+" "+arrtime+" "+carrier+" "+flightno+" "+origin+" "+dest
    val dv: Vector = Vectors.dense(str.split("\\s+").map(_.toDouble))
    
    val NBres = NBModel.predict(dv)
    val SVMres = SVMmodel.predict(dv)
    val RFres = RFModel.predict(dv)
    val LRres = LRModel.predict(dv)
    
    println(dv)
    println("NB: "+NBres)
    println("SVM: "+SVMres)
    println("RF: "+RFres)
    println("LR: "+LRres)
  }
}