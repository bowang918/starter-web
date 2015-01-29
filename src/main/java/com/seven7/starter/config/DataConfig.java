package com.seven7.starter.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages={"com.seven7.starter.config.repository"})
@EnableTransactionManagement
public class DataConfig {
	@Bean
    @Profile("dev")
    public DataSource devDataSource() {
		HikariConfig config = new HikariConfig();
//		config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:starter");
//		config.setUsername("starter");
//		config.setPassword("starter");
//		config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		return new HikariDataSource(config);
    }

    @Bean
    @Profile("!dev")
    public DataSource productionDataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/starter");
    }
    
    
    @Bean
    @Autowired 
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource datasource){
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(datasource);
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		emf.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		emf.setPackagesToScan("com.seven7.starter.config.entity");
		return emf;
	}
    
    @Bean
    @Autowired 
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) throws NamingException {
		return new JpaTransactionManager(emf.getObject());
	}
    
//    @Bean
//    @Autowired
//    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean emf) {
//    	EntityManagerFactory mf = emf.nativeEntityManagerFactory;
//    	return mf.createEntityManager();
//    }
}
