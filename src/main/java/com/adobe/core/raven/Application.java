/*************************************************************************
 * ADOBE CONFIDENTIAL ___________________
 * <p/>
 * Copyright 2022 Adobe Systems Incorporated All Rights Reserved.
 * <p/>
 * NOTICE: All information contained herein is, and remains the property of Adobe Systems
 * Incorporated and its suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary to Adobe Systems Incorporated and its suppliers and are protected by all
 * applicable intellectual property laws, including trade secret and copyright laws. Dissemination
 * of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Adobe Systems Incorporated.
 **************************************************************************/

package com.adobe.core.raven;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.adobe.core.raven", "com.adobe.core.raven.resource.RavenApiResource"})
@ImportResource("classpath:batchjob.xml")
public class Application {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
