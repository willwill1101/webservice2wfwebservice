package com.ehl.tvc.config;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@org.springframework.context.annotation.Configuration
@ImportResource("classpath:config/spring.xml")
public class BeanConfig {
	private Logger log =  LogManager.getLogger(BeanConfig.class);

	@Autowired
	private PushConfig pushConfig;

	
	@Bean
	public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	@ConfigurationProperties(prefix = "dbconfig.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		Integer size =pushConfig.getConfig4KeyInt("threadPoolSize");
		threadPool.setMaxPoolSize(size);
		return threadPool;
	}

	@Bean
	Template template() {
		Template template = null;
		try {
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
			configuration.setDefaultEncoding("UTF-8");
			StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
			String path = pushConfig.getConfig4Key("template");
			String teleplatestr = IOUtils.toString(this.getClass()
					.getResourceAsStream(path));
			stringTemplateLoader.putTemplate("temp", teleplatestr);
			configuration.setTemplateLoader(stringTemplateLoader);
			 template = configuration.getTemplate("temp");
		} catch (Exception e) {
			log.error("获取freemark模板失败",e);
		}
		return template;
	}


}
