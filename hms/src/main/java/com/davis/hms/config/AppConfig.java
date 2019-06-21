package com.davis.hms.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static org.hibernate.cfg.Environment.*;
import com.zaxxer.hikari.*;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@ComponentScans(value = { 
		@ComponentScan("com.davis.hms.dao"),
		@ComponentScan("com.davis.hms.service")
})
public class AppConfig {
	
	@Autowired
	private Environment env;

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		Properties props = new Properties();
				
		// Set Hibernate properties
		props.put(DIALECT, env.getProperty("spring.jpa.database-platform"));
		props.put(SHOW_SQL, env.getProperty("spring.jpa.properties.hibernate.show_sql"));
		props.put(FORMAT_SQL, env.getProperty("spring.jpa.properties.hibernate.format_sql"));
		props.put(HBM2DDL_AUTO, env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
		
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan("com.davis.hms.entity");
		factoryBean.setHibernateProperties(props);
		
		return factoryBean;	
	}
	
	@Bean
	public DataSource dataSource() {
		HikariConfig dataSourceConfig = new HikariConfig();
		
		dataSourceConfig.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
		dataSourceConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
		dataSourceConfig.setUsername(env.getRequiredProperty("spring.datasource.username"));
		dataSourceConfig.setPassword(env.getRequiredProperty("spring.datasource.password"));
		dataSourceConfig.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty("spring.datasource.hikari.maximum-pool-size")));
		dataSourceConfig.setConnectionTimeout(Long.parseLong(env.getRequiredProperty("spring.datasource.hikari.connection-timeout")));
		
		return new HikariDataSource(dataSourceConfig);
	}
	
	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		
		return transactionManager;
	}
	
	
	
	
	
}
