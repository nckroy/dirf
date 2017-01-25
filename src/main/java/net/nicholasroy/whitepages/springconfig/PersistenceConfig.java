package net.nicholasroy.whitepages.springconfig;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import net.nicholasroy.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//Set up outer transactionawaredatasourceproxy, talking to a lazyconnectiondatasourceproxy, talking to connection pooling using bonecp, talking to postgresql database
@Configuration
@EnableJpaRepositories(basePackages = "net.nicholasroy.repository", includeFilters = @ComponentScan.Filter(value = { UserRepository.class }, type = FilterType.ASSIGNABLE_TYPE))
@EnableTransactionManagement
public class PersistenceConfig {
	
	@Bean
	DataSource dataSource() throws SQLException {
		TransactionAwareDataSourceProxy bean = new TransactionAwareDataSourceProxy(lazyDataSource());
		return bean;
	}
	
	@Bean
	DataSource lazyDataSource() throws SQLException {
		LazyConnectionDataSourceProxy bean = new LazyConnectionDataSourceProxy(rootDs());
		return bean;
	}
	
	@Bean
	DataSource rootDs() throws SQLException {
		
		SimpleDriverDataSource bean = new SimpleDriverDataSource(new org.postgresql.Driver(), "jdbc:postgresql://localhost/test");
//TODO: Figure out why BoneCP isn't working
//		BoneCPDataSource bean = new BoneCPDataSource();
//		bean.setDriverClass("org.postgresql.Driver");
//		bean.setJdbcUrl("jdbc:postgresql://localhost/test");
//		bean.setUsername("");
//		bean.setPassword("");
//		bean.setIdleConnectionTestPeriodInMinutes(240L);
//		bean.setIdleMaxAgeInMinutes(60L);
//		bean.setMaxConnectionsPerPartition(60);
//		bean.setMinConnectionsPerPartition(20);
//		bean.setPartitionCount(3);
//		bean.setAcquireIncrement(10);
//		bean.setStatementsCacheSize(50);

		return bean;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() throws SQLException {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("net.nicholasroy.entity");
		factory.setPersistenceUnitName("persistenceUnit");
		factory.setPersistenceXmlLocation("classpath*:persistence.xml");
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws SQLException {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

}