package com.bluejob.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import com.bluejob.security.AuthoritiesConstants;
import com.bluejob.security.jwt.JwtAuthEntryPoint;
import com.bluejob.security.jwt.JwtAuthTokenFilter;
import com.bluejob.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CorsFilter corsFilter;
	@Autowired
	private SecurityProblemSupport problemSupport;
	 
	 
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//	private DriverManagerDataSource dataSource;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtAuthEntryPoint unauthorizedHandler;

	@Bean
	public JwtAuthTokenFilter authenticationJwtTokenFilter() {
		return new JwtAuthTokenFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
         .csrf()
         .disable()
//         .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
         .exceptionHandling()
         .authenticationEntryPoint(unauthorizedHandler)
         .accessDeniedHandler(problemSupport)
     .and()
         .headers()
         .frameOptions()
         .disable()
     .and()
         .sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
     .and()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/api/users*").hasAuthority("WEB_ADMIN")
				.antMatchers("/api/getuser","/api/login","/api/register","/api/candidateregister","/api/sendsms").permitAll()
	            .antMatchers("/api/activate","/api/authenticate","/api/account/reset-password/init","/api/account/reset-password/finish").permitAll()
	            .antMatchers("/api/**").authenticated()
	            .antMatchers("/management/**").hasAuthority("WEB_ADMIN")
//				.antMatchers("/core/address","/viewcart/**").hasAuthority("MANAGE_USERS")
				.anyRequest().authenticated()
//				.and()
//				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				;

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	}
//	@Bean
//	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
//		return new CustomBasicAuthenticationEntryPoint();
//	}
	
	

	@Override
	public void configure(WebSecurity web) throws Exception {
		log.info(" configure WebSecurity :");
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
//		web.expressionHandler(new DefaultWebSecurityExpressionHandler() {
//	        @Override
//	        protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
//	            WebSecurityExpressionRoot root = (WebSecurityExpressionRoot) super.createSecurityExpressionRoot(authentication, fi);
//	            root.setDefaultRolePrefix(""); //remove the prefix ROLE_
//	            return root;
//	        }
//	    })
//		.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
//		;
	}
}