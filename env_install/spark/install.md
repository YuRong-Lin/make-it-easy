# Spark 安装

## windows安装

1、Maven方式

		<dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_${scala.version}</artifactId>
            <version>${spark.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.spark/spark-sql -->
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-sql_${scala.version}</artifactId>
            <version>${spark.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.spark/spark-mllib -->
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-mllib_${scala.version}</artifactId>
            <version>${spark.version}</version>
        </dependency>
		
	在windows环境下启动spark，会报错：Could not locate executable null\bin\winutils.exe in the Hadoop binaries
	
	解决：需要安装hadoop在windows下的支持插件：
	
		下载资源：
		1）http://archive.apache.org/dist/hadoop/core/  找对应版本
		2）https://github.com/cdarlint/winutils
		
	在Path下配置好对应的bin路径即可。（重启了电脑才生效）
	
## yarn完全分布式模式
见[SparrowRecSysZero2One项目环境搭建](https://github.com/YuRong-Lin/SparrowRecSysZero2One)
