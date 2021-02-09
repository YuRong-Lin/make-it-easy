package demo

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.linalg.{DenseVector, Vectors}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

import scala.collection.mutable

/**
 * @Author LinYuRong
 * @Date 2021/2/9 10:01
 * @Version 1.0
 */
object DataFrame01 {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("DataFrame01").setLevel(Level.ERROR)

    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("DataFrame01")
      .set("spark.submit.deployMode", "client")

    val spark = SparkSession.builder().config(conf).getOrCreate()

    val df = spark.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 2.0, -1), Array(Vectors.dense(1))),
      (1, Vectors.dense(3.0, 4.0, -2), Array(Vectors.dense(2), Vectors.dense(3)))
    ))

    df.printSchema()
    df.show(1, truncate = false)

    val map2rdd = df.rdd.map(r => {
      Row(r.getAs[Int](0) + ":" + r.getAs[DenseVector](1).values.mkString(" ") + ":" +
        r.getAs[mutable.WrappedArray[DenseVector]](2).map(o => o.values.mkString(" ")).mkString(" "))
    })

    val schemaString = "emb_lsh"
    val fields = Array(StructField(schemaString, StringType, nullable = true))
    val schema = StructType(fields)

    val res = spark.createDataFrame(map2rdd, schema)
    res.show(1, truncate = false)

    res.write.csv("./temp")
  }
}
