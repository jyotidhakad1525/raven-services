package com.adobe.core.raven.config;

import com.adobe.core.raven.dto.EmailComponent;
import com.adobe.core.raven.service.implementation.*;
import com.adobe.core.raven.service.interfaces.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class SpringBeans {


	@Bean
	public MsgParserService msgParserService() {
		return new MsgParserServiceImpl();
	}

	@Bean
	public QAService qaService() {
		return new QAServiceImpl();
	}

	@Bean
	public URLService urlService() {
		return new URLServiceImpl();
	}

	@Bean
	public ValidateService validateService() {
		return new ValidateServiceImpl();
	}

	@Bean
	public WorkfrontService workfrontService() {
		return new WorkfrontServiceImpl();
	}

	@Bean
	public CgenService cgenService() {
		return new CgenServiceImpl();
	}

	@Bean
	public EmailComponent emailComponent() {
		return new EmailComponent();
	}

	@Bean
	public JobExecutorService jobExecutorService(){return  new JobExecutorServiceImpl();}

	@Bean
	public DataService dataService(){return  new DataServiceImpl();}

	@Bean
	public PollingService pollingService(){return  new PollingServiceImpl();}

	@Bean
	public OutputMapperService outputMapperService(){return  new OutputMapperServiceImpl();}

	/*@Bean
	TaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
		t.setCorePoolSize(12);
		t.setMaxPoolSize(6);
		t.setQueueCapacity(6);
		t.setAllowCoreThreadTimeOut(true);
		t.setKeepAliveSeconds(120);
		return t;
	}*/




}
