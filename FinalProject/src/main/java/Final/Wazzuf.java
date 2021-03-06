package Final;

import org.apache.spark.SparkConf;


import java.io.IOException;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


public class Wazzuf {

	public static void main(String[] args) throws IOException {
		Logger.getLogger("org").setLevel(Level.ERROR);
		// CREATE SPARK CONTEXT
		SparkConf conf = new SparkConf().setAppName("WAZZUF").setMaster("local[3]");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		// LOAD DATASET
		JavaRDD<String> jobs = sparkContext.textFile("src/main/resources/Wuzzuf_Jobs1.csv");
//		/* 3- Cleaning the data */
		Utilities.processDistinctRows(jobs);
		/* 4- Count the Job of each Company  */
		System.out.println("Count the Job of each Company");
		System.out.println("------------------------------------------------------------------------------------");
		 Utilities.companyCount(jobs);
		System.in.read();
		/* 5- Show Company Column in Pie Chart */
		Utilities.companyPieChart(jobs);
		System.in.read();
		/* 6- the Most Popular Job Title */
		System.out.println("The Most Popular Job Title");
		System.out.println("------------------------------------------------------------------------------------");
		Utilities.titleCount(jobs);
		System.in.read();
		/* 7- Show Title Column in Bar Chart */
		JavaRDD<String> title = Utilities.extractTitleColumn(jobs);
		Utilities.columnBarChart(title,"Jobs Title","Title","Count","Company Title");
		System.in.read();
		/* 8- the Most Popular Areas */
		System.out.println("The Most Popular Areas");
		System.out.println("------------------------------------------------------------------------------------");
		Utilities.locationCount(jobs);
		System.in.read();
		/* 9- Show Location Column in Bar Chart */
		JavaRDD<String> location = Utilities.extractLocationColumn(jobs);
		Utilities.columnBarChart(location,"Jobs Location","Location","Count","Company Location");
		System.in.read();
		/* 10- the Most Important Skills Required   */
		System.out.println("The Most Important Skills Required");
		System.out.println("------------------------------------------------------------------------------------");
		Utilities.skillsCount(jobs);




	}


}
