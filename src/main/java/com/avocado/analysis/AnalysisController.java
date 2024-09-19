package com.avocado.analysis;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AnalysisController implements InitializingBean {

  @Autowired
  private SparkSession sparkSession;

  private Dataset<Row> dataset;

  @GetMapping("")
  public String getData() throws IOException {

    List<Row> resultList = dataset.collectAsList();
    Gson gson = new Gson();
    String jsonResult = gson.toJson(resultList);

    return jsonResult;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    InputStream is = getClass().getClassLoader().getResourceAsStream("avocado.csv");
    // Create a temporary file to which we will copy the CSV
    Path tempFile = null;
    try {
      tempFile = Files.createTempFile("avocado", ".csv");
      Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Now we can use the path of the temporary file in Spark
    dataset = sparkSession.read()
        .option("header", "true") // Assume first row is header
        .option("inferSchema", "true") // Let Spark infer the schema
        .csv(tempFile.toString()); // Use path to temporary file

    dataset.createOrReplaceTempView("avocado_data");

    dataset = dataset.sparkSession().sql(
        "SELECT region, SUM(`Total Volume` * `AveragePrice`) AS total_sales "
            + "FROM avocado_data "
            + "GROUP BY region");

  }
}
