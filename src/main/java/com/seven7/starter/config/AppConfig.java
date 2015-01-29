package com.seven7.starter.config;

import javax.validation.Validator;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@ComponentScan(basePackages = {"com.seven7.starter"})
@EnableCaching
public class AppConfig {
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		return messageSource;
	}

	@Bean
	public Validator validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		return localValidatorFactoryBean;
	}
	
	@Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager();
    }

//	@Bean
//	public EhCacheManagerFactoryBean cacheManagerEh(){
//		EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
//		cacheManager.setConfigLocation(new ClassPathResource("ehcache.xml"));
//		return cacheManager;
//	}
}
