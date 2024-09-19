package com.avocado.analysis;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spark.executor.memory}")
    private String executorMemory;

    @Value("${spark.executor.cores}")
    private String executorCores;

    @Value("${spark.driver.memory}")
    private String driverMemory;

    @Value("${spark.default.parallelism}")
    private String defaultParallelism;

    @Value("${spark.shuffle.service.enabled}")
    private String shuffleServiceEnabled;

    @Value("${spark.shuffle.compress}")
    private String shuffleCompress;

    @Value("${spark.shuffle.file.buffer}")
    private String shuffleFileBuffer;

    @Bean
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.ui.enabled", "false");
        sparkConf.set("spark.executor.memory", executorMemory);
        sparkConf.set("spark.executor.cores", executorCores);
        sparkConf.set("spark.driver.memory", driverMemory);
        sparkConf.set("spark.default.parallelism", defaultParallelism);
        sparkConf.set("spark.shuffle.service.enabled", shuffleServiceEnabled);
        sparkConf.set("spark.shuffle.compress", shuffleCompress);
        sparkConf.set("spark.shuffle.file.buffer", shuffleFileBuffer);

        return sparkConf;
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName("avocado analysis")
                .master("local[*]")
                .config(sparkConf())
                .getOrCreate();
    }
}
