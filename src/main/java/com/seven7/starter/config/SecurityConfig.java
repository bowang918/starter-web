package com.seven7.starter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
	
	@Configuration
	@Profile("dev")
	public static class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService);
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http.headers().contentTypeOptions().xssProtection().cacheControl().httpStrictTransportSecurity().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
			.and().authorizeRequests().anyRequest().authenticated()
            .and().formLogin().and().httpBasic()
            .and().csrf().disable();
			//@formatter:on
		}
		
		@Override
		public void configure(WebSecurity builder) throws Exception {
			builder.ignoring().antMatchers("/resources/**", "/api*.js");
		}
		
	}

	@Configuration
	@Profile("!dev")
	public static class DefaultProdWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			RequestHeaderAuthenticationFilter siteMinderFilter = new RequestHeaderAuthenticationFilter();
	        siteMinderFilter.setAuthenticationManager(authenticationManager());
	        http.addFilter(siteMinderFilter).headers().contentTypeOptions().xssProtection().cacheControl().httpStrictTransportSecurity().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
			.and().authorizeRequests().anyRequest().authenticated()
            .and().csrf().disable();
		}
		
		@Override
		public void configure(WebSecurity builder) throws Exception {
			builder.ignoring().antMatchers("/resources/**", "/api*.js");
		}

		@Bean
		public PreAuthenticatedAuthenticationProvider authenticationProvider() {
			PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
			authenticationProvider.setPreAuthenticatedUserDetailsService(preAuthenticatedUserDetailsService());
			return authenticationProvider;
		}

		@Bean
		public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> preAuthenticatedUserDetailsService() {
			UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> uds = new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>();
			uds.setUserDetailsService(userDetailsService);
			return uds;
		}
	}
}
