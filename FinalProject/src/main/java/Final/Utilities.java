package Final;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.theme.GGPlot2Theme;


import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Utilities {
	private static final String COMMA_DELIMITER = ",";
	/////////// String Methods ///////////////////////////////

	/* To Extract the Company Column as string to use it in companyCount */
	public static String extractCompany(String companyColumn ) {
		try {
			return companyColumn.split (COMMA_DELIMITER)[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}
	/* To Extract the Title Column as string to use it in titleCount */
	public static String extractTitle(String titleColumn) {
		try {
			return titleColumn.split (COMMA_DELIMITER)[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}
	/* To Extract the Location Column as string to use it in locationCount */
	public static String extractLocation(String locationColumn) {
		try {
			return locationColumn.split (COMMA_DELIMITER)[2];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}
	/* To Extract the Skills Column as string to use it in skillsCount */
	public static String extractSkills(String skillColumn) {
		try {
			return skillColumn.split (COMMA_DELIMITER)[7];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	/////////// Void Methods   ///////////////////////////////
	/* Removing Replicated Rows */
	public static JavaRDD<String> processDistinctRows(JavaRDD<String> jobs){
		jobs = jobs.distinct();
		return jobs ;
	}
	/* Print the Sorted Company and each Repeated */
	public static void companyCount(JavaRDD<String> jobs){
		// Extract the Company Column
		JavaRDD<String> company = jobs.map(Utilities::extractCompany).filter(StringUtils::isNotBlank);
		JavaRDD<String> words = company.flatMap(word -> Arrays.asList( word
				.toLowerCase()
				.trim()
				.split (",")).iterator ());
		// Count every word
		Map<String, Long> wordCounts = words.countByValue ();
		// map every word by its value
		List<Map.Entry> sorted = wordCounts.entrySet ().stream ()
				.sorted (Map.Entry.comparingByValue ()).collect (Collectors.toList ());
		Collections.reverse(sorted);
		 //print the map
		for (Map.Entry entry : sorted) {
			System.out.println (entry.getKey () + " : " + entry.getValue ());

		}
	}
	/* Print the Sorted Titles and each Repeated */
	public static void titleCount(JavaRDD<String> jobs){
		// Extract the Title Column
		JavaRDD<String> title = jobs.map(Utilities::extractTitle).filter(StringUtils::isNotBlank);
		JavaRDD<String> words = title.flatMap(word -> Arrays.asList( word
				.toLowerCase()
				.trim()
				.split (",")).iterator ());
		System.out.println(words.toString ());
		// Count every word
		Map<String, Long> wordCounts = words.countByValue ();
		// map every word by its value
		List<Map.Entry> sorted = wordCounts.entrySet ().stream ()
				.sorted (Map.Entry.comparingByValue ()).collect (Collectors.toList ());
		Collections.reverse(sorted);
		// print the map
		for (Map.Entry entry : sorted) {
			System.out.println (entry.getKey () + " : " + entry.getValue ());
		}

	}
	/* Print the Sorted Location and each Repeated */
	public static void locationCount(JavaRDD<String> jobs){
		// Extract the Location Column
		JavaRDD<String> location = jobs.map(Utilities::extractLocation).filter(StringUtils::isNotBlank);
		JavaRDD<String> words = location.flatMap(word -> Arrays.asList( word
				.toLowerCase()
				.trim()
				.split (",")).iterator ());
		// Count every word
		Map<String, Long> wordCounts = words.countByValue ();
		// map every word by its value
		List<Map.Entry> sorted = wordCounts.entrySet ().stream ()
				.sorted (Map.Entry.comparingByValue ()).collect (Collectors.toList ());
		Collections.reverse(sorted);
		// print the map
		for (Map.Entry entry : sorted) {
			System.out.println (entry.getKey () + " : " + entry.getValue ());
		}

	}
	/* Print the sorted Skills and each Repeated */
	public static void skillsCount(JavaRDD<String> jobs){
//		Extract the Skills Column
		JavaRDD<String> skills = jobs.map(Utilities::extractSkills).filter(StringUtils::isNotBlank);
		JavaRDD<String> words = skills.flatMap(word -> Arrays.asList( word
				.toLowerCase()
				.trim()
				.split ("\\|")).iterator ());
		List<String> myskills = words.collect();
		List<String> allSkills = new ArrayList<>();
		for (String skillSet:myskills) {
			allSkills.addAll(Arrays.asList(skillSet.split(",")));
		}

		// counting frequency
		Map<String,Integer> temp =new HashMap<>() ;
		for (String s: new HashSet<>(allSkills)) {
			temp.put(s, Collections.frequency(allSkills,s));
		}
		// Sort the map
		List<Map.Entry> sorted = temp.entrySet ().stream ()
				.sorted (Map.Entry.comparingByValue()).collect (Collectors.toList ());
		Collections.reverse(sorted);
		/////////// find the highest freq skill
		for (Map.Entry entry:sorted) {
			System.out.println(entry.getKey () + " : " + entry.getValue ());
			}
	}

	/////////// JavaRDD Methods //////////////////////////////
	/* To Extract the Company Column as JavaRDD to use it in Pie Chart */
	public static JavaRDD<String> extractCompanyColumn(JavaRDD<String> jobs){
		JavaRDD<String> company =jobs.map(Utilities::extractCompany);
		return company;
	}
	/* To Extract the Title Column as JavaRDD to use it in Bar Chart */
	public static JavaRDD<String> extractTitleColumn(JavaRDD<String> jobs){
		JavaRDD<String> title =jobs.map(Utilities::extractTitle);
		return title;
	}
	/* To Extract the Location Column as JavaRDD to use it in Bar Chart */
	public static JavaRDD<String> extractLocationColumn(JavaRDD<String> jobs){
		JavaRDD<String> location =jobs.map(Utilities::extractLocation).filter(StringUtils::isNotBlank);
		return location;
	}

	/////////// Chart Methods ////////////////////////////////
	/* To Draw a Bar Chart " Give it the Wanted Column as JavaRDD and its Title, Labels and Series Name "*/
	public static void columnBarChart(JavaRDD<String> column , String title ,String xLabel,String yLabel,String seriesName) throws IOException {
		/* Map the Column to its values */
		Map<String, Long> count = column.countByValue();
		/* Sort the map */
		List<Map.Entry<String, Long>> sortedMap = count.entrySet().stream().sorted(Comparator.comparing(Map.Entry<String,Long>::getValue).reversed()).limit(3359)
				.collect(Collectors.toList());
		/* Extract the Keys from the Sorted Map and Limit the First 100 */
		List<String> words = sortedMap.stream()
				.map(Map.Entry<String,Long>::getKey).limit(10)
				.collect(Collectors.toList());
		/* Extract the Values from the Sorted Map and Limit the First 100 */
		List<Long> values = sortedMap.stream()
				.map(Map.Entry<String,Long>::getValue).limit(10)
				.collect(Collectors.toList());
		/* Generate and Display the Bar Chart */
		CategoryChart chart = new CategoryChartBuilder().width(1024).height(1024).title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
		chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
		chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);
		chart.getStyler().setHasAnnotations(true);
		chart.getStyler().setStacked(true);
		chart.getStyler().setTheme(new GGPlot2Theme());
		chart.addSeries(seriesName, words, values);
		new SwingWrapper(chart).displayChart();
		BitmapEncoder.saveBitmap(chart,title,BitmapEncoder.BitmapFormat.PNG);
	}
	/* To Draw a Pie Chart for the Company Column */
	public static void companyPieChart(JavaRDD<String> jobs) throws IOException {
		/* Extract the Company Column as JavaRDD */
		JavaRDD<String> company = Utilities.extractCompanyColumn(jobs);
		/* Map te Column to its Values */
		Map<String, Long> map = company.countByValue();
		/* Generate and Display the Pie Chart to the most 7 Companies */
		PieChart chart = new PieChartBuilder().width(800).height(600).title("Companies Jobs").build();
		Color[] sliceColors = new Color[]{ new Color(0, 255, 0), new Color(0, 0, 255),new Color(255, 0, 0), new Color(255, 255, 0),
				 new Color(0, 255, 255), new Color(255, 0, 255), new Color(255, 111, 0),
				new Color(157, 255, 0, 255), new Color(125, 100, 250)};
		chart.getStyler().setHasAnnotations(true);
		chart.getStyler().setAnnotationDistance(1.22);
		chart.getStyler().setPlotContentSize(.7);
		chart.getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setSeriesColors(sliceColors);
		chart.addSeries("Confidential", map.get("Confidential"));
		chart.addSeries("Mishkat Nour", map.get("Mishkat Nour"));
		chart.addSeries("Expand Cart", map.get("Expand Cart"));
		chart.addSeries("EGIC", map.get("EGIC"));
		chart.addSeries("Aqarmap.com", map.get("Aqarmap.com"));
		chart.addSeries("Majorel Egypt", map.get("Majorel Egypt"));
		chart.addSeries("Ghassan Ahmed Alsulaiman for Electronic Services", map.get("Ghassan Ahmed Alsulaiman for Electronic Services"));
		chart.addSeries("Flairstech",map.get("Flairstech"));
		chart.addSeries("Profolio Consulting",map.get("Profolio Consulting"));
		new SwingWrapper(chart).displayChart();
		BitmapEncoder.saveBitmap(chart,"Companies Jobs",BitmapEncoder.BitmapFormat.PNG);
	}



}
